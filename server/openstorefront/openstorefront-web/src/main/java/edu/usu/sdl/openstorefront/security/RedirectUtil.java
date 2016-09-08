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
package edu.usu.sdl.openstorefront.security;

import edu.usu.sdl.openstorefront.web.init.ShiroAdjustedFilter;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author dshurtleff
 */
public class RedirectUtil
{
	
	public static void saveLocation(HttpServletRequest request) 
	{
		String url = request.getRequestURL().toString();
		String queryString = request.getQueryString();

		if (StringUtils.isNotBlank(queryString)) {
			url = url + "?" + queryString;
		}
		request.getSession().setAttribute(ShiroAdjustedFilter.REFERENCED_FILTER_URL_ATTRIBUTE, url);
		
	}
	
}
