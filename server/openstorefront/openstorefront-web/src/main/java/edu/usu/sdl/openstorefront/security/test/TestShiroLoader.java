/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.security.test;

import javax.servlet.ServletContextEvent;
import org.apache.shiro.web.env.EnvironmentLoader;
import org.apache.shiro.web.env.EnvironmentLoaderListener;

/**
 *
 * @author kbair
 */
public class TestShiroLoader
		extends EnvironmentLoaderListener
{

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		sce.getServletContext().setInitParameter(EnvironmentLoader.CONFIG_LOCATIONS_PARAM, "classpath:test.shiro.ini");
		super.contextInitialized(sce);
	}
}
