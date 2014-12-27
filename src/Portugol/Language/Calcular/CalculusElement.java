package Portugol.Language.Calcular;

import java.util.Vector;

public class CalculusElement {
    
   static Vector elemCalc = new Vector();
   
   static{
        elemCalc.add( new Functions());
        elemCalc.add( new Aritmeticos());
        elemCalc.add( new Logico());
        elemCalc.add( new Relationals());      
   }
    
   //verifica se é uma função
   public static boolean IsFunction( String str){
       return ((AbstractCalculus) elemCalc.get(0) ).IsValid(str);
   }
   
    //verifica se é um operador aritmetico
   public static boolean IsAritmetic( String str){
       return ((AbstractCalculus) elemCalc.get(1) ).IsValid(str);
   }
   
     //verifica se é um operador logico
   public static boolean IsLogic( String str){
       return ((AbstractCalculus) elemCalc.get(2) ).IsValid(str);
   }
   
    //verifica se é um operador relacional
   public static boolean IsRelational( String str){
       return ((AbstractCalculus) elemCalc.get(3) ).IsValid(str);
   }
   
   //verifica se é um operador relacional ou logico ou relacional
   public static boolean IsOperator( String str){
       return IsRelational(str) || IsLogic(str) || IsAritmetic(str);               
   }
   
    //verifica se é o testo é um elemento de cálculo
 
   public static boolean IsElemCalculus( String str){
       return  IsFunction(str) ||
               IsAritmetic(str) ||
               IsLogic(str) ||
               IsRelational(str);
   }
    
   //verifica se é um elemento de cálculo válido
    public boolean IsCalculus( String str){
        if( str.equals("(") || str.equals(")") )
            return true;
        
        for( int index=0; index< elemCalc.size() ; index++){
            if ( ((AbstractCalculus) elemCalc.get(index) ).IsValid(str) )
                return true;
        }
        return false;
    }
    
    //--------------------------------------------------------------------------
    public  int GetNumParameters(String str)throws Exception{
        for( int index=0; index< elemCalc.size() ; index++){
            if ( ((AbstractCalculus) elemCalc.get(index) ).IsValid(str) )
                return ((AbstractCalculus) elemCalc.get(index) ).GetNumParameters(str);
        }
        throw new Exception("ERRO 014:\nPARÁMETROS DE OPERADOR DESCONHECIDO [" + str +"]");
    }
    
    //---------------------------------------------------------------------------
    public int GetPriority(String str)throws Exception{
        if( str.equalsIgnoreCase("(") )     return 0;
        if( str.equalsIgnoreCase(")") )     return 0;
        for( int index=0; index< elemCalc.size() ; index++){
            if ( ((AbstractCalculus) elemCalc.get(index) ).IsValid(str) )
                return ((AbstractCalculus) elemCalc.get(index) ).GetPriority(str);
        }
        
        throw new Exception("ERRO 014:\nNA PRIORIDADE ["+str+"]");
    }
    
   
     // Executa o calculo do elemento
 
    public String Calculate( String str , Vector params)throws Exception{
        for( int index=0; index< elemCalc.size() ; index++){
            if ( ((AbstractCalculus) elemCalc.get(index) ).IsValid(str) )
                return ((AbstractCalculus) elemCalc.get(index) ).Calculate(str,params);
        }
        throw new Exception("ERRO 014:\nNO CALCULO DE OPERADOR DESCONHECIDO [" + str +"]" + params.toString());
    }        
}
