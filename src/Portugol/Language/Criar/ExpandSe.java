package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.ParteDeExpresion;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;
import javax.swing.JOptionPane;

public class ExpandSe {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                             ----------------------------
//------------    I N S T R U C A O    S E                ----------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    
    /**
     * expande o ciclo
     * @param begin nodo de inicip
     * @param level nivel
     * @param memory vector de memoria
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    public static void ExpandIF(NodeInstruction begin , int level , Vector memory)
    throws LanguageException{
    //string das instrucoes
        String SE = Keyword.GetTextKey( Keyword.SE);
        String ENTAO = Keyword.GetTextKey( Keyword.ENTAO );
        
        String exp = Normalize(begin.GetText());//David: Normalizado para reconocer então con acento
        int endExp = exp.indexOf(ENTAO);
        if(endExp < 0)
            throw new LanguageException(
                    begin.GetCharNum(), begin.GetText(),
                    "SE sem ENTAO" ,
                    "Coloque o ENTAO no final da instrução");
        
        // SE ocupa dois caracteres
        String  condicao = begin.GetText().substring(SE.length(),endExp).trim(); int cont=0,pos=0;
       
        //David: problemas con el operador <>. ajusté el codigo para que fuera mas estricto en la 
        //verificación. 
        if(condicao.indexOf('<')!=-1 && condicao.indexOf('>') != -1){
         pos = condicao.indexOf('>');
         String partCond2 = condicao.substring(pos+1);
        
            for(int i=0; i<=pos;i++){
                if((condicao.charAt(i)=='<')){
                    cont++;
                    break;
                }
            }
            for(int i=0; i<=pos;i++){
                if(condicao.charAt(i)=='>'){
                    cont++;
                    break;
                }
            }
            //coloca =/= caso o utilizador colocar <>
           if(cont==2 && (condicao.indexOf('<') == condicao.indexOf('>') - 1 || condicao.indexOf('<') == condicao.indexOf('>') - 2)){//Se é umo de cada tipo, continuos con espacio o no por medio 
            condicao = condicao.substring(0, condicao.indexOf('<'));         
            condicao = ((condicao+"=/=")+partCond2).trim();
           }        
        }
        //David: solo cambie desde arriba, desde el otro comentario
        // se nao for uma variavel logica
        ParteDeExpresion var = Variavel.getVariable(condicao, memory);
        if( (var == null || ((Simbolo) var).getType() != Simbolo.LOGICO ) &&
                // uma expressao logica
                Expressao.TypeExpression(condicao,memory)!= Simbolo.LOGICO)
            throw new LanguageException(
                    begin.GetCharNum(), begin.GetText(),
                    "\"" + condicao + "\" Não é uma condição válida" ,
                    "Verifique se a condição está bem escrita");
        
        begin.SetLevel(level);
        // retirar o SE e o ENTAO i faca apenas a condicao
        begin.SetText(condicao);
        // defenir o tipo
        begin.SetType(Keyword.SE);
        
        NodeInstruction tmp = begin.GetNext();
        //David: Fixed some errors about IF or ELSE without instructions
        //ate el fin del metodo tudo trocado
        NodeInstruction endIF= tmp ; // nodo onde termina o if
        NodeInstruction endELSE= tmp ; // nodo onde termina o if
        // incrementar o nivel
        boolean firstOfSpaceTRUE_Assigned = false; //trabajando en espacio true 
        boolean firstOfSpaceFALSE_Assigned = false; //trabajando en espacio false
        boolean In_Space_TRUE = true; //si ya se asigno el primero al espacio true o false (para ambos)
        
        //ligar o ponteiro do verdadeiro
        //-----------------------------------------------------
        //ponteiro para o final do if verdade
        //endIF = tmp;
        boolean encerrar = false;
        do{            
            if(tmp.GetType()== Keyword.FIMSE){
                //fimse, ya terminó SE                
                endIF = tmp;
                begin.SetNext(endIF); //ligacao directa do se ao fimse
                
                tmp = tmp.GetNext();            
                
                endIF.SetNext(tmp);       
                endIF.SetLevel(level);
                endIF.SetType(Keyword.CONECTOR);
                
                if (firstOfSpaceTRUE_Assigned == false)
                    begin.SetIfTrue(endIF); //no há comando para TRUE
                
                if (firstOfSpaceFALSE_Assigned == false)
                    begin.SetIfFalse(endIF); //no há comando para FALSE

                if (In_Space_TRUE == false) //if cambió para SENAO ajustar su conector
                {
                    endELSE.SetNext(endIF);       
                    endELSE.SetLevel(level);
                    endELSE.SetType(Keyword.CONECTOR);                    
                }
                
                encerrar = true;
            }
            else
            if(tmp.GetType()== Keyword.SENAO){
                //comienza el SENAO
                In_Space_TRUE = false;//se cambia al espacio SENAO
                endELSE = tmp;
                tmp = tmp.GetNext();            
            }
            else
            {
                
                if (In_Space_TRUE == true)
                {
                    if (firstOfSpaceTRUE_Assigned == false){
                        begin.SetIfTrue(tmp);
                        firstOfSpaceTRUE_Assigned = true;
                    }
                }
                else
                {
                    if (firstOfSpaceFALSE_Assigned == false){
                        begin.SetIfFalse(tmp);
                        firstOfSpaceFALSE_Assigned = true;
                    }
                }
                
                tmp.SetLevel(level+1);
                tmp = tmp.GetNext();
            }
        } while( !encerrar );              

        cont=0;
    }
//-------------------------------------------------------------------------------------
//------------------------------------------------------------------------------------- 
    /**
     * texto do ciclo
     * @param begin nodo de inicio
     * @return texto do ciclo
     */
    public static  String toString(NodeInstruction begin){
        StringBuffer str = new StringBuffer();
        str.append(begin.toString()+"\n");
        NodeInstruction tmp = begin.GetIfTrue();
        NodeInstruction end = begin.GetNext();
        
        while( tmp != end ){
            str.append(Intermediario.GetCode(tmp));
            tmp = tmp.GetNext();
        }
        str.append("\n");
        tmp = begin.GetIfFalse();
        while(tmp != end){
            str.append(Intermediario.GetCode(tmp));
            tmp = tmp.GetNext();
        }
       
         str.append(end.toString()+ "\n");     
         return str.toString();
    }
 //-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
   
