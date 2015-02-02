package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Operador;
import Portugol.Language.Analisador.ParteDeExpresion;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.TipoClasse;
import Portugol.Language.Analisador.TipoRegisto;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.IteratorExpression;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
//import Portugol.Language.Criar.Subrutine;
import java.util.Stack;
import java.util.Vector;

public class ExpandFluxogram {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";

    public static void ExpandSubrutine(BloqueSubrutine rutina) throws LanguageException {

        NodeInstruction pt = rutina.getStartNode();
        Vector memory = new Vector();//rutina.memory;
        Stack stack = new Stack();

        while (pt != null) {
            //fazer as variaveis            
            if (pt.GetType() == Keyword.DEFINIR) {
                //si existe una variable de registro en definicion entonces todas las variables
                //van como campos de este
                pt = ExpandDefinirSimbol.ExpandVariable(pt, stack.size(), memory);
            } else if (pt.GetType() == Keyword.CALCULAR) {
                VerifyCalculate(pt, memory);
            } //fazer a leitura das  variaveis
            else if (pt.GetType() == Keyword.RETORNE) {
                VerifyRetorno(pt, memory);
            } //fazer a leitura das  variaveis
            else if (pt.GetType() == Keyword.LEIA) {
                pt = ExpandLer.ExpandRead(pt, stack.size(), memory);
            } //verificar a escrita
            else if (pt.GetType() == Keyword.ESCREVA) {
                VerifyWRITE(pt, memory);
            } // nos dos blocos vao para a pilha
            else if (pt.GetType() == Keyword.SE
                    || pt.GetType() == Keyword.PARA
                    || pt.GetType() == Keyword.ENQUANTO
                    || pt.GetType() == Keyword.FAZ
                    || pt.GetType() == Keyword.ESCOLHA
                    || pt.GetType() == Keyword.REPETE) {
                stack.push(pt);
            } //limpar variaveis detro do escolhe
            // o senao esta ao mesmo nivel das variaveis
            else if (pt.GetType() == Keyword.SENAO) {
                if (stack.isEmpty()) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O < SENAO > NÃO PODE EXISTIR SEM O < SE > ",
                            "POR FAVOR COLOQUE O < SE > ANTES DO < SENAO >");
                }

                // obter o IF da pilha
                NodeInstruction n = (NodeInstruction) stack.peek();
                //ERRO
                if (n.GetType() != Keyword.SE) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "A ESTRUTURA :\"" + n.GetText() + "\" ESTÁ ABERTA",
                            " ESCREVA A INSTRUÇÃO FIM" + n.GetTextKey() + " E NÃO \"" + pt.GetText() + "\"");
                }

                cleanMemory(stack.size(), memory);
            } else if (pt.GetType() == Keyword.CASO || pt.GetType() == Keyword.DEFEITO) {
                cleanMemory(stack.size(), memory);
            } // --------------------- Fazer os IFS ----------------------------
            else if (pt.GetType() == Keyword.FIMSE) {
                if (stack.isEmpty()) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O < FIMSE > NÃO PODE EXISTIR SEM O < SE > ",
                            "POR FAVOR COLOQUE O < SE > ANTES DO < FIMSE >");
                }
                // obter o IF da pilha
                NodeInstruction n = (NodeInstruction) stack.pop();
                //ERRO
                if (n.GetType() != Keyword.SE) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "A ESTRUTURA :\"" + n.GetText() + "\" ESTÁ ABERTA",
                            " ESCREVA A INSTRUÇÃO FIM" + n.GetTextKey() + " E NÃO \"" + pt.GetText() + "\"");
                }

                cleanMemory(stack.size() + 1, memory);
                ExpandSe.ExpandIF(n, stack.size(), memory);
            } // --------------------- Fazer os WHILE ----------------------------
            else if (pt.GetType() == Keyword.FIMENQUANTO) {
                if (stack.isEmpty()) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O < FIMENQUANTO > NÃO PODE EXISTIR SEM O < ENQUANTO > ",
                            "POR FAVOR COLOQUE O < ENQUANTO > ANTES DO < FIMENQUANTO >");
                }

                NodeInstruction n = (NodeInstruction) stack.pop();
                if (n.GetType() != Keyword.ENQUANTO) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O CICLO :\"" + n.GetText() + "\" ESTÁ ABERTO",
                            " ESCREVA A INSTRUÇÃO QUE INDICA O FIM DO CICLO E NÃO \"" + pt.GetText() + "\"");
                }

                ExpandEnquanto.ExpandWHILE(n, stack.size(), memory);

            } //------------------------- fazer os FAZ ENQUANTO-------------------------
            else if (pt.GetType() == Keyword.FAZENQUANTO) {
                if (stack.isEmpty()) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O < ENQUANTO > NÃO PODE EXISTIR SEM O < FAZ > ",
                            "POR FAVOR COLOQUE O < FAZ > ANTES DO < ENQUANTO >");
                }

                //-------------------- obter o FAZ da pilha  --------------
                NodeInstruction n = (NodeInstruction) stack.pop();
                if (n.GetType() != Keyword.FAZ) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O CICLO :\"" + n.GetText() + "\" ESTÁ ABERTO",
                            " ESCREVA A INSTRUÇÃO QUE INDICA O FIM DO CICLO E NÃO \"" + pt.GetText() + "\"");
                }

                ExpandFazEnquanto.ExpandDoWhile(n, pt, stack.size(), memory);
                cleanMemory(stack.size() + 1, memory);
            } //------------------------- fazer os REPETE -------------------------
            else if (pt.GetType() == Keyword.ATE) {
                if (stack.isEmpty()) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O < ATE > NÃO PODE EXISTIR SEM O < REPITA > ",
                            "POR FAVOR COLOQUE O < REPITA > ANTES DO < ATE >");
                }
                //-------------------- obter o Repete da pilha  --------------
                NodeInstruction n = (NodeInstruction) stack.pop();
                if (n.GetType() != Keyword.REPETE) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O CICLO :\"" + n.GetText() + "\" ESTÁ ABERTO",
                            " ESCREVA A INSTRUÇÃO QUE INDICA O FIM DO CICLO E NÃO \"" + pt.GetText() + "\"");
                }

                ExpandRepeteAte.ExpandRepeat(n, pt, stack.size(), memory);
                cleanMemory(stack.size() + 1, memory);
            } //--------------------------  Fazer os FOR -----------------------
            else if (pt.GetType() == Keyword.FIMPARA) {
                if (stack.isEmpty()) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O < FIMPARA > NÃO PODE EXISTIR SEM O < PARA > ",
                            "POR FAVOR COLOQUE O < PARA > ANTES DO < FIMPARA >");
                }

                //-------Obter o PARA da pilha ---------
                NodeInstruction n = (NodeInstruction) stack.pop();
                if (n.GetType() != Keyword.PARA) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O CICLO :\"" + n.GetText() + "\" ESTÁ ABERTO",
                            " ESCREVA A INSTRUÇÃO QUE INDICA O FIM DO CICLO E NÃO \"" + pt.GetText() + "\"");
                }

                ExpandPara.ExpandFOR(n, stack.size(), memory);
                cleanMemory(stack.size() + 1, memory);
            } // --------------------- Criar os CASOS ------------------------
            else if (pt.GetType() == Keyword.FIMESCOLHE) {
                if (stack.isEmpty()) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O < FIM ESCOLHE > NÃO PODE EXISTIR SEM O < ESCOLHA > ",
                            "POR FAVOR COLOQUE O < ESCOLHA > ANTES DO < FIMESCOLHA >");
                }
                // obter o ESCOLHA da pilha
                NodeInstruction n = (NodeInstruction) stack.pop();
                //ERRO
                if (n.GetType() != Keyword.ESCOLHA) {
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            "O CICLO :\"" + n.GetText() + "\" ESTÁ ABERTO",
                            " ESCREVA A INSTRUÇÃO QUE INDICA O FIM DO CICLO E NÃO \"" + pt.GetText() + "\"");
                }
                //FAZER O ESCOLHA                
                pt = ExpandEscolhe.ExpandSWITCH(n, stack.size(), memory);
                cleanMemory(stack.size() + 1, memory);
            } else if (pt.GetType() == Keyword.PROCEDIMENTO || pt.GetType() == Keyword.FUNCAO || pt.GetType() == Keyword.CONSTRUTOR) {
                ExpandProcedimento.ExpandSUBRUTINA(rutina, pt, stack.size(), memory);
            } else if (pt.GetType() == Keyword.CHAMADOPROCEDIMENTO) {
                try {
                    ExpandChamadoProcedimento.ExpandCHAMADO(rutina, pt, stack.size(), memory);
                } catch (LanguageException e) {
                    if (e.line > 0 && !e.codeLine.isEmpty()) {
                        throw e;
                    }
                    throw new LanguageException(
                            pt.GetCharNum(), pt.GetText(),
                            e.error, e.solution);
                }
            } else if (pt.GetType() == Keyword.CHAMADOPROCEDIMENTO) {
                ExpandChamadoProcedimento.ExpandCHAMADO(rutina, pt, stack.size(), memory);
            } else if (pt.GetType() == Keyword.DESCONHECIDO) {
                throw new LanguageException(
                        pt.GetCharNum(), pt.GetText(),
                        "INSTRUÇÃO DESCONHECIDA: " + pt.GetText(),
                        "VERIFIQUE A INSTRUÇÃO");
            }

            pt = pt.GetNext();

        }// pt != null        

        //  - - -- ciclos nao fechados  . . . .
        //David: troque el mensaje para que sea acorde con todas las estructuras, antes solo falaba ciclo, agora fala estructura 
        if (!stack.isEmpty()) {
            NodeInstruction n = (NodeInstruction) stack.pop();
            throw new LanguageException(
                    n.GetCharNum(), n.GetText(),
                    //"O CICLO NÃO ESTÁ FECHADO",
                    "A ESTRUTURA " + Keyword.GetTextKey(n.GetType()) + " NÃO ESTÁ FECHADA.",
                    "POR FAVOR, FECHE A ESTRUTURA COM FIM" + Keyword.GetTextKey(n.GetType()));
        }
    }

    //David: NOVO
    public static void ExpandRegisto(BloqueRegisto rutina) throws LanguageException {
        NodeInstruction pt = rutina.getStartNode();

        TipoRegisto tipoRegisto;
        tipoRegisto = new TipoRegisto();
        while (pt != null) {
            //fazer as variaveis            
            if (pt.GetType() == Keyword.DEFINIR) {
                pt = ExpandDefinirSimbol.ExpandVariable(pt, 0, tipoRegisto.Defs);
            } else if (pt.GetType() == Keyword.REGISTO) {
                String name = pt.text.toUpperCase().replace("REGISTO ", "").trim();
                tipoRegisto.Name = name;
                rutina.Nome = name;
                rutina.type = Bloque.REGISTO;
                for (int i = 0; i < Intermediario.tiposRegistos.size(); i++) {
                    TipoRegisto tmp = (TipoRegisto) Intermediario.tiposRegistos.get(i);
                    if (tmp.Name.equals(name)) {
                        throw new LanguageException(
                                "Já existe um registo com esse nome",
                                "Mude o nome do registo"); //David: Revisar ortografia                        
                    }
                }
            } else if (pt.GetType() == Keyword.FIMREGISTO) {
                Intermediario.tiposRegistos.add(tipoRegisto);
            } else {
                throw new LanguageException(
                        "Uma declaração de tipo de dado registo só pode ter declaraçoes de variaveis",
                        "Retire esta instrucção"); //David: Revisar ortografia
            }

            pt = pt.GetNext();
        }
    }

    //David: NOVO
    public static void ExpandClasse(BloqueClasse rutina) throws LanguageException {
        NodeInstruction pt = rutina.getStartNode();

        TipoClasse tipoClasse;
        tipoClasse = new TipoClasse(rutina);
        while (pt != null) {
            //fazer as variaveis            
            if (pt.GetType() == Keyword.DEFINIR) {
                pt = ExpandDefinirSimbol.ExpandVariable(pt, 0, tipoClasse.Defs);
            } else if (pt.GetType() == Keyword.CLASSE) {
                String name = pt.text.toUpperCase().replace("CLASSE ", "").trim();
                tipoClasse.Name = name;
                rutina.Nome = name;
                rutina.type = Bloque.CLASSE;
                for (int i = 0; i < Intermediario.tiposClasses.size(); i++) {
                    TipoClasse tmp = Intermediario.tiposClasses.get(i);
                    if (tmp.Name.equals(name)) {
                        throw new LanguageException(
                                "Já existe uma classe com esse nome",
                                "Mude o nome da classe"); //David: Revisar ortografia                        
                    }
                }
            } else if (pt.GetType() == Keyword.FIMCLASSE) {
                Intermediario.tiposClasses.add(tipoClasse);
            } else {
                throw new LanguageException(
                        "Uma declaração de tipo de dado registo só pode ter declaraçoes de variaveis",
                        "Tire esta instrucção"); //David: Revisar ortografia
            }

            pt = pt.GetNext();
        }
        for (int i = 0; i < rutina.metodos.size(); i++) {
            ExpandProcedimento.ExpandSUBRUTINA(rutina.metodos.get(i), rutina.metodos.get(i).getStartNode(), 0, null);
            rutina.metodos.get(i).getStartNode().Expanded = true;
        }

        BloqueClasse ClaseAnterior = BloqueClasse.ClaseActualParaExpandir;//esto realmente não é preciso aqui, mais melhor fica
        BloqueClasse.ClaseActualParaExpandir = rutina;

        for (int i = 0; i < rutina.metodos.size(); i++) {
            ExpandSubrutine(rutina.metodos.get(i));
        }
        if (rutina.Construtor != null) {
            ExpandSubrutine(rutina.Construtor); //o construtor não precisa expandir previamente como os outros metodos
        }

        BloqueClasse.ClaseActualParaExpandir = ClaseAnterior;
    }

