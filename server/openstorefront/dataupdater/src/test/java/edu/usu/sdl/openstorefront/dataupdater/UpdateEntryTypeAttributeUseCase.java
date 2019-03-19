package edu.usu.sdl.openstorefront.dataupdater;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.usu.sdl.apiclient.APIResponse;
import edu.usu.sdl.apiclient.ClientAPI;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.entity.AttributeType;
import edu.usu.sdl.openstorefront.core.entity.ComponentTypeRestriction;
import edu.usu.sdl.openstorefront.core.model.ComponentTypeNestedModel;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeSave;
import edu.usu.sdl.openstorefront.core.view.AttributeTypeView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class UpdateEntryTypeAttributeUseCase
{

	@Test
	public void updateEntryTypeAttributes()
	{
		ObjectMapper objectMapper = StringProcessor.defaultObjectMapper();

		System.out.println("Connecting to API...");
		ClientAPI clientAPI = new ClientAPI(objectMapper);
		clientAPI.connect("", "", "http://spoonsite.usurf.usu.edu/openstorefront"); //"https://spoonsite.com/openstorefront"  //Cert issue
		System.out.println("Connected");

		//pull nested
		System.out.println("Pulling all components...");
		APIResponse response = clientAPI.httpGet("/api/v1/resource/componenttypes/nested", null);
		ComponentTypeNestedModel componentTypeNestedModel = response.getList(new TypeReference<ComponentTypeNestedModel>()
		{
		});

		for (ComponentTypeNestedModel topLevel : componentTypeNestedModel.getChildren()) {

			List<AttributeTypeSave> updatedAttributes = new ArrayList<>();

			System.out.println("Pulling Required Attributes for: " + topLevel.getComponentType().getLabel());

			Map<String, String> parameters = new HashMap<>();
			parameters.put("componentType", topLevel.getComponentType().getComponentType());

			response = clientAPI.httpGet("/api/v1/resource/attributes/required", parameters);
			List<AttributeTypeView> requiredAttributes = response.getList(new TypeReference<List<AttributeTypeView>>()
			{
			});
			addToRestrictions(requiredAttributes, topLevel, updatedAttributes, true);

			System.out.println("Pulling Optional Attributes for: " + topLevel.getComponentType().getLabel());
			response = clientAPI.httpGet("/api/v1/resource/attributes/optional", parameters);
			List<AttributeTypeView> optionalAttributes = response.getList(new TypeReference<List<AttributeTypeView>>()
			{
			});
			addToRestrictions(optionalAttributes, topLevel, updatedAttributes, false);

			//update attributes
			System.out.println("Updated children of: " + topLevel.getComponentType().getLabel());
			if (!updatedAttributes.isEmpty()) {
				clientAPI.httpPut("/api/v1/resource/attributes/attributetypes/types", updatedAttributes, null);
			}
			//PUT //api/v1/resource/attributes/attributetypes/types

			System.out.println("Done\n");
		}

	}

	private void addToRestrictions(List<AttributeTypeView> attributeViews, ComponentTypeNestedModel topLevel, List<AttributeTypeSave> updatedAttributes, boolean required)
	{
		for (AttributeTypeView requiredView : attributeViews) {
			Set<String> existing;
			if (required) {
				existing = requiredView.getRequiredRestrictions().stream()
						.map(ComponentTypeRestriction::getComponentType)
						.collect(Collectors.toSet());
			} else {
				existing = requiredView.getOptionalRestrictions().stream()
						.map(ComponentTypeRestriction::getComponentType)
						.collect(Collectors.toSet());
			}

			boolean addType = false;
			for (ComponentTypeNestedModel child : topLevel.getChildren()) {
				if (!existing.contains(child.getComponentType().getComponentType())) {
					ComponentTypeRestriction typeRestriction = new ComponentTypeRestriction(child.getComponentType().getComponentType());
					if (required) {
						requiredView.getRequiredRestrictions().add(typeRestriction);
					} else {
						requiredView.getOptionalRestrictions().add(typeRestriction);
					}
					addType = true;
				}
			}
			if (addType) {
				updatedAttributes.add(toSaveAttribute(requiredView));
			}
		}
	}

	private AttributeTypeSave toSaveAttribute(AttributeTypeView attributeTypeView)
	{
		AttributeTypeSave save = new AttributeTypeSave();

		AttributeType attributeType = new AttributeType();
		attributeType.setAllowMultipleFlg(attributeTypeView.getAllowMultipleFlg());
		attributeType.setAllowUserGeneratedCodes(attributeTypeView.getAllowUserGeneratedCodes());
		attributeType.setArchitectureFlg(attributeTypeView.getArchitectureFlg());
		attributeType.setAttributeType(attributeTypeView.getAttributeType());
		attributeType.setAttributeValueType(attributeTypeView.getAttributeValueType());
		attributeType.setDefaultAttributeCode(attributeTypeView.getDefaultAttributeCode());
		attributeType.setDescription(attributeTypeView.getDescription());
		attributeType.setDetailedDescription(attributeTypeView.getDetailedDescription());
		attributeType.setHideOnSubmission(attributeTypeView.getHideOnSubmission());
		attributeType.setImportantFlg(attributeTypeView.getImportantFlg());
		attributeType.setRequiredFlg(attributeTypeView.getRequiredFlg());

		save.setAttributeType(attributeType);
		save.setOptionalComponentTypes(attributeTypeView.getOptionalRestrictions());
		save.setRequiredComponentType(attributeTypeView.getRequiredRestrictions());

		return save;
	}

}
