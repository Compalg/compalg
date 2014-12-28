package Portugol.Language.Criar;

import Editor.Utils.FileManager;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Consola.ConsoleIO;
import java.util.StringTokenizer;
import java.util.Vector;
import Portugol.Language.Utilitario.LanguageException;

/**
 * @author Augusto Bilabila original de Antonio manso
 */
public class Intermediario {

    public static String VERSION = "Versão:1.0 \t(c) Augusto Bilabila";
    static public Vector<Bloque> subrutinas;
    static public Vector tiposRegistos = new Vector();
    private BloqueSubrutine Inicio;

    public static ConsoleIO console;
    public BloqueSubrutine getInicio() {
        return Inicio;
    }

    /**
     * Constroi um fluxograma
     *
     * @param code programa fonte
     * @throws Portugol.Language.Utils.LanguageException excepcao
     */
    public Intermediario(String code) throws LanguageException {        
        Construir(code);
    }

    /**
     * constroi um fluxograma com um ficheiro de programa
     *
     * @param filename no do ficheiro
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    public void ReadFile(String filename) throws LanguageException {
        FileManager f = new FileManager();
        Construir(f.ReadFile(filename));
    }

    /**
     * Controi o fluxograma
     *
     * @param programa Texto do programa
     * @throws Portugol.Language.Utils.LanguageException erro
     */
    protected void Construir(String programa) throws LanguageException {

        subrutinas = new Vector<Bloque>();
        tiposRegistos = new Vector();
        
        int charNum = 0;
        int positionY = 0;

        Bloque rutina = null;
        String instruction;

        //System.out.print("programa \n" + programa);
        // as intrucoes terminam com enter
        StringTokenizer st = new StringTokenizer(programa, "\n\r");

        //no anterior (para fazer a ligacao)
        NodeInstruction previousNode = null;
        // novo no
        NodeInstruction newNode = null;

        //fazer a lista seguida
        
        while (st.hasMoreTokens()) {

            //retirar os espacos
            instruction = st.nextToken();
            //contar o numero de caracteres
            charNum += instruction.length() + 1; // terminador
            //retirar espacos e comentarios
            instruction = RemoveComentarios(instruction);

            //se for uma linha vazia
            if (instruction.length() == 0) {
                continue;
            }

            newNode = new NodeInstruction(instruction, charNum - 1, 0);
            newNode.SetPositionY(++positionY);

            if (newNode.GetType() == Keyword.DEFINIR) {
                if (!newNode.GetText().contains(",")) {
                    if (newNode.GetText().startsWith("literal ") && !newNode.GetText().endsWith("\"")) {
                        newNode.SetText(newNode.GetText() + "<-\"\"");
                    }
                }
            }

            switch (newNode.GetType()) {
                case Keyword.INICIO:
                case Keyword.PROCEDIMENTO:
                case Keyword.FUNCAO:
                    if (rutina != null)
                        throw new LanguageException(
                                "AINDA EM OTRO BLOCO DE CODIGO", "DEBE ENCERRAR O BLOCO"); //David: Correguir ortografia
                    rutina = new BloqueSubrutine();
                    rutina.start = newNode;
                    previousNode = rutina.start;
                    rutina.Nome = instruction;

                    if ((newNode.GetType() == Keyword.INICIO)) {//David: debe ser igual para que arranque por INICIO
                        Inicio = (BloqueSubrutine) rutina;
                    } else {
                        subrutinas.add(rutina);
                    }

                    break;
                case Keyword.REGISTO:
                    if (rutina != null)
                        throw new LanguageException(
                                "AINDA EM OTRO BLOCO DE CODIGO", "DEBE ENCERRAR O BLOCO"); //David: Correguir ortografia
                    
                    rutina = new BloqueRegisto();
                    rutina.start = newNode;
                    previousNode = rutina.start;
                    rutina.Nome = instruction;
                    break;
                    
                case Keyword.FIM:
                case Keyword.FIMPROCEDIMENTO:
                case Keyword.FIMFUNCAO:
                    previousNode.SetNext(newNode);
                    previousNode = newNode;

                    ExpandFluxogram.ExpandSubrutine((BloqueSubrutine)rutina);
                    rutina = null;
                    break;

                case Keyword.FIMREGISTO:
                    previousNode.SetNext(newNode);
                    previousNode = newNode;

                    ExpandFluxogram.ExpandRegisto((BloqueRegisto) rutina);
                    rutina = null;
                    break;
                default:
                    previousNode.SetNext(newNode);
                    previousNode = newNode;
                    break;
            }
        }//fim da lista seguida
    }

//=============================================================================
    public String RemoveComentarios(String str) {
        StringBuffer newStr = new StringBuffer();
        boolean isComented = false;//David: Ojo, ver impacto de traer esta variable para aqui, desde Subrutine 
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
}