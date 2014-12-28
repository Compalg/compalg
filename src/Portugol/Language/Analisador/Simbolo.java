package Portugol.Language.Analisador;

import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import javax.swing.JOptionPane;

public class Simbolo {

    /**
     * tipo Vazio
     */
    public final static int VAZIO = 0;
    /**
     * tipo Logico
     */
    public final static int LOGICO = 1;
    /**
     * tipo Real
     */
    public final static int REAL = 2;
    /**
     * tipo Inteiro
     */
    public final static int INTEIRO = 3;
    /**
     * tipo caracter (simbolo da tabela ASCII )
     */
    public final static int CARACTER = 4;
    /**
     * tipo Texto (strings)
     */
    public final static int TEXTO = 5;
    /**
     * tipo Registo (registers)
     */
    public final static int REGISTO = 6;
    /**
     * tipo Desconhecido
     */
    public final static int DESCONHECIDO = -1;
    /**
     * se is constante ou variavel
     */
    protected boolean isConst;
    /**
     * tipo de dados
     */
    protected int type;
    public String typeLexema;//David: Paa almacenar o nome real do tipo do dado
    public String TextoOrigen; //David: vai ter a linha origen da declaração 
    /**
     * nome do simbolo
     */
    protected String name;
    /**
     * valor
     */
    protected Object value;
    /**
     * nivel do simbolo
     */
    protected int level;

    public Simbolo(String modify, String type, String name, Object value, int level, String origTxt)
            throws LanguageException {
        TextoOrigen = origTxt;
        if (modify.equalsIgnoreCase(Keyword.GetTextKey(Keyword.CONSTANTE))) {
            this.isConst = true;
        } else {
            this.isConst = false;
        }

        this.type = getType(type);
        typeLexema = type;//parametro

        this.name = name.trim();

        if (value == null) {
            setValue(this.getDefaultValue(this.type));
        } else {
            //setValue(getNormalizedValue((String) value).getValue());
            setValue(value);
        }

        this.level = level;
    }

    public Simbolo(Simbolo origen) throws LanguageException {
        TextoOrigen = origen.TextoOrigen;
        this.isConst = origen.isConst;
        this.type = origen.type;
        typeLexema = origen.typeLexema;
        this.name = origen.name.trim();
        setValue(origen.getValue());
        this.level = origen.level;
    }

    public Simbolo() {
        // somente para as classes derivadas (ex. array)
        //Muy mala practica, pero se queda asi por ahora
    }

    public void copyFrom(SimboloDeParametro origen) throws LanguageException {
        if (origen == null) {
            return;
        }

        if (origen.PorValor) {
            setValue(origen.Value);
        } else if (origen.Value instanceof Simbolo) {
            setValue(((Simbolo) origen.Value).value);
        } else {
            throw new LanguageException("Tipo de parâmetro não é equivalente ao esperado", "Mude o tipo de parâmetro na chamada");//David:Revisar
        }
    }

    public boolean IsCompatible(int type) {
        if (this.type == type) {
            return true;
        }
        if (this.type == Simbolo.REAL && type == Simbolo.INTEIRO) {
            return true;
        }
        if (this.type == Simbolo.TEXTO && type == Simbolo.CARACTER) {
            return true;
        }
        return false;
    }

    /**
     * tipo da variavel
     *
     * @param t texto com o tipo
     * @return tipo
     */
    public static int getType(String t) {
        t = t.toUpperCase().trim();
        if (t.equalsIgnoreCase(Keyword.GetTextKey(Keyword.LOGICO))) {
            return LOGICO;
        }
        if (t.equalsIgnoreCase(Keyword.GetTextKey(Keyword.TEXTO))) {
            return TEXTO;
        }
        if (t.equalsIgnoreCase(Keyword.GetTextKey(Keyword.INTEIRO))) {
            return INTEIRO;
        }
        if (t.equalsIgnoreCase(Keyword.GetTextKey(Keyword.REAL))) {
            return REAL;
        }
        if (t.equalsIgnoreCase(Keyword.GetTextKey(Keyword.CARACTER))) {
            return CARACTER;
        }
        if (Keyword.DefineRegisto(t)) {
            return REGISTO;
        }

        return DESCONHECIDO;
    }

    public void setType(int tipo) {
        this.type = tipo;
    }

    public void setLevel(int l) {
        level = l;
    }

