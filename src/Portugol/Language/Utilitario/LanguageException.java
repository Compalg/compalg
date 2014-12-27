
package Portugol.Language.Utilitario;

public class LanguageException extends Exception {
    
    /**
     * numero da linha
     */
    public int line ; // numero da linha
    /**
     * texto da linha
     */
    public String codeLine;  //Linha de codigo

    public String error;

    public String solution;
    
    public LanguageException(int l, String code, String e, String s) {
        super(e);
        line = l;
        codeLine = code;
        error = e;
        solution = s;
    }
    
  
    public LanguageException(String e, String s) {
        super(e);
        line = 0;
        codeLine = "";
        error = e;
        solution = s;
    }
    
    public void Show(){
       System.out.println(GetError());
    }
    
   
    public String GetError(){
        StringBuffer str = new StringBuffer();
        str.append("INSTRUÇÃO:\t" +codeLine  + "\n");
        str.append("ERRO:\t" + error  + "\n");
        str.append("SOLUÇÃO:\t" +solution +"\n");
        str.append("LINHA:\t" +line );
        return str.toString();
    }
    
    /**
     * return object String
     * @return excepcao
     */
    public String toString(){
       return GetError();
    }
}
