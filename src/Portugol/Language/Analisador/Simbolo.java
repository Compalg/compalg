package Portugol.Language.Analisador;

import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import javax.swing.JOptionPane;

public class Simbolo extends ParteDeExpresion {

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
     * tipo Classe 
     */
    public final static int CLASSE = 7;
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
    //public String TextoOrigen; //David: vai ter a linha origen da declaração 
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
            setValue(Values.getDefault(typeLexema));
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
        if (Keyword.DefineClasse(t)) {
            return CLASSE;
        }

        return DESCONHECIDO;
    }

    public void setType(int tipo) {
        this.type = tipo;
    }

    public void setLevel(int l) {
        level = l;
    }

    //------------------------------------------------------------------------
    public boolean isLogico() {
        if (type == LOGICO) {
            return true;
        }
        return false;
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
    public void setValue(Object val) throws LanguageException {
        if (this.isConst) {
            throw new LanguageException("ESTE SÍMBOLO " + this.name + " É UMA CONSTANTE, NÃO PODE RECEBER VALORES",
                    " ALTERE O SIMBOLO PARA VARIAVEL");
        }
        if (val instanceof String && type != TEXTO && type != CARACTER && type != LOGICO) {
            value = getNormalizedValue((String) val).getValue(); //David: aqui debe ficar mesmo asi
        } else if (val instanceof String) {
            value = val;
        } else if (val instanceof Simbolo) {
            if (((Simbolo) val).getValue() instanceof String) {
                value = (String) ((Simbolo) val).getValue();
            } else if (((Simbolo) val).getValue() instanceof Integer) {
                value = new Integer((Integer) ((Simbolo) val).getValue());
            } else if (((Simbolo) val).getValue() instanceof Double) {
                value = new Double((Double) ((Simbolo) val).getValue());
            } else if (((Simbolo) val).getValue() instanceof Boolean) {
                value = (Boolean) ((Simbolo) val).getValue();
            } else {
                value = ((Simbolo) val).getValue();
            }
        } else {
            value = val;
        }
    }

//    public void setValue(String val) throws LanguageException {
//        if (this.isConst) {
//            throw new LanguageException("ESTE SÍMBOLO " + this.name + " É UMA CONSTANTE, NÃO PODE RECEBER VALORES",
//                    " ALTERE O SIMBOLO PARA VARIAVEL");
//        }
//        value = getNormalizedValue(val).getValue(); //David: aqui debe ficar mesmo asi
//    }
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
        } else if (type == CLASSE) {
            if (!Values.IsRegisto(val)) {
                throw new LanguageException(
                        0, "", name + " É UMA VARIÁVEL DO TIPO CLASSE", "\"" + val + "\" NÃO É UMA CLASSE VÁLIDA");
            }

            return new SymbolObjeto("", typeLexema, "", typeLexema, 0, (typeLexema + " nao_nome"));
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

    public Object getDefaultValue() {//David: Não chamar desde construtor        
        return Values.getDefault(typeLexema);
    }

    public String getTypeLexema() {
        return typeLexema;
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
    public void setName(String s) {
        name = s;
    }

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
        str.append("tipo\t:" + getTypeLexema() + "\n");
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
    public static boolean IsCompatible(Object target, Object value) {
        if (target == null || value == null) {
            return false;
        }

        int typeNUM;
        if (target instanceof String) {
            typeNUM = Simbolo.getType((String) target);
            if (typeNUM == Simbolo.DESCONHECIDO) {
                return false;
            }
        } else if (target instanceof Simbolo) {
            if (value instanceof Simbolo
                    && (((Simbolo) target).type == Simbolo.REAL || ((Simbolo) target).type == Simbolo.INTEIRO)
                    && (((Simbolo) value).type == Simbolo.REAL || ((Simbolo) value).type == Simbolo.INTEIRO)) {
                return true;
            }
            if (value instanceof Simbolo
                    && ((Simbolo) target).type != Simbolo.REGISTO
                    && ((Simbolo) target).type != Simbolo.CLASSE
                    && ((Simbolo) target).type == ((Simbolo) value).type) {
                return true;
            }
            if (value instanceof Simbolo
                    && ((Simbolo) target).type == Simbolo.REGISTO
                    && ((Simbolo) target).type == Simbolo.CLASSE
                    && ((Simbolo) target).type == ((Simbolo) value).type
                    && ((Simbolo) target).typeLexema.toUpperCase().equals(((Simbolo) value).typeLexema.toUpperCase())) {
                return true;
            }

            typeNUM = ((Simbolo) target).type;
        } else {
            return false;
        }

        if (typeNUM == Simbolo.REAL && Values.IsNumber(value)) {
            return true;
        }
        if (typeNUM == Simbolo.INTEIRO && Values.IsInteger(value)) {
            return true;
        }
        if (typeNUM == Simbolo.LOGICO && Values.IsBoolean(value)) {
            return true;
        }
        if (typeNUM == Simbolo.CARACTER && Values.IsCharacter(value)) {
            return true;
        }
        if (typeNUM == Simbolo.TEXTO && Values.IsString(value)) {
            return true;
        }

        if (typeNUM == Simbolo.REGISTO || typeNUM == Simbolo.CLASSE
                && value instanceof Simbolo
                && ((Simbolo) value).type == typeNUM
                && target instanceof Simbolo
                && ((Simbolo) value).typeLexema.toUpperCase().equals(((Simbolo) target).typeLexema.toUpperCase())) {
            return true;
        }

        if (typeNUM == Simbolo.REGISTO || typeNUM == Simbolo.CLASSE
                && value instanceof Simbolo
                && ((Simbolo) value).type == typeNUM
                && target instanceof String
                && ((Simbolo) value).typeLexema.toUpperCase().equals(((String) target).toUpperCase())) {
            return true;
        }
        
        //Esta entrada deberia desaparecer. Es para la inicializacion de variables, para tener un valor por defecto para cada registo (su mismo nombre)
        if ( (typeNUM == Simbolo.REGISTO || typeNUM == Simbolo.CLASSE)
                && value instanceof String
                && target instanceof String
                && ((String) value).toUpperCase().equals(((String) target).toUpperCase())) {
            return true;
        }

        if ( (typeNUM == Simbolo.REGISTO || typeNUM == Simbolo.CLASSE)
                && value instanceof String
                && target instanceof Simbolo
                && ((String) value).toUpperCase().equals(((Simbolo) value).typeLexema.toUpperCase())) {
            return true;
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
