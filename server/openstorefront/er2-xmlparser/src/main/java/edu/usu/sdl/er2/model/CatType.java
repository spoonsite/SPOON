/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.er2.model;
import java.util.List;
import org.simpleframework.xml.*;
/**
 *
 * @author rnethercott
 */
@Root
public class CatType {
    
    @Attribute
    public Integer id;
    
    @Attribute
    public String name;
    
    @Attribute
    public String singular;
    
    @Attribute 
    public String plural;
    
    @Attribute
    public String custom;
    
    @ElementList(entry="categorization",inline=true,required=false)
    public List<Categorization> categorizationList;
    
    @Override
       public String toString(){
        String catList="";
        if(categorizationList!=null){
           for (Categorization item:categorizationList){
            catList+=item.toString();
           }
        }
           
           
           
        String retStr="{\n\tid:"+id+
                      "\n\tname:"+name+
                      "\n\tsingular:"+singular+
                      "\n\tplural:"+plural+
                      "\n\tcustom:"+custom+
                      "\n\tcategorizationList:"+catList+
                      "\n\t}";
        return retStr;
        
       }
}
