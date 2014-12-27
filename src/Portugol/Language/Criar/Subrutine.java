package Portugol.Language.Criar;

import Editor.Utils.FileManager;
import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Registo;
import Portugol.Language.Consola.ConsoleIO;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Calcular.Ficheiro;
import Portugol.Language.Utilitario.CodeLine;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import java.util.StringTokenizer;
import java.util.Vector;
import Portugol.Language.Calcular.Procedimento;
import Portugol.Language.Calcular.Funcao;
import Portugol.Language.Criar.ExpandEnquanto;
import Portugol.Language.Criar.ExpandFluxogram;
import Portugol.Language.Criar.ExpandSe;
import Portugol.Language.Criar.NodeInstruction;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import javax.swing.JOptionPane;

/**
 * @author Augusto Bilabila original de Antonio manso
 */

public class Subrutine {
    
    public static String VERSION = "Versão:1.0 \t(c) Augusto Bilabila";
    
   
    protected NodeInstruction start;//esto debe ser de metodo principal
    /**
     * apontador para a no que esta a ser executado
     */
    protected NodeInstruction nodeExecute;
    /**
     * indicador se o texto pertence a um comentario do programa
     */
    protected boolean isComented=false;
    /**
     * vector das variaveis em memoria
     */
    public Vector memory;
    
    public static String VerOperator = " " ;//im
    
    /**
     * Constroi um fluxograma
     * @param code programa fonte
     * @throws Portugol.Language.Utils.LanguageException excepcao
     */
    public Subrutine(/*String code*/)throws LanguageException {
       // Construir(code);
        memory = new Vector();
        start = null;

    }
    
     
    /**
     * constroi um fluxograma com um ficheiro de programa
     * @param filename no do ficheiro
     * @throws Portugol.Language.Utils.LanguageException erro
     */
//    public void ReadFile(String filename)throws LanguageException{
//          FileManager f = new FileManager();          
//          Construir( f.ReadFile(filename));        
//    }
    
    /**
     * retorna o inicio do fluxograma
     * @return no onde começa o fluxograma
     */
    public NodeInstruction getStart(){
        return start;
    }    
    /**
     * Controi o fluxograma
     * @param programa Texto do programa
     * @throws Portugol.Language.Utils.LanguageException erro
     */
//    protected void Construir(String programa)throws LanguageException{
//        int charNum = 0;
//        int positionY =0;
//        memory = new Vector();//silva: esto es de metodo principal
//        String instruction; 
//        
//        /*
//        Registo registos = new Registo(programa);
//       // Ficheiro obficheiro = new Ficheiro(programa);
//                
//        // Comentar o registo             
//        registos.ObterVariaveisRegisto();    
//             if(programa.indexOf("registo ")>0)
//             {
//                                 
//
//                 programa = registos.SegundoTratamento();  
//                 programa = registos.declararVariaveis ();
//                 programa = registos.UltimoTratamento();
//                 
//             }
//          // Trata dos procedimentos  
//          Procedimento procedimento = new Procedimento(programa);
//          programa = procedimento.Tratar_Procedimento();
//          
//          // Trata das funções
//          Funcao funcao = new Funcao(programa);
//          programa = funcao.Tratar_funcao();
//          
//          // Trata do Ficheiro
//       //   obficheiro.trataFicheiro1();
//      //    programa = obficheiro.getPrograma();
//       
//        // JOptionPane.showMessageDialog(null,"programa \n"+programa);
//          */
//        System.out.print("programa \n"+programa);
//        // as intrucoes terminam com enter
//        StringTokenizer st = new StringTokenizer(programa,"\n\r");
//               
//        //no anterior (para fazer a ligacao)
//        NodeInstruction previousNode=null;
//        // novo no
//        NodeInstruction newNode=null;
//        start = null;
//        
//        int bill = 0;
//        //fazer a lista seguida
//        while (st.hasMoreTokens()) {
//             
//            //retirar os espacos
//            instruction = st.nextToken();
//            //contar o numero de caracteres
//            charNum += instruction.length() + 1; // terminador
//            //retirar espacos e comentarios
//            instruction = RemoveComentarios(instruction);
//            
//            //se for uma linha vazia
//            if(instruction.length()==0)
//                continue;
//            
//            //normalizar os operadores e sinais
//            //David: tiré la siguiente instruccion porque se repite en el constructor de la otra siguiente
//            //instruction = CodeLine.GetNormalized(instruction);
//       
//            newNode = new NodeInstruction(instruction, charNum-1, 0);
//            newNode.SetPositionY(++positionY);                        
//            
//            
//             if( newNode.GetType() == Keyword.DEFINIR){
//                 if(newNode.GetText().startsWith("string ")){
//                   newNode.SetText(newNode.GetText().replace("string ", "literal "));
//                 }
//                 
//                 if (!newNode.GetText().contains(","))
//                 if(newNode.GetText().startsWith("literal ")&& !newNode.GetText().endsWith("\""))
//                 newNode.SetText(newNode.GetText()+"<-\"\"");
//             }
//             //---------------------------------------------------------------------------------
//              if( newNode.GetType() == Keyword.CALCULAR){
//                  if(newNode.GetText().contains("mod")){
//                   newNode.SetText(newNode.GetText().replace("mod ", "%"));
//                 }
//              }
//             //--------------------------------------------------------------------------------
//              if( newNode.GetType() == Keyword.CALCULAR){
//                  if(newNode.GetText().contains("div")){
//                   newNode.SetText(newNode.GetText().replace("div ", "/"));
//                   VerOperator = "div";
//                 }
//              }
//              
//            
//             //-----------------------------------------------------------------
// 
//              /* David: Para ignorar el llamado a procedimiento, para que pase y despues reconocerlo si fuera posible
//            if( newNode.GetType() == Keyword.DESCONHECIDO)
//                throw new LanguageException(
//                        newNode.GetCharNum(), newNode.GetText(),
//                        " INSTRUÇÃO NÃO RECONHECIDA",
//                        " CONSULTE A AJUDA DA LINGUAGEM" );
//           */
//            //-----------------------------------------------------------------
//            
//            //inicio do fluxograma
//            if( start == null){
//                // se não for um no de inicio
//                if( (newNode.GetType() != Keyword.INICIO)&&(newNode.GetType() != Keyword.REGISTO)&&(newNode.GetType() != Keyword.PROCEDIMENTO) &&(newNode.GetType() != Keyword.FUNCAO))
//                    throw new LanguageException(
//                            newNode.GetCharNum(), newNode.GetText(),
//                            "Os Algoritmos devem começar com a palavra INICIO ou REGISTO (antes do inicio)",
//                            "Coloque uma instrução INICIO no principio do Algoritmo" );
//                
//                start = newNode;
//                previousNode = start;
//            } else{
//                previousNode.SetNext(newNode);
//                previousNode = newNode;
//            }
//      bill++;  }//fim da lista seguida
//        
//        // se nao for um no de FIM
//        if( newNode.GetType() != Keyword.FIM)
//            throw new LanguageException(
//                    newNode.GetCharNum(), newNode.GetText(),
//                    "Os Algoritmos devem terminar com a palavra FIMALGORITMO ou FIM ALGORITMO ",
//                    "Coloque uma intrução FIMALGORITMO no fim do Algoritmo" );
//        //expande cada uma das linhas
//        ExpandFluxogram.ExpandNodes( start , memory);
//        //calcular as posicoes do fluxograma
//      //  FluxogramVisual.CalculateVisual(start);
//    
//   
//    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                              ---------------------------
//------------  E X E C U C A O   D A   I N S T R U C A O   ---------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * Executa uma linha de codigo - normalmente    
     */
    
