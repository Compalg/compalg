package Portugol.Language.Utilitario;

import java.util.StringTokenizer;

public class CodeLine {
    
    //---------------------------------------------------------------------------
    
    public static String ToPortuguese(String str){    
        StringBuffer newStr = new StringBuffer();
        boolean isString = false; 
        for(int i=0 ; i< str.length() ; i++){
            char ch = str.charAt(i);
            if( ch == '\"' ){
                if( i==0 || (i >0 && str.charAt(i-1) != '\\'))
                    isString = ! isString;
            }
            if(isString)    
                newStr.append(ch);
            else  if(port.indexOf(ch) !=-1)
                newStr.append( noc.charAt(port.indexOf(ch) ));
            else
                newStr.append( (new String(ch+"")).toLowerCase());
        }
        return newStr.toString();
    }
    
    //---------------------------------------------------------------------------
    //---------------------------------------------------------------------------
    /**
     * Normaliza uma linha de codigo colocando espacoes onde sao necessarios e retirando os desnecessarios
     * @param str linha de codigo
     * @return linha de codigo normalizada
     */
    public static String GetNormalized( String str){
        
        String oper ="+-*/^(),%[]";        
        String relat ="><=/";
        boolean isString= false;
        
        str = ToPortuguese(str);
        String tmp= "";
        for(int i=0; i< str.length(); i++){
            
            char ch = str.charAt(i);            
            if( ch =='\"' && i>0 && str.charAt(i-1) != '\\')
                isString = !isString;
            
            //sinal de atribui��o
            if( !isString &&  ch=='<' && i+1 < str.length() && str.charAt(i+1)== '-'  ){
                tmp+= " ";
                tmp+= "<-";
                tmp +=" ";
                //passar por cima do '-'
                i++;
            }
            //acrescenter espacos aos operadores
            else if( !isString &&  oper.indexOf( ch ) != -1 ){
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
            else if( !isString && i < str.length()-2 && (ch == '<' || ch == '>') &&
                    str.charAt(i+1) == '=' ){
                tmp+= " ";
                tmp+= ch;
                tmp +="= ";
                i++;
            }
            // relacional com tres simbolos
            else if(!isString && i < str.length()-3 && str.charAt(i) == '=' && str.charAt(i+1) == '/' && str.charAt(i+2) == '='){
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
   static String normalizeMinus( String str){              
       // retirei o ) por nao contarem como operadores
       String oper = " E OU NAO + - * / ^ (  , <- >= <= < > = =/= <>";
        String tmp="";
        String elem;
        boolean prevOperator = true;
        StringTokenizer it = new StringTokenizer( str," , \t", true);
        while( it.hasMoreElements() ){
            elem = ((String)it.nextElement()).trim();
            if(elem.length() == 0) continue;           
            if( ! elem.equalsIgnoreCase("-") )
                tmp += elem + " ";
            else{
                // se o anterior for um operador
                if( prevOperator )
                    tmp+= "-";
                else
                    //tmp+= " - ";
                    tmp+= "- ";
            }
            if( oper.indexOf(" " + elem + " ") != -1 )
                prevOperator = true;
            else
                prevOperator = false;            
        }
        return tmp.trim();
    }
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
    private static String port  = "AEIOUAEIOUAEIOUAEIOUAOAOAEIOUAEIOUCC";
    private static String noc  =  "aeiouAEIOUaeiouAEIOUaoAOaeiouAEIOUcC";
        
}
