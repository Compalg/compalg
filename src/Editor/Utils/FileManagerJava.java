/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor.Utils;

/**
 * @author Augusto Bilabila
 */
import java.io.*;
import java.awt.*;
import javax.swing.*;

public class FileManagerJava {
    
    
    private String fileName;
    private String path = System.getProperty("user.dir");
    private String noName = "nome_classe";
    private final static String EXTENSION = "java",EXTENSION_1 = "c";
    
    /** Creates a new instance of FileManager */
    public FileManagerJava() {
        fileName = noName + "." + EXTENSION ;
    }
//-------------------------------------------------------------------------------
    public void clearFileName(){
        fileName = noName +"." + EXTENSION;
    }
    
//-------------------------------------------------------------------------------
    public String getFileName(){
        return fileName;
    }
      // Para o Java
     public void setFileName(String nome){
        fileName = nome +"." + EXTENSION;
    }
    // Para o C
     public void setFileName1(String nome){
        fileName = nome +"." + EXTENSION_1;
    }
    public boolean saveFileUpdate(String codeText){
        
        try{
            FileOutputStream fo = new FileOutputStream( getFileName() , false);
            fo.write( codeText.getBytes() );
            fo.flush();
            fo.close();
        } catch(IOException e){
            System.out.println("FileManager:SaveFileUpdate\n" + e.getMessage() );
            return(false);
        }
        return( true );
    }
    

    
}

