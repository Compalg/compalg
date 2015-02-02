package Portugol.Language.Analisador;

import Portugol.Language.Calcular.*;
import Portugol.Language.Criar.BloqueSubrutine;
import Portugol.Language.Utilitario.IteratorElements;
import Portugol.Language.Utilitario.IteratorExpression;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import java.util.Stack;
import java.util.Vector;

public class Expressao {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";

    static public String ErroDeCalculo = "ERRO DE CALCULO";

    public static Vector ReplaceVariablesToValues(Vector expr, Vector memory, boolean safe) throws LanguageException {
        //System.out.print("Entra: " + expr + "\n");
        Vector newExpr = new Vector();
        Object CurrentElem;
        Object NextElem;
        Stack pilha = new Stack();

        for (int i = 0; i < expr.size(); i++) {
            CurrentElem = expr.get(i);
            NextElem = i + 1 < expr.size() - 1 ? expr.get(i + 1) : null;
            if (NextElem != null && NextElem instanceof String && NextElem.equals("(")) {
                Vector e = new Vector();
                e.add(CurrentElem);
                e.add(NextElem);
                pilha.push(e);
                i++;
                continue;
            } else if (CurrentElem != null && CurrentElem instanceof String && CurrentElem.equals(")")) {
                if (pilha.isEmpty()) {
                    throw new LanguageException(
                            "Os parêntesis não estão bem colocados na expressão",
                            "Verifique os parêntesis");
                }
                Vector e = (Vector) pilha.pop();
                e.add(CurrentElem);
                CurrentElem = e;
            }

            if (pilha.isEmpty()) {
                Object val = GetValueElement(CurrentElem, memory, safe);
                newExpr.add(val);
            } else {
                Vector v = new Vector();
                v.add(CurrentElem);

                CurrentElem = ReplaceVariablesToValues(v, memory, safe);

                Vector e = (Vector) pilha.pop();
                for (int j = 0; j < ((Vector) CurrentElem).size(); j++) {
                    e.add(((Vector) CurrentElem).get(j));
                }

                pilha.push(e);
            }
        }

        //System.out.print("Sale: " + newExpr.toString().trim() + "\n");
        return newExpr;
    }

    public static Vector ExpresionStringToVector(String expr) throws LanguageException {
        //System.out.print("Entra: " + expr + "\n");
        Vector newExpr = new Vector();
        String CurrentElem;
        IteratorElements tok = new IteratorElements(expr);
        while (tok.hasMoreElements()) {
            CurrentElem = tok.current();
            tok.next();
            if (CurrentElem.startsWith(".")) {

                if (newExpr.isEmpty() || newExpr.lastElement() instanceof String && ((String) newExpr.lastElement()).trim().equals(")")) {
                    //David: Não é permitido utilizar um campo na mesma chamada á função
                    //throw new Exception("()."); //David: revisar ortografia
                    throw new LanguageException(
                            "Não é permitido utilizar um campo na mesma chamada da função. \nGuarde o resultado da função numa variável e depois utilize o campo desejado",
                            "Em caso de dúvida, consulte a documentação"); //David: revisar ortografia

                }
                String s = (String) newExpr.remove(newExpr.size() - 1);
                newExpr.add(CurrentElem);
            } else {
                newExpr.add(CurrentElem);
            }
        }
        return newExpr;
    }

