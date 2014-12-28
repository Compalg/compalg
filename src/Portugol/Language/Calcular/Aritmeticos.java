package Portugol.Language.Calcular;

import Portugol.Language.Utilitario.Values;
//import Portugol.Language.Criar.Intermediario;
import java.util.Vector;
import javax.swing.JOptionPane;

public class Aritmeticos extends AbstractCalculus{
    
    private static String operBin =" + - * / % ^ mod div ";
    
     // retorna os simbolos da arimetica
   
    public String GetSymbols(){
        return operBin;
    }
    
    //  verifica se é um operador aritmetico ( + - * / % ^ )

    public  boolean IsValid( String str){
        return operBin.indexOf(" " + str + " ") != -1;
    }    

     // calcula o numero de parametros
 
    public  int GetNumParameters(String oper)throws Exception{
        if (operBin.indexOf(" " + oper + " ") != -1)
            return 2;
        throw new Exception("ERRO 012:\nNO OPERADOR ARITMETICO ["+oper+"]");
    }
    //---------------------------------------------------------------------------

     // prioridade do operador

    public  int GetPriority(String oper)throws Exception{
        if( oper.equalsIgnoreCase("+") )     return AbstractCalculus.ARITMETIC_PRIORITY + 1;
        if( oper.equalsIgnoreCase("-") )     return AbstractCalculus.ARITMETIC_PRIORITY + 1;
        if( oper.equalsIgnoreCase("*") )     return AbstractCalculus.ARITMETIC_PRIORITY + 2;
        if( oper.equalsIgnoreCase("/") )     return AbstractCalculus.ARITMETIC_PRIORITY + 2;
        if( oper.equalsIgnoreCase("%") )     return AbstractCalculus.ARITMETIC_PRIORITY + 2;
        if( oper.equalsIgnoreCase("^") )     return AbstractCalculus.ARITMETIC_PRIORITY + 3;
        throw new Exception("ERRO 012:\nNO OPERADOR ARITMÉTCO ["+oper+"]");
    }
    
     // executa o calculo

    @Override
    public  String Calculate( String oper , Vector params)throws Exception{
        if( params.size() != 2 ){
            throw new Exception("ERRO 012:\nARITMÉTCOS COM DOIS PARAMETROS");
        }
        return Calculate2((String)params.get(0),oper,(String)params.get(1));
    }
    
    //---------------------------------------------------------------------------
   
    public  String Calculate2( String str1, String oper , String str2)throws Exception{
        //aritmetica de numeros inteiros
        if( Values.IsInteger(str1) && Values.IsInteger(str2))
            return CalculateInteger(str1,oper,str2);
              
        else if( Values.IsString(str1) &&  Values.IsString(str2))
            return CalculateText(str1,oper,str2);
        
        if( Values.IsNumber(str1) && Values.IsNumber(str2)){
//            if((Intermediario.VerOperator).contains("div")){
//                JOptionPane.showMessageDialog(null,"O OPERADOR [ div ] É UM OPERADOR APENAS PARA INTEIROS \n");
//                Intermediario.VerOperator = " ";
//                return CalculateReal1(str1,oper,str2);
//            }else
            
            return CalculateReal(str1,oper,str2);            
        }
        throw new Exception("ERRO 012:\nOPERADOR [" + oper + "] NÃO DEFINIDO");
    }
    //---------------------------------------------------------------------------
    private  String CalculateReal( String str1, String oper , String str2)throws Exception{
        double n1 = Values.StringToDouble(str1);
        double n2 = Values.StringToDouble(str2);
        double val=0;
        if( oper.equalsIgnoreCase("+") ) val = n1 + n2;
        else if( oper.equalsIgnoreCase("-") ) val = n1 - n2;
        else if( oper.equalsIgnoreCase("*") ) val =  n1 * n2;
        else if( oper.equalsIgnoreCase("/") ){
            if( n2 == 0)
                throw new Exception("ERRO 012:\nDIVISAO POR ZERO");
            val =  n1 / n2;
        } else if( oper.equalsIgnoreCase("^") ) val =  java.lang.Math.pow(n1,n2);
        else throw new Exception("ERRO 012:\nOPERADOR [" + oper + "] NÃO DEFINIDO");
        return Values.DoubleToString(val);
    }
    
     //---------------------------------------------------------------------------
    private  String CalculateReal1( String str1, String oper , String str2)throws Exception{
        double n1 = Values.StringToDouble(str1);
        double n2 = Values.StringToDouble(str2);
        double val=0;
        if( oper.equalsIgnoreCase("/") ){
                throw new Exception("ERRO 012:\nO OPERADOR [ div ] É UM OPERADOR APENAS PARA INTEIROS");
            
        } 
        return Values.DoubleToString(val);
    }
    
    //---------------------------------------------------------------------------
    private  String CalculateInteger( String str1, String oper , String str2)throws Exception{
        int n1 = Values.StringToInteger(str1);
        int n2 = Values.StringToInteger(str2);
        int val=0;
        if( oper.equalsIgnoreCase("+") ) val = n1 + n2;
        else if( oper.equalsIgnoreCase("-") ) val = n1 - n2;
        else if( oper.equalsIgnoreCase("*") ) val =  n1 * n2;
        else if( oper.equalsIgnoreCase("/") ){
            if( n2 == 0)
                throw new Exception("ERRO 012:\nDIVISAO POR ZERO (IMPOSSIVEL CALCULAR)");
            val =  n1 / n2;
        } 
        else if( oper.equalsIgnoreCase("%") ) val =  n1 % n2;
        else if( oper.equalsIgnoreCase("^") ) val = (int) java.lang.Math.pow(n1,n2);
        else throw new Exception("ERRO 012:\nOPERADOR [" + oper + "] NÃO DEFINIDO");
        return Values.IntegerToString(val);
    }
    
    //---------------------------------------------------------------------------
    private  String CalculateText( String str1, String oper , String str2)throws Exception{
        String n1 = Values.TextToString(str1);
        String n2 = Values.TextToString(str2);
        String val= "";
        if( oper.equalsIgnoreCase("+") ) val = n1 + n2;
        else throw new Exception("ERRO 012:\nOPERADOR [" + oper + "] NÃO DEFINIDO");
        return Values.StringToText(val);
    }   
}