
package Portugol.Language.Consola;

import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;


public class ConsoleIO extends JTextArea {
    /**
     * versao do teclado
     */
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila & David Silva Barrera";
    
    /**
     * contrutor
     */
    public ConsoleIO() {
        setBackground(new Color(255, 255, 255));
        setFont(new Font("Courier New", 0, 14));
        setForeground(new Color(0, 0, 255));
        this.setLineWrap(true);
        this.setText("Nenhum algoritmo está sendo executado");
        this.setEditable(false);
    }
    /**
     * limpa a conola
     */
    public void Clear(){
        this.setText("");
    }
    
    public String read(Simbolo s) throws LanguageException{
        try{
            
            String input;
            do{
                input = JOptionPane.showInputDialog(this,"valor da variável: "+s.getName(),"CONSOLA",JOptionPane.QUESTION_MESSAGE);
            }while(input.length() <=0);
            write(input+"\n");
            if( s.getType() == s.TEXTO || s.getType() == s.CARACTER)
                return Values.StringToText(input);
            else
                return input;
        }catch (Exception e){
  
            throw new LanguageException("Leitura abortada pelo utilizador","");
        }
    }
    
    /**
     * Escreve uma string na consola
     * @param str string a escrever
     */
    public void write(String str){
        this.append(str);
        //por o cursor no final i.e. deslizar o texto
        this.setCaretPosition( this.getText().length());
    }
    
    public void setColor(Color backGround, Color text){
        setBackground(backGround);
        setForeground(text);
    }
    
    public void setNewFont( Font f) {
        setFont(f);
    }
}
