package Editor.Utils;

import Portugol.Language.Analisador.SymbolComposto;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.table.*;

import java.awt.Dimension;
import java.awt.Component;
import java.awt.Graphics;

/**
 * This example shows how to create a simple JTreeTable component, by using a
 * JTree as a renderer (and editor) for the cells in a particular column in the
 * JTable.
 *
 * @version %I% %G%
 *
 * @author Philip Milne
 * @author Scott Violet
 */
public class VRTreeCellRenderer extends DefaultTreeCellRenderer {

    public Component getTreeCellRendererComponent(JTree tree, Object value,
            boolean selected, boolean expanded, boolean leaf, int row,
            boolean hasFocus) {
        if (value instanceof InstanciaSubrutineNode) {
            this.setLeafIcon(((InstanciaSubrutineNode) value).getIcon());
            this.setOpenIcon(((InstanciaSubrutineNode) value).getIcon());
            this.setClosedIcon(((InstanciaSubrutineNode) value).getIcon());
        }
        //} else if (value.getClass() == Stream.class) {
        //  this.setLeafIcon(new ImageIcon(MainApp.gifIconPath
        //        + "corrente.gif"));
        //}
        ;
        return super.getTreeCellRendererComponent(tree, value, selected, expanded,
                leaf, row, hasFocus);
    }
}
