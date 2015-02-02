package Portugol.Language.Criar;

import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolObjeto;
import java.util.Vector;
import Portugol.Language.Utilitario.LanguageException;
import javax.swing.Icon;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public class InstanciaSubrutine {

    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";
    public static String ErroRecursividad = "Detectado llamado recursivo. CompAlg no permite este llamado";
    static public Icon ClassSubrutineIcon;
    static public Icon GlobalSubrutineIcon;
    public Vector memory;
    public Vector paramsValues;//David:
    //public String TipoRetorno;
    public boolean EstaDebugeando;
    public SymbolObjeto InstanciaDono;//Se esta dentro duma classe, aqui temos a instancia actual
    public NodeInstruction NodeActual;//esto debe ser de metodo principal
    public NodeInstruction NodePrevio;//esto debe ser de metodo principal
    public BloqueSubrutine BloqueRutina;
    boolean esperarRetorno;
    public boolean EsConstructor;
    //public static String VerOperator = " " ;//im

    /**
     * Constroi um fluxograma
     *
     * @param code programa fonte
     * @throws Portugol.Language.Utils.LanguageException excepcao
     */
    public InstanciaSubrutine(BloqueSubrutine bloqueRutina, SymbolObjeto ObjetoDono, Vector paramVals) throws LanguageException {
        // Construir(code);
        memory = new Vector();
        NodeActual = bloqueRutina.getStartNode();
        NodePrevio = null;
        BloqueRutina = bloqueRutina;
        InstanciaDono = ObjetoDono;
        paramsValues = paramVals;
        esperarRetorno = NodeActual.GetType() == Keyword.FUNCAO;
        EsConstructor = NodeActual.GetType() == Keyword.CONSTRUTOR;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * limpa a memoria, os niveis de memoria. <br>
     * Utiliza-se no ciclo para limpar as variaveis <br>
     * locais ao ciclo.
     *
     * @param level nivel a partir do qual vai limpar
     */
    protected static void cleanMemory(int level, Vector memory) {//sbr
        for (int index = memory.size() - 1; index >= 0; index--) {
            Simbolo v = (Simbolo) memory.get(index);
            //elimina as variaveis superiores ou iguais ao nivel
            if (v.getLevel() >= level) {
                memory.remove(index);
            } else {
                break;
            }
        }
    }

//=============================================================================
    /**
     * string
     *
     * @return string
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        NodeInstruction pt = BloqueRutina.start;
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

    public Icon getIcon() {
        if (InstanciaDono != null) {
            return ClassSubrutineIcon;
        } else {
            return GlobalSubrutineIcon;
        }
    }

    public String getTextoDescriptor() {

        String res = EsConstructor ? "[NEW] " : (InstanciaDono != null ? "[" + InstanciaDono.getName() + "] " : "");
        res += BloqueRutina.TipoRetorno == null || EsConstructor ? "" : BloqueRutina.TipoRetorno + " ";
        res += BloqueRutina.Nome;
        res += "(";
        for (int i = 0; i < BloqueRutina.parametrosDefinition.size(); i++) {
            res += i > 0 ? ", " : "";
            res += BloqueRutina.parametrosDefinition.get(i).Tipo.toString();
            res += " ";
            res += BloqueRutina.parametrosDefinition.get(i).Name;
        }
        res += BloqueRutina.parametrosDefinition.isEmpty() ? " " : "";
        res += ")";
        return res;
    }
}