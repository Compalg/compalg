package Portugol.Language.Utilitario;

import Portugol.Language.Calcular.CalculusElement;

public class IteratorElements {
    
    
    String element;
    IteratorIndex it;
    
    //itera uma string  atraves dos operadores e destes simbolos
    private  static String OTHERSEPARATORS = "(),{}";//David: "(),{}"
    
    public IteratorElements(String code) {
        it = new IteratorIndex(CodeLine.GetNormalized(code));
        next();
    }
    
    //-------------------------------------------------------------------
    public boolean hasMoreElements(){
        return element.length() > 0;
    }
    
    private boolean isSeparator(String str){
        return OTHERSEPARATORS.indexOf(str) != -1 ||
                CalculusElement.IsOperator(str);
    }
//-------------------------------------------------------------------
    public void next() {
        element = it.current();
        it.next();
        
        if(! isSeparator(element) ) {
        //adicionar ate ao proximo separador
            while(  it.hasMoreElements() && ! isSeparator(it.current()) ){
                element += " " + it.current();
                it.next();
            }
        }
    }
    
    
    /**
     * retorna o elemento corrente
     * @return retorna o elemento corrente
     */
    public String current(){
        return element;
    }
    
    
    public static void main(String args[]){
        System.out.println("ITERATOR");
        String str = "((-2 + -3)*-5/valor[123*345][23*2],23*5)";
        //String str = "3--5 ";
        str = CodeLine.GetNormalized(str);
        System.out.println("STR:<" + str + ">");
//        IteratorElements it = new IteratorElements(str);
//        
//        while( it.hasMoreElements()){
//            System.out.println(it.current());
//            it.next();
//        }
    }
}
