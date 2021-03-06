package Portugol.Language.Analisador;

import Portugol.Language.Utilitario.LanguageException;

/**
 * Palavras Reservadas da Linguagem Portugol
 *
 * @author Augusto Bilabila
 */
public class Keyword {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";

    public static final int DESCONHECIDO = 0;
    public static final int INICIO = 1;
    public static final int FIM = 2;
    public static final int LEIA = 3;
    public static final int ESCREVA = 4;
    public static final int CALCULAR = 5;
    public static final int DEFINIR = 6;
    public static final int CONECTOR = 7;
    public static final int SE = 8;
    public static final int SENAO = 9;
    public static final int FIMSE = 10;
    public static final int PARA = 11;
    public static final int FIMPARA = 12;
    public static final int PASSO = 13;
    public static final int ENQUANTO = 14;
    public static final int FIMENQUANTO = 15;
    public static final int REPETE = 16;
    public static final int ATE = 17;
    public static final int FAZ = 18;
    public static final int FAZENQUANTO = 19;
    public static final int ESCOLHA = 20;
    public static final int CASO = 21;
    public static final int DEFEITO = 22;
    public static final int FIMESCOLHE = 23;
    public static final int CONSTANTE = 24;
    public static final int VARIAVEL = 25;
    public static final int ENTAO = 26;
    public static final int DE = 27;
    public static final int VAZIO = 28;
    public static final int LOGICO = 29;
    public static final int TEXTO = 30;
    public static final int INTEIRO = 31;
    public static final int REAL = 32;
    public static final int CARACTER = 33;
    public static final int REGISTO = 34;
    public static final int FIMREGISTO = 35;
    public static final int LIMPATELA = 36;
    public static final int PROCEDIMENTO = 37;
    public static final int FIMPROCEDIMENTO = 38;
    public static final int FUNCAO = 39;
    public static final int FIMFUNCAO = 40;
    public static final int RETORNE = 41;
    public static final int FICHEIRO = 42;
    public static final int CHAMADOPROCEDIMENTO = 43;
    public static final int CLASSE = 44;
    public static final int FIMCLASSE = 45;
    public static final int CONSTRUTOR = 46;
    public static final int FIMCONSTRUTOR = 47;
    public static final int NOVO = 48;
    public static final String ATRIBUI = "<-";
    public static final String PARENTESIS = "("; //David: Para intentar reconocer el llamado a metodos
    public static final String STR = "\""; //David:  Para determinar si  <- o ( no estan dentro de comillas
    static private final String txt[] = {
        "DESCONHECIDO",
        "INICIO",
        "FIMALGORITMO",
        "LEIA",
        "ESCREVA",
        "CALCULAR",
        "DEFINIR",
        "CONECTOR",
        "SE",
        "SENAO",
        "FIMSE",
        "PARA",
        "FIMPARA",
        "PASSO",
        "ENQUANTO",
        "FIMENQUANTO",
        "REPITA",
        "ATE",
        "FACA",
        "ENQUANTO", //faz . . . .enquanto
        "ESCOLHA",
        "CASO",
        "DEFEITO",
        "FIMESCOLHA",
        "CONSTANTE",
        "VARIAVEL",
        "ENTAO",
        "DE",
        //tipos de variaveis
        "VAZIO",
        "LOGICO",
        "LITERAL",
        "INTEIRO",
        "REAL",
        "CARACTER",
        "REGISTO",
        //Outros
        "FIMREGISTO",
        "LIMPATELA",
        "PROCEDIMENTO",
        "FIMPROCEDIMENTO",
        "FUNCAO",
        "FIMFUNCAO",
        "RETORNE",
        "FICHEIRO",
        "CHAMADOPROCEDIMENTO", //David: Acrecentado
        "CLASSE", //David: Acrecentado
        "FIMCLASSE", //David: Acrecentado
        "CONSTRUTOR",
        "FIMCONSTRUTOR",
        "NOVO",
        //Alternativas
        "MOSTRE",
        "RECEBA",
        "STRING",
        "TEXTO"
    //David: Estas 4 alternativas nao ten numeros correspondientes, podem confundir o programador
    };

