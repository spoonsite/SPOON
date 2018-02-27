/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.io.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.manager.FileSystemManager;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.FileHistoryErrorType;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.spi.parser.BaseComponentParser;
import edu.usu.sdl.openstorefront.core.spi.parser.reader.GenericReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.java.truevfs.access.TFile;
import net.java.truevfs.access.TFileInputStream;

/**
 *
 * @author dshurtleff
 */
public class ComponentStandardParser
		extends BaseComponentParser
{

	private static final Logger LOG = Logger.getLogger(ComponentStandardParser.class.getName());

	private Set<String> allowTextTypes = new HashSet<>();
	private Set<String> allowZipTypes = new HashSet<>();

	public ComponentStandardParser()
	{
		allowTextTypes.add("text/json");
		allowTextTypes.add("text");
		allowTextTypes.add("application/octet-stream");
		allowTextTypes.add("application/json");

		//Some times the browsers mark JSON file as this. Meaning they don't know what it is.
		allowTextTypes.add("application/octet-stream");

		allowZipTypes.add("application/zip");
		allowZipTypes.add("application/x-zip-compressed");
	}

	@Override
	public String checkFormat(String mimeType, InputStream input)
	{
		StringBuilder sb = new StringBuilder();

		if (allowTextTypes.contains(mimeType) == false
				&& allowZipTypes.contains(mimeType) == false) {
			sb.append("Format not supported.  Requires json text file or zip file.");
		}

		return sb.toString();
	}

	@Override
	protected GenericReader getReader(InputStream in)
	{
		return new GenericReader<ComponentAll>(in)
		{

			private List<ComponentAll> componentAll = new ArrayList<>();

			@Override
			public void preProcess()
			{
				//check
				if (allowTextTypes.contains(fileHistoryAll.getFileHistory().getMimeType())) {
					try (InputStream inTemp = in) {
						componentAll = StringProcessor.defaultObjectMapper().readValue(inTemp, new TypeReference<List<ComponentAll>>()
						{
						});
					} catch (IOException ex) {
						throw new OpenStorefrontRuntimeException(ex);
					}
				} else {

					TFile archive = new TFile(fileHistoryAll.getFileHistory().pathToFileName().toFile().toString());
					if (archive == null || archive.listFiles() == null) {
						throw new OpenStorefrontRuntimeException("Unsupported format.", "Check file; this format only accpets JSON or ZIP with vaild JSON record.");
					}

					TFile archiveFiles[] = archive.listFiles();
					if (archiveFiles != null) {
						for (TFile file : archiveFiles) {
							if (file.isFile()) {
								try (InputStream inTemp = new TFileInputStream(file)) {
									componentAll = StringProcessor.defaultObjectMapper().readValue(inTemp, new TypeReference<List<ComponentAll>>()
									{
									});
								} catch (IOException ex) {
									throw new OpenStorefrontRuntimeException(ex);
								}
							} else if (file.isDirectory() && "media".equalsIgnoreCase(file.getName())) {
								TFile media[] = file.listFiles();
								if (media != null) {
									for (TFile mediaFile : media) {
										try {
											Files.copy(mediaFile.toPath(), FileSystemManager.getInstance().getDir(FileSystemManager.MEDIA_DIR).toPath().resolve(mediaFile.getName()), StandardCopyOption.REPLACE_EXISTING);
										} catch (IOException ex) {
											LOG.log(Level.WARNING, MessageFormat.format("Failed to copy media to path file: {0}", mediaFile.getName()), ex);
										}
									}
								}
							} else if (file.isDirectory() && "resources".equalsIgnoreCase(file.getName())) {
								TFile resources[] = file.listFiles();
								if (resources != null) {
									for (TFile resourceFile : resources) {
										try {
											Files.copy(resourceFile.toPath(), FileSystemManager.getInstance().getDir(FileSystemManager.RESOURCE_DIR).toPath().resolve(resourceFile.getName()), StandardCopyOption.REPLACE_EXISTING);
										} catch (IOException ex) {
											LOG.log(Level.WARNING, MessageFormat.format("Failed to copy resource to path file: {0}", resourceFile.getName()), ex);
										}
									}
								}
							}
						}
					} else {
						fileHistoryAll.addError(FileHistoryErrorType.PARSE, "No files in archive.");
					}
				}
				totalRecords = componentAll.size();
			}

			@Override
			public ComponentAll nextRecord()
			{
				if (componentAll.size() > 0) {
					currentRecordNumber++;
					return componentAll.remove(0);
				} else {
					return null;
				}
			}

		};
	}

	@Override
	protected <T> Object parseRecord(T record)
	{
		ComponentAll componentAll = (ComponentAll) record;
		updateComponentStandardFields(componentAll.getComponent());
		return componentAll;
	}

}
