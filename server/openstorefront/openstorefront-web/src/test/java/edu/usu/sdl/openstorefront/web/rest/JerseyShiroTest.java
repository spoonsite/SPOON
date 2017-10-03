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
package edu.usu.sdl.openstorefront.web.rest;

import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.security.test.TestRealm;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.manager.OSFCacheManager;
import edu.usu.sdl.openstorefront.service.manager.OsgiManager;
import edu.usu.sdl.openstorefront.service.test.TestPersistenceService;
import edu.usu.sdl.openstorefront.web.init.ShiroAdjustedFilter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.glassfish.jersey.internal.util.Base64;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.test.DeploymentContext;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.ServletDeploymentContext;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.glassfish.jersey.test.spi.TestContainerFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 *
 * @author kbair
 */
public abstract class JerseyShiroTest extends JerseyTest
{

	private static final String USER = "TestUser";
	private static final String PASS = "TestPass";
	private boolean permissionsSet = false;

	protected abstract Class<?> getRestClass();

	protected void setPermissions(Set<String> permissions)
	{
		permissionsSet = true;
		TestRealm.setupRoles("JerseyTest", permissions);
		TestRealm.setLogin(USER, PASS, new HashSet<>(Arrays.asList("JerseyTest")));
	}

	protected String getBasicAuthHeader()
	{
		if (!permissionsSet) {
			setPermissions(new HashSet<>());
		}
		return "Basic " + Base64.encodeAsString(USER + ":" + PASS);
	}

	@Override
	protected TestContainerFactory getTestContainerFactory()
	{
		return new GrizzlyWebTestContainerFactory();
	}

	@Override
	protected DeploymentContext configureDeployment()
	{
		return ServletDeploymentContext
				.forServlet(new ServletContainer(new ResourceConfig(getRestClass())))
				.addListener(edu.usu.sdl.openstorefront.security.test.TestShiroLoader.class)
				.addFilter(ShiroAdjustedFilter.class, "ShiroFilter")
				.build();
	}

	/**
	 * NOTE: (KB) I don't want to start the full system so start minimal pieces
	 * until they can be re-factored so only what is needed for the test is
	 * started
	 */
	@BeforeClass
	public static void init()
	{
		// NOTE: (KB) As this is an integration test it would be nice to hit the 
		// DB however I dont have a good way to get the DB in a clean state
		ServiceProxy.Test.setPersistenceServiceToTest();
		OsgiManager.init();
		OSFCacheManager.init();
	}

	@Before
	public void setup()
	{
		permissionsSet = false;
		((TestPersistenceService) ServiceProxyFactory.getServiceProxy().getPersistenceService()).clear();
		TestRealm.clearLogin();
	}

	@AfterClass
	public static void cleanup()
	{
		OsgiManager.cleanup();
		OSFCacheManager.cleanUp();
	}

}
