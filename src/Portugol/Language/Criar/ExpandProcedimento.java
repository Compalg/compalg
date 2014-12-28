package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.TipoDeParametro;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;
import javax.swing.JOptionPane;

public class ExpandProcedimento {
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
    public static void ExpandSUBRUTINA(BloqueSubrutine rutina, NodeInstruction begin, int level, Vector memory)
            throws LanguageException {
        //string das instrucoes
        String exp = Normalize(begin.GetText());//David: 
        int endExp = exp.indexOf("(");
        if (endExp < 0) {
            throw new LanguageException(
                    begin.GetCharNum(), begin.GetText(),
                    "Chamado não ten parêntese aberto", //David: revisar ortografia
                    "Coloque o parêntese depois do chamado ao procedimento");
        }

        // SE ocupa dois caracteres
        String nome_proced = begin.GetText().substring(0, endExp).trim();
        if (nome_proced.toUpperCase().startsWith("PROCEDIMENTO")) {
            rutina.type = Bloque.PROCEDIMENTO;
            rutina.Nome = nome_proced.toUpperCase().replace("PROCEDIMENTO ", "").trim();
            rutina.TipoRetorno = "";
        } else if (nome_proced.toUpperCase().startsWith("FUNCAO")) {
            nome_proced = nome_proced.toUpperCase().replace("FUNCAO ", "").trim();
            int beg = nome_proced.length()-1;
            while (beg > 0 && nome_proced.charAt(beg) != ' ') {
                beg--;
            }
            rutina.type = Bloque.FUNCAO;
            rutina.Nome = nome_proced.substring(beg, nome_proced.length()).trim();
            rutina.TipoRetorno = nome_proced.substring(0, beg).trim();
        } else {
            throw new LanguageException(
                    "Ten que utilizar PROCEDIMENTO o FUNCAO",
                    "Mude a declaração"); //David: revisar ortografia
        }

        //Procesar os parametros
        String parametros = begin.GetText().substring(endExp, begin.GetText().length()).trim();

        String str = parametros.replace('(', (char) (32)).replace(')', (char) (32)).trim();
        if (str.contains("<-") || str.contains("{") || str.contains("}")) {
            throw new LanguageException(
                    begin.GetCharNum(), begin.GetText(),
                    "O CompAlg não aceita valores por defeito nos parâmetros",
                    "Tire a assinação do valor por defeito");
        }
        String SEPARATORS = ",";//David: virgula e espacio
        String PERMITIDOS = "abcdefghijkmnlopqrstuvwxyzABCDEFGHIJKMNLOPQRSTUVWXYZ0123456789_[]& ";//David: virgula e espacio

        int beg = 0;

        //while( beg  < str.length() &&  SEPARATORS.indexOf(str.charAt(beg))>=0 )
        //quitar los posibles espacios iniciales
        while (beg < str.length() && str.charAt(beg) == ' ') {
            beg++;
        }

        int end = beg;

        String tempStr = "";
        String fullStr = "";
        int contarParamProcesado = 1;
        while (end < str.length()) {
            if (SEPARATORS.indexOf(str.charAt(end)) >= 0) {
                if (str.trim().length() > 0 && tempStr.trim().isEmpty()) {
                    throw new LanguageException(
                            begin.GetCharNum(), begin.GetText(), 
                            "O parâmetro " + Integer.toString(contarParamProcesado) + " ficó vazio", 
                            "Tire uma vírgula o complete o código"); //David:Revisar ortografia
                }
                AddParameter(rutina, begin, tempStr);
                contarParamProcesado++;
                end = end + 1;
                tempStr = "";
            } else {
                if (PERMITIDOS.indexOf(str.charAt(end)) >= 0) {
                    tempStr = tempStr + str.charAt(end);
                    fullStr = tempStr + str.charAt(end);
                }
                end++;
            }
        }

        if (fullStr.trim().length() > 0 && tempStr.trim().isEmpty()) {
            throw new LanguageException(
                    begin.GetCharNum(), begin.GetText(), 
                    "O parâmetro " + Integer.toString(contarParamProcesado) + " ficó vazio", 
                    "Tire uma vírgula o complete o código"); //David:Revisar ortografia
        }
        AddParameter(rutina, begin, tempStr);
        
        //Agregar atributos ao metodo
        if (rutina.classePae != null) {
            NodeInstruction node = rutina.getStartNode(); //primeiro nodo da rutina, depois da declaracion (nome e parametros)
            NodeInstruction pt = rutina.classePae.start.GetNext();//pegar a instruçao depois da clase, deve ser atributo               
            while (pt != null && pt.GetType() != Keyword.FIMCLASSE) {
                NodeInstruction copia = new NodeInstruction(pt);
                copia.SetNext(node.GetNext());
                node.SetNext(copia);
                copia.EsReferencia = true;
                pt = pt.GetNext();
            }
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

    private static void AddParameter(BloqueSubrutine rutina, NodeInstruction begin, String tempStr)
            throws LanguageException {
        if (!tempStr.isEmpty()) {
            String PERMITIDOS = "abcdefghijkmnlopqrstuvwxyzABCDEFGHIJKMNLOPQRSTUVWXYZ0123456789_";//David: virgula e espacio

            //David: provocar la creacion de la variable como un instruccion mas, cuando esto ocurra va a verificar si es parametro para tomar el valor del parametro en el chamado
            NodeInstruction node = new NodeInstruction(tempStr.replace("&", "").replace("*", ""), begin.GetCharNum(), begin.GetLevel()); //los parametros quedan insertados en el principio del metodo, en orden inverso
            node.SetNext(begin.GetNext());
            begin.SetNext(node);

            tempStr = tempStr.trim();
            String TipoDato;

            int beg = 0;
            int CantAmpersands = 0;
            while (beg < tempStr.length() - 1 && PERMITIDOS.indexOf(tempStr.charAt(beg)) > -1) {
                beg++;
            }
            TipoDato = tempStr.substring(0, beg);
            tempStr = tempStr.substring(beg, tempStr.length());

            beg = 0;
            int end = 1;
            while (beg < tempStr.length() - 1 && (tempStr.charAt(beg) == ' ' || tempStr.charAt(beg) == '&')) {
                if (tempStr.charAt(beg) == '&') {
                    CantAmpersands++;
                }
                beg++;
            }

            if (CantAmpersands > 1) {
                throw new LanguageException(begin.GetCharNum(), begin.GetText(),
                        "A declaração dos parâmetros tiene extra símbolos do Ampersand (&)",
                        "Fique só com un símbolo de Ampersand (&)");
            }

            if (tempStr.indexOf("[") > -1) {
                tempStr = tempStr.substring(0, tempStr.indexOf("["));
            }

            TipoDeParametro tipoParam = new TipoDeParametro();
            tipoParam.Name = tempStr.substring(beg, tempStr.length()).trim();
            tipoParam.PorValor = CantAmpersands == 0;
            tipoParam.Tipo = TipoDato;
            rutina.parametros.add(tipoParam);
        }
    }
}
