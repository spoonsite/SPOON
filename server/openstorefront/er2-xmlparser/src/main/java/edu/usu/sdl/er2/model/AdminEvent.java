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
public class AdminEvent {
    
    @Attribute(required=false)
    public String date;
    
    @Element (required=false)
    public User user;
    
    @Override   
    public String toString(){
        
        String UserStr="";
        
        if(user!=null)
        {
            UserStr+=user.toString();
        }
        String retStr="{\n\tdate:"+date+
                      "\n\tuser:"+UserStr+
                      "\n\t}";
        return retStr;
        
    }
            
    
}
