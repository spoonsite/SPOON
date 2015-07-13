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
package edu.usu.sdl.openstorefront.web.init;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;
import org.tuckey.web.filters.urlrewrite.extend.RewriteRule;

/**
 * This handles angular html 5 direct routes
 *
 * @author dshurtleff
 */
public class AngularRewriteRule
		extends RewriteRule
{

	private static final Logger log = Logger.getLogger(AngularRewriteRule.class.getName());

	@Override
	public boolean initialise(ServletContext servletContext)
	{
		log.log(Level.FINEST, "Initialize Angular Rule");
		return super.initialise(servletContext);
	}

	@Override
	public RewriteMatch matches(HttpServletRequest request, HttpServletResponse response)
	{
		String actionPath = request.getRequestURI().replace(request.getContextPath() + "/", "");
		if (StringUtils.isNotBlank(actionPath)) {
			if (actionPath.contains("/") == false) {
				if (actionPath.contains(".") == false) {
					return new AngularRewriteMatch();
				}
			}
		}
		return null;
	}

	@Override
	public void destroy()
	{
		log.log(Level.FINEST, "Destroying Angular Rule");
		super.destroy();
	}

}
