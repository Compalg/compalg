package Portugol.Language.Calcular;

import Portugol.Language.Utilitario.Parentesis;
import Portugol.Language.Utilitario.IteratorExpression;
import Portugol.Language.Utilitario.IteratorString;
import java.util.Stack;
import java.util.Vector;

public class Calculador {
    public static String VERSION = "Versão:1.0 \t(c)Augusto Bilabila";
    /**
     * Creates a new instance of Calculador
     */
    String inFix;
    String posFix;
    private static CalculusElement calculator = new CalculusElement();
    
    
     // constroi um calculador com uma expressao na forma infixa
     
    public Calculador(String exp) {
        inFix = exp;
        try{
        posFix = this.CalulatePosFix(inFix);
        } catch(Exception e){
            posFix = e.getMessage();
        }
    }
    
    // verifica se a string é um elemento de cálculo válido

    public static boolean IsCalculus(String str){  
        CalculusElement calculator = new CalculusElement();
        return calculator.IsCalculus(str);
    }
   
    // retorna a expressao infixa normalizada
      
    public String GetInfix(){
        return inFix;
    }
    
     // retorna a Expressao posfixa
    public String GetPosfix(){
        return posFix;
    }
    
     // faz o calculo da expressao
     
    public Object GetResult() throws Exception {
        return CalulateValue(inFix);
    }
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
    /**
     metodo estatico que converte infixa em posfixa
     */
    public static String CalulatePosFix(String infix) throws Exception{
       if( ! Parentesis.Verify(infix))
            throw new Exception(Parentesis.GetError(infix));
       
        IteratorExpression it = new IteratorExpression(infix);
        Stack s = new Stack();
        String strPosFix ="";
        
        while(it.hasMoreElements() ){
            String elem = it.current();
            it.next();
            
            // parametros das funcoes
            if(elem.equals(",")){
               //retirar todos elementos ate ao parentesis
                while( !s.empty() &&  !((String)s.peek()).equals("(") ){
                    elem = (String) s.pop();
                    strPosFix += elem + " ";
                }      
                continue;
            }
            // introduzir directamente na pilha
            if( elem.equalsIgnoreCase("(") )
                s.push(elem);
            //retirar da pilha todos operadores ate encontrar o (
            else if( elem.equalsIgnoreCase(")") ){
                while( !s.empty() ){
                    elem = (String) s.pop();
                    if(elem.equalsIgnoreCase("(")  )
                        break;
                    strPosFix += elem + " ";
                }               
            }
            // se for um operador
            else if( calculator.IsCalculus( elem ) ){
                int prio = calculator.GetPriority(elem);
                //retirar todos operadores com maior prioridade
                while( !s.empty() && calculator.GetPriority( (String) s.peek() ) >= prio){
                    strPosFix += (String) s.pop() + " ";
                }
                s.push(elem);
            } else
                strPosFix += elem + " ";
        }// fim do iterador
        
        while( ! s.empty()){
            
            strPosFix += (String) s.pop() + " ";
        }
        
        return strPosFix.trim();
    }

//------------------------------------------------------------------------------

     // Executa o calculo
  
   public  static Object CalulateValue(String expr) throws Exception{
         String str = CalulatePosFix(expr);
         // se a expressao for vazia
         // if( str.length() == 0) return expr;
        IteratorString it = new IteratorString(str);
        Stack result = new Stack();
        Vector params = new Vector();
        String elem;
        while( it.hasMoreElements() ){
            elem = it.current();
            it.next();            
            // se for um calculo
            if( calculator.IsCalculus(elem) ){
               // retirar os parametros do calculo
                params.clear();
                for( int index=0; index < calculator.GetNumParameters(elem) ; index++){
                    
                    //verificar se existem parametros
                    if( result.empty())
                         throw new Exception(" ERRO 011:\nNO NÚMERO DE PARÁMETROS NO SÍMBOLO :" + elem );
                    //adicionar no inicio
                    params.add(0,result.pop());
                }
                //adicionar o resultado
                result.push( calculator.Calculate( elem , params) );
            } else
             // se for um valor   
                result.push(elem);
            
        }
        
        if( result.size() == 1){
            return result.pop();        
        }   
        else 
            throw new Exception(" ERRO 011:\nA EXPRESSÃO ["+ expr + "] ESTÁ MAL CONSTRUÍDA");         
    }
    
}

