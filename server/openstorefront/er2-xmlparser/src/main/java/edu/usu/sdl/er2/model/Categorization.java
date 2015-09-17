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
public class Categorization {
    
    @Attribute
    public Integer id;
    
    @Attribute
    public Integer typeid;
    
    @Attribute 
    public Integer level;
    
    @Attribute
    public Integer activestatus;
    
    @Element
    public String name;
    
    @Element (required=false)
    public CDataType description;
    
    @Element
    public String fullyqualifiedname;
    
    @Element(name="super")
    public CategorizationSuper categorizationSuper;
    
    @Element(name="type",required=false)
    public CategorizationTypeWrapper categorizationType;
   
    @ElementList(name="children",required=false)
    public List<Categorization> children; 
            
            
            
    @Override
       public String toString(){
        
        String childrenStr="";
        String categorizationTypeStr="";
        
        if(categorizationType!=null)
        {
           categorizationTypeStr=categorizationType.toString();
        }
        
        if(children!=null)
        {
            for(Categorization item:children)
            {
                childrenStr+=item.toString()+",\n\t ";
            }
        }
           
        String retStr="{\n\tid:"+id+
                      "\n\ttypeid:"+typeid+
                      "\n\tlevel:"+level+
                      "\n\tactivestatus:"+activestatus+
                      "\n\tname:"+name+
                      "\n\tdescription:"+description.toString()+
                      "\n\tfullyqualifiedname:"+fullyqualifiedname+
                      "\n\tcategorizationSuper:"+categorizationSuper.toString()+
                      "\n\tcategorizationType:"+categorizationTypeStr+
                      "\n\tchildren:"+childrenStr+
                      "\n\t}";
        return retStr;
        
       }
    
    
    
    
}
