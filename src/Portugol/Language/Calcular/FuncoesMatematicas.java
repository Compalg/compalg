package Portugol.Language.Calcular;

import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.SymbolComposto;
import Portugol.Language.Analisador.SymbolObjeto;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;

public class FuncoesMatematicas extends AbstractCalculus {

    private static String functions0 = " ALEATORIO ";
    private static String functions1 = " SENO COSENO TANGENTE COTANGENTE "
            + " ASENO ACOSENO ATANGENTE ACOTANGENTE "
            + " SENOH COSENOH TANGENTEH COTANGENTEH "
            + " EXP ABS RAIZ LOG LN "
            + " PARTEINTEIRA PARTEFRAC ARREDONDAR RETORNA_RETORNO "// esta ultima funcao só serve de apoio as funçoes criadas pelo utilizador.
            ;
    private static String functions2 = " POTENCIA "
            + " MAIOR MENOR ";

    public boolean IsValid(Object str) {
        if (!(str instanceof String)) {
            return false;
        }

        return functions0.indexOf(" " + ((String) str).toUpperCase() + " ") != -1
                || functions1.indexOf(" " + ((String) str).toUpperCase() + " ") != -1
                || functions2.indexOf(" " + ((String) str).toUpperCase() + " ") != -1;
    }

    public int GetNumParameters(Object str) throws Exception {
        if (!(str instanceof String)) {
            throw new Exception("ERRO \nFUNÇÂO DESCOHECIDA ");
        }

        if (functions0.indexOf(" " + ((String) str).toUpperCase() + " ") != -1) {
            return 0;
        }
        if (functions1.indexOf(" " + ((String) str).toUpperCase() + " ") != -1) {
            return 1;
        }
        if (functions2.indexOf(" " + ((String) str).toUpperCase() + " ") != -1) {
            return 2;
        }
        throw new Exception("ERRO nos parametros das funções [" + str + "]");
    }
    //---------------------------------------------------------------------------

    public int GetPriority(Object oper) throws Exception {
        if (IsValid(oper)) {
            return AbstractCalculus.FUNCTION_PRIORITY;
        }
        throw new Exception("ERRO na Prioridade das funções [" + oper + "]");
    }

    public String Calculate(Object oper, Vector params) throws Exception {
        if (!(oper instanceof String)) {
            throw new Exception("ERRO \nFUNÇÂO DESCOHECIDA ");
        }
        if (params.isEmpty()) {
            return Calculate0((String) oper);
        }
        if (params.size() == 1) {
            return Calculate1((String) oper, params.get(0));
        }
        if (params.size() == 2) {
            return Calculate2((String) oper, params.get(0), params.get(1));
        }

        throw new Exception("ERRO funçao parametros errados [" + oper + "] " + params.toString());
    }
    //---------------------------------------------------------------------------

    private String Calculate0(String oper) throws Exception {
        double val = 0;
        if (oper.equalsIgnoreCase("ALEATORIO")) {
            val = java.lang.Math.random();
        } else {
            throw new Exception("ERRO funçao Desconhecida 2 [" + oper + "]");
        }
        return Values.DoubleToString(val);
    }
    //--------------------------------------------------------------------------

