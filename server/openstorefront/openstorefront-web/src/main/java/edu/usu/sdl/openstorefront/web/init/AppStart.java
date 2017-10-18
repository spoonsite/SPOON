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
package edu.usu.sdl.openstorefront.web.init;

import javax.inject.Inject;

/**
 *
 * bootstrap class for missing @Imidiate support in jersey
 * Jersey 2 does not support @Immediate once https://github.com/jersey/jersey/issues/2563 is resolved for the version we are using
 * replace register(new AppStart()); with ServiceLocatorUtilities.enableImmediateScope(locator); in RestConfiguration.java
 * then delete this file
 * 
 * @author kbair
 */
public class AppStart
{
	@Inject
	private ApplicationInit init;
	
}