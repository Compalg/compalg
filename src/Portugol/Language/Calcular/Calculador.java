package Portugol.Language.Calcular;

import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Utilitario.Parentesis;
import Portugol.Language.Utilitario.IteratorExpression;
import Portugol.Language.Utilitario.IteratorString;
import java.util.Stack;
import java.util.Vector;

public class Calculador {

    public static String VERSION = "Versão:1.0 \t(c)Augusto Bilabila";
    /**
     * Creates a new instance of Calculador
     */
    Vector inFix;
    Vector posFix;
    private static CalculusElement calculator = new CalculusElement();

    // constroi um calculador com uma expressao na forma infixa
    public Calculador(Vector exp) {
        inFix = exp;
        try {
            posFix = this.CalulatePosFix(inFix);
        } catch (Exception e) {
            //posFix = e.getMessage();
        }
    }

    // verifica se a string é um elemento de cálculo válido
    public static boolean IsCalculus(Object str) {
        CalculusElement calculator = new CalculusElement();
        return calculator.IsCalculus(str);
    }

    // retorna a expressao infixa normalizada
    public Vector GetInfix() {
        return inFix;
    }

    // retorna a Expressao posfixa
    public Vector GetPosfix() {
        return posFix;
    }

    // faz o calculo da expressao
//    public Object GetResult() throws Exception {
//        return CalulateValue(inFix);
//    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
    /**
     * metodo estatico que converte infixa em posfixa
     */
    public static Vector CalulatePosFix(Vector infix) throws Exception {
        if (!Parentesis.Verify(infix)) {
            throw new Exception(Parentesis.GetError(infix));
        }

        Stack s = new Stack();
        Vector strPosFix = new Vector();

        for (int i = 0; i < infix.size(); i++) {//David
            Object elem = infix.get(i);

            // parametros das funcoes
            if (elem instanceof String && ((String) elem).equals(",")) {
                //retirar todos elementos ate ao parentesis
                while (!s.empty() && !((String) s.peek()).equals("(")) {
                    elem = (String) s.pop();
                    strPosFix.add(elem);
                }
                continue;
            }
            // introduzir directamente na pilha
            if (elem instanceof String && ((String) elem).equalsIgnoreCase("(")) {
                s.push(elem);
            } //retirar da pilha todos operadores ate encontrar o (
            else if (elem instanceof String && ((String) elem).equalsIgnoreCase(")")) {
                while (!s.empty()) {
                    elem = (String) s.pop();
                    if (elem instanceof String && ((String) elem).equalsIgnoreCase("(")) {
                        break;
                    }
                    strPosFix.add(elem);;
                }
            } // se for um operador
            else if (calculator.IsCalculus(elem)) {
                int prio = calculator.GetPriority(elem);
                //retirar todos operadores com maior prioridade
                while (!s.empty() && calculator.GetPriority((String) s.peek()) >= prio) {
                    strPosFix.add(s.pop());
                }
                s.push(elem);
            } else {
                strPosFix.add(elem);;
            }
        }// fim do iterador

        while (!s.empty()) {
            strPosFix.add(s.pop());
        }

        return strPosFix;
    }

//------------------------------------------------------------------------------
    // Executa o calculo
    public static Object CalulateValue(Vector expr) throws Exception {
        Vector exprPostFix = CalulatePosFix(expr);
        // se a expressao for vazia
        // if( str.length() == 0) return expr;
        //IteratorString it = new IteratorString(str);
        Stack result = new Stack();
        Vector params = new Vector();
        Object elem;
        for (int i = 0; i < exprPostFix.size(); i++) {
            // se for um calculo
            elem = exprPostFix.get(i);

            if (calculator.IsCalculus(elem)) {
                // retirar os parametros do calculo
                params.clear();
                for (int index = 0; index < calculator.GetNumParameters(elem); index++) {

                    //verificar se existem parametros
                    if (result.empty()) {
                        throw new Exception(" ERRO 011:\nNO NÚMERO DE PARÁMETROS NO SÍMBOLO :" + elem);
                    }
                    //adicionar no inicio
                    params.add(0, result.pop());
                }
                //adicionar o resultado
                result.push(calculator.Calculate(elem, params));
            } else // se for um valor   
            {
                result.push(elem);
            }

        }

        if (result.size() == 1) {
            return result.pop();
        } else {
            throw new Exception("A EXPRESSÃO ESTÁ MAL CONSTRUÍDA");
            //throw new Exception(" ERRO 011:\nA EXPRESSÃO [" + expr + "] ESTÁ MAL CONSTRUÍDA");
        }
    }
}
