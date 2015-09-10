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
public class CategorizationTypeWrapper {
    
    @Element (name="categorizationtype")
    public CategorizationTypeEmbedded categorizationEmbeddedType;
     @Override
       public String toString(){
        
        String retStr="{\n\tcategorizationEmbeddedType:"+categorizationEmbeddedType.toString()+
                      "\n\t}";
        return retStr;
        
       }
}
