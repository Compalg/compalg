package Portugol.Language.Utilitario;

public class IteratorExpression  extends IteratorString{
    
    /**
     * contrutor do iterador
     * @param msg expressao
     */
    public IteratorExpression(String msg) {        
        str = normalize(msg);
        next();
    }
    /**
     * retorna a expressao normalizada
     * @return expressao normalizada
     */
    public String getExpression(){
        return str;
    }
        
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
    /**
     * converte as a string para mausculas internacionais
     * @param src string
     * @return string em mausculas
     */
    public static String ToUpperCase( String src){
        String from ="abcdefghijklmnopqrstuvxyzw" +
                     "aaaaeeeiioooc"+
                     "aaaaeeeiioooc";
        String to   ="ABCDEFGHIJKLMNOPQRSTUVXYZW"+
                     "AAAAEEEIIOOOC"+  
                     "AAAAEEEIIOOOC";
        boolean isString= false;
        StringBuffer tmp = new StringBuffer();
        for(int i=0 ; i< src.length() ; i++){
            char ch = src.charAt(i);
           
            if( ch =='\"' && (i==0 || i>0 && src.charAt(i-1) != '\\') )
                isString = !isString;
            
            if( !isString && from.indexOf(ch) >=0 )
                tmp.append( to.charAt(from.indexOf(ch)) );
            else
                tmp.append(ch);
        }
        return tmp.toString();
    }
//------------------------------------------------------------------------------
    String normalize( String str){
        String oper ="+-*/^()[],%";
        String relat ="><=/";
        boolean isString= false;
        
        str = ToUpperCase(str);
        String tmp= "";
        for(int i=0; i< str.length(); i++){
            
            char ch = str.charAt(i);
            if( ch =='\"' && i>0 && str.charAt(i-1) != '\\')
                isString = !isString;
            
            //acrescenter espacos aos operadores
            if( !isString &&  oper.indexOf( ch ) != -1 ){
                tmp+= " ";
                tmp+= ch;
                tmp +=" ";
            }
            // relacional com um simbolo
            else if( !isString && i < str.length() -1 && relat.indexOf( ch ) != -1 &&
                    relat.indexOf( str.charAt(i+1) ) == -1){
                tmp+= " ";
                tmp+= ch;
                tmp +=" ";
            }
            //David: permitir que se reconozca <> juntos, sin esto el codigo los separa.
            // falta lograr que se pinten de verde como los otros simbolos
            //relacional com dois simbolos
            else if( !isString && i < str.length()-2 && ch == '<' &&
                    str.charAt(i+1) == '>' ){
                tmp+= " ";
                tmp+= ch;
                tmp +="> ";
                i++;
            }

            // relacional com dois simbolos
            else if( !isString && i < str.length()-2 && relat.indexOf( ch ) != -1 &&
                    str.charAt(i+1) == '=' ){
                tmp+= " ";
                tmp+= ch;
                tmp +="= ";
                i++;
            }
            // relacional com tres simbolos
            else if(!isString && i < str.length()-2 && str.charAt(i) == '=' && str.charAt(i+1) == '/' && str.charAt(i+2) == '='){
                tmp += " =/= ";
                i += 2;
            }
            // outros simbolos
            else
                tmp += str.charAt(i);
            
        }
        
        return  normalizeMinus(tmp);       
        
    }
//------------------------------------------------------------------------------
    String normalizeMinus( String str){
        
        String oper = " E OU NAO + - * / ^ (  , >= <= < > = =/= ";
        String tmp="";
        String elem;
        boolean prevOperator = true;
        IteratorString it = new IteratorString(str);
        while( it.hasMoreElements() ){
            elem = it.current();
            it.next();
            if( ! elem.equalsIgnoreCase("-") )
                tmp += elem + " ";
            else{
                // se o anterior for um operador
                if( prevOperator )
                    tmp+= "-";
                else
                    tmp+= " - ";
            }
            if( oper.indexOf(" " + elem + " ") != -1 )
                prevOperator = true;
            else
                prevOperator = false;
            
        }
        return tmp.trim();
    }
    
    
}
