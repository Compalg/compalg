package Portugol.Language.Utilitario;

public class IteratorCodeLine extends IteratorString{
    
    /**
     * Creates a new instance of IteratorCodeLine
     * @param msg string a iterar
     */
    
    public IteratorCodeLine(String msg) {
        
        SEPARATOR = " \t";
        str = msg;
        next();
    }
    
    /**
     * contrutor
     * @param msg string a iterar
     * @param separ caracteres separadores
     */
    public IteratorCodeLine(String msg, String separ) {
        super(msg);
        SEPARATOR = separ;
        str = msg;
        next();
    }
    /**
     * tem de passar por cima das virgulas das fun��es com parametros
     */
    public void next(){
        begin= end+1;
        
        while( begin  < str.length() &&  SEPARATOR.indexOf(str.charAt(begin))>=0 ) // str.charAt(begin)== ' ' )
            begin++;
        
        //INDEX
        if(begin  < str.length() && str.charAt(begin)== '['){
            end = begin;
            int rect =0;
            while( end  < str.length() ){
                // contar os []
                if( str.charAt(end) =='[')  rect++;
                if( str.charAt(end) ==']')   rect--;
                if( rect == 0 )
                    break;
                end++;
            }
            //passar o ]
            end+=2;
        }
        //strings
        else if(begin  < str.length() && str.charAt(begin)== '"'){
            end = begin+1;
            while( end  < str.length() ){
                //quebrar o ciclo
                if( str.charAt(end) =='\"' && (end==0 || end>0 && str.charAt(end-1) != '\\'))
                    break;
                
                end++;
            }
            //se encontrar o \"
            if(end < str.length() && str.charAt(end) =='\"')
                end++; //passar o "
            // senao é um ERRO - String n�o terminada
            
            
        } else{
            end = begin;
            //passar os espacos entre os perentesis
            int parentesis = 0;
            while( end  < str.length()   ){
                if( str.charAt(end) == '(')
                    parentesis++;
                if( str.charAt(end) == ')')
                    parentesis--;
                if( SEPARATOR.indexOf(str.charAt(end))!= -1 && parentesis%2 ==0)
                    break;
                
                end++;
            }
        }
    }
    
    
    public static void main(String args[]){
        System.out.println("IteratorCodeLine");
        String str = " a [1] <- a[3]  + a [4]  bila ";
        IteratorCodeLine it = new IteratorCodeLine(str);
        while( it.hasMoreElements()){
            System.out.println(it.current());
            it.next();
        }
        
    }
    
}
