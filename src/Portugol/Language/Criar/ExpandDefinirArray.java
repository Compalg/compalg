package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SimboloDeParametro;
import Portugol.Language.Analisador.TipoDeParametro;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Utilitario.IteratorArray;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;

/**
 *
 * @author bila
 */
public class ExpandDefinirArray {
    
    public static NodeInstruction Define(String alter , String type , String name , String value, NodeInstruction node, Vector memory)
    throws LanguageException{
        int pCut = name.indexOf("[");
        String indexes = name.substring(pCut).trim();
        name = name.substring(0,pCut-1).trim();
        
         
        //verificar se é um nome aceitavel
        if( !Variavel.isNameAcceptable(name))
            throw new LanguageException(
                    node.GetCharNum(), node.GetText() ,
                    " SIMBOLO " + name + " INVALIDO :" + Variavel.getErrorName(name),
                    "ALTERE O NOME DO SIMBOLO " + name);
        
        //verificar se a variavel ja esta definida
        Simbolo tmpVar = Variavel.getVariable(name,memory);
        if( tmpVar != null && tmpVar.getLevel() == node.GetLevel())
            throw new  LanguageException(
                    node.GetCharNum(), node.GetText() ,
                    "O SIMBOLO <" + name + "> JÁ FOI DECLARADO",
                    " MUDE DE NOME <" + name + "> .");
        
        
        VerifyIndex(indexes, node,memory);
        VerifyValues(type, value, node,memory);
        
        //normalizar o texto da expressao
        String text = alter +" "+type +" "+ name + " " + indexes + "  <- "+ value;
        // fazer um novo no
        NodeInstruction  newNode = new NodeInstruction(node);
        newNode.SetText(text);
        Variavel.defineVAR(newNode,memory, new Vector<TipoDeParametro>(), new Vector<SimboloDeParametro>()); //David: En la original de las variables no se tiene conociminedo de parametros de llamadas
        return newNode;
        
    }
    
    private static void VerifyIndex(String  indexDefs,NodeInstruction node, Vector memory) throws LanguageException {
        IteratorArray iter = new IteratorArray(indexDefs);
        while( iter.hasMoreElements()){
            String value = iter.getNext();
            
            //Avaliar a expressao
            // se nao for possivel avaliar provoca erro
            Object result;
            try {
                result = Expressao.Evaluate(value,memory);
            } catch( Exception e){
                throw new  LanguageException(
                        node.GetCharNum(), node.GetText() ,
                        e.toString(),
                        "VERIFIQUE A EXPRESSÃO <" +  value + ">");
            }
            if( !Values.IsInteger( result) )
                throw new LanguageException(
                        node.GetCharNum(), node.GetText() ,
                        value + " = " + result + " NÃO É UMA VARIÁVEL INTEIRA",
                        "REDIFINA O VALOR INDICE");
            
            if( Integer.parseInt((String) result) <= 0 )
                  throw new LanguageException(
                        node.GetCharNum(), node.GetText() ,
                        value + " = " + result + " NÃO É UM INDICE VÁLIDO",
                        "REDIFINA O VALOR INDICE");
        }
        
    }
    
    private static void VerifyValues(String type, String  values,NodeInstruction node, Vector memory) throws LanguageException {
        IteratorCodeParams iter = new IteratorCodeParams(values , ",{}");
        int index =0;
        while( iter.hasMoreElements()){
            String value = iter.current();
            //Avaliar a expressao
            // se n�o for possivel avaliar provoca erro
            Object result;
            try {
                result = Expressao.EvaluateByDefaults(value,memory);
            } catch( Exception e){
                throw new  LanguageException(
                        node.GetCharNum(), node.GetText() ,
                        e.toString(),
                        "VERIFIQUE A EXPRESSÃO <" +  value + ">");
            }
            
            //verificar se o resultado da expressao e compativel com a variavel
            if( !Simbolo.IsCompatible( Simbolo.getType(type), result) )
                throw new  LanguageException(
                        "O VALOR <" + result + "> NÃO É PERMITIDO PARA VARIÁVEL " + type ,
                        " VERIFIQUE A EXPRESSÃO :" + value);
            
            iter.next();
        }
    }
    
}
