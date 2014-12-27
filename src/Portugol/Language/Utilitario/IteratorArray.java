package Portugol.Language.Utilitario;

import java.util.Stack;
import java.util.Vector;

public class IteratorArray {
    
    /**
     * Creates a new instance of ArrayTokenizer
     */
    Vector elements;
    //tudo aquilo que esta para alem do ]
    String tail="";
    public IteratorArray(String array){
        elements = new Vector();
        BuildElements(array);
    }
    
    public boolean hasMoreElements(){
        return !elements.isEmpty();
    }
    
    public String getNext(){
        return (String) elements.remove(0);
    }
    void BuildElements(String array){
        Stack s= new Stack();
        String elem="";
        for(int index=0 ; index < array.length() ; index++){
            char ch = array.charAt(index);
            if(ch=='['){
                // nome do array
                if(s.empty() && elem.trim().length()>0){
                    elements.add(elem.trim());
                    elem="";
                }
                //retirar o primeiro [
                if(!s.isEmpty())
                    elem +="[";
                
                s.add(""+ch);
                
                
            } else if(ch==']'){
                s.pop();
                if(s.empty()){
                    elements.add(elem.trim());
                    elem="";
                }
                //retirar o ultimo ]
                if(!s.isEmpty())
                    elem +="]";
                
            } else
                elem += ch;
        }
        
        tail = elem.trim();
        
    }
    public String getTail(){
        return tail;
    }
        
   
}
