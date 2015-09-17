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
public class Email {
    @Element(name="email-type",required=false)
    public String emailType;
    
    @Element(name="email-address",required=false)
    public String emailAddress;
    
    @Override   
    public String toString(){
        String retStr="{\n\temailType:"+emailType+
                      "\n\temailAddress:"+emailAddress+
                      "\n\t}";
        return retStr;
        
    }
}
