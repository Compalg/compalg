package Editor.GUI;

import Conversor.*;

import Editor.GUI.Sobre.AboutThis;
import Editor.GUI.Sobre.PortugolInfo;
import Editor.GUI.CodeDocument.*;
import Editor.GUI.Dialogo.Message;

import Editor.Utils.*;
import Editor.help.HelpFileName;
import Editor.help.WWWHelpText;
import Portugol.Language.Consola.ConsoleIO;
import Portugol.Language.Criar.Intermediario;
import Portugol.Language.Criar.NodeInstruction;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Criar.BloqueSubrutine;
import Portugol.Language.Utilitario.LanguageException;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.Element;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.UIManager;

/**
 * @author Augusto Bilabila(2011-2012)
 */
public class EditorCAlg extends javax.swing.JFrame implements Runnable {

    public static String TITLE = "Compilador de Algoritmos";
    public static String DATE = "11-03-2014";
    public static String VERSION = "Versão:1.2 \t(c)Augusto Bilabila";
    //texto modificado
    private boolean textChanged = false;
    private static JFrame frameBusca = null;
    private static JFrame frameLine = null;
    private static JTextField texto_busca;
    private static JRadioButton desdeInicio, desdeAgora;
    private static JButton botao_buscaProximo;
    private static boolean verificaDesdeInicio = false;
    private static String previous = "";
    // Variavel que sera acedida no envio de email
    public static String codigo = "";
    //  MetodoBusca busca = new MetodoBusca();
    public ConsoleIO console;
    private ConsoleIO info;
    // gestor de ficheiros
    public FileManager fileManager;
    private ExemploAlgoritmo fileManager2;
    // propriedades do editor
    private EditorProperties editorProperties;
    private AlgoSyntaxHighlight txtCode;
    //ajuda da linguagem
    private WWWHelpText helpLang;
    // undo manager
    private MyUndoRedoManager urManager;
    //public RedoAction redoAction;
    //---------------------------- EXecucao do programa ---------
    private Thread autoExecute = null;
    private NodeInstruction instruction;
    private BloqueSubrutine prog;
    private Intermediario intermediario;
    //-----------------------------------------------------------
    private UIManager.LookAndFeelInfo[] look;
    private String[] lookNome;
    private JMenu inserir = new JMenu("Auxílio de código");
    private JMenu tipos = new JMenu("Tipo de dado");
    private JMenu ed = new JMenu("Estrutura de dado");
    private JMenu EC = new JMenu("Estrutura de controlo");
    private JMenu ES = new JMenu("Estrutura de controlo");
    private JMenu Edecisao = new JMenu("Decisão");
    private JMenuItem se = new JMenuItem("Se");
    private JMenuItem escolha = new JMenuItem("Escolha");
    private JMenu Erepeticao = new JMenu("Repetição (Ciclos)");
    private JMenuItem para = new JMenuItem("Para");
    private JMenuItem enquanto = new JMenuItem("Enquanto");
    private JMenuItem faca = new JMenuItem("Faça");
    private JMenuItem repita = new JMenuItem("Repita");
    private JMenu MT = new JMenu("Métodos");
    private JMenuItem proc = new JMenuItem("Procedimento");
    private JMenuItem func = new JMenuItem("Função");
    private JMenuItem atrib = new JMenuItem("Atribuição");
    private JMenuItem nlinha = new JMenuItem("Mudar de linha");
    private JMenuItem tabul = new JMenuItem("Tabulação");
    private JMenuItem selecionar, copiar, colar, cortar, ajuda, propriedade = new JMenuItem("Propriedade");
    private JMenuItem escreva = new JMenuItem("Escreva");
    private JMenuItem leia = new JMenuItem("Leia");
    private JMenuItem mostre = new JMenuItem("Mostre");
    private JMenuItem inteiro = new JMenuItem("Inteiro");
    private JMenuItem real = new JMenuItem("Real");
    private JMenuItem logico = new JMenuItem("Logico");
    private JMenuItem literal = new JMenuItem("Literal");
    private JMenuItem caracter = new JMenuItem("Caracter");
    private JMenuItem registo = new JMenuItem("Registo");
    private JMenu matriz = new JMenu("Matriz");
    private JMenuItem matrizU = new JMenuItem("Uni-dimencional");
    private JMenuItem matrizB = new JMenuItem("Bi-dimencional");
    private JMenuItem matrizM = new JMenuItem("Multi-dimencional");
    private JMenuItem exemplo1 = new JMenuItem("Exemplo de Algoritmos");
    construirProgramaJava progJava;
    construirProgramaC progC;
    Calendario calend;
    DicasdoDia dica;
    private String descAlgo = "";
    FormPropriedadeAlgol objeto_propriedade;

    public EditorCAlg() {
        initComponents();
        initMyComponents();
        // ler o ficheiro por defeito
        this.LerFicheiro(fileManager.getFileName());

    }

    public EditorCAlg(String fileName) {
        initComponents();
        initMyComponents();
        // ler o ficheiro por defeito
        this.LerFicheiro(fileName);

    }

    private void initMyComponents() {
        //maximizar a janela
        this.setExtendedState(MAXIMIZED_BOTH);
        //---------------- consola ------------------------
        console = new ConsoleIO();
        scrollMonitor.add(console);
        scrollMonitor.setViewportView(console);
        //--------------------------------------------------
        fileManager = new FileManager();
        fileManager2 = new ExemploAlgoritmo();

        //----------- Propriedades do Editor --------------------
        SetEditorProperties();
        //------------------------  UNDO MANAGER  ---------??????????????????
        urManager = new MyUndoRedoManager();
        TextPaneCode.getDocument().addUndoableEditListener(urManager);

        obj_RA = new Botoes_nav(); // classe com os metodos de avançar e retroceder
        obj_RA.tempo.start();
        //-------------------------------------------------------------------------
        //----------------------------------------
        info.setForeground(Color.BLUE);
        info.write(PortugolInfo.getInformation());

        //---------------------------------------
        MenuEditorCheckSyntax.setSelected(true);
        String txt = TextPaneCode.getText();
        if (MenuEditorCheckSyntax.isSelected()) {
            txtCode = new AlgoCodeStyle();
        } else {
            txtCode = new AlgoCodeBlank();
        }
        editorProperties.SetProperty("sintax", MenuEditorCheckSyntax.isSelected() + "");
        TextPaneCode.setStyledDocument(txtCode); // colorir sintaxe
        TextPaneCode.setText(txt);
        txtCode.clearTextBackground();

        MenuEditorCheckSyntax.setVisible(false);
        menuMudarConsola.setVisible(false);
        //---------------------------------------

        data_hora();

        TextPaneCode.setText(descAlgo);


        TextPaneCode.addMouseListener(
                new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    jPopupMenu1.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });

        // cria o JPopupMenu
        jPopupMenu1 = new JPopupMenu();

