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
@Root(strict=false)
public class MandatoryData {
    
    @Element
    public String uuid;
    
    @Element
    public String name;
    
    @Element
    public String version;
    
    @Element(required=false,data=true)
    public String description;
    
    @Element(name="notification-email",required=false)
    public String notificationEmail;
    
    @Element (name="unique-element", required=false)
    public String uniqueElement;
    
    @ElementList (name="file-informations", required=false)
    public List<FileInformation> fileInformations;
    
    @Element(name="applied-policies", required=false)
    public String appliedPolicies;
    
    @Element (required=false)
    public Relationships relationships;
    
    @Element(name="categorization-types", required=false)
    public CategorizationTypes categorizationTypes;
    
    @Override
       public String toString(){
        
        String fileInfo="";
        if(fileInformations!=null){
           for (FileInformation item:fileInformations){
            fileInfo+=item.toString();
           }
        }
        
           
           
        String retStr="{\n\tuuid:"+uuid+
                      "\n\tname:"+name+
                      "\n\tversion:"+version+
                      "\n\tdescription:"+description+
                      "\n\tnotificationEmail:"+notificationEmail+
                      "\n\tuniqueElement:"+uniqueElement+
                      "\n\tfileInformations:{"+fileInfo+"\n}"+
                      "\n\tappliedPolicies:"+appliedPolicies+
                      "\n\trelationships:"+relationships.toString()+
                      "\n\tcategorizationTypes:"+categorizationTypes.toString()+
                      "\n\t}";
        return retStr;
        
       }
}
