package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Consola.ConsoleIO;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Operador;
import Portugol.Language.Analisador.ParteDeExpresion;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SimboloDeParametro;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.SymbolComposto;
import Portugol.Language.Analisador.SymbolObjeto;
import Portugol.Language.Analisador.TipoDeParametro;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Calcular.Aritmeticos;
import java.util.Vector;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public class BloqueSubrutine extends Bloque {

    public static String VERSION = "Versão:1.0 \t(c) Augusto Bilabila";
    public static String ErroRecursividad = "Detectado llamado recursivo. CompAlg no permite este llamado";
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
    public String TipoRetorno;
    public boolean EstaExecutando;
    public BloqueClasse classePae; //Se esta dentro duma classe, aqui temos a referença
    static public SymbolObjeto InstanciaActual = null;//Se esta dentro duma classe, aqui temos a instancia actual

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
        EstaExecutando = false;
        classePae = null; //vamos supor que a sub-rutina não esta dentro de classe alguma
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
    public Simbolo ExecuteSubrutine(Vector paramVals, SymbolObjeto ObjetoDono) throws LanguageException {
        if (EstaExecutando) {
            throw new LanguageException(
                    ErroRecursividad,
                    "Verifique o nome do sub-algoritmo" //David: Traducir.
                    );
        }

        NodeInstruction temporal = getStartNode();
        boolean esperarRetorno = temporal.GetType() == Keyword.FUNCAO;
        boolean EsConstructor = temporal.GetType() == Keyword.CONSTRUTOR;
        paramsValues = paramVals;

        if (parametros.size() != paramsValues.size()) {
            throw new LanguageException(
                    "No se están pasando todos los parametros requeridos", //David: Traducir
                    "Verifique la cantidad de parametros en el llamado del sub-algoritmo");
        }

        if (Intermediario.console == null) {
            if (!esperarRetorno) {
                if (EsConstructor) {
                    ObjetoDono = new SymbolObjeto("", TipoRetorno, "RETORNO", TipoRetorno, 0, (TipoRetorno + " RETORNO <- " + TipoRetorno));
                    ObjetoDono.Inicializado = true;
                    return ObjetoDono;
                } else {
                    return null;//David: No caso dos procedimentos
                }
            } else {
                if (Simbolo.getType(TipoRetorno) == Simbolo.REGISTO) {
                    return new SymbolComposto("", TipoRetorno, "RETORNO", TipoRetorno, 0, (TipoRetorno + " RETORNO <- " + TipoRetorno));
                } else if (Simbolo.getType(TipoRetorno) == Simbolo.CLASSE) {
                    ObjetoDono = new SymbolObjeto("", TipoRetorno, "RETORNO", TipoRetorno, 0, (TipoRetorno + " RETORNO <- " + TipoRetorno));
                    ObjetoDono.Inicializado = true;
                    return ObjetoDono;
                } else {
                    return new Simbolo("", TipoRetorno, "RETORNO", null, 0, (TipoRetorno + " RETORNO"));
                }
            }
        }

        if (EsConstructor) {
            ObjetoDono = new SymbolObjeto("", TipoRetorno, "RETORNO", TipoRetorno, 0, (TipoRetorno + " RETORNO <- " + TipoRetorno));
            ObjetoDono.Inicializado = true;
        } else if (classePae == null && ObjetoDono != null || classePae != null && ObjetoDono == null) {
            throw new LanguageException(
                    "Problema com paridade entre clase e instancia para um metodo",
                    "Erro interno do CompAlg" //David: Traducir.
                    );
        }

        SymbolObjeto InstanciaAnterior = InstanciaActual;
        InstanciaActual = ObjetoDono;
        memory = new Vector();
        EstaExecutando = true;
        while (temporal != null) //David: novo
        {
            temporal = ExecuteLine(temporal, Intermediario.console);
            //autoExecute.sleep(2);//David: 10 //David: Verificar si es necesario este sleep
        }
        EstaExecutando = false;
        InstanciaActual = InstanciaAnterior;

        if (!esperarRetorno) {
            if (EsConstructor) {
                return ObjetoDono;
            } else {
                return null;//David: No caso dos procedimentos
            }
        } else {
            ParteDeExpresion var = Variavel.getVariable("RETORNE", memory);
            if (var == null || var instanceof Operador) {
                throw new LanguageException(
                        "A função terminó sem retornar o valor",
                        "Especifique o código de retorne na funcão");
            } else {
                return (Simbolo) var;
            }
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

            case Keyword.FUNCAO:
            case Keyword.PROCEDIMENTO:
            case Keyword.CONSTRUTOR:
                cleanMemory(node.GetLevel(), memory);
                return node.GetNext();

            case Keyword.CHAMADOPROCEDIMENTO:
                try {
                    Expressao.ReplaceVariablesToValues(Expressao.ExpresionStringToVector(node.GetText()), memory, true);
                } catch (LanguageException e) {
                    if (e.line > 0 && !e.codeLine.isEmpty()) {
                        throw e;
                    }
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            e.error, e.solution);
                }
                return node.GetNext();

            case Keyword.FIMFUNCAO:
            case Keyword.FIMPROCEDIMENTO:
            case Keyword.FIMCONSTRUTOR:
                cleanMemory(node.GetLevel(), memory);
                return null;//David: Por alguna razon el metodo no se ejecuta como INICIO, se mantiene repitiendo

            case Keyword.DEFINIR:
                try {
                    Variavel.defineVAR(node, memory, this.parametros, paramsValues);
                } catch (LanguageException ex) {
                    if (ex.line > 0 && !ex.codeLine.isEmpty()) {
                        throw ex;
                    }
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            ex.error, ex.solution);
                }
                return node.GetNext();

            case Keyword.CALCULAR:
                try {
                    executeCalculate(node.GetText());
                } catch (Exception ex) {
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            ex.getMessage(),
                            "Verifique a expressão" //David: Traducir.
                            );
                }
                return node.GetNext();

            case Keyword.LEIA:
                try {
                    executeREAD(node.GetText(), console);
                } catch (LanguageException ex) {
                    if (ex.line > 0 && !ex.codeLine.isEmpty()) {
                        throw ex;
                    }
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            ex.error, ex.solution);
                }
                return node.GetNext();

            case Keyword.ESCREVA:
                try {
                    executeWRITE(node.GetText(), console);
                } catch (LanguageException ex) {
                    if (ex.line > 0 && !ex.codeLine.isEmpty()) {
                        throw ex;
                    }
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            ex.error, ex.solution);
                }
                return node.GetNext();

            case Keyword.RETORNE:
                try {
                    executeRETORNO(node.GetText());
                } catch (LanguageException ex) {
                    if (ex.line > 0 && !ex.codeLine.isEmpty()) {
                        throw ex;
                    }
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            ex.error, ex.solution);
                }
                return null;

            case Keyword.LIMPATELA:
                console.Clear();
            //return  node.GetNext();

            case Keyword.CONECTOR:
            case Keyword.FAZ:
            case Keyword.REPETE:
            case Keyword.ESCOLHA:
                return node.GetNext();
            case Keyword.PASSO:
                double valor;
                Object value;
                try {
                    value = Expressao.Evaluate(node.GetText(), memory);
                } catch (LanguageException ex) {
                    if (ex.line > 0 && !ex.codeLine.isEmpty()) {
                        throw ex;
                    }
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            ex.error, ex.solution);
                }
                valor = Double.parseDouble((String) value);

                //passo nulo
                if (valor == 0.0) {
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            "ERRO - o PASSO do ciclo PARA é zero",
                            "Corrija o PASSO");
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
                Object compare;
                try {
                    compare = Expressao.Evaluate(node.GetText(), memory);
                } catch (LanguageException ex) {
                    if (ex.line > 0 && !ex.codeLine.isEmpty()) {
                        throw ex;
                    }
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            ex.error, ex.solution);
                }
                //variaveis defenidas dentro do bloco
                cleanMemory(node.GetLevel() + 1, memory);
                if (((String) compare).equalsIgnoreCase(Values.VERDADEIRO)) {
                    return node.GetIfTrue();
                } else {
                    return node.GetIfFalse();
                }
        }
        throw new LanguageException(
                node.GetCharNum(), node.GetText(),
                "INSTRUÇÃO DESCONHECIDA",
                node.GetText());

    }

