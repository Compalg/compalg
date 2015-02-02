package Portugol.Language.Analisador;

import static Portugol.Language.Analisador.Simbolo.REGISTO;
import Portugol.Language.Calcular.Calculador;
import Portugol.Language.Utilitario.IteratorArray;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;
import javax.swing.Icon;

public class SymbolArray extends Simbolo {

    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";
    static public Icon SymbolArrayIcon;
    /**
     * Creates a new instance of Array
     */
    public Vector<Simbolo> dataValues = new Vector<Simbolo>();
    public String IndiceOrigen;//David: 
    Vector<Integer> indexHeights = new Vector<Integer>();
    Vector<Integer> indexLimits = new Vector<Integer>();
    // index corrent
    int currentIndex = -1;
    private int numElements = 1;
    static public String ErroForaLimites = "FORA DOS LIMITES";
    // definition tem os indexes e os valores

    public SymbolArray(String modify, String type, String name, Vector<Integer> indLims, Vector<Simbolo> values, int level, Vector memory, String origTxt)
            throws LanguageException {
        TextoOrigen = origTxt;
        if (modify.equalsIgnoreCase(Keyword.GetTextKey(Keyword.CONSTANTE))) {
            this.isConst = true;
        } else {
            this.isConst = false;
        }

        this.type = getType(type);
        typeLexema = type;
        this.name = name.trim();
        this.value = Values.getDefault(typeLexema);
        this.level = level;
        IndiceOrigen = ""; //David: Se inicializa mas tarde, en el metodo de abaixo
        MakeArray(indLims);
        PutValues(values);
    }

    public SymbolArray(SymbolArray origen) throws LanguageException {
        TextoOrigen = origen.TextoOrigen;
        isConst = origen.isConst;
        type = origen.type;
        typeLexema = origen.typeLexema;
        IndiceOrigen = origen.IndiceOrigen;
        name = origen.name.trim();
        value = origen.value;
        this.level = level;
        MakeArray(origen.indexLimits);
        PutValues(origen.dataValues);
    }

    public void copyFrom(SymbolArray arreglo) throws LanguageException {
        if (!this.TextoOrigen.equals(arreglo.TextoOrigen)) {
            throw new LanguageException("Os vectores não são equivalentes", "Mude a declaração, no parâmetro de chamada");//David:Revisar
        }
        dataValues.clear();
        for (int i = 0; i < arreglo.dataValues.size(); i++) {
            if (arreglo.dataValues.get(i) instanceof SymbolArray) {
                dataValues.add(new SymbolArray((SymbolArray) arreglo.dataValues.get(i)));
            } else if (arreglo.dataValues.get(i) instanceof SymbolComposto) {
                dataValues.add(new SymbolComposto((SymbolComposto) arreglo.dataValues.get(i)));
            } else if (arreglo.dataValues.get(i) instanceof SymbolObjeto) {
                dataValues.add(new SymbolObjeto((SymbolObjeto) arreglo.dataValues.get(i)));
            } else //if (arreglo.dataValues.get(i) instanceof Simbolo) 
            {
                dataValues.add(new Simbolo(arreglo.dataValues.get(i)));
            }
        }
    }

    public void copyFrom(SimboloDeParametro origen) throws LanguageException {
        if (origen == null) {
            return;
        }
        if (origen.Value instanceof SymbolArray) {
            SymbolArray arreglo = (SymbolArray) origen.Value;
            this.copyFrom(arreglo);
        } else {
            throw new LanguageException("Tipo de parâmetro não é equivalente ao esperado", "Mude o tipo de parâmetro na chamada");//David:Revisar
        }
    }

