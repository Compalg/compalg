/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Portugol.Language.Utilitario;

/**
 *
 * @author hp
 */

public class TrataDefLiteral {
    private String TipoTexto;
    private String tipo;

    public TrataDefLiteral(String tipo) {
        this.tipo = tipo;
    }

    public String getLiteral() {
        return TipoTexto;
    }

    public void setLiteral(String literal) {
        this.TipoTexto = literal;
    }
    
    public void tratar(String literal) {
        this.TipoTexto = literal;
        String st1="";
        if(tipo.equalsIgnoreCase("LITERAL"))
        if(!this.TipoTexto.contains("<-")){
            for (int i=0;i<this.TipoTexto.length();i++){ 
                char a = this.TipoTexto.charAt(i);
                
                if(a==',') st1+="<-\"\"";
                
                st1+=a;
            }
            this.TipoTexto = st1+"<-\"\"";
        }
            
    }
}
