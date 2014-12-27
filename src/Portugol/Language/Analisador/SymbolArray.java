package Portugol.Language.Analisador;

import Portugol.Language.Calcular.Calculador;
import Portugol.Language.Utilitario.IteratorArray;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;

public class SymbolArray extends Simbolo{
    
    /** Creates a new instance of Array */
    Vector dataValues = new Vector();
    Vector indexHeights = new Vector();
    Vector indexLimits = new Vector();
    
    // index corrent
    int currentIndex =-1;
    private int numElements = 1;
    
    // definition tem os indexes e os valores
    public SymbolArray(String modify,String type,String name, String index, String values, int level, Vector memory)
    throws LanguageException {
        
        if(modify.equalsIgnoreCase(Keyword.GetTextKey( Keyword.CONSTANTE)))
            this.isConst = true;
        else
            this.isConst =false;
        
        this.type = getType(type);
        
        this.name = name.trim();
        
        this.value = this.getDefaultValue(this.type);
        
        
        this.level = level;        
        MakeArray(index,memory);
        PutValues(values,memory);
    }
    
    //coloca o current index na posicao pretendida
    public void SetIndex(String varName, Vector memory){
        IteratorArray iter = new IteratorArray(varName);
        String nome = iter.getNext() ;
        while( iter.hasMoreElements()){
            nome+="[";
            String index = iter.getNext();
            String value = Expressao.Evaluate(index,memory);
            nome += value+ "]";            
        }
        try{
            this.currentIndex = this.getFlatIndex(nome);
        }catch( Exception e){
            this.currentIndex = -1;
        }
        
    }
   //-----------------------------------------------------------------------
    public void setValue(String val) throws LanguageException{
        if( this.isConst)
            throw new LanguageException
                    ("O SIMBOLO " + this.name + " É UMA CONSTANTE, POR ISSO, NÃO PODE RECEBER VALOR",
                    " ALTERE O SIMBOLO PARA VARIÁVEL, SE FOR O TEU OBJECTIVO");
        value = getNormalizedValue(val);
        dataValues.set(currentIndex,value);
    }
    
    /**
     * retorna o valor
     * @return valor
     */
    public String getValue(){
        return (String) dataValues.get(currentIndex);
    }
    
    private void MakeArray(String  indexDefs, Vector memory) throws LanguageException {
        IteratorArray iter = new IteratorArray(indexDefs);
        numElements = 1;
        while( iter.hasMoreElements()){
            String exp = iter.getNext();
            String result;
            try{
                result = Expressao.Evaluate(exp,memory);
            }catch(Exception e){
                throw new LanguageException("O INDICE " + exp + " NÃO É UMA EXPRESSÃO VÁLIDA",
                                            "DEFINA UMA EXPRESSÃO VÁLIDA PARA O INDICE");
            }
            if( !Values.IsInteger( result) )
                throw new LanguageException(exp + " = " + result + " NÃO É UMA VARIÁVEL INTEIRA","");
            int number = Integer.parseInt(result);
            if( number <= 0 )
                throw new LanguageException(exp + " = " + result + " NÃO É UM VALOR VÁLIDO","");
            
            indexLimits.add(number);
            indexHeights.add(0);
            numElements *= number;
        }
        // peso de cada indice no array
        // necessario para os tornar linear [2][3] => 12
        int height = 1;
        for(int i= indexLimits.size() -1 ; i >=0 ; i--){
            indexHeights.set(i,height);
            height *= Integer.parseInt(indexLimits.get(i).toString());
        }
    }
    //////////////////////////////////////////////////////////////////////////////
    private void PutValues(String  values, Vector memory)throws LanguageException{
        IteratorCodeParams iter = new IteratorCodeParams(values , ",{}");
        int index =0;
        while( iter.hasMoreElements()){
            String value = iter.current();
            //Avaliar a expressao
            // se nao for possivel avaliar provoca erro
            String result;
            try {
                result = Expressao.Evaluate(value,memory);
            } catch( Exception e){
                throw new  LanguageException(
                        "ERRO NA EXPRESSÃO :" + value,
                        "VERIFIQUE BEM ESTA EXPRESSÃO <" +  value + ">");
            }
            
            //verificar se o resultado da expressao e compativel com a variavel
            if( !Simbolo.IsCompatible( type, result) )
                throw new  LanguageException(
                        "O valor <" + result + "> NÃO É PERMITIDO PARA A VARIÁVEL " + type ,
                        " VERIFIQUE BEM ESTA EXPRESSÃO :" + value);
            //normalizar o resultado
            dataValues.add(getNormalizedValue(result));
            index++;
            if( index > this.numElements)
                throw new LanguageException(values + " TEM MAIS QUE " + numElements + " ELEMENTOS" , " DEFINA MENOS ELEMENTOS");
            iter.next();
        }
        
        String defValue = getDefaultValue(type);
        for(int i= index; i< numElements ; i++)
            dataValues.add( defValue + "");
    }
    
