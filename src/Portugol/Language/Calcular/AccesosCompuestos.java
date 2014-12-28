package Portugol.Language.Calcular;

import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.SymbolComposto;
import Portugol.Language.Analisador.SymbolObjeto;
import Portugol.Language.Utilitario.Values;
//import Portugol.Language.Criar.Intermediario;
import java.util.Vector;

public class AccesosCompuestos extends AbstractCalculus {

    private static String operBin = " . ";
    //static public String ErroDivPorZero = "ERRO 012:\nDIVISAO POR ZERO (IMPOSSIVEL CALCULAR)";
    // retorna os simbolos da arimetica

    public String GetSymbols() {
        return operBin;
    }

    //  verifica se é um operador aritmetico ( + - * / % ^ )
    public boolean IsValid(Object str) {
        return operBin.indexOf(" " + str + " ") != -1;
    }

    // calcula o numero de parametros
    public int GetNumParameters(Object oper) throws Exception {
        if (operBin.indexOf(" " + oper + " ") != -1) {
            return 2;
        }
        throw new Exception("ERRO \nNO OPERADOR DE ACCESO [" + oper + "]");
    }
    //---------------------------------------------------------------------------

    // prioridade do operador
    public int GetPriority(Object oper) throws Exception {
        if (!(oper instanceof String)) {
            throw new Exception("ERRO \nOPERADOR DESCOHECIDO ");
        }

        if (((String) oper).equalsIgnoreCase(".")) {
            return AbstractCalculus.PONTO_PRIORITY;
        }
        throw new Exception("ERRO 012:\nNO OPERADOR DE ACCESO");
    }

    // executa o calculo
    @Override
    public String Calculate(Object oper, Vector params) throws Exception {
        if (params.size() != 2) {
            throw new Exception("ERRO 012:\nACCESO A CAMPO COM DOIS PARAMETROS");
        }
        return Calculate2((String) oper, params.get(0), params.get(1));
    }

    //---------------------------------------------------------------------------
    public String Calculate2(String oper, Object str1, Object str2) throws Exception {
        String n1 = "";
        String n2 = "";
        if (str1 instanceof String) {
            n1 = (String) str1;
        } else if (str1 instanceof SymbolComposto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) str1).getName() + "]");
        } else if (str1 instanceof SymbolObjeto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER OBJETO [" + ((SymbolObjeto) str1).getName() + "]");
        } else if (str1 instanceof SymbolArray) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) str1).getName() + "]");
        } else if (str1 instanceof Simbolo) {
            if (((Simbolo) str1).getType() == Simbolo.TEXTO) {
                n1 = Values.StringToText((String) ((Simbolo) str1).getValue());
            } else {
                n1 = (String) ((Simbolo) str1).getValue();
            }
        } else {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
        }

        if (str2 instanceof String) {
            n2 = (String) str2;
        } else if (str2 instanceof SymbolComposto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) str2).getName() + "]");
        } else if (str2 instanceof SymbolObjeto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER OBJETO [" + ((SymbolObjeto) str2).getName() + "]");
        } else if (str2 instanceof SymbolArray) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) str2).getName() + "]");
        } else if (str2 instanceof Simbolo) {
            if (((Simbolo) str2).getType() == Simbolo.TEXTO) {
                n2 = Values.StringToText((String) ((Simbolo) str2).getValue());
            } else {
                n2 = (String) ((Simbolo) str2).getValue();
            }
        } else {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
        }

        if (Values.IsInteger(n1) && Values.IsInteger(n2)) {
            return CalculateInteger(oper, n1, n2);
        } else if (Values.IsString(n1) && Values.IsString(n2)) {
            return CalculateText(oper, n1, n2);
        }

        if (Values.IsNumber(n1) && Values.IsNumber(n2)) {
//            if((Intermediario.VerOperator).contains("div")){
//                JOptionPane.showMessageDialog(null,"O OPERADOR [ div ] É UM OPERADOR APENAS PARA INTEIROS \n");
//                Intermediario.VerOperator = " ";
//                return CalculateReal1(n1,oper,n2);
//            }else
            return CalculateReal(oper, n1, n2);
        }
        throw new Exception("ERRO 012:\nOPERADOR [" + oper + "] NÃO DEFINIDO");
    }
    //---------------------------------------------------------------------------

    private String CalculateReal(String oper, String str1, String str2) throws Exception {
        double n1 = Values.StringToDouble(str1);
        double n2 = Values.StringToDouble(str2);
        double val = 0;
        if (oper.equalsIgnoreCase("+")) {
            val = n1 + n2;
        } else if (oper.equalsIgnoreCase("-")) {
            val = n1 - n2;
        } else if (oper.equalsIgnoreCase("*")) {
            val = n1 * n2;
        } else if (oper.equalsIgnoreCase("/")) {
            if (n2 == 0) {
                throw new Exception("ERRO 012:\nDIVISAO POR ZERO");
            }
            val = n1 / n2;
        } else if (oper.equalsIgnoreCase("^")) {
            val = java.lang.Math.pow(n1, n2);
        } else {
            throw new Exception("ERRO 012:\nOPERADOR [" + oper + "] NÃO DEFINIDO");
        }
        return Values.DoubleToString(val);
    }

//    //---------------------------------------------------------------------------
//    private String CalculateReal1(String str1, String oper, String str2) throws Exception {
//        double n1 = Values.StringToDouble(str1);
//        double n2 = Values.StringToDouble(str2);
//        double val = 0;
//        if (oper.equalsIgnoreCase("/")) {
//            throw new Exception("ERRO 012:\nO OPERADOR [ div ] É UM OPERADOR APENAS PARA INTEIROS");
//
//        }
//        return Values.DoubleToString(val);
//    }
    //---------------------------------------------------------------------------
    private String CalculateInteger(String oper, String str1, String str2) throws Exception {
        int n1 = Values.StringToInteger(str1);
        int n2 = Values.StringToInteger(str2);
        int val = 0;
        if (oper.equalsIgnoreCase("+")) {
            val = n1 + n2;
        } else if (oper.equalsIgnoreCase("-")) {
            val = n1 - n2;
        } else if (oper.equalsIgnoreCase("*")) {
            val = n1 * n2;
        } else if (oper.equalsIgnoreCase("/")) {
            val = n1 / n2;
        } else if (oper.equalsIgnoreCase("%")) {
            val = n1 % n2;
        } else if (oper.equalsIgnoreCase("^")) {
            val = (int) java.lang.Math.pow(n1, n2);
        } else {
            throw new Exception("ERRO 012:\nOPERADOR [" + oper + "] NÃO DEFINIDO");
        }
        return Values.IntegerToString(val);
    }

    //---------------------------------------------------------------------------
    private String CalculateText(String oper, String str1, String str2) throws Exception {
        String n1 = Values.TextToString(str1);
        String n2 = Values.TextToString(str2);
        String val = "";
        if (oper.equalsIgnoreCase("+")) {
            val = n1 + n2;
        } else {
            throw new Exception("ERRO 012:\nOPERADOR [" + oper + "] NÃO DEFINIDO");
        }
        return Values.StringToText(val);
    }
}