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
public class FileInformation {
    
    @Attribute
    public Integer id;
    
    @Element(name="file-description", required=false)
    public String fileDescription;
    
    @Element(name="file-configfile", required=false)
    public String fileConfigFile;
    
    @Element(name="file-typeid", required=false)
    public Integer fileTypeId;
    
    @Element(name="file-url", required=false)
    public String fileURL;
    
    @Element(name="file-name", required=false)
    public String fileName;
    
    @Override
       public String toString(){
        
        String retStr="{\n\tid:"+id+
                      "\n\tfileName:"+fileName+
                      "\n\tfileConfigFile:"+fileConfigFile+
                      "\n\tdescription:"+fileDescription+
                      "\n\tfileTypeId:"+fileTypeId+
                      "\n\tfileURL:"+fileURL+
                      "\n\t}";
        return retStr;
        
       }
    
}
