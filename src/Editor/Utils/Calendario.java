/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Editor.Utils;
import java.util.Calendar;

/**
 *
 * @author Augusto Bilabila (30-01-2012)
 */
public class Calendario {
    
   String  atual ="";
    
    public Calendario ()
    {
            Calendar now = Calendar.getInstance();
            String mes = ""+(now.get(Calendar.MONTH)+1);
            String me = "";
            switch (mes)
            {
                case "1": me = "Janeiro";  break;
                case "2": me = "Fevereiro";break;
                case "3": me = "Março";    break;  
                case "4": me = "Abril";    break;
                case "5": me = "Maio";     break;
                case "6": me = "Junho";    break;
                case "7": me = "Julho";    break;
                case "8": me = "Agosto";   break; 
                case "9": me = "Setembro"; break;
                case "10": me = "Outubro"; break;
                case "11": me = "Novembro";break;
                case "12": me = "Dezembro";break;
              
            }
            
            atual = " "+now.get(Calendar.DATE)+" de "+me+" de "+now.get(Calendar.YEAR)+" | "+horaAtual (""+now.getTime());
    }

 private String horaAtual (String h){
     String a = ""; int conta=0;
     for(int i=0; i<h.length();i++){              
        if(conta==3) a += h.charAt(i);
        if(h.charAt(i)==' ') conta++;
        if(conta==4) i = h.length();
     }
      return a.trim();
  }
 //-----------------------------------------------------------------------------
  public String dataAtual (){            
      return atual;
  }
  public String dia (){  
      return (atual.substring(0, (atual.indexOf("d")))).trim();
  }
 
}