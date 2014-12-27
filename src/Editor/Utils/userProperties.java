// Original de Antonio Manso, Modificado por Augusto Bilabila 
package Editor.Utils;

import java.util.*;
import java.io.*;

public class userProperties {
    
    private String propertiesFileName;
    private Properties uProperties;
    private boolean loaded;
    
    /** Creates a new instance of userProperties */
    public userProperties() {
        propertiesFileName = "/Editor.defs"; // default properties filename
        loaded = false;
        initProperties(); // init class
    }
    
    public userProperties(String pFileName) {
        this.propertiesFileName = pFileName; // paremeter ser properties filename
        loaded = false;
        initProperties(); //init class
    }
    
    public String getPropertiesFileName(){
        return( propertiesFileName );
    }
    
    public void setPropertiesFileName(String pFileName)
    {
        this.propertiesFileName = pFileName;
    }
    
    public boolean isLoaded()
    {
        return( this.loaded );
    }
    
    
    private void initProperties()
    {
        uProperties = new Properties();
        try{
           
            String path = getClass().getProtectionDomain().getCodeSource().
                    getLocation().toString().substring(6);
            path = path.replaceAll("%20", " ");
            FileInputStream fis = new FileInputStream( path  +  propertiesFileName );
            uProperties.load( fis  ); 
            loaded = true;
            fis.close();
        }catch(FileNotFoundException e){ System.out.println("Ficheiro não encontrado: " + propertiesFileName + "\n" + e.getMessage()); }
        catch(IOException e){ System.out.println("Erro enquanto tentava carregar as propriedades.\n" + e.getMessage() );}
    }
    
    
    public String getPropertie(String key)
    {
        if( !isLoaded() || key==null ) return( null );
        return( uProperties.getProperty( key ) );
    }
    
    public boolean containsValue(Object value)
    {
        return( uProperties.containsValue( value ) );
    }
    
    public void setPropertie( String key, String value )
    {
        uProperties.setProperty( key, value );
       
    }
    
    public boolean saveProperties()
    {
        try{
            
            String path = getClass().getProtectionDomain().getCodeSource().
                    getLocation().toString().substring(6);
            path = path.replaceAll("%20", " ");
            System.out.println("[SAVE] Relative path: " + path + this.propertiesFileName  );
            FileOutputStream out = new FileOutputStream( path + this.propertiesFileName  );
            uProperties.store( out, null );
            out.close();
            return( true );
        }catch(FileNotFoundException e){ System.out.println("Ficheiro não encontrado: "  + this.propertiesFileName + "\n" + e.getMessage() );}
        catch(IOException e) {System.out.println("Erro ao salvar o ficheiro");}
        return( false );
    }
    
    public int size()
    {
        return( uProperties.size() );
    }
    
    public Enumeration getKeys()
    {    
        return( uProperties.keys() );
    }
    
    
    
}
