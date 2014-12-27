package Portugol.Language.Analisador;

import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import javax.swing.JOptionPane;

public class Simbolo {
    /**
     * tipo Vazio
     */
    public final static int VAZIO     = 0;
    /**
     * tipo Logico
     */
    public final static int LOGICO     = 1;
    /**
     * tipo Real
     */
    public final static int REAL      = 2;
    /**
     * tipo Inteiro
     */
    public final static int INTEIRO   = 3;
    /**
     * tipo caracter (simbolo da tabela ASCII )
     */
    public final static int CARACTER = 4;
    /**
     * tipo Texto (strings)
     */
    public final static int TEXTO      = 5;
    
    /**
     * tipo Desconhecido
     */
    public final static int DESCONHECIDO    = -1;
    /**
     * se is constante ou variavel
     */
    protected boolean isConst;
    /**
     * tipo de dados
     */
    protected int type;
    /**
     * nome do simbolo
     */
    protected String name;
            
    /**
     * valor
     */
    protected String value;
    /**
     * nivel do simbolo
     */
    protected int level;
    
    public Simbolo(String modify,String type,String name, String value, int level)
    throws LanguageException {
        if(modify.equalsIgnoreCase(Keyword.GetTextKey( Keyword.CONSTANTE)))
            this.isConst = true;
        else
            this.isConst =false;
        
        this.type = getType(type);
        
        this.name = name.trim();
        
        if(value.length()==0)
            this.value = this.getDefaultValue(this.type);
        else
            this.value = getNormalizedValue(value);
        
        this.level = level;
    }
    
    public Simbolo(int type, String name){
        this.isConst =false;
        
        this.type = type;
        
        this.name = name.trim();
        
        this.value = this.getDefaultValue(this.type);
        
        this.level = 0;
    }
    
    protected Simbolo(){
        // somente para as classes derivadas (ex. array)
    }
    
    public boolean IsCompatible(int type){
        if( this.type == type) return true;
        if( this.type == Simbolo.REAL && type==Simbolo.INTEIRO) return true;
        if( this.type == Simbolo.TEXTO && type==Simbolo.CARACTER) return true;
        return false;
    }
    /**
     * tipo da variavel
     * @param t texto com o tipo
     * @return tipo
     */
    public static  int getType(String t){
        if(t.equalsIgnoreCase(Keyword.GetTextKey(Keyword.LOGICO ))) return LOGICO;
        if(t.equalsIgnoreCase(Keyword.GetTextKey(Keyword.TEXTO ))) return TEXTO;
        if(t.equalsIgnoreCase(Keyword.GetTextKey(Keyword.INTEIRO ))) return INTEIRO;
        if(t.equalsIgnoreCase(Keyword.GetTextKey(Keyword.REAL ))) return REAL;
        if(t.equalsIgnoreCase(Keyword.GetTextKey(Keyword.CARACTER ))) return CARACTER;
        return DESCONHECIDO;
    }
    
    public void setType(int tipo){
        this.type=tipo;
    }
    
    public void setLevel(int l){
        level=l;
    }
    
    /**
     * devolve os valores por defeito de cada tipo
     * @param varType tipo
     * @return valor
     */
    public static  String getDefaultValue(int varType){
        if(varType == LOGICO) return Values.FALSO;
        if(varType == TEXTO ) return new String("\"\"");
        if(varType == INTEIRO ) return "0";
        if(varType == REAL ) return "0.0";
        if(varType == CARACTER) return new String("\"_\"");
        return "ERRO";
    }
    
    /**
     * devolve os valores por defeito de cada tipo e 1 para os numericos
     * @param varType tipo
     * @return valor
     */
    public static  String getSafeDefaultValue(int varType){
        if(varType == LOGICO) return Values.FALSO;
        if(varType == TEXTO ) return new String("\"\"");
        if(varType == INTEIRO ) return "1";
        if(varType == REAL ) return "1.0";
        if(varType == CARACTER) return new String("\" \" ");
        return "ERRO";
    }
    /**
     * devolve os valores por defeito de cada tipo
     * @param varType tipo
     * @return valor
     */
    public static  String getDefaultValue(String varType){
        return getDefaultValue(getType(varType));
    }
    
    //------------------------------------------------------------------------
    public  boolean isNumber(){
        if( type == REAL) return true;
        if(type == INTEIRO) return true;
        return false;
    }
    
    //-----------------------------------------------------------------------
    public  boolean isString(){
        if( type == TEXTO) return true;
        if(type == CARACTER) return true;
        return false;
    }
    
    //------------------------------------------------------------------------
    protected void setCanonicalValue(String val) throws LanguageException{
        if(this.isString())
            value = getNormalizedValue("\"" + val + "\"");
        else
            value = getNormalizedValue(val);
    }
    
   //-----------------------------------------------------------------------
    public void setHardValue(String val){
        value = val;
    }

