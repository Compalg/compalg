/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Editor.Utils;
import java.io.*;
import java.awt.*;
import javax.swing.*;
/**
 *
 * @author Bilabla 09-04-2012
 */
public class ExemploAlgoritmo {
    private String fileName;
    private String path = System.getProperty("user.dir")+"/Exemplos de Algoritmos/";
    private String noName = "Sem Nome";
    private final static String EXTENSION = "alg";
    
    /** Creates a new instance of FileManager */
    public ExemploAlgoritmo() {
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
    
//-------------------------------------------------------------------------------
    public String ReadFile(String file){
        fileName = file;
        StringBuffer text = new StringBuffer();
        String tmp="";
        try{            
            BufferedReader fi = new BufferedReader(new FileReader(fileName));
            while(true) {
                if(!fi.ready())
                    break;
                tmp = new String(fi.readLine());
                text.append(tmp +"\n");
            }
            fi.close();
        } catch(IOException e){
            text.append(" ERRO: na leitura do ficheiro\n" +  e.getMessage());
        }
        return text.toString();
    }
//-------------------------------------------------------------------------------
    public String openFileWindow(Component component ) {
        int returnValue = 0;
        try{
            JFileChooser jfc = new JFileChooser(path);
            jfc.setFileFilter(new AlgorithmFileFilter());
            returnValue = jfc.showOpenDialog( component );
            if( (returnValue == jfc.ERROR_OPTION ) || (returnValue==jfc.CANCEL_OPTION)  )
                return null;
            path = jfc.getSelectedFile().toString() ;
        }catch(java.awt.HeadlessException e){
           return "ERRO: na abertura do ficheiro\n" + e.getMessage();
        }
        return ReadFile(path);
    }
    
    
    public boolean isFileOpened() {
        return !fileName.equals(noName+"."+EXTENSION);
    }
       
   
    public boolean FileExists(String filename) {
        return( new File(filename).exists() );
    }
 

    
}

