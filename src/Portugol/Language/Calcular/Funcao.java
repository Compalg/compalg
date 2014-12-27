/*
 * Classe que trata sobre as funções que o utilizador poderá utilizar...
 * Esta classe é muito com a classe procedimento com o detalhe de retornar
 * sempre um valor específico.
 */
package Portugol.Language.Calcular;

import Portugol.Language.Criar.NodeInstruction;
import Portugol.Language.Analisador.Keyword;
import Portugol.Language.Utilitario.LanguageException;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.swing.JOptionPane;

/**
 * @author Augusto Bilabila 08-02-2012
 */
public class Funcao {
    
    private String algol="",Novoalgol="";
    Vector acumulafuncao = new Vector();
    Vector acumulaTipofuncao = new Vector();
    
    Vector acumulaInst = new Vector();
    Vector acumulaSimb = new Vector();
    int contInst = 0;
    byte controlaFuncao = 0;
    
    String strInteiro ="",strReal ="",strLiteral ="",strLogico ="",strCaracter ="";
    
    String []tipos = {"inteiro","real","logico","literal","caracter"};
    
    Keyword palavraChave;
    
    public Funcao(String programa) { algol= programa; }

    public String getNovoalgol() {
        return Novoalgol;
    }

    public void setNovoalgol(String Novoalgol) {
        this.Novoalgol = Novoalgol;
    }

    public String getAlgol() {
        return algol;
    }

    public void setAlgol(String algol) {
        this.algol = algol;
    }
    
    public String VariaveisTipo(String var){
        int av = -1;
                av = strInteiro.indexOf(var);
                if(av!=-1) return "inteiro ";
                
                av = strReal.indexOf(var);
                if(av!=-1) return "real ";
                
                av = strLiteral.indexOf(var);
                if(av!=-1) return "literal ";
                
                av = strLogico.indexOf(var);
                if(av!=-1) return "logica ";
                //
                av = strCaracter.indexOf(var);
                if(av!=-1) return "char ";
       
                return "N";
    }
    //--------------------------------------------------------------------
    public String RemoverCaract(String str){
       str = str.replace(',', ' ');
       str = str.replace(')', ' ');
       str = str.trim();
       
       return str;
    }
    //--------------------------------------------------------------------
    public String RemoveAspas(String str){
        
        int pos=0,qtd1=0; String str1="";
       for(int i=0;i<str.length();i++){
            String a=""+str.charAt(i);
            if(a.endsWith("\""))
            {qtd1++;}
            
            if(qtd1==1)
            {
               str1+=str.charAt(i); 
            }           
       }
             
                if(str1.length()>1){
                String simbolo = "*%-%*"+contInst;
                str1 = str1.replace("\"", "");
                str = str.replace(str1, simbolo);
                
                acumulaInst.add(str1);
                acumulaSimb.add(simbolo);
                contInst++;
                }
         
       return str;
    }
    //--------------------------------------------------------------------------
    public String DevolveAspas(String str){
       for(int i=0;i<acumulaSimb.size();i++){
           int k = str.indexOf(""+acumulaSimb.get(i)) ;
           if(k!=-1){
               str = str.replace(""+acumulaSimb.get(i), ""+acumulaInst.get(i));
           }
       }
       
       return str;
    }
    //---------- Metodo que substitui o valor do argumento no parametro --------
    public String SubstituirVar(String var1,String var2,String instrucao){
        
        var1 = (var1.substring(var1.indexOf(" "))).trim();
        
        StringTokenizer st = new StringTokenizer(instrucao,"\n");
        NodeInstruction node=null;
        String linha=""; 
         String straux = "",instrucaoaux = "";
                
        while (st.hasMoreTokens()) {
            linha = st.nextToken();
            
            node = new NodeInstruction(linha,0,0); 
                       
            instrucaoaux += RemoveAspas(node.GetText())+"\n";                         
      }
        
        for(int i=0;i<instrucaoaux.length();i++){
                String a=""+instrucaoaux.charAt(i);

                  if(a.equals(" ")){
                    straux ="";                   
                  }else{
                    straux +=a;
                    } 
        
            straux = straux.trim();

            if(straux.equals(var1)){
                instrucaoaux = instrucaoaux.replace(straux, var2);
         
            }           
        }
        return instrucaoaux;
    }
    
