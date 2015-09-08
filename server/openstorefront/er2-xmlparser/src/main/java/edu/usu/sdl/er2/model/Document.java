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
public class Document {
    @Element(name="document-name",required=false)
    public String documentName;
    
    @Element(name="document-url",required=false)
    public String documentURL;
    
    @Element(name="document-approved",required=false)
    public String documentApproved;
    
    @Override   
    public String toString(){
        String retStr="{\n\tdocumentName:"+documentName+
                      "\n\tdocumentURL:"+documentURL+
                      "\n\tdocumentApproved:"+documentApproved+
                      "\n\t}";
        return retStr;
        
    } 
}
