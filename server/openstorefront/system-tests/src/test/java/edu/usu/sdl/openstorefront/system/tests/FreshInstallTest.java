/*
 * Copyright 2017 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.system.tests;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * The purpose of this test is to verify that the application can be install
 * without pre-existing data.
 *
 * @author dshurtleff
 */
public class FreshInstallTest
{

	private static final Logger LOG = Logger.getLogger(FreshInstallTest.class.getName());

	@Test
	public void testFreshInstall()
	{
		Tomcat tomcat = initTomcat();
		deployApplicaiton(tomcat);
		try {
			verifyApplicationInstall(tomcat);
		} finally {
			cleanupShutdown(tomcat);
		}
	}

	private Tomcat initTomcat()
	{
		LOG.info("Initing tomcat");
		Tomcat tomcat = new Tomcat();
		tomcat.setPort(7980);
//		Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());

//		Tomcat.addServlet(ctx, "hello", new HttpServlet()
//		{
//			protected void service(HttpServletRequest req, HttpServletResponse resp)
//					throws ServletException, IOException
//			{
//				Writer w = resp.getWriter();
//				w.write("Hello, World!");
//				w.flush();
//			}
//		});
//		ctx.addServletMapping("/*", "hello");
		//	tomcat.setHost();
		return tomcat;
	}

	private void deployApplicaiton(Tomcat tomcat)
	{
		LOG.info("deploying application");

		//Context ctx = tomcat.addContext("/openstorefront", new File("../../../openstorefront-web/target/openstorefront").getAbsolutePath());
		try {
			System.setProperty("application.datadir", "/var/autotest");
			tomcat.addWebapp("/openstorefront", "../../../openstorefront-web/target/openstorefront");

			LOG.info("Starting Server");
			tomcat.start();

		} catch (ServletException | LifecycleException ex) {
			throw new RuntimeException(ex);
		}
	}

	private void verifyApplicationInstall(Tomcat tomcat)
	{
		try {
			LOG.log(Level.INFO, "Application Deployed Successfully");
			URL myURL = new URL("http://localhost:7980/openstorefront/Login.action");

			HttpURLConnection connection = (HttpURLConnection) myURL.openConnection();
			connection.setFollowRedirects(true);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestMethod("GET");
			connection.connect();

			int code = connection.getResponseCode();
			Assert.assertEquals("Application didn't deploy successfully", 200, code);
			LOG.log(Level.INFO, "Application Deployed Successfully");
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	private void cleanupShutdown(Tomcat tomcat)
	{
		LOG.info("Stopping Server");
		try {
			tomcat.stop();
		} catch (LifecycleException ex) {
			throw new RuntimeException(ex);
		} finally {
			File file = new File("/var/autotest");
			if (file.exists()) {
				try {
					FileUtils.deleteDirectory(file);
				} catch (IOException ex) {
					throw new RuntimeException("Unable to delete test directory", ex);
				}
			}
		}
	}

}
