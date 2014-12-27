package Portugol.Language.Analisador;

import Portugol.Language.Criar.NodeInstruction;
import Portugol.Language.Utilitario.LanguageException;
import java.util.StringTokenizer;
import java.util.Vector;
import Portugol.Language.Analisador.Keyword;
import java.util.Enumeration;
/**
 * @author Augusto Bilabila (23-12-2011)
 */
public class Registo {
    
    String programa=""; 
    public Vector registo = new Vector();
    public int qtdRegisto = 0;
    
    public Vector VarRegisto1 = new Vector(); // guarda as variaveis do primeiro registo
    public Vector VarRegisto2 = new Vector(); // guarda as variaveis do segundo registo
    
    public Vector VarTipo1 = new Vector(); // guarda as variaveis que acede o primeiro registo
    public Vector VarTipo2 = new Vector(); // guarda as variaveis que acede o segundo registo
    
    public Registo (String registo){
        programa = registo;
    }   
    
  //----------------------------------------------------------------------------  
    public String primeiroTratamento (){
        programa = programa.replace("registo ", "/*\nregisto ");
        programa = programa.replace("fimregisto", "-*/");
        return programa;
    }
    //--------------------------------------------------------------------------
    public String UltimoTratamento (){
        programa = programa.replace(programa.substring(programa.indexOf("/*\nregisto "), programa.indexOf("-*/")+3),"");
       
        return programa;
    }
    //----------------------------------------------------------------------------  
    public String SegundoTratamento () throws LanguageException{
        
        int cont=0;
        programa = primeiroTratamento ();
        
        obterRegisto(); // detem os registos
        
        int inic = programa.indexOf("inicio");
        String str = (programa.substring(inic+7, programa.length())).trim();  
        
      if(str.indexOf(""+registo.get(0))!=-1){
         if(qtdRegisto>1){
             
             int comp1 = (""+registo.get(0)).length();
             int comp2 = (""+registo.get(1)).length();
             
             int ind1 = str.indexOf(""+registo.get(0));
             int ind2 = str.indexOf(""+registo.get(1)); 
             
             int t = 0, t1 = 0;
             
     
             do {  
                 
             if(ind1!=-1 && ind2!=-1 && cont==2 ){
                 for (int i = t+2; i<str.length();i++ ){ 
                  
                     if(str.charAt(i)=='\n'){
           
                        VarTipo1.add(str.substring(ind1+comp1, i).trim()); // guarda o nome
                        i = str.length(); cont = 1;
                     } 
                 }
                  programa = programa.replace(""+registo.get(0), "//");
                 } else
             
               if((ind2!=-1) && (ind1 > ind2) && (cont==1 || cont==0)){
                 for (int i = 0; i<str.length();i++ ){
                     if(str.charAt(i)=='\n'){
                    
                        VarTipo2.add(str.substring(ind2+comp2, i).trim()); // guarda o nome
                        cont = 2; t = i; i = str.length();
                     } 
                 }
                  programa = programa.replace(""+registo.get(1), "//");
 
                 }
                 if(ind1 < ind2) cont = 1;
                 
             }while(cont!=1);
           
             // Segunda Avaliacao   --------------------------------------------------- 
             if(ind1!=-1 && ind1 < ind2 && cont==1 ){
                 for (int i = 0; i<str.length();i++ ){ 
                     if(str.charAt(i)=='\n'){
                        VarTipo1.add(str.substring(ind1+comp1, i).trim()); // guarda o nome
                        t1 = i; i = str.length(); cont = 2;
                     } 
                 }
                  programa = programa.replace(""+registo.get(0), "//");
                 } 

               if(ind2!=-1 && cont==2){
                 for (int i = t1 + 2; i<str.length();i++ ){ 
                     
                     if(str.charAt(i)=='\n'){
                        
                        VarTipo2.add(str.substring(ind2+comp2, i).trim()); // guarda o nome
                        i = str.length();
                     } 
                 }
                  programa = programa.replace(""+registo.get(1), "//");

                 }               
             //----------------------------------------------------------------------------
             } 
            
        if(qtdRegisto==1){
                 
                 
                 int comp = (""+registo.get(0)).length();
                 int ind = str.indexOf(""+registo.get(0));
                
                 
                 if(ind!=-1){
                 for (int i = 0; i<str.length();i++ ){
                     if(str.charAt(i)=='\n'){
                        VarTipo1.add(str.substring(ind+comp, i).trim()); // guarda o nome
                        i = str.length();
                     } 
                 }
                  programa = programa.replace(""+registo.get(0), "//");
                 }
             } 
            }
      
        return programa;
    }
  //----------------------------------------------------------------------------  
     public String trechoRegisto (){
        String trecho = programa.substring(programa.indexOf("registo "), programa.indexOf("inicio"));
        return trecho;
    }
     
