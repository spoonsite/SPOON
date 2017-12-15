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
import edu.usu.sdl.openstorefront.core.entity.Faq;
import edu.usu.sdl.openstorefront.core.entity.SecurityPermission;
import edu.usu.sdl.openstorefront.doc.security.RequireSecurity;
import edu.usu.sdl.openstorefront.security.SecurityUtil;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author cyearsley
 */
@Path("v1/resource/faq")
@APIDescription("An FAQ contains data in regards to frequently asked quesitons and answers.")
public class FaqResource
		extends BaseResource
{
	@GET
	@APIDescription("Gets a list of all FAQs. If user is admin, will also return inactive FAQs")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Faq.class)
	public Response faqLookupAll()
	{
		
		List<Faq> faqs = service.getFaqService().getFaqs(SecurityUtil.hasPermission(SecurityPermission.ADMIN_FAQ));
		
		GenericEntity<List<Faq>> entity = new GenericEntity<List<Faq>>(faqs)
			{
			};
		return sendSingleEntityResponse(entity);
	}
	
	@GET
	@APIDescription("Gets a single FAQ")
	@Produces({MediaType.APPLICATION_JSON})
	@DataType(Faq.class)
	@Path("/{id}")
	public Response faqSingleLookup(@PathParam("id") String faqId)
	{
//		faqExample1.setAnswer("This is the answer to the FAQ (Example 1)");
//		faqExample1.setQuestion("This is the question to the FAQ (Example 1)");
//		faqExample1.setActiveStatus("A");
//		faqExample1.setCategory("CAT1");
//		
//		faqExample2.setQuestion("This is the question to the FAQ (Example 2)");
//		faqExample2.setAnswer("This is the answer to the FAQ (Example 2)");
//		faqExample2.setActiveStatus("A");
//		faqExample2.setCategory("CAT2");
		
		Faq faq = service.getFaqService().getFaq(faqId, SecurityUtil.hasPermission(SecurityPermission.ADMIN_FAQ));
		
		GenericEntity<Faq> entity = new GenericEntity<Faq>(faq)
			{
			};
		return sendSingleEntityResponse(entity);
	}
	
	@POST
	@APIDescription("Creates an FAQ")
	@RequireSecurity(SecurityPermission.ADMIN_FAQ)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(Faq.class)
	public Response createFaq(Faq faq)
	{
		faq.populateBaseCreateFields();
		Faq createdFaq = service.getFaqService().createFaq(faq);
		
		GenericEntity<Faq> entity = new GenericEntity<Faq>(createdFaq)
			{
			};
		return sendSingleEntityResponse(entity);
	}
	
	@PUT
	@APIDescription("Updates an FAQ")
	@RequireSecurity(SecurityPermission.ADMIN_FAQ)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(Faq.class)
	@Path("/{id}")
	public Response updateFaq(
			@PathParam("id") String faqId,
			Faq faq
	)
	{
		Faq updatedFaq = service.getFaqService().updateFaq(faqId, faq);
		
		GenericEntity<Faq> entity = new GenericEntity<Faq>(updatedFaq)
			{
			};
		return sendSingleEntityResponse(entity);
	}
	
	@DELETE
	@APIDescription("Deletes an FAQ")
	@RequireSecurity(SecurityPermission.ADMIN_FAQ)
	@Produces({MediaType.APPLICATION_JSON})
	@Consumes({MediaType.APPLICATION_JSON})
	@DataType(Faq.class)
	@Path("/{id}")
	public void deleteFaq(@PathParam("id") String faqId)
	{
		service.getFaqService().deleteFaq(faqId);
	}
}
