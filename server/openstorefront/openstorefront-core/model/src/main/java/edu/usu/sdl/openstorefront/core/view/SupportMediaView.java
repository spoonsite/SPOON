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
package edu.usu.sdl.openstorefront.core.view;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.MediaType;
import edu.usu.sdl.openstorefront.core.entity.SupportMedia;
import edu.usu.sdl.openstorefront.core.util.TranslateUtil;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.beanutils.BeanUtils;

/**
 *
 * @author dshurtleff
 */
public class SupportMediaView
		extends SupportMedia
{

	private String mediaTypeDescription;

	public SupportMediaView()
	{
	}

	public static SupportMediaView toView(SupportMedia supportedMedia)
	{
		SupportMediaView view = new SupportMediaView();
		try {
			BeanUtils.copyProperties(view, supportedMedia);
		} catch (IllegalAccessException | InvocationTargetException ex) {
			throw new OpenStorefrontRuntimeException(ex);
		}
		view.setMediaTypeDescription(TranslateUtil.translate(MediaType.class, supportedMedia.getMediaType()));
		return view;
	}

	public static List<SupportMediaView> toView(List<SupportMedia> supportedMediaRecords)
	{
		List<SupportMediaView> views = new ArrayList<>();
		supportedMediaRecords.forEach(supportedMedia -> {
			views.add(toView(supportedMedia));
		});
		return views;
	}

	public String getMediaTypeDescription()
	{
		return mediaTypeDescription;
	}

	public void setMediaTypeDescription(String mediaTypeDescription)
	{
		this.mediaTypeDescription = mediaTypeDescription;
	}

}
