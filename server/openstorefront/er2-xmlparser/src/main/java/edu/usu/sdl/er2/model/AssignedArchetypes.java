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
public class AssignedArchetypes {
    
    @ElementList (entry="assigned-archetype",inline=true,required=false)
    public List<AssignedArchetype> assignedArchetypeList;
    
    @Override   
    public String toString(){
        String aaListStr="";
        
        if(assignedArchetypeList!=null)
        {
           for(AssignedArchetype item:assignedArchetypeList)
           {
               aaListStr+=item.toString()+", ";
           }
        }
        
        String retStr="{\n\tassignedArchetypeList:"+aaListStr+
                      "\n\t}";
        return retStr;
        
    } 
}
