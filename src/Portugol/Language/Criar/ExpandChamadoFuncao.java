package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;
import javax.swing.JOptionPane;

public class ExpandChamadoFuncao {
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
    public static BloqueSubrutine ExpandCHAMADO(String Text, Vector<BloqueSubrutine> metodos)
            throws LanguageException {
        //string das instrucoes
        String exp = Normalize(Text);//David: Normalizado 
//        int endExp = exp.indexOf("(");
//        if (endExp <= 0) {
//            throw new LanguageException(
//                    "Chamado não ten parêntese aberto", //David: revisar ortografia
//                    "Coloque o parêntese depois do chamado ao procedimento");
//        }

        // SE ocupa dois caracteres
        String nome_proced = Text./*substring(0, endExp).*/trim();
        //String parametros = Text.substring(endExp, Text.length()).trim();
        //int cont = 0, pos = 0;
        
        if (BloqueSubrutine.InstanciaActual != null) {
            BloqueClasse claseOrigen = BloqueSubrutine.InstanciaActual.tipoClasseBase.claseOrigen;
            for (int i = 0; i < claseOrigen.metodos.size(); i++) {
                if (claseOrigen.metodos.elementAt(i).Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                    return claseOrigen.metodos.elementAt(i);
                }
            }
        }
        
        for (int i = 0; i < Intermediario.subrutinas.size(); i++) {
            if (Intermediario.subrutinas.elementAt(i).Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                return Intermediario.subrutinas.elementAt(i);
            }
        }

        if (metodos != null) {
            for (int i = 0; i < metodos.size(); i++) {
                if (metodos.elementAt(i).Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                    return metodos.elementAt(i);
                }
            }
        }

        if (BloqueClasse.ClaseActualParaExpandir != null) {
            for (int i = 0; i < BloqueClasse.ClaseActualParaExpandir.metodos.size(); i++) {
                if (BloqueClasse.ClaseActualParaExpandir.metodos.elementAt(i).Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                    return BloqueClasse.ClaseActualParaExpandir.metodos.elementAt(i);
                }
            }
        }
        
        throw new LanguageException(
                "O procedimento <" + nome_proced + "> não existe", //David: revisar ortografia
                "Revise o nome do procedimento ou função");

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
