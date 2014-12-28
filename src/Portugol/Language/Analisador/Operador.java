package Portugol.Language.Analisador;

import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import javax.swing.JOptionPane;

public class Operador extends ParteDeExpresion {

    public final static int DESCONHECIDO = -1;
    public final static int ARITMETICO = 0;
    public final static int LOGICO = 1;
    public final static int RELACIONAL = 2;
    public final static int ASIGNACION = 3;
    protected int type;

    public Operador(String origTxt) {
        TextoOrigen = origTxt;
        this.type = getType(origTxt);
    }

    public void copyFrom(Operador origen) throws LanguageException {
        if (origen == null) {
            return;
        }

        if (!(origen instanceof Operador)) {
            throw new LanguageException("Tipo de objeto não é correto", "Mude o tipo de parâmetro na chamada");//David:Revisar
        }

        TextoOrigen = origen.TextoOrigen;
        type = origen.type;

    }

    public boolean IsCompatible(int type) {
        if (this.type == type) {
            return true;
        }

        return false;
    }

    /**
     * tipo da variavel
     *
     * @param t texto com o tipo
     * @return tipo
     */
    public static int getType(String oper) {
        oper = oper.toUpperCase().trim();
        if (oper.equals("+") || oper.equals("-") || oper.equals("*") || oper.equals("/") || oper.equals("%")
                || oper.equals("^") /*|| oper.equals("mod") || oper.equals("div")*/) {
            return ARITMETICO;
        }
        if (oper.equals("NAO") || oper.equals("E") || oper.equals("OU") || oper.equals("XOU")) {
            return LOGICO;
        }
        if (oper.equals(">") || oper.equals(">=") || oper.equals("<") || oper.equals("<=")
                || oper.equals("=") || oper.equals("=/=") || oper.equals("<>")) {
            return RELACIONAL;
        }
        if (oper.equals("<-")) {
            return ASIGNACION;
        }

        return DESCONHECIDO;
    }

    public static Operador TryOperador(String txt){
        Operador res = new Operador(txt);
        if (res.type == Operador.DESCONHECIDO)
            return null;
        return res;
    }
            
    //------------------------------------------------------------------------
    public boolean isAritmetico() {
        return type == ARITMETICO;
    }

    public boolean isLogico() {
        return type == LOGICO;
    }

    public boolean isRelacional() {
        return type == RELACIONAL;
    }

    public boolean isAsignacion() {
        return type == ASIGNACION;
    }

    /**
     * return object String
     *
     * @return informacao da variavel
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append("valor\t:" + TextoOrigen + "\n");
        str.append("tipo\t:" + type + "\n");
        return str.toString();
    }

    public static boolean IsCompatible(int type1, Object value) {
        if (value == null) {
            return false;
        }

        if (!(value instanceof Operador)) {
            return false;
        }

        return type1 == ((Operador) value).type;
    }

    /**
     * igual
     *
     * @param other segundo argumento
     * @return sao iguais?
     */
    public boolean equal(Operador other) {
        return other.TextoOrigen.equals(TextoOrigen);
    }

    public boolean typeEqual(Operador otro) {
        if (otro == null) {
            return false;
        }

        if (!(otro instanceof Operador)) {
            return false;
        }

        return type == ((Operador) otro).type;
    }
}
