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
package edu.usu.sdl.openstorefront.web.action;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.TestEntity;
import edu.usu.sdl.openstorefront.web.rest.model.ComponentDetailView;
import edu.usu.sdl.openstorefront.web.tool.OldAsset;
import edu.usu.sdl.openstorefront.web.viewmodel.LookupModel;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;

/**
 *
 * @author dshurtleff
 */
public class TestAction
		extends BaseAction
{

	private boolean generateData;

	@DefaultHandler
	public Resolution checkSetup()
	{
		List<String> data = Arrays.asList("Hello", "Test", "Apple");
		data = data.stream().filter(item -> item.startsWith("A")).collect(Collectors.toList());
		return streamResults(data);
	}

	@HandlesEvent("TestList")
	public Resolution testList()
	{
		List<LookupModel> lookups = new ArrayList<>();

		List<TestEntity> test = service.getLookupService().findLookup(TestEntity.class);
		test.forEach(t -> {
			LookupModel lookup = new LookupModel();
			lookup.setCode(t.getCode());
			lookup.setDescription(t.getDescription());
			lookups.add(lookup);
		});
		return streamResults(lookups);
	}

	@HandlesEvent("ConvertData")
	public Resolution convertData() throws IOException
	{
		List<OldAsset> assets = objectMapper.readValue(new File("C:\\development\\storefront\\source\\old_data\\new-asset-data-all.json"), new TypeReference<List<OldAsset>>()
		{
		});

		List<ComponentDetailView> newAssets = new ArrayList<>();
		assets.forEach(oldAsset -> {

			ComponentDetailView componentDetail = new ComponentDetailView();
			//defaults
			componentDetail.setActiveStatus(BaseEntity.ACTIVE_STATUS);

			//map form old
			if (generateData) {
				//filling some details at random
			}

			newAssets.add(componentDetail);
		});

		return streamResults(newAssets);
	}

	public boolean getGenerateData()
	{
		return generateData;
	}

	public void setGenerateData(boolean generateData)
	{
		this.generateData = generateData;
	}

}
