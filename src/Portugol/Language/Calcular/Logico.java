package Portugol.Language.Calcular;

import Portugol.Language.Utilitario.Values;
import java.util.Vector;

public class Logico  extends AbstractCalculus{
    
    private static String logics1 =" NAO ";
    private static String logics2 =" E OU XOU ";
    
    
    public String GetSymbols(){
        return logics1 + logics2;
    }
       
    
    // verifica se é um operador lógico
    public boolean IsValid( String str){
        return logics1.indexOf(" " + str.toUpperCase() + " ") != -1 ||
                logics2.indexOf(" " + str.toUpperCase() + " ") != -1;
    }
    
     // calcula o número de parámetros
    public  int GetNumParameters(String str)throws Exception{
        if(logics1.indexOf(" " + str.toUpperCase() + " ") != -1 ) return 1;
        if(logics2.indexOf(" " + str.toUpperCase() + " ") != -1 ) return 2;
        throw new Exception("ERRO nos parametros do Logico ["+str+"]");
    }
    
    //--------------------------------------------------------------------------
    
     // prioridade do operador
    public  int GetPriority(String oper)throws Exception{
        if( oper.equalsIgnoreCase("OU"))    return AbstractCalculus.LOGIC_PRIORITY + 1;
        if( oper.equalsIgnoreCase("XOU" ))    return AbstractCalculus.LOGIC_PRIORITY + 1;
        if( oper.equalsIgnoreCase("E" ))    return AbstractCalculus.LOGIC_PRIORITY + 2;
        if( oper.equalsIgnoreCase("NAO"))   return AbstractCalculus.LOGIC_PRIORITY + 3;
        
        throw new Exception("ERRO na prioridade do Logico ["+oper+"]");
        
    }
    
    //--------------------------------------------------------------------------
    public  String Calculate( String oper , Vector params)throws Exception{
        if( params.size() == 1 )
            return CalculateLogic1(oper,(String)params.get(0));
        if( params.size() == 2 )
            return CalculateLogic2((String)params.get(0),oper,(String)params.get(1));
        
        throw new Exception("ERRO 010:\nPARAMETROS LOGICO ERRADO ["+oper+"] " + params.toString() );
        
    }
    
    private  String CalculateLogic2( String str1, String oper , String str2)throws Exception{
        boolean n1 = Values.StringToBoolean(str1);
        boolean n2 = Values.StringToBoolean(str2);
        boolean val=false;
        if( oper.equalsIgnoreCase("E") ) val = n1 && n2;
        else if( oper.equalsIgnoreCase("OU") ) val =  n1 || n2;
        else if( oper.equalsIgnoreCase("XOU") ) val =  n1 != n2;
        else
            throw new Exception("ERRO 010:\nOPERADOR LOGICO DESCONHECIDO ["+ oper+"]");
        return Values.BooleanToString(val);
    }
//---------------------------------------------------------------------------
    private String CalculateLogic1(String oper , String str1)throws Exception{
        boolean n1 = Values.StringToBoolean(str1);
        boolean val=false;
        if( oper.equalsIgnoreCase("NAO") ) val = !n1;
        else
            throw new Exception("ERRO 010:\nOPERADOR LOGICO UNARIO DESCONHECIDO ["+ oper+"]");
        return Values.BooleanToString(val);
        
    }
}
