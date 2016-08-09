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
package edu.usu.sdl.openstorefront.core.spi.parser.mapper;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.core.entity.DataMapTransform;
import edu.usu.sdl.openstorefront.core.entity.FileDataMapField;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class DataMapper
{
	private static final Logger LOG = Logger.getLogger(DataMapper.class.getName());
	
	private String field;
	private boolean rootField;
	private List<DataTransform> transforms = new ArrayList<>();
	private Class entityClass;
	private String entityField;
	private boolean useAsAttributeLabel;
	private boolean concatenate;
	private boolean addEndPathToValue;
	private boolean fileAttachment;
	private String pathToEnityField;
	private List<DataTransform> pathTransforms = new ArrayList<>();

	public DataMapper()
	{
	}
	
	public static DataMapper toDataMapper(FileDataMapField fileDataMapField) 
	{
		DataMapper dataMapper = null;
		try {
			dataMapper = new DataMapper();
			dataMapper.setPathToEnityField(fileDataMapField.getPathToEnityField());
			dataMapper.setAddEndPathToValue(Convert.toBoolean(fileDataMapField.getAddEndPathToValue()));
			dataMapper.setConcatenate(Convert.toBoolean(fileDataMapField.getConcatenate()));			
			dataMapper.setEntityClass(DataMapper.class.getClassLoader().loadClass(fileDataMapField.getEntityClass()));
			dataMapper.setEntityField(fileDataMapField.getEntityField());
			dataMapper.setField(fileDataMapField.getField());
			dataMapper.setUseAsAttributeLabel(Convert.toBoolean(fileDataMapField.getUseAsAttributeLabel()));
			dataMapper.setFileAttachment(Convert.toBoolean(fileDataMapField.getFileAttachment()));
			
			if (fileDataMapField.getTransforms() != null) {				
				for (DataMapTransform dataMapTransform : fileDataMapField.getTransforms()) {
					DataTransform dataTransform = getDataTransform(dataMapTransform.getTransform());
					if (dataTransform != null) {
						dataMapper.getTransforms().add(dataTransform);
					}
				}
			}

			if (fileDataMapField.getPathTransforms() != null) {
				for (DataMapTransform dataMapTransform : fileDataMapField.getPathTransforms()) {
					DataTransform dataTransform = getDataTransform(dataMapTransform.getTransform());
					if (dataTransform != null) {
						dataMapper.getPathTransforms().add(dataTransform);
					}
				}
			}
			
		} catch (ClassNotFoundException ex) {
			throw new OpenStorefrontRuntimeException("Unable to find class", "Check class: " + fileDataMapField.getEntityClass());
		}
		return dataMapper;
	}
	
	private static DataTransform getDataTransform(String transform) 
	{
		DataTransform dataTransform = null;
		
		if (StringUtils.isNotBlank(transform)) {
			try {
				dataTransform = StringTransforms.valueOf(transform);
			} catch (IllegalArgumentException ioe) {
				try {
					dataTransform = TypeTransforms.valueOf(transform);
				} catch (IllegalArgumentException ioex) {
					LOG.log(Level.WARNING, MessageFormat.format("Unable to find transform:  {0}", transform));
				}
			}
		}
		
		return dataTransform;
	}
	
	public Object applyTransforms(String value) 
	{
		Object transformedData = value;
		for (DataTransform dataTransform : transforms) {
			transformedData = dataTransform.transform(transformedData);
		}
		return transformedData;
	}
	
	public Object applyPathTransforms(String value)
	{
		Object transformedData = value;
		for (DataTransform dataTransform : pathTransforms) {
			transformedData = dataTransform.transform(transformedData);
		}
		return transformedData;
	}	

	public String getField()
	{
		return field;
	}

	public void setField(String field)
	{
		this.field = field;
	}

	public List<DataTransform> getTransforms()
	{
		return transforms;
	}

	public void setTransforms(List<DataTransform> transforms)
	{
		this.transforms = transforms;
	}

	public String getEntityField()
	{
		return entityField;
	}

	public void setEntityField(String entityField)
	{
		this.entityField = entityField;
	}

	public Class getEntityClass()
	{
		return entityClass;
	}

	public void setEntityClass(Class entityClass)
	{
		this.entityClass = entityClass;
	}

	public boolean getRootField()
	{
		return rootField;
	}

	public void setRootField(boolean rootField)
	{
		this.rootField = rootField;
	}

	public boolean getUseAsAttributeLabel()
	{
		return useAsAttributeLabel;
	}

	public void setUseAsAttributeLabel(boolean useAsAttributeLabel)
	{
		this.useAsAttributeLabel = useAsAttributeLabel;
	}

	public boolean getConcatenate()
	{
		return concatenate;
	}

	public void setConcatenate(boolean concatenate)
	{
		this.concatenate = concatenate;
	}

	public List<DataTransform> getPathTransforms()
	{
		return pathTransforms;
	}

	public void setPathTransforms(List<DataTransform> pathTransforms)
	{
		this.pathTransforms = pathTransforms;
	}

	public String getPathToEnityField()
	{
		return pathToEnityField;
	}

	public void setPathToEnityField(String pathToEnityField)
	{
		this.pathToEnityField = pathToEnityField;
	}

	public boolean getAddEndPathToValue()
	{
		return addEndPathToValue;
	}

	public void setAddEndPathToValue(boolean addEndPathToValue)
	{
		this.addEndPathToValue = addEndPathToValue;
	}

	public boolean getAttachment()
	{
		return fileAttachment;
	}

	public void setFileAttachment(boolean fileAttachment)
	{
		this.fileAttachment = fileAttachment;
	}

}
