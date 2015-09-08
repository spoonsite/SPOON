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
public class AssetTypeFile {
    
    @Attribute
    public Integer id;
    
    @Attribute
    public String icon;
    
    @Attribute
    public Integer activeStatus;
    
    @Attribute(name="uddi-support")
    public String uddiSupport;
    
    @Element (required=false)
    public AdminEvent updated;
    
    @Element (name="assigned-archetypes",required=false)
    public AssignedArchetypes assignedArchtypes;
    
    @Element
    public String name;
    
    @ElementList (required=false)
    public List<UUID> uuids;
    
    @Element(data=true)
    public String editorXML;
    
    @Element(data=true)
    public String viewerXML;
    
    @Override
    public String toString(){
        
        String aarchTypesStr="";
        String uuidStr="";
        
        if(assignedArchtypes!=null)
        {
           aarchTypesStr=assignedArchtypes.toString();
        }
        
        if(uuids!=null)
        {
           for(UUID item:uuids)
           {
               uuidStr+=item.toString()+", ";
           }
        }
        
        
        String retStr="{\n\tid:"+id+
                      "\n\ticon:"+icon+
                      "\n\tactiveStatus:"+activeStatus+
                      "\n\tuddiSupport:"+uddiSupport+
                      "\n\tupdated:"+updated.toString()+
                      "\n\tassignedArchTypes:"+aarchTypesStr+
                      "\n\tname:"+name+
                      "\n\tuuids:"+uuidStr+
                      "\n\teditorXML:"+editorXML+
                      "\n\tviewerXML:"+viewerXML+
                      "\n\t}";
        return retStr;
        
    } 
}
