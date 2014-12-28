package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;
import javax.swing.JOptionPane;

public class ExpandChamadoProcedimento {
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                             ----------------------------
//------------    I N S T R U C A O    S E                ----------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------

    /**
     * expande o ciclo
     *
     * @param begin nodo de inicip
     * @param level nivel
     * @param memory vector de memoria
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    public static void ExpandCHAMADO(BloqueSubrutine rutina, NodeInstruction begin, int level, Vector memory)
            throws LanguageException {
        //string das instrucoes
        String exp = Normalize(begin.GetText());//David: Normalizado para reconocer então con acento
        int endExp = exp.indexOf("(");
        if (endExp <= 0) {
            throw new LanguageException(
                    begin.GetCharNum(), begin.GetText(),
                    "Chamado não ten parêntese aberto", //David: revisar ortografia
                    "Coloque o parêntese depois do chamado ao procedimento");
        }

        try {
            Expressao.ReplaceVariablesToValues(Expressao.ExpresionStringToVector(begin.GetText()), memory, false);
        } catch (LanguageException e) {
            throw new LanguageException(
                    begin.GetCharNum(), begin.GetText(),
                    e.error, e.solution);
        }
    }
//-------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------- 

    /**
     * texto do ciclo
     *
     * @param begin nodo de inicio
     * @return texto do ciclo
     */
    public static String toString(NodeInstruction begin) {
        StringBuffer str = new StringBuffer();
        str.append(begin.toString() + "\n");
        NodeInstruction tmp = begin.GetIfTrue();
        NodeInstruction end = begin.GetNext();

        while (tmp != end) {
            str.append(Intermediario.GetCode(tmp));
            tmp = tmp.GetNext();
        }
        str.append("\n");
        tmp = begin.GetIfFalse();
        while (tmp != end) {
            str.append(Intermediario.GetCode(tmp));
            tmp = tmp.GetNext();
        }

        str.append(end.toString() + "\n");
        return str.toString();
    }
    //-------------------------------------------------------------------------------------
    //-------------------------------------------------------------------------------------
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
