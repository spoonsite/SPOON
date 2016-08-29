/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.spoon.importer;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.model.AttributeAll;
import edu.usu.sdl.openstorefront.core.spi.parser.BaseAttributeParser;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.AttributeMapper;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.MapModel;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.XMLMapReader;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author dshurtleff
 */
public class AttributeSpoonParser 
	extends BaseAttributeParser
{
	public static final String FORMAT_CODE = "SPOONATTR";

	private List<AttributeAttachment> attachments = new ArrayList<>();
	private List<AttributeAll> attributeAlls;
	
	@Override
	public String checkFormat(String mimeType, InputStream input)
	{
		if (mimeType.contains("xml")) {
			return "";
		} else {
			return "Invalid format. Please upload a XML file.";
		}		
	}

	@Override
	protected GenericReader getReader(InputStream in)
	{
		return new XMLMapReader(in);
	}

	@Override
	protected String handlePreviewOfRecord(Object data)
	{
		String output = "";
		if (attributeAlls != null && 
			!attributeAlls.isEmpty()) {
			try {			
				output = service.getSystemService().toJson(attributeAlls.get(0));
			} catch (Exception ex) {
				output = "Unable preview attributes.  <br>Trace:<br>" + StringProcessor.parseStackTraceHtml(ex);
			}
		}
		return output;
	}
	
	@Override
	protected <T> Object parseRecord(T record)
	{
		MapModel mapModel = (MapModel) record;
		
		AttributeMapper attributeMapper = new AttributeMapper(() -> {
				AttributeAll attributeAll = defaultAttributeAll();
				return attributeAll;
		}, fileHistoryAll);
			
		attributeAlls  = attributeMapper.multiMapData(mapModel);
		
		Set<String> dedupSet = new HashSet<>();
		int realRecordNumber = 0;
		for (AttributeAll attributeAll : attributeAlls) {			
			realRecordNumber++;
			
			boolean addRecord = true;
			ValidationResult validationResult = attributeAll.validate();
			if (validationResult.valid()) {
				
				//handle saving of attachments					
				//Record should come in order and we will get a phatom record for the attachment
				AttributeCode previousCode = null;
				for (AttributeCode attributeCode : attributeAll.getAttributeCodes()) {
					if (attributeCode.getAttachmentFileName() != null &&
						!"".equals(attributeCode.getAttachmentFileName())) {
						
						AttributeAttachment attributeAttachment = new AttributeAttachment();
						
						if (AttributeAll.ATTACHMENT_CODE.equals(attributeCode.getAttributeCodePk().getAttributeCode())) {
							if (previousCode != null) {
								attributeAttachment.setAttributeCode(previousCode.getAttributeCodePk().getAttributeCode());
								attributeAttachment.setAttributeType(attributeCode.getAttributeCodePk().getAttributeType());
								attributeAttachment.setFileData(attributeCode.getAttachmentFileName());
								attributeAttachment.setFilename(attributeCode.getAttachmentOriginalFileName());								
								attachments.add(attributeAttachment);								
							}
						}						
						
						//Clear the data out
						attributeCode.setAttachmentFileName(null);
						
						addRecord = false;
					} else {
						previousCode = attributeCode;
					}
				}				
				if (addRecord) {										
					//remove bad codes
					for (int i = attributeAll.getAttributeCodes().size() - 1; i>=0; i--) {
						AttributeCode attributeCode = attributeAll.getAttributeCodes().get(i);						
						if (AttributeAll.ATTACHMENT_CODE.equals(attributeCode.getAttributeCodePk().getAttributeCode())) {
							attributeAll.getAttributeCodes().remove(i);
						}
					}
					
					//remove dups caused by attachments
					StringBuilder key = new StringBuilder();
					for (AttributeCode attributeCode : attributeAll.getAttributeCodes()) {
						key.append(attributeCode.getAttributeCodePk().toKey());
					}
					
					if (dedupSet.contains(key.toString()) == false) {
						addRecordToStorage(attributeAll);
						dedupSet.add(key.toString());
					}
				}
				
			} else {
				fileHistoryAll.addError(FileHistoryErrorType.VALIDATION, validationResult.toHtmlString(), realRecordNumber);
			}
		}
				
		return null;
	}

	@Override
	protected void finishProcessing()
	{
		for (AttributeAttachment attachment : attachments) {		
			try {
				AttributeCode attributeCode = new AttributeCode();
				AttributeCodePk attributeCodePk = new AttributeCodePk();
				attributeCodePk.setAttributeCode(attachment.getAttributeCode());
				attributeCodePk.setAttributeType(attachment.getAttributeType());
				attributeCode.setAttributeCodePk(attributeCodePk);
				
				attributeCode.setAttachmentOriginalFileName(attachment.getFilename());
				attributeCode.setAttachmentMimeType(
						OpenStorefrontConstant.getMimeForFileExtension(
								StringProcessor.getFileExtension(attachment.getFilename())
						)
				);				
				
				byte[] fileData = Base64.getMimeDecoder().decode(attachment.getFileData());
				service.getAttributeService().saveAttributeCodeAttachment(attributeCode, new ByteArrayInputStream(fileData));				
			} catch (Exception e) {
				fileHistoryAll.addError(FileHistoryErrorType.SYSTEM, "Unable to add attachment to:  "
						+ attachment.getAttributeType() 
						+ "#" 
						+ attachment.getAttributeCode() + " Attribute Code may have failed to save. <br>Trace: <br>"
						+ StringProcessor.parseStackTraceHtml(e)
				);				
			}
		}
	}
	
	
	
}
