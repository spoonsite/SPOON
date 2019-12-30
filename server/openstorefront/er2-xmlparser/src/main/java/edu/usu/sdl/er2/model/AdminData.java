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
@Root(strict=false)
public class AdminData {

    @Element(required=false)
    public Status status;

    @Element(required=false)
    public Status registrationstatus;

    @Element(required=false)
    public Status quicksubmit;

    @Element(required=false)
    public AdminEvent updated;

    @Element(required=false)
    public AdminEvent created;

    @Element(required=false)
    public AdminEvent submitted;

    @Element(required=false)
    public AdminEvent accepted;

    @Element(required=false)
    public AdminEvent registered;

    @Element(name="security-settings",required=false)
    public SecuritySettings securitySettingsList;

    @Override
    public String toString(){

        String ssList="";
        if(securitySettingsList!=null){

            ssList+=securitySettingsList.toString();

        }

        String retStr = "{\n\tstatus:" + status + "\n\tregistrationStatus:" + registrationstatus + "\n\tquicksubmit:"
                + quicksubmit + "\n\tupdated:" + updated + "\n\tcreated:" + created + "\n\tsubmitted:" + submitted
                + "\n\taccepted:" + accepted + "\n\tregistered:" + registered + "\n\tsecuritySettingsList:" + ssList
                + "\n\t}";
        return retStr;
    }

}
