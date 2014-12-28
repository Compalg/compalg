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
import static Portugol.Language.Criar.BloqueRegisto.GetCode;
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
public abstract class Bloque {
    /**
     * tipo Registo
     */
    public final static int REGISTO = 0;
    /**
     * tipo Procedimento
     */
    public final static int PROCEDIMENTO = 1;
    /**
     * tipo Funcao
     */
    public final static int FUNCAO = 2;

    /**
     * tipo Classe
     */
    public final static int CLASSE = 3;
    /**
     * tipo Construtor
     */
    public final static int CONSTRUTOR = 4;
    
    public static String VERSION = "Versão:1.0 \t(c) Augusto Bilabila";
    public String Nome; //David: nome do Bloco
    public int type;
    protected NodeInstruction start;//esto debe ser de metodo principal
    /**
     * apontador para a no que esta a ser executado
     */
    protected NodeInstruction nodeExecute;
    /**
     * indicador se o texto pertence a um comentario do programa
     */
    protected boolean isComented = false;
    /**
     * Constroi um fluxograma
     *
     * @param code programa fonte
     * @throws Portugol.Language.Utils.LanguageException excepcao
     */
    public Bloque(/*String code*/) throws LanguageException {
        start = null;
        type = -1;
    }

    /**
     * retorna o inicio do fluxograma
     *
     * @return no onde começa o fluxograma
     */
    public NodeInstruction getStartNode() {
        return start;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                              ---------------------------
//------------  E X E C U C A O   D A   I N S T R U C A O   ---------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * Executa uma linha de codigo - normalmente
     */

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