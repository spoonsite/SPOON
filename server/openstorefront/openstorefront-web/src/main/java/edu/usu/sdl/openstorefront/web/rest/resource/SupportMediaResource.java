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
package edu.usu.sdl.openstorefront.web.rest.resource;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.DataType;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.core.entity.SupportMedia;
import edu.usu.sdl.openstorefront.core.view.SupportMediaView;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import java.util.List;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 *
 * @author dshurtleff
 */
@Path("v1/resource/supportmedia")
@APIDescription("Support media records")
public class SupportMediaResource
		extends BaseResource
{

	@GET
	@APIDescription("Gets the active support media record. Use Media.action?SupportMedia&mediaId=<supportMediaId> to get the actual media")
	@DataType(SupportMediaView.class)
	public List<SupportMediaView> getSupportMediaRecords()
	{
		SupportMedia supportMediaExample = new SupportMedia();
		supportMediaExample.setActiveStatus(SupportMedia.ACTIVE_STATUS);

		List<SupportMedia> supportMedia = supportMediaExample.findByExample();
		supportMedia.sort((a, b) -> {
			return a.getOrderNumber().compareTo(b.getOrderNumber());
		});
		return SupportMediaView.toView(supportMedia);
	}

	@GET
	@APIDescription("Gets a support media record. Use Media.action?SupportMedia&mediaId=<supportMediaId> to get the actual media")
	@Path("/{supportMediaId}")
	public SupportMediaView getSupportMedia(
			@PathParam("supportMediaId") String supportMediaId
	)
	{
		SupportMediaView view = null;

		SupportMedia supportMedia = new SupportMedia();
		supportMedia.setSupportMediaId(supportMediaId);
		supportMedia = supportMedia.find();
		if (supportMedia != null) {
			view = SupportMediaView.toView(supportMedia);
		}
		return view;
	}

	@PUT
	@RequireSecurity(SecurityPermission.ADMIN_SUPPORT_MEDIA_UPDATE)
	@APIDescription("Updates the db record only (use Media.action?UploadSupportMedia to upload new media)")
	@DataType(SupportMediaView.class)
	@Path("/{supportMediaId}")
	public Response updateSupportMedia(
			@PathParam("supportMediaId") String supportMediaId,
			SupportMedia supportMedia
	)
	{
		supportMedia.setSupportMediaId(supportMediaId);

		SupportMedia existing = new SupportMedia();
		existing.setSupportMediaId(supportMediaId);
		existing = existing.find();
		if (existing != null) {
			ValidationResult validationResult = supportMedia.validate();
			if (validationResult.valid()) {
				supportMedia = service.getHelpSupportService().saveSupportMedia(supportMedia);
				return Response.ok(supportMedia).build();
			} else {
				return Response.ok(validationResult.toRestError()).build();
			}
		}
		return sendSingleEntityResponse(existing);
	}

	@DELETE
	@RequireSecurity(SecurityPermission.ADMIN_SUPPORT_MEDIA_DELETE)
	@APIDescription("Deletes a support media record. Hard Delete.")
	@Path("/{supportMediaId}")
	public void deleteSupportMedia(
			@PathParam("supportMediaId") String supportMediaId)
	{
		service.getHelpSupportService().deleteSupportMedia(supportMediaId);
	}

}
