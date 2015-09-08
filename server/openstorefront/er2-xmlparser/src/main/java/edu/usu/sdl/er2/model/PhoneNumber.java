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
public class PhoneNumber {
    @Element(name="phone-type",required=false)
    public String phoneType;
    
    @Element(name="phone-number",required=false)
    public String phoneNumber;
    
    @Override   
    public String toString(){
        String retStr="{\n\tphoneType:"+phoneType+
                      "\n\tphoneNumber:"+phoneNumber+
                      "\n\t}";
        return retStr;
        
    } 
}
