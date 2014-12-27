package Conversor;
/**
 * @author Augusto Bilabila 26-10-2011
 */

import Portugol.Language.Criar.NodeInstruction;
import java.util.StringTokenizer;
import Portugol.Language.Analisador.Keyword;
import javax.swing.JOptionPane;


public class construirProgramaJava {
    
    private String programaJava="",algol="",Acumularegisto="",GuardaNomeregisto="";
    
    String strInteiro ="",strReal ="",strLiteral ="",strLogico ="",strCaracter ="";
    int contInicio = 0,contProc = 0;
    
    StringBuilder str = new StringBuilder();
    
    int controlaRegisto = 0;
    
    public construirProgramaJava(String algoritmo){
           algol= algoritmo;
           //regist=registo;
    }
    
    public String getAlgoritmo(){
       return algol;
    }
    public String getProgramaJava(){
       return programaJava;
    }
    
    public String TrocaTipos(String stri){
         
        stri = stri.replace("inteiro ", "int ");stri = stri.replace("real ", "float ");
        stri = stri.replace("caracter ", "char "); stri = stri.replace("literal ", "String ");
        stri = stri.replace("logico ", "boolean ");
        
        return stri;
        
    }
   
    public String VariaveisTipo(String var){
        int av = -1;
                av = strInteiro.indexOf(var);
                if(av!=-1) str.append("     "+var+" = teclado.nextInt();\n");
                
                av = strReal.indexOf(var);
                if(av!=-1) str.append("     "+var+" = teclado.nextFloat();\n");
                
                av = strLiteral.indexOf(var);
                if(av!=-1) str.append("     "+var+" = teclado.nextLine();\n");
                
                av = strLogico.indexOf(var);
                if(av!=-1) str.append("     "+var+" = teclado.nextBoolean();\n");
                //
                av = strCaracter.indexOf(var);
                if(av!=-1) str.append("     "+var+" = teclado.nextChar();\n");
       
                
                return ""+str;
    }
    
