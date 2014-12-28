package Portugol.Language.Utilitario;

public class IteratorCodeParams {
    
    /**
     * string a iterar
     */
    protected String str;
    /**
     * ponteiro para o inicio
     */
    protected int begin;
    /**
     * ponteiro para o fim
     */
    protected int end;
    private char STR = '\"';
    private  String BLANK = " \t\n\r";
    private  String SEPAR = ",";
    
    // ignorar o que esta dentro destes simbolos
    // por exemplo as funcoes com os parentesis
    private  String OPEN_STAT = "{([";
    private  String CLOSE_STAT = "})]";
    
    /**
     * construtor
     */
    public IteratorCodeParams() {
        begin=0;
        end=-1;
    }
    
    /**
     * construtor
     * @param msg string a iterar
     */
    public IteratorCodeParams(String msg) {
        begin=0;
        end=-1;
        str=msg;
        next();
    }
    
     /**
     * construtor
     * @param msg string a iterar
     */
    public IteratorCodeParams(String msg, String separators) {
        begin=0;
        end=-1;
        SEPAR = separators;
        str=msg;
        next();
    }
    
    /**
     * construtor
     * @param msg string a iterar
     */
    
    private boolean isOpenStats(char ch){
        return  OPEN_STAT.indexOf(ch) != -1;
    }
    
    private boolean isCloseStats(char ch){
        return  CLOSE_STAT.indexOf(ch) != -1;
    }
    private boolean isBlank(char ch){
        return  BLANK.indexOf(ch) != -1;
    }
    
    private boolean isSeparator(char ch){
        return  SEPAR.indexOf(ch) != -1;
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
     * retorna o elemento corrent e passa para o proximo
     * @return elemento corrente
     */
    public String getNext(){
        String tmp= current();
        next();
        return tmp.trim();
    }
    
    /**
     * avan�a para o proximo elemento
     */
    public void next(){
        begin= end+1;
        //passar os caracteres  brancos e a virgula
        while( begin  < str.length() && ( isBlank(str.charAt(begin)) || isSeparator(str.charAt(begin)) ) )
            begin++;
        //calcular o fim
        end = begin;
        boolean isString = false;
        int parentesis = 0; // numero de parentesis
        char anterior , ch=' '; // caracter anterior
        while( end  < str.length()){
            anterior = ch;
            ch = str.charAt(end);
            // sair se apanhar um separador fora da string e dos parentesis
            if( (isSeparator(ch) /*|| isBlank(ch)*/) && !isString && parentesis==0) //David: Aqui fue necesario tener en cuenta el espacio, para casos como: "hola"  , a,b,c
                break;
            //fim e inicio da string
            else if(ch == STR && anterior != '\\' )
                isString = !isString;
            //parentesis
            else if( isOpenStats(ch) && !isString)
                parentesis ++;
            else if( isCloseStats(ch)  && !isString)
                parentesis --;
            end++;
        }//while
    }
    
    /**
     * teste
     * @param args args
     */
    public static void main(String[] args) {
        System.out.println("TESTE ITERATOR");
        String test = "  seno( 12,23)     ,     coseno( 1,2,3) , \" era , , ((( \" , potencia(2,1) " ;
        IteratorCodeParams it = new IteratorCodeParams(test);
        while( it.hasMoreElements())
            System.out.println( it.getNext());
             
    }
    
}

