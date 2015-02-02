package Editor.GUI.CodeDocument;

import java.awt.Color;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

public class AlgoSyntaxHighlight extends DefaultStyledDocument {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";

    public static Color defaultBackGround = Color.WHITE;
    public boolean esDebug = true;

    /**
     * Creates a new instance of AlgoSyntaxHighlight
     */
    public AlgoSyntaxHighlight() {
    }

    private void printSelectLine(int numChar) {
    }

    public void selectErrorLine(int numChar) {
    }

    public void selectCodeLine(int numChar) {
    }

    public void deSelectCodeLine(int numChar) {
    }

    public void clearTextBackground() {
    }

    /*
     * //David: Para saber la linea actual del Caret
    static int getLineOfOffset(JTextComponent comp, int offset) throws BadLocationException {
        Document doc = comp.getDocument();
        if (offset < 0) {
            throw new BadLocationException("Can't translate offset to line", -1);
        } else if (offset > doc.getLength()) {
            throw new BadLocationException("Can't translate offset to line", doc.getLength() + 1);
        } else {
            Element map = doc.getDefaultRootElement();
            return map.getElementIndex(offset);
        }
    }

    static int getLineStartOffset(JTextComponent comp, int line) throws BadLocationException {
        Element map = comp.getDocument().getDefaultRootElement();
        if (line < 0) {
            throw new BadLocationException("Negative line", -1);
        } else if (line >= map.getElementCount()) {
            throw new BadLocationException("No such line", comp.getDocument().getLength() + 1);
        } else {
            Element lineElem = map.getElement(line);
            return lineElem.getStartOffset();
        }
    }

    public int currentLine() {
        int dot = e.getDot();
        int line = getLineOfOffset(textComponent, dot);
        int positionInLine = dot - getLineStartOffset(textComponent, line);
        return positionInLine;
    }
    */
}
