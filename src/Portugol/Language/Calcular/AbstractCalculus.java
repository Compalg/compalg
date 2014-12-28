package Portugol.Language.Calcular;

import java.util.Vector;

public abstract class AbstractCalculus {

    /**
     * Prioridade dos operadores Logicos
     */
    public static int LOGIC_PRIORITY = 100;
    /**
     * Prioridade dos operadores Relacionais
     */
    public static int RELATIONAL_PRIORITY = 200;
    /**
     * Prioridade dos operadores Aritmeticos
     */
    public static int ARITMETIC_PRIORITY = 300;
    /**
     * Prioridade das Funcoes
     */
    public static int FUNCTION_PRIORITY = 400;
    /**
     * Prioridade do operador PONTO (accesos de campos de registos y clases)
     */
    public static int PONTO_PRIORITY = 500;

    /**
     * veririfica se o parametro ou um elemento de calculo
     *
     * @param str nome do elemento
     * @return veririfica se o parametro ou um elemento de calculo
     */
    public abstract boolean IsValid(Object str);

    public abstract int GetNumParameters(Object oper) throws Exception;

    public abstract int GetPriority(Object oper) throws Exception;

    public abstract String Calculate(Object oper, Vector params) throws Exception;
}
