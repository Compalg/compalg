package Portugol.Language.Criar;

import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Utilitario.IteratorCodeLine;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.TrataDefLiteral;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;
import javax.swing.JOptionPane;

public class ExpandDefinirSimbol {
    private static int SIMPLES     = 0;
    private static int ARRAY        = 1;
    
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                             ----------------------------
//------------    V A R I A V E I S                        ----------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    
    public static NodeInstruction ExpandVariable(NodeInstruction node , int level, Vector memory)
    throws LanguageException{
        int tipoVar = SIMPLES ; // tipo simples ou array
        boolean isConst, isRegist; // para constante
        node.SetLevel(level);
        String alter , type , name, value="",memValue="";
        String CONSTANTE = Keyword.GetTextKey( Keyword.CONSTANTE);
        String VARIAVEL = Keyword.GetTextKey( Keyword.VARIAVEL);
        
        
        //lugar para onde aponta node
        NodeInstruction originalNextNode = node.GetNext();
        //David: this new object is created to keep a copy of the original node, in case of
        //an expantion action changes the original text, it will be used to show message errors to the user. 
        //Separating the internal list's node and the "user written" (and almost not processed) node 
        NodeInstruction CopyOriginalNode = new NodeInstruction(node);
        
        // uma instrucao pode desgenerar em muitos nos
        // com a definicao de varias variaveis ao mesmo tempo
        NodeInstruction prevNode = null;             
        
        String instruction = node.GetText();
        IteratorCodeLine tok = new IteratorCodeLine(instruction);
        alter = tok.current().toUpperCase();
        tok.next();
        //verificar se is constante ou variavel
        if (alter.equalsIgnoreCase(CONSTANTE)){
            isConst = true;
            type = tok.current().toUpperCase(); tok.next();
            
        }else if (alter.equals(VARIAVEL)){
            isConst = false;
            type = tok.current().toUpperCase(); tok.next();
        } else{
            isConst = false;
            type = alter;
            alter = VARIAVEL;
        }
        
        if( Simbolo.getType(type) == Simbolo.DESCONHECIDO  ){
            throw new LanguageException(
                    node.GetCharNum(), node.GetText() ,
                    type + " NÃO É UM TIPO DE SIMBOLO VÁLIDO ",
                    " VERIFIQUE O TIPO DA CONSTANTE OU VARIÁVEL");
        }
        
        //----definicao de  variaveis e valores --------------------
        //---------------------------------------------------------
        // as variaveis estao separadas por virgulas
        //--------------------------------------------------------
        String vars = tok.getUnprocessedString().trim();
        if(vars.length() == 0 )
            throw new LanguageException(
                    node.GetCharNum(), node.GetText() ,
                    " NÃO EXISTE NOME DO SÍMBOLO",
                    " INSIRA UM NOME PARA O SÍMBOLO ");
        
        TrataDefLiteral defLiteral = new TrataDefLiteral(type);
        defLiteral.tratar(vars);
        vars = defLiteral.getLiteral();
        
        // iterador por virgulas
        IteratorCodeParams params = new IteratorCodeParams(vars);
        
        while(params.hasMoreElements()){
            // definir as variaveis
            //David: Agora se utiliza CopyOriginalNode, it was "node" before, but once it chances starts to show
            //the new value and not the original one in the user code
            //This object is used just in error codes, to uses it's attributes for message text creation
            NodeInstruction newNode = DefineSymbol( alter, type, params.current(),CopyOriginalNode, memory);
            params.getNext();
            
            //----------------- ligar os diversos nodos --------------------
            if( prevNode == null){
                node.SetText( newNode.GetText());
                prevNode = node;
            } else{
                newNode.SetPositionY( prevNode.GetPositionY() + 1 );
                prevNode.SetNext(newNode);
                prevNode = newNode;
            }
        }
        
        //-------------------------------------------------------
        //fazer a ligacao com o no da intrucao seguinte
        prevNode.SetNext(originalNextNode);
        //retornar o ultimo
        return prevNode;
//------------------------------------------------------------------------------
    }
    
    private static NodeInstruction DefineSymbol(String alter , String type , String code , NodeInstruction node , Vector memory)
    throws LanguageException{
        
        String value;
        String name;
        int pos_atr = code.indexOf( Keyword.ATRIBUI);
        if( pos_atr == -1) { // nao tem valores
            name = code.trim();
            //se for constante tem de ser inicializada por isso provoca excepcao
            if (alter.equalsIgnoreCase(Keyword.GetTextKey( Keyword.CONSTANTE)))
                throw new LanguageException(
                        node.GetCharNum(), node.GetText() ,
                       "<" + name + "> É UMA CONSTANTE E DEVE SER INICIALIZADA",
                        " INSIRA UM VALOR PARA O SÍMBOLO  <" + name + ">");
            
                value = Values.getDefault(type);
            } else{
                name = code.substring(0,pos_atr).trim();
                value = code.substring(pos_atr + Keyword.ATRIBUI.length() ).trim();
            }
            
          
            if ( SymbolArray.isArray(name))
                return ExpandDefinirArray.Define(alter,type,name,value,node,memory);
            else
                return ExpandDefinirVariavel.Define(alter,type,name,value,node,memory);
            
        }
        //---------------------------------------------------------------------------------------------------
        
        public static void main(String args[]){
            System.out.println("EXPAND SIMBOL");
            Vector memory = new Vector();
            try{
                NodeInstruction node = new NodeInstruction("literal a,b,c", 0 , 0);
                ExpandVariable( node,0,memory);
                
            } catch( Exception e){
                System.out.println(e.toString());
            }
        }
        
    }
