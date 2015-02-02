package Portugol.Language.Analisador;

import java.util.Vector;
/**
 * @author Augusto Bilabila (23-12-2011)
 */
public class TipoRegisto {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";

    public String Name;
    public Vector Defs;
    
    public TipoRegisto (){
        Name = "";
        Defs = new Vector();
    }   
   
}