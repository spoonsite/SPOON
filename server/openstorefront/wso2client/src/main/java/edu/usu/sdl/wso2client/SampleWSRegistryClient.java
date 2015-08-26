/*
 *  Copyright (c) 2005-2010, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 *  WSO2 Inc. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.usu.sdl.wso2client;

import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.model.ComponentAll;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.axis2.context.ConfigurationContext;
import org.apache.axis2.context.ConfigurationContextFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.osgi.service.component.annotations.Component;
import org.wso2.carbon.base.ServerConfiguration;
import org.wso2.carbon.registry.core.Registry;
import org.wso2.carbon.registry.core.Resource;
import org.wso2.carbon.registry.ws.client.registry.WSRegistryServiceClient;

public class SampleWSRegistryClient
{

	private static ConfigurationContext configContext = null;

	//private static final String CARBON_HOME = ".." + File.separator + ".." + File.separator;
	//private static final String axis2Repo = CARBON_HOME + File.separator + "repository"
	//		+ File.separator + "deployment" + File.separator + "client";
	private static final String axis2Repo = "\\var\\openstorefront\\config\\wso2\\";
	private static final String axis2Conf = ServerConfiguration.getInstance().getFirstProperty("Axis2Config.clientAxis2XmlLocation");
	private static final String username = "admin";
	private static final String password = "admin";
	private static final String serverURL = "https://localhost:9443/services/";

	private static WSRegistryServiceClient initialize() throws Exception
	{

		System.setProperty("javax.net.ssl.trustStore", "\\var\\openstorefront\\config\\wso2\\" + "wso2carbon.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "wso2carbon");
		System.setProperty("javax.net.ssl.trustStoreType", "JKS");
		System.setProperty("carbon.repo.write.mode", "true");
		configContext = ConfigurationContextFactory.createConfigurationContextFromFileSystem(
				axis2Repo, axis2Conf);
		return new WSRegistryServiceClient(serverURL, username, password, configContext);
	}

	public static void main(String[] args) throws Exception
	{
		Registry registry = initialize();
		try {
			//load component

			List<ComponentAll> components;
			try (InputStream in = new FileInputStream("C:\\temp\\components.json")) {
				components = StringProcessor.defaultObjectMapper().readValue(in, new TypeReference<List<ComponentAll>>()
				{
				});
			} catch (IOException ex) {
				throw ex;
			}

			ComponentAll componentAll = components.get(0);

			Resource resource = registry.newResource();
			resource.setContent(componentAll.getComponent().getDescription());

			resource.setDescription("Storefront Component");
			resource.setMediaType("application/openstorefront");
			resource.setUUID(componentAll.getComponent().getComponentId());

			try {
				Map fieldMap = BeanUtils.describe(componentAll.getComponent());
				fieldMap.keySet().stream().forEach((key) -> {
					if ("description".equals(key) == false) {
						resource.setProperty(Component.class.getSimpleName() + "_" + key, "" + fieldMap.get(key));
						//System.out.println("key  = " + Component.class.getSimpleName() + "_" + key);
						//System.out.println("Value  = " + fieldMap.get(key));
					}
				});
			} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
				Logger.getLogger(StringProcessor.class.getName()).log(Level.SEVERE, null, ex);
			}

			String resourcePath = "/storefront/components/" + componentAll.getComponent().getComponentId();
			registry.put(resourcePath, resource);

//
			System.out.println("A resource added to: " + resourcePath);
//			registry.rateResource(resourcePath, 4);
//
//			System.out.println("Resource rated with 4 stars!");
//			Comment comment = new Comment();
//			comment.setText("Testing Connection");
//			registry.addComment(resourcePath, comment);
//			System.out.println("Comment added to resource");
//
//			Resource getResource = registry.get("/abc2");
//			System.out.println("Resource retrived");
//			System.out.println("Printing retrieved resource content: "
//					+ new String((byte[]) getResource.getContent()));

//			Resource resource = registry.newResource();
//			resource.setContent("Hello Out there!");
//
//			String resourcePath = "/abc3";
//			registry.put(resourcePath, resource);
//
//			System.out.println("A resource added to: " + resourcePath);
//
//			registry.rateResource(resourcePath, 4);
//
//			System.out.println("Resource rated with 4 stars!");
//			Comment comment = new Comment();
//			comment.setText("Testing Connection");
//			registry.addComment(resourcePath, comment);
//			System.out.println("Comment added to resource");
//
//			Resource getResource = registry.get("/abc3");
//			System.out.println("Resource retrived");
//			System.out.println("Printing retrieved resource content: "
//					+ new String((byte[]) getResource.getContent()));
		} finally {
			//Close the session
			((WSRegistryServiceClient) registry).logut();
		}
		System.exit(0);
	}
}
