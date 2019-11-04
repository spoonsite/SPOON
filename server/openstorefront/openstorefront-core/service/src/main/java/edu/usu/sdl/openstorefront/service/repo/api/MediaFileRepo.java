/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.repo.api;

import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.entity.MediaFile;

/**
 *
 * @author dshurtleff
 */
public interface MediaFileRepo
{

	/**
	 * Orient will create a Media file collection/table automatically However,
	 * Mongo see it as embedded which is fine but the application is set to
	 * query the Media files as if it was a separate table. This is used to
	 * normalize the behavior
	 *
	 * @param mediaFile
	 */
	public void handleMediFileSave(PersistenceService persistenceService, MediaFile mediaFile);

}
