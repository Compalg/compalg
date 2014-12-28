package Editor.GUI.CodeDocument;

import Portugol.Language.Criar.NodeInstruction;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Utilitario.CodeLine;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class BeautifyCode {

    protected static boolean isComented = false;
    public static String TAB_SPACES = "    ";
    protected static String comments = "";

    public static String IndentCode(String program) {
        isComented = false;
        StringTokenizer st = new StringTokenizer(program, "\n\r");
        NodeInstruction node = null;
        String instruction;
        String line = "";
        StringBuffer newCode = new StringBuffer();
        int level = 0;

        while (st.hasMoreTokens()) {
            line = st.nextToken();
            instruction = NormalizeString(line);

            //instruction = CodeLine.GetNormalized(line);
            node = new NodeInstruction(instruction, 0, 0);
            //    JOptionPane.showMessageDialog(null,"node =  "+node.GetText()+" nodetipo =  "+node.GetType());
            if (node.IsNodeClose()) {
                level--;
            }
            //o case tem identacao 2
            if (node.GetType() == Keyword.FIMESCOLHE) {
                level--;
            }
            //o else vem para tras
            if (node.GetType() == Keyword.SENAO) {
                level--;
            }
            //os casos tem as intrucoes is frente
            if (node.GetType() == Keyword.CASO) {
                level--;
            }
            if (node.GetType() == Keyword.DEFEITO) {
                level--;
            }
            if (node.GetType() == Keyword.FIMCLASSE) {
                level--;
            }

            line = IdentLine(node.GetText() + comments, level);


            newCode.append(line + "\n");
            //regressar a posicao boa
            if (node.GetType() == Keyword.SENAO) {
                level++;
            }
            if (node.GetType() == Keyword.CLASSE) {
                level++;
            }

            //os casos tem as intrucoes em frente
            if (node.GetType() == Keyword.CASO) {
                level++;
            }
            if (node.GetType() == Keyword.DEFEITO) {
                level++;
            }

            //nos que abrem um ciclo
            if (node.IsNodeOpen()) {
                level++;
            }
            //o case tem identacao 2
            if (node.GetType() == Keyword.ESCOLHA) {
                level++;
            }
            if (node.GetType() == Keyword.FIMFUNCAO
                    || node.GetType() == Keyword.FIMPROCEDIMENTO
                    || node.GetType() == Keyword.FIMREGISTO
                    || node.GetType() == Keyword.FIMCONSTRUTOR
                    || node.GetType() == Keyword.FIMCLASSE) {
                newCode.append("\n");
            }
        }
        newCode.append("\n");
        return newCode.toString();

    }

    private static String IdentLine(String line, int level) {
        StringBuffer tmp = new StringBuffer();
        String tab = "";
        for (int i = 0; i < level; i++) {
            tmp.append(TAB_SPACES);
        }
        tmp.append(line.trim());
        return tmp.toString();
    }

    public static String NormalizeString(String str) {
        comments = "";
        StringBuffer newStr = new StringBuffer();
        for (int index = 0; index < str.length(); index++) {
            switch (str.charAt(index)) {
                case '/':
                    // comentario "//"
                    if (index + 1 < str.length() && str.charAt(index + 1) == '/') {
                        comments = str.substring(index);
                        return newStr.toString().trim();
                    }
                    //inicio de um comentario /*
                    if (index + 1 < str.length() && str.charAt(index + 1) == '*') {
                        comments += "/";
                        isComented = true;
                    } else if (index > 0 && str.charAt(index - 1) == '*') {
                        comments += "/";
                        isComented = false;
                    } else {
                        newStr.append(str.charAt(index));
                    }
                    break;
                default:
                    // se nao for comentario
                    if (!isComented) {
                        newStr.append(str.charAt(index));
                    } else {
                        comments += str.charAt(index);
                    }
            }
        }
        return newStr.toString().trim();
    }
}
