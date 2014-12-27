package Editor.help;

import Portugol.Language.Calcular.CalculusElement;

/**
 *
 * @author Bilabila
 */
public class HelpFileName {
    public static String getHelpFile(String word){
        String keyword = "index";
        if(word.equalsIgnoreCase("se") ) keyword = "se";
        else if(word.equalsIgnoreCase("entao") ) keyword = "se";
        else if(word.equalsIgnoreCase("senao") ) keyword = "se";
        else if(word.equalsIgnoreCase("fimse") ) keyword = "se";
        
        
        else if(word.equalsIgnoreCase("escolhe") ) keyword = "escolhe";
        else if(word.equalsIgnoreCase("caso") ) keyword = "escolhe";
        else if(word.equalsIgnoreCase("defeito") ) keyword = "escolhe";
        else if(word.equalsIgnoreCase("fimescolhe") ) keyword = "escolhe";
        
        else if(word.equalsIgnoreCase("enquanto") ) keyword = "enquanto";
        else if(word.equalsIgnoreCase("fimenquanto") ) keyword = "enquanto";
        
        else if(word.equalsIgnoreCase("repete") ) keyword = "repete";
        
        else if(word.equalsIgnoreCase("faz") ) keyword = "faz";
        
        else if(word.equalsIgnoreCase("para") ) keyword = "para";
        else if(word.equalsIgnoreCase("passo") ) keyword = "para";
        
        else if(word.equalsIgnoreCase("ler") ) keyword = "ler";
        else if(word.equalsIgnoreCase("escrever") ) keyword = "escrever";
        
        
        else if(word.equalsIgnoreCase("INTEIRO")) keyword = "tipo_dados_basicos";
        else if(word.equalsIgnoreCase("REAL")) keyword = "tipo_dados_basicos";
        else if(word.equalsIgnoreCase("CARACTER")) keyword = "tipo_dados_basicos";
        else if(word.equalsIgnoreCase("LOGICO")) keyword = "tipo_dados_basicos";
        else if(word.equalsIgnoreCase("TEXTO")) keyword = "tipo_dados_basicos";
        
        else if(word.equalsIgnoreCase("CONSTANTE")) keyword = "tipo_dados_constantes";
        else if(word.equalsIgnoreCase("E")) keyword = "operadores_logicos";
        else if(word.equalsIgnoreCase("OU")) keyword = "operadores_logicos";
        else if(word.equalsIgnoreCase("NAO")) keyword = "operadores_logicos";
        else if(word.equalsIgnoreCase("VERDADEIRO")) keyword = "operadores_logicos";
        else if(word.equalsIgnoreCase("FALSO")) keyword = "operadores_logicos";
        else if( CalculusElement.IsFunction(word) ) keyword = "funcoes_biblioteca";
        else{
            String opAritm ="+-*/%^";
            String opRelac ="<>=";
            if( opAritm.indexOf(word) != -1) keyword = "operadores_aritmeticos";
            else if( opRelac.indexOf(word) != -1) keyword = "operadores_relacionais";
        }
        return "Editor/help/" + keyword + ".html";
    }
    
}
