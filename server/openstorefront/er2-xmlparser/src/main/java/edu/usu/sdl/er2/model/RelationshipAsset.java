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
@Root(strict=false)
public class RelationshipAsset {
    
    @Attribute
    public Integer id;
    
    @Attribute
    public String name;
    
    @Attribute
    public String version;
    
    @Attribute
    public String activestatus;
    
    @Override
       public String toString(){
        
        String retStr="{\n\tid:"+id+
                      "\n\tname:"+name+
                      "\n\tversion:"+version+
                      "\n\tactivestatus:"+activestatus+
                      "\n\t}";
        return retStr;
        
       }
    
}
