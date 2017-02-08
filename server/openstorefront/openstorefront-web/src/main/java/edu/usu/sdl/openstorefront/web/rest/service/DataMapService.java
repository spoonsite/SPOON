/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.web.rest.service;

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.common.util.ReflectionUtil;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.AttributeCodePk;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.BaseComponent;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.core.entity.ComponentEvaluationSectionPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewConPk;
import edu.usu.sdl.openstorefront.core.entity.ComponentReviewProPk;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import edu.usu.sdl.openstorefront.core.sort.BeanComparator;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.StringTransforms;
import edu.usu.sdl.openstorefront.core.spi.parser.mapper.TypeTransforms;
import edu.usu.sdl.openstorefront.core.view.LookupModel;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.web.rest.resource.BaseResource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.util.ResolverUtil;

/**
 * Data mapping support
 * 
 * @author dshurtleff
 */
@Path("v1/service/datamapping")
@APIDescription("Data mapping support services")
public class DataMapService
	extends BaseResource
{

	@GET
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Gets the application system status")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/transforms")
	public List<LookupModel> getDataTransforms() 
	{
		List<LookupModel> transforms = new ArrayList<>();
		
		for (StringTransforms stringTransform : StringTransforms.values())
		{
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(stringTransform.name());
			lookupModel.setDescription(stringTransform.getDescription());
			transforms.add(lookupModel);
		}
		
		for (TypeTransforms typeTransforms : TypeTransforms.values())
		{
			LookupModel lookupModel = new LookupModel();
			lookupModel.setCode(typeTransforms.name());
			lookupModel.setDescription(typeTransforms.getDescription());
			transforms.add(lookupModel);
		}	
		transforms.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, LookupModel.DESCRIPTION_FIELD));
		
		return transforms;
	}
	
	@GET
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Gets entities for data mapping")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/mappingentities")
	public List<LookupModel> getComponentEntities() 
	{
		List<LookupModel> entities = new ArrayList<>();
		
		LookupModel lookupModel = new LookupModel();
		lookupModel.setCode(Component.class.getName());
		lookupModel.setDescription(Component.class.getSimpleName());
		entities.add(lookupModel);
		
		ResolverUtil resolverUtil = new ResolverUtil();
		resolverUtil.find(new ResolverUtil.IsA(BaseComponent.class), Component.class.getPackage().getName());
		for (Object baseComponent : resolverUtil.getClasses()) {
			Class baseComponentClass = (Class) baseComponent;
			lookupModel = new LookupModel();
			lookupModel.setCode(baseComponentClass.getName());
			lookupModel.setDescription(baseComponentClass.getSimpleName());
			entities.add(lookupModel);			
		}
		
		lookupModel = new LookupModel();
		lookupModel.setCode(ComponentAll.class.getName());
		lookupModel.setDescription(ComponentAll.class.getSimpleName());
		entities.add(lookupModel);		
		
		//Add missing Pks
		lookupModel = new LookupModel();
		lookupModel.setCode(ComponentAttributePk.class.getName());
		lookupModel.setDescription(ComponentAttributePk.class.getSimpleName());
		entities.add(lookupModel);
		
		lookupModel = new LookupModel();
		lookupModel.setCode(ComponentEvaluationSectionPk.class.getName());
		lookupModel.setDescription(ComponentEvaluationSectionPk.class.getSimpleName());
		entities.add(lookupModel);

		lookupModel = new LookupModel();
		lookupModel.setCode(ComponentReviewConPk.class.getName());
		lookupModel.setDescription(ComponentReviewConPk.class.getSimpleName());
		entities.add(lookupModel);
		
		lookupModel = new LookupModel();
		lookupModel.setCode(ComponentReviewProPk.class.getName());
		lookupModel.setDescription(ComponentReviewProPk.class.getSimpleName());
		entities.add(lookupModel);	
		
		//Attributes
		lookupModel = new LookupModel();
		lookupModel.setCode(AttributeType.class.getName());
		lookupModel.setDescription(AttributeType.class.getSimpleName());
		entities.add(lookupModel);
		
		lookupModel = new LookupModel();
		lookupModel.setCode(AttributeCode.class.getName());
		lookupModel.setDescription(AttributeCode.class.getSimpleName());
		entities.add(lookupModel);
		
		//Add missing Pks
		lookupModel = new LookupModel();
		lookupModel.setCode(AttributeCodePk.class.getName());
		lookupModel.setDescription(AttributeCodePk.class.getSimpleName());
		entities.add(lookupModel);
				
		
		entities.sort(new BeanComparator<>(OpenStorefrontConstant.SORT_ASCENDING, LookupModel.DESCRIPTION_FIELD));
		
		return entities;
	}	
	
	//get entity fields
	@GET
	@RequireSecurity(SecurityPermission.ADMIN_DATA_IMPORT_EXPORT)
	@APIDescription("Gets component entities for data mapping")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(LookupModel.class)
	@Path("/entityfields/{entity}")
	public Response getEntityFields(
			@PathParam("entity") String entity
	) 
	{
		List<LookupModel> entities = new ArrayList<>();

		try {
			Class entityClass = getClass().getClassLoader().loadClass(entity);
			List<Field> fields = ReflectionUtil.getAllFields(entityClass);
			for (Field field : fields) {
				LookupModel lookupModel = new LookupModel();
				lookupModel.setCode(field.getName());
				lookupModel.setDescription(field.getName());
				entities.add(lookupModel);				
			}
			
		} catch (ClassNotFoundException ex) {
			return Response.status(Response.Status.NOT_FOUND).build();
		}
		
		GenericEntity<List<LookupModel>> gEntity = new GenericEntity<List<LookupModel>>(entities)
		{
		};
		return sendSingleEntityResponse(gEntity);
	}	

}
