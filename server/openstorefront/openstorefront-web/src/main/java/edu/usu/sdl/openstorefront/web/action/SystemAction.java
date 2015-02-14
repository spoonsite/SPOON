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

import edu.usu.sdl.openstorefront.service.manager.UserAgentManager;
import edu.usu.sdl.openstorefront.storage.model.AttributeCode;
import edu.usu.sdl.openstorefront.storage.model.AttributeCodePk;
import edu.usu.sdl.openstorefront.storage.model.AttributeType;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttribute;
import edu.usu.sdl.openstorefront.storage.model.ComponentAttributePk;
import edu.usu.sdl.openstorefront.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.util.SecurityUtil;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import net.sf.uadetector.ReadableUserAgent;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;

/**
 * System Actions
 *
 * @author dshurtleff
 */
public class SystemAction
		extends BaseAction
{

	private static final Logger log = Logger.getLogger(SystemAction.class.getName());

	@HandlesEvent("UserAgent")
	public Resolution userAgent()
	{
		return new StreamingResolution(MediaType.APPLICATION_JSON)
		{

			@Override
			protected void stream(HttpServletResponse response) throws Exception
			{
				ReadableUserAgent readableUserAgent = UserAgentManager.parse(getContext().getRequest().getHeader(OpenStorefrontConstant.HEADER_USER_AGENT));
				objectMapper.writeValue(response.getOutputStream(), readableUserAgent);
			}
		};
	}

	@HandlesEvent("AppVersion")
	public Resolution appVersion()
	{
		return new StreamingResolution("text/plain", getApplicationVersion());
	}

	//Temp method for 1.3-->remove when done
	@HandlesEvent("AttributeConvert")
	public Resolution attributeConvert()
	{
		if (SecurityUtil.isAdminUser()) {
			log.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));

			ComponentAttribute componentAttributeExample = new ComponentAttribute();
			ComponentAttributePk componentAttributePk = new ComponentAttributePk();
			componentAttributePk.setAttributeType(AttributeType.DI2E_SVCV4);
			componentAttributeExample.setComponentAttributePk(componentAttributePk);

			List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(ComponentAttribute.class, componentAttributeExample);

			List<AttributeCode> attributeCodes = service.getAttributeService().findCodesForType(AttributeType.DI2E_SVCV4);
			Map<String, AttributeCode> attributeMap = new HashMap<>();
			attributeCodes.forEach(code -> {
				String key = code.getLabel().split(" ")[0];
				attributeMap.put(key, code);
			});

			StringBuilder messages = new StringBuilder();
			componentAttributes.forEach(attribute -> {
				AttributeCode attributeCode = attributeMap.get(attribute.getComponentAttributePk().getAttributeCode());
				if (attributeCode != null) {
					ComponentAttribute oldAttribute = service.getPersistenceService().findById(ComponentAttribute.class, attribute.getComponentAttributePk());
					service.getPersistenceService().delete(oldAttribute);
					attribute.getComponentAttributePk().setAttributeCode(attributeCode.getAttributeCodePk().getAttributeCode());
					service.getPersistenceService().persist(attribute);
				} else {
					messages.append("Unable to find old code:  ").append(attribute.getComponentAttributePk().getAttributeCode());
					messages.append(" on component: ").append(service.getComponentService().getComponentName(attribute.getComponentId()));
					messages.append("<br>");
				}
			});

			//Delete inactive codes
			AttributeCode attributeCodeExample = new AttributeCode();
			attributeCodeExample.setActiveStatus(AttributeCode.INACTIVE_STATUS);
			AttributeCodePk attributeCodePk = new AttributeCodePk();
			attributeCodePk.setAttributeType(AttributeType.DI2E_SVCV4);
			attributeCodeExample.setAttributeCodePk(attributeCodePk);
			service.getPersistenceService().deleteByExample(attributeCodeExample);

			return new StreamingResolution("text/html", "Finished conversion of " + componentAttributes.size() + " component attributes.<br>  Messages: " + messages.toString());
		}
		return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Access denied");
	}

}
