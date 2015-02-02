package Portugol.Language.Criar;

import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.TipoDeParametro;
import java.util.Vector;
import Portugol.Language.Utilitario.LanguageException;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public class BloqueSubrutine extends Bloque {

    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";
    public static String ErroRecursividad = "Detectado llamado recursivo. CompAlg no permite este llamado";
    public boolean EstaDebugeando;
    /**
     * vector dos parametros
     */
    public Vector<TipoDeParametro> parametrosDefinition;//David:
    /**
     * vector dos valoes para os parametros
     */
    public String TipoRetorno;
    public boolean EstaExecutando;
    public BloqueClasse classePae; //Se esta dentro duma classe, aqui temos a referença
    //static public SymbolObjeto InstanciaActual = null;//Se esta dentro duma classe, aqui temos a instancia actual

    //public static String VerOperator = " " ;//im
    /**
     * Constroi um fluxograma
     *
     * @param code programa fonte
     * @throws Portugol.Language.Utils.LanguageException excepcao
     */
    public BloqueSubrutine(/*String code*/) throws LanguageException {
        // Construir(code);
        start = null;
        parametrosDefinition = new Vector<TipoDeParametro>();
        EstaExecutando = false;
        classePae = null; //vamos supor que a sub-rutina não esta dentro de classe alguma
        EstaDebugeando = false;
    }

//=============================================================================
    public String RemoveComentarios(String str) {
        StringBuffer newStr = new StringBuffer();
        for (int index = 0; index < str.length(); index++) {
            switch (str.charAt(index)) {
                case '/':
                    // comentario "//"
                    if (index + 1 < str.length() && str.charAt(index + 1) == '/') {
                        return newStr.toString().trim();
                    }
                    //inicio de um comentario /*
                    if (index + 1 < str.length() && str.charAt(index + 1) == '*') {
                        isComented = true;
                    } else //fim do comentario */
                    if (index > 0 && str.charAt(index - 1) == '*') {
                        isComented = false;
                    } //introduz caracter /
                    else {
                        newStr.append(str.charAt(index));
                    }
                    break;
                default:
                    // se nao for comentario
                    if (!isComented) {
                        newStr.append(str.charAt(index));
                    }
            }
        }
        return newStr.toString().trim();
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