    /**
     * retorna o texto de uma palavra chave
     *
     * @param key palavra chave
     * @return texto
     */
    public static String GetTextKey(int key) {
        return txt[key];
    }

    /**
     * calcula o tipo de key atraves da string
     *
     * @param instr instruçao
     * @return chave da instruçao
     */
    public static final int GetKey(String instr) {
        //String instrucao = instr.toUpperCase();
        String instrucao = Normalize(instr);
        if (instrucao.startsWith("LEIA ")) {
            return LEIA;
        }
        if (instrucao.startsWith("RECEBA ")) {
            return LEIA;
        }

        if (instrucao.startsWith("MOSTRE")) {
            return ESCREVA;
        }
        if (instrucao.startsWith("ESCREVA ")) {
            return ESCREVA;
        }

        if (instrucao.equals("FACA")) {
            return FAZ;
        }
        // se for um enquanto sem o faz
        if (instrucao.startsWith("ENQUANTO")
                && !instrucao.endsWith("FACA")) {
            return FAZENQUANTO;
        }

        if (instrucao.equals("SENAO")) {
            return SENAO;
        }
        if (instrucao.startsWith("SE ")) {
            return SE;
        }
        if (instrucao.equals("FIMSE")) {
            return FIMSE;
        }
        if (instrucao.equals("FIM SE")) {
            return FIMSE;
        }

        if (instrucao.startsWith("PARA ")) {
            return PARA;
        }
        if (instrucao.equals("FIMPARA")) {
            return FIMPARA;
        }
        if (instrucao.equals("FIM PARA")) {
            return FIMPARA;
        }

        if (instrucao.startsWith("ENQUANTO ")) {
            return ENQUANTO;
        }
        if (instrucao.equals("FIMENQUANTO")) {
            return FIMENQUANTO;
        }
        if (instrucao.equals("FIM ENQUANTO")) {
            return FIMENQUANTO;
        }

        if (instrucao.equals("REPITA")) {
            return REPETE;
        }
        if (instrucao.startsWith("ATE ")) {
            return ATE;
        }

        if (instrucao.startsWith("INTEIRO ")) {
            return DEFINIR;
        }
        if (instrucao.startsWith("REAL ")) {
            return DEFINIR;
        }
        if (instrucao.startsWith("CARACTER ")) {
            return DEFINIR;
        }
        if (instrucao.startsWith("LOGICO ")) {
            return DEFINIR;
        }
        if (instrucao.startsWith("LITERAL ")) {
            return DEFINIR;
        }
        if (instrucao.startsWith("TEXTO ")) {
            return DEFINIR;
        }
        if (instrucao.startsWith("STRING ")) {
            return DEFINIR;
        }
        if (DefineRegisto(instrucao)) {
            return DEFINIR; //David: Funcao que determina si é uma declaracao de variavel de registo
        }        
        if (DefineClasse(instrucao)) {
            return DEFINIR; //David: Funcao que determina si é uma declaracao de variavel de Objeto
        }        
        if (instrucao.startsWith("CONSTANTE ")) {
            return DEFINIR;
        }
        if (instrucao.startsWith("VARIAVEL ")) {
            return DEFINIR;
        }

        if (instrucao.startsWith("ESCOLHA ")) {
            return ESCOLHA;
        }
        if (instrucao.startsWith("CASO ")) {
            return CASO;
        }
        if (instrucao.startsWith("DEFEITO")) {
            return DEFEITO;
        }
        if (instrucao.equals("FIMESCOLHA")) {
            return FIMESCOLHE;
        }
        if (instrucao.equals("FIM ESCOLHA")) {
            return FIMESCOLHE;
        }

        if (instrucao.equals("INICIO")) {
            return INICIO;
        }
        if (instrucao.equals("FIMALGORITMO")) {
            return FIM;
        }
        if (instrucao.equals("FIM ALGORITMO")) {
            return FIM;
        }

        if (instrucao.startsWith("REGISTO ")) {
            return REGISTO;
        }
        if (instrucao.startsWith("CLASSE ")) {
            return CLASSE;
        }
        if (instrucao.startsWith("FIMCLASSE")) {
            return FIMCLASSE;
        }
        if (instrucao.startsWith("FIM CLASSE")) {
            return FIMCLASSE;
        }
        if (instrucao.startsWith("FIMREGISTO")) {
            return FIMREGISTO;
        }
        if (instrucao.startsWith("FIM REGISTO")) {
            return FIMREGISTO;
        }

        if (instrucao.startsWith("PROCEDIMENTO ")) {
            return PROCEDIMENTO;
        }
        if (instrucao.startsWith("FIMPROCEDIMENTO")) {
            return FIMPROCEDIMENTO;
        }
        if (instrucao.startsWith("FIM PROCEDIMENTO")) {
            return FIMPROCEDIMENTO;
        }
        if (instrucao.startsWith("CONSTRUTOR ")) {
            return CONSTRUTOR;
        }
        if (instrucao.startsWith("FIMCONSTRUTOR")) {
            return FIMCONSTRUTOR;
        }
        if (instrucao.startsWith("FIM CONSTRUTOR")) {
            return FIMCONSTRUTOR;
        }
        if (instrucao.equals("NOVO")) {
            return NOVO;
        }
        if (instrucao.startsWith("FUNCAO ")) {
            return FUNCAO;
        }
        if (instrucao.startsWith("FIMFUNCAO")) {
            return FIMFUNCAO;
        }
        if (instrucao.startsWith("FIM FUNCAO")) {
            return FIMFUNCAO;
        }
        if (instrucao.startsWith("RETORNE")) {
            return RETORNE;
        }

        if (instrucao.startsWith("FICHEIRO ")) {
            return FICHEIRO;
        }

        if (instrucao.startsWith("LIMPATELA")) {
            return LIMPATELA;
        }
        
        if (instrucao.indexOf(ATRIBUI) > -1
                && (instrucao.indexOf(STR) == -1
                || instrucao.indexOf(STR) > instrucao.indexOf(ATRIBUI))) {
            return CALCULAR;
        }

        if (instrucao.indexOf(PARENTESIS) > -1
                && (instrucao.indexOf(STR) == -1
                || instrucao.indexOf(STR) > instrucao.indexOf(PARENTESIS))) {
            return CHAMADOPROCEDIMENTO;
        }

        return DESCONHECIDO;
    }

