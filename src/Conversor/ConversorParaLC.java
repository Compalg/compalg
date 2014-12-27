/*
 * ConversorParaLC.java
 *
 * Created on Oct 4, 2011, 10:32:36 PM
 */
package Conversor;

import Editor.Utils.FileManagerJava;
import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;

/**
 *
 * @author hp
 */
public final class ConversorParaLC extends javax.swing.JDialog {
 
   // gestor de ficheiros
    private FileManagerJava fileManager;
    private String nome = "sem nome";
    
    private static String caracteres_aceites =
            "abcdefghijklmnopqrstuvxywz"+
            "ABCDEFGHIJKLMNOPQRSTUVXYWZ"+
            "_";
    
    /** Creates new form ConversorParaLC */
    public ConversorParaLC(java.awt.Frame parent, boolean modal, String codogoJava) {
        super(parent, modal);
        initComponents();
        iniciaMyConp();
              
        // Limpa antes o Codigo em Java no Text Pane
        limpa(jTextPane1);
        jTextPane1.setText(codogoJava);
    }
    
    public static void limpa(JTextPane pane){
         // Adiciona o Codigo em C no Text Pane
         pane.setText("");
    }
        
     public void iniciaMyConp(){
         fileManager = new FileManagerJava();
          // coloca uma figura na barra de título da janela  
        URL url = this.getClass().getResource("monitor_48.png");  
        Image imagemTitulo = Toolkit.getDefaultToolkit().getImage(url);  
        this.setIconImage(imagemTitulo);
    }        
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        btcancelar = new javax.swing.JButton();
        btguardar = new javax.swing.JButton();
        btcopiar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Programa Java");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(0, 0, 0));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Conversor/C1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(24, 24, 24))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTextPane1.setBackground(new java.awt.Color(0, 0, 0));
        jTextPane1.setEditable(false);
        jTextPane1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jTextPane1.setForeground(new java.awt.Color(255, 255, 255));
        jScrollPane1.setViewportView(jTextPane1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));
        jPanel2.setForeground(new java.awt.Color(0, 0, 51));

        btcancelar.setBackground(new java.awt.Color(102, 153, 255));
        btcancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Conversor/cancelar.png"))); // NOI18N
        btcancelar.setToolTipText("Volta para o programa em Portugol");
        btcancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btcancelarActionPerformed(evt);
            }
        });

        btguardar.setBackground(new java.awt.Color(102, 153, 255));
        btguardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Conversor/guardar.png"))); // NOI18N
        btguardar.setToolTipText("Guarda a classe");
        btguardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btguardarActionPerformed(evt);
            }
        });

        btcopiar.setBackground(new java.awt.Color(102, 153, 255));
        btcopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Conversor/copiar.png"))); // NOI18N
        btcopiar.setToolTipText("Copia o programa para área de transferência");
        btcopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btcopiarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btguardar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btcancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(btcopiar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 261, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(71, 71, 71)
                .addComponent(btcopiar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
                .addComponent(btcancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 567, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 576, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btcancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btcancelarActionPerformed
        // TODO add your handling code here:
        jTextPane1.setText("");
        dispose();
    }//GEN-LAST:event_btcancelarActionPerformed

    private void btcopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btcopiarActionPerformed
        // TODO add your handling code here:
        jTextPane1.selectAll(); // Seleciona tudo
        jTextPane1.copy();  // Copia para area de transferencia
        JOptionPane.showMessageDialog(null, "O programa foi colocado na área de transferência,\nagora basta apenas escolher um lugar para colar");
    }//GEN-LAST:event_btcopiarActionPerformed

    private void btguardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btguardarActionPerformed
        // TODO add your handling code here:
        GuardarFicheiro( fileManager.getFileName());
    }//GEN-LAST:event_btguardarActionPerformed

    /**
     * @param args the command line arguments
     */
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btcancelar;
    private javax.swing.JButton btcopiar;
    private javax.swing.JButton btguardar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextPane jTextPane1;
    // End of variables declaration//GEN-END:variables

public void GuardarFicheiro(String fileName) {
        //se conseguir salvar
        int confirma = JOptionPane.showConfirmDialog(rootPane, "Deseja mesmo salvar esta classe");
        //JOptionPane.showMessageDialog(null, confirma);
        if(confirma == 0){
            fileManager.setFileName1(nome);
            if( fileManager.saveFileUpdate(jTextPane1.getText()) ){
            this.setTitle( "A classe ja foi guardada como" + " - " + nome +".c");
        }
        }
        
    }
}
