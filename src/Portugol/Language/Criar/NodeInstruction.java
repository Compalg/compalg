package Portugol.Language.Criar;

import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Utilitario.CodeLine;
//import VisualFluxogram.Patterns.Forma; // Uso para o fluxograma

public class NodeInstruction {
    //posicao no editor texto
    /**
     */
    protected   int     charNum;
    /**
     * nivel do nodo no fluxograma
     */
    protected   int     level;
    /**
     * posicao x do simbolo
     */
    protected   double  positionX;
    /**
     * posicao y do simbolo
     */
    protected   double  positionY;
    /**
     * representacao grafica do simbolo
     */
// protected   Forma   visual;    // fluxograma
    /**
     * texto
     */
    protected   String  text;
    /**
     * tipo
     */
    protected   int     type;
    
    
    /**
     * ponteiro par o proximo nodo
     */
    protected   NodeInstruction     next;
    /**
     * ponteiro par o nodo se verdadeiro
     */
    protected   NodeInstruction     ifTrue;
    /**
     * ponteiro par o nodo se falso
     */
    protected   NodeInstruction     ifFalse;
  
    public boolean EsReferencia;
    /**
     * construtor
     * @param instruction linha de codigo
     * @param charNum nó do caracter da primeira letra
     * @param level nivel do codigo
     */
    public NodeInstruction(String instruction, int charNum , int level) {
        this.text =  CodeLine.GetNormalized(instruction);
        this.type = Keyword.GetKey(instruction);
        this.level = level;
        this.positionX = 0;
        this.positionY = 0;
        this.next =null;
        this.ifTrue =null;
        this.ifFalse =null;
        this.charNum = charNum;
        this.EsReferencia = false;
    }
        
    /**
     * construtor cópia
     * @param node no original
     */
    public NodeInstruction(NodeInstruction node) {
        this.text =  node.text;
        this.type = node.type;
        this.level = node.level;
        this.next = node.next;
        this.positionX = node.positionX;
        this.positionY = node.positionY;
        this.ifTrue = node.ifTrue;
        this.ifFalse = node.ifFalse;
        this.charNum = node.charNum;   
        this.EsReferencia = node.EsReferencia;
    }
    
    
    public int GetType(){
        return type;
    }
    
    /**
     * altera o tipo do nodo
     * @param newType novo tipo
     */
    public void SetType(int newType){
        type = newType;
    }
    
    /**
     * retorna o texto
     * @return texto
     */
    public String GetText(){
        return text;
    }
    
    public String GetTextKey(){
        return Keyword.GetTextKey(type);
    }
            
    
    /**
     * retorna o texto
     * @return texto
     */
    public String GetIntruction(){
        StringBuffer str = new StringBuffer();
        str.append(Keyword.GetTextKey(GetType()) );
        str.append("\t");
        str.append(text);
        return str.toString();
    }
    
    /**
     * altera o texto
     * @param newText novo texto
     */
    public void SetText(String newText){
        text = CodeLine.GetNormalized(newText);
    }
    
    /**
     * altera o ponteiro para o proximo nodo
     * @param n novo no
     */
    public void SetNext(NodeInstruction n){
        next = n;
    }
    
    /**
     * altera o ponteiro para o nodo verdadeiro
     * @param n no
     */
    public void SetIfTrue(NodeInstruction n){
        ifTrue = n;
    }
    
    /**
     * altera o ponteiro para o nodo falso
     * @param n no
     */
    public void SetIfFalse(NodeInstruction n){
        ifFalse = n;
    }
      
    /**
     * retorna o proximo nodo
     * @return retorna o proximo nodo
     */
    public NodeInstruction GetNext(){
        return next;
    }
    
    /**
     * retorna o nodo se verdadeiro
     * @return retorna o nodo se verdadeiro
     */
    public NodeInstruction GetIfTrue(){
        return ifTrue;
    }
    
    /**
     * retorna o nodo se falso
     * @return retorna o nodo se falso
     */
    public NodeInstruction GetIfFalse(){
        return ifFalse;
    }
    
    /**
     * retorna o nivel do nodo
     * @return nivel
     */
    public int GetLevel(){
        return level;
    }
    /**
     * altera o nivel
     * @param lv novo nivel
     */
    public void  SetLevel(int lv){
        level=lv;
    }
    /**
     * incrementa o nivel do nodo
     */
    public void  IncrementLevel(){
        level++;
    }
    
    /**
     * decrementa o nivel do nodo
     */
    public void  DecrementLevel(){
        level--;
    }
    
    /**
     * retorna a posicao x
     * @return retorna a posicao x
     */
    public double GetPositionX(){
        return positionX;
    }
    /**
     * Atera a posicao x
     * @param p novo x
     */
    public void  SetPositionX(double p){
        positionX = p;
    }
    /**
     * retorna a posicao y
     * @return retorna a posicao x
     */
    public double GetPositionY(){
        return positionY;
    }
    /**
     * Atera a posicao y
     * @param p posicao y
     */
    public void  SetPositionY(double p){
        positionY = p;
    }
    
    
    /**
     * numero do caracter
     * @return numero do caracter
     */
    public int GetCharNum(){
        return charNum;
    }
    /**
     * altera o numero do caracter
     * @param cn novo numero do caracter
     */
    public void  SetCharNum(int cn){
        charNum=cn;
    }
    
    /**
     * se is um nodo que abre um ciclo
     * @return se for um no que abre um ciclo
     */
    public boolean IsNodeOpen(){
        return
                type == Keyword.INICIO ||
                type == Keyword.SE     ||
                type == Keyword.PARA   ||
                type == Keyword.ENQUANTO ||
                type == Keyword.FAZ    ||
                type == Keyword.ESCOLHA ||
                type == Keyword.REGISTO ||
                type == Keyword.PROCEDIMENTO ||
                type == Keyword.FUNCAO ||
                type == Keyword.REPETE ;
    }
    
    /**
     * se for um no que fecha um ciclo
     * @return se for um no que fecha um ciclo
     */
    public boolean IsNodeClose(){
        return
                type == Keyword.FIM ||
                type == Keyword.FIMSE ||
                type == Keyword.FIMPARA ||
                type == Keyword.FAZENQUANTO ||
                type == Keyword.FIMENQUANTO ||
                type == Keyword.FIMESCOLHE ||
                type == Keyword.FIMREGISTO ||
                type == Keyword.FIMPROCEDIMENTO ||
                type == Keyword.FIMFUNCAO ||
                type == Keyword.ATE ;
        
    }
    
    /**
     * texto
     * @return texto
     */
    public String toString(){
        StringBuffer str = new StringBuffer();
        str.append(level + "\t");
        str.append("[" + positionY + "," + positionX + "]\t");
        str.append(Keyword.GetTextKey(GetType()) );
        while( str.length() < 35)
            str.append(" ");
        str.append("\t");
        for(int i=0; i< level ; i++)
            str.append("\t");
        str.append(text);
        
        return str.toString();
    }
    
}
