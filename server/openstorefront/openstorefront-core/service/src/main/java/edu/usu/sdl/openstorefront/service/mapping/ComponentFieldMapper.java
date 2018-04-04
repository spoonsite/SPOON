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
package edu.usu.sdl.openstorefront.service.mapping;

import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentMedia;
import edu.usu.sdl.openstorefront.core.entity.MediaType;
import edu.usu.sdl.openstorefront.core.entity.SubmissionFormField;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionField;
import edu.usu.sdl.openstorefront.core.entity.UserSubmissionMedia;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.beanutils.BeanUtils;

/**
 * This mapper handles field to field mapping basically direct value to value.
 *
 * @author dshurtleff
 */
public class ComponentFieldMapper
		extends BaseMapper
{

	private static final Logger LOG = Logger.getLogger(ComponentFieldMapper.class.getName());

	@Override
	public List<ComponentAll> mapField(ComponentAll componentAll, SubmissionFormField submissionField, UserSubmissionField userSubmissionField)
	{
		List<ComponentAll> childComponents = new ArrayList<>();

		if (componentAll.getComponent() == null) {
			componentAll.setComponent(new Component());
		}

		try {
			BeanUtils.setProperty(componentAll.getComponent(), submissionField.getFieldName(), userSubmissionField.getRawValue());
		} catch (IllegalAccessException | InvocationTargetException ex) {
			LOG.log(Level.WARNING, () -> "Field cannot be mapped.  Check template for field label. Label: " + submissionField.getLabel());
			if (LOG.isLoggable(Level.FINER)) {
				LOG.log(Level.FINER, null, ex);
			}
		}
		//handle any media (inline media)
		componentAll.getMedia().addAll(createInlineMedia(userSubmissionField.getMedia()));

		return childComponents;
	}

	private List<ComponentMedia> createInlineMedia(List<UserSubmissionMedia> submissionMedia)
	{
		List<ComponentMedia> componentMediaList = new ArrayList<>();

		if (submissionMedia != null) {
			for (UserSubmissionMedia userSubmissionMedia : submissionMedia) {
				ComponentMedia componentMedia = new ComponentMedia();
				componentMedia.setMediaTypeCode(MediaType.typeFromMimeType(userSubmissionMedia.getFile().getMimeType()).getCode());
				componentMedia.setFile(userSubmissionMedia.getFile());
				componentMedia.setUsedInline(Boolean.TRUE);
				componentMedia.setHideInDisplay(Boolean.TRUE);
				componentMediaList.add(componentMedia);
			}
		}

		return componentMediaList;
	}

}
