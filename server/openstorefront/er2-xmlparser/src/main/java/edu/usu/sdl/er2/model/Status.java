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
public class Status {
    
    @Attribute(required=false)
    public Integer id;
    
    @Text(required=false)
    public String statusText;
    
    
    @Override   
    public String toString(){
        String retStr="{\n\tid:"+id+
                      "\n\tstatusText:"+statusText+
                      "\n\t}";
        return retStr;
        
    } 
    
}
