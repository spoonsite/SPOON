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
public class User {
    
   @Attribute(required=false)
   public String email;
   
   @Attribute(required=false)
   public Integer id;
   
   @Attribute(required=false)
   public String username;
   
   @Override   
    public String toString(){
        String retStr="{\n\temail:"+email+
                      "\n\tid:"+id+
                      "\n\tusername:"+username+
                      "\n\t}";
        return retStr;
    }
    
}
