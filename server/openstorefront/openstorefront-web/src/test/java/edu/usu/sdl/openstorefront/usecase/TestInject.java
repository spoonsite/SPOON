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
package edu.usu.sdl.openstorefront.usecase;

import edu.usu.sdl.openstorefront.web.model.PageModel;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;

/**
 *
 * @author dshurtleff
 */
public class TestInject
{

	@Inject
	@RequestScoped
	PageModel pagemodel;

	public TestInject()
	{
		//this.pagemodel = pagemodel;
	}

	public void printName()
	{
		pagemodel.setPage("Test2");
		System.out.println("Name: " + pagemodel.getPage());
	}

}
