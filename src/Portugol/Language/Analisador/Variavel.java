package Portugol.Language.Analisador;

import Portugol.Language.Calcular.Calculador;
import Portugol.Language.Criar.BloqueSubrutine;
import Portugol.Language.Criar.ExpandChamadoFuncao;
import Portugol.Language.Criar.NodeInstruction;
import Portugol.Language.Utilitario.IteratorArray;
import Portugol.Language.Utilitario.IteratorCodeParams;
import Portugol.Language.Utilitario.IteratorString;
import Portugol.Language.Utilitario.LanguageException;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;

/**
 * @author Augusto Bilabila(2011) Original de Antonio manso(2006)
 */
public class Variavel {

    private static String caracteres_aceites =
            "abcdefghijklmnopqrstuvxywz"
            + "ABCDEFGHIJKLMNOPQRSTUVXYWZ"
            + "._0123456789";

    public static boolean isNameAcceptable(String nameVar) {

        String name = nameVar.trim(); //David: Aqui deberia revolverse otro valor, de modo que el chamador pueda saber cual es el error especifico y dar una mejor propuesta de solucion

        if (name.length() == 0) {
            return false;
        }
        if (Character.isDigit(name.charAt(0))) {
            return false;
        }

        for (int i = 1; i < name.length(); i++) {
            if (caracteres_aceites.indexOf(name.charAt(i)) == -1) {
                return false;
            }
        }

        if (Keyword.IsKeyword(nameVar) || Calculador.IsCalculus(nameVar)) {
            return false;
        }


        return true;
    }

    /**
     * calcula o erro do nome da variavel
     *
     * @param nameVar nome da variavel
     * @return causa do erro
     */
    public static String getErrorName(String nameVar) {
        String name = nameVar.trim();
        if (name.length() == 0) {
            return " O NOME NÃO PODE SER VAZIO ";
        }
        if (Character.isDigit(name.charAt(0))) {
            return " O NOME DE UMA VARIÁVEL NÃO PODE COMEÇAR COM UM NÚMERO ";
        }
        if (Keyword.IsKeyword(nameVar)) {
            return name + ", NÃO PODE SER O NOME DE UMA VARIÁVEL, PORQUE É UMA PALAVRA RESERVADA!!! ";
        }
        if (Calculador.IsCalculus(nameVar)) {
            return name + " É UM ELEMENTO DE CÁLCULO ";
        }

        if (name.indexOf("=") != -1) {
            return "O SINAL \"=\" DEVE SER SUBSTITUÍDO POR \"<-\" (SINAL DE ATRIBUÍÇÃO DO PSEUDOCODIGO) ";
        }

        for (int i = 1; i < name.length(); i++) {
            if (caracteres_aceites.indexOf("" + name.charAt(i)) == -1) {
                return name + " CONTÉM O CARACTER \"" + name.charAt(i) + "\" NÃO É VÁLIDO ";
            }
        }

        return "ERRO NO NOME";
    }

    // PARA REGISTO
    public static String getErrorNameRegisto(String nameVar) {
        String nome = nameVar.trim();
        if (nome.length() == 0) {
            return " O NOME NÃO PODE SER VAZIO ";
        }
        if (Character.isDigit(nome.charAt(0))) {
            return " O NOME DE UM REGISTO NÃO PODE COMEÇAR COM UM NÚMERO ";
        }
        if (Keyword.IsKeyword(nameVar)) {
            return nome + ", NÃO PODE SER O NOME DE UM REGISTO, PORQUE É UMA PALAVRA RESERVADA!!! ";
        }
        if (Calculador.IsCalculus(nameVar)) {
            return nome + " É UM ELEMENTO DE CÁLCULO ";
        }

        for (int i = 1; i < nome.length(); i++) {
            if (caracteres_aceites.indexOf("" + nome.charAt(i)) == -1) {
                return nome + " CONTÉM O CARACTER \"" + nome.charAt(i) + "\" NÃO É VÁLIDO ";
            }
        }
        return "ERRO NO NOME";
    }