    public static Object GetValueElement(Object elemento, Vector memory, boolean safe) throws LanguageException {
        String elem = "";
        if (elemento instanceof String) {
            elem = (String) elemento;
        } else if (elemento instanceof Vector) {
            elem = (String) ((Vector) elemento).get(0);
        } else if (elemento instanceof Simbolo) {
            return elemento;
        }

        ParteDeExpresion pde;
        boolean minus = false;
        //------------------------- sinal -  -------------
        if (!elem.isEmpty() && elem.charAt(0) == '-') {
            minus = true;
        }

        //-------------------------------------------------------------
        //  variavel sem valor negativo 
        //------------------------------------------------------------
        pde = Variavel.getVariable(elemento, memory);
        if (pde == null) {
            if (minus) {
                return "(" + elem + "* -1)";
            } else {
                return elem;
            } //David: tener en cuenta los parentesis de las operaciones
        } else if (pde instanceof Operador) {

            return pde.TextoOrigen;

        } else if (pde instanceof SymbolArray) {
            if (elem.contains("[") && elem.contains("]")) {
                SymbolArray a = (SymbolArray) pde;
                a.SetIndex(elem, memory);
                String RetVal;
                if (safe) {
                    RetVal = ((Simbolo) a.getDefaultValue()).getValue().toString();
                } else {
                    RetVal = ((Simbolo) a.getValue()).getValue().toString();
                }
                if (minus) {
                    return "(" + RetVal + "* -1)";
                } else {
                    return RetVal;
                }
            } else {
                return pde;
            }
        } else if (pde instanceof SymbolComposto) {
            return pde;
        } else if (pde instanceof SymbolObjeto) {
            return pde;
        } else {
            return (Simbolo) pde;
        }
    }

    public static boolean IsExpression(String express, Vector memory) throws LanguageException {
        Vector exp = ReplaceVariablesToValues(ExpresionStringToVector(express), memory, true);
        for (int i = 0; i < exp.size(); i++) {
            Object elem = exp.get(i);
            if (!Calculador.IsCalculus(elem)
                    && !Values.IsValue(elem)
                    && !Keyword.DefineRegisto(elem)
                    && !Keyword.DefineClasse(elem)
                    && !(elem instanceof Simbolo)
                    && elem instanceof String
                    && !(Variavel.getVariable(elem, memory) instanceof Simbolo)
                    && (!elem.equals("(") && !elem.equals(")")
                    && !elem.equals(","))) {
                return false;
            }
        }
        return true;
    }

    public static String ErrorExpression(String express, Vector memory) throws LanguageException {
        String exp = (String) ReplaceVariablesToValues(ExpresionStringToVector(express), memory, true).get(0);
        IteratorExpression it = new IteratorExpression(exp);
        while (it.hasMoreElements()) {
            String elem = it.current();
            if (!elem.equals("(") && !elem.equals(")")
                    && !elem.equals(",")
                    && !Calculador.IsCalculus(elem)
                    && !Values.IsValue(elem)
                    && !Keyword.DefineRegisto(elem)
                    && !(Variavel.getVariable(elem, memory) instanceof Simbolo)) {
                return it.current();
            }
            it.next();
        }
        return "NAO ERRO";
    }

    public static int TypeExpression(String elem, Vector memory) {
        try {
            Vector exp = ReplaceVariablesToValues(ExpresionStringToVector(elem), memory, true);
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

    /**
     *
     * @param expression
     * @param memory
     * @return
     */
    public static Object Evaluate(String expression, Vector memory) throws LanguageException {
        return Evaluate(ExpresionStringToVector(expression), memory, false);
    }

    public static Object Evaluate(String expression, Vector memory, boolean safe) throws LanguageException {
        return Evaluate(ExpresionStringToVector(expression), memory, safe);
    }

    public static Object Evaluate(Vector expression, Vector memory, boolean safe) throws LanguageException {
        try {
            Vector exp = ReplaceVariablesToValues(expression, memory, safe);
            return exp.size() > 0 ? Calculador.CalulateValue(exp) : "";
        } catch (Exception e) {
            if (safe && e.getMessage().equals(Aritmeticos.ErroDivPorZero)) {
                return "1";
            } else if (e.getMessage().equals(Aritmeticos.ErroDivPorZero)) {
                return Aritmeticos.ErroDivPorZero;
            } else if (e.getMessage().startsWith(SymbolArray.ErroForaLimites)) {
                return SymbolArray.ErroForaLimites;
            } else if (e.getMessage().startsWith(BloqueSubrutine.ErroRecursividad)) {
                return BloqueSubrutine.ErroRecursividad;
            } else {
                if (e instanceof LanguageException) {
                    throw (LanguageException) e;
                }

                throw new LanguageException(
                        e.getMessage(),
                        "Verifique a expressão");
                //return ErroDeCalculo;
            }
        }
    }
}
