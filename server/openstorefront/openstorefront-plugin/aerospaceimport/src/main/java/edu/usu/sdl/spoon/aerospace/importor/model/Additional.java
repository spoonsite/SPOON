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
public class Additional
{

	@ElementList(name = "float_feature", inline = true, required = false)
	private List<FloatFeature> floatFeatures = new ArrayList<>();

	@ElementList(name = "int_feature", inline = true, required = false)
	private List<IntFeature> intFeatures = new ArrayList<>();

	@ElementList(name = "text_feature", inline = true, required = false)
	private List<TextFeature> textFeatures = new ArrayList<>();

	public Additional()
	{
	}

	public List<FloatFeature> getFloatFeatures()
	{
		return floatFeatures;
	}

	public void setFloatFeatures(List<FloatFeature> floatFeatures)
	{
		this.floatFeatures = floatFeatures;
	}

	public List<IntFeature> getIntFeatures()
	{
		return intFeatures;
	}

	public void setIntFeatures(List<IntFeature> intFeatures)
	{
		this.intFeatures = intFeatures;
	}

	public List<TextFeature> getTextFeatures()
	{
		return textFeatures;
	}

	public void setTextFeatures(List<TextFeature> textFeatures)
	{
		this.textFeatures = textFeatures;
	}

}
