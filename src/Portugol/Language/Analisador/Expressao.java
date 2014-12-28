package Portugol.Language.Analisador;

import Portugol.Language.Calcular.*;
import Portugol.Language.Utilitario.IteratorCodeLine;
import Portugol.Language.Utilitario.IteratorElements;
import Portugol.Language.Utilitario.IteratorExpression;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Parentesis;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;

public class Expressao {

    public static void Verify(String infix) throws Exception {
        if (!IsGood(infix)) {
            throw new Exception("ERRO NO ANALISADOR :" + GetError(infix));
        }
    }

    public static boolean IsGood(String infix) {
        //verificar parentesis
        if (!Parentesis.Verify(infix)) {
            return false;
        }
        //verificar valores operadores e funcoes
        IteratorExpression it = new IteratorExpression(infix);
        CalculusElement calc = new CalculusElement();
        while (it.hasMoreElements()) {
            String elem = it.current();
            it.next();
            if (!calc.IsCalculus(elem) && !Values.IsValue(elem) && !Parentesis.IsParentesis(elem)) {
                return false;
            }
        }
        return true;
    }

    public static String GetError(String infix) {
        if (!Parentesis.Verify(infix)) {
            return Parentesis.GetError(infix);
        }

        IteratorExpression it = new IteratorExpression(infix);
        CalculusElement calc = new CalculusElement();
        while (it.hasMoreElements()) {
            String elem = it.current();
            it.next();
            if (!calc.IsCalculus(elem) && !Values.IsValue(elem) && !Parentesis.IsParentesis(elem)) {
                return " ERRO : simbolo [" + elem + "] desconhecido ";
            }
        }
        return "OK";
    }

    public static String ReplaceVariablesToValues(String expr, Vector memory) throws LanguageException {

        StringBuffer newExpr = new StringBuffer();
        // iterador de elementos do codigo
        //IteratorExpression tok = new IteratorExpression(expr);
        IteratorElements tok = new IteratorElements(expr);
        //Simbolo var;
        String elem;
        while (tok.hasMoreElements()) {
            elem = tok.current();
            tok.next();
            newExpr.append(GetValueElement(elem, memory));
        }
        return newExpr.toString().trim();
    }

    public static String GetValueElement(String elem, Vector memory) {
        Simbolo var;
        //------------------------- sinal -  -------------
        if (elem.charAt(0) == '-') {
            //--operador - 
            if (elem.length() == 1) {
                return elem;
            }
            //---resto
            String resto = elem.substring(1);
            var = Variavel.getVariable(resto, memory);
            //valor negativo
            if (var == null) {
                return elem + " ";
            } // se for uma variavel vai selecionar o valor            
            else if (var instanceof SymbolArray) {
                SymbolArray a = (SymbolArray) var;
                a.SetIndex(elem, memory);
                return " ( " + a.getValue() + " * -1 ) ";
            } else {
                return " ( " + var.getValue() + " * -1 ) ";
            }
        }
        //-------------------------------------------------------------
        //  variavel sem valor negativo 
        //------------------------------------------------------------
        var = Variavel.getVariable(elem, memory);
        if (var == null) {
            return elem + " ";
        } else if (var instanceof SymbolArray) {
            SymbolArray a = (SymbolArray) var;
            a.SetIndex(elem, memory);
            return ((Simbolo) a.getValue()).getValue().toString() + " ";
        } else {
            return var.getValue().toString() + " ";
        }

    }
    //--------------------------------------------------------------------------- 
    //--------------------------------------------------------------------------- 

    public static String ReplaceVariablesToDefaults(String expr, Vector memory) {
        StringBuffer newExpr = new StringBuffer();
        // iterador de elementos do codigo
        //IteratorExpression tok = new IteratorExpression(expr);
        IteratorElements tok = new IteratorElements(expr);
        //Simbolo var;
        String elem;
        while (tok.hasMoreElements()) {
            elem = tok.current();
            tok.next();
            newExpr.append(GetSafeValueElement(elem, memory));
        }
        return newExpr.toString().trim();
    }

