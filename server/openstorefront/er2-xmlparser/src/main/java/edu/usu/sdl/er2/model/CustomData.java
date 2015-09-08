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
public class CustomData {
    
    @ElementList (name="emails",required=false)
    public List<Email> emailList;
    
    @ElementList (name="phone-numbers",required=false)
    public List<PhoneNumber> phoneList;
    
    @Element(name="first-name",required=false)
    public String firstName;
    
    @Element(name="last-name",required=false)
    public String lastName;
    
    @Element(name="poc-type",required=false)
    public String pocType;
    
    @Element(required=false)
    public String agency;
    
    @Element(name="distinguished-name",required=false)
    public String distinguishedName;
    
    @Element(name="overall-description-security-marking",required=false)
    public String overallDescriptionSecurityMarking;
    
    @Element(name="classification-of-widget-metadata",required=false)
    public String classificationOfWidgetMetaData;
    
    @Element(required=false)
    public String comments;
    
    @Element(required=false)
    public String location;
    
    @Element(required=false)
    public String tests;
    
    @ElementList(name="documentation",required=false)
    public List<Document> documentList;
    
    @ElementList(name="asset-type-catalog",required=false)
    public List<Document> assetTypeDocumentsList;
    
    @Element(required=false)
    public String submitter;
    
    @Element(name="producing-agency",required=false)
    public String producingAgency;
    
    @Element(name="launch-url",required=false)
    public String launchURL;
    
    @Element(name="custom-category-1",required=false)
    public String customCategory1;
    
    @Element(name="custom-category-2",required=false)
    public String customCategory2;
    
    @Element(required=false)
    public String networks;
    
    @Element(name="operating-systems",required=false)
    public String operatingSystems;
    
    @Element(name="required-certifications",required=false)
    public String requiredCertifications;
    
    @Element(name="version-history",required=false)
    public String versionHistory;
    
    @Element(name="license-costs",required=false)
    public String licenseCosts;
    
    @Element(required=false)
    public String requirements;
    
    @Element(name="external-id",required=false)
    public String externalId;
    
    @Element(name="ato-start-date",required=false)
    public String atoStartDate;
    
    @Element(name="ato-expiration-date",required=false)
    public String atoExpirationDate;
    
    @Element(name="security-mechanisms",required=false)
    public String securityMechanisms;
    
    @Element(required=false)
    public String dependencies;
    
    @Element(name="widget-layout",required=false)
    public String widgetLayout;
    
    @Element(required=false)
    public String singleton;
    
    @Element(required=false)
    public String visible;
    
    @Element(name="run-in-background",required=false)
    public String runInBackground;
    
    @Element(name="externally-governed",required=false)
    public String externallyGoverned;
    
    @Element(name="external-source-name",required=false)
    public String externalSourceName;
    
    @Element(name="external-source-id",required=false)
    public String externalSourceId;
    
    @Element(name="default-parameters",required=false)
    public String defaultParameters;
    
    @ElementList(name="milestones",required=false)
    public List<Milestone> milestoneList;
    
    @Override   
    public String toString(){
        
        String emailL="";
        if(emailList!=null){
           for (Email item:emailList){
            emailL+=item.toString()+", ";
           }
        }
        
        String phoneL="";
        if(phoneList!=null){
           for (PhoneNumber item:phoneList){
            phoneL+=item.toString()+", ";
           }
        }
        
        String docL="";
        if(documentList!=null){
           for (Document item:documentList){
            docL+=item.toString()+", ";
           }
        }
        
        String docAL="";
        if(assetTypeDocumentsList!=null){
           for (Document item:assetTypeDocumentsList){
            docAL+=item.toString()+", ";
           }
        }
        
        String milestoneL="";
        if(milestoneList!=null){
           for (Milestone item:milestoneList){
            milestoneL+=item.toString()+", ";
           }
        }
        
        
        String retStr="{\n\temails:"+emailL+
                      "\n\tphoneNumbers:"+phoneL+
                      "\n\tdocuments:"+docL+
                      "\n\tassetTypeDocumentsList:"+docAL+
                      "\n\tmilestoneList:"+milestoneL+
                      "\n\tfirstName:"+firstName+
                      "\n\tlastName:"+lastName+
                      "\n\tpocType:"+pocType+
                      "\n\tagency:"+agency+
                      "\n\tdistinguishedName:"+distinguishedName+
                      "\n\toverallDescriptionSecurityMarking:"+overallDescriptionSecurityMarking+
                      "\n\tclassificationOfWidgetMetaData:"+classificationOfWidgetMetaData+
                      "\n\tcomments:"+comments+
                      "\n\tlocation:"+location+
                      "\n\ttests:"+tests+
                      "\n\tsubmitter:"+submitter+
                      "\n\tproducingAgency:"+producingAgency+
                      "\n\tlaunchURL:"+launchURL+
                      "\n\tcustomCategory1:"+customCategory1+
                      "\n\tcustomCategory2:"+customCategory2+
                      "\n\tnetworks:"+networks+
                      "\n\toperatingSystems:"+operatingSystems+
                      "\n\trequiredCertifications:"+requiredCertifications+
                      "\n\tversionHistory:"+versionHistory+
                      "\n\tlicenseCosts:"+licenseCosts+
                      "\n\trequirements:"+requirements+
                      "\n\texternalId:"+externalId+
                      "\n\tatoStartDate:"+atoStartDate+
                      "\n\tatoExpirationDate:"+atoExpirationDate+
                      "\n\tsecurityMechanisms:"+securityMechanisms+
                      "\n\tdependencies:"+dependencies+
                      "\n\twidgetLayout:"+widgetLayout+
                      "\n\tsingleton:"+singleton+
                      "\n\tvisible:"+visible+
                      "\n\trunInBackground:"+runInBackground+
                      "\n\texternallyGoverned:"+externallyGoverned+
                      "\n\texternalSourceName:"+externalSourceName+
                      "\n\texternalSourceId:"+externalSourceId+
                      "\n\tdefaultParameters:"+defaultParameters+
                      "\n\t}";
        return retStr;
        
    }
    
}
