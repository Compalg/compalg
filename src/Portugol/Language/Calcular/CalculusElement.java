package Portugol.Language.Calcular;

import java.util.Vector;

public class CalculusElement {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";

    static Vector<AbstractCalculus> elemCalc;
    static Functions calcFunctions;
    static Aritmeticos calcAritmeticos;
    static Logico calcLogico;
    static Relationals calcRelationals;
    static AccesosCompuestos calcAccesosCompuestos;

    static {
        calcFunctions = new Functions();
        calcAritmeticos = new Aritmeticos();
        calcLogico = new Logico();
        calcRelationals = new Relationals();
        calcAccesosCompuestos = new AccesosCompuestos();

        elemCalc = new Vector<>();
        elemCalc.add(calcFunctions);
        elemCalc.add(calcAritmeticos);
        elemCalc.add(calcLogico);
        elemCalc.add(calcRelationals);
        elemCalc.add(calcAccesosCompuestos);
    }

    //verifica se é uma função
    public static boolean IsFunction(String str) {
        return calcFunctions.IsValid(str);
    }

    //verifica se é um operador aritmetico
    public static boolean IsAritmetic(String str) {
        return calcAritmeticos.IsValid(str);
    }

    //verifica se é um operador logico
    public static boolean IsLogic(String str) {
        return calcLogico.IsValid(str);
    }

    //verifica se é um operador relacional
    public static boolean IsRelational(String str) {
        return calcRelationals.IsValid(str);
    }

    //verifica se é um operador relacional ou logico ou relacional
    public static boolean IsOperator(String str) {
        return IsRelational(str) || IsLogic(str) || IsAritmetic(str);
    }

    //verifica se é o testo é um elemento de cálculo
    public static boolean IsElemCalculus(String str) {
        return IsFunction(str)
                || IsAritmetic(str)
                || IsLogic(str)
                || IsRelational(str);
    }

    //verifica se é um elemento de cálculo válido
    public boolean IsCalculus(Object str) {
        if (str instanceof String && ((String) str).equals("(")
                || str instanceof String && ((String) str).equals(")")) {
            return true;
        }

        for (int index = 0; index < elemCalc.size(); index++) {
            if (((AbstractCalculus) elemCalc.get(index)).IsValid(str)) {
                return true;
            }
        }
        return false;
    }

    //--------------------------------------------------------------------------
    public int GetNumParameters(Object str) throws Exception {
        for (int index = 0; index < elemCalc.size(); index++) {
            if (((AbstractCalculus) elemCalc.get(index)).IsValid(str)) {
                return ((AbstractCalculus) elemCalc.get(index)).GetNumParameters(str);
            }
        }
        throw new Exception("ERRO 014:\nPARÁMETROS DE OPERADOR DESCONHECIDO [" + str + "]");
    }

    //---------------------------------------------------------------------------
    public int GetPriority(Object str) throws Exception {
        if (str instanceof String && ((String)str).equalsIgnoreCase("(")) {
            return 0;
        }
        if (str instanceof String && ((String)str).equalsIgnoreCase(")")) {
            return 0;
        }
        for (int index = 0; index < elemCalc.size(); index++) {
            if (((AbstractCalculus) elemCalc.get(index)).IsValid(str)) {
                return ((AbstractCalculus) elemCalc.get(index)).GetPriority(str);
            }
        }

        throw new Exception("ERRO 014:\nNA PRIORIDADE [" + (String) str + "]");
    }

    // Executa o calculo do elemento
    public String Calculate(Object str, Vector params) throws Exception {
        for (int index = 0; index < elemCalc.size(); index++) {
            if (((AbstractCalculus) elemCalc.get(index)).IsValid(str)) {
                return ((AbstractCalculus) elemCalc.get(index)).Calculate(str, params);
            }
        }
        throw new Exception("ERRO 014:\nNO CALCULO DE OPERADOR DESCONHECIDO [" + str + "]" + params.toString());
    }
}
