package Portugol.Language.Criar;

import Editor.Utils.FileManager;
import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Consola.ConsoleIO;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Calcular.Ficheiro;
import Portugol.Language.Utilitario.CodeLine;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import java.util.StringTokenizer;
import java.util.Vector;
import Portugol.Language.Calcular.Procedimento;
import Portugol.Language.Calcular.Funcao;
import Portugol.Language.Criar.ExpandEnquanto;
import Portugol.Language.Criar.ExpandSe;
import Portugol.Language.Criar.NodeInstruction;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import javax.swing.JOptionPane;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public class BloqueClasse extends Bloque{

    public static String VERSION = "Versão:1.0 \t(c) Augusto Bilabila";

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