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
@Root(name="security-setting")
public class SecuritySettings {
    
    @ElementList(entry="security-setting",inline=true,required=false)
    public List<String> securitySettingList; 
    
    @Override   
    public String toString(){
             
        String ssStr="";
        if(securitySettingList!=null)
        {
            for(String item:securitySettingList)
            {
               ssStr+=item; 
            }
        }
        String retStr="{\n\tsecuritySetting:"+ssStr+
                      "\n\t}";
        return retStr;
    }
}
