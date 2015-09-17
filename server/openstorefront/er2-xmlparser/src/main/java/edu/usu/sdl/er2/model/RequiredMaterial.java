/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.er2.model;
import org.simpleframework.xml.*;
/**
 *
 * @author rnethercott
 */
@Root
public class RequiredMaterial {
    
    @Attribute
    public Integer assettypeid;
    
    @Attribute
    public Integer id;
    
    @Element
    public String name;
    
    @Element
    public String xmlmapping;
    
    @Override   
    public String toString(){
        String retStr="{\n\tassettypeid:"+assettypeid+
                      "\n\tid:"+id+
                      "\n\tname:"+name+
                      "\n\txmlmapping:"+xmlmapping+
                      "\n\t}";
        return retStr;
        
    }
}