        selecionar = new JMenuItem("Selecionar Tudo");
        selecionar.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                TextPaneCode.requestFocus();
                TextPaneCode.selectAll();
            }
        });

        atrib.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe(" <- ");
            }
        });
        //----------------------------------------------------------------------- 
        nlinha.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe(" \\n ");
            }
        });

        //----------------------------------------------------------------------- 
        tabul.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("\\t");
            }
        });
        //---------------------------------------------------------------------
        copiar = new JMenuItem("Copiar");
        copiar.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                TextPaneCode.copy();
            }
        });
        //---------------------------------------------------------------------
        colar = new JMenuItem("Colar");
        colar.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                TextPaneCode.paste();
            }
        });
        //---------------------------------------------------------------------
        cortar = new JMenuItem("Cortar");
        cortar.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                TextPaneCode.cut();
            }
        });

        //---------------------------------------------------------------------
        cortar = new JMenuItem("Cortar");
        cortar.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                TextPaneCode.cut();
            }
        });

        //---------------------------------------------------------------------
        escreva.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("escreva ( \" \" )");
            }
        });

        //---------------------------------------------------------------------
        leia.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("leia variavel");
            }
        });

        //---------------------------------------------------------------------
        mostre.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("mostre (\" \")");
            }
        });

        jButton3.setVisible(false);
        //-----------------------TIPOS-----------------------------------------
        inteiro.addActionListener(
                new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("inteiro varI1, varI2");
            }
        });
        //---------------------------------------------------------------------
        real.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("real varR1, varR2");
            }
        });
        //---------------------------------------------------------------------
        logico.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("logico varL1, varL2");
            }
        });

        //---------------------------------------------------------------------
        literal.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("literal varL <- \"\"");
            }
        });

        caracter.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("caracter varC");
            }
        });
        //---------------------------------------------------------------------
        registo.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("registo nome_registo\n //variaveis\nfimregisto");
            }
        });

        //---------------------------------------------------------------------
        matrizU.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("tipo_dado nome_vector[dimensao] ");
            }
        });
        //---------------------------------------------------------------------
        matrizB.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("tipo_dado nome_matriz[linhas][colunas] ");
            }
        });

        //---------------------------------------------------------------------
        matrizM.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("tipo_dado nome_matriz[dimensao1][dimensao2][dimensao3][dimensao4][dimensaoN] ");
            }
        });

        //========================= E.C ============================================
        se.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("\tse ( condição ) entao\n"
                        + "\t//instrucoes caso a condição seja verdadeira\n"
                        + "\tsenao\n"
                        + "\t//instrucoes caso a condição seja falsa\n"
                        + "\tfimse ");
            }
        });
        //-------------------------------------------------------------------------
        escolha.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("\tescolha ( expressão )\n"
                        + "\t caso expressão1:\n"
                        + "\t //comando1\n"
                        + "\t caso expressão2:\n"
                        + "\t //comando2\n"
                        + "\t caso expressão3:\n"
                        + "\t //comando3\n"
                        + "\t caso expressãoN:\n"
                        + "\t //comandoN\n"
                        + "\t defeito:\n"
                        + "\t //comando executado quando a expressão avaliada não se verifica nos casos acima\n"
                        + "\tfimescolha ");
            }
        });
        //-------------------------------------------------------------------------
        para.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe("\tinteiro var \n"
                        + "\tpara var de inic ate fim passo inc|dec\n"
                        + "\t //comando\n"
                        + "\tfimpara ");
            }
        });
        //-------------------------------------------------------------------------
        enquanto.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe(""
                        + "\tenquanto ( condição ) faça\n"
                        + "\t //comando\n"
                        + "\tfimenquanto ");
            }
        });
        //-------------------------------------------------------------------------
        faca.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe(""
                        + "\tfaça \n"
                        + "\t //comando, executado enquanto a condição for verdadeira\n"
                        + "\tenquanto ( condição )");
            }
        });
        //----------------------------------------------------------------------
        repita.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe(""
                        + "\trepita \n"
                        + "\t //comando, executado sempre que a condição for falsa\n"
                        + "\tate ( condição )");
            }
        });

        //----------------------------------------------------------------------
        proc.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe(""
                        + "procedimento nome ( tipo args ) \n"
                        + "     //comando\n"
                        + "fimprocedimento");
            }
        });
        //----------------------------------------------------------------------
        func.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                insereSintaxe(""
                        + "funcao tipo nome ( tipo args ) \n"
                        + "     //comando\n"
                        + "fimfuncao");
            }
        });
        //----------------------------------------------------------------------      
        exemplo1.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                menuExemploAlgoritmoActionPerformed(null);
            }
        });
        //----------------------------------------------------------------------      
        propriedade.addActionListener(
                new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                FormPropriedadeAlgol dialog = new FormPropriedadeAlgol(new javax.swing.JFrame(), true, fileManager.getFileName(), TextPaneCode.getHeight());
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        //System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
        //----------------------------------------------------------------------

        jPopupMenu1.add(selecionar);
        jPopupMenu1.addSeparator();
        jPopupMenu1.add(copiar);
        jPopupMenu1.add(colar);
        jPopupMenu1.add(cortar);

        jPopupMenu1.addSeparator();

        Edecisao.add(se);
        Edecisao.add(escolha);
        Erepeticao.add(para);
        Erepeticao.add(enquanto);
        Erepeticao.add(faca);
        Erepeticao.add(repita);

        EC.add(Edecisao);
        EC.add(Erepeticao);

        MT.add(proc);
        MT.add(func);

        inserir.add(atrib);
        inserir.add(escreva);
        inserir.add(leia);
        inserir.add(mostre);
        inserir.add(nlinha);
        inserir.add(tabul);
        tipos.add(inteiro);
        tipos.add(real);
        tipos.add(logico);
        tipos.add(caracter);
        tipos.add(literal);
        inserir.add(tipos); // adiciona o menu com os tipos primitivos

        ed.add(matriz);
        ed.add(registo);
        matriz.add(matrizU);
        matriz.add(matrizB);
        matriz.add(matrizM);
        inserir.add(ed); // adiciona o menu de Estrutura de dados

        inserir.add(EC); // adiciona o menu de Estrutura de controlo

        inserir.add(MT);

        jPopupMenu1.add(inserir);
        jPopupMenu1.addSeparator();
        jPopupMenu1.add(exemplo1);
        jPopupMenu1.addSeparator();
        jPopupMenu1.add(propriedade);

        // fim menu

        // coloca uma figura na barra de título da janela  
        URL url = this.getClass().getResource("logoCompAlg48.png");
        Image imagemTitulo = Toolkit.getDefaultToolkit().getImage(url);
        this.setIconImage(imagemTitulo);

        // Aparencia por defeito
        aparencias(3);
    }

    public void insereSintaxe(String sintaxe) {

        int posicaoAtual = TextPaneCode.getCaret().getMark();

        StringBuffer novoTexto = new StringBuffer();
        novoTexto.append(TextPaneCode.getText().substring(0, posicaoAtual));
        novoTexto.append(sintaxe);
        novoTexto.append(TextPaneCode.getText().substring(posicaoAtual));

        TextPaneCode.setText(novoTexto.toString());

    }

    public void aparencias(int valor) {
        try {
            look = UIManager.getInstalledLookAndFeels();

            UIManager.setLookAndFeel(look[valor].getClassName());
            SwingUtilities.updateComponentTreeUI(this);

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditorCAlg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(EditorCAlg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(EditorCAlg.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(EditorCAlg.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void SetEditorProperties() {
        editorProperties = new EditorProperties();
        //-----------------------------MENU FICHEIRO --------------------
        MenuFicheiroAberto1.setText(editorProperties.GetProperty("file1"));
        MenuFicheiroAberto2.setText(editorProperties.GetProperty("file2"));
        MenuFicheiroAberto3.setText(editorProperties.GetProperty("file3"));
        //----------------------------FONTE ----------------------------------
        String fontName = editorProperties.GetProperty("fontName");
        int size = Integer.parseInt(editorProperties.GetProperty("fontSize"));
        int style = 0;
        if (editorProperties.GetProperty("fontItalic").equalsIgnoreCase("true")) {
            style += Font.ITALIC;
        }
        if (editorProperties.GetProperty("fontBold").equalsIgnoreCase("true")) {
            style += Font.BOLD;
        }
        TextPaneCode.setFont(new Font(fontName, style, size));

        //------------------------------------COR ------------------------------- 
        int R = Integer.parseInt(editorProperties.GetProperty("backColorR"));
        int G = Integer.parseInt(editorProperties.GetProperty("backColorG"));
        int B = Integer.parseInt(editorProperties.GetProperty("backColorB"));
        txtCode.defaultBackGround = new Color(R, G, B);
        TextPaneCode.setBackground(new Color(R, G, B));

        //------------------------  SISTEMA DE COR DO EDITOR ---------
        MenuEditorCheckSyntax.setState(editorProperties.GetProperty("sintax").equalsIgnoreCase("true"));
        MenuEditorCheckSyntaxActionPerformed(null);
    }

    public void SelectErrorLine(int numChar) {
        txtCode.selectErrorLine(numChar);
        TextPaneCode.setCaretPosition(numChar);
    }

    public void SelectCodeLine(int numChar) {
        txtCode.selectCodeLine(numChar);
        TextPaneCode.setCaretPosition(numChar);
    }

    public void DeSelectLine(int numChar) {
        txtCode.deSelectCodeLine(numChar);
    }
///////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////

    public void SelectTabUnderEditor(int index) {
        this.tpUnderCodeEditor.setSelectedIndex(index);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        popupMenu1 = new java.awt.PopupMenu();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        ToolBarPrincipal = new javax.swing.JToolBar();
        ToolBarFicheiro = new javax.swing.JToolBar();
        ButtonFicheiroNovo = new javax.swing.JButton();
        jLabel14 = new javax.swing.JLabel();
        ButtonFicheiroAbrir = new javax.swing.JButton();
        jLabel13 = new javax.swing.JLabel();
        ButtonFicheiroGuardar = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        TooBarEditar = new javax.swing.JToolBar();
        ButtonEditarReformatar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        ButtonEditarRecuar = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        ButtonEditarAvancar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        ButtonEditarCopiar = new javax.swing.JButton();
        jLabel11 = new javax.swing.JLabel();
        ButtonEditarColar = new javax.swing.JButton();
        jLabel10 = new javax.swing.JLabel();
        ButtonEditarCortar = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        ToolBarPrograma = new javax.swing.JToolBar();
        jLabel6 = new javax.swing.JLabel();
        ButtonProgramaVerificar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        ButtonProgramaCorrer = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        ButtonProgramaParar = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jToolBarConversao = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel15 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        btEmail = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        btDica = new javax.swing.JButton();
        jLabel17 = new javax.swing.JLabel();
        jButtonSairToobar = new javax.swing.JButton();
        jSplitPane2 = new javax.swing.JSplitPane();
        scrollCodeEditor = new javax.swing.JScrollPane();
        TextPaneCode = new javax.swing.JTextPane();
        tpUnderCodeEditor = new javax.swing.JTabbedPane();
        spOutput = new javax.swing.JScrollPane();
        scrollMonitor = new javax.swing.JScrollPane();
        spInfo = new javax.swing.JScrollPane();
        scrollInfo = new javax.swing.JScrollPane();
        jMenuBar2 = new javax.swing.JMenuBar();
        MenuFicheiro1 = new javax.swing.JMenu();
        MenuFicheiroNovo1 = new javax.swing.JMenuItem();
        MenuFicheiroAbrir1 = new javax.swing.JMenuItem();
        MenuFicheiroGuardar = new javax.swing.JMenuItem();
        MenuFicheiroGuardarComo = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JSeparator();
        MenuFicheiroAberto1 = new javax.swing.JMenuItem();
        MenuFicheiroAberto2 = new javax.swing.JMenuItem();
        MenuFicheiroAberto3 = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JSeparator();
        MenuFicheiroSair1 = new javax.swing.JMenuItem();
        MenuEditar1 = new javax.swing.JMenu();
        MenuEditarUndo = new javax.swing.JMenuItem();
        MenuEditarRedo = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JSeparator();
        MenuEditarCopiar = new javax.swing.JMenuItem();
        MenuEditarColar = new javax.swing.JMenuItem();
        MenuEditarCortar = new javax.swing.JMenuItem();
        MenuSelecionarTudo = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JSeparator();
        MenuEditarReformatar = new javax.swing.JMenuItem();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        MenuAlgoritmo = new javax.swing.JMenu();
        MenuAlgoritmoVerificar = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JSeparator();
        MenuAlgoritmoCorrer = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JSeparator();
        MenuProgramaParar = new javax.swing.JMenuItem();
        MenuEditor = new javax.swing.JMenu();
        MenuEditorCheckSyntax = new javax.swing.JCheckBoxMenuItem();
        jSeparator10 = new javax.swing.JSeparator();
        MenuEditorFonte = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        menuItemNimbus = new javax.swing.JMenuItem();
        menuItemWindows = new javax.swing.JMenuItem();
        menuItemMetal = new javax.swing.JMenuItem();
        menuMudarConsola = new javax.swing.JMenuItem();
        MenuAjuda = new javax.swing.JMenu();
        MenuAjudaAcerca = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        menuExemploAlgoritmo = new javax.swing.JMenuItem();

        popupMenu1.setLabel("popupMenu1");
        popupMenu1.getAccessibleContext().setAccessibleParent(TextPaneCode);

        jPopupMenu1.setBackground(new java.awt.Color(0, 153, 255));
        jPopupMenu1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 51, 255)));

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("CompAlg - Versão 1.2 (03/14)");

        ToolBarPrincipal.setBackground(new java.awt.Color(255, 255, 255));
        ToolBarPrincipal.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        ToolBarPrincipal.setMaximumSize(new java.awt.Dimension(667, 45));
        ToolBarPrincipal.setMinimumSize(new java.awt.Dimension(667, 45));
        ToolBarPrincipal.setPreferredSize(new java.awt.Dimension(667, 50));

        ToolBarFicheiro.setBackground(new java.awt.Color(255, 255, 255));
        ToolBarFicheiro.setMaximumSize(new java.awt.Dimension(145, 40));
        ToolBarFicheiro.setMinimumSize(new java.awt.Dimension(145, 40));
        ToolBarFicheiro.setPreferredSize(new java.awt.Dimension(145, 45));

        ButtonFicheiroNovo.setBackground(new java.awt.Color(255, 255, 255));
        ButtonFicheiroNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/novo (2).png"))); // NOI18N
        ButtonFicheiroNovo.setToolTipText("Novo");
        ButtonFicheiroNovo.setBorder(null);
        ButtonFicheiroNovo.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonFicheiroNovo.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonFicheiroNovo.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonFicheiroNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFicheiroNovoActionPerformed(evt);
            }
        });
        ToolBarFicheiro.add(ButtonFicheiroNovo);

        jLabel14.setText(" ");
        ToolBarFicheiro.add(jLabel14);

        ButtonFicheiroAbrir.setBackground(new java.awt.Color(255, 255, 255));
        ButtonFicheiroAbrir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/abrir.png"))); // NOI18N
        ButtonFicheiroAbrir.setToolTipText("Abrir");
        ButtonFicheiroAbrir.setBorder(null);
        ButtonFicheiroAbrir.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonFicheiroAbrir.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonFicheiroAbrir.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonFicheiroAbrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFicheiroAbrirActionPerformed(evt);
            }
        });
        ToolBarFicheiro.add(ButtonFicheiroAbrir);

        jLabel13.setText(" ");
        ToolBarFicheiro.add(jLabel13);

        ButtonFicheiroGuardar.setBackground(new java.awt.Color(255, 255, 255));
        ButtonFicheiroGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/salvar.png"))); // NOI18N
        ButtonFicheiroGuardar.setToolTipText("Guardar");
        ButtonFicheiroGuardar.setBorder(null);
        ButtonFicheiroGuardar.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonFicheiroGuardar.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonFicheiroGuardar.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonFicheiroGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonFicheiroGuardarActionPerformed(evt);
            }
        });
        ToolBarFicheiro.add(ButtonFicheiroGuardar);

        jLabel8.setText("        ");
        ToolBarFicheiro.add(jLabel8);

        ToolBarPrincipal.add(ToolBarFicheiro);

        TooBarEditar.setBackground(new java.awt.Color(255, 255, 255));
        TooBarEditar.setMaximumSize(new java.awt.Dimension(290, 40));
        TooBarEditar.setMinimumSize(new java.awt.Dimension(290, 40));
        TooBarEditar.setPreferredSize(new java.awt.Dimension(290, 45));

        ButtonEditarReformatar.setBackground(new java.awt.Color(255, 255, 255));
        ButtonEditarReformatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/corretor.png"))); // NOI18N
        ButtonEditarReformatar.setToolTipText("Correção automática do código");
        ButtonEditarReformatar.setBorder(null);
        ButtonEditarReformatar.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonEditarReformatar.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonEditarReformatar.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonEditarReformatar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEditarReformatarActionPerformed(evt);
            }
        });
        TooBarEditar.add(ButtonEditarReformatar);

        jLabel1.setText("  ");
        TooBarEditar.add(jLabel1);

        ButtonEditarRecuar.setBackground(new java.awt.Color(255, 255, 255));
        ButtonEditarRecuar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/left_32.png"))); // NOI18N
        ButtonEditarRecuar.setToolTipText("Anterior");
        ButtonEditarRecuar.setBorder(null);
        ButtonEditarRecuar.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonEditarRecuar.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonEditarRecuar.setName(""); // NOI18N
        ButtonEditarRecuar.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonEditarRecuar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEditarRecuarActionPerformed(evt);
            }
        });
        TooBarEditar.add(ButtonEditarRecuar);
        ButtonEditarRecuar.getAccessibleContext().setAccessibleName("retroceder");

        jLabel12.setText(" ");
        TooBarEditar.add(jLabel12);

        ButtonEditarAvancar.setBackground(new java.awt.Color(255, 255, 255));
        ButtonEditarAvancar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/right_32.png"))); // NOI18N
        ButtonEditarAvancar.setToolTipText("Posterior");
        ButtonEditarAvancar.setBorder(null);
        ButtonEditarAvancar.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonEditarAvancar.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonEditarAvancar.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonEditarAvancar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEditarAvancarActionPerformed(evt);
            }
        });
        TooBarEditar.add(ButtonEditarAvancar);
        ButtonEditarAvancar.getAccessibleContext().setAccessibleName("avancar");

        jLabel2.setText("  ");
        TooBarEditar.add(jLabel2);

        ButtonEditarCopiar.setBackground(new java.awt.Color(255, 255, 255));
        ButtonEditarCopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/copiar.png"))); // NOI18N
        ButtonEditarCopiar.setToolTipText("Copiar");
        ButtonEditarCopiar.setBorder(null);
        ButtonEditarCopiar.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonEditarCopiar.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonEditarCopiar.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonEditarCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEditarCopiarActionPerformed(evt);
            }
        });
        TooBarEditar.add(ButtonEditarCopiar);

        jLabel11.setText(" ");
        TooBarEditar.add(jLabel11);

        ButtonEditarColar.setBackground(new java.awt.Color(255, 255, 255));
        ButtonEditarColar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/colar.png"))); // NOI18N
        ButtonEditarColar.setToolTipText("Colar");
        ButtonEditarColar.setBorder(null);
        ButtonEditarColar.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonEditarColar.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonEditarColar.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonEditarColar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEditarColarActionPerformed(evt);
            }
        });
        TooBarEditar.add(ButtonEditarColar);

        jLabel10.setText(" ");
        TooBarEditar.add(jLabel10);

        ButtonEditarCortar.setBackground(new java.awt.Color(255, 255, 255));
        ButtonEditarCortar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/cut.png"))); // NOI18N
        ButtonEditarCortar.setToolTipText("Cortar");
        ButtonEditarCortar.setBorder(null);
        ButtonEditarCortar.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonEditarCortar.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonEditarCortar.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonEditarCortar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonEditarCortarActionPerformed(evt);
            }
        });
        TooBarEditar.add(ButtonEditarCortar);

        jLabel9.setText("   ");
        TooBarEditar.add(jLabel9);

        ToolBarPrincipal.add(TooBarEditar);

        ToolBarPrograma.setBackground(new java.awt.Color(255, 255, 255));
        ToolBarPrograma.setMaximumSize(new java.awt.Dimension(190, 40));
        ToolBarPrograma.setMinimumSize(new java.awt.Dimension(190, 40));
        ToolBarPrograma.setPreferredSize(new java.awt.Dimension(190, 45));
        ToolBarPrograma.add(jLabel6);

        ButtonProgramaVerificar.setBackground(new java.awt.Color(255, 255, 255));
        ButtonProgramaVerificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/Compile.png"))); // NOI18N
        ButtonProgramaVerificar.setToolTipText("Compilar o algoritmo");
        ButtonProgramaVerificar.setBorder(null);
        ButtonProgramaVerificar.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonProgramaVerificar.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonProgramaVerificar.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonProgramaVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonProgramaVerificarActionPerformed(evt);
            }
        });
        ToolBarPrograma.add(ButtonProgramaVerificar);

        jLabel3.setText("  ");
        ToolBarPrograma.add(jLabel3);

        ButtonProgramaCorrer.setBackground(new java.awt.Color(255, 255, 255));
        ButtonProgramaCorrer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/start.png"))); // NOI18N
        ButtonProgramaCorrer.setToolTipText("Compila e Executar o algoritmo");
        ButtonProgramaCorrer.setBorder(null);
        ButtonProgramaCorrer.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonProgramaCorrer.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonProgramaCorrer.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonProgramaCorrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonProgramaCorrerActionPerformed(evt);
            }
        });
        ToolBarPrograma.add(ButtonProgramaCorrer);

        jLabel4.setText("  ");
        ToolBarPrograma.add(jLabel4);

        ButtonProgramaParar.setBackground(new java.awt.Color(255, 255, 255));
        ButtonProgramaParar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/stop.jpg"))); // NOI18N
        ButtonProgramaParar.setToolTipText("Parar o algoritmo");
        ButtonProgramaParar.setBorder(null);
        ButtonProgramaParar.setMaximumSize(new java.awt.Dimension(33, 36));
        ButtonProgramaParar.setMinimumSize(new java.awt.Dimension(33, 36));
        ButtonProgramaParar.setPreferredSize(new java.awt.Dimension(33, 36));
        ButtonProgramaParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ButtonProgramaPararActionPerformed(evt);
            }
        });
        ToolBarPrograma.add(ButtonProgramaParar);

        jLabel7.setText("  ");
        ToolBarPrograma.add(jLabel7);

        ToolBarPrincipal.add(ToolBarPrograma);

        jToolBarConversao.setBackground(new java.awt.Color(255, 255, 255));
        jToolBarConversao.setToolTipText("");
        jToolBarConversao.setMaximumSize(new java.awt.Dimension(400, 40));
        jToolBarConversao.setMinimumSize(new java.awt.Dimension(400, 40));
        jToolBarConversao.setName(""); // NOI18N
        jToolBarConversao.setPreferredSize(new java.awt.Dimension(370, 45));

        jButton1.setBackground(new java.awt.Color(255, 255, 255));
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/C.png"))); // NOI18N
        jButton1.setToolTipText("Converter na Linguagem C");
        jButton1.setBorder(null);
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setMaximumSize(new java.awt.Dimension(33, 36));
        jButton1.setMinimumSize(new java.awt.Dimension(33, 36));
        jButton1.setPreferredSize(new java.awt.Dimension(33, 36));
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBarConversao.add(jButton1);
        jButton1.getAccessibleContext().setAccessibleParent(jToolBarConversao);

        jLabel16.setText(" ");
        jToolBarConversao.add(jLabel16);

        jButton2.setBackground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/Java-icon.png"))); // NOI18N
        jButton2.setToolTipText("Converter na Linguagem JAVA");
        jButton2.setBorder(null);
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setMaximumSize(new java.awt.Dimension(33, 36));
        jButton2.setMinimumSize(new java.awt.Dimension(33, 36));
        jButton2.setPreferredSize(new java.awt.Dimension(33, 36));
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBarConversao.add(jButton2);

        jLabel15.setText("     ");
        jToolBarConversao.add(jLabel15);

        jButton3.setBackground(new java.awt.Color(255, 255, 255));
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/ajuda.png"))); // NOI18N
        jButton3.setToolTipText("Ajuda da linguagem");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setMaximumSize(new java.awt.Dimension(33, 36));
        jButton3.setMinimumSize(new java.awt.Dimension(33, 36));
        jButton3.setPreferredSize(new java.awt.Dimension(33, 36));
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jToolBarConversao.add(jButton3);

        jLabel5.setText("  ");
        jLabel5.setToolTipText("");
        jToolBarConversao.add(jLabel5);

        btEmail.setBackground(new java.awt.Color(255, 255, 255));
        btEmail.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/Email-Atmark.png"))); // NOI18N
        btEmail.setToolTipText("Envia uma sugestão/dúvida sobre o compilador");
        btEmail.setFocusable(false);
        btEmail.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btEmail.setMaximumSize(new java.awt.Dimension(33, 36));
        btEmail.setMinimumSize(new java.awt.Dimension(33, 36));
        btEmail.setPreferredSize(new java.awt.Dimension(33, 36));
        btEmail.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btEmail.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btEmailActionPerformed(evt);
            }
        });
        jToolBarConversao.add(btEmail);

        jLabel18.setText("  ");
        jToolBarConversao.add(jLabel18);

        btDica.setBackground(new java.awt.Color(255, 255, 255));
        btDica.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/bubble_32.png"))); // NOI18N
        btDica.setToolTipText("Ver a Dica do dia");
        btDica.setFocusable(false);
        btDica.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btDica.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btDica.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btDicaActionPerformed(evt);
            }
        });
        jToolBarConversao.add(btDica);

        jLabel17.setText("       ");
        jLabel17.setToolTipText("");
        jToolBarConversao.add(jLabel17);

        jButtonSairToobar.setBackground(new java.awt.Color(255, 255, 255));
        jButtonSairToobar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/Exit.png"))); // NOI18N
        jButtonSairToobar.setToolTipText("Sair do compilador");
        jButtonSairToobar.setFocusable(false);
        jButtonSairToobar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButtonSairToobar.setMaximumSize(new java.awt.Dimension(33, 36));
        jButtonSairToobar.setMinimumSize(new java.awt.Dimension(33, 36));
        jButtonSairToobar.setPreferredSize(new java.awt.Dimension(33, 36));
        jButtonSairToobar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButtonSairToobar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButtonSairToobarActionPerformed(evt);
            }
        });
        jToolBarConversao.add(jButtonSairToobar);

        ToolBarPrincipal.add(jToolBarConversao);

        getContentPane().add(ToolBarPrincipal, java.awt.BorderLayout.NORTH);

        jSplitPane2.setBackground(new java.awt.Color(153, 180, 209));
        jSplitPane2.setDividerSize(20);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane2.setMinimumSize(new java.awt.Dimension(402, 280));
        jSplitPane2.setPreferredSize(new java.awt.Dimension(409, 202));

        scrollCodeEditor.setMinimumSize(new java.awt.Dimension(400, 300));
        scrollCodeEditor.setPreferredSize(new java.awt.Dimension(400, 900));

        TextPaneCode.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 180, 209), 2));
        TextPaneCode.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        TextPaneCode.setToolTipText("Área de densenvolvimento de Algoritmo");
        TextPaneCode.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        TextPaneCode.setMinimumSize(new java.awt.Dimension(4, 280));
        TextPaneCode.setPreferredSize(new java.awt.Dimension(4, 280));
        TextPaneCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TextPaneCodeKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                TextPaneCodeKeyTyped(evt);
            }
        });
        scrollCodeEditor.setViewportView(TextPaneCode);

        jSplitPane2.setTopComponent(scrollCodeEditor);

        tpUnderCodeEditor.setBackground(new java.awt.Color(102, 102, 102));
        tpUnderCodeEditor.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(185, 209, 234)));
        tpUnderCodeEditor.setForeground(new java.awt.Color(255, 255, 255));
        tpUnderCodeEditor.setAutoscrolls(true);
        tpUnderCodeEditor.setPreferredSize(new java.awt.Dimension(407, 80));

        spOutput.setBackground(new java.awt.Color(255, 255, 255));
        spOutput.setAutoscrolls(true);
        spOutput.setPreferredSize(new java.awt.Dimension(400, 200));

        scrollMonitor.setBackground(new java.awt.Color(255, 255, 255));
        scrollMonitor.setForeground(new java.awt.Color(0, 0, 255));
        scrollMonitor.setToolTipText("Execução do Algoritmo na Consola");
        scrollMonitor.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        spOutput.setViewportView(scrollMonitor);

        tpUnderCodeEditor.addTab("Consola", new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/consola (2).png")), spOutput, "Esta \"tab\" apresenta a saida por defeito do interpretador..."); // NOI18N

        spInfo.setBackground(new java.awt.Color(255, 255, 255));
        spInfo.setAutoscrolls(true);
        spInfo.setPreferredSize(new java.awt.Dimension(400, 200));

        scrollInfo.setBackground(new java.awt.Color(0, 0, 0));
        scrollInfo.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 180, 209)));
        scrollInfo.setForeground(new java.awt.Color(255, 255, 255));
        scrollInfo.setToolTipText("Execução do Algoritmo na Consola");
        scrollInfo.setFont(new java.awt.Font("Courier New", 0, 12)); // NOI18N
        info = new ConsoleIO();
        info.setColor(Color.WHITE,Color.BLACK);
        scrollInfo.add(info);
        scrollInfo.setViewportView(info);
        spInfo.setViewportView(scrollInfo);

        tpUnderCodeEditor.addTab("Informação", new javax.swing.ImageIcon(getClass().getResource("/Icons/toolbar/info.png")), spInfo, "Esta \"tab\" apresenta informação da execução do compilador."); // NOI18N

        jSplitPane2.setBottomComponent(tpUnderCodeEditor);

        getContentPane().add(jSplitPane2, java.awt.BorderLayout.CENTER);

        jMenuBar2.setBackground(new java.awt.Color(51, 153, 255));
        jMenuBar2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        MenuFicheiro1.setBackground(new java.awt.Color(51, 153, 255));
        MenuFicheiro1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        MenuFicheiro1.setForeground(new java.awt.Color(255, 255, 255));
        MenuFicheiro1.setMnemonic('F');
        MenuFicheiro1.setText("Ficheiro");
        MenuFicheiro1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        MenuFicheiro1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFicheiro1ActionPerformed(evt);
            }
        });

        MenuFicheiroNovo1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        MenuFicheiroNovo1.setBackground(new java.awt.Color(255, 255, 255));
        MenuFicheiroNovo1.setForeground(new java.awt.Color(0, 51, 255));
        MenuFicheiroNovo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/novo.png"))); // NOI18N
        MenuFicheiroNovo1.setText("Novo");
        MenuFicheiroNovo1.setToolTipText("novo Algoritmo");
        MenuFicheiroNovo1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFicheiroNovo1ActionPerformed(evt);
            }
        });
        MenuFicheiro1.add(MenuFicheiroNovo1);

        MenuFicheiroAbrir1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        MenuFicheiroAbrir1.setBackground(new java.awt.Color(255, 255, 255));
        MenuFicheiroAbrir1.setForeground(new java.awt.Color(0, 51, 255));
        MenuFicheiroAbrir1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/abrir.png"))); // NOI18N
        MenuFicheiroAbrir1.setText("Abrir");
        MenuFicheiroAbrir1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFicheiroAbrir1ActionPerformed(evt);
            }
        });
        MenuFicheiro1.add(MenuFicheiroAbrir1);

        MenuFicheiroGuardar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        MenuFicheiroGuardar.setBackground(new java.awt.Color(255, 255, 255));
        MenuFicheiroGuardar.setForeground(new java.awt.Color(0, 51, 255));
        MenuFicheiroGuardar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/salvar.png"))); // NOI18N
        MenuFicheiroGuardar.setText("Guardar");
        MenuFicheiroGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFicheiroGuardarActionPerformed(evt);
            }
        });
        MenuFicheiro1.add(MenuFicheiroGuardar);

        MenuFicheiroGuardarComo.setBackground(new java.awt.Color(255, 255, 255));
        MenuFicheiroGuardarComo.setForeground(new java.awt.Color(0, 51, 255));
        MenuFicheiroGuardarComo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/guardarComo.png"))); // NOI18N
        MenuFicheiroGuardarComo.setText("Guardar Como . . .");
        MenuFicheiroGuardarComo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFicheiroGuardarComoActionPerformed(evt);
            }
        });
        MenuFicheiro1.add(MenuFicheiroGuardarComo);
        MenuFicheiro1.add(jSeparator2);

        MenuFicheiroAberto1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/file_document.png"))); // NOI18N
        MenuFicheiroAberto1.setText("ficheiro_sem_nome");
        MenuFicheiroAberto1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFicheiroAberto1ActionPerformed(evt);
            }
        });
        MenuFicheiro1.add(MenuFicheiroAberto1);

        MenuFicheiroAberto2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/file_document.png"))); // NOI18N
        MenuFicheiroAberto2.setText("ficheiro_sem_nome");
        MenuFicheiroAberto2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFicheiroAberto2ActionPerformed(evt);
            }
        });
        MenuFicheiro1.add(MenuFicheiroAberto2);

        MenuFicheiroAberto3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/file_document.png"))); // NOI18N
        MenuFicheiroAberto3.setText("ficheiro_sem_nome");
        MenuFicheiroAberto3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFicheiroAberto3ActionPerformed(evt);
            }
        });
        MenuFicheiro1.add(MenuFicheiroAberto3);
        MenuFicheiro1.add(jSeparator1);

        MenuFicheiroSair1.setBackground(new java.awt.Color(255, 255, 255));
        MenuFicheiroSair1.setForeground(new java.awt.Color(204, 0, 0));
        MenuFicheiroSair1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/sairG.png"))); // NOI18N
        MenuFicheiroSair1.setText("Sair");
        MenuFicheiroSair1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuFicheiroSair1ActionPerformed(evt);
            }
        });
        MenuFicheiro1.add(MenuFicheiroSair1);

        jMenuBar2.add(MenuFicheiro1);

        MenuEditar1.setBackground(new java.awt.Color(51, 153, 255));
        MenuEditar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        MenuEditar1.setForeground(new java.awt.Color(255, 255, 255));
        MenuEditar1.setMnemonic('E');
        MenuEditar1.setText("Editar");
        MenuEditar1.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        MenuEditarUndo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        MenuEditarUndo.setBackground(new java.awt.Color(255, 255, 255));
        MenuEditarUndo.setForeground(new java.awt.Color(0, 51, 255));
        MenuEditarUndo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/left_32.png"))); // NOI18N
        MenuEditarUndo.setText("Anterior");
        MenuEditarUndo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuEditarUndoActionPerformed(evt);
            }
        });
        MenuEditar1.add(MenuEditarUndo);

        MenuEditarRedo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        MenuEditarRedo.setBackground(new java.awt.Color(255, 255, 255));
        MenuEditarRedo.setForeground(new java.awt.Color(0, 51, 255));
        MenuEditarRedo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/right_32.png"))); // NOI18N
        MenuEditarRedo.setText("Avançar");
        MenuEditarRedo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuEditarRedoActionPerformed(evt);
            }
        });
        MenuEditar1.add(MenuEditarRedo);
        MenuEditar1.add(jSeparator3);

        MenuEditarCopiar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        MenuEditarCopiar.setBackground(new java.awt.Color(255, 255, 255));
        MenuEditarCopiar.setForeground(new java.awt.Color(0, 51, 255));
        MenuEditarCopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/copiar.png"))); // NOI18N
        MenuEditarCopiar.setText("Copiar");
        MenuEditarCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuEditarCopiarActionPerformed(evt);
            }
        });
        MenuEditar1.add(MenuEditarCopiar);

        MenuEditarColar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        MenuEditarColar.setBackground(new java.awt.Color(255, 255, 255));
        MenuEditarColar.setForeground(new java.awt.Color(0, 51, 255));
        MenuEditarColar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/colar.png"))); // NOI18N
        MenuEditarColar.setText("Colar");
        MenuEditarColar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuEditarColarActionPerformed(evt);
            }
        });
        MenuEditar1.add(MenuEditarColar);

        MenuEditarCortar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        MenuEditarCortar.setBackground(new java.awt.Color(255, 255, 255));
        MenuEditarCortar.setForeground(new java.awt.Color(0, 51, 255));
        MenuEditarCortar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/cut.png"))); // NOI18N
        MenuEditarCortar.setText("Cortar");
        MenuEditarCortar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuEditarCortarActionPerformed(evt);
            }
        });
        MenuEditar1.add(MenuEditarCortar);

        MenuSelecionarTudo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.ALT_MASK));
        MenuSelecionarTudo.setBackground(new java.awt.Color(255, 255, 255));
        MenuSelecionarTudo.setForeground(new java.awt.Color(0, 51, 255));
        MenuSelecionarTudo.setText("Seleccionar tudo");
        MenuSelecionarTudo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSelecionarTudoActionPerformed(evt);
            }
        });
        MenuEditar1.add(MenuSelecionarTudo);
        MenuEditar1.add(jSeparator4);

        MenuEditarReformatar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.ALT_MASK));
        MenuEditarReformatar.setBackground(new java.awt.Color(255, 255, 255));
        MenuEditarReformatar.setForeground(new java.awt.Color(0, 51, 255));
        MenuEditarReformatar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/corretor.png"))); // NOI18N
        MenuEditarReformatar.setText("Correcção automático");
        MenuEditarReformatar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuEditarReformatarActionPerformed(evt);
            }
        });
        MenuEditar1.add(MenuEditarReformatar);

        jMenuItem1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem1.setForeground(new java.awt.Color(0, 51, 255));
        jMenuItem1.setMnemonic('l');
        jMenuItem1.setText("Ir para linha");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        MenuEditar1.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_P, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem2.setForeground(new java.awt.Color(0, 51, 255));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/search_32.png"))); // NOI18N
        jMenuItem2.setText("Busca por palavra");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        MenuEditar1.add(jMenuItem2);

        jMenuBar2.add(MenuEditar1);

        MenuAlgoritmo.setBackground(new java.awt.Color(51, 153, 255));
        MenuAlgoritmo.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        MenuAlgoritmo.setForeground(new java.awt.Color(255, 255, 255));
        MenuAlgoritmo.setMnemonic('A');
        MenuAlgoritmo.setText("Algoritmo");
        MenuAlgoritmo.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        MenuAlgoritmo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAlgoritmoActionPerformed(evt);
            }
        });

        MenuAlgoritmoVerificar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        MenuAlgoritmoVerificar.setBackground(new java.awt.Color(255, 255, 255));
        MenuAlgoritmoVerificar.setForeground(new java.awt.Color(0, 51, 255));
        MenuAlgoritmoVerificar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/Compile.png"))); // NOI18N
        MenuAlgoritmoVerificar.setText("Compilar o algoritmo");
        MenuAlgoritmoVerificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAlgoritmoVerificarActionPerformed(evt);
            }
        });
        MenuAlgoritmo.add(MenuAlgoritmoVerificar);
        MenuAlgoritmo.add(jSeparator5);

        MenuAlgoritmoCorrer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        MenuAlgoritmoCorrer.setBackground(new java.awt.Color(255, 255, 255));
        MenuAlgoritmoCorrer.setForeground(new java.awt.Color(0, 51, 255));
        MenuAlgoritmoCorrer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/executar.png"))); // NOI18N
        MenuAlgoritmoCorrer.setText("Executar");
        MenuAlgoritmoCorrer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAlgoritmoCorrerActionPerformed(evt);
            }
        });
        MenuAlgoritmo.add(MenuAlgoritmoCorrer);
        MenuAlgoritmo.add(jSeparator6);

        MenuProgramaParar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F10, 0));
        MenuProgramaParar.setBackground(new java.awt.Color(255, 255, 255));
        MenuProgramaParar.setForeground(new java.awt.Color(0, 51, 255));
        MenuProgramaParar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/parar.png"))); // NOI18N
        MenuProgramaParar.setText("Parar a Execução");
        MenuProgramaParar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuProgramaPararActionPerformed(evt);
            }
        });
        MenuAlgoritmo.add(MenuProgramaParar);

        jMenuBar2.add(MenuAlgoritmo);

        MenuEditor.setBackground(new java.awt.Color(51, 153, 255));
        MenuEditor.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        MenuEditor.setForeground(new java.awt.Color(255, 255, 255));
        MenuEditor.setMnemonic('d');
        MenuEditor.setText("Editor");
        MenuEditor.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N
        MenuEditor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuEditorActionPerformed(evt);
            }
        });

        MenuEditorCheckSyntax.setBackground(new java.awt.Color(255, 255, 255));
        MenuEditorCheckSyntax.setForeground(new java.awt.Color(0, 51, 255));
        MenuEditorCheckSyntax.setSelected(true);
        MenuEditorCheckSyntax.setText("Colorir Programa");
        MenuEditorCheckSyntax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuEditorCheckSyntaxActionPerformed(evt);
            }
        });
        MenuEditor.add(MenuEditorCheckSyntax);
        MenuEditor.add(jSeparator10);

        MenuEditorFonte.setBackground(new java.awt.Color(255, 255, 255));
        MenuEditorFonte.setForeground(new java.awt.Color(0, 51, 255));
        MenuEditorFonte.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/editor_font.png"))); // NOI18N
        MenuEditorFonte.setMnemonic('o');
        MenuEditorFonte.setText("Fonte");
        MenuEditorFonte.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuEditorFonteActionPerformed(evt);
            }
        });
        MenuEditor.add(MenuEditorFonte);

        jMenu1.setBackground(new java.awt.Color(255, 255, 255));
        jMenu1.setForeground(new java.awt.Color(0, 51, 255));
        jMenu1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/looknfeelsuportados.png"))); // NOI18N
        jMenu1.setMnemonic('A');
        jMenu1.setText("Aparência");

        menuItemNimbus.setBackground(new java.awt.Color(255, 255, 255));
        menuItemNimbus.setForeground(new java.awt.Color(0, 51, 255));
        menuItemNimbus.setText("CDE/Motif");
        menuItemNimbus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemNimbusActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemNimbus);

        menuItemWindows.setBackground(new java.awt.Color(255, 255, 255));
        menuItemWindows.setForeground(new java.awt.Color(0, 51, 255));
        menuItemWindows.setText("Windows");
        menuItemWindows.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemWindowsActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemWindows);

        menuItemMetal.setBackground(new java.awt.Color(255, 255, 255));
        menuItemMetal.setForeground(new java.awt.Color(0, 51, 255));
        menuItemMetal.setText("Metal");
        menuItemMetal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuItemMetalActionPerformed(evt);
            }
        });
        jMenu1.add(menuItemMetal);

        MenuEditor.add(jMenu1);

        menuMudarConsola.setBackground(new java.awt.Color(255, 255, 255));
        menuMudarConsola.setForeground(new java.awt.Color(0, 51, 255));
        menuMudarConsola.setText("Mudar a Cor da Consola");
        MenuEditor.add(menuMudarConsola);

        jMenuBar2.add(MenuEditor);

        MenuAjuda.setBackground(new java.awt.Color(51, 153, 255));
        MenuAjuda.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        MenuAjuda.setForeground(new java.awt.Color(255, 255, 255));
        MenuAjuda.setMnemonic('u');
        MenuAjuda.setText("Ajuda");
        MenuAjuda.setFont(new java.awt.Font("Dialog", 1, 12)); // NOI18N

        MenuAjudaAcerca.setBackground(new java.awt.Color(255, 255, 255));
        MenuAjudaAcerca.setForeground(new java.awt.Color(0, 51, 255));
        MenuAjudaAcerca.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/info_32.png"))); // NOI18N
        MenuAjudaAcerca.setText("Informaçoes sobre o autor");
        MenuAjudaAcerca.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAjudaAcercaActionPerformed(evt);
            }
        });
        MenuAjuda.add(MenuAjudaAcerca);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        jMenuItem3.setBackground(new java.awt.Color(255, 255, 255));
        jMenuItem3.setForeground(new java.awt.Color(0, 51, 255));
        jMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/ajuda.png"))); // NOI18N
        jMenuItem3.setText("Ajuda da linguagem");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        MenuAjuda.add(jMenuItem3);
        jMenuItem3.getAccessibleContext().setAccessibleDescription("");

        menuExemploAlgoritmo.setBackground(new java.awt.Color(255, 255, 255));
        menuExemploAlgoritmo.setForeground(new java.awt.Color(0, 51, 255));
        menuExemploAlgoritmo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/menu/exemplo2.png"))); // NOI18N
        menuExemploAlgoritmo.setText("Exemplos de Algoritmos");
        menuExemploAlgoritmo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExemploAlgoritmoActionPerformed(evt);
            }
        });
        MenuAjuda.add(menuExemploAlgoritmo);

        jMenuBar2.add(MenuAjuda);

        setJMenuBar(jMenuBar2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void MenuEditorFonteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuEditorFonteActionPerformed
        FontChooser f = new FontChooser(this, TextPaneCode.getFont(), TextPaneCode.getBackground());
        f.setVisible(true);
        if (f.fontSelected) {
            Font fnt = f.getNewFont();
            System.out.println("FONTE   :" + fnt.getFontName());
            System.out.println("TAMANHO :" + fnt.getSize());
            TextPaneCode.setFont(f.getNewFont());
            TextPaneCode.setBackground(f.getNewColor());
            txtCode.defaultBackGround = f.getNewColor();

            editorProperties.SetProperty("fontName", fnt.getName());
            editorProperties.SetProperty("fontSize", fnt.getSize() + "");
            editorProperties.SetProperty("fontBold", fnt.isBold() + "");
            editorProperties.SetProperty("fontItalic", fnt.isItalic() + "");
            editorProperties.SetProperty("backColorR", "" + f.getNewColor().getRed());
            editorProperties.SetProperty("backColorG", "" + f.getNewColor().getGreen());
            editorProperties.SetProperty("backColorB", "" + f.getNewColor().getBlue());
            //recolorir
            this.MenuEditorCheckSyntaxActionPerformed(evt);
        }

    }//GEN-LAST:event_MenuEditorFonteActionPerformed

    private void MenuEditorCheckSyntaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuEditorCheckSyntaxActionPerformed
    }//GEN-LAST:event_MenuEditorCheckSyntaxActionPerformed

    private void MenuAjudaAcercaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAjudaAcercaActionPerformed
        tpUnderCodeEditor.setSelectedIndex(1);
        info.Clear();
        info.write(PortugolInfo.getInformation());
        AboutThis about = new AboutThis();
        about.setAlwaysOnTop(true);
        about.setVisible(true);
        about.requestFocus();
    }//GEN-LAST:event_MenuAjudaAcercaActionPerformed

    private void MenuProgramaPararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuProgramaPararActionPerformed

        autoExecute = null;
        if (instruction != null) {
            DeSelectLine(instruction.GetCharNum());
        }
        this.tpUnderCodeEditor.setSelectedIndex(1);
        info.write("\n" + System.getProperty("user.name") + ", VOCÊ PAROU O ALGORITMO...");
        console.setText("Nenhum algoritmo está sendo executado");
    }//GEN-LAST:event_MenuProgramaPararActionPerformed

    private void ButtonProgramaPararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonProgramaPararActionPerformed
        MenuProgramaPararActionPerformed(evt);
    }//GEN-LAST:event_ButtonProgramaPararActionPerformed

    private void ButtonProgramaCorrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonProgramaCorrerActionPerformed
        MenuAlgoritmoCorrerActionPerformed(evt);
    }//GEN-LAST:event_ButtonProgramaCorrerActionPerformed

    private void MenuAlgoritmoCorrerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAlgoritmoCorrerActionPerformed

        MenuEditarReformatarActionPerformed(null);

        NodeInstruction instruction = null;

        //se estiver a ser executado nao faz nada
        if (autoExecute != null) {
            return;
        }

        //verificar o programa e construir o fluxograma
        MenuAlgoritmoVerificarActionPerformed(null);

        if (prog == null) {
            return;
        }
        instruction = prog.getStartNode();
        //------------------- fazer uma thread -------------------------
        if (instruction == null) {
            return;
        }

        try {
            calend = new Calendario();
            this.autoExecute = new Thread(this);
            this.autoExecute.start();
        } catch (Exception e) {
            Message.Error("ERRO DE EXECUÇÃO:\n" + instruction.GetText()
                    + "\n" + e.getMessage());
            this.tpUnderCodeEditor.setSelectedIndex(1);
            info.write("\n\nO algoritmo foi abortado");
        }
    }//GEN-LAST:event_MenuAlgoritmoCorrerActionPerformed

    private void ButtonProgramaVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonProgramaVerificarActionPerformed
        MenuAlgoritmoVerificarActionPerformed(null);
    }//GEN-LAST:event_ButtonProgramaVerificarActionPerformed

    private void MenuAlgoritmoVerificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAlgoritmoVerificarActionPerformed
        MenuEditarReformatarActionPerformed(null);
        veriricarAlgoritmo();
    }//GEN-LAST:event_MenuAlgoritmoVerificarActionPerformed

    private void MenuAlgoritmoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAlgoritmoActionPerformed
// TODO add your handling code here:
    }//GEN-LAST:event_MenuAlgoritmoActionPerformed

    private void ButtonEditarReformatarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEditarReformatarActionPerformed
        MenuEditarReformatarActionPerformed(null);
    }//GEN-LAST:event_ButtonEditarReformatarActionPerformed

    private void ButtonEditarCortarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEditarCortarActionPerformed
        this.MenuEditarCortarActionPerformed(null);
    }//GEN-LAST:event_ButtonEditarCortarActionPerformed

    private void ButtonEditarColarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEditarColarActionPerformed
        this.MenuEditarColarActionPerformed(null);
    }//GEN-LAST:event_ButtonEditarColarActionPerformed

    private void ButtonEditarCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEditarCopiarActionPerformed
        this.MenuEditarCopiarActionPerformed(null);
    }//GEN-LAST:event_ButtonEditarCopiarActionPerformed

    private void ButtonEditarAvancarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEditarAvancarActionPerformed
        obj_RA.eventoAvancar();
    }//GEN-LAST:event_ButtonEditarAvancarActionPerformed

    private void ButtonEditarRecuarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonEditarRecuarActionPerformed
        obj_RA.eventoRetroceder();
    }//GEN-LAST:event_ButtonEditarRecuarActionPerformed

    private void TextPaneCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextPaneCodeKeyPressed
        try {
            if (evt.VK_F1 == evt.getKeyCode()) {
                tpUnderCodeEditor.setSelectedIndex(2);
                int caretPosition = TextPaneCode.getCaretPosition();
                int startIndex = javax.swing.text.Utilities.getWordStart(TextPaneCode, caretPosition);
                int endIndex = javax.swing.text.Utilities.getWordEnd(TextPaneCode, caretPosition);
                String keyword = TextPaneCode.getDocument().getText(startIndex, endIndex - startIndex).trim();
                helpLang.setPage(HelpFileName.getHelpFile(keyword));
            }

            if (evt.VK_TAB == evt.getKeyCode()) {
                // introduzir a TAB
                int caretPosition = TextPaneCode.getCaretPosition();
                txtCode.insertString(caretPosition, BeautifyCode.TAB_SPACES, new SimpleAttributeSet());
                // consumir o enter
                evt.consume();

            } else if (evt.VK_ENTER == evt.getKeyCode()) {
                // ir buscar o paragrafo actual
                int caretPosition = TextPaneCode.getCaretPosition();
                Element element = txtCode.getParagraphElement(caretPosition);
                int start = element.getStartOffset();
                int end = element.getEndOffset();
                String old = txtCode.getText(start, end - start);

                int spaces = 0;
                String tab = "\n";
                while (old.charAt(spaces) == ' ') {
                    spaces++;
                    tab += " ";
                }
                // introduzir a nova string
                txtCode.insertString(caretPosition, tab, new SimpleAttributeSet());
                // consumir o enter
                evt.consume();
            }
        } catch (Exception e) {
        }
    }//GEN-LAST:event_TextPaneCodeKeyPressed

    private void ButtonFicheiroGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonFicheiroGuardarActionPerformed
        this.MenuFicheiroGuardarActionPerformed(null);
    }//GEN-LAST:event_ButtonFicheiroGuardarActionPerformed

    private void ButtonFicheiroAbrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonFicheiroAbrirActionPerformed
        this.MenuFicheiroAbrir1ActionPerformed(null);

        // ajudaWWW.add(new WWWHelpText)

    }//GEN-LAST:event_ButtonFicheiroAbrirActionPerformed

    private void ButtonFicheiroNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ButtonFicheiroNovoActionPerformed
        this.MenuFicheiroNovo1ActionPerformed(null);
    }//GEN-LAST:event_ButtonFicheiroNovoActionPerformed

    private void MenuFicheiroGuardarComoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFicheiroGuardarComoActionPerformed
        GuardarFicheiroComo(fileManager.getFileName());
    }//GEN-LAST:event_MenuFicheiroGuardarComoActionPerformed

    private void MenuFicheiroGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFicheiroGuardarActionPerformed
        info.write("\nGuardar o Algoritmo :" + fileManager.getFileName());
        if (!fileManager.isFileOpened()) {
            GuardarFicheiroComo(fileManager.getFileName());
        } else {
            GuardarFicheiro(fileManager.getFileName());
        }
    }//GEN-LAST:event_MenuFicheiroGuardarActionPerformed

    private void MenuFicheiroAberto3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFicheiroAberto3ActionPerformed
        LerFicheiro(MenuFicheiroAberto3.getText());
    }//GEN-LAST:event_MenuFicheiroAberto3ActionPerformed

    private void MenuFicheiroAberto2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFicheiroAberto2ActionPerformed
        LerFicheiro(MenuFicheiroAberto2.getText());
    }//GEN-LAST:event_MenuFicheiroAberto2ActionPerformed

    private void MenuFicheiroAberto1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFicheiroAberto1ActionPerformed
        LerFicheiro(MenuFicheiroAberto1.getText());
    }//GEN-LAST:event_MenuFicheiroAberto1ActionPerformed

    private void TextPaneCodeKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TextPaneCodeKeyTyped
        if (!this.getTextChanged()) {
            setTextChanged(true);
        }
    }//GEN-LAST:event_TextPaneCodeKeyTyped

    private void MenuFicheiroNovo1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFicheiroNovo1ActionPerformed
        NovoFicheiro();
    }//GEN-LAST:event_MenuFicheiroNovo1ActionPerformed

    private void MenuFicheiroSair1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFicheiroSair1ActionPerformed
        confirmarSaidaNoAlgoritmo();
    }//GEN-LAST:event_MenuFicheiroSair1ActionPerformed

    private void MenuFicheiroAbrir1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFicheiroAbrir1ActionPerformed
        if (fileManager.openFileWindow(this) != null) {
            LerFicheiro(fileManager.getFileName());
        }
    }//GEN-LAST:event_MenuFicheiroAbrir1ActionPerformed

    private void MenuEditorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuEditorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuEditorActionPerformed

    private void MenuEditarCortarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuEditarCortarActionPerformed

        TextPaneCode.cut();     }//GEN-LAST:event_MenuEditarCortarActionPerformed

    private void MenuEditarColarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuEditarColarActionPerformed

        TextPaneCode.paste();     }//GEN-LAST:event_MenuEditarColarActionPerformed

    private void MenuEditarCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuEditarCopiarActionPerformed

        TextPaneCode.copy();     }//GEN-LAST:event_MenuEditarCopiarActionPerformed

    private void MenuEditarRedoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuEditarRedoActionPerformed

        urManager.redo();     }//GEN-LAST:event_MenuEditarRedoActionPerformed

    private void MenuEditarUndoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuEditarUndoActionPerformed

        urManager.undo();     }//GEN-LAST:event_MenuEditarUndoActionPerformed

    private void MenuEditarReformatarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuEditarReformatarActionPerformed

        int initPosCaret = TextPaneCode.getCaretPosition();         String newCode = BeautifyCode.IndentCode(TextPaneCode.getText());         TextPaneCode.setText(newCode);         txtCode.clearTextBackground();         TextPaneCode.setCaretPosition(initPosCaret);     }//GEN-LAST:event_MenuEditarReformatarActionPerformed

    private void menuItemNimbusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemNimbusActionPerformed
        // Muda a aparencia para Nimbus  
        aparencias(2);
    }//GEN-LAST:event_menuItemNimbusActionPerformed

    private void menuItemWindowsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemWindowsActionPerformed
        // Muda a aparencia para Windows
        aparencias(3);
    }//GEN-LAST:event_menuItemWindowsActionPerformed

    private void menuItemMetalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuItemMetalActionPerformed
        // Muda a aparencia para Windows
        aparencias(0);
    }//GEN-LAST:event_menuItemMetalActionPerformed

    private void jButtonSairToobarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSairToobarActionPerformed
        confirmarSaidaNoAlgoritmo();
    }//GEN-LAST:event_jButtonSairToobarActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        irParaLinha();
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        buscaPalavra();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        veriricarAlgoritmo();

        progC.construirC(); // constroi o programa C

        ConversorParaLC dialog = new ConversorParaLC(new javax.swing.JFrame(), true, progC.getProgramaC());
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                //System.exit(0);
            }
        });
        dialog.setVisible(true);

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem3ActionPerformed
        // TODO add your handling code here:
        FormAjuda dialog = new FormAjuda(new javax.swing.JFrame(), true);

        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                //System.exit(0);
            }
        });
        dialog.setVisible(true);
    }//GEN-LAST:event_jMenuItem3ActionPerformed

    private void MenuSelecionarTudoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSelecionarTudoActionPerformed
        // TODO add your handling code here:
        TextPaneCode.requestFocus();
        TextPaneCode.selectAll();
    }//GEN-LAST:event_MenuSelecionarTudoActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        veriricarAlgoritmo();

        progJava.construirJava(); // constroi o programa JAVA

        ConversorParaLJava dialog = new ConversorParaLJava(new javax.swing.JFrame(), true, progJava.getProgramaJava());
        dialog.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                //System.exit(0);
            }
        });
        dialog.setVisible(true);

        //JOptionPane.showMessageDialog(null, progJava.getProgramaJava());
    }//GEN-LAST:event_jButton2ActionPerformed

    private void btEmailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btEmailActionPerformed
        codigo = TextPaneCode.getText();
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                // new Envio_Email().setVisible(true);
            }
        });

    }//GEN-LAST:event_btEmailActionPerformed

    private void menuExemploAlgoritmoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExemploAlgoritmoActionPerformed
        // TODO add your handling code here:
        if (fileManager2.openFileWindow(this) != null) {
            LerExemploAlgol(fileManager2.getFileName());
        }
    }//GEN-LAST:event_menuExemploAlgoritmoActionPerformed

    private void MenuFicheiro1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuFicheiro1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_MenuFicheiro1ActionPerformed

    private void btDicaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btDicaActionPerformed
        // TODO add your handling code here:
        dica = new DicasdoDia();
        JOptionPane.showMessageDialog(null, dica.dicaDia(), "Dica do dia...", WIDTH);
    }//GEN-LAST:event_btDicaActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        jMenuItem3ActionPerformed(null); // executa o form de ajuda
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new EditorCAlg().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton ButtonEditarAvancar;
    private javax.swing.JButton ButtonEditarColar;
    private javax.swing.JButton ButtonEditarCopiar;
    private javax.swing.JButton ButtonEditarCortar;
    private javax.swing.JButton ButtonEditarRecuar;
    private javax.swing.JButton ButtonEditarReformatar;
    private javax.swing.JButton ButtonFicheiroAbrir;
    private javax.swing.JButton ButtonFicheiroGuardar;
    private javax.swing.JButton ButtonFicheiroNovo;
    private javax.swing.JButton ButtonProgramaCorrer;
    private javax.swing.JButton ButtonProgramaParar;
    private javax.swing.JButton ButtonProgramaVerificar;
    private javax.swing.JMenu MenuAjuda;
    private javax.swing.JMenuItem MenuAjudaAcerca;
    private javax.swing.JMenu MenuAlgoritmo;
    private javax.swing.JMenuItem MenuAlgoritmoCorrer;
    private javax.swing.JMenuItem MenuAlgoritmoVerificar;
    private javax.swing.JMenu MenuEditar1;
    private javax.swing.JMenuItem MenuEditarColar;
    private javax.swing.JMenuItem MenuEditarCopiar;
    private javax.swing.JMenuItem MenuEditarCortar;
    private javax.swing.JMenuItem MenuEditarRedo;
    private javax.swing.JMenuItem MenuEditarReformatar;
    private javax.swing.JMenuItem MenuEditarUndo;
    private javax.swing.JMenu MenuEditor;
    private javax.swing.JCheckBoxMenuItem MenuEditorCheckSyntax;
    private javax.swing.JMenuItem MenuEditorFonte;
    private javax.swing.JMenu MenuFicheiro1;
    private javax.swing.JMenuItem MenuFicheiroAberto1;
    private javax.swing.JMenuItem MenuFicheiroAberto2;
    private javax.swing.JMenuItem MenuFicheiroAberto3;
    private javax.swing.JMenuItem MenuFicheiroAbrir1;
    private javax.swing.JMenuItem MenuFicheiroGuardar;
    private javax.swing.JMenuItem MenuFicheiroGuardarComo;
    private javax.swing.JMenuItem MenuFicheiroNovo1;
    private javax.swing.JMenuItem MenuFicheiroSair1;
    private javax.swing.JMenuItem MenuProgramaParar;
    private javax.swing.JMenuItem MenuSelecionarTudo;
    public javax.swing.JTextPane TextPaneCode;
    private javax.swing.JToolBar TooBarEditar;
    private javax.swing.JToolBar ToolBarFicheiro;
    private javax.swing.JToolBar ToolBarPrincipal;
    private javax.swing.JToolBar ToolBarPrograma;
    private javax.swing.JButton btDica;
    private javax.swing.JButton btEmail;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButtonSairToobar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenuBar jMenuBar2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToolBar jToolBarConversao;
    private javax.swing.JMenuItem menuExemploAlgoritmo;
    private javax.swing.JMenuItem menuItemMetal;
    private javax.swing.JMenuItem menuItemNimbus;
    private javax.swing.JMenuItem menuItemWindows;
    private javax.swing.JMenuItem menuMudarConsola;
    private java.awt.PopupMenu popupMenu1;
    private javax.swing.JScrollPane scrollCodeEditor;
    private javax.swing.JScrollPane scrollInfo;
    private javax.swing.JScrollPane scrollMonitor;
    private javax.swing.JScrollPane spInfo;
    private javax.swing.JScrollPane spOutput;
    private javax.swing.JTabbedPane tpUnderCodeEditor;
    // End of variables declaration//GEN-END:variables