    //---------------------------------------------------------------------------
    private String Calculate1(String oper, Object str1) throws Exception {
        double n1 = 0;
        double val = 0;
        if (str1 instanceof String) {
            n1 = Values.StringToDouble((String) str1) + 1;
        } else if (str1 instanceof SymbolComposto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) str1).getName() + "]");
        } else if (str1 instanceof SymbolObjeto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER OBJETO [" + ((SymbolObjeto) str1).getName() + "]");
        } else if (str1 instanceof SymbolArray) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) str1).getName() + "]");
        } else if (str1 instanceof Simbolo) {
            if (((Simbolo) str1).getType() != Simbolo.INTEIRO) {
                throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER DO TIPO [" + ((Simbolo) str1).getTypeLexema() + "]");
            }
            n1 = Values.StringToInteger((String) ((Simbolo) str1).getValue());

            //n1 = ((Integer) ((Simbolo) str1).getValue()); //David: aqui nao é preciso "-2"
        } else {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
        }

        if (oper.equalsIgnoreCase("SENO")) {
            val = java.lang.Math.sin(n1);
        } else if (oper.equalsIgnoreCase("COSENO")) {
            val = java.lang.Math.cos(n1);
        } else if (oper.equalsIgnoreCase("TANGENTE")) {
            val = java.lang.Math.tan(n1);
        } else if (oper.equalsIgnoreCase("COTANGENTE")) {
            val = 1.0 / java.lang.Math.tan(n1);
        } else if (oper.equalsIgnoreCase("ASENO")) {
            val = java.lang.Math.asin(n1);
        } else if (oper.equalsIgnoreCase("ACOSENO")) {
            val = java.lang.Math.acos(n1);
        } else if (oper.equalsIgnoreCase("ATANGENTE")) {
            val = java.lang.Math.atan(n1);
        } else if (oper.equalsIgnoreCase("ACOTANGENTE")) {
            val = 1.0 / java.lang.Math.atan(n1);
        } else if (oper.equalsIgnoreCase("SENOH")) {
            val = java.lang.Math.sinh(n1);
        } else if (oper.equalsIgnoreCase("COSENOH")) {
            val = java.lang.Math.cosh(n1);
        } else if (oper.equalsIgnoreCase("TANGENTEH")) {
            val = java.lang.Math.tanh(n1);
        } else if (oper.equalsIgnoreCase("COTANGENTEH")) {
            val = 1.0 / java.lang.Math.tanh(n1);
        } else if (oper.equalsIgnoreCase("EXP")) {
            val = java.lang.Math.exp(n1);
        } //valor absoluto de inteiros sao inteiros
        else if (oper.equalsIgnoreCase("ABS")) {
            val = java.lang.Math.abs(n1);
            if (Values.IsInteger(str1)) {
                return Values.IntegerToString(val);
            }
        } else if (oper.equalsIgnoreCase("RAIZ")) {
            val = java.lang.Math.sqrt(n1);
        } else if (oper.equalsIgnoreCase("LOG")) {
            val = java.lang.Math.log10(n1);
        } else if (oper.equalsIgnoreCase("LN")) {
            val = java.lang.Math.log(n1);
        } // parte inteira do numeros
        else if (oper.equalsIgnoreCase("PARTEINTEIRA")) {
            return Values.IntegerToString(n1);
        } //parte real
        else if (oper.equalsIgnoreCase("PARTEFRAC")) {
            String num = Values.DoubleToString(n1);
            return num.substring(num.indexOf('.') + 1);
        } else if (oper.equalsIgnoreCase("ARREDONDAR")) {
            double vm = java.lang.Math.ceil(n1);
            if (n1 - vm >= 0.5) {
                return Values.IntegerToString((int) n1 + 1);
            }
            return Values.IntegerToString((int) n1);
        } else if (oper.equalsIgnoreCase("RETORNA_RETORNO")) {
            return Values.StringToText("" + str1);
        } else {
            throw new Exception("ERRO função Desconhecida 1 [" + oper + "]");
        }
        return Values.DoubleToString(val);
    }
    //---------------------------------------------------------------------------

    private String Calculate2(String oper, Object str1, Object str2) throws Exception {
        double n1 = 1;
        double n2 = 1;
        double val = 0;
        int tipo = -1;
        if (str1 instanceof String) {
            n1 = Values.StringToDouble((String) str1) + 1;
        } else if (str1 instanceof SymbolComposto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) str1).getName() + "]");
        } else if (str1 instanceof SymbolObjeto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER OBJETO [" + ((SymbolObjeto) str1).getName() + "]");
        } else if (str1 instanceof SymbolArray) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) str1).getName() + "]");
        } else if (str1 instanceof Simbolo) {
            if (((Simbolo) str1).getType() != Simbolo.INTEIRO && ((Simbolo) str1).getType() != Simbolo.REAL) {
                throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER DO TIPO [" + ((Simbolo) str1).getTypeLexema() + "]");
            }
            n1 = Values.StringToInteger((String) ((Simbolo) str1).getValue());
            tipo = ((Simbolo) str1).getType();
        } else {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
        }

        if (str2 instanceof String) {
            n1 = Values.StringToDouble((String) str2) + 1;
        } else if (str2 instanceof SymbolComposto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) str2).getName() + "]");
        } else if (str2 instanceof SymbolObjeto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER OBJETO [" + ((SymbolObjeto) str2).getName() + "]");
        } else if (str2 instanceof SymbolArray) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) str2).getName() + "]");
        } else if (str2 instanceof Simbolo) {
            if (((Simbolo) str1).getType() != Simbolo.INTEIRO && ((Simbolo) str1).getType() != Simbolo.REAL) {
                throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER DO TIPO [" + ((Simbolo) str2).getTypeLexema() + "]");
            }
            n2 = Values.StringToInteger((String) ((Simbolo) str2).getValue());
        } else {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
        }

        // potencia de inteiros sao inteiros
        if (oper.equalsIgnoreCase("POTENCIA")) {
            val = java.lang.Math.pow(n1, n2);
            //if (Values.IsInteger(str1) && Values.IsInteger(str2)) {
            return Values.IntegerToString(val);
            //}
        }

        // maior de 2 numeros
        if (oper.equalsIgnoreCase("MAIOR")) {
            val = java.lang.Math.max(n1, n2);
            if (Values.IsInteger(str1) && Values.IsInteger(str2)) {
                return Values.IntegerToString(val);
            }
        } else if (oper.equalsIgnoreCase("MENOR")) {
            val = java.lang.Math.min(n1, n2);
            //if (Values.IsInteger(str1) && Values.IsInteger(str2)) {
            return Values.IntegerToString(val);
            //}
        } else {
            throw new Exception("ERRO FUNÇÃO DESCONHECIA 2 [" + oper + "]");
        }
        
        if (tipo == 0 && Values.IsInteger(str1) ||  tipo == 3) {
            return Values.IntegerToString(val);
        }
        return Values.DoubleToString(val);
    }
}
