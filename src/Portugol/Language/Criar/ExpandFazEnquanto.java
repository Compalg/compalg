package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;

public class ExpandFazEnquanto {
    
//------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                                    ---------------------
//------------    I N S T R U Ç A O    F A Z  E N Q U A N T O     ---------------------
//------------                                                     --------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
 
    public static void ExpandDoWhile(NodeInstruction doNode,NodeInstruction endNode,int level, Vector memory)throws LanguageException{
        String FAZ = Keyword.GetTextKey( Keyword.FAZ);
        String FAZENQUANTO = Keyword.GetTextKey( Keyword.FAZENQUANTO);
        //marcar o no como FAZ
        doNode.SetType(Keyword.FAZ);
        doNode.SetLevel(level);
        
        NodeInstruction tmp = doNode.GetNext();
        if (tmp.equals(endNode)) {
            //no caso do ciclo vazio, não permitir, mas se permitere tem as condições para executar sem problemas
            throw new LanguageException(
                    endNode.GetCharNum(),
                    doNode.GetText()+" - "+endNode.GetText(),
                    "O ciclo Faz-ENQUANTO está vazío",
                    "Acrecente instruções dentro do ciclo.");
        }
        
        //procurar o fim do ciclo
        while(!tmp.equals(endNode)){
            tmp.SetLevel(level+1);
            tmp = tmp.GetNext();
        }
        //fim do Faz enquanto
        NodeInstruction endDoNode = tmp;
        //fazer o no do endDowhile
        String  condic = (endDoNode.GetText().trim()).substring(FAZENQUANTO.length()).trim();
        //verificar a condicao
        
        if( Expressao.TypeExpression(condic,memory)!= Simbolo.LOGICO)
            throw new LanguageException(
                    doNode.GetCharNum(), doNode.GetText(),
                    "\"" + condic + "\" NÃO É UMA CONDIÇÃO VÁLIDA" ,
                    "VERIFIQUE SE A CONDIÇÃO ESTÁ BEM ESCRITA");
        
        //alterar o texto para a condicao
        endDoNode.SetText(condic);
        //tipo de no dp tipo DOWHILE
        endDoNode.SetType(Keyword.FAZENQUANTO);
        //nivel do no
        endDoNode.SetLevel(level);
        //se for verdadeiro volta para cima
        endDoNode.SetIfTrue(doNode);
        //se for falso continua
        endDoNode.SetIfFalse(endDoNode.GetNext());
        
        //ligar o FAZ é primeira instrucao fora do ciclo.
        // utilizo esta ligacao para escrever (toString)
        doNode.SetIfTrue(endDoNode.GetNext());
        doNode.SetIfFalse(endDoNode.GetNext());
        
    }
    
    public static void CalculatePositions( NodeInstruction begin ,double Y, double X ){
        double PY ,  PX ;
        PX = 0.5/(begin.level + 1.0);
        
        begin.SetPositionY(Y);
        begin.SetPositionX(X);
        NodeInstruction tmp = begin.GetNext();
        //fazer o  if
        PY = Y+1;
        while(tmp.GetIfTrue() != begin){
          //  FluxogramVisual.ProcessNodePosition(tmp, PY , X + PX);
            PY = tmp.GetPositionY() + 1;
            tmp = tmp.GetNext();
        }
        //colocar a decisao ao mesmo nivel do faz
        tmp.SetPositionY(PY);
        tmp.SetPositionX(X);
        
    }
    
    
    public static  String toString(NodeInstruction begin){
        StringBuffer str = new StringBuffer();
        str.append(begin.toString()+"\n");
        NodeInstruction tmp = begin.GetNext();
        while(tmp.GetIfTrue() != begin){
            str.append(Intermediario.GetCode(tmp));
            tmp = tmp.GetNext();
        }
        return str.toString();
    }
}