    public static ParteDeExpresion getVariable(Object elemento, Vector searchMemory) throws LanguageException {
        String name = "";
        if (elemento instanceof String) {
            name = (String) elemento;

            Operador res = Operador.TryOperador(name);
            if (res != null) {
                return res;
            }
        } else if (elemento instanceof Vector) {
            Vector chmd = (Vector) elemento;
            name = (String) chmd.get(0);
            if (((String) chmd.get(1)).equals("(") && ((String) chmd.get(chmd.size() - 1)).equals(")")) {
                BloqueSubrutine rutina = ExpandChamadoFuncao.ExpandCHAMADO(name);
                Vector paramVals = rutina.ObterParametrosValues(chmd, searchMemory);

                return rutina.ExecuteSubrutine(paramVals);
            }

        }

        return getVariable(name, searchMemory, searchMemory);
    }
    //-------------------------------------------------------------------------

    private static ParteDeExpresion getVariable(String name, Vector searchMemory, Vector dataMemory) throws LanguageException {
        //     return null;
        String varName;
        String Campo;
        int posR = name.indexOf(".");
        int posA = -1;//name.indexOf("[");

        if (posR == -1 && posA == -1) {
            varName = name;
            Campo = "";
        } else //if (posR > -1 && posA == -1) 
        {
            varName = name.substring(0, posR).trim();
            Campo = name.substring(posR + 1).trim();
        }

        //String varName = name.trim();
        for (int index = searchMemory.size() - 1; index >= 0; index--) {
            Simbolo v = (Simbolo) searchMemory.get(index);
            if (v.nameEqual(varName)) {
                if (posR == -1 && Campo.isEmpty()) {

                    if (v instanceof SymbolArray) {
                        if (varName.contains("[") && varName.contains("]")) {
                            ((SymbolArray) v).SetIndex(varName, dataMemory);
                            return ((SymbolArray) v).getValue();
                        } else {
                            return v;
                        }
                    } else {
                        return v;
                    }

                } else {
                    if (v instanceof SymbolArray) {
                        ((SymbolArray) v).SetIndex(varName, dataMemory);
                        return getVariable(Campo, ((SymbolComposto) v.getValue()).Campos, dataMemory);
                    } else {
                        return getVariable(Campo, ((SymbolComposto) v).Campos, dataMemory);
                    }
                }
            }
        }
        return null;
    }

    public static void defineVAR(NodeInstruction node, Vector memory, Vector<TipoDeParametro> params, Vector<SimboloDeParametro> paramVals) throws LanguageException {
        if (SymbolArray.isArray(node.GetText())) {
            Variavel.defineArray(node, memory, params, paramVals);
        } else {
            Variavel.defineSimples(node, memory, params, paramVals);
        }
    }

    //------------------------------------------------------------------------
    public static void defineSimples(NodeInstruction node, Vector memory, Vector<TipoDeParametro> params, Vector<SimboloDeParametro> paramVals) throws LanguageException {
        // VARIAVEL TIPO VAR <- VALOR
        IteratorString tok = new IteratorString(node.GetText());
        String modif = tok.current();
        tok.next();
        String tipo = tok.current();
        tok.next();
        String nome = tok.current();
        tok.next();
        String atribui = tok.current();
        tok.next();
        String valorUnproc = tok.getUnprocessedString();

        Object valor = null;
        SimboloDeParametro Parametro = null;
        try {
            int pos = -1;
            for (int i = 0; i < params.size(); i++) {
                if (params.get(i).Name.toUpperCase().equals(nome.toUpperCase())) {
                    pos = i;
                    break;
                }
            }
            if (pos > -1) {
                valor = null;
                Parametro = paramVals.get(pos);
            } else {
                valor = Expressao.Evaluate(valorUnproc, memory);
            }
        } catch (Exception e) {
            throw new LanguageException(
                    node.GetCharNum(), node.GetText(),
                    e.toString(),
                    "VERIFIQUE A EXPRESSÃO <" + valorUnproc + ">");
        }
        Simbolo v;
        if (Simbolo.getType(tipo) == Simbolo.REGISTO) {
            v = new SymbolComposto(modif, tipo, nome, valor, node.GetLevel(), node.GetText());
        } else {
            v = new Simbolo(modif, tipo, nome, valor, node.GetLevel(), node.GetText());
        }
        v.copyFrom(Parametro);
        memory.add(v);
    }

