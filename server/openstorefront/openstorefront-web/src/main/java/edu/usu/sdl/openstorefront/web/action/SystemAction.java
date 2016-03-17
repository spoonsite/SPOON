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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.entity.AttributeCode;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttribute;
import edu.usu.sdl.openstorefront.core.entity.ComponentAttributePk;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import edu.usu.sdl.openstorefront.service.manager.UserAgentManager;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import net.sf.uadetector.ReadableUserAgent;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.HandlesEvent;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.StreamingResolution;
import net.sourceforge.stripes.validation.Validate;

/**
 * System Actions
 *
 * @author dshurtleff
 */
public class SystemAction
		extends BaseAction
{

	private static final Logger log = Logger.getLogger(SystemAction.class.getName());

	@Validate(required = true, on = {"BulkAttributeStatusUpdate", "AttributeCleanup"})
	private String attributeType;
	
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

	@HandlesEvent("BulkAttributeStatusUpdate")
	public Resolution attributeStatusUpdate()
	{
		if (SecurityUtil.isAdminUser()) {
			log.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));

			ComponentAttribute componentAttributeExample = new ComponentAttribute();
			ComponentAttributePk componentAttributePk = new ComponentAttributePk();
			componentAttributePk.setAttributeType(attributeType);
			componentAttributeExample.setComponentAttributePk(componentAttributePk);

			QueryByExample queryByExample = new QueryByExample(componentAttributeExample);
			queryByExample.setReturnNonProxied(false);

			List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(ComponentAttribute.class, queryByExample);
			int updateCount = 0;
			for (ComponentAttribute attribute : componentAttributes) {
				if (ComponentAttribute.ACTIVE_STATUS.equals(attribute.getActiveStatus()) == false) {
					attribute.setActiveStatus(AttributeCode.ACTIVE_STATUS);
					service.getPersistenceService().persist(attribute);
					updateCount++;
				}
			}
			return new StreamingResolution("text/html", "Updated Status on: " + updateCount + " component attibutes.");
		}
		return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Access denied");
	}

	//Temp 1.4 Clean the duplicate attibutes
	@HandlesEvent("AttributeCleanup")
	public Resolution attributeCleanup()
	{
		if (SecurityUtil.isAdminUser()) {
			log.log(Level.INFO, SecurityUtil.adminAuditLogMessage(getContext().getRequest()));

			//Deduplicate
			ComponentAttribute componentAttributeExample = new ComponentAttribute();
			ComponentAttributePk componentAttributePk = new ComponentAttributePk();
			componentAttributePk.setAttributeType(attributeType);
			componentAttributeExample.setComponentAttributePk(componentAttributePk);

			Set<String> attributeKeySet = new HashSet<>();
			QueryByExample queryByExample = new QueryByExample(componentAttributeExample);
			queryByExample.setReturnNonProxied(false);

			List<ComponentAttribute> componentAttributes = service.getPersistenceService().queryByExample(ComponentAttribute.class, queryByExample);
			int dupCount = 0;
			StringBuilder details = new StringBuilder();
			for (ComponentAttribute componentAttribute : componentAttributes) {
				if (attributeKeySet.contains(componentAttribute.getComponentAttributePk().pkValue())) {
					service.getPersistenceService().delete(componentAttribute);
					details.append("Remove duplication: ").append(componentAttribute.getComponentAttributePk().pkValue()).append("<br>");
					dupCount++;
				} else {
					attributeKeySet.add(componentAttribute.getComponentAttributePk().pkValue());
				}
			}
			return new StreamingResolution("text/html", dupCount + " Duplicate Component Attribute Remove on: " + attributeType + " attribute Type. <br> Details: <br>" + details);
		}
		return new ErrorResolution(HttpServletResponse.SC_FORBIDDEN, "Access denied");
	}

	public String getAttributeType()
	{
		return attributeType;
	}

	public void setAttributeType(String attributeType)
	{
		this.attributeType = attributeType;
	}

}
