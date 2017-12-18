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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.SupportMedia;
import java.io.InputStream;

/**
 *
 * @author dshurtleff
 */
public interface HelpSupportService
		extends AsyncService
{

	/**
	 * Adds/updates a support media record Save the media
	 *
	 * @param supportMedia
	 * @return
	 */
	public SupportMedia saveSupportMedia(SupportMedia supportMedia, InputStream mediaData);

	/**
	 * Adds/updates a support media record
	 *
	 * @param supportMedia
	 * @return
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public SupportMedia saveSupportMedia(SupportMedia supportMedia);

	/**
	 * Remove database record and the associated file
	 *
	 * @param supportMediaId
	 */
	public void deleteSupportMedia(String supportMediaId);

}
