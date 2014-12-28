package Testes;

import Editor.GUI.EditorCAlg;
import Editor.GUI.SplashScreen;
import Editor.Utils.DicasdoDia;
import Portugol.Language.Calcular.Aritmeticos;
import Portugol.Language.Calcular.Calculador;
import Portugol.Language.Calcular.CalculusElement;
import Portugol.Language.Calcular.Functions;
import Portugol.Language.Calcular.Relationals;
//import Portugol.Language.Criar.Intermediario;
import Portugol.Language.Utilitario.Parentesis;
import Portugol.Language.Utilitario.IteratorString;
import Portugol.Language.Utilitario.IteratorExpression;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;
import Editor.Utils.LinhaEditor;
import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Simbolo;
import javax.swing.JOptionPane;

public class Main {

    private static DicasdoDia dica;

    /**
     * Creates a new instance of Main
     */
    public Main() {
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println(args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println(args[i]);
        }

        SplashScreen splash = new SplashScreen(4000);// 6 segundos ate abrir o principal
        splash.showSplashAndExit();

        EditorCAlg frmain;
        LinhaEditor linha = new LinhaEditor();
        if (args.length > 0) {
            frmain = new EditorCAlg(args[0]);
        } else {

            frmain = new EditorCAlg();
            //Linhas do editor
            frmain.setScrolEditorTexto(linha.obterScrolPane(frmain.getEditorTexto(), frmain.getScrolEditorTexto()));
        }

        frmain.setVisible(true);
        //David: rehabilitar  Dica        
//        dica = new DicasdoDia();
//        for (long i = 0; i < 555555555; i++) {
//            for (long j = 0; j < 5; j++) {
//            }
//        }
        //JOptionPane.showMessageDialog(null, dica.dicaDia(), "Dica do dia...", 1);
        
        //David: Para testar todas as expressoes
//        try {
//            TestParentesis();
//            TestStrings();
//            TestPosFix();
//            TestStringCalculus();
//            TestCalculus();
//            testRelational();
//            testAritmetic();
//        } catch (Exception e) {
//            System.out.printf(e.getMessage());
//        }
    }
// --------------------------------------------

    public static void TestParentesis() throws Exception {
        String str = " (2 + (3) ) * 5";
        System.out.println("STR: " + str);
        if (Parentesis.Verify(Expressao.ExpresionStringToVector(str))) {
            System.out.println("OK");
        } else {
            System.out.println(Parentesis.GetError(Expressao.ExpresionStringToVector(str)));
        }
    }
//----------------------------------------------------

    public static void TestStrings() throws Exception {
        String msg = "OLA MUNDO";
        String txt = Values.StringToText(msg);
        String str = Values.TextToString(txt);
        System.out.println("MSG\t:" + msg);
        System.out.println("TXT\t:" + txt);
        System.out.println("STR\t:" + str);
        Functions fstr = new Functions();
        Vector v = new Vector();
        v.clear();
        v.add(txt);
        String comp = fstr.Calculate("comprimento", v);
        int max = Values.StringToInteger(comp);

        for (int i = 0; i < max; i++) {
            v.clear();
            v.add(txt);
            v.add(Values.IntegerToString(i));
            System.out.println(fstr.Calculate("letra", v));
        }


    }
    //----------------------------------------------------
//----------------------------------------------------

    public static void TestPosFix() throws Exception {
        //String str= " 2+ 3 * 5";
        // String str= " -2+ -3 * -5";
        String str = " -2+ -30.0 / -5 + comprimento(\"Ola mundo novo\")";

        //String str= " 2 / 3.0  ";

        Calculador exp = new Calculador(Expressao.ExpresionStringToVector(str));
        System.out.println("INFIX  :" + exp.GetInfix());
        System.out.println("POSFIX :" + exp.GetPosfix());
        //  System.out.println("RESULT :" + exp.GetResult());
    }

    public static void TestStringCalculus() throws Exception {
        Vector v = new Vector();
        v.clear();
        v.add("\"Ola\"");
        v.add("\"Mundo\"");
        Calculate("+", v);
        v.clear();
        v.add("\"Ola\"");
        Calculate("comprimento", v);
        Simbolo a = new Simbolo("", "literal", "a", "David", 0, "literal a<-\"Ola\"");
        Simbolo b = new Simbolo("", "literal", "b", "Silva", 0, "literal b<-\"Ola\"");
        v.clear();
        v.add(a);
        Calculate("comprimento", v);
        v.add(b);
        Calculate("+", v);
    }
//----------------------------------------------------

