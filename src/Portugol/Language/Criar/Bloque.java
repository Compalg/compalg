package Portugol.Language.Criar;

import Portugol.Language.Analisador.Keyword;
import static Portugol.Language.Criar.BloqueRegisto.GetCode;
import Portugol.Language.Utilitario.LanguageException;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public abstract class Bloque {
     public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";
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