    //coloca o current index na posicao pretendida
    public void SetIndex(String varName, Vector memory) throws LanguageException {
        IteratorArray iter = new IteratorArray(varName);
        String nome = iter.getNext();
        while (iter.hasMoreElements()) {
            nome += "[";
            String index = iter.getNext();
            Object value = Expressao.Evaluate(index, memory);
            if (value instanceof String) {
                nome += Integer.parseInt((String) value) + "]";
            } else if (value instanceof Simbolo) {
                if (!((Simbolo) value).isNumber()) {
                    throw new LanguageException("O símbolo " + varName + " tem de ser um número",
                            " Tente alterar o símbolo para um tipo numérico");
                }

                nome += ((Integer) ((Simbolo) value).getValue()).toString() + "]";
            } else {
                nome += "0" + "]";
            }
        }
        try {
            this.currentIndex = this.getFlatIndex(nome);
        } catch (Exception e) {
            //this.currentIndex = -1;
            throw new LanguageException(
                    e.getMessage(),
                    " Tente alterar o símbolo para número");

        }

    }
    //-----------------------------------------------------------------------

    public void setValue(Object val) throws LanguageException {
        if (this.isConst) {
            throw new LanguageException("O símbolo " + this.name + " é uma constante, por isso, não pode receber valor",
                    " Altere o símbolo para variável normal");
        }
        Simbolo v = getNormalizedValue((String) val);
        v.setName(new Integer(currentIndex).toString());
        dataValues.set(currentIndex, v);
    }

    /**
     * retorna o valor
     *
     * @return valor
     */
    @Override
    public Simbolo getValue() {
        return dataValues.get(currentIndex);
    }

    private void MakeArray(Vector<Integer> indLims) {
        numElements = 1;
        for (int i = 0; i < indLims.size(); i++) {
            int number = indLims.get(i);
            indexLimits.add(number);
            indexHeights.add(0);
            numElements *= number;
            IndiceOrigen = IndiceOrigen + "[" + Integer.toString(number) + "]";
        }
        // peso de cada indice no array
        // necessario para os tornar linear [2][3] => 12
        int height = 1;
        for (int i = indexLimits.size() - 1; i >= 0; i--) {
            indexHeights.set(i, height);
            height *= Integer.parseInt(indexLimits.get(i).toString());
        }
    }
    //////////////////////////////////////////////////////////////////////////////

    private void PutValues(Vector<Simbolo> values) throws LanguageException {
        int index = 0;
        for (int i = 0; i < values.size(); i++) {
            Object value = values.get(i);
            if ((type == Simbolo.REGISTO || type == Simbolo.CLASSE) && value instanceof String && typeLexema.equals((String) value)) {
                break;//David: 
            }
            if (value instanceof SymbolComposto) {
                dataValues.add((SymbolComposto) value);
            } else if (value instanceof SymbolObjeto) {
                dataValues.add((SymbolObjeto) value);
            } else if (value instanceof Simbolo) {
                dataValues.add((Simbolo) value);
            } else if (value instanceof SymbolArray) {
                //David: Isto nao pode acontecer
            } else {
                dataValues.add(getNormalizedValue((String) value));
            }
            dataValues.get(index).setName(new Integer(index).toString());
            index++;
        }
        Object defValue = Values.getDefault(typeLexema);
        for (int i = index; i < numElements; i++) {
            if (type == REGISTO) {
                dataValues.add(new SymbolComposto("", typeLexema + " ", " nao_nome", typeLexema + "", level, (typeLexema + " nao_nome <- " + typeLexema)));
            } else if (type == CLASSE) {
                dataValues.add(new SymbolObjeto("", typeLexema + " ", " nao_nome", typeLexema + "", level, (typeLexema + " nao_nome <- " + typeLexema)));
            } else {
                dataValues.add(new Simbolo("", typeLexema + " ", " nao_nome", defValue, level, (typeLexema + " nao_nome")));
            }
            dataValues.get(i).setName(new Integer(i).toString());
        }
    }
//////////////////////////////////////////////////////////////////////////////

