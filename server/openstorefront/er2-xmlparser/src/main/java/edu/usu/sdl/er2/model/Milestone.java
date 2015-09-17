/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.er2.model;

import org.simpleframework.xml.Element;

/**
 *
 * @author rnethercott
 */
public class Milestone {
    
    @Element(name="name",required=false)
    public String name;
    
    @Element(name="date",required=false)
    public String date;
    
    @Element(name="description",required=false)
    public String description;
    
    @Element(name="other-name",required=false)
    public String otherName;
    
    @Override   
    public String toString(){
        String retStr="{\n\tname:"+name+
                      "\n\tdate:"+date+
                      "\n\tdescription:"+description+
                      "\n\totherName:"+otherName+
                      "\n\t}";
        return retStr;
        
    } 
}