    //-----------------------------------------------------------------
    public void construirJava(){
        StringTokenizer st = new StringTokenizer(getAlgoritmo(),"\n");
        NodeInstruction node=null;
        String linha="",st1=getAlgoritmo();
        
         //JOptionPane.showMessageDialog(null,"la ja \n"+regist);
        // verifica se existe entrada de dados
        int conta = st1.indexOf("leia "); int conta1 = st1.indexOf("receba ");
       
        if(conta!=-1||conta1!=-1) str.append("import java.util.Scanner;\n");
        
        while (st.hasMoreTokens()) {
            linha = st.nextToken();
            
            //instruction = CodeLine.GetNormalized(linha);
            node = new NodeInstruction(linha,0,0);
          
                String verObjecto = "";
            //o case tem identacao 2
            if (node.GetType() == Keyword.DESCONHECIDO){
                String stri = ""+node.GetText();
                int index = -1; 
                
                String st2 = "";
                if(controlaRegisto == 2 ){
                    for(int i = 0; i<stri.length();i++){
                        st2 += stri.charAt(i);
                        if(stri.charAt(i) == ' '){
                            i = stri.length();
                        }                                              
                    }
   
                index = GuardaNomeregisto.indexOf(st2.trim()); // Verifica se existe alguma declaracao de registo
                
                if(index != -1){
                    str.append("    nome_classe."+node.GetText()+" = objecto.new "+st2.trim()+"();"+"\n\n");
                    verObjecto = node.GetText();
                }else str.append(node.GetText()+"\n"); 
                
                }else if(contInicio != 1) {
                    str.append(node.GetText()+"\n");
                }              
                     
                
                if(contInicio == 1){
                    if(node.GetText().length() > 1 && !(node.GetText().equals(verObjecto))){
                        
                       if(node.GetText().indexOf("/")!=-1) str.append("  "+node.GetText()+"\n");
                       
                       else str.append("  "+node.GetText()+";\n");
                                         
                }
            }
           }
            //-------------------------------------------------
            if (node.GetType() == Keyword.INICIO){
                 contInicio = 1;
                 
                 if(contProc == 1){
                     String st3 = ""+str;
                     st3 = st3.replace("+=+=+", "\npublic class nome_classe {\n\n");
                     str.delete(0,str.length()-1);
                     str.append(st3);
                     contProc = 2;                 
                 }else
                     if (contProc == 0)str.append("\npublic class nome_classe {\n\n");
                 
                 
                 if(controlaRegisto == 2){
                     str.append(Acumularegisto+"\n");
                 }
                 
                 str.append("public static void main(String[] args) {\n");
                 
                 if(conta!=-1)
                 str.append("   Scanner teclado = new Scanner(System.in);\n");
                 
                 if(controlaRegisto == 2){  
                     str.append("   nome_classe objecto = new nome_classe();\n");
                 }
                                  
            }
            //-------------E.C (Decisao)------------------------------------
            if (node.GetType() == Keyword.SE){
                 String stri = ""+node.GetText();
                 stri = stri.replace("se ", " if ");stri = stri.replace("entao", "{");
                 
                 int indice = stri.indexOf("(");
                
                if (indice==-1){
                    stri = stri.replace("if ","if (");
                    stri = stri.replace("{", "){");
                }                 
                 str.append(" "+stri+"\n");
            }
            //................................................................
            if (node.GetType() == Keyword.SENAO){
                 String stri = ""+node.GetText();
                 stri = stri.replace("senao", "else");
                 str.append("  "+stri+"\n");
            }
            
            //----------------- Tipo de dado Primitivo ----------------------
            if (node.GetType() == Keyword.DEFINIR){
                String stri = ""+node.GetText(); 
                
                stri = TrocaTipos(stri); // troca os tipos de dados
                
                stri = stri.replace("<-", "=");
                
                stri += ";";
                
                if(controlaRegisto == 1){
                    Acumularegisto+="   "+stri+"\n";
                }else {
                    str.append("    "+stri+"\n");
                
                    // separa as variaveis por tipo
                    int contInt = stri.indexOf("int "); int contFloat = stri.indexOf("float ");
                    int contString = stri.indexOf("String "); int contChar = stri.indexOf("char ");
                    int contBoolean = stri.indexOf("boolean ");

                    if(contInt!=-1) strInteiro += stri;

                    if(contFloat!=-1) strReal += stri;

                    if(contString!=-1) strLiteral += stri;

                    if(contChar!=-1) strLogico += stri;

                    if(contBoolean!=-1) strCaracter += stri;
                }
                
            }
            //------------------ Print -------------------------------------
            if (node.GetType() == Keyword.ESCREVA){
                String stri = ""+node.GetText(); 
                
                stri = stri.replace("escreva ", "System.out.print ");
                stri = stri.replace("mostre ", "System.out.print ");
                stri = stri.replace(",", "+");
                
                int indice = stri.indexOf("(");
                
                if (indice==-1){
                    stri = stri.replace("System.out.print ","System.out.print(");
                    stri +=")"; 
                } 
                
                stri += ";";
                
                str.append("    "+stri+"\n");
            }
            //--------------- Leitura de dados -------------------------------
            if (node.GetType() == Keyword.LEIA){
                String stri = ""+node.GetText(); 
                String var ="";
                stri = stri.replace("(", "");stri = stri.replace(")", "");
                // obtem as variaveis que estao no leia ou receba
                var = (stri.substring("leia".length()-1)).trim(); // caso seja leia
                var = (stri.substring("receba".length()-1)).trim();// caso seja receba
                
                int ind = var.indexOf(",");
                if(ind != -1){
                    String a ="";
                    for (int i=0;i<var.length();i++){                        
                        if (var.charAt(i) == ','){ 
                            VariaveisTipo(a);a="";
                        }
                         a +=var.charAt(i);
                         a = (a.replace(",", "")).trim();
                    }
                     VariaveisTipo(a);
                }else
                    //verifica se a variavel  
                    VariaveisTipo(var);
                
            }
            
            //-------------E.C (Repeticao)------------------------------------
            if (node.GetType() == Keyword.ENQUANTO){
                 String stri = ""+node.GetText();
                // JOptionPane.showMessageDialog(null,"Ja " +node.GetType());              
                     stri = stri.replace("enquanto ", " while ");stri = stri.replace("faca", "{");
                                
                 str.append(" "+stri+"\n");
            }
            //................................................................
             if (node.GetType() == Keyword.PARA){
                 String stri = ""+node.GetText();
                 stri = stri.replace("para ", " for (");stri = stri.replace("de", "=");
                 
                 String var = stri.substring(stri.indexOf('(')+1,stri.indexOf('='));
                                  
                 stri =stri.replace("ate", "; "+var+" <=");
                 int cont = stri.indexOf("passo");
                 if(cont!=-1) stri =stri.replace("passo", "; "+var+" = "+var+" +");
                 else
                 if(cont==-1) stri += "; "+var+"++";
                 
                 stri += "){";
                 str.append(" "+stri+"\n");
            }
            //................................................................
             if (node.GetType() == Keyword.FAZ){
                 String stri = ""+node.GetText();
                 stri = stri.replace("faca", " do{");
                 
                 str.append(" "+stri+"\n");
            }
             //................................................................
             if (node.GetType() == Keyword.FAZENQUANTO){
                 String stri = ""+node.GetText();
                     stri = stri.replace("enquanto", " }while");
                     stri += ";";
                     
                 str.append(" "+stri+"\n");
            }
            //................................................................
            
            //--------------- Fim do Algoritmo -------------------------------
            if (node.GetType() == Keyword.FIM){
                                            
                    str.append("}\n}\n");                
            }
             //------------------- Calculo -------------------------------
            if (node.GetType() == Keyword.CALCULAR){
                    String stri = ""+node.GetText();
                    stri = stri.replace("<-", "="); 
                    
                    str.append("    "+stri+";\n");               
            }
            
            //------------------- Registo -------------------------------
            if (node.GetType() == Keyword.REGISTO){
                    String stri = ""+node.GetText();
                    stri = stri.replace("registo", " class"); 
                    
                    GuardaNomeregisto+=stri;
                    controlaRegisto = 1; 
                    Acumularegisto+=stri+" {\n";
            }
            //...........................................................
            if (node.GetType() == Keyword.FIMREGISTO){
                    String stri = ""+node.GetText();
                    stri = stri.replace("fimregisto", " }"); 
                    
                    Acumularegisto+=stri+"\n"; // acumula a chaveta de fecho
                    controlaRegisto = 2; // Muda o valor da variavel controladora de registos
                    
            }
            //------------------- Procedimento -------------------------------
            if (node.GetType() == Keyword.PROCEDIMENTO){
                    String stri = ""+node.GetText();
                 
                    if(contProc == 0){
                        stri = stri.replace("procedimento", "+=+=+ public void"); 
                        contProc = 1;
                    }else
                    
                    stri = stri.replace("procedimento", " public void"); 
                    
                    stri = TrocaTipos(stri);
                    
                    str.append(" "+stri+"{\n");
            }            
            //------------------- Funcoes -------------------------------
            if (node.GetType() == Keyword.FUNCAO){
                    String stri = ""+node.GetText();
                 
                    if(contProc == 0){
                        stri = stri.replace("funcao", "+=+=+ public "); 
                        contProc = 1;
                    }else
                    
                    stri = stri.replace("funcao", " public "); 
                    
                    stri = TrocaTipos(stri);
                    
                    str.append(" "+stri+"{\n");
            }            
            
            if (node.GetType() == Keyword.RETORNE){
                    String stri = ""+node.GetText();
                    stri = stri.replace("retorne", " return "); 
                    str.append(" "+stri+";\n");
            }            

            //-----------------------------------------------------------
            
            if ((node.GetType() == Keyword.FIMSE)||
                    (node.GetType() == Keyword.FIMPARA)||
                    (node.GetType() == Keyword.FIMESCOLHE)||
                    (node.GetType() == Keyword.FIMPROCEDIMENTO)||
                    (node.GetType() == Keyword.FIMFUNCAO)||
                    (node.GetType() == Keyword.FIMENQUANTO)){                                           
                     str.append(" }\n");                
            }
           
        }
        
        programaJava = ""+str;
}    
}