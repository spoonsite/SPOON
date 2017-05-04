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
package edu.usu.sdl.openstorefront.web.util;

import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import net.sourceforge.stripes.controller.ExecutionContext;

/**
 * Stripes / jax-rs bridge for security
 * @author dshurtleff
 */
public class StripesContainerRequestContext 
		implements ContainerRequestContext
{
	private final ExecutionContext context;

	public StripesContainerRequestContext(ExecutionContext context)
	{
		this.context = context;
	}

	@Override
	public Object getProperty(String string)
	{
		throw new UnsupportedOperationException("Not supported yet."); 		
	}

	@Override
	public Collection<String> getPropertyNames()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public void setProperty(String string, Object o)
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public void removeProperty(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public UriInfo getUriInfo()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public void setRequestUri(URI uri)
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public void setRequestUri(URI uri, URI uri1)
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public Request getRequest()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public String getMethod()
	{
		return getContext().getHandler().getName();
	}

	@Override
	public void setMethod(String string)
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public MultivaluedMap<String, String> getHeaders()
	{
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public String getHeaderString(String string)
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public Date getDate()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public Locale getLanguage()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public int getLength()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public MediaType getMediaType()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public List<MediaType> getAcceptableMediaTypes()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public List<Locale> getAcceptableLanguages()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public Map<String, Cookie> getCookies()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public boolean hasEntity()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public InputStream getEntityStream()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public void setEntityStream(InputStream in)
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public SecurityContext getSecurityContext()
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public void setSecurityContext(SecurityContext sc)
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	@Override
	public void abortWith(Response rspns)
	{
		throw new UnsupportedOperationException("Not supported yet."); 
	}

	public ExecutionContext getContext()
	{
		return context;
	}
	
}