    public static boolean IsKeyword(String key) {
        String tmp = Normalize(key);
        for (int i = 0; i < txt.length; i++) {
            if (tmp.equalsIgnoreCase(txt[i])) {
                return true;
            }
        }
        return false;
    }
    static private String from = "ãõáéíóúàèìòùâêîôûÁÉÍÓÚÀÈÌÒÙÃÕÂÊÎÔÛçÇ";
    static private String to = "AOAEIOUAEIOUAEIOUAEIOUAEIOUAOAEIOUCC";

    public static String Normalize(String str) {
        StringBuffer tmp = new StringBuffer();
        int index;
        for (int i = 0; i < str.length(); i++) {
            index = from.indexOf(str.charAt(i));
            if (index == -1) {
                tmp.append(str.charAt(i));
            } else {
                tmp.append(to.charAt(index));
            }
        }
        return tmp.toString().trim().toUpperCase();
    }

    //David: Funcao que determina si é uma declaracao de variavel de registo
    public static boolean DefineRegisto(Object elem) {
        if (!(elem instanceof String))
            return false;
        
        String instrucao = (String) elem;
        String varName;
        int pos = instrucao.indexOf(" ");
        if (pos == -1) {
            varName = instrucao;
        } else {
            varName = instrucao.substring(0, pos).trim();
        }

        try {
            return (SymbolComposto.ObterTipoRegisto(varName) != null);
        } catch (LanguageException e) {
            return false;
        }
    }
    
    //David: Funcao que determina si é uma declaracao de variavel de classe
    public static boolean DefineClasse(Object elem) {
        if (!(elem instanceof String))
            return false;
        
        String instrucao = (String) elem;
        String varName;
        int pos = instrucao.indexOf(" ");
        if (pos == -1) {
            varName = instrucao;
        } else {
            varName = instrucao.substring(0, pos).trim();
        }

        try {
            return (SymbolObjeto.ObterTipoClasse(varName) != null);
        } catch (LanguageException e) {
            return false;
        }
    }
    
}