package Editor.GUI.Dialogo;

import Portugol.Language.Utilitario.LanguageException;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;


public class Message {
    
    private static final String title =  "CompAlg 1.0" ;
    private static ImageIcon icoBug = new javax.swing.ImageIcon("Bug-32.ico");     
    private static ImageIcon icoInf = new javax.swing.ImageIcon("Info_32.ico"); 
    private static ImageIcon icoWar = new javax.swing.ImageIcon("Warning_32.ico");
    
    
    public static  void Error(String msg){
       JOptionPane.showMessageDialog(null, msg, title, JOptionPane.ERROR_MESSAGE,icoBug); 
    }
    
    public static void Information(String msg){
       JOptionPane.showMessageDialog(null, msg, title,JOptionPane.INFORMATION_MESSAGE,icoInf); 
    }
    
    public static void Warning(String msg){
       JOptionPane.showMessageDialog(null, msg, title,JOptionPane.WARNING_MESSAGE,icoWar); 
    }
    
    public static int Confirm(String msg){
        return JOptionPane.showConfirmDialog(null,msg,title,JOptionPane.YES_NO_CANCEL_OPTION);
    }
    
    public static  void CompileError(LanguageException e){
        String 
              str = "INSTRUÇÃO:\n" + e.codeLine  + "\n";
              str +="ERRO:\n" + e.error  + "\n";
              str +="SOLUÇÃO:\n" + e.solution +"\n";
       
       JOptionPane.showMessageDialog(null, str, title, JOptionPane.ERROR_MESSAGE,icoBug); 
    }
    public static  void ExecutionError(String msg,LanguageException e){
        String 
              str = msg + "\n\n";
              str += "INSTRUÇÃO:\n" + e.codeLine  + "\n";
              str +="ERRO:\n" + e.error  + "\n";
              str +="SOLUÇÃO:\n" + e.solution +"\n";
       
       JOptionPane.showMessageDialog(null, str, title, JOptionPane.ERROR_MESSAGE,icoBug); 
    }
    
}
