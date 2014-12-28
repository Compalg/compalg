package Portugol.Language.Calcular;

import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SymbolArray;
import Portugol.Language.Analisador.SymbolComposto;
import Portugol.Language.Analisador.SymbolObjeto;
import Portugol.Language.Utilitario.Values;
import java.util.Vector;

public class Logico extends AbstractCalculus {

    private static String logics1 = " NAO ";
    private static String logics2 = " E OU XOU ";

    public String GetSymbols() {
        return logics1 + logics2;
    }

    // verifica se é um operador lógico
    public boolean IsValid(Object str) {
        if (!(str instanceof String)) {
            return false;
        }
        return logics1.indexOf(" " + ((String) str).toUpperCase() + " ") != -1
                || logics2.indexOf(" " + ((String) str).toUpperCase() + " ") != -1;
    }

    // calcula o número de parámetros
    public int GetNumParameters(Object str) throws Exception {
        if (!(str instanceof String)) {
            throw new Exception("ERRO \nFUNÇÂO DESCOHECIDA ");
        }
        if (logics1.indexOf(" " + ((String) str).toUpperCase() + " ") != -1) {
            return 1;
        }
        if (logics2.indexOf(" " + ((String) str).toUpperCase() + " ") != -1) {
            return 2;
        }
        throw new Exception("ERRO nos parametros do Logico [" + str + "]");
    }

    //--------------------------------------------------------------------------
    // prioridade do operador
    public int GetPriority(Object oper) throws Exception {
        if (!(oper instanceof String)) {
            throw new Exception("ERRO \nOPERADOR DESCOHECIDO ");
        }
        if (((String) oper).equalsIgnoreCase("OU")) {
            return AbstractCalculus.LOGIC_PRIORITY + 1;
        }
        if (((String) oper).equalsIgnoreCase("XOU")) {
            return AbstractCalculus.LOGIC_PRIORITY + 1;
        }
        if (((String) oper).equalsIgnoreCase("E")) {
            return AbstractCalculus.LOGIC_PRIORITY + 2;
        }
        if (((String) oper).equalsIgnoreCase("NAO")) {
            return AbstractCalculus.LOGIC_PRIORITY + 3;
        }

        throw new Exception("ERRO na prioridade do Logico [" + (String) oper + "]");

    }

    //--------------------------------------------------------------------------
    public String Calculate(Object oper, Vector params) throws Exception {
        if (!(oper instanceof String)) {
            throw new Exception("ERRO \nOPERADOR DESCOHECIDO ");
        }
        if (params.size() == 1) {
            return CalculateLogic1((String) oper, params.get(0));
        }
        if (params.size() == 2) {
            return CalculateLogic2((String) oper, params.get(0), params.get(1));
        }

        throw new Exception("ERRO 010:\nPARAMETROS LOGICO ERRADO [" + oper + "] " + params.toString());

    }

    private String CalculateLogic2(String oper, Object str1, Object str2) throws Exception {
        boolean n1 = false;
        boolean n2 = false;
        boolean val = false;
        if (str1 instanceof String) {
            n1 = Values.StringToBoolean((String) str1);
        } else if (str1 instanceof SymbolComposto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) str1).getName() + "]");
        } else if (str1 instanceof SymbolObjeto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER OBJETO [" + ((SymbolObjeto) str1).getName() + "]");
        } else if (str1 instanceof SymbolArray) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) str1).getName() + "]");
        } else if (str1 instanceof Simbolo) {
            if (((Simbolo) str1).getType() != Simbolo.LOGICO) {
                throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER DO TIPO [" + ((Simbolo) str1).getTypeLexema() + "]");
            }
            n1 = Values.StringToBoolean((String) ((Simbolo) str1).getValue());
        } else {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
        }

        if (str2 instanceof String) {
            n1 = Values.StringToBoolean((String) str2);
        } else if (str2 instanceof SymbolComposto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) str2).getName() + "]");
        } else if (str2 instanceof SymbolObjeto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER OBJETO [" + ((SymbolObjeto) str2).getName() + "]");
        } else if (str2 instanceof SymbolArray) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) str2).getName() + "]");
        } else if (str2 instanceof Simbolo) {
            if (((Simbolo) str2).getType() != Simbolo.LOGICO) {
                throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER DO TIPO [" + ((Simbolo) str2).getTypeLexema() + "]");
            }
            n2 = Values.StringToBoolean((String) ((Simbolo) str2).getValue());
        } else {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
        }


        if (oper.equalsIgnoreCase("E")) {
            val = n1 && n2;
        } else if (oper.equalsIgnoreCase("OU")) {
            val = n1 || n2;
        } else if (oper.equalsIgnoreCase("XOU")) {
            val = n1 != n2;
        } else {
            throw new Exception("ERRO 010:\nOPERADOR LOGICO DESCONHECIDO [" + oper + "]");
        }
        return Values.BooleanToString(val);
    }
//---------------------------------------------------------------------------

    private String CalculateLogic1(String oper, Object str1) throws Exception {
        boolean n1 = false;
        if (str1 instanceof String) {
            n1 = Values.StringToBoolean((String) str1);
        } else if (str1 instanceof SymbolComposto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER REGISTO [" + ((SymbolComposto) str1).getName() + "]");
        } else if (str1 instanceof SymbolObjeto) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER OBJETO [" + ((SymbolObjeto) str1).getName() + "]");
        } else if (str1 instanceof SymbolArray) {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER VETOR [" + ((SymbolArray) str1).getName() + "]");
        } else if (str1 instanceof Simbolo) {
            if (((Simbolo) str1).getType() != Simbolo.LOGICO) {
                throw new Exception("ERRO 013:\nA POSIÇAO NAO POSE SER DO TIPO [" + ((Simbolo) str1).getTypeLexema() + "]");
            }
            n1 = Values.StringToBoolean((String) ((Simbolo) str1).getValue());
        } else {
            throw new Exception("ERRO 013:\nA POSIÇAO NAO É VALIDO ");
        }

        boolean val = false;
        if (oper.equalsIgnoreCase("NAO")) {
            val = !n1;
        } else {
            throw new Exception("ERRO 010:\nOPERADOR LOGICO UNARIO DESCONHECIDO [" + oper + "]");
        }
        return Values.BooleanToString(val);

    }
}
