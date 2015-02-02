package Portugol.Language.Analisador;

import Portugol.Language.Criar.BloqueClasse;
import java.util.Vector;
/**
 * @author Augusto Bilabila (23-12-2011)
 */
public class TipoClasse {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";

    public String Name;
    public Vector Defs;
    public BloqueClasse claseOrigen;
    
    public TipoClasse (BloqueClasse co){
        Name = "";
        Defs = new Vector();
        claseOrigen = co;
    }   
   
}