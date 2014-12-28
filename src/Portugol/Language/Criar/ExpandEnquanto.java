package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;

public class ExpandEnquanto {

    /**
     * Creates a new instance of ExpandEnquanto
     */
    public ExpandEnquanto() {
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                             ----------------------------
//------------    I N S T R U C A O    E N Q U A N T O     ----------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * expand o enquanto
     *
     * @param whileNode nodo de inicio do ciclo
     * @param level nivel
     * @param memory vector de memoria
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    public static void ExpandWHILE(NodeInstruction whileNode, int level, Vector memory) throws LanguageException {
        String ENQUANTO = Keyword.GetTextKey(Keyword.ENQUANTO);
        String FIMENQUANTO = Keyword.GetTextKey(Keyword.FIMENQUANTO);
        //David: 
        String FACA = Keyword.GetTextKey(Keyword.FAZ);

        String exp = Normalize(whileNode.GetText());//David: Para reconocer faça con acento
        int endCondic = exp.indexOf(FACA);
        if (endCondic < 0) {
            throw new LanguageException(
                    whileNode.GetCharNum(),
                    whileNode.GetText(),
                    "Ciclo ENQUANTO sem FACA",
                    "Escreva ENQUANTO condição <FACA>");
        }

        //enquanto = 8 caracteres
        String condic = whileNode.GetText().substring(ENQUANTO.length(), endCondic).trim();

        if (Expressao.TypeExpression(condic, memory) != Simbolo.LOGICO) {
            throw new LanguageException(
                    whileNode.GetCharNum(), whileNode.GetText(),
                    "\"" + endCondic + "\" NÃO É UMA CONDIÇÃO VÁLIDA",
                    "VERIFIQUE SE A CONDIÇÃO ESTÁ BEM ESCRITA");
        }


        NodeInstruction tmp = whileNode.GetNext();
        if (tmp.GetType() == Keyword.FIMENQUANTO) {
            //no caso do ciclo vazio, não permitir, mas se permitere tem as condições para executar sem problemas
            throw new LanguageException(
                    whileNode.GetCharNum(),
                    whileNode.GetText(),
                    "O ciclo ENQUANTO está vazío",
                    "Acrecente instruções dentro do ciclo.");
        }
        //fazer do for node a inicializacao
        whileNode.SetText(condic);
        whileNode.SetType(Keyword.ENQUANTO);
        whileNode.SetLevel(level);

        // ligar a condic e o bloco
        whileNode.SetIfTrue(tmp);

        while (tmp != null && tmp.GetType() != Keyword.FIMENQUANTO
                && tmp.GetNext().GetType() != Keyword.FIMENQUANTO) {
            tmp.SetLevel(level + 1);
            tmp = tmp.GetNext();
        }
        NodeInstruction endWhile = tmp.GetType() == Keyword.FIMENQUANTO ? tmp : tmp.GetNext();
        //fazer o ciclo
        if (tmp.GetType() != Keyword.FIMENQUANTO) {
            //o ultimo antes do fim-enquanto
            tmp.SetLevel(level + 1);
            //fim do while
            tmp.SetNext(whileNode);
        }
        //ligar o false de condic
        whileNode.SetIfFalse(endWhile);
        //modificar o endWhile para JOIN
        endWhile.SetType(Keyword.CONECTOR);
        //instrucao seguinte ao ciclo
        endWhile.SetLevel(level);
        whileNode.SetNext(endWhile);
    }

    /**
     * texto com as intrucoes do ciclo
     *
     * @param begin inicio do ciclo
     * @return texto com as intrucoes do ciclo
     */
    public static String toString(NodeInstruction begin) {
        StringBuffer str = new StringBuffer();
        str.append(begin.toString() + "\n");
        NodeInstruction tmp = begin.GetIfTrue();
        while (tmp != null && tmp.GetType() != Keyword.ENQUANTO) {
            str.append(Intermediario.GetCode(tmp));
            tmp = tmp.GetNext();
        }
        tmp = begin.GetIfFalse();
        str.append(tmp.toString() + "\n");


        return str.toString();
    }

//-------------------------------------------------------------------------------------
//    FL
//-------------------------------------------------------------------------------------    
    public static void CalculatePositions(NodeInstruction begin, double Y, double X) {
        double PY, PX;
        PX = 0.5 / (begin.level + 1.0);

        begin.SetPositionY(Y);
        begin.SetPositionX(X);
        NodeInstruction tmp = begin.GetIfTrue();
        //fazer o  if
        PY = Y + 1;
        NodeInstruction end = begin;
        while (tmp != end) {
            //   FluxogramVisual.ProcessNodePosition(tmp, PY , X + PX);
            PY = tmp.GetPositionY() + 1;
            tmp = tmp.GetNext();
        }
        //processar o fim do enquanto
        tmp = begin.GetIfFalse();
        tmp.SetPositionY(PY);
        tmp.SetPositionX(X);

    }
    //David: Agregado para permitir que se identifique ENTÃO con acento
    static private String from = "ãõáéíóúàèìòùâêîôûÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛçÇ";
    static private String to = "AOAEIOUAEIOUAEIOUAEIOUAEIOUAOAEIOUCC";

    public static String Normalize(String str) {
        StringBuffer tmp = new StringBuffer();
        int index;
        for (int i = 0; i < str.length(); i++) {
            index = from.indexOf(str.charAt(i));
            if (index == -1) {
                tmp.append(str.charAt(i));
            } else {
                tmp.append(to.charAt(index));
            }
        }
        return tmp.toString().trim().toUpperCase();
    }
}