    //////////////////////////////////////////////////////////////////////////////
    /**
     * retorna o valor
     * @return valor
     */
    public String getValue(String var){
        try{
            int index = getFlatIndex(var);
            return dataValues.get(index).toString();
        }catch ( Exception e){
            return e.getMessage();
        }
    }
    
    /**
     * retorna o valor
     * @return valor
     */
    public String getFlatValue(int index){
        if( index >=0 && index < this.numElements)
            return dataValues.get(index).toString();
        else
            return "ERRO";
    }
    //////////////////////////////////////////////////////////////////////////////
    /**
     * retorna o valor
     */
    public void setValue(String var, String value)throws Exception{
        // introduzir o value em this
        // causa excepcoes se houver erro em value e normaliza o valor
        this.value = getNormalizedValue(value);
        int index = getFlatIndex(var);
        dataValues.set(index , this.value);
    }
    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////
    private int getFlatIndex(String var) throws Exception{
        int index = 0;
        IteratorArray iter = new IteratorArray(var.trim());
        String nameVar = iter.getNext().trim();
        if( !nameVar.equalsIgnoreCase( name))
            throw new Exception("ERRO NO NOME da VARIAVEL");
        
        int numIndex = 0;
        while( iter.hasMoreElements()){
            String exp = iter.getNext();
            
            String result = Calculador.CalulateValue(exp);
            int number = Integer.parseInt(result);
            if( number < 0 )
                throw new Exception("ERRO NO INDECE " + exp);
            if( number >=  Integer.parseInt(indexLimits.get(numIndex).toString())  )
                throw new Exception("ERRO NO INDECE :" + exp + " FORA DOS LIMITES ");
            if( numIndex > indexLimits.size() )
                throw new Exception("ERRO NO INDECE :" + exp + " - NAO DEFENIDO");
            index +=  number * Integer.parseInt(indexHeights.get(numIndex).toString());
            numIndex++;
        }
        return index;
    }
    
    
    
    /**
     * verifica se uma expressao is um array
     * @param name expressao
     * @return array[] ?
     */
    public static boolean isArray(String name){
        int c1 = name.indexOf('[');
        int c2 = name.indexOf(']');
        return c1*c2 > 0 && c2 > c1;
    }
    
    
    public String toString(){
        return name + " = " + dataValues.toString();
    }
    
    public Vector getDimensions(){
        // fazer uma copia
        // para evitar ser alterado
        return indexLimits;
    }
    
    public int getNumElements(){
        return numElements;
    }
    
    /**
     * nome =
     * @param var varivel a comparar
     * @return nome = paramentro
     */
    public boolean nameEqual(String var){
        int p = var.indexOf('[');
        if( p> 0)
            return name.equalsIgnoreCase( var.substring(0,p).trim());
        else
            return var.equalsIgnoreCase(name);
    }
    
    
    public static void main(String args[]){
        System.out.println("ARRAYS");
        Vector memory = new Vector();
        try{
            memory.add( new Simbolo("variavel","inteiro", "numero1", "10", 0) );
            memory.add( new Simbolo("variavel","inteiro", "numero2", "20", 0) );
            memory.add( new Simbolo("variavel","inteiro", "numero3", "30", 0) );
            SymbolArray a = new SymbolArray("variavel", "real","vetor" , "[2][2][2]" , " { 1+3 ,2,3,4,5,6,7,8} ",  1, memory);
            
            //System.out.println("Nome\t:" + a.getName());
            //String var ="vetor[1][1][1]";
            //System.out.println(a.toString());
            //a.setValue(var,"20.0");
            String var ="vetor[numero1/10][0][numero3/numero1 - 2]";
            a.SetIndex(var,memory);
            a.setValue("10");
            System.out.println(" Var = " + a.getValue());
            //a.setValue(var,"10");
            System.out.println(a.toString());
            //System.out.println(var + " = " + a.getValue(var));
        }catch( Exception e){
            System.out.println(e.toString());
        }
    }
    
}
