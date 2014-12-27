package Portugol.Language.Utilitario;

import java.util.Stack;


public class Parentesis {
    /**
     * parentesis que abrem
     */
    protected static String parOpen     ="([{";
    /**
     * parentesis que fecham
     */
    protected static String parClose    =")]}";
    
    
    /**
     * verifica se is parentesis
     * @param ch caracter a verificar
     * @return verdadeiro se for um paratesis
     */
    public static boolean IsParentesis(char ch){
        return parOpen.indexOf(ch)!= -1 || parClose.indexOf(ch) != -1;
    }
    
    /**
     * verifica se is parentesis
     * @param ch string a verificar
     * @return verdadeiro se for um parentesis
     */
    public static boolean IsParentesis(String ch){
        return parOpen.indexOf(ch)!= -1 || parClose.indexOf(ch) != -1;
    }

    
    /**
     * verifica se dois parentesis casam
     * @param open parentesis a abrir
     * @param close parentesis a fechar
     * @return verdadeiro se forem complementares
     */
    protected static boolean Match(char open, char close){
        return parOpen.indexOf(open)== parClose.indexOf(close);
    }
    
    /**
     * verifica se uma string tem os parentesis na ordem correcta
     * @param expr string a verificar
     * @return verdadeiro se os parentesis estiverem correctos
     */
    public static boolean Verify(String expr){
        Stack s = new Stack();
        for(int i=0; i< expr.length(); i++){
            char ch = expr.charAt(i);
            if(parOpen.indexOf(ch) != -1)
                       s.push(""+ch);
            else if(parClose.indexOf(ch) != -1){
                if( s.empty()) 
                    return  false;
                char ch2 = ((String)s.pop()).charAt(0);
                if( ! Match( ch , ch2))
                    return  false;
            }            
        }
        if( ! s.empty())
            return false;
        return true;                    
    }
    
    /**
     * calcula o erro de parentesis numa expressao
     * @param expr expressao a verificar
     * @return descricao do erro
     */
    public static String GetError(String expr){
        Stack s = new Stack();
        for(int i=0; i< expr.length(); i++){
            char ch = expr.charAt(i);
            if(parOpen.indexOf(ch) != -1)
                s.push(""+ch);
            else if(parClose.indexOf(ch) != -1){
                if( s.empty())
                    return  " PPARENTESIS " + ch  + "NAO ESTA ABERTO";
                char ch2 = ((String)s.pop()).charAt(0);
                if( ! Match( ch , ch2))
                    return  " PARENTESIS " + ch  + " E " + ch2 + "NAO COMBINAM";
            }            
        }
        if( ! s.empty())
            return "FALTA FECHAR OS PARENTESIS: " + s.toString();        
        return "OK";                    
    }
}
