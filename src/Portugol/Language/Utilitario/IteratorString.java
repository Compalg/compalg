package Portugol.Language.Utilitario;

public class IteratorString {
    
    /**
     * string a iterar
     */
    protected String str;
    /**
     * ponteiro para o inicio do elemento
     */
    protected int begin;
    /**
     * ponteiro para o fim do elemento
     */
    protected int end;
    /**
     * string com os caracters separadores
     */
    protected static String SEPARATOR = " ,\t";
    protected char STR = '\"';
    
    /**
     * construtor
     */
    protected IteratorString(){
        begin=0;
        end=-1;
    }
    /**
     * constroi o iterador
     * @param msg texto a iterar
     */
    public IteratorString(String msg) {
        begin=0;
        end=-1;
        str=msg;
        next();
    }
    
    /**
     * verifica se existem mais elementos
     * @return verifica se existem mais elementos
     */
    public boolean hasMoreElements(){
        return  str!= null && begin < str.length();
    }
    
    /**
     * retorna o elemento corrente
     * @return retorna o elemento corrente
     */
    public String current(){
        String tmp="";
        for(int i=begin; i< end; i++)
            tmp += str.charAt(i);
        
        return tmp.trim();
    }
    
    /**
     * avanca para o proximo elemento
     */
    public void next(){
        begin= end+1;
        //passar po cima dos separadores
        while( begin  < str.length() &&  SEPARATOR.indexOf(str.charAt(begin))>=0 ) 
            begin++;        
        
        //strings
        if(begin  < str.length() && str.charAt(begin)== STR){
            end = begin+1;
            while( end  < str.length() ){
                //quebrar o ciclo 
                if( str.charAt(end) == STR && str.charAt(end-1) != '\\') 
                   break;
                 
                 end++;
            }
            //se encontrar o \"
            if(end < str.length() && str.charAt(end) ==STR)
               end++; //passar o "
            // senao e um ERRO - String nao terminada
            
            
        } else{
            end = begin;
            while( end  < str.length() &&  SEPARATOR.indexOf(str.charAt(end))==-1 ) // str.charAt(end)!= ' ')
                end++;
        }
    }
    
    
   public String getProcessedString(){
        String tmp="";
        for(int i=0; i<begin; i++)
            tmp += str.charAt(i);
        
        return tmp.trim();
    }

    public String getUnprocessedString(){
        String tmp="";
        for(int i=begin; i< str.length(); i++)
            tmp += str.charAt(i);
        
        return tmp.trim();
    }
    
}
