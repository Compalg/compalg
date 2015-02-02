/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Portugol.Language.Calcular;

import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Criar.NodeInstruction;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 *
 * @author hp
 */
public class Ficheiro {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";
    String programa="";
    
    public Ficheiro(String prog) {
        programa = prog;
    }

    public String getPrograma() {
        return programa;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }
    // Trata do ficheiro
    public void trataFicheiro1(){
         
        StringTokenizer st = new StringTokenizer(programa,"\n\r");
        NodeInstruction node=null;
        String linha="";
        
        Vector instrucpro = new Vector();
                       
        while (st.hasMoreTokens()) {
            linha = st.nextToken();
            
            node = new NodeInstruction(linha,0,0);        
            
             if (node.GetType() == Keyword.FICHEIRO){
                 
                 programa = programa.replace("ficheiro ", "//ficheiro ");
             }
        }        
    
    }
}