//-----------------------------------------------------------------------------
//------------                                             --------------------
//------------       E S C R E V A  E  M O S T R E         --------------------
//------------                                              -------------------
//-----------------------------------------------------------------------------
    protected static void VerifyWRITE(NodeInstruction node, Vector memory) throws LanguageException {

        String MOSTRAR = "MOSTRE";
        int cont = 0;
        String ESCREVER = Keyword.GetTextKey(Keyword.ESCREVA);
        String instrucao = node.GetText().trim();

        //verificar se a primeira palavra É "escreva
        String escr = instrucao.substring(0, ESCREVER.length());

        if (!escr.equalsIgnoreCase(ESCREVER)) {
            cont = 1;
            escr = instrucao.substring(0, MOSTRAR.length());
            if (!escr.equalsIgnoreCase(MOSTRAR)) {
                throw new LanguageException(
                        node.GetCharNum(), node.GetText(),
                        " ESPERAVA UM... " + ESCREVER, " VERIFICAR A INSTRUÇÃO");
            }
        }

        if (cont == 0) {
            instrucao = instrucao.substring(ESCREVER.length()).trim();
        } else {
            instrucao = instrucao.substring(MOSTRAR.length()).trim();
        }
        //dividir as variaveis

        IteratorCodeParams tok = new IteratorCodeParams(instrucao, ",");
        while (tok.hasMoreElements()) {
            String elem = tok.current();
            tok.next();

            try {
                if (!Values.IsString(elem) && !Expressao.IsExpression(elem, memory)) {
                    throw new LanguageException(
                            node.GetCharNum(),
                            node.GetText(),
                            "O ELEMENTO \"" + elem + "\" NÃO É UM TEXTO OU UMA EXPRESSÃO VÁLIDA",
                            " COLOQUE ASPAS NO ELEMENTO OU VERIFIQUE A EXPRESSÃO");
                }
            } catch (Exception e) {
                if (e instanceof LanguageException) {
                if (((LanguageException)e).line > 0 && !((LanguageException)e).codeLine.isEmpty()) {
                    throw e;
                }
                throw new LanguageException(
                        node.GetCharNum(), node.GetText(),
                        ((LanguageException)e).error, ((LanguageException)e).solution);
                } else {
                    throw new LanguageException(
                            node.GetCharNum(),
                            node.GetText(),
                            "O ELEMENTO \"" + elem + "\" NÃO É UMA EXPRESSÃO VÁLIDA",
                            " VERIFIQUE A EXPRESSÃO");
                }
            }
        }
        cont = 0;
    }