    public static void CalculatePositions( NodeInstruction begin ,double Y, double X ){
        double PY ,  PX ;
        PX = 0.5/(begin.level + 1.0);       
        begin.SetPositionY(Y);
        begin.SetPositionX(X);
        NodeInstruction tmp = begin.GetIfTrue();
        //fazer o  if
        PY = Y+1;
        NodeInstruction end = begin.GetNext();
        while(tmp != end){            
           // FluxogramVisual.ProcessNodePosition(tmp, PY , X + PX);
            PY = tmp.GetPositionY() + 1;
            tmp = tmp.GetNext();
        }
        // posicao Y do conector
         end.SetPositionY(PY); 
        // fazer o else
        tmp = begin.GetIfFalse();
        PY = Y+1;
        while(tmp != end){
          //  FluxogramVisual.ProcessNodePosition(tmp,PY , X - PX);
            PY = tmp.GetPositionY() + 1;
            tmp = tmp.GetNext();
        }
        //conector ( calcular o maximo dos ys do if e do else
        if( end.GetPositionY() < PY)
            end.SetPositionY(PY);
        //posicao X do conector
        end.SetPositionX(X);
                
    }

    //David: Agregado para permitir que se identifique ENTÃO con acento
    static private String from = "ãõáéíóúàèìòùâêîôûÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛçÇ";
    static private String to   = "AOAEIOUAEIOUAEIOUAEIOUAEIOUAOAEIOUCC";
    
    public static String Normalize(String str){
        StringBuffer tmp = new StringBuffer();
        int index;
        for(int i=0; i< str.length();i++ ){
            index = from.indexOf(str.charAt(i));
            if( index == -1 )
                tmp.append(str.charAt(i));
            else
                tmp.append(to.charAt(index));
        }
        return tmp.toString().trim().toUpperCase();
    }
}