//////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////
    private void setTextChanged(boolean flag) {
        textChanged = flag;
        if (flag) {
            this.setTitle(this.getTitle() + "*");
        }
    }

    private boolean getTextChanged() {
        return textChanged;
    }

    private void ActualizarMenuFicheiro(String newFile) {
        this.editorProperties.SetLoadFileName(newFile);
        MenuFicheiroAberto1.setText(editorProperties.GetProperty("file1"));
        MenuFicheiroAberto2.setText(editorProperties.GetProperty("file2"));
        MenuFicheiroAberto3.setText(editorProperties.GetProperty("file3"));
        editorProperties.Save();
    }

    private void NovoFicheiro() {

        if (getTextChanged()) //  verifica se o texto foi alterado
        {
            int action = Message.Confirm("DESEJA SALVAR O ALGORITMO ACTUAL\n" + fileManager.getFileName());

            if (action == JOptionPane.CANCEL_OPTION) {
                return;
            }

            if (action == JOptionPane.YES_OPTION) {
                this.MenuFicheiroGuardarActionPerformed(null);
            }

        }
        fileManager.clearFileName();
        textChanged = false;

        data_hora();

        TextPaneCode.setText(descAlgo);

        this.setTitle(this.TITLE + " - " + fileManager.getFileName());
    }
    //---------------------------------------------------------------------------

    public void LerFicheiro(String fileName) {
        if (fileManager.FileExists(fileName)) {
            TextPaneCode.setText(fileManager.ReadFile(fileName));
            //colocar o cursor no inicio do texto
            TextPaneCode.setCaretPosition(1);
            // actualizar o menu
            ActualizarMenuFicheiro(fileName);
            textChanged = false;
            this.setTitle(this.TITLE + " - " + fileManager.getFileName());
        }
    }

    //---------------------------------------------------------------------------
    public void LerExemploAlgol(String fileName) {
        if (fileManager.FileExists(fileName)) {
            TextPaneCode.setText(fileManager.ReadFile(fileName));
            //colocar o cursor no inicio do texto
            TextPaneCode.setCaretPosition(1);
            // actualizar o menu
            ActualizarMenuFicheiro(fileName);
            textChanged = false;
            this.setTitle(this.TITLE + " - " + fileManager.getFileName());
            objeto_propriedade.buscaNome_linha(fileManager.getFileName(), TextPaneCode.getSelectionEnd());
        }
    }
    //---------------------------------------------------------------------------

    public void GuardarFicheiro(String fileName) {
        //se conseguir salvar
        if (fileManager.saveFileUpdate(TextPaneCode.getText())) {
            textChanged = false;
            this.setTitle(this.TITLE + " - " + fileManager.getFileName());
            objeto_propriedade.buscaNome_linha(fileManager.getFileName(), TextPaneCode.getSelectionEnd());
        }
    }
    //---------------------------------------------------------------------------

    public void GuardarFicheiroComo(String fileName) {
        //se conseguir salvar
        if (fileManager.saveFile(this, fileManager.getFileName(), TextPaneCode.getText())) {
            textChanged = false;
            this.setTitle(this.TITLE + " - " + fileManager.getFileName());
            ActualizarMenuFicheiro(fileName);
            objeto_propriedade.buscaNome_linha(fileManager.getFileName(), TextPaneCode.getSelectionEnd());
        }
    }

