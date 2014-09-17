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
package edu.usu.sdl.openstorefront.web.action;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.storage.model.ComponentResource;
import java.nio.file.Files;
import java.nio.file.Path;
import javax.servlet.http.HttpServletResponse;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;

/**
 *
 * @author dshurtleff
 */
public class ResourceAction
		extends BaseAction
{

	@Validate(required = true, on = "LoadResource")
	private String resourceId;

	private ComponentResource componentResource;

	@HandlesEvent("LoadResource")
	public Resolution loadResource()
	{
		componentResource = service.getPersistenceService().findById(ComponentResource.class, resourceId);
		if (componentResource == null) {
			throw new OpenStorefrontRuntimeException("Resource not Found", "Check resource Id");
		}

		return new StreamingResolution(componentResource.getMimeType())
		{

			@Override
			protected void stream(HttpServletResponse response) throws Exception
			{
				Path path = componentResource.pathToMedia();
				if (path != null) {
					Files.copy(path, response.getOutputStream());
				} else {
					throw new OpenStorefrontRuntimeException("Resource not on disk", "Check resource record: " + resourceId);
				}
			}

		};
	}

	public String getResourceId()
	{
		return resourceId;
	}

	public void setResourceId(String resourceId)
	{
		this.resourceId = resourceId;
	}

}
