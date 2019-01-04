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


import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.model.FileHistoryAll;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminView;
import edu.usu.sdl.openstorefront.core.view.ComponentAdminWrapper;
import edu.usu.sdl.openstorefront.core.view.ComponentFilterParams;
import edu.usu.sdl.spoon.aerospace.importer.model.Product;
import edu.usu.sdl.spoon.aerospace.importer.model.Services;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 *
 * @author bmichaelis
 */
public class AerospaceReader 
        extends GenericReader<Product>
{
    private static final Logger LOG = Logger.getLogger(AerospaceReader.class.getName());
    public static final String XMLFILE = "recordsList.xml";
    private static final String NAME_SORT_FIELD = "name";
    private static final String PRODUCT_IMAGES_FOLDER_NAME  = "product_images/";
    
    private ZipFile zipFile;
    private List<Product> productList = new ArrayList<>();
    private Iterator<Product> productIterator;
    protected Service service = ServiceProxyFactory.getServiceProxy();
    
    public List<ComponentAdminView> masterComponentAdminViewList = new ArrayList<>();

    public AerospaceReader(InputStream in, FileHistoryAll fileHistoryAll) throws IOException {
        super(in);
        
        zipFile = new ZipFile(fileHistoryAll.getFileHistory().pathToFileName().toFile());       
        
    }
    
   public void preProcess() {
        try {
            AerospaceXMLParser aeroXMLParser = new AerospaceXMLParser();
            ZipEntry zipEntry = zipFile.getEntry(XMLFILE);
            Services services = aeroXMLParser.parseXML(zipFile.getInputStream(zipEntry));
            productList.addAll(services.getProducts());
            totalRecords = productList.size();
            productIterator = productList.iterator();
            
            ComponentFilterParams filterQueryParams = new ComponentFilterParams();
            filterQueryParams.setMax(20000);
            filterQueryParams.setApprovalState(ComponentFilterParams.FILTER_ALL);
            filterQueryParams.setComponentType(ComponentFilterParams.FILTER_ALL);
            filterQueryParams.setSortField(NAME_SORT_FIELD);
            filterQueryParams.setStatus(ComponentFilterParams.FILTER_ALL);
            filterQueryParams.setName("");
            filterQueryParams.setComponentName("");
            ComponentAdminWrapper componentAdminWrapper = service.getComponentService().getFilteredComponents(filterQueryParams, null);
            
            for(ComponentAdminView componentAdminView : componentAdminWrapper.getComponents()) {
                masterComponentAdminViewList.add(componentAdminView);
            }              
            
        } catch (IOException ex) {
            Logger.getLogger(AerospaceReader.class.getName()).log(Level.SEVERE, null, ex);
        }
       
   }

    @Override
    public Product nextRecord() {
        if(productIterator.hasNext()){
            currentRecordNumber++;
            return productIterator.next();
        }
        return null;
    }
    
    
    @Override
    public void close() throws Exception
    {
        super.close();
        zipFile.close();
    }
    
    public InputStream getZipFileEntry(String entryName) {
        InputStream in = null;
        ZipEntry zipEntry = zipFile.getEntry(entryName);
        
        if(zipEntry == null) {
            zipEntry = getFileFromWebKey(entryName);
        }
        
        if(zipEntry != null) {
            try {
                in = zipFile.getInputStream(zipEntry);
            } catch (IOException ex) {
                LOG.log(Level.WARNING, "Cannot read file for ZipEntry; " + entryName, ex);
            }
        }
        
        return in;
    }
    
    public ZipEntry getFileFromWebKey(String fileName) {
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        ZipEntry found = null;
        while(entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            if(zipEntry.getName().contains(fileName)) {
                found = zipEntry;
                break;
            }
        }
        
        return found;
        
    }
    
    public String checkForProductImage(int pk) {
        String mediaName = "";
        Integer productKey = pk;
        String stringKey = productKey.toString();
        stringKey+= "_";
        
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            ZipEntry zipEntry = entries.nextElement();
            String fileName = zipEntry.getName();
            if(fileName.contains(PRODUCT_IMAGES_FOLDER_NAME) && fileName.substring(PRODUCT_IMAGES_FOLDER_NAME.length()).startsWith(stringKey)) {
                mediaName = fileName.substring(PRODUCT_IMAGES_FOLDER_NAME.length());
                break;
            }
        }
              
        return mediaName;
    }
   
    
}