    public NodeInstruction  ExecuteLine(NodeInstruction node,ConsoleIO console) throws LanguageException{
        int line = node.GetCharNum();
        while( line == node.GetCharNum())
            node = Execute(node,console);
        return node;
    }
        
    public NodeInstruction Execute(NodeInstruction node,ConsoleIO console) throws LanguageException{
        switch(node.GetType() ){
                
            case Keyword.INICIO :
                cleanMemory( node.GetLevel(),memory);
                return node.GetNext();
                
            case Keyword.FIM:
                cleanMemory( node.GetLevel(),memory);
                return start;
                
            case Keyword.DEFINIR:
                Variavel.defineVAR(node,memory);
                return node.GetNext();
                
            case Keyword.CALCULAR :
                executeCalculate(node.GetText());
                return node.GetNext();
                
            case Keyword.LEIA:
                executeREAD(node.GetText(),console);
                return  node.GetNext();
                
            case Keyword.ESCREVA:
                executeWRITE(node.GetText(), console);
                return node.GetNext();
            
            case Keyword.LIMPATELA:
                         console.Clear();
                //return  node.GetNext();
                
            case Keyword.CONECTOR:
            case Keyword.FAZ:
            case Keyword.REPETE:
            case Keyword.ESCOLHA :
                return node.GetNext();
                
                // verifica o passo do for e modifica a condiçao se necessario
            case Keyword.PASSO:
                String value = Expressao.Evaluate(node.GetText(),memory);
                //avalia o passo
                double valor =Values.StringToDouble(value);
                //passo nulo
                if(valor == 0.0)
                    throw new LanguageException("ERRO - o PASSO do ciclo PARA é zero","Corrija o PASSO");
                //passa para o no da condiçao
                NodeInstruction forNode = node.GetNext();
                String instruction = forNode.GetText();
                // se o passo for negativo a condiçao
                if( valor > 0 )
                    forNode.SetText( instruction.replaceFirst(">=" , "<=") );
                else
                    forNode.SetText( instruction.replaceFirst("<=" , ">=") );
                return  node.GetNext();
            
            case Keyword.ATE:
            case Keyword.FAZENQUANTO:
            case Keyword.ENQUANTO:    
            case Keyword.SE:    
            case Keyword.PARA :
                String compare = Expressao.Evaluate(node.GetText(),memory);
                //variaveis defenidas dentro do bloco
                cleanMemory( node.GetLevel()+1,memory);
                if(compare.equalsIgnoreCase( Values.VERDADEIRO))
                    return  node.GetIfTrue();
                else
                    return  node.GetIfFalse();
                                
        }       
        throw new LanguageException("ERRRO - NODO DESCONHECIDO " , node.toString());
        
    }
    
//------------------------------------------------------------------------------
//------------                                             ---------------------
//------------         C A L C U L A R                     ---------------------
//------------                                              --------------------
//------------------------------------------------------------------------------      
    protected void executeCalculate(String str)  throws LanguageException{
        int pos = str.indexOf(Keyword.ATRIBUI);
        String var = str.substring(0, pos).trim();
        String values = str.substring(pos + Keyword.ATRIBUI.length() ).trim();
        
        String newValue = Expressao.Evaluate(values,memory);
        Variavel.replaceVariableValue(var, newValue,memory);
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * limpa a memoria,  os niveis de memoria. <br>
     * Utiliza-se no ciclo para limpar as variaveis <br>
     * locais ao ciclo.
     * @param level nivel a partir do qual vai limpar
     */
    
    protected static void cleanMemory(int level, Vector memory){//sbr
        for( int index = memory.size()-1 ; index >=0 ; index--){
            Simbolo v = (Simbolo) memory.get(index);
            //elimina as variaveis superiores ou iguais ao nivel
            if (v.getLevel()>= level )
                memory.remove(index);
            else
                break;
        }
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                             ----------------------------
//------------         E S C R E V E R                     ----------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * executa a instrucao de escrever
     * @param str string com a instrucao
     * @param console consola para escrever
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    protected void executeWRITE(String str,ConsoleIO console) throws LanguageException{
        str = str.substring( Keyword.GetTextKey( Keyword.ESCREVA ).length()); 
        IteratorCodeParams tok = new IteratorCodeParams(str.trim());
        //WriteTokenizer tok = new  WriteTokenizer(str);
        StringBuffer line = new StringBuffer();
        String elemLine ;
        //parametros
        while(tok.hasMoreElements()){
            String elem = tok.current();
            tok.next();
            
            if( !Values.IsString(elem) )
                elemLine = Expressao.Evaluate(elem,memory);
            else
                elemLine = elem;
            line.append( Values.getStringValue(elemLine));
        }
        console.write(line.toString());
    }
    
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                             ----------------------------
//------------         L E R                               ----------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * Executa a instrucao de leitura
     * @param str linha de codigo
     * @param console consola de leitura
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    
    protected void executeREAD(String str,ConsoleIO console) throws LanguageException{
        //ler = 3 caracteres
        String varName = str.substring(3).trim();
       
        Simbolo var = Variavel.getVariable(varName,memory);
        // fazer o set ao index do array
        if( var instanceof SymbolArray)
            ((SymbolArray) var).SetIndex(varName,memory);
        
        String value = console.read(var);
        var.setValue(value);
    }
    
    
//=============================================================================
    public   String RemoveComentarios(String str){
        StringBuffer newStr = new StringBuffer();
        for(int index=0 ; index< str.length() ; index++ ) {
            switch (str.charAt(index)) {
                case '/':
                    // comentario "//"
                    if(index +1 < str.length() && str.charAt(index+1)=='/')
                        return newStr.toString().trim();
                    //inicio de um comentario /*
                    if(index +1 < str.length() && str.charAt(index+1)=='*')
                        isComented=true;
                    else
                        //fim do comentario */
                        if( index >0 && str.charAt(index-1)=='*')
                            isComented=false;
                    //introduz caracter /
                        else
                            newStr.append(str.charAt(index));
                    break;
                default:
                    // se nao for comentario
                    if( !isComented)
                        newStr.append(str.charAt(index));
            }
        }
        return newStr.toString().trim();
    }
    
    /**
     * string
     * @return string
     */
    public String toString(){
        StringBuffer str = new StringBuffer();
        NodeInstruction pt = start;
        while( pt != null){
            str.append( GetCode(pt));
            pt = pt.next;
        }
        return str.toString();
    }
    
    /**
     * calcula o texto de um nodo
     * @param node nodo de origem
     * @return o texto de um nodo
     */
   public static String GetCode(NodeInstruction node){//im
        
        if( node.GetType() == Keyword.CONECTOR )    return "";
        if( node.GetType() == Keyword.SE )    return ExpandSe.toString(node);
        if( node.GetType() == Keyword.ENQUANTO ) return ExpandEnquanto.toString(node);
        return node.toString()+ "\n";
    }
    
}