/*
 * Copyright 2018 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.spoon.aerospace.importor.model;

import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.ElementList;

/**
 *
 * @author dshurtleff
 */
public class Comment
{

	@ElementList(name = "revision_comment", inline = true, required = false)
	private List<RevisionComment> comments = new ArrayList<>();

	public Comment()
	{
	}

	public List<RevisionComment> getComments()
	{
		return comments;
	}

	public void setComments(List<RevisionComment> comments)
	{
		this.comments = comments;
	}

}
