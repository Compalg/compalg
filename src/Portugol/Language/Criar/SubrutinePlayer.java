package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import static Portugol.Language.Analisador.Expressao.ExpresionStringToVector;
import Portugol.Language.Consola.ConsoleIO;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.Operador;
import Portugol.Language.Analisador.ParteDeExpresion;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SimboloDeParametro;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.SymbolComposto;
import Portugol.Language.Analisador.SymbolObjeto;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Calcular.Aritmeticos;
import java.util.Vector;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.RunListener;
import Portugol.Language.Utilitario.Values;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public class SubrutinePlayer {

    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";
    public static String ErroRecursividad = "Detectado llamado recursivo. CompAlg no permite este llamado";
    public Vector<InstanciaSubrutine> pilhaExecucao;
    public InstanciaSubrutine instanciaSubrutineActual;
    static public SubrutinePlayer Player;
    static public int ProgStateRUN = 1000;
    static public int ProgStateDEBUG = 2000;
    boolean suspended = false;
    public int RunState;
    private RunListener runAdapter = null;
    //public static String VerOperator = " " ;//im

    /**
     * Constroi um fluxograma
     *
     * @param code programa fonte
     * @throws Portugol.Language.Utils.LanguageException excepcao
     */
    public SubrutinePlayer(RunListener runAdapter) {
        pilhaExecucao = new Vector<InstanciaSubrutine>();
        instanciaSubrutineActual = null;
        RunState = -1;
        Player = this;
        this.runAdapter = runAdapter;
    }

//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
//------------                                              ---------------------------
//------------  E X E C U C A O   D A   I N S T R U C A O   ---------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
//-------------------------------------------------------------------------------------
    public void SuspendPlay() {
        suspended = true;
    }

    public synchronized void ResumePlay() {
        suspended = false;
        notify();
    }

    public void LimparPilhaExecucao() {
        instanciaSubrutineActual = null;
        pilhaExecucao.clear();
    }

    /**
     * Executa uma linha de codigo - normalmente
     */
    public Simbolo ExecuteSubrutine(BloqueSubrutine bloqueRutina, Vector paramVals, SymbolObjeto ObjetoDono) throws LanguageException, InterruptedException {
        if (instanciaSubrutineActual != null && Intermediario.console != null) { //não é inicio  E  não e expandir senao executar
            pilhaExecucao.insertElementAt(instanciaSubrutineActual, 0);
        }
        instanciaSubrutineActual = new InstanciaSubrutine(bloqueRutina, ObjetoDono, paramVals);

        if (bloqueRutina.parametrosDefinition.size() != instanciaSubrutineActual.paramsValues.size()) {
            throw new LanguageException(
                    "No se están pasando todos los parametros requeridos", //David: Traducir
                    "Verifique la cantidad de parametros en el llamado del sub-algoritmo");
        }

        if (Intermediario.console == null) {
            if (!instanciaSubrutineActual.esperarRetorno) {
                if (instanciaSubrutineActual.EsConstructor) {
                    ObjetoDono = new SymbolObjeto("", instanciaSubrutineActual.BloqueRutina.TipoRetorno, "RETORNO", instanciaSubrutineActual.BloqueRutina.TipoRetorno, 0, (instanciaSubrutineActual.BloqueRutina.TipoRetorno + " RETORNO <- " + instanciaSubrutineActual.BloqueRutina.TipoRetorno));
                    ObjetoDono.Inicializado = true;
                    this.instanciaSubrutineActual.InstanciaDono = ObjetoDono;
                    return ObjetoDono;
                } else {
                    return null;//David: No caso dos procedimentos
                }
            } else {
                if (Simbolo.getType(instanciaSubrutineActual.BloqueRutina.TipoRetorno) == Simbolo.REGISTO) {
                    return new SymbolComposto("", instanciaSubrutineActual.BloqueRutina.TipoRetorno, "RETORNO", instanciaSubrutineActual.BloqueRutina.TipoRetorno, 0, (instanciaSubrutineActual.BloqueRutina.TipoRetorno + " RETORNO <- " + instanciaSubrutineActual.BloqueRutina.TipoRetorno));
                } else if (Simbolo.getType(instanciaSubrutineActual.BloqueRutina.TipoRetorno) == Simbolo.CLASSE) {
                    ObjetoDono = new SymbolObjeto("", instanciaSubrutineActual.BloqueRutina.TipoRetorno, "RETORNO", instanciaSubrutineActual.BloqueRutina.TipoRetorno, 0, (instanciaSubrutineActual.BloqueRutina.TipoRetorno + " RETORNO <- " + instanciaSubrutineActual.BloqueRutina.TipoRetorno));
                    ObjetoDono.Inicializado = true;
                    return ObjetoDono;
                } else {
                    return new Simbolo("", instanciaSubrutineActual.BloqueRutina.TipoRetorno, "RETORNO", null, 0, (instanciaSubrutineActual.BloqueRutina.TipoRetorno + " RETORNO"));
                }
            }
        }

        if (instanciaSubrutineActual.EsConstructor) {
            ObjetoDono = new SymbolObjeto("", instanciaSubrutineActual.BloqueRutina.TipoRetorno, "RETORNO", instanciaSubrutineActual.BloqueRutina.TipoRetorno, 0, (instanciaSubrutineActual.BloqueRutina.TipoRetorno + " RETORNO <- " + instanciaSubrutineActual.BloqueRutina.TipoRetorno));
            ObjetoDono.Inicializado = true;
            this.instanciaSubrutineActual.InstanciaDono = ObjetoDono;
        } else if (instanciaSubrutineActual.BloqueRutina.classePae == null && ObjetoDono != null || instanciaSubrutineActual.BloqueRutina.classePae != null && ObjetoDono == null) {
            throw new LanguageException(
                    "Problema com paridade entre clase e instancia para um metodo",
                    "Erro interno do CompAlg" //David: Traducir.
            );
        }

        //SymbolObjeto InstanciaAnterior = instanciaActual.InstanciaActual;
        //instanciaActual.InstanciaActual = ObjetoDono;
        while (instanciaSubrutineActual.NodeActual != null) //David: novo
        {
            instanciaSubrutineActual.NodePrevio = instanciaSubrutineActual.NodeActual;
            instanciaSubrutineActual.NodeActual = ExecuteLine(instanciaSubrutineActual.NodeActual, Intermediario.console);
            if (RunState == SubrutinePlayer.ProgStateDEBUG) {
                if (instanciaSubrutineActual.NodeActual != null && instanciaSubrutineActual.NodeActual.EsReferencia == false) {
                    SubrutinePlayer.Player.SuspendPlay();
                }
                runAdapter.RunPerformed(instanciaSubrutineActual);
                synchronized (this) {
                    while (suspended && instanciaSubrutineActual.NodeActual != null && instanciaSubrutineActual.NodeActual.EsReferencia == false) {
                        wait();
                    }
                }
            }
        }

        //instanciaActual.InstanciaActual = InstanciaAnterior;
        Simbolo OQueVamosRetornar = null;
        if (!instanciaSubrutineActual.esperarRetorno) {
            if (instanciaSubrutineActual.EsConstructor) {
                OQueVamosRetornar = (Simbolo) ObjetoDono;
            }
        } else {
            ParteDeExpresion var = Variavel.getVariable("RETORNE", instanciaSubrutineActual.memory);
            if (var == null || var instanceof Operador) {
                throw new LanguageException(
                        "A função terminó sem retornar o valor",
                        "Especifique o código de retorne na funcão");
            } else {
                OQueVamosRetornar = (Simbolo) var;
            }
        }
        if (!pilhaExecucao.isEmpty()) {
            instanciaSubrutineActual = pilhaExecucao.get(0);
            runAdapter.RunPerformed(instanciaSubrutineActual);
            pilhaExecucao.removeElementAt(0);
        } else {
            instanciaSubrutineActual = null;
        }
        return OQueVamosRetornar;
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
                cleanMemory(node.GetLevel(), instanciaSubrutineActual.memory);
                return node.GetNext();

            case Keyword.FIM:
                //cleanMemory(node.GetLevel(), rutinaActual.memory);
                return null;

            case Keyword.FUNCAO:
            case Keyword.PROCEDIMENTO:
            case Keyword.CONSTRUTOR:
                cleanMemory(node.GetLevel(), instanciaSubrutineActual.memory);
                return node.GetNext();

            case Keyword.CHAMADOPROCEDIMENTO:
                try {
                    Expressao.ReplaceVariablesToValues(Expressao.ExpresionStringToVector(node.GetText()), instanciaSubrutineActual.memory, true);
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
                cleanMemory(node.GetLevel(), instanciaSubrutineActual.memory);
                return null;//David: Por alguna razon el metodo no se ejecuta como INICIO, se mantiene repitiendo

            case Keyword.DEFINIR:
                try {
                    Variavel.defineVAR(node, instanciaSubrutineActual.memory, instanciaSubrutineActual.BloqueRutina.parametrosDefinition, instanciaSubrutineActual.paramsValues);
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
                    executeCalculate(node, instanciaSubrutineActual.memory);
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
                    value = Expressao.Evaluate(node.GetText(), instanciaSubrutineActual.memory, false);
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
                    compare = Expressao.Evaluate(node.GetText(), instanciaSubrutineActual.memory, false);
                } catch (LanguageException ex) {
                    if (ex.line > 0 && !ex.codeLine.isEmpty()) {
                        throw ex;
                    }
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            ex.error, ex.solution);
                }
                //variaveis defenidas dentro do bloco
                cleanMemory(node.GetLevel() + 1, instanciaSubrutineActual.memory);
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
    protected void executeCalculate(NodeInstruction node, Vector memory) throws LanguageException, Exception {
        String str = node.GetText();
        int pos = str.indexOf(Keyword.ATRIBUI);
        String var = str.substring(0, pos).trim();
        String values = str.substring(pos + Keyword.ATRIBUI.length()).trim();
        Vector expressao = ExpresionStringToVector(values);

        Object newValue = Expressao.Evaluate(expressao, memory, false);//false == nao safe, executar na verdade
        if (newValue != null && newValue instanceof String
                && (newValue.equals(Expressao.ErroDeCalculo)
                || newValue.equals(Aritmeticos.ErroDivPorZero)
                || newValue.equals(SubrutinePlayer.ErroRecursividad)
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
                elemLine = Expressao.Evaluate(elem, instanciaSubrutineActual.memory, false);
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
                    a.SetIndex(elem, instanciaSubrutineActual.memory);
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
        Vector expressao = ExpresionStringToVector(values);
        Object newValue = Expressao.Evaluate(expressao, instanciaSubrutineActual.memory, false);

        int type = Simbolo.getType(instanciaSubrutineActual.BloqueRutina.TipoRetorno);

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
            v = new Simbolo("const", instanciaSubrutineActual.BloqueRutina.TipoRetorno, "RETORNE", newValue, 0, ("const " + instanciaSubrutineActual.BloqueRutina.TipoRetorno + " RETORNE <- " + (String) newValue));
        }

        v.setName("RETORNE");
        instanciaSubrutineActual.memory.add(v);
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

        ParteDeExpresion pde = Variavel.getVariable(varName, instanciaSubrutineActual.memory);
        // fazer o set ao index do array
        if (str.contains("[") && varName.contains("]")) {
            ((SymbolArray) pde).SetIndex(varName, instanciaSubrutineActual.memory);
        } else {
            throw new LanguageException(0, varName,
                    "O vector " + varName + " não pode ser uma variável na instrução LEIA ",
                    "Mude a variavel");//David:Revisar
        }

        String value = console.read((Simbolo) pde);
        ((Simbolo) pde).setValue(value);
    }

//=============================================================================
    /**
     * string
     *
     * @return string
     */
    public String toString() {
        StringBuffer str = new StringBuffer();
        NodeInstruction pt = instanciaSubrutineActual.BloqueRutina.start;
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
                    "A chamada do sub-algoritmo não está correta",
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
                            var = Variavel.getVariable((String) x, /*instanciaActual.*/ memory);
                        } else if (x instanceof Simbolo) {
                            var = (Simbolo) x;
                        }
                    }

                    if (paramx.isEmpty()) {
                        continue;
                    }

                    SP = new SimboloDeParametro();
                    if (var == null) {
                        valor = Expressao.Evaluate(paramx, /*instanciaActual.*/ memory, false);
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

    public ParteDeExpresion SearchAndExecuteSubrutine(SymbolObjeto ObjetoDono, String name, Vector chmd, Vector searchMemory) throws LanguageException {
        if (ObjetoDono == null && instanciaSubrutineActual != null && instanciaSubrutineActual.InstanciaDono != null) {
            ObjetoDono = instanciaSubrutineActual.InstanciaDono;
        }
        Vector<BloqueSubrutine> metodos = ObjetoDono != null ? ObjetoDono.tipoClasseBase.claseOrigen.metodos : null;
        BloqueSubrutine rutina = ExpandCHAMADO(name, metodos);
        if (Intermediario.console != null //não é expandir senao executar
                && ObjetoDono != null && ObjetoDono.Inicializado == false) {
            throw new LanguageException(
                    "Não pode utilizar o objeto \" " + ObjetoDono.getName() + " \" antes de ser inicializado com o Construtor ", //David: revisar ortografia
                    "Antes utilice um código similar ao seguinte: \"" + ObjetoDono.getName() + " <- NOVO " + ObjetoDono.tipoClasseBase.Name.toLowerCase() + "( . . . )\"");
        }

        Vector paramVals = ObterParametrosValues(chmd, searchMemory);
        //ObjetoDono = ObjetoDono != null ? ObjetoDono : (BloqueSubrutine.InstanciaActual);
        try {
            return ExecuteSubrutine(rutina, paramVals, ObjetoDono);
        } catch (InterruptedException ex) {
            throw new LanguageException(
                    "Error de Interrupçao da execuçao", "Erro do CompAlg");
        }
    }

    public static BloqueSubrutine ExpandCHAMADO(String Text, Vector<BloqueSubrutine> metodos)
            throws LanguageException {
        //string das instrucoes
        //String exp = Normalize(Text);//David: Normalizado 
        // SE ocupa dois caracteres
        String nome_proced = Text./*substring(0, endExp).*/trim();
        //String parametros = Text.substring(endExp, Text.length()).trim();
        //int cont = 0, pos = 0;

        if (nome_proced.toUpperCase().startsWith(Keyword.GetTextKey(Keyword.NOVO) + " ")) {
            nome_proced = Text.substring(((String) Keyword.GetTextKey(Keyword.NOVO) + " ").length(), Text.length()).trim();
            for (int i = 0; i < Intermediario.tiposClasses.size(); i++) {
                if (Intermediario.tiposClasses.elementAt(i).claseOrigen.Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                    if (Intermediario.tiposClasses.elementAt(i).claseOrigen.Construtor != null
                            && Intermediario.tiposClasses.elementAt(i).claseOrigen.Construtor.Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                        return Intermediario.tiposClasses.elementAt(i).claseOrigen.Construtor;
                    } else {
                        throw new LanguageException(
                                "A Classe <" + nome_proced + "> não tem Construtor definido", //David: revisar ortografia
                                "Programe o construtor da Classe");
                    }
                }
            }
            throw new LanguageException(
                    "A Classe <" + nome_proced + "> não existe", //David: revisar ortografia
                    "Mude o código");
        }

//        if (BloqueSubrutine.InstanciaActual != null) {
//            BloqueClasse claseOrigen = BloqueSubrutine.InstanciaActual.tipoClasseBase.claseOrigen;
//            for (int i = 0; i < claseOrigen.metodos.size(); i++) {
//                if (claseOrigen.metodos.elementAt(i).Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
//                    return claseOrigen.metodos.elementAt(i);
//                }
//            }
//        }
        if (metodos != null) {
            for (int i = 0; i < metodos.size(); i++) {
                if (metodos.elementAt(i).Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                    return metodos.elementAt(i);
                }
            }
        }

        if (BloqueClasse.ClaseActualParaExpandir != null) {
            for (int i = 0; i < BloqueClasse.ClaseActualParaExpandir.metodos.size(); i++) {
                if (BloqueClasse.ClaseActualParaExpandir.metodos.elementAt(i).Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                    return BloqueClasse.ClaseActualParaExpandir.metodos.elementAt(i);
                }
            }
        }

        for (int i = 0; i < Intermediario.subrutinas.size(); i++) {
            if (Intermediario.subrutinas.elementAt(i).Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                return Intermediario.subrutinas.elementAt(i);
            }
        }

        for (int i = 0; i < Intermediario.tiposClasses.size(); i++) {
            if (Intermediario.tiposClasses.elementAt(i).claseOrigen.Nome.toUpperCase().equals(nome_proced.toUpperCase())) {
                throw new LanguageException(
                        "Para chamar ao Construtor <" + nome_proced + "> debe utilizar o operador NOVO", //David: revisar ortografia
                        "Acrecente o operador NOVO no chamado do Construtor.");
            }
        }

        throw new LanguageException(
                "O procedimento <" + nome_proced + "> não existe", //David: revisar ortografia
                "Verifique no seu código se existe um procedimento com este nome");

    }
}
