/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.HelpSupportService;
import edu.usu.sdl.openstorefront.core.entity.SupportMedia;
import edu.usu.sdl.openstorefront.core.util.MediaFileType;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dshurtleff
 */
public class HelpSupportServiceImpl
		extends ServiceProxy
		implements HelpSupportService
{

	private static final Logger LOG = Logger.getLogger(HelpSupportServiceImpl.class.getName());

	@Override
	public SupportMedia saveSupportMedia(SupportMedia supportMedia)
	{
		Objects.requireNonNull(supportMedia);

		SupportMedia existing = getPersistenceService().findById(SupportMedia.class, supportMedia.getSupportMediaId());
		if (existing != null) {
			existing.updateFields(supportMedia);
			getPersistenceService().persist(existing);
		} else {
			supportMedia.setSupportMediaId(getPersistenceService().generateId());
			supportMedia.populateBaseCreateFields();
			existing = getPersistenceService().persist(supportMedia);
		}
		existing = getPersistenceService().unwrapProxyObject(existing);
		return existing;
	}

	@Override
	public SupportMedia saveSupportMedia(SupportMedia supportMedia, InputStream mediaData)
	{
		try {
			Objects.requireNonNull(supportMedia);
			Objects.requireNonNull(supportMedia.getFile());
			Objects.requireNonNull(supportMedia.getFile().getMimeType());
			Objects.requireNonNull(supportMedia.getFile().getOriginalName());
			Objects.requireNonNull(mediaData);

			supportMedia = saveSupportMedia(supportMedia);
			SupportMedia existing = getPersistenceService().findById(SupportMedia.class, supportMedia.getSupportMediaId());

			existing.getFile().setFileName(getPersistenceService().generateId() + OpenStorefrontConstant.getFileExtensionForMime(existing.getFile().getMimeType()));
			existing.getFile().setFileType(MediaFileType.SUPPORT);
			Path path = Paths.get(MediaFileType.SUPPORT.getPath() + "/" + existing.getFile().getFileName());
			Files.copy(mediaData, path, StandardCopyOption.REPLACE_EXISTING);
			getPersistenceService().persist(existing);

			supportMedia = getPersistenceService().unwrapProxyObject(existing);

		} catch (IOException ex) {
			throw new OpenStorefrontRuntimeException("Unable to save support file.", "Contact System Admin.  Check file permissions and disk space ", ex);
		}
		return supportMedia;
	}

	@Override
	public void deleteSupportMedia(String supportMediaId)
	{
		SupportMedia supportMedia = getPersistenceService().findById(SupportMedia.class, supportMediaId);
		if (supportMedia != null) {
			Path path = supportMedia.pathToMedia();
			if (path != null) {
				if (path.toFile().exists()) {
					if (path.toFile().delete() == false) {
						LOG.log(Level.WARNING, MessageFormat.format("Unable to delete support media. Path: {0}", path.toString()));
					}
				}
			}
			getPersistenceService().delete(supportMedia);
		}
	}

}
