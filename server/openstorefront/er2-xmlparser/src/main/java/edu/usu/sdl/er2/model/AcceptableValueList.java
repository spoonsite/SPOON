/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.usu.sdl.er2.model;
import java.util.List;
import org.simpleframework.xml.*;

/**
 *
 * @author rnethercott
 */
@Root
public class AcceptableValueList {

    @Attribute
    public Integer id;
    
    @Element
    public String name;
    
    @ElementList
    public List<AcceptableValue> acceptablevalues;

    
    @Override
    public String toString(){
        
        String retStr="{\nid:"+id+"\nname:"+name+"\nacceptablevalues:";
        for (AcceptableValue item:acceptablevalues){
            retStr+=item.toString();
        }
        retStr+="}\n";
        return retStr;
        
    }
}
