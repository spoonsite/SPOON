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
public class UUID {
    @Attribute(required=false)
    public String idspace;
    
    @Text(required=false)
    public String uuidValue;
    
     @Override   
    public String toString(){
        String retStr="{\n\tidspace:"+idspace+
                      "\n\tuuidValue:"+uuidValue+
                      "\n\t}";
        return retStr;
        
    } 
}
