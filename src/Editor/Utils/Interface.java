/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor.Utils;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 *
 * @author Zenildo Pinto
 */
public class Interface extends JFrame{
    
    Interface()
    {
        super(" Comp Alg");
        setSize(450,370);
        
        Container panel = getContentPane();
        panel.setLayout(null);
                
        // ATENÇÃO BOTOES
        retroceder = new JButton(); panel.add(retroceder);
        retroceder.setBounds(145, 35, 35, 35);
        retroceder.setIcon(new ImageIcon("CompAlg\\ret.PNG"));
        retroceder.setFocusable(false); retroceder.setEnabled(false);
        retroceder.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e)
        {
               obj_RA.eventoRetroceder();
        }});
        
        avancar  = new JButton(); panel.add(avancar);
        avancar.setBounds(180, 35, 35, 35);
        avancar.setIcon(new ImageIcon("CompAlg\\ava.PNG"));
        avancar.setFocusable(false); avancar.setEnabled(false);
        avancar.addActionListener(new ActionListener(){@Override public void actionPerformed(ActionEvent e) 
        {
               obj_RA.eventoAvancar();
        }});
        //______________________________________________________________________
        
        
        tela = new JTextArea();
        tela.setLineWrap(true);
        tela.setWrapStyleWord(true);
        tela.requestFocus();
        tela.setFocusable(true);
        JScrollPane roll = new JScrollPane(tela,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); panel.add(roll);
        roll.setBounds(50, 70, 300, 250);
       
        obj_RA = new Botoes_nav(); // classe com os metodos de avançar e retroceder
        obj_RA.tempo.start();
        
        setResizable(false);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    public static void main(String[]args)
    {
        UIManager.LookAndFeelInfo look[]=UIManager.getInstalledLookAndFeels();
        try{UIManager.setLookAndFeel(look[3].getClassName());
        }
        catch(Exception er){}
        
        Interface app = new Interface();
        app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
    
    JButton retroceder,avancar;
    JTextArea tela;
    Botoes_nav obj_RA;
    
    // class com as operações ______________________
    
    public class Botoes_nav{
    
    Botoes_nav()
    {
        // evento de gravação
         conteudo[0]=tela.getText();
         
           ActionListener gravar = new ActionListener(){public void actionPerformed (ActionEvent e)
           {
               
               if (!tela.getText().equals(conteudo[cont]))
               {     
                             if (cont==limite)  
                             {cont=0; girou=true;}     // se estamos no limite de armazenamento retorna apangando os ultimos valores graudados
                                                           
                        cont++;     // dou uma nova posicão de armazenamento
                        inicio=cont;
                        fim=cont;
                        retroceder.setEnabled(true);
                        avancar.setEnabled(false);
                        conteudo[cont]=tela.getText();   // quardo novo conteudo
                       
               }
           }};
       
                tempo=new Timer(750,gravar); 
    }
    
                public void eventoRetroceder()
                {
                      tempo.stop();
                      if ((cont==1)&&(girou==true))  
                       cont=limite+1;  // +1 ajuste porque logo abaixo sera descrementado
                      
                      cont--;
                      tela.setText(conteudo[cont]);  
                     // System.out.println(cont+" retroceder "+ inicio);
                      desabilitarInicio(inicio); // controlo no retrov
                      tempo.start();
                 }
                    
                public void desabilitarInicio(int param)
                {
                    if (girou==false)
                    {param=0;inicio=0;}
                    if (param==limite) //(cont==param+1)||
                     {param=1;inicio=1;}
                    if ((cont==param)||(girou==true)&&(cont==param+1)) // +1 para no ultimo alteração possivel e a apenas = caso param é o limite
                      {
                        retroceder.setEnabled(false);
                        //girou=false;
                      }
                    else 
                        retroceder.setEnabled(true);
               
                     avancar.setEnabled(true);
                }
                
                public void eventoAvancar()
                {
                        tempo.stop();
                        if ((cont==limite)&&(girou==true))
                            cont=0;
                        
                        cont++;
                        tela.setText(conteudo[cont]);
                      //  System.out.println(cont+ " avanco "+ fim);
                        desabilitarFim(fim);
                        retroceder.setEnabled(true);
                        tempo.start();  
                }
                
                private void desabilitarFim(int param)
                {
                    if (cont==param)
                        avancar.setEnabled(false);
                }
                
                 private int cont=0,inicio=0,fim=0,critical=-1;  
                 private final int limite =1000; // quantidade de palavras que vai armazenar por vez
                 private boolean girou=false;
    private String[] conteudo= new String[limite+2];  //  mais dois porque começamos  o vecto com 0 e a primeira posicão ñ utilizamos porque consideramos o estado inicial           
    private Timer tempo;
}
}