    public static String GetSafeValueElement(String elem, Vector memory) {
        Simbolo var;
        //------------------------- sinal -  -------------
        if (elem.charAt(0) == '-') {
            //--operador - 
            if (elem.length() == 1) {
                return elem;
            }
            //---resto
            String resto = elem.substring(1);
            var = Variavel.getVariable(resto, memory);
            //valor negativo
            if (var == null) {
                return elem + " ";
            } // se for uma variavel vai selecionar o valor            
            else {
                return " ( " + var.getSafeDefaultValue() + " * -1 ) ";
            }
        }
        //-------------------------------------------------------------
        //  variavel sem valor negativo 
        //------------------------------------------------------------
        var = Variavel.getVariable(elem, memory);
        if (var == null) {
            return elem + " ";
        } else {
            return var.getSafeDefaultValue() + " ";
        }
    }
    //--------------------------------------------------------------------------- 
    //--------------------------------------------------------------------------- 

    public static String ReplaceVariablesToDefaults(String expr, Vector memory, int tryUnknow) {

        StringBuffer newExpr = new StringBuffer();
        // iterador de elementos do codigo
        IteratorExpression tok = new IteratorExpression(expr);
        Simbolo var;
        String elem;
        while (tok.hasMoreElements()) {
            elem = tok.current();
            tok.next();
            var = Variavel.getVariable(elem, memory);
            // se for uma variavel vai selecionar o valor
            if (var != null) {
                if (var.getType() != var.DESCONHECIDO) {
                    newExpr.append(var.getDefaultValue() + " ");
                } else {
                    newExpr.append(Simbolo.getDefaultValue(tryUnknow) + " ");
                }
            } //senao mete o elmento
            else {
                newExpr.append(elem + " ");
            }
        }
        return newExpr.toString().trim();
    }

    public static boolean IsExpression(String express, Vector memory) {
        String exp = ReplaceVariablesToDefaults(express, memory);
        IteratorExpression it = new IteratorExpression(exp);
        while (it.hasMoreElements()) {
            String elem = it.current();
            if (!elem.equals("(") && !elem.equals(")")
                    && !elem.equals(",")
                    && !Calculador.IsCalculus(elem)
                    && !Values.IsValue(elem)
                    && !Keyword.DefineRegisto(elem)) {
                return false;
            }
            it.next();
        }
        return true;
    }

    public static String ErrorExpression(String elem, Vector memory) {
        String exp = ReplaceVariablesToDefaults(elem, memory);
        IteratorExpression it = new IteratorExpression(exp);
        while (it.hasMoreElements()) {
            if (!Calculador.IsCalculus(it.current())) {
                return it.current();
            }
            it.next();
        }
        return "NO ERROR";
    }

    public static int TypeExpression(String elem, Vector memory) {
        try {
            String exp = ReplaceVariablesToDefaults(elem, memory);
            Object result = Calculador.CalulateValue(exp);
            if (Values.IsBoolean(result)) {
                return Simbolo.LOGICO;
            }
            if (Values.IsInteger(result)) {
                return Simbolo.INTEIRO;
            }
            if (Values.IsCharacter(result)) {
                return Simbolo.CARACTER;
            }
            if (Values.IsString(result)) {
                return Simbolo.TEXTO;
            }
            if (Values.IsReal(result)) {
                return Simbolo.REAL;
            }
            return Simbolo.DESCONHECIDO;
        } catch (Exception e) {
            return Simbolo.DESCONHECIDO;
        }

    }

    public static Object Evaluate(String expression, Vector memory) {
        try {
            String exp = ReplaceVariablesToValues(expression, memory);
            return Calculador.CalulateValue(exp);
        } catch (Exception e) {
            return "ERRO DE CALCULO";
        }

    }

    public static Object EvaluateByDefaults(String expression, Vector memory) {
        try {
            String exp = ReplaceVariablesToDefaults(expression, memory);
            return Calculador.CalulateValue(exp);
        } catch (Exception e) {
            return "ERRO DE CALCULO";
        }

    }
}