///////////////////////////////////////////////////////////////////////////////
    public void veriricarAlgoritmo() {
        try {
            calend = new Calendario();
            this.tpUnderCodeEditor.setSelectedIndex(1);
            info.Clear();
            info.write("\n" + calend.dataAtual());
            info.write("\nAlgoritmo: " + fileManager.getFileName());
            info.write("\n\nA compilar o algoritmo...");

            intermediario = new Intermediario(TextPaneCode.getText());
            prog = intermediario.getInicio();
            info.write("\nO algoritmo não tem erros da compilação...\n");
            progJava = new construirProgramaJava(TextPaneCode.getText());
            progC = new construirProgramaC(TextPaneCode.getText());
        } catch (LanguageException e) {
            SelectErrorLine(e.line);
            Message.CompileError(e);
            info.write("\n\n---------------------\nERRO DE COMPILAÇÃO: \n"
                    //+ "  LINHA:\n" + e.line + "\n"
                    + "  INSTRUÇÃO:\n\t" + e.codeLine + "\n"
                    + "  ERRO:\n\t" + e.error + "\n"
                    + "  SOLUÇÃO:\n\t" + e.solution + "\n");
            prog = null;
        }
    }

///////////////////////////////////////////////////////////////////////////////
    public void run() {
        try {
            //this.MenuFicheiroGuardarActionPerformed(null);
            console.Clear();
            //mostrar a consola
            tpUnderCodeEditor.setSelectedIndex(0);

            Intermediario.console = console; //David: Isto é preciso, não tirar
            //prog = intermediario.getInicio();
            instruction = prog.getStartNode();

            //David: Ejecutar el llamado a la instruccion falsa temporal
            info.write("\nO Algoritmo está sendo executado. . . ");
            prog.ExecuteSubrutine(new Vector<String>(), null);

            Intermediario.console = null; //David: Isto é preciso, não tirar

            calend = new Calendario();
            info.write("\nO programa terminou com sucesso... ");
            info.write("\n" + calend.dataAtual());
        } catch (LanguageException e) {
            SelectErrorLine(e.line == 0 ? instruction.GetCharNum() : e.line);
            Message.ExecutionError("ERRO DE EXECUÇÃO", e);
            info.write("\n\n---------------------\nERRO DE EXECUÇÃO: \n"
                    //+ "  LINHA:\n\t" + (e.line == 0 ? instruction.GetCharNum() : e.line) + "\n" //DAVID: o valor da linha não é real
                    + "  INSTRUÇÃO:\n\t" + e.codeLine + "\n"
                    + "  ERRO:\n\t" + e.error + "\n"
                    + "  SOLUÇÃO:\n\t" + e.solution + "\n");
            prog = null;
        }
        autoExecute = null;
    }

    public void confirmarSaidaNoAlgoritmo() {
        if (getTextChanged()) //  verifica se o texto foi alterado
        {
            int action = Message.Confirm("DESEJA SALVAR O ALGORITMO ACTUAL\n" + fileManager.getFileName());

            if (action == JOptionPane.CANCEL_OPTION) {
                return;
            }

            if (action == JOptionPane.YES_OPTION) {
                this.MenuFicheiroGuardarActionPerformed(null);
                //guardar as propriedades
                editorProperties.Save();
                //libertar os recursos
                this.dispose();
                System.exit(0);
            }


            if (action == JOptionPane.NO_OPTION) {
                //guardar as propriedades
                editorProperties.Save();
                //libertar os recursos
                this.dispose();
                System.exit(0);
            }

        } else {
            //guardar as propriedades
            editorProperties.Save();
            this.dispose();
            System.exit(0);
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////
    //--------------- Retornar componentes ------------------------------------
    public JTextPane getEditorTexto() {
        return TextPaneCode;
    }
    //--------------- Retornar componentes ------------------------------------

    public void getEditorTextoFocus() {
        TextPaneCode.requestFocus();
    }
    //------------------------------------------------------------------------

    public JScrollPane getScrolEditorTexto() {
        return scrollCodeEditor;
    }
    // Linhas
    //------------------------Mudar a component ------------------------------

    public void setScrolEditorTexto(JScrollPane scroll) {
        jSplitPane2.setTopComponent(scroll);
    }

    //------------------------Mudar a component ------------------------------
    public void setEditorTexto(JTextPane texto) {
        TextPaneCode = texto;
    }

    /**
     * Retorna o numero total de linhas do TextPane
     */
    public int getTotalLinhas() {
        int numeroLinhas = 0;
        for (int i = 0; i < TextPaneCode.getText().length(); i++) {
            if (TextPaneCode.getText().charAt(i) == '\n') {
                numeroLinhas++;
            }
        }
        return numeroLinhas + 1;
    }

    //==========================================================================
    private void irParaLinha() {
        if ((frameLine == null) || (!frameLine.isShowing())) {

//		bool que verifica se a pessoa quer fazer a busca desde o inicio				
            frameLine = new JFrame("Ir para a linha...");
            Container Lexcontainer = new Container();

            texto_busca = new JTextField(15);
            botao_buscaProximo = new JButton("Ir");

            Lexcontainer = frameLine.getContentPane();
            Lexcontainer.setLayout(new FlowLayout());

            Lexcontainer.add(texto_busca);
            Lexcontainer.add(botao_buscaProximo);
            botao_buscaProximo.addActionListener(
                    new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    int contadorDeLinhas = 0;
                    int caretPos = 0;

                    if (verificaDesdeInicio == true) {
                        TextPaneCode.setCaretPosition(0);
                        verificaDesdeInicio = false;
                        desdeInicio.setSelected(false);
                        desdeAgora.setSelected(true);
                    }
//						int k = 0;
                    if (texto_busca.getText().length() == 0) {
                        JOptionPane.showMessageDialog(frameLine,
                                "O CAMPO NÃO PODE ESTAR VAZIO!",
                                VERSION, JOptionPane.ERROR_MESSAGE);
                    } else {
                        try {

                            int pos = Integer.parseInt(texto_busca.getText().toString());
                            int Total = getTotalLinhas();

                            if ((pos <= Total) && (pos > 0)) {
                                for (int i = 0; i < TextPaneCode.getText().length(); i++) {
                                    if (TextPaneCode.getText().charAt(i) == '\n') {
                                        contadorDeLinhas++;
                                        if (contadorDeLinhas == pos) {
                                            break;
                                        }
                                    } else {
                                        caretPos++;
                                    }
                                }
                                if (pos == 1) {
                                    TextPaneCode.setCaretPosition(0);
                                } else if (pos == Total) {
                                    TextPaneCode.setCaretPosition(caretPos);
                                } else {
                                    TextPaneCode.setCaretPosition(caretPos + contadorDeLinhas - 1);
                                }

                                frameLine.setVisible(false);
                                TextPaneCode.requestFocus();
                            } else {
                                JOptionPane.showMessageDialog(frameLine,
                                        "A linha da busca nao existe!",
                                        VERSION, JOptionPane.ERROR_MESSAGE);
                            }
                        } catch (NumberFormatException numero) {
                            JOptionPane.showMessageDialog(frameLine,
                                    "Formato de numero invalido!",
                                    VERSION, JOptionPane.ERROR_MESSAGE);
                        }
                    }//fim else
                }
            });

            frameLine.setSize(270, 70);
            Lexcontainer.setVisible(true);
            frameLine.setResizable(false);
            frameLine.setLocationRelativeTo(frameLine);
            frameLine.setAlwaysOnTop(true);
            frameLine.setVisible(true);
            //setando o botao como submit do formulario de busca
            texto_busca.getRootPane().setDefaultButton(botao_buscaProximo);
        } else {
            frameLine.requestFocus();
        }

    }

    //--------------------------------------------------------------------------
    /**
     * Classe que realiza busca de palavras
     */
    public void buscaPalavra() {
        if ((frameBusca == null) || (!frameBusca.isShowing())) {
            //bool que verifica se a pessoa quer fazer a busca desde o inicio				
            frameBusca = new JFrame("Procurar...");
            Container Lexcontainer = new Container();

            texto_busca = new JTextField(20);
            final JLabel labelPesquisa = new JLabel("");
            labelPesquisa.setForeground(Color.red);
            botao_buscaProximo = new JButton("    Buscar    ");
            desdeAgora = new JRadioButton("A partir deste ponto                            ");
            desdeInicio = new JRadioButton("Desde o inicio                                 ");
            desdeAgora.setToolTipText("Realiza a busca a partir da posicao actual do cursor");
            desdeInicio.setToolTipText("Realiza a busca desde o inicio do codigo");
            desdeInicio.setSelected(true);
            verificaDesdeInicio = true;
            Lexcontainer = frameBusca.getContentPane();


            JPanel painel_head = new JPanel();
            painel_head.setLayout(new FlowLayout());
            painel_head.setBackground(Color.DARK_GRAY);
            JLabel lb = new JLabel("Encontrar");
            lb.setForeground(Color.white);
            painel_head.add(lb);
            painel_head.add(texto_busca); //153,180,209


            JPanel painel_body = new JPanel();
            painel_body.setLayout(new BorderLayout());
            painel_body.add(desdeAgora, BorderLayout.NORTH);
            painel_body.add(desdeInicio, BorderLayout.SOUTH);


            JPanel painel_butao = new JPanel();
            painel_butao.setLayout(new BorderLayout());
            painel_butao.add(botao_buscaProximo, BorderLayout.CENTER);

            JPanel painel_feedback = new JPanel();
            painel_feedback.setLayout(new FlowLayout());
            painel_feedback.add(labelPesquisa);

            Lexcontainer.setLayout(new FlowLayout());
            Lexcontainer.setBackground(Color.white);
            Lexcontainer.add(painel_head);
            Lexcontainer.add(painel_body);
            Lexcontainer.add(painel_feedback);
            Lexcontainer.add(painel_butao);
            Box.createGlue();

            botao_buscaProximo.addActionListener(
                    new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    labelPesquisa.setText("");
                    if (verificaDesdeInicio == true) {
                        TextPaneCode.setCaretPosition(0);
                        verificaDesdeInicio = false;
                        desdeInicio.setSelected(false);
                        desdeAgora.setSelected(true);
                    }

                    int k = 0;
                    if (texto_busca.getText().length() == 0) {
                        JOptionPane.showMessageDialog(frameBusca,
                                "O campo de busca está vazio!",
                                VERSION, JOptionPane.WARNING_MESSAGE);
                    } else {
                        int posAtual = TextPaneCode.getCaretPosition(), posInicial = 0;
                        String palavra = texto_busca.getText().toString();

                        for (int i = posAtual; i < TextPaneCode.getText().length(); i++) {
                            if (palavra.charAt(0) == TextPaneCode.getText().charAt(i)) {
                                posInicial = i;
                                k = i;
                                for (int j = 0; j < palavra.length(); j++) {
                                    if (palavra.charAt(j) == TextPaneCode.getText().charAt(k)) {
                                        k++;
                                    } else {
                                        k = -1;
                                        labelPesquisa.setText("String nao encontrada");
                                        break;
                                    }
                                }
                                if (k != -1) {
                                    if (previous.equals(TextPaneCode.getSelectedText())) {
                                        labelPesquisa.setText("String nao encontrada");
                                    } else {
                                        labelPesquisa.setText("");
                                    }

                                    TextPaneCode.select(posInicial, k);
                                    previous = TextPaneCode.getSelectedText();
                                    break;
                                }
                            } else {
                                labelPesquisa.setText("String nao encontrada");
                            }
                        }
                    }//fim else
                }
            });
            desdeAgora.addActionListener(
                    new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    verificaDesdeInicio = false;
                    desdeInicio.setSelected(false);
                    desdeAgora.setSelected(true);
                }
            });
            desdeInicio.addActionListener(
                    new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    verificaDesdeInicio = true;
                    desdeInicio.setSelected(true);
                    desdeAgora.setSelected(false);
                }
            });

            frameBusca.setSize(350, 160);
            Lexcontainer.setVisible(true);
            frameBusca.setResizable(false);
            frameBusca.setLocationRelativeTo(frameBusca);
            frameBusca.setAlwaysOnTop(true);
            frameBusca.setVisible(true);
            texto_busca.getRootPane().setDefaultButton(botao_buscaProximo);
        } else {
            frameBusca.requestFocus();
        }
    }

    public void procuraPalavra() {

        /* Alterada no dia 11 de Março de 2014
            
         int k = 0;
         if( txtbuscador.getText().length() == 0 ){
         JOptionPane.showMessageDialog(null,"O campo de busca está vazio!",
         VERSION,JOptionPane.WARNING_MESSAGE);
         }					
         else{
         TextPaneCode.setCaretPosition(0);
         TextPaneCode.requestFocus();
         int posAtual = TextPaneCode.getCaretPosition(), posInicial = 0;
         String palavra = txtbuscador.getText().toString();
                                
         for(int i = posAtual; i < TextPaneCode.getText().length(); i++){								 	
         if(palavra.charAt(0) == TextPaneCode.getText().charAt(i)){
         posInicial = i;
         k = i;
         for(int j=0; j<palavra.length(); j++){								 				
         if(palavra.charAt(j) == TextPaneCode.getText().charAt(k)){
         k++;								 													 						
         }
         else{
         k = -1;		
         break;
         }								 				
         }
         if( k != -1){						 			
					
         TextPaneCode.select( posInicial, k);
         previous = TextPaneCode.getSelectedText(); 							 											 										 				
         break;						 				
         }
                                          
         }
					 		
         }						 	
         }//fim else
         if( k == -1){
         JOptionPane.showMessageDialog(null,"A palavra não foi encontrada!",
         VERSION,JOptionPane.WARNING_MESSAGE);
                                            
         }   
         */
    }

    private void data_hora() {

        calend = new Calendario();

        //David:inicio
        descAlgo = ""
                //+  "/*ALGORITMO: \"Nome do algoritmo\""
                //+ "\n--AUTOR: "+System.getProperty("user.name")+""
                //+ "\n--DATA E HORA:"+calend.dataAtual()
                //+ " */ \n"
                //+ "procedimento idade(inteiro a[3])  \n"
                //+ "  mostre \"idade \\n\"\n"
                //+ "   inteiro a\n"
                //+ "  escreva a\n"
                //+ "   a[0] <- 10 \n"
                //+ "  escreva a[0]+a[1]+a[2]\n"
                //+ "fimprocedimento\n"
                + "classe pessoa\n"
                + "   literal nome\n"
                + "   inteiro idade\n"
                + "fimclasse\n"
                + "\n"
                + "inicio\n"
                + "  escreva \"Codigo principal \\n\"\n"
                + "   pessoa b\n"
                + "   b.nome <- \"David\"\n"
                + "   b.idade <- 34\n"
                + "  escreva b.idade\n"
                + "fimalgoritmo \n";

    }
