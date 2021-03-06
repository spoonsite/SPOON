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
public class CategorizationTypeEmbedded {
    @Attribute
    public Integer id;
    
    @Attribute
    public String custom;
    
    @Attribute (name="system-only")
    public String systemOnly;
    
    @Attribute (name="asset-assignable")
    public String assetAssignable;
    
    @Attribute(name="project-assignable")
    public String projectAssignable;
    
    @Attribute
    public String flat;
    
    @Attribute(name="exclusive-assign")
    public String exclusiveAssign;
    
    @Element
    public String name;
    
    @Element 
    public String externalids;
    
    @Element
    public String displaysingular;
    
    @Element
    public String displayplural;
    
     @Override
       public String toString(){
        
        String retStr="{\n\tid:"+id+
                      "\n\tcustom:"+custom+
                      "\n\tsystemOnly:"+systemOnly+
                      "\n\tassetAssignable:"+assetAssignable+
                      "\n\tprojectAssignable:"+projectAssignable+
                      "\n\tflat:"+flat+
                      "\n\texclusiveAssign:"+exclusiveAssign+
                      "\n\tname:"+name+
                      "\n\texternalids:"+externalids+
                      "\n\tdisplaysingular:"+displaysingular+
                      "\n\tdisplayplural:"+displayplural+
                      "\n\t}";
        return retStr;
        
       }
    
}