//------------------------------------------------------------------------------
//------------                  CALCULO                     --------------------
//------------------------------------------------------------------------------ 

    public static void VerifyCalculate(NodeInstruction node, Vector memory)
            throws LanguageException {
        String str = node.GetText();
        int pos = str.indexOf(Keyword.ATRIBUI);
        String name = str.substring(0, pos).trim();
        String elem = str.substring(pos + Keyword.ATRIBUI.length()).trim();
        ParteDeExpresion var = Variavel.getVariable(name, memory);
        if (var == null || !(var instanceof Simbolo)) {
            throw new LanguageException(
                    node.GetCharNum(), str,
                    " O SIMBOLO \"" + name + "\" NÃO ESTÁ DEFINIDO NA MEMÓRIA",
                    " DEFINA O SÍMBOLO ANTES DE USAR");
        }
        elem = NormalizeMinus(elem, memory);
        Object value = null;
        try {
            value = Expressao.Evaluate(elem, memory, true);
        } catch (Exception e) {
            throw new LanguageException(
                    node.GetCharNum(), node.GetText(),
                    " O VALOR DA EXPRESSÃO :" + elem,
                    e.getMessage());
        }
        //
        // if ( !Values.IsNumber(value))
        if (!Simbolo.IsCompatible(var, value)) {
            throw new LanguageException(
                    node.GetCharNum(), str,
                    " O VALOR DA EXPRESSÃO :" + elem,
                    " NÃO É COMPATÍVEL COM O TIPO DE DADO:" + ((Simbolo) var).getTypeLexema());
        }
        node.SetText(name + " " + Keyword.ATRIBUI + " " + elem);
    }

    public static void VerifyRetorno(NodeInstruction node, Vector memory) throws LanguageException {
        String str = node.GetText().substring(Keyword.GetTextKey(Keyword.RETORNE).length()).trim();

        try {
            if (!Expressao.IsExpression(str, memory)) {
                throw new LanguageException(
                        node.GetCharNum(),
                        node.GetText(),
                        "O ELEMENTO \"" + str + "\" NÃO É UMA EXPRESSÃO VÁLIDA",
                        " VERIFIQUE A EXPRESSÃO DO RETORNO");
            }
        } catch (Exception e) {
            if (e instanceof LanguageException) {
                if (((LanguageException) e).line > 0 && !((LanguageException) e).codeLine.isEmpty()) {
                    throw e;
                }
                throw new LanguageException(
                        node.GetCharNum(), node.GetText(),
                        ((LanguageException) e).error, ((LanguageException) e).solution);
            } else {
                throw new LanguageException(
                        node.GetCharNum(),
                        node.GetText(),
                        "O ELEMENTO \"" + str + "\" NÃO É UMA EXPRESSÃO VÁLIDA",
                        " VERIFIQUE A EXPRESSÃO DO RETORNO");
            }
        }
    }

    public static String NormalizeMinus(String str, Vector memory) throws LanguageException {
        StringBuffer newExpr = new StringBuffer();
        IteratorExpression tok = new IteratorExpression(str);
        Simbolo var;
        String elem;
        //se comecar pelo sinal menos
        if (tok.current().equals("-(")) {
            newExpr.append("-1 * (");
            tok.next();
        }

        while (tok.hasMoreElements()) {
            elem = tok.current();
            tok.next();
            newExpr.append(GetSafeElement(elem, memory));
        }
        return newExpr.toString().trim();
    }

    public static String GetSafeElement(String elem, Vector memory) throws LanguageException {
        ParteDeExpresion var;
        //------------------------- sinal -  -------------
        if (elem.charAt(0) == '-') {
            //--operador - 
            if (elem.length() == 1) {
                return elem + " ";
            }
            //---resto
            String resto = elem.substring(1);
            var = Variavel.getVariable(resto, memory);
            //valor negativo
            if (var == null || var instanceof Operador) {
                return elem + " ";
            } // se for uma variavel com sinal -
            else {
                return " ( " + resto + " * -1 ) ";
            }
        }
        return elem + " ";
    }
    protected static void cleanMemory(int level, Vector memory) {//sbr
        for (int index = memory.size() - 1; index >= 0; index--) {
            Simbolo v = (Simbolo) memory.get(index);
            //elimina as variaveis superiores ou iguais ao nivel
            if (v.getLevel() >= level) {
                memory.remove(index);
            } else {
                break;
            }
        }
    }
    
}
