package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;


public class ExpandRepeteAte {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";
    /**
     * expande o ciclo
     * @param repeatNode nodo de inicio
     * @param level nivel
     * @param memory vector de memoria
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    public static void ExpandRepeat(NodeInstruction repeatNode,NodeInstruction endNode,int level, Vector memory)throws LanguageException{
        String REPETE = Keyword.GetTextKey( Keyword.REPETE);
        String ATE = Keyword.GetTextKey( Keyword.ATE);
        //marcar o no como FAZ
        repeatNode.SetType(Keyword.REPETE);
        repeatNode.SetLevel(level);
        
        NodeInstruction tmp = repeatNode.GetNext();
        if (tmp.equals(endNode)) {
            //no caso do ciclo vazio, não permitir, mas se permitere tem as condições para executar sem problemas
            throw new LanguageException(
                    endNode.GetCharNum(),
                    repeatNode.GetText()+" - "+endNode.GetText(),
                    "O ciclo REPITA-ATE está vazío",
                    "Acrecente instruções dentro do ciclo.");
        }
        
        //procurar o fim do ciclo
        // while(tmp.GetType() != Keyword.ATE){
        while(!tmp.equals(endNode)){
            tmp.SetLevel(level+1);
            tmp = tmp.GetNext();
        }
        //fim do REPETE
        NodeInstruction endRepeatNode = tmp;
        //fazer o no do ATE
        String  condic = (endRepeatNode.GetText().trim()).substring(ATE.length()).trim();
        //verificar a condição              
        if( Expressao.TypeExpression(condic,memory)!= Simbolo.LOGICO)
            throw new LanguageException(
                    endRepeatNode.GetCharNum(), endRepeatNode.GetText(),
                    "\"" + condic + "\" NÃO É UMA CONDIÇÃO VÁLIDA" ,
                    "VERIFIQUE SE A CONDIÇÃO ESTÁ BEM ESCRITA");
                
        //alterar o texto para a condição
        endRepeatNode.SetText(condic);
        //tipo de no do tipo ATE
        endRepeatNode.SetType(Keyword.ATE);
        //nivel do no
        endRepeatNode.SetLevel(level);
        //se for false volta para cima
        endRepeatNode.SetIfFalse(repeatNode);
        //se for verdadeiro continua
        endRepeatNode.SetIfTrue(endRepeatNode.GetNext());
        
        //ligar o REPETE é condição
        // utilizo esta ligação para escrever (toString)
        repeatNode.SetIfTrue(endRepeatNode.GetNext());
        repeatNode.SetIfFalse(endRepeatNode.GetNext());
        
    }
    
 
     public static void CalculatePositions( NodeInstruction begin ,double Y, double X ){
        double PY ,  PX ;
        PX = 0.5/(begin.level + 1.0);
        
        begin.SetPositionY(Y);
        begin.SetPositionX(X);
        NodeInstruction tmp = begin.GetNext();
        //fazer o  if
        PY = Y+1;        
        while(tmp.GetIfFalse() != begin){
           // FluxogramVisual.ProcessNodePosition(tmp, PY , X - PX);
            PY = tmp.GetPositionY() + 1;
            tmp = tmp.GetNext();
        } 
        //colocar a decisao ao mesmo nivel do faz
        tmp.SetPositionY(PY);
        tmp.SetPositionX(X);

        
    }
     
    /**
     * texto do ciclo
     * @param begin inicio do ciclo
     * @return texto do ciclo
     */
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