    //=========================================================================
    public static void defineArray(NodeInstruction node, Vector memory, Vector<TipoDeParametro> params, Vector<SimboloDeParametro> paramVals) throws LanguageException {
        // VARIAVEL TIPO VAR <- VALOR
        IteratorString tok = new IteratorString(node.GetText());
        String modif = tok.current();
        tok.next();
        String type = tok.current();
        tok.next();
        String name = tok.current();
        tok.next();

        String rest = tok.getUnprocessedString();
        int atr = rest.indexOf(Keyword.ATRIBUI);
        String indexes = rest.substring(0, atr).trim();
        String valorUnProc = rest.substring(atr + Keyword.ATRIBUI.length()).trim();
        Vector<Integer> indexLimits = new Vector<Integer>();
        String NovosIndexes = "[1]";//David: Inicialização falsa
        IteratorArray iter = new IteratorArray(indexes);
        while (iter.hasMoreElements()) {
            String exp = iter.getNext();
            Object result;
            try {
                result = Expressao.Evaluate(exp, memory);
            } catch (Exception e) {
                throw new LanguageException(
                        node.GetCharNum(), node.GetText(),
                        "O INDICE " + exp + " NÃO É UMA EXPRESSÃO VÁLIDA",
                        "DEFINA UMA EXPRESSÃO VÁLIDA PARA O INDICE");
            }
            if (!Values.IsInteger(result)) {
                throw new LanguageException(
                        node.GetCharNum(), node.GetText(),
                        exp + " = " + result + " NÃO É UMA EXPRESSÃO INTEIRA",
                        "");
            }
            int number = Integer.parseInt((String) result);
            if (number <= 0) {
                throw new LanguageException(
                        node.GetCharNum(), node.GetText(),
                        exp + " = " + result + " NÃO É UM VALOR VÁLIDO", "");
            }
            indexLimits.add(number);
        }
        SimboloDeParametro Parametro = null;
        IteratorCodeParams itercp;
        Vector<Simbolo> dataValues = new Vector<Simbolo>();
        ParteDeExpresion pdexpr;

        int pos = -1;
        for (int i = 0; i < params.size(); i++) {
            if (params.get(i).Name.toUpperCase().equals(name.toUpperCase())) {
                pos = i;
                break;
            }
        }
        if (pos > -1) {
            Parametro = paramVals.get(pos);
        } else if (indexLimits.size() == 1) {
            int index = 0;

            itercp = new IteratorCodeParams(valorUnProc, ",{}");
            while (itercp.hasMoreElements()) {
                String value = itercp.current();
                //Avaliar a expressao
                // se nao for possivel avaliar provoca erro
                Object result;
                try {
                    pdexpr = Variavel.getVariable(value, memory);
//                    if (pdexpr == null) {
//                        throw new LanguageException(
//                                node.GetCharNum(), node.GetText(),
//                                "A EXPRESSÃO :" + value + " NÃO FOI RECONHECIDA",
//                                "MUDE A EXPRESSÃO <" + value + ">");
//                    }
                    ///Hay que verificar que coincidan los tipos de VAR y el tipo base del arreglo
                    if (pdexpr instanceof Operador) {
                        //esto no debe pasar
                        throw new LanguageException(
                                node.GetCharNum(), node.GetText(),
                                "A EXPRESSÃO :" + value + " NÃO POSE SER UM OPERADOR",
                                "MUDE A EXPRESSÃO <" + value + ">");
                    } else if (pdexpr instanceof SymbolComposto) {
                        if (pdexpr instanceof SymbolArray) {
                            //esto no debe pasar
                            result = new SymbolArray((SymbolArray) pdexpr);
                        } else if (pdexpr instanceof SymbolComposto) {
                            if (((SymbolComposto) pdexpr).typeLexema.toUpperCase().trim().equals(type.toUpperCase().trim())) {
                                result = new SymbolComposto((SymbolComposto) pdexpr);
                            } else {
                                throw new LanguageException(
                                        node.GetCharNum(), node.GetText(),
                                        "A EXPRESSÃO :" + value + " NÃO É DE TIPO " + ((SymbolComposto) pdexpr).typeLexema,
                                        "MUDE ESTA EXPRESSÃO <" + value + ">");
                            }
                        } else if (pdexpr instanceof Simbolo) {
                            if (((Simbolo) pdexpr).typeLexema.toUpperCase().trim().equals(type.toUpperCase().trim())) {
                                result = new Simbolo((Simbolo) pdexpr);
                            } else {
                                throw new LanguageException(
                                        node.GetCharNum(), node.GetText(),
                                        "A EXPRESSÃO :" + value + " NÃO É DE TIPO " + ((SymbolComposto) pdexpr).typeLexema,
                                        "MUDE ESTA EXPRESSÃO <" + value + ">");
                            }
                        } else {
                            throw new LanguageException(
                                    node.GetCharNum(), node.GetText(),
                                    "ERRO NA EXPRESSÃO :" + value,
                                    "VERIFIQUE BEM ESTA EXPRESSÃO <" + value + ">");
                        }
                    } else {
                        result = Expressao.Evaluate(value, memory);
                    }
                } catch (Exception e) {
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            "ERRO NA EXPRESSÃO :" + value,
                            "VERIFIQUE BEM ESTA EXPRESSÃO <" + value + ">");
                }

                //verificar se o resultado da expressao e compativel com a variavel
                if (!Simbolo.IsCompatible(type, result)) {
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            "O valor <" + result + "> NÃO É PERMITIDO PARA A VARIÁVEL " + type,
                            " VERIFIQUE BEM ESTA EXPRESSÃO :" + value);
                }

                if (result instanceof Simbolo) //normalizar o resultado
                {
                    dataValues.add((Simbolo) result);
                } else if (result instanceof String) {
                    if (type.toUpperCase().equals(((String) result).toUpperCase())) {
                        //David: simbolo composto
                        dataValues.add(new SymbolComposto("", type, "nao_nome", result, node.GetLevel(), (type + " nao_nome <-" + type)));
                    } else {
                        //David: Só simbolo
                        dataValues.add(new Simbolo("", type, "nao_nome", result, node.GetLevel(), (type + " nao_nome")));
                    }
                }
                index++;
                if (index > indexLimits.get(0)) {
                    throw new LanguageException(
                            node.GetCharNum(), node.GetText(),
                            valorUnProc + " TEM MAIS QUE " + indexLimits.size() + " ELEMENTOS",
                            " DEFINA MENOS ELEMENTOS");
                }
                itercp.next();
            }
        } else if (indexLimits.size() > 1 && !valorUnProc.equals("0")) {
            throw new LanguageException(
                    node.GetCharNum(), node.GetText(),
                    "O CompAlg não suporta inicialização dos vetores com mais duma dimenção",
                    "Tire a inicialização.");
        }
        SymbolArray v = new SymbolArray(modif, type, name, indexLimits, dataValues, node.GetLevel(), memory, node.GetText());

        v.copyFrom(Parametro);
        memory.add(v);
    }

