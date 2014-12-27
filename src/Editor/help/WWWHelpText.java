package Editor.help;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

public class WWWHelpText extends javax.swing.JPanel implements HyperlinkListener{
    public static String VERSION = "";  
    // Variables declaration - do not modify                     
    private javax.swing.JButton btHome;
    private javax.swing.JButton btSair;
    private javax.swing.JButton btPrevious;
    private javax.swing.JButton btNext;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JEditorPane txtHelp;
    private javax.swing.JLabel txtUrl;
    private String home;
    Vector urlNext; // vector com links
    Vector urlPrevious;// vector com links
    
    
    
    
    
    
    public WWWHelpText(String file) {
        super();
        
        initComponents();
        urlNext = new Vector();
        urlPrevious = new Vector();
        txtUrl.setVisible(false);
        setPage(file);
        home = file;
        
    }
    
    public void setPage(String file){
        try {            
            txtUrl.setText(ClassLoader.getSystemResource(file).toString());
            txtHelp.setPage(ClassLoader.getSystemResource(file));
        } catch(IOException ioe) {
            txtHelp.setText("ERROR: " + ioe );
        }
    }
     public void setURLPage(String file){
        try {            
                txtHelp.setPage( new URL(file));
                txtUrl.setText(file);
        } catch(IOException ioe) {
            txtHelp.setText("ERROR: " + ioe );
        }
    }
    
    public void hyperlinkUpdate(HyperlinkEvent event) {
        if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                urlPrevious.add(txtUrl.getText());
                setURLPage(event.getURL().toString());         
        }
    }
    
    
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        btHome = new javax.swing.JButton();
        btSair = new javax.swing.JButton();
        btPrevious = new javax.swing.JButton();
        btNext = new javax.swing.JButton();
        txtUrl = new javax.swing.JLabel();
        txtHelp = new javax.swing.JEditorPane();
        
        txtHelp = new javax.swing.JEditorPane();
        txtHelp.setEditable(false);
        txtHelp.addHyperlinkListener(this);
        JScrollPane scrollPane = new JScrollPane(txtHelp);
        add(scrollPane);
        
        setLayout(new java.awt.BorderLayout());
        jPanel1.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
       
        btPrevious.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/www/previous.png")));
        btPrevious.setPreferredSize(new java.awt.Dimension(80, 35));
       // btPrevious.setText("Anterior");
        btPrevious.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btPreviousAction(evt);
            }
        });        
        jPanel1.add(btPrevious);
        //-----------------------------------------------------------------------
         
       //-----------------------------------------------------------------------
        btHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/www/home.png")));
        btHome.setPreferredSize(new java.awt.Dimension(110, 35));
        btHome.setText("Inicio");
        btHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btHomeAction(evt);
            }
        });        
        jPanel1.add(btHome);
        //-----------------------------------------------------------------------
        btNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/www/next.png")));
        btNext.setPreferredSize(new java.awt.Dimension(80, 35));
        //btNext.setText("seguinte");
        btNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btNextAction(evt);
            }
        });        
        jPanel1.add(btNext);
        //-----------------------------------------------------------------------
      //  btSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icons/www/Exit.png")));
       // btSair.setPreferredSize(new java.awt.Dimension(80, 35));
        
       // btSair.addActionListener(new java.awt.event.ActionListener() {
          //  public void actionPerformed(java.awt.event.ActionEvent evt) {
              //  btSairAction(evt);
            //}
       // });        
        //jPanel1.add(btSair);
        //-----------------------------------------------------------------------
        
        jPanel1.add(txtUrl);
        add(jPanel1, java.awt.BorderLayout.NORTH);
        //para ocupar todo o ecra
        add(txtHelp, java.awt.BorderLayout.CENTER);
    }
    private void btHomeAction(java.awt.event.ActionEvent evt) {                                       
      setPage(home);
    }
    
    private void btNextAction(java.awt.event.ActionEvent evt) {                                       
        if( ! urlNext.isEmpty()){            
            //adicionar ao previous que esta na string URL
            urlPrevious.add(txtUrl.getText());
            //import buscar o ultimo ao next
            String link = (String) urlNext.remove(urlNext.size()-1);                        
            //actualizar a pagina
            
            setURLPage(link);
       }
 
    }
    
    private void btSairAction(java.awt.event.ActionEvent evt) {                                       
         
    }
    
    private void btPreviousAction(java.awt.event.ActionEvent evt) {                                       
        if( ! urlPrevious.isEmpty()){            
            urlNext.add(txtUrl.getText());
            String link = (String) urlPrevious.remove(urlPrevious.size()-1);                                    
            setURLPage(link);
       }
    }
        
}