    /**
     * devolve os valores por defeito de cada tipo
     *
     * @param varType tipo
     * @return valor
     */
    public static Object getDefaultValue(int varType) {
        if (varType == LOGICO) {
            return new String(Values.FALSO);
        }
        if (varType == TEXTO) {
            return new String("\"\"");
        }
        if (varType == INTEIRO) {
            return new Integer(0);
        }
        if (varType == REAL) {
            return new Double(0.0);
        }
        if (varType == CARACTER) {
            return new String("\"_\"");
        }
        if (varType == REGISTO) {
            return new String("REGISTO");
        }

        return new String("ERRO");
    }

    /**
     * devolve os valores por defeito de cada tipo e 1 para os numericos
     *
     * @param varType tipo
     * @return valor
     */
    public static String getSafeDefaultValue(int varType) {
        if (varType == LOGICO) {
            return Values.FALSO;
        }
        if (varType == TEXTO) {
            return new String("\"\"");
        }
        if (varType == INTEIRO) {
            return "1";
        }
        if (varType == REAL) {
            return "1.0";
        }
        if (varType == CARACTER) {
            return new String("\" \" ");
        }
        return "ERRO";
    }

    /**
     * devolve os valores por defeito de cada tipo
     *
     * @param varType tipo
     * @return valor
     */
    public static Object getDefaultValue(String varType) {
        return getDefaultValue(getType(varType));
    }

    //------------------------------------------------------------------------
    public boolean isNumber() {
        if (type == REAL) {
            return true;
        }
        if (type == INTEIRO) {
            return true;
        }
        return false;
    }

    //-----------------------------------------------------------------------
    public boolean isString() {
        if (type == TEXTO) {
            return true;
        }
        if (type == CARACTER) {
            return true;
        }
        return false;
    }

    //------------------------------------------------------------------------
//    protected void setCanonicalValue(String val) throws LanguageException{
//        if(this.isString())
//            //value = getNormalizedValue("\"" + val + "\"");
//            setValue((String)getNormalizedValue("\"" + val + "\""));//David: usar el metodo
//        else
//            //value = getNormalizedValue(val);
//            setValue((String)getNormalizedValue(val));//David: usar el metodo
//    }
    //David: comentado, no se usa
//   //-----------------------------------------------------------------------
//    public void setHardValue(String val){
//        value = val;
//    }
    //------------------------------------------------------------------------
    public void setValue(Object val) throws LanguageException {
        if (this.isConst) {
            throw new LanguageException("ESTE SÍMBOLO " + this.name + " É UMA CONSTANTE, NÃO PODE RECEBER VALORES",
                    " ALTERE O SIMBOLO PARA VARIAVEL");
        }
        value = val; //David: aqui debe ficar mesmo asi
    }

    public void setValue(String val) throws LanguageException {
        if (this.isConst) {
            throw new LanguageException("ESTE SÍMBOLO " + this.name + " É UMA CONSTANTE, NÃO PODE RECEBER VALORES",
                    " ALTERE O SIMBOLO PARA VARIAVEL");
        }
        value = getNormalizedValue(val).getValue(); //David: aqui debe ficar mesmo asi
    }

    //David:
    public void PilaValuePush() {
    }
    //

    protected Simbolo getNormalizedValue(String val) throws LanguageException {
        val = val.trim();

        if (type == TEXTO) {
            if (val.length() < 2 || !Values.IsString(val)) {
                throw new LanguageException(
                        0, "", name + " É UMA VARIÁVEL DO TIPO STRING", "\"" + val + "\" NÃO É UM TEXTO");
            }
            return new Simbolo("", typeLexema, "", new String(val), 0, (typeLexema + " nao_nome"));
        } else if (type == CARACTER) {

            if (!Values.IsString(val) || val.length() != 3) {
                throw new LanguageException(
                        0, "", name + " É UMA VARIÁVEL DO TIPO CARACTER", "\"" + val + "\" NÃO É UM CARACTER");
            }
            return new Simbolo("", typeLexema, "", new String(val), 0, (typeLexema + " nao_nome"));
        } else if (type == LOGICO) {
            if (!Values.IsBoolean(val)) {
                throw new LanguageException(
                        0, "", name + " É UMA VARIÁVEL DO TIPO LOGICO", "\"" + val + "\"  NÃO É UM VALOR LÓGICO VÁLIDO");
            }
            return new Simbolo("", typeLexema, "", new String(val.toUpperCase()), 0, (typeLexema + " nao_nome"));
        } else if (type == REAL) {
            if (!Values.IsNumber(val)) {
                throw new LanguageException(
                        0, "", name + " É UMA VARIÁVEL DO TIPO REAL", "\"" + val + "\"  NÃO É UM NÚMERO REAL VÁLIDO");
            }

            double d = Values.StringToDouble(val);
            return new Simbolo("", typeLexema, "", new Double(d), 0, (typeLexema + " nao_nome"));
        } else if (type == INTEIRO) {
            if (!Values.IsValue(val)) {
                throw new LanguageException(
                        0, "", name + " É UMA VARIÁVEL DO TIPO INTEIRO", "\"" + val + "\" NÃO É UM NÚMERO INTEIRO VÁLIDO");
            }
            int i = Values.StringToInteger(val);
            return new Simbolo("", typeLexema, "", new Integer(i), 0, (typeLexema + " nao_nome"));
        } else if (type == REGISTO) {
            if (!Values.IsRegisto(val)) {
                throw new LanguageException(
                        0, "", name + " É UMA VARIÁVEL DO TIPO REGISTO", "\"" + val + "\" NÃO É UM REGISTO VÁLIDO");
            }

            return new SymbolComposto("", typeLexema, "", typeLexema, 0, (typeLexema + " nao_nome"));
        }
        return null;
    }

