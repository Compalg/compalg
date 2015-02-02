package Portugol.Language.Criar;

import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public class BloqueClasse extends Bloque{

    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";

    public Vector<BloqueSubrutine> metodos;
    public BloqueSubrutine Construtor;
    NodeInstruction lastNode;
    static public BloqueClasse ClaseActualParaExpandir=null;
    
    /**
     * Constroi um fluxograma
     *
     * @param code programa fonte
     * @throws Portugol.Language.Utils.LanguageException excepcao
     */
    public BloqueClasse(/*String code*/) throws LanguageException {
        // Construir(code);
        start = null;
        metodos = new Vector<BloqueSubrutine>();
        lastNode = null;
        Construtor = null; 
    }

    /**
     * string
     *
     * @return string
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        NodeInstruction pt = start;
        while (pt != null) {
            str.append(GetCode(pt));
            pt = pt.next;
        }
        return str.toString();
    }

    /**
     * calcula o texto de um nodo
     *
     * @param node nodo de origem
     * @return o texto de um nodo
     */
    public static String GetCode(NodeInstruction node) {//im

        if (node.GetType() == Keyword.CONECTOR) {
            return "";
        }
        if (node.GetType() == Keyword.SE) {
            return ExpandSe.toString(node);
        }
        if (node.GetType() == Keyword.ENQUANTO) {
            return ExpandEnquanto.toString(node);
        }
        return node.toString() + "\n";
    }

}