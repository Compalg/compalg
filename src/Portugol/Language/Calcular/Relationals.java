package Portugol.Language.Calcular;

import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.SymbolComposto;
import Portugol.Language.Analisador.SymbolObjeto;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;

public class Relationals extends AbstractCalculus {

    private static String relationals = " > >= < <= ";
    private static String equalsOper = " = =/= <> "; //David: acrecentado o operador <>

    /**
     * verifica se é um operador relacional
     *
     * @param str nome do operador
     * @return operador valido
     */
    public boolean IsValid(Object str) {
        if (!(str instanceof String)) {
            return false;
        }
        return relationals.indexOf(" " + ((String) str).toUpperCase() + " ") != -1
                || equalsOper.indexOf(" " + ((String) str).toUpperCase() + " ") != -1;
    }

    /**
     * calcula o numero de parametros do operador
     *
     * @return numero de parametros
     * @param oper nome do operador
     * @throws java.lang.Exception ERRO
     */
    public int GetNumParameters(Object oper) throws Exception {
        if (!(oper instanceof String)) {
            throw new Exception("ERRO \nFUNÇÂO DESCOHECIDA ");
        }

        if (IsValid(oper)) {
            return 2;
        }
        throw new Exception("ERRO 009:\nNOS PARAMETROS DO OPERADOR RELACIONAL [" + oper + "]");
    }
    //---------------------------------------------------------------------------

    /**
     * retorna a prioridade do operador
     *
     * @return prioridade do operdor
     * @param oper nome do operador
     * @throws java.lang.Exception ERRO
     */
    public int GetPriority(Object oper) throws Exception {
        if (!(oper instanceof String)) {
            throw new Exception("ERRO \nOPERADOR DESCOHECIDO ");
        }
        if (((String) oper).equalsIgnoreCase(">")) {
            return AbstractCalculus.RELATIONAL_PRIORITY;
        }
        if (((String) oper).equalsIgnoreCase("<")) {
            return AbstractCalculus.RELATIONAL_PRIORITY;
        }
        if (((String) oper).equalsIgnoreCase(">=")) {
            return AbstractCalculus.RELATIONAL_PRIORITY;
        }
        if (((String) oper).equalsIgnoreCase("<=")) {
            return AbstractCalculus.RELATIONAL_PRIORITY;
        }
        if (((String) oper).equalsIgnoreCase("=")) {
            return AbstractCalculus.RELATIONAL_PRIORITY;
        }
        if (((String) oper).equalsIgnoreCase("=/=")) {
            return AbstractCalculus.RELATIONAL_PRIORITY;
        }
        if (((String) oper).equalsIgnoreCase("<>")) {
            return AbstractCalculus.RELATIONAL_PRIORITY; //David: acrecentado ((String) oper)ador <>
        }
        throw new Exception("ERRO 009:\nNA PRIORIDADE DO OPERADOR RELACIONAAL [" + ((String) oper) + "]");
    }

    /**
     * Executa o calculo
     *
     * @return valor do calculo
     * @param oper nome do operador
     * @param params parametros
     * @throws java.lang.Exception ERRO
     */
    public String Calculate(Object oper, Vector params) throws Exception {
        if (!(oper instanceof String)) {
            throw new Exception("ERRO \nOPERADOR DESCOHECIDO ");
        }

        if (params.size() != 2) {
            throw new Exception("ERRO 009:\nRELACIONAIS - RELACIONAIS COM DOIS PARAMETROS");
        }
        return Calculate((String) oper, params.get(0), params.get(1));
    }
    //---------------------------------------------------------------------------

