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

import edu.usu.sdl.openstorefront.core.api.PersistenceService;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.entity.SecurityRole;
import edu.usu.sdl.openstorefront.core.entity.UserProfile;
import edu.usu.sdl.openstorefront.security.UserContext;
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
	private static final String ROLE = "JerseyTest";
	private UserContext userContext = null;
	private boolean permissionsSet = false;
	private boolean userContextSet = false;

	protected abstract Class<?> getRestClass();

	/**
	 * sets the userContext to be retrieved from the session
	 *
	 * @param userContext user context to return
	 */
	protected void setUserContext(UserContext userContext)
	{
		if (userContextSet) {
			throw new IllegalStateException("setUserContext should only be called once per test");
		}
		userContextSet = true;
		this.userContext = userContext;
		TestRealm.setUserContext(this.userContext);
	}

	protected UserContext getUserContext()
	{
		if (!userContextSet) {
			createUserContext();
		}
		return this.userContext;
	}

	/**
	 * Sets a single user with a set of specified set of Security Permissions.
	 * This should be called at most once per test to add additional users
	 * and/or roles with different Security Permissions use TestRealm.setLogin()
	 * and TestRealm.setupRoles()
	 *
	 * @param permissions set of Security Permissions to configure the user with
	 */
	protected void setPermissions(Set<String> permissions)
	{
		if (permissionsSet) {
			throw new IllegalStateException("setPermissions should only be called once per test");
		}
		permissionsSet = true;
		if (!userContextSet) {
			createUserContext();
		}
		TestRealm.setupRoles(ROLE, permissions);
		TestRealm.setLogin(USER, PASS, new HashSet<>(Arrays.asList(ROLE)));
	}

	/**
	 * Sets a single user with a single specified Security Permission. This
	 * should be called at most once per test to set multiple permissions use
	 * setPermissions(Set&lt;String&gt; permissions)
	 *
	 * @param permission Security Permission to set for the user
	 */
	protected void setSinglePermission(String permission)
	{
		if (permissionsSet) {
			throw new IllegalStateException("setSinglePermission should only be called once per test");
		}
		setPermissions(new HashSet<>(Arrays.asList(permission)));
	}

	/**
	 * creates the Basic Authentication Header with the default credentials
	 * (i.e. base64 encoded string of "username:password")
	 *
	 * @return the value for the authentication header
	 */
	protected String getBasicAuthHeader()
	{
		return getBasicAuthHeader(USER, PASS);
	}

	/**
	 * creates the Basic Authentication Header with the specified credentials
	 * (i.e. base64 encoded string of "username:password")
	 *
	 * @param username desired username
	 * @param password desired password
	 * @return the value for the authentication header
	 */
	protected String getBasicAuthHeader(String username, String password)
	{
		if (!permissionsSet) {
			setPermissions(new HashSet<>());
		}
		return "Basic " + Base64.encodeAsString(username + ":" + password);
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

	@BeforeClass
	public static void init()
	{
		/**
		 * NOTE: (KB) I don't want to start the full system so start minimal
		 * pieces until they can be re-factored so only what is needed for the
		 * test is started. As this is an integration test it would be nice to
		 * hit the DB however I don't have a good way to get the DB in a clean
		 * state
		 */
		ServiceProxy.Test.setPersistenceServiceToTest();
		OsgiManager.init();
		OSFCacheManager.init();
	}

	@Before
	public void setup()
	{
		permissionsSet = false;
		PersistenceService persistenceService = ServiceProxyFactory.getServiceProxy().getPersistenceService();
		if (persistenceService instanceof TestPersistenceService) {
			((TestPersistenceService) persistenceService).clear();
		}
		ServiceProxyFactory.setTestService(null);
		TestRealm.clearLogin();
	}

	@AfterClass
	public static void cleanup()
	{
		OsgiManager.cleanup();
		OSFCacheManager.cleanUp();
	}

	private void createUserContext()
	{
		UserProfile userProfile = new UserProfile();
		userProfile.setUsername(USER);
		userProfile.setActiveStatus(UserProfile.ACTIVE_STATUS);
		userProfile.setFirstName("TEST");
		userProfile.setLastName("USER");
		UserContext context = new UserContext();
		context.setAdmin(false);
		context.setUserProfile(userProfile);

		SecurityRole role = new SecurityRole();
		role.setActiveStatus(SecurityRole.ACTIVE_STATUS);
		role.setAllowUnspecifiedDataSensitivity(Boolean.TRUE);
		role.setAllowUnspecifiedDataSource(Boolean.TRUE);
		context.setRoles(Arrays.asList(role));
		setUserContext(context);
	}

}
