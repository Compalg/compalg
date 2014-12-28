package Portugol.Language.Calcular;

import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.SymbolComposto;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;

public class FuncaoTexto extends AbstractCalculus {

    private static String functions1 = " COMPRIMENTO ";
    private static String functions2 = " LETRA ";
    //VERIFICA SE O PARAMETRO E UM ELEMENTO DE CALCULO

    public boolean IsValid(Object str) {
        if (!(str instanceof String)) {
            return false;
        }
        return functions1.indexOf(" " + ((String) str).toUpperCase() + " ") != -1
                || functions2.indexOf(" " + ((String) str).toUpperCase() + " ") != -1;

    }
    //--------------------------------------------------------------------------

    public int GetNumParameters(Object oper) throws Exception {
        if (!(oper instanceof String)) {
            throw new Exception("ERRO 013:\nFUNÇÂO DESCOHECIDA ");
        }

        if (functions1.indexOf(" " + ((String) oper).toUpperCase() + " ") != -1) {
            return 1;
        }
        if (functions2.indexOf(" " + ((String) oper).toUpperCase() + " ") != -1) {
            return 2;
        }
        throw new Exception("ERRO 013:\nNOS PARAMETROS DAS FUNCOES [" + (String) oper + "]");
    }
    //--------------------------------------------------------------------------

    public int GetPriority(Object oper) throws Exception {
        if (IsValid(oper)) {
            return AbstractCalculus.FUNCTION_PRIORITY;
        }
        throw new Exception("ERRO 013:\nNA PRIORIDADE DAS FUNCOES");

    }
    //---------------------------------------------------------------------------

    @Override
    public String Calculate(Object oper, Vector params) throws Exception {
        if (!(oper instanceof String)) {
            throw new Exception("ERRO 013:\nFUNÇÂO DESCOHECIDA ");
        }

        if (params.size() == 1) {
            return Calculate1((String) oper, params.get(0));
        }
        if (params.size() == 2) {
            return Calculate2((String) oper, params.get(0), params.get(1));
        }

        throw new Exception("ERRO 013:\nFUNCAO COM PARAMETROS ERRADOS [" + oper + "] "
                + "" + params.toString());
    }
    //--------------------------------------------------------------------------

    private String Calculate1(String oper, Object param) throws Exception {
        double val = 0;
        // param = "texto" => comprimento -2 porque " nao conta
        if (((String) oper).equalsIgnoreCase("COMPRIMENTO")) {
            if (param instanceof String) {
                val = ((String) param).length() - 2;
            } else if (param instanceof SymbolComposto) {
                throw new Exception("ERRO 013:\nOPERANDO NAO POSE SER REGISTO [" + ((SymbolComposto) param).getName() + "]");
            } else if (param instanceof SymbolArray) {
                throw new Exception("ERRO 013:\nOPERANDO NAO POSE SER VETOR [" + ((SymbolArray) param).getName() + "]");
            } else if (param instanceof Simbolo) {
                if (((Simbolo) param).getType() != Simbolo.TEXTO) {
                    throw new Exception("ERRO 013:\nOPERANDO NAO POSE SER DO TIPO [" + ((Simbolo) param).getTypeLexema() + "]");
                }
                val = ((String) ((Simbolo) param).getValue()).length(); //David: aqui nao é preciso "-2"
            }
        } else {
            throw new Exception("ERRO 013:\nFUNCOES DESCONHECIDAS [" + oper + "]");
        }
        return Values.IntegerToString(val);
    }
    //--------------------------------------------------------------------------

    private String Calculate2(String oper, Object param1, Object param2) throws Exception {
        if (((String) oper).equalsIgnoreCase("LETRA")) {
            int pos = 0;            
            String val = "";
            
            if (param2 instanceof String) {
                pos = Values.StringToInteger((String) param2) + 1;
            } else if (param2 instanceof SymbolComposto) {
                throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) param2).getName() + "]");
            } else if (param2 instanceof SymbolArray) {
                throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) param2).getName() + "]");
            } else if (param2 instanceof Simbolo) {
                if (((Simbolo) param2).getType() != Simbolo.INTEIRO) {
                    throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER DO TIPO [" + ((Simbolo) param2).getTypeLexema() + "]");
                }
                pos = ((Integer) ((Simbolo) param2).getValue()); //David: aqui nao é preciso "-2"
            } else {
                throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
            }
            
            if (param1 instanceof String) {
                val = (String) param1;
            } else if (param1 instanceof SymbolComposto) {
                throw new Exception("ERRO 013:\nOPERANDO NAO POSE SER REGISTO [" + ((SymbolComposto) param1).getName() + "]");
            } else if (param1 instanceof SymbolArray) {
                throw new Exception("ERRO 013:\nOPERANDO NAO POSE SER VETOR [" + ((SymbolArray) param1).getName() + "]");
            } else if (param1 instanceof Simbolo) {
                if (((Simbolo) param1).getType() != Simbolo.TEXTO) {
                    throw new Exception("ERRO 013:\nOPERANDO NAO POSE SER DO TIPO [" + ((Simbolo) param1).getTypeLexema() + "]");
                }
                val = ((String) ((Simbolo) param1).getValue()); //David: aqui nao é preciso "-2"
            } else {
                throw new Exception("ERRO 013:\nOPERANDO NAO É VALIDO ");
            }

            char ch = val.charAt(pos);
            return Values.StringToText("" + ch);
        } else {
            throw new Exception("ERRO 013:\nFUNCAO DESCONHECIDA [" + oper + "]");
        }
    }
}
