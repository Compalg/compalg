package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Utilitario.LanguageException;
import java.util.StringTokenizer;
import java.util.Vector;

public class ExpandEscolhe {
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

    public static NodeInstruction ExpandSWITCH(NodeInstruction switchNode, int level, Vector memory)
            throws LanguageException {
        //-----------------------------------------------------ESCOLHA --------
        String line = switchNode.GetText().trim();
        String ESCOLHE = Keyword.GetTextKey(Keyword.ESCOLHA);
        //palavra ESCOLHA
        String escolhe = line.substring(0, ESCOLHE.length());
        // expressao de controle
        String expressaoEscolhe = line.substring(ESCOLHE.length()).trim();

        if (!escolhe.equalsIgnoreCase(ESCOLHE)) {
            throw new LanguageException(
                    switchNode.GetCharNum(), switchNode.GetText(),
                    " Esperava o comando " + ESCOLHE,
                    " VERIFICAR A SINTAXE DA ESTRUTURA DE CONTROLO");
        }

        try {
            if (!Expressao.IsExpression(expressaoEscolhe, memory)) {
                throw new LanguageException(
                        switchNode.GetCharNum(), switchNode.GetText(),
                        expressaoEscolhe + " NÃO É UMA VARIÁVEL NEM EXPRESSÃO ",
                        " VERIFIQUE A EXPRESSÃO " + ESCOLHE);
            }
        } catch (Exception e) {
            if (e instanceof LanguageException) {
                if (((LanguageException)e).line > 0 && !((LanguageException)e).codeLine.isEmpty()) {
                    throw e;
                }
                throw new LanguageException(
                        switchNode.GetCharNum(), switchNode.GetText(),
                        ((LanguageException)e).error, ((LanguageException)e).solution);
            } else {
                throw new LanguageException(
                        switchNode.GetCharNum(),
                        switchNode.GetText(),
                        expressaoEscolhe + " NÃO É UMA VARIÁVEL NEM EXPRESSÃO ",
                        " VERIFIQUE A EXPRESSÃO " + ESCOLHE);
            }
        }
        //procurar o ultimo no do escolhe
        NodeInstruction end = switchNode.GetNext();
        if (end.GetType() == Keyword.FIMESCOLHE) {
            throw new LanguageException(
                    switchNode.GetCharNum(),
                    switchNode.GetText(),
                    " A INSTRUÇÃO ESCOLHA TEM DE TER CASOS E/OU DEFEITO",
                    " ACrecente Casos na instrução");

        }
        while (end != null && end.GetType() != Keyword.FIMESCOLHE) {
            end = end.GetNext();
        }
        //se nao houver fim do ciclo
        if (end == null) {
            throw new LanguageException(
                    switchNode.GetCharNum(), switchNode.GetText(),
                    " Não existe, FIM ESCOLHA que fecha esta estrutura ",
                    " Escreva um FIM ESCOLHA depois deste BLOCO");
        }
        //----------------------------------- CASOS --------------------------
        NodeInstruction tmp2, tmp1, oldDecision = null, nodeAnt = null;
        tmp1 = switchNode.GetNext();
        //fazer todos os casos
        while (tmp1.GetType() != Keyword.FIMESCOLHE && tmp1.GetType() != Keyword.DEFEITO) {
            // se for um caso expandir
            if (tmp1.GetType() == Keyword.CASO) {
                //---------------------------------------Ligar o ultimo ao END
                if (nodeAnt != null) //se nao fo o primeiro caso
                {
                    // anterior aponta para fim
                    nodeAnt.SetNext(end);
                    //se o falso apontar par o no TMP1, passa a apontar para fim
                    if (nodeAnt.GetIfFalse() != null && nodeAnt.GetIfFalse() == tmp1) {
                        nodeAnt.SetIfFalse(end);
                    }
                    //se o verdadeiro apontar par o no TMP1, passa a apontar para fim
                    if (nodeAnt.GetIfTrue() != null && nodeAnt.GetIfTrue() == tmp1) {
                        nodeAnt.SetIfTrue(end);
                    }

                }
                String tmp1_txt = tmp1.GetText();
                tmp2 = ExpandCase(tmp1, level, memory, expressaoEscolhe, end);
                if (tmp2.GetType() == Keyword.CASO || tmp2.GetType() == Keyword.DEFEITO) {
                    throw new LanguageException(
                            tmp1.GetCharNum(), tmp1_txt,
                            " O " + tmp1_txt + " não tem instruções ",
                            " Acrecente instruções no CASO");

                }
                //actualizar o ponteiro da condicao anterior
                if (oldDecision == null) {
                    switchNode.SetNext(tmp1);
                } else {
                    oldDecision.SetIfFalse(tmp1);
                }
                //actualizar a condicao anterior
                oldDecision = tmp1;
                // colocar o tmp1 no next
                tmp1 = tmp2;
            } //senao actualizar o nivel
            else {
                tmp1.IncrementLevel();
            }
            nodeAnt = tmp1;
            //passar para a frente
            tmp1 = tmp1.GetNext();
        }
        if (oldDecision == null) {
            throw new LanguageException(
                    switchNode.GetCharNum(), switchNode.GetText(),
                    " Não existe, CASO nesta estrutura ",
                    " Escreva um CASO depois do Escolha");
        }

        //--------------------------- actualizar os ponteiro do ultimo caso        
        // anterior aponta para fim
        nodeAnt.SetNext(end);
        //se o falso apontar par o no TMP1, passa a apontar para fim
        if (nodeAnt.GetIfFalse() != null && nodeAnt.GetIfFalse() == tmp1) {
            nodeAnt.SetIfFalse(end);
        }
        //se o verdadeiro apontar par o no TMP1, passa a apontar para fim
        if (nodeAnt.GetIfTrue() != null && nodeAnt.GetIfTrue() == tmp1) {
            nodeAnt.SetIfTrue(end);
        }


        //------------------------------------------------------Fazer o default
        if (tmp1.GetType() == Keyword.DEFEITO) {
            NodeInstruction tmpd = tmp1;
            tmp1 = ExpandDefaultCase(tmp1, level, memory, expressaoEscolhe, end);
            if (tmp1.GetType() == Keyword.FIMESCOLHE) {
                throw new LanguageException(
                        tmpd.GetCharNum(), tmpd.GetText(),
                        " O DEFEITO não tem instruções ",
                        " Acrecente instruções no CASO por DEFEITO");

            }
            oldDecision.SetIfFalse(tmp1);
            while (tmp1.GetType() != Keyword.FIMESCOLHE) {
                tmp1.IncrementLevel();
                tmp1 = tmp1.GetNext();
            }
        }
        //fazer um no de juncao
        tmp1.SetType(Keyword.CONECTOR);
        //fim do escolhe
        return tmp1;
    }
    //-------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------

