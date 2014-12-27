
package Portugol.Language.Calcular;

import Portugol.Language.Utilitario.Values;
import java.util.Vector;

public class Relationals extends AbstractCalculus {
    
    private static String relationals =" > >= < <= ";
    private static String equalsOper = " = =/= <> "; //David: acrecentado operador <>
    
   
    /**
     * verifica se é um operador relacional
     * @param str nome do operador
     * @return operador valido
     */
    public  boolean IsValid( String str){
        return relationals.indexOf(" " + str.toUpperCase() + " ") != -1 ||
                equalsOper.indexOf(" " + str.toUpperCase() + " ") != -1;
    }
    
    /**
     * calcula o numero de parametros do operador
     * @return numero de parametros
     * @param oper nome do operador
     * @throws java.lang.Exception ERRO
     */
    public  int GetNumParameters(String oper) throws Exception{
        if ( IsValid(oper) )
            return 2;
        throw new Exception("ERRO 009:\nNOS PARAMETROS DO OPERADOR RELACIONAL ["+oper+"]");
    }
    //---------------------------------------------------------------------------
    /**
     * retorna a prioridade do operador
     * @return prioridade do operdor
     * @param oper nome do operador
     * @throws java.lang.Exception ERRO
     */
    public  int GetPriority(String oper) throws Exception{
        if( oper.equalsIgnoreCase(">") )     return  AbstractCalculus.RELATIONAL_PRIORITY;
        if( oper.equalsIgnoreCase("<") )     return  AbstractCalculus.RELATIONAL_PRIORITY;
        if( oper.equalsIgnoreCase(">=") )    return AbstractCalculus.RELATIONAL_PRIORITY;
        if( oper.equalsIgnoreCase("<=") )    return AbstractCalculus.RELATIONAL_PRIORITY;
        if( oper.equalsIgnoreCase("=") )     return  AbstractCalculus.RELATIONAL_PRIORITY;
        if( oper.equalsIgnoreCase("=/=") )   return AbstractCalculus.RELATIONAL_PRIORITY;
        if( oper.equalsIgnoreCase("<>") )   return AbstractCalculus.RELATIONAL_PRIORITY; //David: acrecentado operador <>
        throw new Exception("ERRO 009:\nNA PRIORIDADE DO OPERADOR RELACIONAAL ["+oper+"]");
    }
    /**
     * Executa o calculo
     * @return valor do calculo
     * @param oper nome do operador
     * @param params parametros
     * @throws java.lang.Exception ERRO
     */
    public  String Calculate( String oper , Vector params)throws Exception{
        if( params.size() != 2 ){
            throw new Exception("ERRO 009:\nRELACIONAIS - RELACIONAIS COM DOIS PARAMETROS");
        }
        return Calculate((String)params.get(0),oper,(String)params.get(1));
    }
    //---------------------------------------------------------------------------
    private  String Calculate( String str1, String oper , String str2) throws Exception{
        //aritmetica de numeros inteiros
        if( Values.IsNumber(str1) && Values.IsNumber(str2))
            return CalculateValueRelational(str1,oper,str2);
        //aritmetica de numeros inteiros
        if( Values.IsString(str1) && Values.IsString(str2))
            return CalculateTextRelational(str1,oper,str2);
        else if( Values.IsBoolean(str1) && Values.IsBoolean(str2) )
            return CalculateLogicRelational(str1,oper,str2);
        else
            throw new Exception("ERRO 009:\nNO TIPO DE PARAMETROS DO OPERADOR:" + oper );
    }
    
    //---------------------------------------------------------------------------
    private  String CalculateLogicRelational( String str1, String oper , String str2)throws Exception{
        boolean n1 = Values.StringToBoolean(str1);
        boolean n2 = Values.StringToBoolean(str2);
        boolean val=false;
        if( oper.equalsIgnoreCase("=") ) val =  n1 == n2;
        else if( oper.equalsIgnoreCase("=/=") ) val =  n1 != n2;
        else if( oper.equalsIgnoreCase("<>") ) val =  n1 != n2; //David: acrecentado operador <>
        else
            throw new Exception("ERRO 009:\nNO CALCULO BOOLEANO DO OPERADOR ["+oper+"]");
        return Values.BooleanToString(val);
    }
    //---------------------------------------------------------------------------
    private  String CalculateValueRelational( String str1, String oper , String str2)throws Exception{
        double n1 = Values.StringToDouble(str1);
        double n2 = Values.StringToDouble(str2);
        boolean val= false;
        if( oper.equalsIgnoreCase(">") ) val = n1 > n2;
        else if( oper.equalsIgnoreCase(">=") ) val = n1 >= n2;
        else if( oper.equalsIgnoreCase("<") ) val =  n1 < n2;
        else if( oper.equalsIgnoreCase("<=") ) val =  n1 <= n2;
        else if( oper.equalsIgnoreCase("=") ) val =  n1 == n2;
        else if( oper.equalsIgnoreCase("=/=") ) val =  n1 != n2;
        else if( oper.equalsIgnoreCase("<>") ) val =  n1 != n2; //David: acrecentado operador <>
        else
            throw new Exception("ERRO 009:\nNO CALCULO DO OPERADOR RELACIONAL ["+oper+"]");
        return Values.BooleanToString(val);
    }
    //---------------------------------------------------------------------------
    private  String CalculateTextRelational( String str1, String oper , String str2)throws Exception{
        String n1 = Values.TextToString(str1);
        String n2 = Values.TextToString(str2);
        boolean val= false;
        if( oper.equalsIgnoreCase(">") ) val = n1.compareTo(n2)>0;
        else if( oper.equalsIgnoreCase(">=") ) val = n1.compareTo(n2)>=0;
        else if( oper.equalsIgnoreCase("<") ) val =  n1.compareTo(n2)<0;
        else if( oper.equalsIgnoreCase("<=") ) val =  n1.compareTo(n2)<=0;
        else if( oper.equalsIgnoreCase("=") ) val =  n1.compareTo(n2)==0;
        else if( oper.equalsIgnoreCase("=/=") ) val =  n1.compareTo(n2)!=0;
        else if( oper.equalsIgnoreCase("<>") ) val =  n1.compareTo(n2)!=0; //David: acrecentado operador <>
        else
            throw new Exception("ERRO 009:\nNO CALCULO DO OPERADOR RELACIONAL ["+oper+"]");
        return Values.BooleanToString(val);
    }    
    
}