    /**
     * retorna o valor
     *
     * @return valor
     */
    public Object getValue() {
        return value;
    }

    //David:
    public void PilaValuePop() {
    }

    /**
     * devolve os valores por defeito de cada tipo
     *
     * @return valor por defeito
     */
    public Object getDefaultValue() {
        return getDefaultValue(this.type);
    }

    /**
     * devolve os valores por defeito de cada tipo e os numericos a 1 por causa
     * das divisoes
     *
     * @return valor por defeito
     */
    public String getSafeDefaultValue() {
        return getSafeDefaultValue(this.type);
    }

    public static String getStringType(int t) {
        if (t == LOGICO) {
            return "LOGICO";
        }
        if (t == TEXTO) {
            return "TEXTO";
        }
        if (t == CARACTER) {
            return "CARACTER";
        }
        if (t == REAL) {
            return "REAL";
        }
        if (t == INTEIRO) {
            return "INTEIRO";
        }
        return "DESCONHECIDO";
    }

    public String getStringType() {
        if (type == LOGICO) {
            return "LOGICO";
        }
        if (type == TEXTO) {
            return "TEXTO";
        }
        if (type == CARACTER) {
            return "CARACTER";
        }
        if (type == REAL) {
            return "REAL";
        }
        if (type == INTEIRO) {
            return "INTEIRO";
        }
        return "DESCONHECIDO";
    }

    /**
     * tipo da variavel
     *
     * @return tipo
     */
    public int getType() {
        return type;
    }

    /**
     * gets nome
     *
     * @return nome
     */
    public String getName() {
        return name;
    }

    /**
     * nivel
     *
     * @return nivel da varivel
     */
    public int getLevel() {
        return level;
    }

    /**
     * verifica se is constante
     *
     * @return a constante
     */
    public boolean isConstant() {
        return this.isConst;
    }

    /**
     * return object String
     *
     * @return informacao da variavel
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        if (isConstant()) {
            str.append("\tCONSTANTE\n");
        } else {
            str.append("\tVARIAVEL\n");
        }
        str.append("nome\t:" + getName() + "\n");
        str.append("valor\t:" + getValue() + "\n");
        str.append("tipo\t:" + getStringType() + "\n");
        str.append("nivel\t:" + getLevel() + "\n");
        return str.toString();
    }

    /**
     * nome =
     *
     * @param var varivel a comparar
     * @return nome = paramentro
     */
    public boolean nameEqual(String var) {
        return var.equalsIgnoreCase(name);
    }

    /**
     * verifica se o valor e o tipo sao compativeis
     *
     * @param type1 type de dados
     * @param value valor
     * @return compatibilidade
     */
    public static boolean IsCompatible(int type1, Object value) {

        if (type1 == Simbolo.REAL && Values.IsNumber(value)) {
            return true;
        }
        if (type1 == Simbolo.INTEIRO && Values.IsInteger(value)) {
            return true;
        }
        if (type1 == Simbolo.LOGICO && Values.IsBoolean(value)) {
            return true;
        }
        if (type1 == Simbolo.CARACTER && Values.IsCharacter(value)) {
            return true;
        }
        if (type1 == Simbolo.TEXTO && Values.IsString(value)) {
            return true;
        }

        if (type1 == Simbolo.getType((String) value)) {
            return true;//David: requiere troque, es solo invento
        }
        return false;
    }

    /**
     * igual
     *
     * @param other segundo argumento
     * @return sao iguais?
     */
    public boolean equal(Simbolo other) {
        if (!nameEqual(other.name)) {
            return false;
        }
        return other.level == level;
    }

    public boolean typeEqual(Simbolo otro) {
        if (otro instanceof Simbolo) {
            return type == otro.type && typeLexema.equals(otro.typeLexema);
        } else {
            return false;
        }
    }
}
