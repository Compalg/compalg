                package Portugol.Language.Calcular;

import java.util.Vector;
    
public class Functions  extends AbstractCalculus{
     
    private FuncoesMatematicas math = new FuncoesMatematicas();
    private FuncaoTexto txt = new FuncaoTexto();
    
    public boolean IsValid( String str){
        return math.IsValid(str ) || txt.IsValid(str);
    }
    
    public  int GetNumParameters(String oper)throws Exception{
        if( math.IsValid(oper) ) return math.GetNumParameters(oper);
        if( txt.IsValid(oper) ) return txt.GetNumParameters(oper);
        throw new Exception("ERRO 013:\nOPERADOR DE FUNCOES DESCONHECIDO :" + oper );        
    }
    
    public  int GetPriority(String oper)throws Exception{
        if( math.IsValid(oper) ) return math.GetPriority(oper);
        if( txt.IsValid(oper) ) return txt.GetPriority(oper);
        throw new Exception("ERRO 013:\nOPERADOR DE FUNCOES DESCONHECIDO :" + oper );
    }
    
    public String Calculate( String oper , Vector params)throws Exception{
        if( math.IsValid(oper) ) return math.Calculate(oper,params);
        if( txt.IsValid(oper) ) return txt.Calculate(oper,params);
        throw new Exception("ERRO 013:\nOPERADOR DE FUNCOES DESCONHECIDO :" + oper );        
    } 
     
 }
