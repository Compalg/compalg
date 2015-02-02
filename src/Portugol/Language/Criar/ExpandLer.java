package Portugol.Language.Criar;

import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Analisador.ParteDeExpresion;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Utilitario.LanguageException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;

public class ExpandLer {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";

//-------------------------------------------------------------------------------------
//------------                                             ----------------------------
//------------    V A R I A V E I S                        ----------------------------
//------------                                             ----------------------------
//-------------------------------------------------------------------------------------
    public static NodeInstruction ExpandRead(NodeInstruction node, int level, Vector memory)
            throws LanguageException {
        String LER = Keyword.GetTextKey(Keyword.LEIA);
        String RECEBE = "RECEBA";

        NodeInstruction prevNode = null;
        //lugar para onde aponta node
        NodeInstruction originalNextNode = node.GetNext();
        //intrucao
        String instruction = node.GetText().trim();
        //verificar se a primeira palavra é leia
        String ler = instruction.substring(0, LER.length());
        String receber = instruction.substring(0, 6);
        if ((!ler.equalsIgnoreCase(LER)) && (!receber.equalsIgnoreCase(RECEBE))) {
            throw new LanguageException(
                    node.GetCharNum(), node.GetText(),
                    " Esperava um encontrar um LEIA ou RECEBA",
                    " VERIFICAR BEM A INSTRUÇÃO");
        }
        if (ler.equalsIgnoreCase(LER)) {
            instruction = instruction.substring(LER.length()).trim();
        } else if (receber.equalsIgnoreCase(RECEBE)) {
            instruction = instruction.substring(RECEBE.length()).trim();
        }

        instruction = (instruction.trim()).replace('(', ' '); //Retira os parenteses
        instruction = instruction.replace(')', ' ');


        //dividir as variaveis
        StringTokenizer tok = new StringTokenizer(instruction, ",");
        while (tok.hasMoreElements()) {
            String variavel = ((String) tok.nextElement()).trim();
            ParteDeExpresion v = Variavel.getVariable(variavel, memory);
            if (v == null || !(v instanceof Simbolo)) {
                throw new LanguageException(
                        node.GetCharNum(),
                        node.GetText(),
                        "A VARIÁVEL \"" + variavel + "\" NÃO FOI DECLARADA",
                        "DECLARE PRIMEIRO A VARIÁVEL \"" + variavel + "\" ANTES DE LER");
            }

            if (((Simbolo) v).isConstant()) {
                throw new LanguageException(
                        node.GetCharNum(),
                        node.GetText(),
                        variavel + " => É UMA CONSTANTE E NÃO PODE ALTERAR O SEU VALOR",
                        "DECLARE  \"" + variavel + "\" COMO VARIÁVEL ");
            }


            //texto da definiçao da variável
            String text = "LER " + variavel;
            if (prevNode == null) {
                node.SetText(text);
                prevNode = node;
            } else {
                NodeInstruction newNode = new NodeInstruction(node);
                newNode.SetText(text);
                newNode.SetPositionY(node.GetPositionY() + 1);
                prevNode.SetNext(newNode);
                prevNode = newNode;
            }

        }

        //fazer a ligação com o no da intruçao seguinte
        originalNextNode.SetPositionY(prevNode.GetPositionY() + 1);
        prevNode.SetNext(originalNextNode);
        //retornar o ultimo
        return prevNode;
//------------------------------------------------------------------------------
    }
}
