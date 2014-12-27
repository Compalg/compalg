/*
 * Augusto Bilabila - 17-12-2011
 * 
 * Linhas
 */
package Editor.Utils;

import java.awt.*;


import javax.swing.*;
import javax.swing.JViewport;
import javax.swing.border.AbstractBorder;

 	public class LinhaEditor extends AbstractBorder {  
	 	    private int lineHeight = 22;  
	 	    private int characterHeight = 10;  
	 	    private int characterWidth = 10;  
	 	    private Color myColor;  
	 	    private JViewport viewport;  
	 	      
	 	    public LinhaEditor() {  
	 	        this.myColor = new Color(0, 0, 102);  
	 	    }  
	 	      
	    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {  
	 	        if (this.viewport == null) {  
	 	            searchViewport(c);  
	 	        }  
	 	          
	 	        Point point;  
	         Dimension d = null;  
	 	        if (this.viewport != null) {  
	 	            point = this.viewport.getViewPosition();              
	 	        } else {  
	 	            point = new Point();  
	 	        }  
	 	          
	 	        Color oldColor = g.getColor();  
	 	        g.setColor(this.myColor);  
	 	          
	 	        double r = (double) height / (double) this.lineHeight;  
	 	        int rows = (int) (r + 0.5);  
	 	        String str = String.valueOf(rows);  
	 	        int maxLenght = str.length();  
	 	          
	  	        int py;  
	 	        int i = 0;  
	 	        if (point.y > 0) {  
	 	            i = point.y / this.lineHeight;  
	 	        }  
	 	          
	 	        if (d != null){ 
	 	        	r = (double) d.height / (double) this.lineHeight;  
	 	            rows = (int) (r + 0.5);  
	 	            rows += i + 1;  
	 	        }  
	 	        int lenght;  
	 	        int px;  
	 	        for( ; i < rows; i++) {            
	 	            str = String.valueOf(i + 1);  
	             lenght = str.length();  
	 	            lenght = maxLenght - lenght;  
	 	              
	 	            py = this.lineHeight * i + 14;  
	             px = this.characterWidth * lenght + 2;  
	              
	 	            g.drawString(str, px, py);  
	 	        }         
	 	          
	 	        int left = this.calculateLeft(height) + 7;  
	 	        //left += point.x;  
	 	        g.drawLine(left, 0, left, height);  
	 	          
	 	        g.setColor(oldColor);  
	 	    }  
	        
	 	    public Insets getBorderInsets(Component c) {  
	  	        int left = this.calculateLeft(c.getHeight()) + 10;  
	 	        return new Insets(1, left, 1, 1);  
	 	    }  
	 	      
	 	    public Insets getBorderInsets(Component c, Insets insets) {           
	 	        insets.top = 1;  
	 	        insets.left = this.calculateLeft(c.getHeight()) + 10;  
		        insets.bottom = 1;  
	 	        insets.right = 1;  
	 	        return insets;  
	 	    }  
	 	      
	 	    protected int calculateLeft(int height) {  
	 	        double r = (double) height / (double) this.lineHeight;  
	 	        int rows = (int) (r + 0.5);  
	 	        String str = String.valueOf(rows);  
	 	        int lenght = str.length();  
	 	  
	         return this.characterHeight * lenght;  
	 	    }  
	 	      
	 	    protected void searchViewport(Component c) {  
	 	        Container parent = c.getParent();  
	 	        if (parent instanceof JViewport) {  
	 	            this.viewport = (JViewport) parent;  
	 	        }  
	 	    }  
	 	      
	 	    public static JScrollPane obterScrolPane(JTextPane area, JScrollPane scroll) {  
	 	     
	 	        area.setBorder(new LinhaEditor());     
	 	        scroll.setMinimumSize(new java.awt.Dimension(400, 300));
                        scroll.setPreferredSize(new java.awt.Dimension(400, 450)); 
                        scroll.setViewportView(area);               
	 	        return scroll;  
	  	    }  
	 	      
	}  