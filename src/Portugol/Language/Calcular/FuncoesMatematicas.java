package Portugol.Language.Calcular;

import Portugol.Language.Utilitario.Values;
import java.util.Vector;


public class FuncoesMatematicas extends AbstractCalculus{
    
    private static String functions0 =" ALEATORIO ";
    private static String functions1 =" SENO COSENO TANGENTE COTANGENTE "
                                    + " ASENO ACOSENO ATANGENTE ACOTANGENTE " +
                                      " SENOH COSENOH TANGENTEH COTANGENTEH " +
                                      " EXP ABS RAIZ LOG LN " +
                                      " PARTEINTEIRA PARTEFRAC ARREDONDAR RETORNA_RETORNO "// esta ultima funcao só serve de apoio as funçoes criadas pelo utilizador.
                                      ;
    
    private static String functions2 =" POTENCIA "
                                      + " MAIOR MENOR "  
                                      ;
    
    
    public  boolean IsValid( String str){
        return functions0.indexOf(" " + str.toUpperCase() + " ") != -1 ||
                functions1.indexOf(" " + str.toUpperCase() + " ") != -1 ||
                functions2.indexOf(" " + str.toUpperCase() + " ") != -1;
    }
    
    public  int GetNumParameters(String str)throws Exception{
        if(functions0.indexOf(" " + str.toUpperCase() + " ") != -1 ) return 0;
        if(functions1.indexOf(" " + str.toUpperCase() + " ") != -1 ) return 1;
        if(functions2.indexOf(" " + str.toUpperCase() + " ") != -1 ) return 2;
        throw new Exception("ERRO nos parametros das funções ["+str+"]");
    }
    //---------------------------------------------------------------------------
  
    public  int GetPriority(String oper)throws Exception{
        if( IsValid(oper)) return AbstractCalculus.FUNCTION_PRIORITY;
        throw new Exception("ERRO na Prioridade das funções ["+oper+"]");        
    }
    
    
    public  String Calculate( String oper , Vector params)throws Exception{
        if( params.size() == 0 )
            return Calculate0(oper);
        if( params.size() == 1 )
            return Calculate1(oper,(String)params.get(0));
        if( params.size() == 2 )
            return Calculate2(oper,(String)params.get(0),(String)params.get(1));
        
        throw new Exception("ERRO funçao parametros errados ["+oper+"] " + params.toString() );        
    }
    //---------------------------------------------------------------------------
    private  String Calculate0( String oper )throws Exception{
        double val=0;
        if( oper.equalsIgnoreCase("ALEATORIO") ) val = java.lang.Math.random();
        else throw new Exception("ERRO funçao Desconhecida 2 ["+oper+"]");
        return Values.DoubleToString(val);
    }
    //--------------------------------------------------------------------------
    
    //---------------------------------------------------------------------------
    private  String Calculate1( String oper , String str1)throws Exception{
        double n1 = Values.StringToDouble(str1);
        double val=0;
        
        if( oper.equalsIgnoreCase("SENO") ) val = java.lang.Math.sin(n1);
        else if( oper.equalsIgnoreCase("COSENO") ) val = java.lang.Math.cos(n1);
        else if( oper.equalsIgnoreCase("TANGENTE") ) val =  java.lang.Math.tan(n1);
        else if( oper.equalsIgnoreCase("COTANGENTE") ) val =  1.0 / java.lang.Math.tan(n1);
        
        else if( oper.equalsIgnoreCase("ASENO") ) val = java.lang.Math.asin(n1);
        else if( oper.equalsIgnoreCase("ACOSENO") ) val = java.lang.Math.acos(n1);
        else if( oper.equalsIgnoreCase("ATANGENTE") ) val =  java.lang.Math.atan(n1);
        else if( oper.equalsIgnoreCase("ACOTANGENTE") ) val =  1.0 / java.lang.Math.atan(n1);

        else if( oper.equalsIgnoreCase("SENOH") ) val = java.lang.Math.sinh(n1);
        else if( oper.equalsIgnoreCase("COSENOH") ) val = java.lang.Math.cosh(n1);
        else if( oper.equalsIgnoreCase("TANGENTEH") ) val =  java.lang.Math.tanh(n1);
        else if( oper.equalsIgnoreCase("COTANGENTEH") ) val =  1.0 / java.lang.Math.tanh(n1);
    
        else if( oper.equalsIgnoreCase("EXP") ) val = java.lang.Math.exp(n1);
        //valor absoluto de inteiros sao inteiros
        else if( oper.equalsIgnoreCase("ABS") ){
            val = java.lang.Math.abs(n1);
            if( Values.IsInteger(str1))
                return Values.IntegerToString(val);
        }
        else if( oper.equalsIgnoreCase("RAIZ") ) val =  java.lang.Math.sqrt(n1);
        else if( oper.equalsIgnoreCase("LOG") ) val =  java.lang.Math.log10(n1);
        else  if( oper.equalsIgnoreCase("LN") ) val =  java.lang.Math.log(n1);
       // parte inteira do numeros
        else  if( oper.equalsIgnoreCase("PARTEINTEIRA") ){
            return Values.IntegerToString(n1);
        }
        //parte real
        else  if( oper.equalsIgnoreCase("PARTEFRAC") ){
            String num= Values.DoubleToString(n1);
            return num.substring( num.indexOf('.')+1);
        }    
        
        else  if( oper.equalsIgnoreCase("ARREDONDAR") ){
            double  vm = java.lang.Math.ceil(n1);            
            if( n1 - vm >= 0.5)
                return Values.IntegerToString((int) n1+1);
           return Values.IntegerToString((int) n1); 
        }    
        
        else  if( oper.equalsIgnoreCase("RETORNA_RETORNO") ){ 
           return Values.StringToText(""+str1);
        } 
        
        else throw new Exception("ERRO função Desconhecida 1 ["+oper+"]");
        return Values.DoubleToString(val);
    }
    //---------------------------------------------------------------------------
    private  String Calculate2( String oper , String str1, String str2)throws Exception{
        double n1 = Values.StringToDouble(str1);
        double n2 = Values.StringToDouble(str2);
        double val=0;
        // potencia de inteiros sao inteiros
        if( oper.equalsIgnoreCase("POTENCIA") ) {
            val = java.lang.Math.pow(n1,n2);
            if( Values.IsInteger(str1) && Values.IsInteger(str2) )
                return Values.IntegerToString(val);
        }
        
        // maior de 2 numeros
        if( oper.equalsIgnoreCase("MAIOR") ) {
            val = java.lang.Math.max(n1,n2);
            if( Values.IsInteger(str1) && Values.IsInteger(str2) )
                return Values.IntegerToString(val);
        }
        
         // menor de 2 numeros
        if( oper.equalsIgnoreCase("MENOR") ) {
            val = java.lang.Math.min(n1,n2);
            if( Values.IsInteger(str1) && Values.IsInteger(str2) )
                return Values.IntegerToString(val);
        }
        
        
        else throw new Exception("ERRO FUNÇÃO DESCONHECIA 2 ["+oper+"]");
        return Values.DoubleToString(val);
    }
}
