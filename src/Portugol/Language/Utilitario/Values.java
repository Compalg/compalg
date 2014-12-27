package Portugol.Language.Utilitario;

import Portugol.Language.Analisador.Simbolo;
import javax.swing.JOptionPane;

public class Values {
    
    /**
     * string que representa o valor "verdadeiro"
     */
    public static String VERDADEIRO = "VERDADEIRO";
    /**
     * string que representa o valor "falso"
     */
    public static String FALSO = "FALSO";
    //---------------------------------------------------------------------------
    /**
     * Retorna os valores por defeito dos tipo
     * @param type nome do tipo
     * @return string com o valor por defeito
     */
    public static String getDefault(String type){
        if( type.equalsIgnoreCase("real") ) return "0.0";
        if( type.equalsIgnoreCase("inteiro") ) return "0";
        if( type.equalsIgnoreCase("logico") ) return FALSO;
        if( type.equalsIgnoreCase("caracter") ) return "\" \"";
        if( type.equalsIgnoreCase("texto") ) return "\"\"";
        return "erro";
    }
    //---------------------------------------------------------------------------
    /**
     * Converte um numero para uma string com o inteiro
     * @param val valor do numero
     * @return string com o inteiro
     */
    public static String IntegerToString(double val){
        String num =  Double.toString(val);
        return num.substring(0, num.indexOf('.'));
    }
//---------------------------------------------------------------------------
    /**
     * converte um string para inteiro
     * @param val string com o inteiro
     * @return valor inteiro
     */
    public static int StringToInteger(String val){
        Double d = new Double(val);
        return d.intValue();
    }
//---------------------------------------------------------------------------
    
//---------------------------------------------------------------------------
    /**
     * converte um numero para uma string
     * @param val valor
     * @return string
     */
    public static String DoubleToString(double val){
        String num= Double.toString(val);
        if( num.indexOf('.')==-1) return num + ".0";
        else return num;
    }
//---------------------------------------------------------------------------
    /**
     * converte uma string para numero
     * @param val texto
     * @return valor
     */
    public static double StringToDouble(String val){
        return  Double.valueOf(val);
    }
//---------------------------------------------------------------------------
    /**
     * converte um valor logico para texto
     * @param val valor logico
     * @return texto
     */
    public static String BooleanToString(boolean val){
        if( val ) return VERDADEIRO;
        return FALSO;
    }
//---------------------------------------------------------------------------
    /**
     * converte texto para logico
     * @param val texto com o valor
     * @return valor logico
     */
    public static boolean StringToBoolean(String val){
        if( val.equalsIgnoreCase(VERDADEIRO) ) return true;
        if( val.equalsIgnoreCase(FALSO) ) return false;
        System.out.println("ERRO StringToBoolean ["+ val + "] nao é um boleano");
        return false;
    }
//------------------------------------------------------------------------------
    /**
     * verifica se is um numero
     * @param n texto
     * @return verifica se is um inteiro ou um real
     */
    public static  boolean IsNumber(String n) {
        try{
            double v = Double.parseDouble(n);
            return true;
        } catch ( Exception e ){
            return false;
        }
    }
    //------------------------------------------------------------------------------
    /**
     * verifica se is um numero inteiro
     * @param n text
     * @return verifica se is um numero inteiro
     */
    public static boolean IsInteger(String n){
        if( ! IsNumber(n))
            return false;
        return n.indexOf(".") == -1;
    }
    //------------------------------------------------------------------------------
    /**
     * verifica se is um numero inteiro
     * @param n text
     * @return verifica se is um numero inteiro
     */
    public static boolean IsReal(String n){
        if( ! IsNumber(n))
            return false;
        return n.indexOf(".") != -1;
    }
    //------------------------------------------------------------------------------
    /**
     * verifica se is um valor logico
     * @param val texto
     * @return verifica se is um valor logico
     */
    public static boolean IsBoolean(String val){
        if( val.equalsIgnoreCase(VERDADEIRO) ||
                val.equalsIgnoreCase(FALSO) )   return true;
        
        return false;
    }
    /**
     * verifica se is uma string
     * @return verifica se is uma string
     * @param expr expressao
     */
    public static boolean IsString(String expr){
        if( !expr.startsWith("\"") ||  !expr.endsWith("\""))
            return false;
        //contar os "
        for(int index= 1 ; index < expr.length()-1 ; index++){
            if(expr.charAt(index)=='"' && expr.charAt(index-1)!='\\' )
                return false;
        }
        return true;
    }
    /**
     * verifica se is uma string
     * @return verifica se is uma string
     * @param expr expressao
     */
    public static boolean IsCharacter(String expr){
        return IsString(expr) && expr.length()==3;
    }
    
    //---------------------------------------------------------------------------
    /**
     * converte um valor Texto para String (retira as aspas)
     * @param str string com aspas
     * @return "conjunto de caracteres"
     */
    public static String TextToString(String str){
        if(str.startsWith("\"") && str.endsWith("\""))
            return str.substring(1, str.length() -1);
        return "ERRO TEXT TO STRING";
    }
//---------------------------------------------------------------------------
    /**
     * converte uma string para texto (adiciona-lhe aspas)
     * @return conjunto de caracteres
     * @param str string sem aspas
     */
    public static String StringToText(String str){
        return "\""+ str + "\"";
    }
    //-------------------------------------
    /**
     * Verifica se is um numero um logico ou uma string
     * @param str expressao
     * @return se is um valor
     */
    public static boolean IsValue(String str){
        return IsString(str) || IsBoolean(str) || IsNumber(str);
    }
    
//---------------------------------------------------------------------------
//---------------------------------------------------------------------------
    /**
     * remove os carecteres "" da string
     * @param orig string com ""
     * @return string sem ""
     */
    public static String removeStringComas(String orig){
        String tmp;
        if(orig.startsWith("\"") )
            tmp = orig.substring(1);
        else
            tmp = orig;
        if(tmp.endsWith("\"") )
            return tmp.substring(0, tmp.length()-1);
        else
            return tmp;
    }
    /**
     * retira as comas da string e introduz os caracteres especiais (\n \r, etc.)
     * @param orig string de origem
     * @return valor da string
     */
    public static String getStringValue(String orig){
        StringBuffer tmp = new StringBuffer();
        // remover as aspas se existirem
        orig = removeStringComas(orig);
        // substituir os caracteres de escape        
        for(int i= 0; i< orig.length(); i++){
            switch(orig.charAt(i)){
                case '\\':
                    switch(orig.charAt(i+1)){
                       case 'n':
                                tmp.append("\n");
                                break;
                        case 'r':
                                tmp.append("\r");
                                break;
                        case 't':
                                tmp.append("\t");
                                break;
                        default:
                                tmp.append(orig.charAt(i+1));
                                break;                                                                
                    }// switch i+1
                    //passar para frente
                    i++;
                    break;
                    
                default:
                    tmp.append( orig.charAt(i));
            }
        }
        return tmp.toString();
    }
    
    public static int getTypeOfValue(String value){
        if( Values.IsReal(value)) return Simbolo.REAL;
        if( Values.IsInteger(value)) return Simbolo.INTEIRO;
        if( Values.IsBoolean(value)) return Simbolo.LOGICO;
        return Simbolo.TEXTO;
    }
    
}
