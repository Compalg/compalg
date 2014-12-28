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
        if (endExp < 0) {
            throw new LanguageException(
                    begin.GetCharNum(), begin.GetText(),
                    "Chamado não ten parêntese aberto", //David: revisar ortografia
                    "Coloque o parêntese depois do chamado ao procedimento");
        }

        // SE ocupa dois caracteres
        String nome_proced = begin.GetText().substring(0, endExp).trim();
        String parametros = begin.GetText().substring(endExp, begin.GetText().length()).trim();
        int cont = 0, pos = 0;
        BloqueSubrutine subr_destino = null;
        for (int i = 0; i < Intermediario.subrutinas.size(); i++) {
            if (Intermediario.subrutinas.elementAt(i).Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                subr_destino = (BloqueSubrutine) Intermediario.subrutinas.elementAt(i);
                break;
            }
        }

        if (subr_destino == null) {
            throw new LanguageException(
                    begin.GetCharNum(), begin.GetText(),
                    "O procedimento <" + nome_proced + "> não existe", //David: revisar ortografia
                    "Revise o nome do procedimento");
        }

        begin.subrutine = subr_destino;
//        String str = parametros;
//        String SEPARATORS = ",";//David: virgula e espacio
//
//        int beg = 0;
//
//        //while( beg  < str.length() &&  SEPARATORS.indexOf(str.charAt(beg))>=0 )
//        //quitar los posibles espacios iniciales
//        while (beg < str.length() && str.charAt(beg) == ' ') {
//            beg++;
//        }
//
//        int end = beg;
//
//        String tempStr = "";
//        while (end < str.length()) {
//            if (SEPARATORS.indexOf(str.charAt(end)) >= 0) {
//
//                NodeInstruction node = new NodeInstruction(tempStr, 0, 0); //los parametros quedan insertados en el principio del metodo, en orden inverso
//                node.SetNext(rutina.start);
//                rutina.start = node;
//
//                beg = end + 1;
//                end = end + 1;
//            } else {
//                tempStr = tempStr + str.charAt(end);
//                end++;
//            }
//        }
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

    public static void CalculatePositions(NodeInstruction begin, double Y, double X) {
        double PY, PX;
        PX = 0.5 / (begin.level + 1.0);
        begin.SetPositionY(Y);
        begin.SetPositionX(X);
        NodeInstruction tmp = begin.GetIfTrue();
        //fazer o  if
        PY = Y + 1;
        NodeInstruction end = begin.GetNext();
        while (tmp != end) {
            // FluxogramVisual.ProcessNodePosition(tmp, PY , X + PX);
            PY = tmp.GetPositionY() + 1;
            tmp = tmp.GetNext();
        }
        // posicao Y do conector
        end.SetPositionY(PY);
        // fazer o else
        tmp = begin.GetIfFalse();
        PY = Y + 1;
        while (tmp != end) {
            //  FluxogramVisual.ProcessNodePosition(tmp,PY , X - PX);
            PY = tmp.GetPositionY() + 1;
            tmp = tmp.GetNext();
        }
        //conector ( calcular o maximo dos ys do if e do else
        if (end.GetPositionY() < PY) {
            end.SetPositionY(PY);
        }
        //posicao X do conector
        end.SetPositionX(X);

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
