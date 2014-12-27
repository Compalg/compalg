

package Portugol.Language.Analisador;

import Portugol.Language.Calcular.Calculador;
import Portugol.Language.Criar.NodeInstruction;
import Portugol.Language.Utilitario.IteratorString;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;

/**
 * @author Augusto Bilabila(2011) Original de Antonio manso(2006)
 */

public class Variavel {
    
    private static String caracteres_aceites =
            "abcdefghijklmnopqrstuvxywz"+
            "ABCDEFGHIJKLMNOPQRSTUVXYWZ"+
            "._0123456789";
    
    
    public static boolean isNameAcceptable(String nameVar){
        
        String name = nameVar.trim();
        
        if(name.length()==0) return false;
        if(Character.isDigit( name.charAt(0))) return false;
        
        for(int i=1; i< name.length() ; i++)
            if(  caracteres_aceites.indexOf(name.charAt(i)) == -1)
                return false;
        
        if( Keyword.IsKeyword(nameVar) || Calculador.IsCalculus(nameVar))
            return false;
        
        
        return true;
    }
    
    /**
     * calcula o erro do nome da variavel
     * @param nameVar nome da variavel
     * @return causa do erro
     */
    public static String getErrorName(String nameVar){
        String name = nameVar.trim();
        if(name.length()==0)
            return " O NOME NÃO PODE SER VAZIO ";
        if(Character.isDigit( name.charAt(0)))
            return " O NOME DE UMA VARIÁVEL NÃO PODE COMEÇAR COM UM NÚMERO ";
        if (Keyword.IsKeyword(nameVar))
            return name + ", NÃO PODE SER O NOME DE UMA VARIÁVEL, PORQUE É UMA PALAVRA RESERVADA!!! ";
        if (Calculador.IsCalculus(nameVar))
            return name + " É UM ELEMENTO DE CÁLCULO ";
        
        if(name.indexOf("=") != -1)
            return "O SINAL \"=\" DEVE SER SUBSTITUÍDO POR \"<-\" (SINAL DE ATRIBUÍÇÃO DO PSEUDOCODIGO) ";
        
        for(int i=1; i< name.length() ; i++)
            if( caracteres_aceites.indexOf(""+name.charAt(i)) == -1)
                return  name + " CONTÉM O CARACTER \"" + name.charAt(i) + "\" NÃO É VÁLIDO ";
        
        return "ERRO NO NOME";
    }
    
    // PARA REGISTO
     public static String getErrorNameRegisto(String nameVar){
        String nome = nameVar.trim();
        if(nome.length()==0)
            return " O NOME NÃO PODE SER VAZIO ";
        if(Character.isDigit( nome.charAt(0)))
            return " O NOME DE UM REGISTO NÃO PODE COMEÇAR COM UM NÚMERO ";
        if (Keyword.IsKeyword(nameVar))
            return nome + ", NÃO PODE SER O NOME DE UM REGISTO, PORQUE É UMA PALAVRA RESERVADA!!! ";
        if (Calculador.IsCalculus(nameVar))
            return nome + " É UM ELEMENTO DE CÁLCULO ";
        
        for(int i=1; i< nome.length() ; i++)
            if( caracteres_aceites.indexOf(""+nome.charAt(i)) == -1)
                return  nome + " CONTÉM O CARACTER \"" + nome.charAt(i) + "\" NÃO É VÁLIDO ";    
        return "ERRO NO NOME";
    }
    
    //-------------------------------------------------------------------------
    public static Simbolo getVariable(String name, Vector memory){
       //     return null;
        String varName = name.trim();
        for( int index = memory.size()-1 ; index >=0 ; index--){
            Simbolo v = (Simbolo) memory.get(index);
            if (v.nameEqual(varName)){
                return v;
            }
        }
        return null;
    }
    
    
    public static void defineVAR(NodeInstruction node, Vector memory)throws LanguageException{
        if( SymbolArray.isArray( node.GetText()))
            Variavel.defineArray(node,memory);
        else
            Variavel.defineSimples(node,memory);
    }
    
    //------------------------------------------------------------------------
    public static void defineSimples(NodeInstruction node, Vector memory)throws LanguageException{
        // VARIAVEL TIPO VAR <- VALOR
        IteratorString tok = new IteratorString(node.GetText());
        String modif = tok.current(); tok.next();
        String tipo = tok.current() ; tok.next();
        String nome = tok.current() ; tok.next();
        String atribui = tok.current() ; tok.next();
        String valor = tok.getUnprocessedString();

        try {
            valor = Expressao.Evaluate(valor,memory);
        } catch( Exception e){
            throw new  LanguageException(
                    node.GetCharNum(), node.GetText() ,
                    e.toString(),
                        "VERIFIQUE A EXPRESSÃO <" +  valor + ">");
        }
        Simbolo v = new Simbolo(modif,tipo, nome,valor, node.GetLevel());
        memory.add(v);
    }
    
    //=========================================================================
    public static void defineArray(NodeInstruction node, Vector memory)throws LanguageException{
        // VARIAVEL TIPO VAR <- VALOR
        IteratorString tok = new IteratorString(node.GetText());
        String modif = tok.current(); tok.next();
        String type = tok.current() ; tok.next();
        String name = tok.current() ; tok.next();
        
        String rest = tok.getUnprocessedString();
        int  atr = rest.indexOf(Keyword.ATRIBUI);
        String indexes = rest.substring(0,atr).trim();
        String value = rest.substring(atr + Keyword.ATRIBUI.length()).trim();
        
        SymbolArray v = new SymbolArray(modif,type, name, indexes,value, node.GetLevel(),memory);
        memory.add(v);
    }
    
//------------------------------------------------------------------------------
    
    
  //-----------------------------------------------------------------------------
    public static void replaceVariableValue(String varName, String newValue, Vector memory)throws LanguageException{
        Simbolo var = Variavel.getVariable(varName.trim(), memory);
        if( var == null)
              throw new LanguageException(0, varName, " A VARIÁVEL \""+ varName + "\" NÃO ESTÁ DEFINIDA ",
                    " POR FAVOR, VERIFIQUE O NOME DA VARIÁVEL");
        if(var instanceof SymbolArray){
            ((SymbolArray) var).SetIndex(varName,memory);
        }
        
        var.setValue(newValue);
    }
}
