package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public class ExpandDefinirVariavel {
    //---------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------
    public static NodeInstruction Define(String alter , String type , String name , String value, NodeInstruction node, Vector memory)
    throws LanguageException{
    
        //verificar se is um nome aceitavel
        if( !Variavel.isNameAcceptable(name))
            throw new LanguageException(
                    node.GetCharNum(), node.GetText() ,
                    " SIMBOLO " + name + " INVÁLIDO :" + Variavel.getErrorName(name),
                    "ALTERE O NOME DO SIMBOLO " + name);
    
        //verificar se a variavel ja esta definida
        //David:
        Simbolo tmpVar = Variavel.getVariable(name,memory);
        if( tmpVar != null && tmpVar.getLevel() == node.GetLevel())
            throw new  LanguageException(
                    node.GetCharNum(), node.GetText() ,
                    "O SIMBOLO <" + name + "> JÁ FOI DECLARADO",
                    " MUDE DE NOME <" + name + "> .");
        
        //verificar a expressao
        if ( ! Expressao.IsExpression(value, memory))
            throw new  LanguageException(
                    node.GetCharNum(), node.GetText() ,
                    "O SIMBOLO <"+ Expressao.ErrorExpression(value,memory) + "> NÃO FOI DECLARADO",
                    "DECLARE ANTES DE UTILIZAR");
        
        //Avaliar a expressao
        // se nao for possivel avaliar provoca erro
        String memValue;
        try {
            memValue = Expressao.EvaluateByDefaults(value,memory);
        } catch( Exception e){
            throw new  LanguageException(
                    node.GetCharNum(), node.GetText() ,
                    e.toString(),
                    "VERIFIQUE A EXPRESSÃO <" +  value + ">");
        }
        
        //verificar se o resultado da expressao e compativel com a variavel
        if( !Simbolo.IsCompatible( Simbolo.getType(type), memValue ) )
            throw new  LanguageException(
                    "O VALOR <" + value + "> NÃO É PERMITIDO PARA UMA VARIÁVEL " + type ,
                    " VERIFIQUE A EXPRESSÃO :" + value);
        
        //normalizar o texto da expressao
        String text = alter +" "+type +" "+ name + " <- "+ value;
        // fazer um novo no
        NodeInstruction  newNode = new NodeInstruction(node);
        newNode.SetText(text);
        Variavel.defineVAR(newNode,memory);
        return newNode;
    }
    
}
