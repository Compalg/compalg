package Portugol.Language.Criar;

import Portugol.Language.Analisador.Expressao;
import Portugol.Language.Analisador.ParteDeExpresion;
import Portugol.Language.Analisador.Simbolo;
import Portugol.Language.Analisador.SimboloDeParametro;
import Portugol.Language.Analisador.TipoDeParametro;
import Portugol.Language.Analisador.Variavel;
import Portugol.Language.Utilitario.LanguageException;
import java.util.Vector;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public class ExpandDefinirVariavel {
    //---------------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------

    public static NodeInstruction Define(String alter, String type, String name, String value, NodeInstruction node, Vector memory)
            throws LanguageException {

        //verificar se is um nome aceitavel
        if (!Variavel.isNameAcceptable(name)) {
            throw new LanguageException(
                    node.GetCharNum(), node.GetText(),
                    " SIMBOLO " + name + " INVÁLIDO :" + Variavel.getErrorName(name),
                    "ALTERE O NOME DO SIMBOLO " + name);
        }

        //verificar se a variavel ja esta definida
        //David:
        ParteDeExpresion tmpVar = Variavel.getVariable(name, memory);
        if (tmpVar != null && tmpVar instanceof Simbolo/*&& tmpVar.getLevel() == node.GetLevel()*/) //David: quitado lo de posible repeticion de variables para niveles distintos
        {
            throw new LanguageException(
                    node.GetCharNum(), node.GetText(),
                    "O SIMBOLO <" + name + "> JÁ FOI DECLARADO",
                    " MUDE DE NOME <" + name + "> .");
        }

        //verificar a expressao
        try {
            if (!Expressao.IsExpression(value, memory)) {
                throw new LanguageException(
                        node.GetCharNum(), node.GetText(),
                        "O ELEMENTO \"" + Expressao.ErrorExpression(value, memory) + "\" NÃO É UMA EXPRESSÃO VÁLIDA",
                        " VERIFIQUE A EXPRESSÃO");
            }
        } catch (Exception e) {
            if (e instanceof LanguageException) {
                if (((LanguageException)e).line > 0 && !((LanguageException)e).codeLine.isEmpty()) {
                    throw e;
                }
                throw new LanguageException(
                        node.GetCharNum(), node.GetText(),
                        ((LanguageException)e).error, ((LanguageException)e).solution);
            } else {
                throw new LanguageException(
                        node.GetCharNum(),
                        node.GetText(),
                        "O ELEMENTO \"" + Expressao.ErrorExpression(value, memory) + "\" NÃO É UMA EXPRESSÃO VÁLIDA",
                        " VERIFIQUE A EXPRESSÃO");
            }
        }
        //Avaliar a expressao
        // se nao for possivel avaliar provoca erro
        Object memValue;
        try {
            memValue = Expressao.Evaluate(value, memory, true);
        } catch (Exception e) {
            throw new LanguageException(
                    node.GetCharNum(), node.GetText(),
                    e.toString(),
                    "VERIFIQUE A EXPRESSÃO <" + value + ">");
        }

        //verificar se o resultado da expressao e compativel com a variavel
        if (!Simbolo.IsCompatible(type, memValue)) {
            throw new LanguageException(
                    node.GetCharNum(), node.GetText(),
                    "O VALOR <" + value + "> NÃO É PERMITIDO PARA UMA VARIÁVEL " + type,
                    " VERIFIQUE A EXPRESSÃO :" + value);
        }

        //normalizar o texto da expressao
        String text = alter + " " + type + " " + name + " <- " + value;
        // fazer um novo no
        NodeInstruction newNode = new NodeInstruction(node);
        newNode.SetText(text);
        Variavel.defineVAR(newNode, memory, new Vector<TipoDeParametro>(), new Vector<SimboloDeParametro>());
        return newNode;
    }
}