    //--------------------------------------------------------------------------
    public void VerOcorrencia_funcao() throws LanguageException{
        
       for(int i=0;i<acumulafuncao.size();i++){
           for(int j=0;j<acumulafuncao.size();j++){
               if(i!=j)
               if((subst(""+acumulafuncao.get(i))).equals((subst(""+acumulafuncao.get(j)))))
                   throw new LanguageException(                    
                        " O COMPILADOR DETECTOU FUNÇÕES COM O MESMO NOME\n",
                        "DEVE MUDAR O NOME DO DESTA FUNÇÃO: " + acumulafuncao.get(i) +""); 
           }
       }
    }
     //--------------------------------------------------------------------------
    /*Verifica se o tipo de retorno esta bem definido na funcao*/
    public void controlatipo_funcao(String tipo,NodeInstruction node) throws LanguageException{
        int j = -1;
       for(int i=0;i<tipos.length;i++){
           
           if(tipos[i].equals(tipo)){
               j = i; 
               i = tipos.length;
           }              
       }
       
       if (j!=-1){
           acumulaTipofuncao.add(tipos[j]);
       } 
       else  throw new LanguageException(node.GetCharNum(),node.GetText(),                 
                        " O COMPILADOR ESPERAVA UM TIPO DE DADO PRIMITIMO ANTES DO NOME DA FUNÇÃO\n",
                        "COLOQUE O TIPO DE RETORNO DA FUNÇÃO ENTRE A PALAVRA FUNÇÃO E O NOME DA FUNÇÃO\n"
                      + "OS TIPOS PRIMITIVOS SÃO: inteiro,real,logico,literal e caracter"); 
    }
     //-------------------------------------------------------------------------
    public String subst(String str){
        
        str = str.substring(0, str.indexOf("("));
        return str;
        
    }
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    
    public String Tratar_funcao() throws LanguageException {
        
        StringTokenizer st = new StringTokenizer(getAlgol(),"\n\r");
        NodeInstruction node=null;
        String linha="";
        String str = "",str1 = "";
        Vector instrucpro = new Vector();
        int cont = 0, contapro =0;
                
        while (st.hasMoreTokens()) {
            linha = st.nextToken();
            
            node = new NodeInstruction(linha,0,0);          
            if(controlaFuncao == 1){
             if (node.GetType() == Keyword.DESCONHECIDO){
                
                str = node.GetText();
                int index = acumulafuncao.indexOf(str);
                               
                if(index != -1 && cont == 2){
                    algol = algol.replace(str,""+instrucpro.get(index)+"\nretorna_retorno ( id )");
                }
                else{ 
                    if(str.indexOf('(')!=-1){
                    int t=0;
                        String sta ="",stb="",st3="",param1="";
                        sta = str.substring(0, str.indexOf('('));                       
                    for(int i =0; i<acumulafuncao.size();i++){
                        
                        if((""+acumulafuncao.get(i)).startsWith(sta)){
                            t=1; 
                            st3 = ""+acumulafuncao.get(i); // guarda a instrucao
                            i=acumulafuncao.size();
                        } 
                        
                    }
                    
                    if(t!=1)
                        throw new LanguageException( node.GetCharNum(), node.GetText(),                    
                        " ESTA FUNÇÃO É UNEXISTENTE",
                        "TENTA VERIFICAR BEM NOME DA FUNÇÃO " + sta +"\nOU SE POSSUE ALGUM PARAMETRO"); 
                
                        stb = st3.substring(str.indexOf('(')); // tira os parametros na declaracao
                        param1 = str.substring(str.indexOf('(')); // tira os argumentos na utilizacao
                    
                        int p1 = 0,p2 = 0; 
                        for(int i =0; i<stb.length();i++){
                            if(stb.charAt(i)==',')  p1++;                                           
                        } 
                        for(int i =0; i<param1.length();i++){
                            if(param1.charAt(i)==',')  p2++;                                           
                        } 
                        
                        if(p1!=p2)
                        throw new LanguageException( node.GetCharNum(), node.GetText(),                    
                        " NUMERO DE ARGUMENTO INCORRECTO",
                        "VERIFICA COMO CONSTRUIU ESTA FUNÇÃO ("+ sta +"), QUANTO AO PARÁMETRO"); 
                        
                        else{
                            
                            String strp = "",strp1 = ""; int v1=0,i1=1,i2=1;
                            String inst = "";
                            do{
                               
                                for(int i =i1; i<stb.length();i++){
                                    if(stb.charAt(i)!=','||stb.charAt(i)!=')'){ 
                                        strp +=stb.charAt(i);

                                    }
                                        if (stb.charAt(i)==','||stb.charAt(i)==')') {
                                            v1++; i1 = i+1;
                                            i=stb.length(); //forca o termino do ciclo 

                                    }                                 
                                 }
                                
                                for(int f =i2; f<param1.length();f++){
                                    if(param1.charAt(f)!=','||param1.charAt(f)!=')'){ 
                                        strp1 +=param1.charAt(f);

                                    }
                                    if (param1.charAt(f)==','||param1.charAt(f)==')') {
                                        i2 = f+1;
                                        f=param1.length(); //forca o termino do ciclo 
                                        
                                    }                                 
                                }
                                
                                strp = RemoverCaract(strp); strp1 = RemoverCaract(strp1);
                                
                                
                                String tipo = VariaveisTipo(strp1); //retorna o tipo da variavel
                                
                                
                                if(!strp.startsWith(tipo)) //verifica se o argumento tem um tipo diferente do parametro
                                    throw new LanguageException( node.GetCharNum(), node.GetText(),                    
                                    " O TIPO DE DADO DO ARGUMENTO NÃO É COMPATIVEL COM O PARÂMETRO\n",
                                    "VERIFICA COMO ESTÁ DEFINIDO OS PARAMETROS DA FUNÇÃO ("+ sta +")");  
                               
                                
                                //Substituir as variaveis do funçao(parametro) 
                                index = -1; 

                                for (int j=0;j<acumulafuncao.size();j++){
                                    String a = ""+acumulafuncao.get(j);
                                    if(a.startsWith(sta)) index = j;                                                                     
                                }
                                if(index != -1 && cont == 2){
                                    inst = ""+instrucpro.get(index);
                                }                                
                                
                                inst = (SubstituirVar(strp,strp1,inst));
                                
                                
                                instrucpro.set(index, inst);
                               
                                inst = (DevolveAspas(inst));
                     
                            strp = "";  strp1 = "";                                                             
                            }while(v1<=p1);         
                             
                            inst = (DevolveAspas(inst));
                            instrucpro.set(index, inst);
                            algol = algol.replace(str,""+instrucpro.get(index)+"");
                        }                       
                  }
                 }
            }
             }
             
             //----------------- Tipo de dado Primitivo ----------------------
            if (node.GetType() == Keyword.DEFINIR){
                String stri = ""+node.GetText(); 
                
                    // separa as variaveis por tipo
                    int contInt = stri.indexOf("inteiro "); int contFloat = stri.indexOf("real ");
                    int contString = stri.indexOf("literal "); int contChar = stri.indexOf("caracter ");
                    int contBoolean = stri.indexOf("logico ");

                    if(contInt!=-1) strInteiro += stri;

                    if(contFloat!=-1) strReal += stri;

                    if(contString!=-1) strLiteral += stri;

                    if(contChar!=-1) strLogico += stri;

                    if(contBoolean!=-1) strCaracter += stri;
                
                
            }
            //------------------------------------------------------------------
             if (node.GetType() == Keyword.INICIO){
                VerOcorrencia_funcao();
            }
             
            //*****************************************************************
            if(cont == 1 && (node.GetType() != Keyword.FIMFUNCAO)){
                str1 += node.GetText()+"\n";
            }
            
            //*****************************************************************
             if(cont == 2){
               instrucpro.add(str1+"\n");  
               str1 = "";
            }
            
            if (node.GetType() == Keyword.FUNCAO){
                String algolAux = algol.substring(0,algol.indexOf("inicio"));
                algol = algol.replace(algolAux, "+00+00"); // muda temporariamente o algoritmo
                
                str = node.GetText();
                String parte1 = (str.substring(6)).trim();
                
                String str_tipo = (parte1.substring(0,parte1.indexOf(' '))).trim();  // Pega o tipo de retorno
             //   JOptionPane.showMessageDialog(null, "Tipo ="+str_tipo);
                
                controlatipo_funcao(str_tipo,node); // Verifica se o utilicador colocou um tipo valido
                
                int c1 = parte1.indexOf(' ');
                if(c1 == -1 )
                    throw new LanguageException( node.GetCharNum(), node.GetText(),                    
                                    " A SINTAXE DA FUNÇÃO ESTÁ MAL CONSTRUIDA\n",
                                    "CONSULTE A AJUDA DO COMPILADOR, (pressione a tecla F1)");  
             
                
                String nomeFunc = (parte1.substring(c1)).trim();// Pega o nome da funcao
                palavraChave = new Keyword();
                
                if(palavraChave.IsKeyword(nomeFunc.substring(0, nomeFunc.indexOf("(")).trim()))
                    throw new LanguageException( node.GetCharNum(), node.GetText(),                    
                                    " O NOME DA FUNÇÃO NÃO PODE SER UMA PALAVRA RESERVADA\n",
                                    "MUDA O NOME DA FUNÇÃO ");   
                
                acumulafuncao.add(nomeFunc); // guarda o nome da funcao
                              
                algolAux = algolAux.replace("funcao ", "/*funcao "); // faz a alteracao 
                algol = algol.replace("+00+00",algolAux); // volta no algoritmo anterior ja com a mudanca
                cont = 1;
               
                contapro++;
            }
            //******************************************************************
            if (node.GetType() == Keyword.FIMFUNCAO){               
                algol = algol.replace("fimfuncao", "fimfuncao*/");
                cont = 2;
                controlaFuncao = 1;
            } 
            //******************************************************************
            if (cont == 1 && node.GetType() == Keyword.RETORNE){               
                
                String str2 = node.GetText();
                String a = (str2.substring(str2.indexOf(" "))).trim(); // obtem a variavel de retorno
                
               String tipo = VariaveisTipo(a);
                        
              //  JOptionPane.showMessageDialog(null, "Tipo ="+tipo);
            } 
            
            }
        
         return algol;
        }
}

