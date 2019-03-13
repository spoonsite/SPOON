/*
 * Copyright 2019 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.service.repo;

import edu.usu.sdl.openstorefront.core.api.repo.AttributeRepo;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author dshurtleff
 */
public class AttributeOrientRepoImpl
		extends BaseOrientRepo
		implements AttributeRepo
{

	@Override
	public void changeAttributeCode(AttributeCodePk attributeCodePk, String newCode)
	{
		String query = "Update " + ComponentAttributePk.class.getSimpleName() + " set attributeCode = :attributeCodeParamReplace where attributeCode = :oldCodeParam and attributeType = :attributeTypeParam";
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("attributeCodeParamReplace", newCode);
		parameters.put("oldCodeParam", attributeCodePk.getAttributeCode());
		parameters.put("attributeTypeParam", attributeCodePk.getAttributeType());

		service.getPersistenceService().runDbCommand(query, parameters);

		query = "Update " + AttributeCodePk.class.getSimpleName() + " set attributeCode = :attributeCodeParamReplace where attributeCode = :oldCodeParam and attributeType = :attributeTypeParam";
		parameters = new HashMap<>();
		parameters.put("attributeCodeParamReplace", newCode);
		parameters.put("oldCodeParam", attributeCodePk.getAttributeCode());
		parameters.put("attributeTypeParam", attributeCodePk.getAttributeType());

		service.getPersistenceService().runDbCommand(query, parameters);
	}

}