    //------------------------------------------------------------------------
    public void setValue(String val) throws LanguageException{
        if( this.isConst)
            throw new LanguageException
                    ("ESTE SÍMBOLO " + this.name + " É UMA CONSTANTE, NÃO PODE RECEBER VALORES",
                    " ALTERE O SIMBOLO PARA VARIAVEL");
        value = getNormalizedValue(val);
    }
    //
    protected  String getNormalizedValue(String val)throws LanguageException{
        val = val.trim();
        
        if( type == TEXTO){
            if(val.length() <2 || !Values.IsString(val))
                throw new LanguageException(
                        0, "", name +" É UMA VARIÁVEL DO TIPO STRING",  "\""+val + "\" NÃO É UM TEXTO");
            return val;
        } else if( type == CARACTER ) {
            
            if( !Values.IsString(val) || val.length() != 3)
                throw new LanguageException(
                        0, "", name +" É UMA VARIÁVEL DO TIPO CARACTER",  "\""+val + "\" NÃO É UM CARACTER");
            return  val;
        } else if( type == LOGICO ) {
            if( !Values.IsBoolean(val))
                throw new LanguageException(
                        0, "", name +" É UMA VARIÁVEL DO TIPO LOGICO", "\""+val + "\"  NÃO É UM VALOR LÓGICO VÁLIDO");
             return val.toUpperCase();
        } else if( type == REAL ) {
            if(!Values.IsNumber(val))
                throw new LanguageException(
                        0, "", name +" É UMA VARIÁVEL DO TIPO REAL", "\""+val + "\"  NÃO É UM NÚMERO REAL VÁLIDO");
            
            double d = Values.StringToDouble(val);
            return Values.DoubleToString(d);
        } else if( type == INTEIRO ) {
            if(!Values.IsValue(val))
                throw new LanguageException(
                        0, "", name +" É UMA VARIÁVEL DO TIPO INTEIRO", "\""+val + "\" NÃO É UM NÚMERO INTEIRO VÁLIDO");
            int i = Values.StringToInteger(val);
            return  "" + i;
        }
        return "ERRO TIPO DE VARIAVEL DESCONHECIDO";
    }
    
    
    /**
     * retorna o valor
     * @return valor
     */
    public String getValue(){
        return value;
    }
    /**
     * devolve os valores por defeito de cada tipo
     * @return valor por defeito
     */
    public String getDefaultValue(){
        return getDefaultValue( this.type);
    }
    
    
      /**
     * devolve os valores por defeito de cada tipo e os numericos a 1 por causa das divisoes
     * @return valor por defeito
     */
    public String getSafeDefaultValue(){
        return getSafeDefaultValue( this.type);
    }
    
     public static  String getStringType(int t){
        if(t == LOGICO)return "LOGICO";
        if(t == TEXTO) return "TEXTO";
        if(t == CARACTER) return "CARACTER";
        if(t == REAL) return "REAL";
        if(t == INTEIRO) return "INTEIRO";
        return "DESCONHECIDO";
    }
     
    public String getStringType(){
        if(type == LOGICO)return "LOGICO";
        if(type == TEXTO) return "TEXTO";
        if(type == CARACTER) return "CARACTER";
        if(type == REAL) return "REAL";
        if(type == INTEIRO) return "INTEIRO";
        return "DESCONHECIDO";
    }
    
    /**
     * tipo da variavel
     * @return tipo
     */
    public int getType(){
        return type;
    }
    
    /**
     * gets nome
     * @return nome
     */
    public String getName(){
        return name;
    }
    
    /**
     * nivel
     * @return nivel da varivel
     */
    public int getLevel(){
        return level;
    }
    
    /**
     * verifica se is constante
     * @return a constante
     */
    public boolean isConstant(){
        return this.isConst;
    }
    
    /**
     * return object String
     * @return informacao  da variavel
     */
    public String toString(){
        StringBuffer str = new StringBuffer();
        if( isConstant())
            str.append("\tCONSTANTE\n");
        else
            str.append("\tVARIAVEL\n");
        str.append("nome\t:" + getName() + "\n");
        str.append("valor\t:" +getValue() + "\n");
        str.append("tipo\t:" + getStringType()+ "\n");
        str.append("nivel\t:" +getLevel()+ "\n");
        return str.toString();
    }
    
    /**
     * nome =
     * @param var varivel a comparar
     * @return nome = paramentro
     */
    public boolean nameEqual(String var){
        return var.equalsIgnoreCase(name);
    }
    
    /**
     * verifica se o valor e o tipo sao compativeis
     * @param type1 type de dados
     * @param value valor
     * @return compatibilidade
     */
    public static boolean IsCompatible(int type1, String value){
        
        if(type1 == Simbolo.REAL && Values.IsNumber(value)) return true;
        if(type1 == Simbolo.INTEIRO && Values.IsInteger(value)) return true;
        if(type1 == Simbolo.LOGICO && Values.IsBoolean(value)) return true;
        if(type1 == Simbolo.CARACTER && Values.IsCharacter(value)) return true;
        if(type1 == Simbolo.TEXTO && Values.IsString(value)) return true;
        return false;
    }
    
    /**
     * igual
     * @param other segundo argumento
     * @return sao iguais?
     */
    public boolean equal(Simbolo other){
        if( !nameEqual(other.name)) return false;
        return other.level == level;
    }
}