    private static NodeInstruction ExpandDefaultCase(NodeInstruction defaultNode, int level, Vector memory,
            String expressaoEscolhe, NodeInstruction end) throws LanguageException {
        String line = defaultNode.GetText().trim();
        //defeito tem 7 letras
        String strCaso = line.substring(0, 7);
        if (!strCaso.equalsIgnoreCase("DEFEITO")) {
            throw new LanguageException(
                    defaultNode.GetCharNum(), defaultNode.GetText(),
                    " Esperava um, DEFEITO : intruções",
                    " ???? ");
        }
        int indexPontos = line.indexOf(':');
        if (indexPontos < 0) {
            throw new LanguageException(
                    defaultNode.GetCharNum(), defaultNode.GetText(),
                    " Esperava um DEFEITO  \":\" intruções",
                    " Coloque os dois pontos : ao DEFEITO");
        }
        //------------------------------------------ FAZER A PRIMEIRA INSTRUCAO
        //import buscar a intruçao
        String instrucao = line.substring(indexPontos + 1).trim();
        // no da intruçao
        NodeInstruction nodeInstr;
        //fazer um novo no
        if (instrucao.length() > 0) {
            nodeInstr = new NodeInstruction(instrucao, defaultNode.GetCharNum(), level + 1);
        } else {
            nodeInstr = defaultNode.GetNext();
        }
        return nodeInstr;
    }
    //-------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------

