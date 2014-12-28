package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.ParteDeExpresion;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;

public class ExpandPara {

//-------------------------------------------------------------------------------------
//------------                                             ----------------------------
//------------    I N S T R U C A O    P A R A            ----------------------------
//------------                                              ---------------------------
//-------------------------------------------------------------------------------------
    /**
     * expande o ciclo
     *
     * @param forNode nodo de inicio do ciclo
     * @param level nivel
     * @param memory vector de memoria
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    public static void ExpandFOR(NodeInstruction forNode, int level, Vector memory) throws LanguageException {
        String PARA = Keyword.GetTextKey(Keyword.PARA);
        String DE = " " + Keyword.GetTextKey(Keyword.DE) + " ";
        String ATE = " " + Keyword.GetTextKey(Keyword.ATE) + " ";
        String PASSO = " " + Keyword.GetTextKey(Keyword.PASSO) + " ";

        String exp = forNode.GetText().toUpperCase().trim();
        //--------------------------------------------------------------------------------
        // -------------------- extrair a variavel----------------------------------
        //--------------------------------------------------------------------------------
        int endVar = exp.indexOf(DE);
        if (endVar < 0) {
            throw new LanguageException(
                    forNode.GetCharNum(), forNode.GetText(),
                    "Ciclo PARA sem DE",
                    "Escreva PARA variavel <DE> inicio ATE fim PASSO p");
        }

        String variable = forNode.GetText().substring(PARA.length(), endVar).trim();
        //extrair a variavel da memoria
        ParteDeExpresion var = Variavel.getVariable(variable, memory);
        if (var == null || !(var instanceof Simbolo)) {
            throw new LanguageException(
                    forNode.GetCharNum(), forNode.GetText(),
                    "\"" + variable + "\" não esta definida",
                    "Defina a variavel numerica \"" + variable + "\" antes deste ciclo ");
        }
        if (((Simbolo) var).isConstant()) {
            throw new LanguageException(
                    forNode.GetCharNum(), forNode.GetText(),
                    "\"" + variable + "\" é uma constante ",
                    "Altere o tipo de \"" + variable + "\" de constante para variavel ");
        }

        if (!((Simbolo) var).isNumber()) {
            throw new LanguageException(
                    forNode.GetCharNum(), forNode.GetText(),
                    "\"" + variable + "\" não é numérica",
                    "Altere o tipo de \"" + variable + "\" para real ou inteiro ");
        }
        //--------------------------------------------------------------------------------
        // -------------------- extrair o valor de inicializacao --------------------------
        //--------------------------------------------------------------------------------
        int endValue = exp.indexOf(ATE);
        if (endValue < 0) {
            throw new LanguageException(
                    forNode.GetCharNum(), forNode.GetText(),
                    "Ciclo PARA sem ATE",
                    "Escreva PARA variável DE inicio <ATE> fim PASSO p");
        }

        String value = forNode.GetText().substring(endVar + DE.length(), endValue).trim();
        //--------------------------------------------------------------------------------
        // -------------------- extrair o Passo e LIMITE --------------------------
        //--------------------------------------------------------------------------------
        int begPasso = exp.indexOf(PASSO);
        String limite;
        String passo = new String("1");
        //se nao houver passo leio apenas o limite
        if (begPasso == -1) {
            limite = forNode.GetText().substring(endValue + ATE.length()).trim();
        } // ler limite e passo
        else {
            limite = forNode.GetText().substring(endValue + ATE.length(), begPasso).trim();
            passo = forNode.GetText().substring(begPasso + PASSO.length()).trim();
        }
        //--------------------------------------------------------------------------------
        //--------------------------- VERIFICAR se sao expressoes numericas ---------------
        //--------------------------------------------------------------------------------
        int type = Expressao.TypeExpression(value, memory);
        if (type != Simbolo.INTEIRO && type != Simbolo.REAL) {
            throw new LanguageException(
                    forNode.GetCharNum(), forNode.GetText(),
                    "\"" + value + "\" não é uma expressão numérica",
                    "Verifique se a expressão está bem escrita");
        }

        type = Expressao.TypeExpression(passo, memory);
        if (type != Simbolo.INTEIRO && type != Simbolo.REAL) {
            throw new LanguageException(
                    forNode.GetCharNum(), forNode.GetText(),
                    "\"" + passo + "\" nao é uma expressão numérica",
                    "Verifique se a expressão está bem escrita");
        }

        type = Expressao.TypeExpression(limite, memory);
        if (type != Simbolo.INTEIRO && type != Simbolo.REAL) {
            throw new LanguageException(
                    forNode.GetCharNum(), forNode.GetText(),
                    "\"" + limite + "\" não é uma expressão numérica",
                    "Verifique se a expressão está bem escrita");
        }


        forNode.SetText(variable + " " + Keyword.ATRIBUI + " " + value);
        forNode.SetType(Keyword.CALCULAR);
        forNode.SetLevel(level);
        //--------------------------------------------------------------------------------
        //------------------ fazer o no da actualizaçao da variavel ----------------------
        //--------------------------------------------------------------------------------
        NodeInstruction passNode = new NodeInstruction(passo, forNode.GetCharNum(), level);
        passNode.SetType(Keyword.PASSO);
        passNode.SetLevel(level);
        //--------------------------------------------------------------------------------
        //------------------ fazer o no da condicao                 ----------------------
        //--------------------------------------------------------------------------------
        NodeInstruction condic = new NodeInstruction(variable + " <= " + limite, forNode.GetCharNum(), level);
        condic.SetType(Keyword.ENQUANTO);
        condic.SetLevel(level);
        //--------------------------------------------------------------------------------
        //------------------ fazer as ligaçoes                     ----------------------
        //--------------------------------------------------------------------------------        
        NodeInstruction tmp = forNode.GetNext();
        //---------------- ligar o init e a condic -------------
        forNode.SetNext(passNode);
        passNode.SetNext(condic);
        //-------- ligar a condic e o bloco ------
        condic.SetIfTrue(tmp);
        while (tmp != null && tmp.GetType() != Keyword.FIMPARA
                && tmp.GetNext() != null && tmp.GetNext().GetType() != Keyword.FIMPARA) {
            tmp.SetLevel(level + 1);
            tmp = tmp.GetNext();
        }
        NodeInstruction endFor = tmp.GetType() == Keyword.FIMPARA ? tmp : tmp.GetNext();

        //------- no antes do proximo ------
        if (tmp.GetType() != Keyword.FIMPARA) {
            tmp.SetLevel(level + 1);
            //-------- fazer o no de actualizaçao da var  a var -------
            NodeInstruction actualize = new NodeInstruction(variable + " " + Keyword.ATRIBUI + "  " + variable + " + " + passo, forNode.GetCharNum(), level + 1);
            actualize.SetLevel(level + 1);
            actualize.SetType(Keyword.CALCULAR);
            //---------fim do for-------------------
            // ligar actualize a condic
            actualize.SetNext(condic);
            //--------------- ligar o corpo do ciclo a actualize -------
            //no caso de ser um enquanto o terminar ligar o falso
            if (tmp.GetIfFalse() != null && tmp.GetIfFalse().GetType() == Keyword.FIMPARA) {
                tmp.SetIfFalse(actualize);
            }
            //no caso de ser um Repeat o terminar ligar o verdadeiro
            if (tmp.GetIfTrue() != null && tmp.GetIfTrue().GetType() == Keyword.FIMPARA) {
                tmp.SetIfTrue(actualize);
            }
            // ligar o next do tmp
            tmp.SetNext(actualize);
        }
        //ligar o true de condic
        condic.SetIfFalse(endFor);
        //modificar o endFor para JOIN
        endFor.SetType(Keyword.CONECTOR);
        //nivel do endfor
        endFor.SetLevel(level);
        //instrução seguinte ao ciclo
        condic.SetNext(endFor);
        //-------------------------
    }
}