//------------------------------------------------------------------------------
//------------                                             ---------------------
//------------         C A L C U L A R                     ---------------------
//------------                                              --------------------
//------------------------------------------------------------------------------      
    protected void executeCalculate(String str) throws LanguageException, Exception {
        int pos = str.indexOf(Keyword.ATRIBUI);
        String var = str.substring(0, pos).trim();
        String values = str.substring(pos + Keyword.ATRIBUI.length()).trim();

        Object newValue = Expressao.Evaluate(values, memory);
        if (newValue != null && newValue instanceof String
                && (newValue.equals(Expressao.ErroDeCalculo)
                || newValue.equals(Aritmeticos.ErroDivPorZero)
                || newValue.equals(BloqueSubrutine.ErroRecursividad)
                || ((String) newValue).startsWith(SymbolArray.ErroForaLimites))) {
            throw new Exception((String) newValue);
        }
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
            //line.append(Values.getStringValue(elemLine.toString()));

            if (elemLine == null) {
                //error
            } else if (elemLine instanceof String) {
                console.write(Values.getStringValue((String) elemLine));
            } else if (elemLine instanceof Operador) {
                //nada ou error
            } else if (elemLine instanceof SymbolArray) {
                if (elem.contains("[") && elem.contains("]")) {
                    SymbolArray a = (SymbolArray) elemLine;
                    a.SetIndex(elem, memory);
                    console.write(Values.getStringValue(((SymbolArray) elemLine).getValue().toString()));
                } else {
                    //error
                }
            } else if (elemLine instanceof SymbolComposto) {
                //error
            } else if (elemLine instanceof SymbolObjeto) {
                //error
            } else {
                console.write(Values.getStringValue(((Simbolo) elemLine).getValue().toString()));
            }
        }
        //console.write(line.toString());//David: Os dados tinam de sair pra consola enquanto são calculados
    }

    protected void executeRETORNO(String str) throws LanguageException {
        String values = str.substring(Keyword.GetTextKey(Keyword.RETORNE).length()).trim();

        Object newValue = Expressao.Evaluate(values, memory);

        int type = Simbolo.getType(TipoRetorno);

        Simbolo v;
        if (type == Simbolo.REGISTO && newValue instanceof SymbolComposto) {
            v = new SymbolComposto((SymbolComposto) newValue);
        } else if (type == Simbolo.CLASSE && newValue instanceof SymbolObjeto) {
            v = new SymbolObjeto((SymbolObjeto) newValue);
        } else if (newValue instanceof SymbolArray) {
            v = new SymbolArray((SymbolArray) newValue);
        } else if (newValue instanceof Simbolo) {
            v = new Simbolo((Simbolo) newValue);
        } else {
            v = new Simbolo("const", TipoRetorno, "RETORNE", newValue, 0, ("const " + TipoRetorno + " RETORNE <- " + (String) newValue));
        }

        v.setName("RETORNE");
        memory.add(v);
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

        ParteDeExpresion pde = Variavel.getVariable(varName, memory);
        // fazer o set ao index do array
        if (str.contains("[") && varName.contains("]")) {
            ((SymbolArray) pde).SetIndex(varName, memory);
        } else {
            throw new LanguageException(0, varName,
                    "O vetor " + varName + " não pose ser uma variavel num operador LEIA ",
                    "Mude a variavel");//David:Revisar
        }

        String value = console.read((Simbolo) pde);
        ((Simbolo) pde).setValue(value);
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

    public Vector ObterParametrosValues(Vector params, Vector memory) throws LanguageException {
        if (params.size() < 3) {
            throw new LanguageException(
                    "O chamado á sub-algoritmo não é correto",
                    "Verifique a expressão da chamada"); //David: revisar ortografia
        }
        Vector paramvals = new Vector();
        Vector paramx = new Vector();
        Object valor;
        ParteDeExpresion var = null;
        SimboloDeParametro SP;
        for (int i = 2; i < params.size(); i++) { //David: tiré -1 par que incluyera el parentesis final
            if (params.get(i) instanceof String) {
                String param = ((String) params.get(i)).trim();
                if (param.equals(",") || param.equals(")")) {
                    if (paramx.size() == 1) {
                        Object x = paramx.get(0);
                        if (x instanceof String) {
                            var = Variavel.getVariable((String) x, memory);
                        } else if (x instanceof Simbolo) {
                            var = (Simbolo) x;
                        }
                    }

                    if (paramx.isEmpty()) {
                        continue;
                    }

                    SP = new SimboloDeParametro();
                    if (var == null) {
                        valor = Expressao.Evaluate(paramx, memory);
                        SP.Value = valor;
                        SP.PorValor = true;
                    } else if (var instanceof Simbolo) {
                        SP.Value = (Simbolo) var;
                        SP.PorValor = false;
                    } else {
                        throw new LanguageException(0, param,
                                "O parâmetro " + param + " não pode ser um operador ",
                                "Mude a expressão");//David:Revisar                
                    }
                    paramvals.add(SP);
                    paramx.clear();

                } else {
                    paramx.add(param);
                }
            } else {
                paramx.add(params.get(i));
            }
        } //for
        return paramvals;
    }
}