    private static NodeInstruction ExpandCase(NodeInstruction caseNode, int level, Vector memory,
            String expressaoEscolhe, NodeInstruction end) throws LanguageException {
        String line = caseNode.GetText().trim();

        //caso tem 4 letras
        String strCaso = line.substring(0, 4);
        if (!strCaso.equalsIgnoreCase("CASO")) {
            throw new LanguageException(
                    caseNode.GetCharNum(), caseNode.GetText(),
                    " Esperava um CASO [expressao] : intrucoes",
                    " Depois do comando ESCOLHA vem um CASO ");
        }
        int indexPontos = line.indexOf(':');
        if (indexPontos < 0) {
            throw new LanguageException(
                    caseNode.GetCharNum(), caseNode.GetText(),
                    " Esperava um CASO [expressao] \":\" intrucoes",
                    " Coloque os dois pontos : depois da expressao");
        }
        //----------------------------------------------------------------
        //-------    condicoes multiplas no CASO exp, exp, exp : --------
        //----------------------------------------------------------------
        // expressao da condicao (multipla)
        StringBuffer condicaoCaso = new StringBuffer();
        // expressao simples do caso
        String expressaoCaso = "";
        // expressao do caso
        String todasCaso = line.substring(4, indexPontos).trim();
        //as condicoes no caso estao separadas por virgulas
        StringTokenizer tok = new StringTokenizer(todasCaso, ",");
        while (tok.hasMoreElements()) {
            expressaoCaso = tok.nextToken().trim();

            //verificar se nao uma variavel ou expressao            
            try {
                if (!Expressao.IsExpression(expressaoCaso, memory)) {
                    throw new LanguageException(
                            caseNode.GetCharNum(), caseNode.GetText(),
                            expressaoCaso + " NÃO É UMA VARIÁVEL NEM EXPRESSÃO ",
                            " VERIFIQUE A EXPRESSÃO DO <ESCOLHA>");
                }
            } catch (Exception e) {
                if (e instanceof LanguageException) {
                if (((LanguageException)e).line > 0 && !((LanguageException)e).codeLine.isEmpty()) {
                    throw e;
                }
                throw new LanguageException(
                        caseNode.GetCharNum(), caseNode.GetText(),
                        ((LanguageException)e).error, ((LanguageException)e).solution);
                } else {
                    throw new LanguageException(
                            caseNode.GetCharNum(), caseNode.GetText(),
                            expressaoCaso + " NÃO É UMA VARIÁVEL NEM EXPRESSÃO ",
                            " VERIFIQUE A EXPRESSÃO DO <ESCOLHA>");
                }
            }
            //adicionar uma nova condicao
            condicaoCaso.append(expressaoEscolhe + " = " + expressaoCaso);
            // adicionar a condicao OU caso haja mais elementos
            if (tok.hasMoreElements()) {
                condicaoCaso.append(" OU ");
            }
        }
        //----------------------------------------------------------------
        //--------------- FAZER A PRIMEIRA INSTRUCAO ------------------------
        //----------------------------------------------------------------
        //import buscar a intrucao
        String instrucao = line.substring(indexPontos + 1).trim();
        // no da intrucao
        NodeInstruction nodeInstr;
        //fazer um novo no
        if (instrucao.length() > 0) {
            nodeInstr = new NodeInstruction(instrucao, caseNode.GetCharNum(), level + 1);
            nodeInstr.SetNext(caseNode.GetNext());
        } else {
            nodeInstr = caseNode.GetNext();
            nodeInstr.IncrementLevel();
        }

        //-------------------------------------------------------ALTERAR O CASO
        //alterar o no para ser um IF
        caseNode.SetLevel(level);
        // retirar o CASO e o :
        caseNode.SetText(condicaoCaso.toString());
        // defenir o tipo
        caseNode.SetType(Keyword.SE);
        // actualizar os ponteiros next e false  para end;
        caseNode.SetNext(end);
        caseNode.SetIfFalse(end);
        //ligar o verdadeiro a intrucao
        caseNode.SetIfTrue(nodeInstr);
        // retorna um o com a intrucao seguinte
        return nodeInstr;
    }
}
