/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * See NOTICE.txt for more information.
 */
package edu.usu.sdl.openstorefront.web.action;

import edu.usu.sdl.openstorefront.common.util.NetworkUtil;
import edu.usu.sdl.openstorefront.common.util.TimeUtil;
import edu.usu.sdl.openstorefront.core.entity.Component;
import edu.usu.sdl.openstorefront.core.entity.ComponentTracking;
import edu.usu.sdl.openstorefront.core.entity.TrackEventCode;
import javax.ws.rs.core.Response;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;

/**
 *
 * @author dshurtleff
 */
public class ViewAction
		extends BaseAction
{

	@Validate(required = true)
	private String id;

	@DefaultHandler
	public Resolution viewComponent()
	{
		Component component = new Component();
		component.setComponentId(getId());
		component = component.find();
		component = filterEngine.filter(component);

		if (component != null) {
			ComponentTracking componentTracking = new ComponentTracking();
			componentTracking.setClientIp(NetworkUtil.getClientIp(getContext().getRequest()));
			componentTracking.setComponentId(getId());
			componentTracking.setComponentType(component.getComponentType());
			componentTracking.setEventDts(TimeUtil.currentDate());
			componentTracking.setTrackEventTypeCode(TrackEventCode.EXTERNAL_VIEW);
			componentTracking.populateBaseCreateFields();
			service.getComponentService().saveComponentTracking(componentTracking);

			return new ForwardResolution("view.jsp")
					.addParameter("id", getId())
					.addParameter("fullPage", true);
		} else {
			return new ErrorResolution(Response.Status.NOT_FOUND.getStatusCode(), "Not found or not accessible");
		}
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

}
