package Portugol.Language.Calcular;

import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.SymbolComposto;
import Portugol.Language.Analisador.SymbolObjeto;
import Portugol.Language.Utilitario.Values;
//import Portugol.Language.Criar.Intermediario;
import java.util.Vector;

public class Aritmeticos extends AbstractCalculus {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";

    private static String operBin = " + - * / % ^ mod div ";
    static public String ErroDivPorZero = "DIVISAO POR ZERO";
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
        throw new Exception("ERRO 012:\nNO OPERADOR ARITMETICO [" + oper + "]");
    }
    //---------------------------------------------------------------------------

    // prioridade do operador
    public int GetPriority(Object oper) throws Exception {
        if (!(oper instanceof String)) {
            throw new Exception("ERRO \nOPERADOR DESCOHECIDO ");
        }

        if (((String) oper).equalsIgnoreCase("+")) {
            return AbstractCalculus.ARITMETIC_PRIORITY + 1;
        }
        if (((String) oper).equalsIgnoreCase("-")) {
            return AbstractCalculus.ARITMETIC_PRIORITY + 1;
        }
        if (((String) oper).equalsIgnoreCase("*")) {
            return AbstractCalculus.ARITMETIC_PRIORITY + 2;
        }
        if (((String) oper).equalsIgnoreCase("/")) {
            return AbstractCalculus.ARITMETIC_PRIORITY + 2;
        }
        if (((String) oper).equalsIgnoreCase("%")) {
            return AbstractCalculus.ARITMETIC_PRIORITY + 2;
        }
        if (((String) oper).equalsIgnoreCase("^")) {
            return AbstractCalculus.ARITMETIC_PRIORITY + 3;
        }
        throw new Exception("ERRO 012:\nNO OPERADOR ARITMÉTCO [" + (String) oper + "]");
    }

    // executa o calculo
    @Override
    public String Calculate(Object oper, Vector params) throws Exception {
        if (params.size() != 2) {
            throw new Exception("ERRO 012:\nARITMÉTCOS COM DOIS PARAMETROS");
        }
        return Calculate2((String) oper, params.get(0), params.get(1));
    }

    //---------------------------------------------------------------------------
    public String Calculate2(String oper, Object str1, Object str2) throws Exception {
        Object n1 = null;
        Object n2 = null;
        if (str1 instanceof String) {
            n1 = (String) str1;
        } else if (str1 instanceof SymbolComposto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) str1).getName() + "]");
        } else if (str1 instanceof SymbolObjeto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER OBJETO [" + ((SymbolObjeto) str1).getName() + "]");
        } else if (str1 instanceof SymbolArray) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) str1).getName() + "]");
        } else if (str1 instanceof Simbolo) {
            if (((Simbolo) str1).isNumber()) {
                n1 = ((Simbolo) str1).getValue();
            } else if (((Simbolo) str1).isString()) {
                n1 = (String) ((Simbolo) str1).getValue();
            } else if (((Simbolo) str1).isLogico()) {
                n1 = ((String) ((Simbolo) str1).getValue()).toUpperCase().equals("VERDADEIRO");
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
            if (((Simbolo) str2).isNumber()) {
                //n2 = new Double((Double) ((Simbolo) str2).getValue());
                n2 = ((Simbolo) str2).getValue();
            } else if (((Simbolo) str2).isString()) {
                n2 = (String) ((Simbolo) str2).getValue();
            } else if (((Simbolo) str2).isLogico()) {
                n2 = ((String) ((Simbolo) str2).getValue()).toUpperCase().equals("VERDADEIRO");
            }
        } else {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
        }

        if (Values.IsInteger(n1) && Values.IsInteger(n2)) {
            return CalculateInteger(oper, n1, n2);
        } else if (Values.IsString(n1) && Values.IsString(n2)) {
            return CalculateText(oper, n1, n2);
        } else if (Values.IsNumber(n1) && Values.IsNumber(n2)) {
            return CalculateReal(oper, n1, n2);
        }
        throw new Exception("ERRO 012:\nOPERADOR [" + oper + "] NÃO DEFINIDO");
    }
    //---------------------------------------------------------------------------

    private String CalculateReal(String oper, Object str1, Object str2) throws Exception {
        double n1 = str1 instanceof String ? Double.parseDouble((String) str1) : (str1 instanceof Double ? ((Double) str1).doubleValue() : new Double(((Integer) str1).intValue()));
        double n2 = str2 instanceof String ? Double.parseDouble((String) str2) : (str2 instanceof Double ? ((Double) str2).doubleValue() : new Double(((Integer) str2).intValue()));
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
    //---------------------------------------------------------------------------

    private String CalculateInteger(String oper, Object str1, Object str2) throws Exception {
        int n1 = (int) (str1 instanceof String ? Integer.parseInt((String) str1) : ((Integer) str1).intValue());
        int n2 = (int) (str2 instanceof String ? Integer.parseInt((String) str2) : ((Integer) str2).intValue());
        int val = 0;
        if (oper.equalsIgnoreCase("+")) {
            val = n1 + n2;
        } else if (oper.equalsIgnoreCase("-")) {
            val = n1 - n2;
        } else if (oper.equalsIgnoreCase("*")) {
            val = n1 * n2;
        } else if (oper.equalsIgnoreCase("/")) {
            if (n2 == 0) {
                throw new Exception(ErroDivPorZero);
            }
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
    private String CalculateText(String oper, Object str1, Object str2) throws Exception {
        String n1 = (String) str1;
        String n2 = (String) str2;
        String val = "";
        if (oper.equalsIgnoreCase("+")) {
            val = Values.getStringValue(n1) + Values.getStringValue(n2);
        } else {
            throw new Exception("ERRO 012:\nOPERADOR [" + oper + "] NÃO DEFINIDO");
        }
        return Values.StringToText(val);
    }
}