//------------------------------------------------------------------------------
    //-----------------------------------------------------------------------------
    public static void replaceVariableValue(String varName, Object newValue, Vector memory) throws LanguageException {
        ParteDeExpresion pde = Variavel.getVariable(varName.trim(), memory);
        if (pde == null) {
            throw new LanguageException(0, varName, " A VARIÁVEL \"" + varName + "\" NÃO ESTÁ DEFINIDA ",
                    " POR FAVOR, VERIFIQUE O NOME DA VARIÁVEL");//David:Revisar
        }

        if (pde instanceof SymbolArray) {
            if (varName.contains("[") && varName.contains("]")) {
                ((SymbolArray) pde).SetIndex(varName, memory);
            } else if (newValue instanceof SymbolArray) {
                ((SymbolArray) pde).copyFrom((SymbolArray) newValue);
                return; //debe parar aqui, ya a copia foi feita
            } else {
                throw new LanguageException(0, varName,
                        "O vetor " + varName + " só pode recever valores do tipo " + pde.TextoOrigen,
                        "Mude a assinação");//David:Revisar
            }
        }

        if (pde instanceof SymbolComposto) {
            if (newValue instanceof SymbolComposto) {
                ((SymbolComposto) pde).copyFrom((SymbolComposto) newValue);
                return; //debe parar aqui, ya a copia foi feita
            } else {
                throw new LanguageException(0, varName,
                        "O registo " + varName + " só pode recever valores do tipo " + pde.TextoOrigen,
                        "Mude a assinação");//David:Revisar
            }
        }

        ((Simbolo) pde).setValue(newValue);
    }
}
