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
package edu.usu.sdl.openstorefront.core.view;

/**
 * Statistics on the listings
 *
 * @author dshurtleff
 */
public class ListingStats
{

	private long numberOfComponents;
	private long numberOfArticles;

	public long getNumberOfComponents()
	{
		return numberOfComponents;
	}

	public void setNumberOfComponents(long numberOfComponents)
	{
		this.numberOfComponents = numberOfComponents;
	}

	public long getNumberOfArticles()
	{
		return numberOfArticles;
	}

	public void setNumberOfArticles(long numberOfArticles)
	{
		this.numberOfArticles = numberOfArticles;
	}

}
