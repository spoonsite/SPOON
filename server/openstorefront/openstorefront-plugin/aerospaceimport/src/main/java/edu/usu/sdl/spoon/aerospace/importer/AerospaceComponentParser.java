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
import edu.usu.sdl.openstorefront.core.entity.ComponentResource;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;
import edu.usu.sdl.openstorefront.core.entity.ResourceType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.spi.parser.BaseComponentParser;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.ComponentMapper;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.AttributeContext;
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
import java.util.zip.ZipEntry;

/**
 *
 * @author rfrazier
 */
public class AerospaceComponentParser
        extends BaseComponentParser {

    private static final String UNKNOWN_ORGANIZATION = "Unknown";
    private static final String MANUFACTURER_ORG = "Manufacturer";
    public static final String FORMAT_CODE = "AEROSPACECMP";
    private static final String KEY_SPLITTER = "#";
    
    private List<String> resourceWebKeys = new ArrayList<String>();
    private List<String> resourceDocKeys = new ArrayList<String>();
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
        component.setActiveStatus(Component.ACTIVE_STATUS);
        component.setApprovalState(ApprovalStatus.APPROVED);
        component.setExternalId(Integer.toString(product.getKey()));
        
        if(StringProcessor.stringIsNotBlank(product.getLongName())){
            // use long name
            component.setName(product.getLongName());
        } else if(StringProcessor.stringIsNotBlank(product.getShortName())) {
            // use short name
            component.setName(product.getShortName());
        } else {
            component.setName("AeroSpaceImport, name is not available.");
        }
        
        descriptionMetaData = "<strong>Long Name: </strong>" + product.getLongName() + "<br>"
                                + "<strong>Product Source: </strong>" + product.getProductSource() + "<br>"
                                + "<strong>Model Number: </strong>" + product.getModelNumber()+ "<br>"
                                + "<strong>From Date: </strong>" + product.getProductRevision().getFromDate() + "<br>"
                                + "<strong>Through Date: </strong>" + product.getProductRevision().getThruDate() + "<br>";
        
        component.setComponentType(parser.getComponentType(product));
        Map<String, AttributeContext> attributeContextMap = new HashMap<>();
        
        if(product.getProductRevision().getProductType().getClassification().size() > 0) {
            ComponentAttribute attribute = new ComponentAttribute();
            ComponentAttributePk attributePk = new ComponentAttributePk();
            attributePk.setAttributeType("Product Type");
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
      
        floatList.addAll(product.getProductRevision().getSpecs().getFloatFeatures());
        floatList.addAll(product.getProductRevision().getShape().getFloatFeatures());
        floatList.addAll(product.getProductRevision().getAdditional().getFloatFeatures());
        intList.addAll(product.getProductRevision().getSpecs().getIntFeatures());
        intList.addAll(product.getProductRevision().getShape().getIntFeatures());
        intList.addAll(product.getProductRevision().getAdditional().getIntFeatures());
        textList.addAll(product.getProductRevision().getSpecs().getTextFeatures());
        textList.addAll(product.getProductRevision().getShape().getTextFeatures());
        textList.addAll(product.getProductRevision().getAdditional().getTextFeatures());
                
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
            attributeContext.setAttributeDescription("<strong>Name: </strong>" + floatFeature.getName() + "<br>" 
                                                     + "<strong>Description: </strong>" + floatFeature.getDescription() + "<br>" 
                                                     + "<strong>Value Description: </strong>" + floatFeature.getValueDescription() + "<br>"
                                                     + "<strong>Value Type: </strong>" + floatFeature.getType() + "<br>"
                                                     + "<strong>Unit: </strong>" + floatFeature.getUnit()+ "<br>");
            attributeContextMap.put(attributePk.getAttributeType(), attributeContext);
        }

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
            attributeContext.setAttributeDescription("<strong>Name: </strong>" + intFeature.getName() + "<br>"
                                                    + "<strong>Description: </strong>" + intFeature.getDescription() + "<br>"
                                                    + "<strong>Value Description: </strong>" + intFeature.getValueDescription() + "<br>"
                                                    + "<strong>Value Type: </strong>" + intFeature.getType() + "<br>"
                                                    + "<strong>Unit: </strong>" + intFeature.getUnit() + "<br>");
            attributeContextMap.put(attributePk.getAttributeType(), attributeContext);
        }
        
        for(TextFeature textFeature : textList) {
            ComponentAttribute attribute = new ComponentAttribute();
            ComponentAttributePk attributePk = new ComponentAttributePk();
            attributePk.setAttributeType(textFeature.getName());
            attributePk.setAttributeCode(textFeature.getValue());
            attribute.setComponentAttributePk(attributePk);
            componentAll.getAttributes().add(attribute);
            
            AttributeContext attributeContext = new AttributeContext();
            attributeContext.setComponentType(component.getComponentType());
            attributeContext.setAttributeValueType(AttributeValueType.TEXT);
            attributeContext.setAttributeDescription("<strong>Name: </strong>" + textFeature.getName() + "<br>"
                                                    + "<strong>Description: </strong>" + textFeature.getDescription() + "<br>"
                                                    + "<strong>Value Description: </strong>" + textFeature.getValueDescription() + "<br>"
                                                    + "<strong>Value Type: </strong>" + textFeature.getType() + "<br>"
                                                    + "<strong>Value: </strong>" + textFeature.getValue() + "<br>");
            attributeContextMap.put(attributePk.getAttributeType(), attributeContext);
        }
        
        componentMapper.applyAttributeMapping(componentAll, attributeContextMap);
        
        int listSize = product.getOrganizations().getRelatedOrganizations().size();
        boolean foundManufacturer = false;
        
        if(listSize == 0) {
            component.setOrganization(UNKNOWN_ORGANIZATION);
        } else if(listSize == 1) {
            component.setOrganization(product.getOrganizations().getRelatedOrganizations().get(0).getOrganization().getLongName());
            RelatedOrganization rOrg = product.getOrganizations().getRelatedOrganizations().get(0);
            descriptionMetaData += "<strong>Organization Short Name: </strong>" + rOrg.getOrganization().getShortName() + "<br>"
                    + "<strong>Organization Long Name: </strong>" + rOrg.getOrganization().getLongName() + "<br>"
                    + "<strong>Organization Description: </strong>" + rOrg.getOrganization().getDescription() + "<br>"
                    + "<strong>Type: </strong>" + rOrg.getOrganization().getType() + "<br>";
        } else {
            for(RelatedOrganization relatedOrganization : product.getOrganizations().getRelatedOrganizations()) {
                if(relatedOrganization.getRole().equals(MANUFACTURER_ORG)) {
                    component.setOrganization(relatedOrganization.getOrganization().getLongName());
                    foundManufacturer = true;
                } else if(!foundManufacturer){
                    component.setOrganization(relatedOrganization.getOrganization().getLongName());
                }
                descriptionMetaData += "<strong>Organization Short Name: </strong>" + relatedOrganization.getOrganization().getShortName() + "<br>"
                                     + "<strong>Organization Long Name: </strong>" + relatedOrganization.getOrganization().getLongName() + "<br>"
                                     + "<strong>Organization Description: </strong>" + relatedOrganization.getOrganization().getDescription() + "<br>"
                                     + "<strong>Type: </strong>" + relatedOrganization.getOrganization().getType() + "<br>";
            }
        }
        
        component.setDescription(descriptionMetaData + "<br>" + product.getDescription());
        
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
                    componentResource.setLink(revisionProvenanceWebsite.getUrl());
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
            if (StringProcessor.stringIsNotBlank(revisionProvenanceWebsite.getLastVisited())) {
                componentResourceDescription += "<strong>Last Visited Date: </strong>" + revisionProvenanceWebsite.getLastVisited() + "<br>";
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
                componentResourceDescription += "<strong>Document Descritption: </strong>" + revisionProvenanceDocument.getDescription() + "<br>";
            }
            
            componentResource.setDescription(componentResourceDescription);
            componentAll.getResources().add(componentResource);
        }
        
        return componentAll;
    }

    @Override
    protected void finishProcessing() {
        super.finishProcessing(); //To change body of generated methods, choose Tools | Templates.
        
        for(String key : resourceWebKeys) {
            ComponentResource componentResource = new ComponentResource();
            componentResource.setExternalId(key);
            componentResource = componentResource.find();
            String webId = key.split(KEY_SPLITTER)[1];
            ZipEntry zipFileEntry = reader.getFileFromWebKey(webId);
            if(zipFileEntry != null) {
                String fileExtension = StringProcessor.getFileExtension(zipFileEntry.getName());
                
                InputStream in = reader.getZipFileEntry(webId);
                service.getComponentService().saveResourceFile(componentResource, in, OpenStorefrontConstant.getMimeForFileExtension(fileExtension), "Snapshot." + fileExtension);
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
    }

}























































