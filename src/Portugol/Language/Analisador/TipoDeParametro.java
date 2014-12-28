package Portugol.Language.Analisador;

import java.util.Vector;
/**
 * @author Augusto Bilabila (23-12-2011)
 */
public class TipoDeParametro {

    public String Name;
    public boolean PorValor;
    public Object Tipo;
    
    public TipoDeParametro (){
        Name = "";
        PorValor = true;
        Tipo = null;
    }   
   
}