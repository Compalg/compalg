/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
public final class ConversorParaLJava extends javax.swing.JDialog {
 
   // gestor de ficheiros
    private FileManagerJava fileManager;
    private String nome = "nome_classe";
    
    private static String caracteres_aceites =
            "abcdefghijklmnopqrstuvxywz"+
            "ABCDEFGHIJKLMNOPQRSTUVXYWZ"+
            "_";
    
    /** Creates new form ConversorParaLC */
    public ConversorParaLJava(java.awt.Frame parent, boolean modal, String codogoJava) {
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
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jPanel2 = new javax.swing.JPanel();
        btcancelar = new javax.swing.JButton();
        btguardar = new javax.swing.JButton();
        btcopiar = new javax.swing.JButton();
        btmodificar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Programa Java");
        setBackground(new java.awt.Color(255, 255, 255));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Conversor/java_logo_2.png"))); // NOI18N

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Conversor/j1.png"))); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(69, Short.MAX_VALUE)
                .addComponent(jLabel2)
                .addGap(56, 56, 56))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addContainerGap(32, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTextPane1.setEditable(false);
        jTextPane1.setFont(new java.awt.Font("Times New Roman", 0, 18)); // NOI18N
        jScrollPane1.setViewportView(jTextPane1);

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)));

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

        btmodificar.setBackground(new java.awt.Color(102, 153, 255));
        btmodificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Conversor/modificar.png"))); // NOI18N
        btmodificar.setToolTipText("Muda o nome da classe");
        btmodificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btmodificarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btcopiar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(btguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btmodificar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 261, Short.MAX_VALUE)
                    .addComponent(btcancelar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 261, Short.MAX_VALUE))
                .addGap(25, 25, 25))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(btguardar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addComponent(btcopiar, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42)
                .addComponent(btmodificar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 36, Short.MAX_VALUE)
                .addComponent(btcancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 663, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(41, Short.MAX_VALUE))
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

    private void btmodificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btmodificarActionPerformed
        // TODO add your handling code here:
        int cont =0;
        do {
            String nomeClasse = JOptionPane.showInputDialog(" Digite o novo nome para a classe\n\n"
                + "Evita dar espaço, ou começar com um número.\n\n"
                + "Nota: Caso coloque um nome ja existente no seu directório\n"
                + "será automáticamente substituido!");
        
            String str = jTextPane1.getText();
            

            int ver = nomeClasse.indexOf(' ');
            if(ver != -1)
                JOptionPane.showMessageDialog(null, "O nome da classe não pode ter espaço ");

            else {
                int f=0;
                for (int i =0;i<10;i++){
                    if(nomeClasse.startsWith(""+i)){
                        f=1;
                        i=10; //interrompe o ciclo
                    }
                }
                if(f==1)JOptionPane.showMessageDialog(null, "O nome da classe não pode começar com um número ");
                
                else{
                     f =0;
                    for (int i =0;i < nomeClasse.length();i++){
                      if(caracteres_aceites.indexOf(nomeClasse.charAt(i)) == -1){
                          f =1;
                          i = nomeClasse.length(); //interrompe o ciclo
                      } 
                     }
                       if(f==1)JOptionPane.showMessageDialog(null, "O nome da classe não pode ter caracteres especiais\n"
                               + "com a excepção do underscore ( _ ) ");   
                    
                       else {
                            limpa(jTextPane1);

                            jTextPane1.setText(str.replace(nome, "class "+nomeClasse));
                            nome = nomeClasse;
                            cont = 1;
                        }
                }
            }
        }while(cont == 0);   
    }//GEN-LAST:event_btmodificarActionPerformed

    /**
     * @param args the command line arguments
     */
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btcancelar;
    private javax.swing.JButton btcopiar;
    private javax.swing.JButton btguardar;
    private javax.swing.JButton btmodificar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
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
            fileManager.setFileName(nome);
            if( fileManager.saveFileUpdate(jTextPane1.getText()) ){
            this.setTitle( "A classe ja foi guardada como" + " - " + nome +".java");
        }
        }
        
    }
}
