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
package edu.usu.sdl.openstorefront.service.transfermodel;

import edu.usu.sdl.openstorefront.storage.model.ComponentReview;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewCon;
import edu.usu.sdl.openstorefront.storage.model.ComponentReviewPro;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author dshurtleff
 */
public class ReviewAll
{

	private ComponentReview componentReview;
	private List<ComponentReviewPro> pros = new ArrayList<>();
	private List<ComponentReviewCon> cons = new ArrayList<>();

	public ReviewAll()
	{
	}

	public ComponentReview getComponentReview()
	{
		return componentReview;
	}

	public void setComponentReview(ComponentReview componentReview)
	{
		this.componentReview = componentReview;
	}

	public List<ComponentReviewPro> getPros()
	{
		return pros;
	}

	public void setPros(List<ComponentReviewPro> pros)
	{
		this.pros = pros;
	}

	public List<ComponentReviewCon> getCons()
	{
		return cons;
	}

	public void setCons(List<ComponentReviewCon> cons)
	{
		this.cons = cons;
	}

}