//----------------------AVANCAR E RETROCEDER ---------------------------------
    Botoes_nav obj_RA;

    public class Botoes_nav {

        Botoes_nav() {
            // evento de gravação
            conteudo[0] = TextPaneCode.getText();

            ActionListener gravar = new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    if (!TextPaneCode.getText().equals(conteudo[cont])) {
                        if (cont == limite) {
                            cont = 0;
                            girou = true;
                        }     // se estamos no limite de armazenamento retorna apangando os ultimos valores graudados

                        cont++;     // dou uma nova posicão de armazenamento
                        inicio = cont;
                        fim = cont;
                        ButtonEditarRecuar.setEnabled(true);
                        ButtonEditarAvancar.setEnabled(false);
                        conteudo[cont] = TextPaneCode.getText();   // quardo novo conteudo

                    }
                }
            };

            tempo = new Timer(750, gravar);
        }

        public void eventoRetroceder() {
            tempo.stop();
            if ((cont == 1) && (girou == true)) {
                cont = limite + 1;  // +1 ajuste porque logo abaixo sera descrementado
            }
            cont--;
            TextPaneCode.setText(conteudo[cont]);
            // System.out.println(cont+" retroceder "+ inicio);
            desabilitarInicio(inicio); // controlo no retrov
            tempo.start();
        }

        public void desabilitarInicio(int param) {
            if (girou == false) {
                param = 0;
                inicio = 0;
            }
            if (param == limite) //(cont==param+1)||
            {
                param = 1;
                inicio = 1;
            }
            if ((cont == param) || (girou == true) && (cont == param + 1)) // +1 para no ultimo alteração possivel e a apenas = caso param é o limite
            {
                ButtonEditarRecuar.setEnabled(false);
                //girou=false;
            } else {
                ButtonEditarRecuar.setEnabled(true);
            }

            ButtonEditarAvancar.setEnabled(true);
        }

        public void eventoAvancar() {
            tempo.stop();
            if ((cont == limite) && (girou == true)) {
                cont = 0;
            }

            cont++;
            TextPaneCode.setText(conteudo[cont]);
            //  System.out.println(cont+ " avanco "+ fim);
            desabilitarFim(fim);
            ButtonEditarRecuar.setEnabled(true);
            tempo.start();
        }

        private void desabilitarFim(int param) {
            if (cont == param) {
                ButtonEditarAvancar.setEnabled(false);
            }
        }
        private int cont = 0, inicio = 0, fim = 0, critical = -1;
        private final int limite = 1000; // quantidade de palavras que vai armazenar por vez
        private boolean girou = false;
        private String[] conteudo = new String[limite + 2];  //  mais dois porque começamos  o vecto com 0 e a primeira posicão ñ utilizamos porque consideramos o estado inicial           
        private Timer tempo;
    }
}