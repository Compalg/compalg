package Portugol.Language.Analisador;

import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import javax.swing.JOptionPane;

public class ParteDeExpresion {
    String TextoOrigen;    

    public ParteDeExpresion(String origTxt)
            throws LanguageException {
        TextoOrigen = origTxt;
    }

    public ParteDeExpresion(){
        TextoOrigen = "";
    }

    /**
     * return object String
     *
     * @return informacao da variavel
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        str.append(TextoOrigen);
        return str.toString();
    }

    /**
     * igual
     *
     * @param other segundo argumento
     * @return sao iguais?
     */
    public boolean equal(ParteDeExpresion other) {
        return TextoOrigen == other.TextoOrigen;
    }

    public boolean typeEqual(ParteDeExpresion otro) {
        return (otro instanceof ParteDeExpresion);
    }
}