    private String Calculate(String oper, Object str1, Object str2) throws Exception {
        //aritmetica de numeros inteiros
        Object n1 = "";
        Object n2 = "";
        int t1 = 0;
        int t2 = 0;
        if (str1 instanceof String) {
            n1 = (String) str1;
        } else if (str1 instanceof SymbolComposto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) str1).getName() + "]");
        } else if (str1 instanceof SymbolObjeto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER OBJETO [" + ((SymbolObjeto) str1).getName() + "]");
        } else if (str1 instanceof SymbolArray) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) str1).getName() + "]");
        } else if (str1 instanceof Simbolo) {
            n1 = ((Simbolo) str1).getValue();
            t1 = ((Simbolo) str1).getType();
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
            n2 = (String) ((Simbolo) str2).getValue();
            t2 = ((Simbolo) str2).getType();
        } else {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
        }

        if (Values.IsNumber(n1) && Values.IsNumber(n2)) {
            return CalculateValueRelational(oper, n1, n2);
        }
        //aritmetica de numeros inteiros
        if (Values.IsString(n1) && Values.IsString(n2)) {
            return CalculateTextRelational(oper, n1, n2);
        } else if (Values.IsBoolean(n1) && Values.IsBoolean(n2)) {
            return CalculateLogicRelational(oper, n1, n2);
        } else {
            throw new Exception("ERRO 009:\nNO TIPO DE PARAMETROS DO OPERADOR:" + oper);
        }
    }

    //---------------------------------------------------------------------------
    private String CalculateLogicRelational(String oper, Object str1, Object str2) throws Exception {
        boolean n1 = Values.StringToBoolean((String) str1);
        boolean n2 = Values.StringToBoolean((String) str2);
        boolean val = false;
        if (oper.equalsIgnoreCase("=")) {
            val = n1 == n2;
        } else if (oper.equalsIgnoreCase("=/=")) {
            val = n1 != n2;
        } else if (oper.equalsIgnoreCase("<>")) {
            val = n1 != n2; //David: acrecentado operador <>
        } else {
            throw new Exception("ERRO 009:\nNO CALCULO BOOLEANO DO OPERADOR [" + oper + "]");
        }
        return Values.BooleanToString(val);
    }
    //---------------------------------------------------------------------------

    private String CalculateValueRelational(String oper, Object str1, Object str2) throws Exception {
        double n1 = str1 instanceof String ? Double.parseDouble((String) str1) : (str1 instanceof Double ? ((Double) str1).doubleValue() : new Double(((Integer) str1).intValue()));
        double n2 = str2 instanceof String ? Double.parseDouble((String) str2) : (str2 instanceof Double ? ((Double) str2).doubleValue() : new Double(((Integer) str2).intValue()));
        boolean val = false;
        if (oper.equalsIgnoreCase(">")) {
            val = n1 > n2;
        } else if (oper.equalsIgnoreCase(">=")) {
            val = n1 >= n2;
        } else if (oper.equalsIgnoreCase("<")) {
            val = n1 < n2;
        } else if (oper.equalsIgnoreCase("<=")) {
            val = n1 <= n2;
        } else if (oper.equalsIgnoreCase("=")) {
            val = n1 == n2;
        } else if (oper.equalsIgnoreCase("=/=")) {
            val = n1 != n2;
        } else if (oper.equalsIgnoreCase("<>")) {
            val = n1 != n2; //David: acrecentado operador <>
        } else {
            throw new Exception("ERRO 009:\nNO CALCULO DO OPERADOR RELACIONAL [" + oper + "]");
        }
        return Values.BooleanToString(val);
    }
    //---------------------------------------------------------------------------

    private String CalculateTextRelational(String oper, Object str1, Object str2) throws Exception {
        String n1 = Values.TextToString((String) str1);
        String n2 = Values.TextToString((String) str2);
        boolean val = false;
        if (oper.equalsIgnoreCase(">")) {
            val = n1.compareTo(n2) > 0;
        } else if (oper.equalsIgnoreCase(">=")) {
            val = n1.compareTo(n2) >= 0;
        } else if (oper.equalsIgnoreCase("<")) {
            val = n1.compareTo(n2) < 0;
        } else if (oper.equalsIgnoreCase("<=")) {
            val = n1.compareTo(n2) <= 0;
        } else if (oper.equalsIgnoreCase("=")) {
            val = n1.compareTo(n2) == 0;
        } else if (oper.equalsIgnoreCase("=/=")) {
            val = n1.compareTo(n2) != 0;
        } else if (oper.equalsIgnoreCase("<>")) {
            val = n1.compareTo(n2) != 0; //David: acrecentado operador <>
        } else {
            throw new Exception("ERRO 009:\nNO CALCULO DO OPERADOR RELACIONAL [" + oper + "]");
        }
        return Values.BooleanToString(val);
    }
}