    /**
     * retorna o valor
     *
     * @return valor
     */
    public String getValue(String var) {
        try {
            int index = getFlatIndex(var);
            return dataValues.get(index).toString();
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    /**
     * retorna o valor
     *
     * @return valor
     */
    public String getFlatValue(int index) {
        if (index >= 0 && index < this.numElements) {
            return dataValues.get(index).toString();
        } else {
            return "ERRO";
        }
    }
    //////////////////////////////////////////////////////////////////////////////

    /**
     * retorna o valor
     */
    public void setValue(String var, String value) throws Exception {
        // introduzir o value em this
        // causa excepcoes se houver erro em value e normaliza o valor
        Simbolo v = getNormalizedValue(value);//David: Aqui pode ficar asi direito
        int index = getFlatIndex(var);
        v.setName(new Integer(index).toString());
        dataValues.set(index, v); //David: Aqui pode ficar asi direito
    }
    //////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////

    private int getFlatIndex(String var) throws Exception {
        int index = 0;
        IteratorArray iter = new IteratorArray(var.trim());
        String nameVar = iter.getNext().trim();
        if (!nameVar.equalsIgnoreCase(name)) {
            throw new Exception("ERRO NO NOME da VARIAVEL");
        }

        int numIndex = 0;
        while (iter.hasMoreElements()) {
            String exp = iter.getNext();

            Object result = Calculador.CalulateValue(Expressao.ExpresionStringToVector(exp));
            int number = Integer.parseInt((String) result);
            if (number < 0) {
                throw new Exception("ERRO NO INDECE " + exp);
            }
            if (number >= Integer.parseInt(indexLimits.get(numIndex).toString())) {
                throw new Exception(ErroForaLimites + ": " + exp);
            }
            if (numIndex > indexLimits.size()) {
                throw new Exception("ERRO NO INDECE :" + exp + " - NAO DEFENIDO");
            }
            index += number * Integer.parseInt(indexHeights.get(numIndex).toString());
            numIndex++;
        }
        return index;
    }

    /**
     * verifica se uma expressao is um array
     *
     * @param name expressao
     * @return array[] ?
     */
    public static boolean isArray(String name) {
        int c1 = name.indexOf('[');
        int c2 = name.indexOf(']');
        return c1 * c2 > 0 && c2 > c1;
    }

    public String toString() {
        return name + " = " + dataValues.toString();
    }

    public Vector getDimensions() {
        // fazer uma copia
        // para evitar ser alterado
        return indexLimits;
    }

    public int getNumElements() {
        return numElements;
    }

    /**
     * nome =
     *
     * @param var varivel a comparar
     * @return nome = paramentro
     */
    public boolean nameEqual(String var) {
        int p = var.indexOf('[');
        if (p > 0) {
            return name.equalsIgnoreCase(var.substring(0, p).trim());
        } else {
            return var.equalsIgnoreCase(name);
        }
    }

    public static void main(String args[]) {
        System.out.println("ARRAYS");
        Vector memory = new Vector();
        try {
//            memory.add(new Simbolo("variavel", "inteiro", "numero1", "10", 0));
//            memory.add(new Simbolo("variavel", "inteiro", "numero2", "20", 0));
//            memory.add(new Simbolo("variavel", "inteiro", "numero3", "30", 0));
//            SymbolArray a = new SymbolArray("variavel", "real", "vetor", "[2][2][2]", " { 1+3 ,2,3,4,5,6,7,8} ", 1, memory);

            //System.out.println("Nome\t:" + a.getName());
            //String var ="vetor[1][1][1]";
            //System.out.println(a.toString());
            //a.setValue(var,"20.0");
            String var = "vetor[numero1/10][0][numero3/numero1 - 2]";
//            a.SetIndex(var, memory);
            //a.setValue("10");
            //          System.out.println(" Var = " + a.getValue());
            //a.setValue(var,"10");
            //        System.out.println(a.toString());
            //System.out.println(var + " = " + a.getValue(var));
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }

    public boolean typeEqual(Simbolo otro) {
        if (otro instanceof SymbolArray) {
            return (typeLexema + " " + IndiceOrigen).equals((((SymbolArray) otro).typeLexema + " " + ((SymbolArray) otro).IndiceOrigen));
        } else {
            return false;
        }
    }

    public Icon getIcon() {
        return SymbolArrayIcon;
    }
}