    public static void TestCalculus() throws Exception {
        Vector v = new Vector();
        v.clear();

        v.add("100");
        v.add("2");
        Calculate("+", v);
        Calculate("-", v);
        Calculate("^", v);
        Calculate(">", v);
        Calculate("<>", v);
        Calculate("=", v);
        v.clear();
        v.add("verdadeiro");
        v.add("falso");
        Calculate("<>", v);
        Calculate("=", v);
        Calculate("E", v);
        Calculate("OU", v);
        v.clear();
        v.add("verdadeiro");
        v.add("verdadeiro");
        Calculate("<>", v);
        Calculate("=", v);
        Calculate("E", v);
        Calculate("OU", v);
        v.clear();
        v.add("falso");
        v.add("falso");
        Calculate("<>", v);
        Calculate("=", v);
        Calculate("E", v);
        Calculate("OU", v);

        v.clear();
        v.add("falso");
        Calculate("NAO", v);
        v.clear();
        v.add("verdadeiro");
        Calculate("NAO", v);

        v.clear();
        v.add("25");
        Calculate("SENO", v);
        Calculate("RAIZ", v);

        v.clear();
        v.add("5");
        v.add("2");
        Calculate("POTENCIA", v);

        v.clear();
        Calculate("Aleatorio", v);


        Simbolo a = new Simbolo("", "inteiro", "a", "6", 0, "inteiro a<-6");
        Simbolo b = new Simbolo("", "inteiro", "b", "4", 0, "inteiro b<-4");
        v.clear();
        v.add(a);
        v.add("4");
        Calculate("+", v);
        Calculate("-", v);
        Calculate("^", v);
        Calculate(">", v);
        Calculate("<>", v);
        Calculate("=", v);
        
        Simbolo c = new Simbolo("", "logico", "c", "verdadeiro", 0, "logico c<-verdadeiro");
        Simbolo d = new Simbolo("", "logico", "d", "verdadeiro", 0, "logico d<-verdadeiro");
        v.clear();        
        v.add(c);
        v.add("falso");
        Calculate("<>", v);
        Calculate("=", v);
        Calculate("E", v);
        Calculate("OU", v);
        v.clear();
        v.add(c);
        v.add(d);
        Calculate("<>", v);
        Calculate("=", v);
        Calculate("E", v);
        Calculate("OU", v);
        v.clear();
        v.add("falso");
        v.add("falso");
        Calculate("<>", v);
        Calculate("=", v);
        Calculate("E", v);
        Calculate("OU", v);

        v.clear();
        v.add("falso");
        Calculate("NAO", v);
        v.clear();
        v.add(c);
        Calculate("NAO", v);

        v.clear();
        v.add(a);
        Calculate("SENO", v);
        Calculate("RAIZ", v);

        v.clear();
        v.add(a);
        v.add(b);
        Calculate("POTENCIA", v);
    }

    public static void Calculate(String oper, Vector params) throws Exception {
        CalculusElement calc = new CalculusElement();
        System.out.println(oper + " <" + params.toString() + "> =" + calc.Calculate(oper, params));
    }

//----------------------------------------------------
    public static void testRelational() throws Exception {
        testAllRelational("1", "1");
        testAllRelational("10", "1");
        testAllRelational("9.895", "200.52");
        testAllRelational("verdadeiro", "falso");
        testAllRelational("\"Ola\"", "\"Ola\"");
        testAllRelational("\"Ola\"", "\"Mundo\"");
        testAllRelational("\"Mundo\"", "\"Ola\"");
    }

    public static void testAllRelational(String v1, String v2) throws Exception {
        Relationals relat = new Relationals();
        Vector v = new Vector();
        v.add(v1);
        v.add(v2);
        System.out.println("---------------------------------------");
        String oper = "=";
        System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + relat.Calculate(oper, v));
        oper = "<>";
        System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + relat.Calculate(oper, v));
        if (!v1.equals("verdadeiro") && !v2.equals("verdadeiro")) {
            oper = ">";
            System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + relat.Calculate(oper, v));
            oper = ">=";
            System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + relat.Calculate(oper, v));
            oper = "<";
            System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + relat.Calculate(oper, v));
            oper = "<=";
            System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + relat.Calculate(oper, v));
        }
    }
//----------------------------------------------------

    public static void testAritmetic() throws Exception {
        testAllAritmetic("10", "5");
        testAllAritmetic("5", "10");
        testAllAritmetic("10", "3.0");
        testAllAritmetic("\"OLA\"", "\"MUNDO\"");
    }

    public static void testAllAritmetic(String v1, String v2) throws Exception {
        Aritmeticos aritm = new Aritmeticos();
        Vector v = new Vector();
        v.add(v1);
        v.add(v2);
        System.out.println("---------------------------------------");
        String oper = "+";
        System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + aritm.Calculate(oper, v));
        if (!v1.contains("OLA")) {
            oper = "-";
            System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + aritm.Calculate(oper, v));
            oper = "*";
            System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + aritm.Calculate(oper, v));
            oper = "/";
            System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + aritm.Calculate(oper, v));
            if (!v2.equals("3.0")) {
                oper = "%";
                System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + aritm.Calculate(oper, v));
            }
            oper = "^";
            System.out.println(v1 + " " + oper + " " + v2 + " ->\t " + aritm.Calculate(oper, v));
        }
    }

    //----------------------------------------------------
    public static void testExpressionIterator() throws Exception {
        //String str= " -2+ -3 * -5";
        String str = "potencia(2,10)%23";
        IteratorExpression it = new IteratorExpression(str);
        System.out.println("EXPRESSION\t:" + str);
        System.out.println("NORMALIZED\t:" + it.getExpression());
        System.out.println("ELEMS\n");
        while (it.hasMoreElements()) {
            System.out.println("[" + it.current() + "]");
            it.next();
        }
    }

    //----------------------------------------------------
    public static void testStringIterator() throws Exception {
        String str = " 1234 + 2344 * sen ( 3 * 10 )";
        IteratorString it = new IteratorString(str);
        while (it.hasMoreElements()) {
            System.out.println("[" + it.current() + "]");
            it.next();
        }
    }
}
