/*
 * AboutThis.java
 *
 */

package Editor.GUI.Sobre;

import Editor.GUI.Dialogo.Message;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;


public class AboutThis extends javax.swing.JFrame {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";
    
    /** Creates new form AboutThis */
    public AboutThis() {
        initComponents();
         // coloca uma figura na barra de título da janela  
        URL url = this.getClass().getResource("monitor_48.png");  
        Image imagemTitulo = Toolkit.getDefaultToolkit().getImage(url);  
        this.setIconImage(imagemTitulo);
        this.setBounds(200, 100, 600, 420);
    }
    
   
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btOK = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Acerca de . . .");
        setBackground(new java.awt.Color(153, 255, 255));
        setResizable(false);
        getContentPane().setLayout(null);

        btOK.setBackground(new java.awt.Color(255, 255, 255));
        btOK.setFont(new java.awt.Font("Pristina", 0, 24)); // NOI18N
        btOK.setForeground(new java.awt.Color(255, 51, 51));
        btOK.setText("OK");
        btOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btOKActionPerformed(evt);
            }
        });
        getContentPane().add(btOK);
        btOK.setBounds(460, 350, 100, 40);

        jScrollPane1.setViewportBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new java.awt.Color(204, 204, 255));
        jTextPane1.setFont(new java.awt.Font("Simplified Arabic Fixed", 0, 14)); // NOI18N
        jTextPane1.setForeground(new java.awt.Color(0, 0, 102));
        jTextPane1.setText(".................................................................\n.\t    COMPILADOR DE ALGORITMOS VERSÃO Beta\t          .\n.................................................................\n\nESTE SOFTWARE FOI DESENVOLVIDO, PARA SERVIR DE FERRAMENTA DE ESTUDO \nDA PROGRAMAÇÃO. \nCRIADA EM DEZEMBRO DE 2011 PELO AUTOR AUGUSTO MANUEL BILABILA\nATUALIZADA EM 2012 POR AUGUSTO MANUEL BILABILA. \nATUALIZADA EM 2014 POR DAVID SILVA BARRERA. \n\nO COMPILADOR RECONHECE A LINGUAGEM PORTUGOL, QUE, QUANDO BEM ES-\nCRITA PODE SER CONVERTIDA NA LINGUAGEM C E/OU EM JAVA. \n\nDISPONÍVEL NO GITHUB: https://github.com/Compalg/compalg/\nCOMUNIDADE: http://www.compalg.org/\n\nAUGUSTO MANUEL BILABILA (augustobilabila@gmail.com)\nDAVID SILVA BARRERA (dsbarrera@gmail.com)");
        jScrollPane1.setViewportView(jTextPane1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(20, 10, 560, 330);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btOKActionPerformed
     this.dispose();
    }//GEN-LAST:event_btOKActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AboutThis().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btOK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables
    
}
