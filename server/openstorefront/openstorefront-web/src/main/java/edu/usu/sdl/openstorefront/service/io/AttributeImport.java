/*
 * Copyright 2014 Space Dynamics Laboratory - Utah State University Research Foundation.
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

package edu.usu.sdl.openstorefront.service.io;

import au.com.bytecode.opencsv.CSVParser;
import edu.usu.sdl.openstorefront.service.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeCodeView;
import edu.usu.sdl.openstorefront.web.rest.model.AttributeTypeView;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class AttributeImport
{
	private static final Logger log = Logger.getLogger(AttributeImport.class.getName());
	
	public List<AttributeTypeView> loadAttributes()
	{
		List<AttributeTypeView> attributeTypeViews = new ArrayList<>();
		
		Map<String, AttributeTypeView> attributeMap = loadAttributeMap();
		attributeTypeViews.addAll(attributeMap.values());	
	
		return attributeTypeViews;
	}
	
	public Map<String, AttributeTypeView> loadAttributeMap()
	{
		Map<String, AttributeTypeView> attributeMap = new HashMap<>();
		
		CSVParser parser = new CSVParser();		
		Path path = Paths.get(FileSystemManager.getImportDir() + "/attributes.csv");		
		try
		{
			
			List<String> lines =  Files.readAllLines(path, Charset.defaultCharset());
			lines.forEach(line ->{				
				if (StringUtils.isNotBlank(line))
				{
					if (line.toLowerCase().startsWith("attribute type") == false)
					{
						try
						{
							String data[] = parser.parseLine(line);	
							String type = data[0].toUpperCase().trim();							
							AttributeTypeView attributeTypeView = null;
							if (attributeMap.containsKey(type))
							{
								attributeTypeView = attributeMap.get(type);
							}
							else
							{							
								attributeTypeView = new AttributeTypeView();
								attributeTypeView.setType(data[0].trim());
								attributeTypeView.setDescription(data[1].trim());
								attributeTypeView.setArchtechtureFlg(Boolean.parseBoolean(data[2].trim()));
								attributeTypeView.setVisibleFlg(Boolean.parseBoolean(data[3].trim()));
								attributeTypeView.setImportantFlg(Boolean.parseBoolean(data[4].trim()));
								attributeTypeView.setRequiredFlg(Boolean.parseBoolean(data[5].trim()));
								attributeTypeView.setAllowMutlipleFlg(Boolean.parseBoolean(data[6].trim()));
								

								attributeMap.put(type, attributeTypeView);
							}
							AttributeCodeView attributeCodeView = new AttributeCodeView();
							attributeCodeView.setCode(data[7].toUpperCase().trim());
							attributeCodeView.setLabel(data[8].trim());

							if (data.length > 9)
							{
								attributeCodeView.setDescription(data[9].trim());
							}
							attributeTypeView.getCodes().add(attributeCodeView);

						} catch (IOException ex)
						{
							log.log(Level.SEVERE, null, ex);
						}
					}
				}
			});
		} catch (IOException ex)
		{
			log.log(Level.SEVERE, null, ex);
		}
			
		
		//load archtec
		AttributeTypeView archView = loadDI2EArch();
		attributeMap.put(archView.getType(), archView);
		
		archView = loadJCFSLArch();
		attributeMap.put(archView.getType(), archView);		

		archView = loadJCAArch();
		attributeMap.put(archView.getType(), archView);		
		
		archView = loadJARMESArch();
		attributeMap.put(archView.getType(), archView);		
		
		return attributeMap;
	}
	
	
	private AttributeTypeView loadDI2EArch()
	{
		AttributeTypeView attributeTypeView = new AttributeTypeView();

		CSVParser parser = new CSVParser();		
		Path path = Paths.get(FileSystemManager.getImportDir() + "/di2esv4.csv");	
		try
		{
			List<String> lines = Files.readAllLines(path);
			//read type
			try
			{
				String data[] = parser.parseLine(lines.get(1));
								
				attributeTypeView.setType(data[0].trim());
				attributeTypeView.setDescription(data[1].trim());
				attributeTypeView.setArchtechtureFlg(Boolean.parseBoolean(data[2].trim()));
				attributeTypeView.setVisibleFlg(Boolean.parseBoolean(data[3].trim()));
				attributeTypeView.setImportantFlg(Boolean.parseBoolean(data[4].trim()));
				attributeTypeView.setRequiredFlg(Boolean.parseBoolean(data[5].trim()));
				attributeTypeView.setAllowMutlipleFlg(Boolean.parseBoolean(data[6].trim()));
				
				for (int i=2; i<lines.size(); i++)				
				{
					if (StringUtils.isNotBlank(lines.get(i)))
					{
						data = parser.parseLine(lines.get(i));					
						AttributeCodeView attributeCodeView = new AttributeCodeView();
						if (StringUtils.isNotBlank(data[0].trim()))
						{
							attributeCodeView.setCode(data[0].toUpperCase().trim());
							attributeCodeView.setLabel(data[0].toUpperCase().trim() + " " + data[1].trim());
							attributeTypeView.getCodes().add(attributeCodeView);
						}
					}
				}				
				

			} catch (IOException ex)
			{
				log.log(Level.SEVERE, null, ex);
			}		
			
		} catch (IOException ex)
		{
	
			log.log(Level.SEVERE, null, ex);
		}
		
		return attributeTypeView;
	}
	
	private AttributeTypeView loadJCFSLArch()
	{
		AttributeTypeView attributeTypeView = new AttributeTypeView();
		Set<String> codeSet = new HashSet();
		
		CSVParser parser = new CSVParser();		
		Path path = Paths.get(FileSystemManager.getImportDir() + "/jcfsl.csv");				
		try
		{
			List<String> lines = Files.readAllLines(path);
			//read type
			try
			{
				String data[] = parser.parseLine(lines.get(1));
								
				attributeTypeView.setType(data[0].trim());
				attributeTypeView.setDescription(data[1].trim());
				attributeTypeView.setArchtechtureFlg(Boolean.parseBoolean(data[2].trim()));
				attributeTypeView.setVisibleFlg(Boolean.parseBoolean(data[3].trim()));
				attributeTypeView.setImportantFlg(Boolean.parseBoolean(data[4].trim()));
				attributeTypeView.setRequiredFlg(Boolean.parseBoolean(data[5].trim()));
				attributeTypeView.setAllowMutlipleFlg(Boolean.parseBoolean(data[6].trim()));
				
				for (int i=2; i<lines.size(); i++)				
				{
					if (StringUtils.isNotBlank(lines.get(i)))
					{
						data = parser.parseLine(lines.get(i));					
						AttributeCodeView attributeCodeView = new AttributeCodeView();
						String code[]  = data[0].split(" ");
						
						if (code.length > 0)
						{
							String codeKey = code[0].toUpperCase().trim();
							if (codeSet.contains(codeKey) == false)
							{
								if (StringUtils.isNotBlank(codeKey))
								{
									attributeCodeView.setCode(codeKey);
									attributeCodeView.setLabel(data[0].trim());
									attributeTypeView.getCodes().add(attributeCodeView);
									codeSet.add(codeKey);
								}
							}
						}
					}
				}				
				

			} catch (IOException ex)
			{
				log.log(Level.SEVERE, null, ex);
			}	
			

		} catch (IOException ex)
		{
	
			log.log(Level.SEVERE, null, ex);
		}
		
		return attributeTypeView;
	}
	

	private AttributeTypeView loadJCAArch()
	{
		AttributeTypeView attributeTypeView = new AttributeTypeView();
		
		CSVParser parser = new CSVParser();		
		Path path = Paths.get(FileSystemManager.getImportDir() + "/jca.csv");				
		try
		{
			List<String> lines = Files.readAllLines(path, Charset.defaultCharset());
			//read type
			try
			{
				String data[] = parser.parseLine(lines.get(1));
								
				attributeTypeView.setType(data[0].trim());
				attributeTypeView.setDescription(data[1].trim());
				attributeTypeView.setArchtechtureFlg(Boolean.parseBoolean(data[2].trim()));
				attributeTypeView.setVisibleFlg(Boolean.parseBoolean(data[3].trim()));
				attributeTypeView.setImportantFlg(Boolean.parseBoolean(data[4].trim()));
				attributeTypeView.setRequiredFlg(Boolean.parseBoolean(data[5].trim()));
				attributeTypeView.setAllowMutlipleFlg(Boolean.parseBoolean(data[6].trim()));
				
				for (int i=2; i<lines.size(); i++)				
				{
					if (StringUtils.isNotBlank(lines.get(i)))
					{
						data = parser.parseLine(lines.get(i));					
						AttributeCodeView attributeCodeView = new AttributeCodeView();
						if (StringUtils.isNotBlank(data[0].trim()))
						{							
							attributeCodeView.setCode(data[0].toUpperCase().trim());

							StringBuilder label = new StringBuilder();
							for (int j=1; j<data.length; j++)
							{
								label.append(data[j]);
							}

							attributeCodeView.setLabel(data[0].toUpperCase().trim() + " " + label.toString().trim());					
							attributeTypeView.getCodes().add(attributeCodeView);
						}
					}
				}				
				

			} catch (IOException ex)
			{
				log.log(Level.SEVERE, null, ex);
			}		
			
		} catch (IOException ex)
		{
	
			log.log(Level.SEVERE, null, ex);
		}
		
		return attributeTypeView;	
	}
	
	private AttributeTypeView loadJARMESArch()
	{
		AttributeTypeView attributeTypeView = new AttributeTypeView();

		CSVParser parser = new CSVParser();		
		Path path = Paths.get(FileSystemManager.getImportDir() + "/jarmesl.csv");	
		try
		{
			List<String> lines = Files.readAllLines(path);
			//read type
			try
			{
				String data[] = parser.parseLine(lines.get(1));
								
				attributeTypeView.setType(data[0].trim());
				attributeTypeView.setDescription(data[1].trim());
				attributeTypeView.setArchtechtureFlg(Boolean.parseBoolean(data[2].trim()));
				attributeTypeView.setVisibleFlg(Boolean.parseBoolean(data[3].trim()));
				attributeTypeView.setImportantFlg(Boolean.parseBoolean(data[4].trim()));
				attributeTypeView.setRequiredFlg(Boolean.parseBoolean(data[5].trim()));
				attributeTypeView.setAllowMutlipleFlg(Boolean.parseBoolean(data[6].trim()));
				
				for (int i=2; i<lines.size(); i++)				
				{
					if (StringUtils.isNotBlank(lines.get(i)))
					{
						data = parser.parseLine(lines.get(i));					
						AttributeCodeView attributeCodeView = new AttributeCodeView();
						if (StringUtils.isNotBlank(data[0].trim()))
						{		
							attributeCodeView.setCode(data[0].toUpperCase().trim());
							attributeCodeView.setLabel(data[0].toUpperCase().trim() + " " + data[1].trim());
							attributeTypeView.getCodes().add(attributeCodeView);
						}
					}
				}				
				

			} catch (IOException ex)
			{
				log.log(Level.SEVERE, null, ex);
			}		
			
		} catch (IOException ex)
		{
	
			log.log(Level.SEVERE, null, ex);
		}
		
		return attributeTypeView;		
	}	
	
}
