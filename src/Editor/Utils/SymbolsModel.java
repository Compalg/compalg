package Editor.Utils;

import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.SymbolComposto;
import Portugol.Language.Analisador.SymbolObjeto;
import Portugol.Language.Criar.Bloque;
import Portugol.Language.Criar.BloqueClasse;
import Portugol.Language.Criar.BloqueSubrutine;
import Portugol.Language.Criar.InstanciaSubrutine;
import java.io.File;
import java.util.Date;
import java.util.Vector;
import javax.swing.Icon;

/**
 * FileSystemModel is a TreeTableModel representing a hierarchical file system.
 * Nodes in the FileSystemModel are FileNodes which, when they are directory
 * nodes, cache their children to avoid repeatedly querying the real file
 * system.
 *
 * @version %I% %G%
 *
 * @author Philip Milne
 * @author Scott Violet
 */
public class SymbolsModel extends AbstractTreeTableModel
        implements TreeTableModel {

    // Names of the columns.
    static protected String[] cNames = {"Nome", "Tipo", "Valor"};//, "Type", "Modified"};
    // Types of the columns.
    static protected Class[] cTypes = {TreeTableModel.class, String.class, String.class, Date.class};
    // The the returned file length for directories. 
    public static final Integer ZERO = new Integer(0);

    public SymbolsModel(InstanciaSubrutine prog) {
        super(new InstanciaSubrutineNode(prog));
    }

    //
    // Some convenience methods. 
    //
    protected Object getRutina(Object node) {
        InstanciaSubrutineNode Node = ((InstanciaSubrutineNode) node);
        return Node.getBloque();
    }

    protected Object[] getChildren(Object node) {
        InstanciaSubrutineNode Node = ((InstanciaSubrutineNode) node);
        return Node.getChildren();
    }

    //
    // The TreeModel interface
    //
    public int getChildCount(Object node) {
        Object[] children = getChildren(node);
        return (children == null) ? 0 : children.length;
    }

    public Object getChild(Object node, int i) {
        return getChildren(node)[i];
    }

    // The superclass's implementation would work, but this is more efficient. 
    public boolean isLeaf(Object node) {
        InstanciaSubrutineNode Node = ((InstanciaSubrutineNode) node);
        return Node.isLeaf();
    }

    //
    //  The TreeTableNode interface. 
    //
    public int getColumnCount() {
        return cNames.length;
    }

    public String getColumnName(int column) {
        return cNames[column];
    }

    public Class getColumnClass(int column) {
        return cTypes[column];
    }

    public Object getValueAt(Object node, int column) {
        Object dato = getRutina(node);
        try {
            switch (column) {
                case 0://NOME
                    if (dato instanceof BloqueSubrutine) {
                        return ((BloqueSubrutine) dato).Nome;
                    } else if (dato instanceof SymbolObjeto) {
                        return ((SymbolObjeto) dato).getName();
                    } else if (dato instanceof SymbolComposto) {
                        return ((SymbolComposto) dato).getName();
                    } else if (dato instanceof SymbolArray) {
                        return ((SymbolArray) dato).getName();
                    } else if (dato instanceof Simbolo) {
                        return ((Simbolo) dato).getName();
                    } else {
                        throw new Exception();
                    }
                case 1://TIPO
                    if (dato instanceof InstanciaSubrutine) {
                        switch (((InstanciaSubrutine) dato).BloqueRutina.type) {
                            case Bloque.CONSTRUTOR:
                                return Keyword.GetTextKey(Keyword.CONSTRUTOR);
                            case Bloque.PROCEDIMENTO:
                                return Keyword.GetTextKey(Keyword.PROCEDIMENTO);
                            case Bloque.FUNCAO:
                                return Keyword.GetTextKey(Keyword.FUNCAO);
                            default:
                                return "";
                        }
                    } else if (dato instanceof SymbolObjeto) {
                        return "(" + ((SymbolObjeto) dato).getTypeLexema() + ")";
                    } else if (dato instanceof SymbolComposto) {
                        return "(" + ((SymbolComposto) dato).getTypeLexema() + ")";
                    } else if (dato instanceof SymbolArray) {
                        return "Vector de (" + ((SymbolArray) dato).getTypeLexema() + ")";
                    } else if (dato instanceof Simbolo) {
                        return ((Simbolo) dato).getTypeLexema();
                    } else {
                        throw new Exception();
                    }
                case 2://VALOR
                    if (dato instanceof InstanciaSubrutine) {
                        return "";
                    } else if (dato instanceof SymbolObjeto) {
                        return "";
                    } else if (dato instanceof SymbolComposto) {
                        return "";
                    } else if (dato instanceof SymbolArray) {
                        return "";
                    } else if (dato instanceof Simbolo) {
                        return ((Simbolo) dato).getValue().toString();
                    } else {
                        throw new Exception();
                    }
            }
        } catch (Exception se) {
        }

        return null;
    }
}
/* A FileNode is a derivative of the File class - though we delegate to 
 * the File object rather than subclassing it. It is used to maintain a 
 * cache of a directory's children and therefore avoid repeated access 
 * to the underlying file system during rendering. 
 */

