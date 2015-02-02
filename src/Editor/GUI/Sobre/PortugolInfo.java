package Editor.GUI.Sobre;

import Editor.GUI.EditorCAlg;

public class PortugolInfo {
    public static String VERSION = "Versão:2.0 \t(c)Augusto Bilabila e David Silva Barrera";
    
    public PortugolInfo() {
    }
    public static String getInformation(){
        StringBuffer str = new StringBuffer();
        
            str.append("\n\n\tTÍTULO              : " + EditorCAlg.TITLE);
            str.append("\n\tDATA DE ACTUALIZAÇÃO  : " + EditorCAlg.DATE );            
            str.append("\n");
            str.append("\n\tUTILIZADOR          : " + System.getProperty("user.name"));
            str.append("\n\tSISTEMA OPERATIVO   : " + System.getProperty("os.name") +  " VERSÃO " + System.getProperty("os.version"));
            str.append("\n\tLOCAL DE EXECUÇÃO   : " + System.getProperty("user.dir"));
            str.append("\n\tLOCAL DO UTILIZADOR : " + System.getProperty("user.home"));            
            
            return str.toString();
    }
    
}
