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

import java.io.IOException;
import java.text.MessageFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.tuckey.web.filters.urlrewrite.extend.RewriteMatch;

/**
 *
 * @author dshurtleff
 */
public class AngularRewriteMatch
		extends RewriteMatch
{

	private static final Logger log = Logger.getLogger(AngularRewriteMatch.class.getName());

	@Override
	public boolean execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String query = request.getQueryString();
		String basicPath = request.getRequestURI().replace(request.getContextPath(), "");
		request.getSession().setAttribute(ShiroAdjustedFilter.REFERENCED_URL_ATTRIBUTE, basicPath + "?" + query);

		log.log(Level.FINEST, MessageFormat.format("Matching virtual route: {0}?{1}  going to angularLogin.jsp", new Object[]{basicPath, query}));
		RequestDispatcher rd = request.getRequestDispatcher("/angularLogin.jsp");
		if (rd == null) {
			log.log(Level.SEVERE, "Unable to find a dispatcher for /angularLogin.jsp make sure it exists.  (Returned as No Match)");
			return false;
		} else {
			rd.forward(request, response);
			return true;
		}
	}

}
