/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.er2.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

/**
 *
 * @author rnethercott
 */
@Root 
public class AssetType {
    
       @Attribute
       public Integer id;

       @Attribute
       public String icon;

       @Attribute
       public String lastSavedDate;

       @Text
       public String type;
       
       @Override
       public String toString(){
        
        String retStr="{\n\tid:"+id+"\n\ticon:"+icon+"\n\tlastSavedDate:"+lastSavedDate+"\n\ttype:"+type+"\n\t}";
        return retStr;
        
       }
}
