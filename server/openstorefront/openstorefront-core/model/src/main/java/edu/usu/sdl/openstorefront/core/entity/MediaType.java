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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.SystemTable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author jlaw
 */
@SystemTable
@APIDescription("Media Types: Video, Image, Text, etc")
public class MediaType
		extends LookupEntity<MediaType>
{

	private static final long serialVersionUID = 1L;

	public static final String IMAGE = "IMG";
	public static final String VIDEO = "VID";
	public static final String TEXT = "TEX";
	public static final String AUDIO = "AUD";
	public static final String ARCHIVE = "ARC";
	public static final String OTHER = "OTH";

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public MediaType()
	{
	}

	/**
	 * Converts a mime type to a media
	 *
	 * @param mimeType
	 * @return Best guess MediaType or other if un-determinable
	 */
	public static MediaType typeFromMimeType(String mimeType)
	{
		MediaType found = new MediaType();

		Map<String, LookupEntity> typeMap = found.systemCodeMap();
		if (StringUtils.isNotBlank(mimeType)) {
			if (mimeType.toLowerCase().contains("video")) {
				found = (MediaType) typeMap.get(VIDEO);
			} else if (mimeType.toLowerCase().contains("image")) {
				found = (MediaType) typeMap.get(IMAGE);
			} else if (mimeType.toLowerCase().contains("text")) {
				found = (MediaType) typeMap.get(TEXT);
			} else if (mimeType.toLowerCase().contains("audio")) {
				found = (MediaType) typeMap.get(AUDIO);
			} else {
				found = (MediaType) typeMap.get(OTHER);
			}
		} else {
			found = (MediaType) typeMap.get(OTHER);
		}

		return found;
	}

	@Override
	protected Map<String, LookupEntity> systemCodeMap()
	{
		Map<String, LookupEntity> codeMap = new HashMap<>();
		codeMap.put(IMAGE, newLookup(MediaType.class, IMAGE, "Image"));
		codeMap.put(VIDEO, newLookup(MediaType.class, VIDEO, "Video"));
		codeMap.put(TEXT, newLookup(MediaType.class, TEXT, "Text"));
		codeMap.put(AUDIO, newLookup(MediaType.class, AUDIO, "Audio"));
		codeMap.put(ARCHIVE, newLookup(MediaType.class, ARCHIVE, "Archive"));
		codeMap.put(OTHER, newLookup(MediaType.class, OTHER, "Other"));
		return codeMap;
	}

}
