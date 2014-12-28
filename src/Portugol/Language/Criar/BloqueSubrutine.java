package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Consola.ConsoleIO;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SimboloDeParametro;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.TipoDeParametro;
import Portugol.Language.Analisador.Variavel;
import java.util.Vector;
import Portugol.Language.Criar.ExpandEnquanto;
import Portugol.Language.Criar.ExpandSe;
import Portugol.Language.Criar.NodeInstruction;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public class BloqueSubrutine extends Bloque {

    public static String VERSION = "Versão:1.0 \t(c) Augusto Bilabila";
    /**
     * vector das variaveis em memoria
     */
    public Vector memory;
    /**
     * vector dos parametros
     */
    public Vector<TipoDeParametro> parametros;//David:
    /**
     * vector dos valoes para os parametros
     */
    public Vector paramsValues;//David:

    //public static String VerOperator = " " ;//im
    /**
     * Constroi um fluxograma
     *
     * @param code programa fonte
     * @throws Portugol.Language.Utils.LanguageException excepcao
     */
    public BloqueSubrutine(/*String code*/) throws LanguageException {
        // Construir(code);
        memory = new Vector();
        start = null;
        parametros = new Vector<TipoDeParametro>();
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                              ---------------------------
//------------  E X E C U C A O   D A   I N S T R U C A O   ---------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * Executa uma linha de codigo - normalmente
     */
    public String ExecuteSubrutine(Vector<String> paramVals, ConsoleIO console) throws LanguageException {
        NodeInstruction temporal = getStart();
        boolean esperarRetorno = temporal.GetType() == Keyword.FUNCAO;
        paramsValues = paramVals;

        if (parametros.size() != paramsValues.size()) {
            throw new LanguageException(
                    "No se están pasando todos los parametros requeridos", //David: Traducir
                    "Verifique la cantidad de parametros en el llamado del sub-algoritmo");
        }

        while (temporal != null) //David: novo
        {
            temporal = ExecuteLine(temporal, console);
            //autoExecute.sleep(2);//David: 10 //David: Verificar si es necesario este sleep
        }

        if (!esperarRetorno) {
            return "";//David: Futuro uso de funciones
        } else {
            throw new LanguageException("Todavia no se implrementa la funcionalidad de retorno de funciones", "no usar por ahora");
        }
        //David: usar la memoria igual que en el index de los arreglos
    }

    public NodeInstruction ExecuteLine(NodeInstruction node, ConsoleIO console) throws LanguageException {
        int line = node.GetCharNum();
        while (node != null && line == node.GetCharNum())//David: acrecentado node != null
        {
            node = Execute(node, console);
        }
        return node;
    }

    public NodeInstruction Execute(NodeInstruction node, ConsoleIO console) throws LanguageException {
        switch (node.GetType()) {

            case Keyword.INICIO:
                cleanMemory(node.GetLevel(), memory);
                return node.GetNext();

            case Keyword.FIM:
                cleanMemory(node.GetLevel(), memory);
                return null;

            case Keyword.PROCEDIMENTO:
                cleanMemory(node.GetLevel(), memory);
                return node.GetNext();

            case Keyword.CHAMADOPROCEDIMENTO:
                if (this.Nome == node.subrutine.Nome) {
                    throw new LanguageException(
                            "Detectado llamado recursivo. CompAlg no permite este llamado",
                            "Verifique el nombre del sub-algoritmo al que llama" //David: Traducir.
                            );
                }
                Vector<String> paramVals = ObterParametrosValues(node.GetText());
                node.subrutine.ExecuteSubrutine(paramVals, console);
                return node.GetNext();

            case Keyword.FIMPROCEDIMENTO:
                cleanMemory(node.GetLevel(), memory);
                return null;//David: Por alguna razon el metodo no se ejecuta como INICIO, se mantiene repitiendo

            case Keyword.FUNCAO:
                throw new LanguageException("hay que hacer algo aqui", "metodo Execute, clase Subrutine");
            //cleanMemory( node.GetLevel(),memory);
            //return node.GetNext();

            case Keyword.FIMFUNCAO:
                throw new LanguageException("hay que hacer algo aqui", "metodo Execute, clase Subrutine");
            //cleanMemory( node.GetLevel(),memory);
            //return start;

            case Keyword.DEFINIR:
                Variavel.defineVAR(node, memory, this.parametros, paramsValues);
                return node.GetNext();

            case Keyword.CALCULAR:
                executeCalculate(node.GetText());
                return node.GetNext();

            case Keyword.LEIA:
                executeREAD(node.GetText(), console);
                return node.GetNext();

            case Keyword.ESCREVA:
                executeWRITE(node.GetText(), console);
                return node.GetNext();

            case Keyword.LIMPATELA:
                console.Clear();
            //return  node.GetNext();

            case Keyword.CONECTOR:
            case Keyword.FAZ:
            case Keyword.REPETE:
            case Keyword.ESCOLHA:
                return node.GetNext();

            // verifica o passo do for e modifica a condiçao se necessario
            case Keyword.PASSO:
                Object value = Expressao.Evaluate(node.GetText(), memory);
                //avalia o passo
                double valor = Double.parseDouble((String) value);
                //passo nulo
                if (valor == 0.0) {
                    throw new LanguageException("ERRO - o PASSO do ciclo PARA é zero", "Corrija o PASSO");
                }
                //passa para o no da condiçao
                NodeInstruction forNode = node.GetNext();
                String instruction = forNode.GetText();
                // se o passo for negativo a condiçao
                if (valor > 0) {
                    forNode.SetText(instruction.replaceFirst(">=", "<="));
                } else {
                    forNode.SetText(instruction.replaceFirst("<=", ">="));
                }
                return node.GetNext();

            case Keyword.ATE:
            case Keyword.FAZENQUANTO:
            case Keyword.ENQUANTO:
            case Keyword.SE:
            case Keyword.PARA:
                Object compare = Expressao.Evaluate(node.GetText(), memory);
                //variaveis defenidas dentro do bloco
                cleanMemory(node.GetLevel() + 1, memory);
                if (((String) compare).equalsIgnoreCase(Values.VERDADEIRO)) {
                    return node.GetIfTrue();
                } else {
                    return node.GetIfFalse();
                }
        }
        throw new LanguageException("ERRRO - NODO DESCONHECIDO ", node.toString());

    }

//------------------------------------------------------------------------------
//------------                                             ---------------------
//------------         C A L C U L A R                     ---------------------
//------------                                              --------------------
//------------------------------------------------------------------------------      
    protected void executeCalculate(String str) throws LanguageException {
        int pos = str.indexOf(Keyword.ATRIBUI);
        String var = str.substring(0, pos).trim();
        String values = str.substring(pos + Keyword.ATRIBUI.length()).trim();

        Object newValue = Expressao.Evaluate(values, memory);
        Variavel.replaceVariableValue(var, newValue, memory);
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * limpa a memoria, os niveis de memoria. <br>
     * Utiliza-se no ciclo para limpar as variaveis <br>
     * locais ao ciclo.
     *
     * @param level nivel a partir do qual vai limpar
     */
    protected static void cleanMemory(int level, Vector memory) {//sbr
        for (int index = memory.size() - 1; index >= 0; index--) {
            Simbolo v = (Simbolo) memory.get(index);
            //elimina as variaveis superiores ou iguais ao nivel
            if (v.getLevel() >= level) {
                memory.remove(index);
            } else {
                break;
            }
        }
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                             ----------------------------
//------------         E S C R E V E R                     ----------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * executa a instrucao de escrever
     *
     * @param str string com a instrucao
     * @param console consola para escrever
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    protected void executeWRITE(String str, ConsoleIO console) throws LanguageException {
        str = str.substring(Keyword.GetTextKey(Keyword.ESCREVA).length());
        IteratorCodeParams tok = new IteratorCodeParams(str.trim());
        //WriteTokenizer tok = new  WriteTokenizer(str);
        StringBuffer line = new StringBuffer();
        Object elemLine;
        //parametros
        while (tok.hasMoreElements()) {
            String elem = tok.current();
            tok.next();

            if (!Values.IsString(elem)) {
                elemLine = Expressao.Evaluate(elem, memory);
            } else {
                elemLine = elem;
            }
            line.append(Values.getStringValue(elemLine.toString()));
        }
        console.write(line.toString());
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                             ----------------------------
//------------         L E R                               ----------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    /**
     * Executa a instrucao de leitura
     *
     * @param str linha de codigo
     * @param console consola de leitura
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    protected void executeREAD(String str, ConsoleIO console) throws LanguageException {
        //ler = 3 caracteres
        String varName = str.substring(3).trim();

        Simbolo var = Variavel.getVariable(varName, memory);
        // fazer o set ao index do array
        if (var instanceof SymbolArray) {
            ((SymbolArray) var).SetIndex(varName, memory);
        }

        String value = console.read(var);
        var.setValue(value);
    }

//=============================================================================
    public String RemoveComentarios(String str) {
        StringBuffer newStr = new StringBuffer();
        for (int index = 0; index < str.length(); index++) {
            switch (str.charAt(index)) {
                case '/':
                    // comentario "//"
                    if (index + 1 < str.length() && str.charAt(index + 1) == '/') {
                        return newStr.toString().trim();
                    }
                    //inicio de um comentario /*
                    if (index + 1 < str.length() && str.charAt(index + 1) == '*') {
                        isComented = true;
                    } else //fim do comentario */
                    if (index > 0 && str.charAt(index - 1) == '*') {
                        isComented = false;
                    } //introduz caracter /
                    else {
                        newStr.append(str.charAt(index));
                    }
                    break;
                default:
                    // se nao for comentario
                    if (!isComented) {
                        newStr.append(str.charAt(index));
                    }
            }
        }
        return newStr.toString().trim();
    }

    /**
     * string
     *
     * @return string
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        NodeInstruction pt = start;
        while (pt != null) {
            str.append(GetCode(pt));
            pt = pt.next;
        }
        return str.toString();
    }

    /**
     * calcula o texto de um nodo
     *
     * @param node nodo de origem
     * @return o texto de um nodo
     */
    public static String GetCode(NodeInstruction node) {//im

        if (node.GetType() == Keyword.CONECTOR) {
            return "";
        }
        if (node.GetType() == Keyword.SE) {
            return ExpandSe.toString(node);
        }
        if (node.GetType() == Keyword.ENQUANTO) {
            return ExpandEnquanto.toString(node);
        }
        return node.toString() + "\n";
    }

    private Vector ObterParametrosValues(String text) {
        int i = text.indexOf("(");
        int f = text.length() - 1;
        while (f > i && text.charAt(f) != ')') {
            f--;
        }
        text = text.substring(i + 1, f);
        Vector paramvals = new Vector();

        IteratorCodeParams params = new IteratorCodeParams(text);

        while (params.hasMoreElements()) {
            String param = params.current();
            Object valor;
            Simbolo var = Variavel.getVariable(param, memory);
            SimboloDeParametro SP = new SimboloDeParametro();
            SP.Name = param;
            if (var == null) {
                valor = Expressao.Evaluate(param, memory);
                SP.Value = valor;
                SP.PorValor = true;
            } else {
                SP.Value = var;
                SP.PorValor = false;
            }
            paramvals.add(SP);
            params.getNext();
        }

        return paramvals;
    }
}