class InstanciaSubrutineNode {

    Object dato;

    public InstanciaSubrutineNode(Object dato) {
        this.dato = dato;
    }

    /**
     * Returns the the string to be used to display this leaf in the JTree.
     */
    public String toString() {
        if (dato instanceof InstanciaSubrutine) {
            return ((InstanciaSubrutine) dato).getTextoDescriptor();
        } else if (dato instanceof SymbolObjeto) {
            return ((SymbolObjeto) dato).getName();
        } else if (dato instanceof SymbolComposto) {
            return ((SymbolComposto) dato).getName();
        } else if (dato instanceof SymbolArray) {
            return ((SymbolArray) dato).getName();
        } else if (dato instanceof Simbolo) {
            return ((Simbolo) dato).getName();
        }

        return dato.toString();
    }

    public Icon getIcon() {
        if (dato instanceof InstanciaSubrutine) {
            return ((InstanciaSubrutine) dato).getIcon();
        } else if (dato instanceof Simbolo) { //polimorfico, cada uno retorna su icon estatico en la clase
            return ((Simbolo) dato).getIcon();
        }

        return null;
    }

    public Object getBloque() {
        return dato;
    }

    public boolean isLeaf() {
        return !(dato instanceof InstanciaSubrutine)
                && !(dato instanceof SymbolComposto)
                && !(dato instanceof SymbolObjeto)
                && !(dato instanceof SymbolArray);
    }

    /**
     * Loads the children, caching the results in the children ivar.
     */
    protected Object[] getChildren() {
        if (dato instanceof InstanciaSubrutine) {
            Object[] children = new InstanciaSubrutineNode[((InstanciaSubrutine) dato).memory.size()];
            //String path = bloque.getPath();
            for (int i = 0; i < ((InstanciaSubrutine) dato).memory.size(); i++) {
                children[i] = new InstanciaSubrutineNode(((InstanciaSubrutine) dato).memory.get(i));
            }
            return children;
        } else if (dato instanceof SymbolObjeto) {
            Object[] children = new InstanciaSubrutineNode[((SymbolObjeto) dato).Campos.size()];
            //String path = bloque.getPath();
            for (int i = 0; i < ((SymbolObjeto) dato).Campos.size(); i++) {
                children[i] = new InstanciaSubrutineNode(((SymbolObjeto) dato).Campos.get(i));
            }
            return children;
        } else if (dato instanceof SymbolComposto) {
            Object[] children = new InstanciaSubrutineNode[((SymbolComposto) dato).Campos.size()];
            //String path = bloque.getPath();
            for (int i = 0; i < ((SymbolComposto) dato).Campos.size(); i++) {
                children[i] = new InstanciaSubrutineNode(((SymbolComposto) dato).Campos.get(i));
            }
            return children;
        } else if (dato instanceof SymbolArray) {
            Object[] children = new InstanciaSubrutineNode[((SymbolArray) dato).dataValues.size()];
            //String path = bloque.getPath();
            for (int i = 0; i < ((SymbolArray) dato).dataValues.size(); i++) {
                children[i] = new InstanciaSubrutineNode(((SymbolArray) dato).dataValues.get(i));
            }
            return children;
        } 

        return new InstanciaSubrutineNode[0];
    }
}
