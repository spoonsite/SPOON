/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.usu.sdl.spoon.aerospace.importer;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.ApprovalStatus;
import edu.usu.sdl.openstorefront.core.entity.AttributeValueType;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.DataSource;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import edu.usu.sdl.openstorefront.core.entity.MediaType;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.spi.parser.BaseComponentParser;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.ComponentMapper;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.AttributeContext;
import edu.usu.sdl.openstorefront.core.util.MediaFileType;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.spoon.aerospace.importer.model.FloatFeature;
import edu.usu.sdl.spoon.aerospace.importer.model.IntFeature;
import edu.usu.sdl.spoon.aerospace.importer.model.Product;
import edu.usu.sdl.spoon.aerospace.importer.model.RelatedOrganization;
import edu.usu.sdl.spoon.aerospace.importer.model.RevisionProvenanceDocument;
import edu.usu.sdl.spoon.aerospace.importer.model.RevisionProvenanceWebsite;
import edu.usu.sdl.spoon.aerospace.importer.model.TextFeature;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;

/**
 *
 * @author rfrazier
 */
public class AerospaceComponentParser
        extends BaseComponentParser {

    private static final Logger LOG = Logger.getLogger(AerospaceReader.class.getName());
    private static final String UNKNOWN_ORGANIZATION = "Unknown";
    private static final String MANUFACTURER_ORG = "Manufacturer";
    public static final String FORMAT_CODE = "AEROSPACECMP";
    private static final String KEY_SPLITTER = "#";
    private static final String SITE_DELISTED = "[SITE DELISTED]";
    private static final String SYSTEM_DATA_OWNER = "SYSTEM";
    private static final String PRODUCT_TYPE_ATTRIBUTE = "Product Type";
    private static final String SNAPSHOT_FILE_NAME = "Snapshot.";
    private static final String HTTP_PREPENDER = "http://";
    private static final String AEROSPACE_IMPORT_KEY_PREPENDER = "AEROSPACE_IMPORT#";
    
    private List<String> resourceWebKeys = new ArrayList<String>();
    private List<String> resourceDocKeys = new ArrayList<String>();
    private List<String> resourceMediaKeys = new ArrayList<String>();
    private AerospaceReader reader;
    private AerospaceXMLParser parser;

    public AerospaceComponentParser() {
        this.parser = new AerospaceXMLParser();
    }

    @Override
    public String checkFormat(String mimeType, InputStream input) {
        if (mimeType.contains("zip")) {
            return "";
        } else {
            return "Invalid format. Please upload a ZIP file.";
        }
    }

    @Override
    protected GenericReader getReader(InputStream in) {
        try {
            //return a product
            reader = new AerospaceReader(in, fileHistoryAll);
            return reader;
        } catch (IOException ex) {
            throw new OpenStorefrontRuntimeException("Could not read file", "check file format, should be zip or permission", ex);
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    protected <T> Object parseRecord(T record) {
        
        // Set up the product
        Product product = (Product) record;       
        String descriptionMetaData;
        
        // Set up the ComponentMapper
        ComponentMapper componentMapper = new ComponentMapper(() -> {
            ComponentAll componentAll = defaultComponentAll();
            componentAll.getComponent().setDescription(null);
            return componentAll;
        }, fileHistoryAll);
        
        // Set up a Default ComponentAll
        ComponentAll componentAll = defaultComponentAll();
        // Shortcut to the component
        Component component = componentAll.getComponent();
        component.setActiveStatus(Component.INACTIVE_STATUS);
        component.setApprovalState(ApprovalStatus.APPROVED);
        component.setExternalId(AEROSPACE_IMPORT_KEY_PREPENDER + Integer.toString(product.getKey()));
        
        String lookUpDataSourceType = getLookup(DataSource.class, AEROSPACE_IMPORT_KEY_PREPENDER);
        component.setDataSource(lookUpDataSourceType);
        
        // Get the name of the component.
        if(StringProcessor.stringIsNotBlank(product.getLongName())){
            component.setName(product.getLongName());
        } else if(StringProcessor.stringIsNotBlank(product.getShortName())) {
            component.setName(product.getShortName());
        } else {
            LOG.log(Level.WARNING, "This product does not have any associated name. Entry cannot be created. " + "Key: " + product.getKey());
            return null;
        }
        
        // Verify that the product is not from delisted site.
        if(component.getName().startsWith(SITE_DELISTED)) {  
            for (ComponentAdminView componentAdminView : reader.masterComponentAdminViewList) {
                if((componentAdminView.getComponent().getExternalId() != null) 
                        && componentAdminView.getComponent().getExternalId().equals(component.getExternalId())) {
                    Component exampleComponent = new Component();
                    exampleComponent.setComponentId(componentAdminView.getComponent().getComponentId());
                    List<Component> foundComponents = new ArrayList<>();
                    foundComponents = exampleComponent.findByExample();
                    
                    if(foundComponents != null){
                        for(Component comp : foundComponents){
                            comp.setActiveStatus(Component.INACTIVE_STATUS);
                            comp.save();
                            service.getComponentService().cascadeDeleteOfComponent(comp.getComponentId());
                            comp.delete();
                        }
                    }
                }
            }
            LOG.log(Level.WARNING, "THIS RECORD HAS BEEN DELISTED AND WAS NOT IMPORTED. "
                    + "THE CORRESPONDING ENTRY (IF ANY) HAS BEEN DELETED." + component.getName() + " Key: " + product.getKey());
            fileHistoryAll.addError(FileHistoryErrorType.WARNING, "This record has been delisted and was not imported. "
                    + "The corresponding entry (if any) has been deleted. " + component.getName() + " Key: " + product.getKey());
            return null;            
        }
        
        // Verify conditions are correct to import/update the product.
        for (ComponentAdminView componentAdminView : reader.masterComponentAdminViewList) {
            if((componentAdminView.getComponent().getExternalId() != null)
                    && (componentAdminView.getComponent().getExternalId().equals(component.getExternalId())) 
                    && (!componentAdminView.getComponent().getCurrentDataOwner().equals(SYSTEM_DATA_OWNER))) {
                LOG.log(Level.WARNING, ""
                        + "THIS RECORD WAS PREVIOUSLY IMPORTED AND IS NOW BEING MAINTAINED BY A THIRD PARTY. "
                        + "THE RECORD WAS NOT IMPORTED/UPDATED. " + component.getName() + " Key: " + product.getKey());
                fileHistoryAll.addError(FileHistoryErrorType.WARNING, ""
                        + "This record was previously imported and is now being maintained by a third party. "
                        + "The record was not imported/updated. " + component.getName() + " Key: " + product.getKey());
                return null;
            }
            
            if ((componentAdminView.getComponent().getName().toLowerCase().equals(component.getName().toLowerCase()))
                    && (!componentAdminView.getComponent().getName().equals(component.getName()))) {
                LOG.log(Level.WARNING, ""
                        + "THIS ENTRY ALREADY EXISTS, AND HAS A NAME-CASE DIFFERENCE. "
                        + "THE RECORD WAS NOT IMPORTED. " + component.getName() + " Key: " + product.getKey());
                fileHistoryAll.addError(FileHistoryErrorType.WARNING, ""
                        + "This entry already exists, and has a name-case difference. "
                        + "The record was not imported. " + component.getName() + " Key: " + product.getKey());
                return null;
            }
            
            if ((componentAdminView.getComponent().getName().equals(component.getName()))
                    && (!componentAdminView.getComponent().getCurrentDataOwner().equals(SYSTEM_DATA_OWNER))) {
                LOG.log(Level.WARNING, ""
                        + "THIS ENTRY ALREADY EXISTS, AND IS BEING MAINTAINED BY A THIRD PARTY. "
                        + "THE RECORD WAS NOT IMPORTED. " + component.getName() + " Key: " + product.getKey());
                fileHistoryAll.addError(FileHistoryErrorType.WARNING, ""
                        + "This entry already exists, and is being maintained by a third party. "
                        + "The record was not imported. " + component.getName() + " Key: " + product.getKey());
                return null;
            }
            
            if (componentAdminView.getComponent().getName().equals(component.getName())
                    && (!componentAdminView.getComponent().getExternalId().equals(component.getExternalId()))) {
                LOG.log(Level.WARNING, ""
                        + "THIS ENTRY ALREADY EXISTS, AND HAS THE EXACT SAME NAME BUT A DIFFERENT KEY! "
                        + "THE RECORD WAS NOT IMPORTED. " + component.getName() + " Key: " + product.getKey());
                fileHistoryAll.addError(FileHistoryErrorType.WARNING, ""
                        + "This entry already exists, and has the exact same name but a different key! "
                        + "The record was not imported. " + component.getName() + " Key: " + product.getKey());
                return null;
            }
            
        }
        // If we made it this far we have ownership and can import/update the entry as needed.
        
        // Created a String and stored description information.
        descriptionMetaData = "<strong>Product Name: </strong>" + component.getName() + "<br>";
        if(StringProcessor.stringIsNotBlank(product.getModelNumber())){
            descriptionMetaData += "<strong>Model Number: </strong>" + product.getModelNumber()+ "<br>";
        }
        
        component.setComponentType(parser.getComponentType(product));
        
        // Create an attributeContext that holds information about the attribute. This will be used to create each individual attribute.
        Map<String, AttributeContext> attributeContextMap = new HashMap<>();
        
        if(product.getProductRevision().getProductType().getClassification().size() > 0) {
        
            ComponentAttribute attribute = new ComponentAttribute();
            ComponentAttributePk attributePk = new ComponentAttributePk();
            attributePk.setAttributeType(PRODUCT_TYPE_ATTRIBUTE);
            attributePk.setAttributeCode(product.getProductRevision().getProductType().getClassification().get(0).getCategoryName());
            attribute.setComponentAttributePk(attributePk);
            componentAll.getAttributes().add(attribute);
            AttributeContext attributeContext = new AttributeContext();
            attributeContext.setComponentType(component.getComponentType());
            attributeContext.setAttributeValueType(AttributeValueType.TEXT);
            attributeContext.setAttributeDescription("");
            attributeContextMap.put(attributePk.getAttributeType(), attributeContext);

        }
        
        List<FloatFeature> floatList = new ArrayList<>();
        List<IntFeature> intList = new ArrayList<>();
        List<TextFeature> textList = new ArrayList<>();
      
        // Gather all the features, these will be turned into attributes.
        floatList.addAll(product.getProductRevision().getSpecs().getFloatFeatures());
        floatList.addAll(product.getProductRevision().getShape().getFloatFeatures());
        floatList.addAll(product.getProductRevision().getAdditional().getFloatFeatures());
        intList.addAll(product.getProductRevision().getSpecs().getIntFeatures());
        intList.addAll(product.getProductRevision().getShape().getIntFeatures());
        intList.addAll(product.getProductRevision().getAdditional().getIntFeatures());
        textList.addAll(product.getProductRevision().getSpecs().getTextFeatures());
        textList.addAll(product.getProductRevision().getShape().getTextFeatures());
        textList.addAll(product.getProductRevision().getAdditional().getTextFeatures());
        
        // Parse the features
        for(FloatFeature floatFeature : floatList) {
            if(floatFeature.getValue() == null) {
                continue;
            }
            ComponentAttribute attribute = new ComponentAttribute();
            ComponentAttributePk attributePk = new ComponentAttributePk();
            attributePk.setAttributeType(floatFeature.getName() + "(" + floatFeature.getUnitAbbr() + ")");
            attributePk.setAttributeCode(floatFeature.getValue().toString());            
            attribute.setComponentAttributePk(attributePk);
            componentAll.getAttributes().add(attribute);
            
            AttributeContext attributeContext = new AttributeContext();
            attributeContext.setComponentType(component.getComponentType());
            attributeContext.setAttributeValueType(AttributeValueType.NUMBER);          
         
            attributeContextMap.put(attributePk.getAttributeType(), attributeContext);
        }

        // Parse the features
        for(IntFeature intFeature : intList) {
            if(intFeature.getValue() == null) {
                continue;
            }
            ComponentAttribute attribute = new ComponentAttribute();
            ComponentAttributePk attributePk = new ComponentAttributePk();
            attributePk.setAttributeType(intFeature.getName() + "(" + intFeature.getUnitAbbr() + ")");
            attributePk.setAttributeCode(intFeature.getValue().toString());
            attribute.setComponentAttributePk(attributePk);
            componentAll.getAttributes().add(attribute);
            
            AttributeContext attributeContext = new AttributeContext();
            attributeContext.setComponentType(component.getComponentType());
            attributeContext.setAttributeValueType(AttributeValueType.NUMBER);
            
            attributeContextMap.put(attributePk.getAttributeType(), attributeContext);
        }
        
        // Parse the features
        for(TextFeature textFeature : textList) {
            if(textFeature.getValue() == null) {
                continue;
            }
            ComponentAttribute attribute = new ComponentAttribute();
            ComponentAttributePk attributePk = new ComponentAttributePk();
            attributePk.setAttributeType(textFeature.getName());
            attributePk.setAttributeCode(textFeature.getValue());
            attribute.setComponentAttributePk(attributePk);
            componentAll.getAttributes().add(attribute);
            
            AttributeContext attributeContext = new AttributeContext();
            attributeContext.setComponentType(component.getComponentType());
            attributeContext.setAttributeValueType(AttributeValueType.TEXT);
            
            attributeContextMap.put(attributePk.getAttributeType(), attributeContext);
        }
        
        // Once we have all the features, then we are going to apply then to the componentAll
        componentMapper.applyAttributeMapping(componentAll, attributeContextMap);
        
        int listSize = product.getOrganizations().getRelatedOrganizations().size();
        boolean foundManufacturer = false;
        
        // Logic to determine which organization to name
        if(listSize == 0) {
            component.setOrganization(UNKNOWN_ORGANIZATION);
        } else if(listSize == 1) {
            component.setOrganization(product.getOrganizations().getRelatedOrganizations().get(0).getOrganization().getLongName());            
        } else {
            for(RelatedOrganization relatedOrganization : product.getOrganizations().getRelatedOrganizations()) {
                if(relatedOrganization.getRole().equals(MANUFACTURER_ORG)) {
                    component.setOrganization(relatedOrganization.getOrganization().getLongName());
                    foundManufacturer = true;
                } else if(!foundManufacturer){
                    component.setOrganization(relatedOrganization.getOrganization().getLongName());
                }
                
                if (StringProcessor.stringIsNotBlank(relatedOrganization.getOrganization().getShortName())) {
                    descriptionMetaData += "<strong>Organization Short Name: </strong>" + relatedOrganization.getOrganization().getShortName() + "<br>";
                }
                if (StringProcessor.stringIsNotBlank(relatedOrganization.getOrganization().getLongName())) {
                    descriptionMetaData += "<strong>Organization Long Name: </strong>" + relatedOrganization.getOrganization().getLongName() + "<br>";
                }
                if (StringProcessor.stringIsNotBlank(relatedOrganization.getOrganization().getDescription())) {
                    descriptionMetaData += "<strong>Organization Description: </strong>" + relatedOrganization.getOrganization().getDescription() + "<br>";
                }
                if (StringProcessor.stringIsNotBlank(relatedOrganization.getOrganization().getType())) {
                    descriptionMetaData += "<strong>Type: </strong>" + relatedOrganization.getOrganization().getType() + "<br>";
                } 
            }
        }
        
        if(StringProcessor.stringIsNotBlank(product.getDescription())){
            component.setDescription(descriptionMetaData + "<br>" + product.getDescription());
        }
        
        // For every piece of provenance documentaton we need to add it as a resource to the component
        for(RevisionProvenanceWebsite revisionProvenanceWebsite : product.getProductRevision().getProvenance().getWebsites()) {
            ComponentResource componentResource = new ComponentResource();
            boolean snapshotExists = false;
            String lookUpResourceType = getLookup(ResourceType.class, ResourceType.HOME_PAGE);
            componentResource.setResourceType(lookUpResourceType);
            
            String componentResourceDescription = "<br>";
            if (StringProcessor.stringIsNotBlank(revisionProvenanceWebsite.getSnapshotUrl())) {
                componentResourceDescription += "<strong>Snapshot URL: </strong>" + revisionProvenanceWebsite.getSnapshotUrl() + "<br>";
                snapshotExists = true;
            }
            if(StringProcessor.stringIsNotBlank(revisionProvenanceWebsite.getUrl())) {
                if(snapshotExists){
                    componentResourceDescription += "<strong>Website URL: </strong>" + revisionProvenanceWebsite.getUrl() + "<br>";
                } else {
                    if(revisionProvenanceWebsite.getUrl().startsWith("https:") || revisionProvenanceWebsite.getUrl().startsWith("http:")){
                       componentResource.setLink(revisionProvenanceWebsite.getUrl());
                    }
                    else {
                        // otherwise use a prepend
                        String linkUrl = HTTP_PREPENDER + revisionProvenanceWebsite.getUrl();
                        componentResource.setLink(linkUrl);
                    }
                }              
            }

            if (StringProcessor.stringIsNotBlank(revisionProvenanceWebsite.getSnapshotId())) {

                // ADD TO LIST HERE
                String key = fileHistoryAll.getFileHistory().getFileHistoryId() + KEY_SPLITTER + revisionProvenanceWebsite.getSnapshotId();
                componentResource.setExternalId(key);
                resourceWebKeys.add(key);
            }
            if (StringProcessor.stringIsNotBlank(revisionProvenanceWebsite.getTitle())) {
                componentResourceDescription += "<strong>Website Title: </strong>"  + revisionProvenanceWebsite.getTitle() + "<br>";
            }
            if (StringProcessor.stringIsNotBlank(revisionProvenanceWebsite.getDescription())) {
                componentResourceDescription += "<br><strong>Website Description: </strong>" + revisionProvenanceWebsite.getDescription()  + "<br>";
            }
            
            componentResource.setDescription(componentResourceDescription);
            componentAll.getResources().add(componentResource);
        }
        
        for (RevisionProvenanceDocument revisionProvenanceDocument : product.getProductRevision().getProvenance().getDocuments()) {
            ComponentResource componentResource = new ComponentResource();
            String lookUpResourceType = getLookup(ResourceType.class, ResourceType.DOCUMENT);
            componentResource.setResourceType(lookUpResourceType);
            
            String componentResourceDescription = "<br>";

            if (StringProcessor.stringIsNotBlank(revisionProvenanceDocument.getKey())) {
                // Save key and add to list
                String key = fileHistoryAll.getFileHistory().getFileHistoryId() + KEY_SPLITTER + revisionProvenanceDocument.getKey();
                componentResource.setExternalId(key);
                resourceDocKeys.add(key);
            }
            if (StringProcessor.stringIsNotBlank(revisionProvenanceDocument.getFilename())) {

                MediaFile mediaFile = new MediaFile();
                mediaFile.setOriginalName(revisionProvenanceDocument.getFilename());
                mediaFile.setMimeType(OpenStorefrontConstant.getMimeForFileExtension(revisionProvenanceDocument.getType()));
                
                componentResource.setFile(mediaFile);
            }
            if (StringProcessor.stringIsNotBlank(revisionProvenanceDocument.getTitle())) {
                componentResourceDescription += "<strong>Document Title: </strong>" + revisionProvenanceDocument.getTitle() + "<br>";
            }
            if (StringProcessor.stringIsNotBlank(revisionProvenanceDocument.getDescription())) {
                componentResourceDescription += "<strong>Document Description: </strong>" + revisionProvenanceDocument.getDescription() + "<br>";
            }
            
            componentResource.setDescription(componentResourceDescription);
            componentAll.getResources().add(componentResource);
        }
        
        // We need to check if there is an image or images for us to use.
        String imageFileName = reader.checkForProductImage(product.getKey());
        if(StringProcessor.stringIsNotBlank(imageFileName)) {

            ComponentMedia componentMedia = new ComponentMedia();
            String lookUpMediaCode = getLookup(MediaType.class, MediaType.IMAGE);
            componentMedia.setMediaTypeCode(lookUpMediaCode);
            componentMedia.setCaption(imageFileName);
            componentMedia.setActiveStatus(ComponentMedia.ACTIVE_STATUS);            

            String key = fileHistoryAll.getFileHistory().getFileHistoryId() + KEY_SPLITTER + imageFileName;
            componentMedia.setExternalId(key);
            resourceMediaKeys.add(key);

            MediaFile mediaFile = new MediaFile();
            mediaFile.setOriginalName(imageFileName);
            mediaFile.setMimeType(OpenStorefrontConstant.getMimeForFileExtension(StringProcessor.getFileExtension(imageFileName)));
            mediaFile.setFileType(MediaFileType.MEDIA);

            componentMedia.setFile(mediaFile);
            
            componentAll.getMedia().add(componentMedia);
            
        }
        
        return componentAll;
    }

    @Override
    protected void finishProcessing() {
        super.finishProcessing(); //To change body of generated methods, choose Tools | Templates.
        
        // Work through the provenance items and build each resource.
        for(String key : resourceWebKeys) {
            ComponentResource componentResource = new ComponentResource();
            componentResource.setExternalId(key);
            componentResource = componentResource.find();
            String webId = key.split(KEY_SPLITTER)[1];
            ZipEntry zipFileEntry = reader.getFileFromWebKey(webId);
            if(zipFileEntry != null) {
                String fileExtension = StringProcessor.getFileExtension(zipFileEntry.getName());
                
                InputStream in = reader.getZipFileEntry(webId);
                service.getComponentService().saveResourceFile(componentResource, in, OpenStorefrontConstant.getMimeForFileExtension(fileExtension), SNAPSHOT_FILE_NAME + fileExtension);
            }
        }
        
        for (String key : resourceDocKeys) {
            ComponentResource componentResource = new ComponentResource();
            componentResource.setExternalId(key);
            componentResource = componentResource.find();
            
            String docId = key.split(KEY_SPLITTER)[1];
            String zipFileName = docId + "_" + componentResource.getFile().getOriginalName();
            InputStream in = reader.getZipFileEntry(zipFileName);
            if(in != null) {
                service.getComponentService().saveResourceFile(componentResource, in, componentResource.getFile().getMimeType(), componentResource.getFile().getOriginalName());
            }

        }
        
        for (String key : resourceMediaKeys) {
            ComponentMedia componentMedia = new ComponentMedia();
            componentMedia.setExternalId(key);
            componentMedia = componentMedia.find();

            String zipFileName = key.split(KEY_SPLITTER)[1];
            InputStream in = reader.getZipFileEntry(zipFileName);
            if (in != null) {
                service.getComponentService().saveMediaFile(componentMedia, in, componentMedia.getFile().getMimeType(), componentMedia.getFile().getOriginalName());
            }

        }
    }

}
