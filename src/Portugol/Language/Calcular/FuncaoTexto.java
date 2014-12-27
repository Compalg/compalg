package Portugol.Language.Calcular;

import Portugol.Language.Utilitario.Values;
import java.util.Vector;

public class FuncaoTexto extends AbstractCalculus{
    
    private static String functions1 =" COMPRIMENTO ";
    private static String functions2 =" LETRA ";
    //VERIFICA SE O PARAMETRO E UM ELEMENTO DE CALCULO
    public  boolean IsValid( String str){
        return functions1.indexOf(" " + str.toUpperCase() + " ") != -1 ||
                functions2.indexOf(" " + str.toUpperCase() + " ") != -1;
        
    }
    //--------------------------------------------------------------------------
    public int GetNumParameters(String oper)throws Exception{
        if(functions1.indexOf(" " + oper.toUpperCase() + " ") != -1 ) return 1;
        if(functions2.indexOf(" " + oper.toUpperCase() + " ") != -1 ) return 2;
        throw new Exception("ERRO 013:\nNOS PARAMETROS DAS FUNCOES ["+oper+"]");                
    }
    //--------------------------------------------------------------------------
    public int GetPriority(String oper)throws Exception{
        if( IsValid(oper)) return AbstractCalculus.FUNCTION_PRIORITY;
        throw new Exception("ERRO 013:\nNA PRIORIDADE DAS FUNCOES ["+oper+"]");
          
    }
   //---------------------------------------------------------------------------
    public String Calculate( String oper , Vector params)throws Exception{
        if( params.size() == 1 )
            return Calculate1(oper,(String)params.get(0));
        if( params.size() == 2 )
            return Calculate2(oper,(String)params.get(0),(String)params.get(1));
        
        throw new Exception("ERRO 013:\nFUNCAO COM PARAMETROS ERRADOS ["+oper+"] "
                + "" + params.toString() );        
    }
    //--------------------------------------------------------------------------
    private  String Calculate1( String oper, String param )throws Exception{
        double val=0;
        // param = "texto" => comprimento -2 porque " nao conta
        if( oper.equalsIgnoreCase("COMPRIMENTO") ) 
            val = param.length() - 2;
        else 
            throw new Exception("ERRO 013:\nFUNCOES DESCONHECIDAS ["+oper+"]");
        return Values.IntegerToString(val);
    }
    //--------------------------------------------------------------------------
    private  String Calculate2( String oper, String param1, String param2 )throws Exception{                
        if( oper.equalsIgnoreCase("LETRA") ){
            int pos = Values.StringToInteger(param2) + 1; 
            char ch = param1.charAt(pos);
            return Values.StringToText(""+ch);
        }
        else 
            throw new Exception("ERRO 013:\nFUNCAO DESCONHECIDA ["+oper+"]");
           }
}