   //---------------------------------------------------------------------------
    public void obterRegisto () throws LanguageException{
        
        String str = trechoRegisto ().trim();
        String aux2=""; int cont=0,cont3 = 0,f = 0,f1 = 0, contaRegisto = 0;
        
        for (int i=0; i<str.length();i++){
            
            aux2 += ""+str.charAt(i);
            
            int inic = aux2.indexOf("registo",cont3);
            
            
             // Verifica se tem algum registo
                if(inic!= -1 && cont == 0){
                    cont=1; cont3 = i+8; //aux2 = aux2.replace("registo", "");
                }
                               
              //  JOptionPane.showMessageDialog(null,"I = "+i+" Inic = "+inic+" cont = "+cont);        
            // Verifica o primeiro registo
            if (cont == 1){
                if(str.charAt(i)=='\n'){ cont=2;
                       adicionarRegisto (str.substring(inic+8, i).trim());
                       contaRegisto++;
                }                
            } 
            
              if(inic!= -1 && cont == 2){
                    cont=3;
                }
            // Verifica o segundo registo caso exista
                if (cont == 3){ 
                   if(str.charAt(i)=='\n')
                   { cont=4;  f ++;
                       if(f==1){
                          adicionarRegisto (str.substring(inic+8, i).trim());
                          contaRegisto++;
                       }
                       
                   }                             
                }
        }
        // verifica os nomes dos registos
        for(int i=0; i<contaRegisto;i++){
            String nome = ""+registo.get(i);
            
            VerificarRegisto(nome); // se o nome esta bem escrito
        } 
        VerificarRegistoExistente(contaRegisto); // se o nome este nome ja existe
    }
    
    //---------------------------------------------------------------------------
    public void adicionarRegisto (String reg){       
        registo.add(reg);    
    }
    
    //--------------------------------------------------------------------------
    public String RegistoGuardado(){       
        return registo.toString();    
    }
    
    //--------------------------------------------------------------------------
    public String VariavelRegistoGuardado(){       
        return VarTipo1.toString()+" "+VarTipo2.toString()+"\nVariaveis 1\n"+VarRegisto1.toString()+"\nVariaveis 2\n"+VarRegisto2.toString();    
    }
    
    //--------------------------------------------------------------------------
    public void VerificarRegisto(String nome) throws LanguageException{       
        
        //verificar se o registo um nome aceitavel
        if( !Variavel.isNameAcceptable(nome))
             throw new LanguageException(                     
                    " IDENTIFICADOR " + nome + " INVÁLIDO :" + Variavel.getErrorNameRegisto(nome),
                    "ALTERE O NOME DO REGISTO " + nome); 
    }
    //--------------------------------------------------------------------------
    public void VerificarRegistoExistente(int qtd) throws LanguageException{       
        
        //verificar se o registo o nome ja existe       
        if(qtd>1)
        for(int i=1;i<qtd;i++){
            String reg = ""+registo.get(0);
            String reg1 = ""+registo.get(i);
            
            if(reg.equals(reg1))
               throw new LanguageException(                     
                    " NÃO É POSSIVEL TER DOIS REGISTOS COM O MESMO NOME",
                    "ALTERE UM DOS NOMES DO REGISTO " + reg); 
        }
        qtdRegisto = qtd;
    }
    
    //--------------------------------------------------------------------------
    public void obterNomesVariaveisRegisto(String nome) throws LanguageException{       
        
        //verificar se o registo um nome aceitavel
        if( !Variavel.isNameAcceptable(nome))
             throw new LanguageException(                     
                    " IDENTIFICADOR " + nome + " INVÁLIDO :" + Variavel.getErrorNameRegisto(nome),
                    "ALTERE O NOME DO REGISTO " + nome); 
    }
    //--------------------------------------------------------------------------
    
    public void ObterVariaveisRegisto(){
        int conta = 0;
        int inic = programa.indexOf("registo");
        int fim = programa.indexOf("inicio");

        String registo="";
        
        if(inic!=-1){
           registo = (programa.substring(inic,fim)).trim();
        }
        
        
        StringTokenizer st = new StringTokenizer(registo,"\n\r");
        NodeInstruction node=null;
        String linha="";
        
        while (st.hasMoreTokens()) {
            linha = st.nextToken();
            
            //instruction = CodeLine.GetNormalized(linha);
            node = new NodeInstruction(linha,0,0);
            //o case tem identacao 2
            if (node.GetType() == Keyword.REGISTO){
                 conta ++;
            }
            
            if(conta == 1){
                    if(node.GetType() == Keyword.DEFINIR) VarRegisto1.add(node.GetText());
                }
                
                if(conta == 2){
                    if(node.GetType() == Keyword.DEFINIR) VarRegisto2.add(node.GetText());
                }
     //       if(node.GetType() == Keyword.DEFEITO) level--;      VarRegisto1   
    }
}
    public String declararVariaveis () throws LanguageException{
        //
        Enumeration vEnum1 = VarRegisto1.elements(); 
	Enumeration vEnum2 = VarRegisto2.elements();
        String str1 = "",str2 = "";
        String st1 = "",st2 = "";
        NodeInstruction node=null;
        
        if(VarTipo1.size()!=0){
            str1 = ""+VarTipo1.get(0);      
            
	while(vEnum1.hasMoreElements()) {
            String var = ""+vEnum1.nextElement();
            var = var.replace(" ", " "+str1+".");            
            st1 += var+"\n";
	} 
        }
        
        if(VarTipo2.size()!=0){ 
            str2 = ""+VarTipo2.get(0);
        while(vEnum2.hasMoreElements()) {
            String var = ""+vEnum2.nextElement();
            var = var.replace(" ", " "+str2+".");            
            st2 += var+"\n";
	} 
        }
    
     if(!st1.equals("")){
          if(!st2.equals(""))
              st1 += st2;
          programa = programa.replace("inicio","inicio\n"+st1+"\n");
     }else 
        if(!st2.equals(""))
          programa = programa.replace("inicio","inicio\n"+st2+"\n"); 
     
     return programa;
    }
    
}