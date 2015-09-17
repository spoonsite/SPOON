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
public class AssignedArchetype {
    @Attribute
    public Integer id;
    
    @Override   
    public String toString(){
        String retStr="{\n\tid:"+id+
                      "\n\t}";
        return retStr;
        
    } 
}
