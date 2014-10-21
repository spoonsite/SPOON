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
package edu.usu.sdl.openstorefront.service.manager;

import edu.usu.sdl.openstorefront.exception.OpenStorefrontRuntimeException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Provide single access to system properties
 *
 * @author dshurtleff
 */
public class PropertiesManager
{

	private static final Logger log = Logger.getLogger(PropertiesManager.class.getName());

	public static final String KEY_USE_REST_PROXY = "service.rest.proxy";
	public static final String KEY_DB_CONNECT_MIN = "db.connectionpool.min";
	public static final String KEY_DB_CONNECT_MAX = "db.connectionpool.max";
	public static final String KEY_DB_USER = "db.user";
	public static final String KEY_DB_AT = "db.pw";
	public static final String KEY_MAX_ERROR_TICKETS = "errorticket.max";
	public static final String KEY_SOLR_URL = "solr.server.url";

	public static final String KEY_OPENAM_URL = "openam.url";
	public static final String KEY_LOGOUT_URL = "logout.url";
	public static final String KEY_OPENAM_HEADER_USERNAME = "openam.header.username";
	public static final String KEY_OPENAM_HEADER_FIRSTNAME = "openam.header.firstname";
	public static final String KEY_OPENAM_HEADER_LASTNAME = "openam.header.lastname";
	public static final String KEY_OPENAM_HEADER_EMAIL = "openam.header.email";
	public static final String KEY_OPENAM_HEADER_GROUP = "openam.header.group";
	public static final String KEY_OPENAM_HEADER_LDAPGUID = "openam.header.ldapguid";
	public static final String KEY_OPENAM_HEADER_ORGANIZATION = "openam.header.organization";
	public static final String KEY_OPENAM_HEADER_ADMIN_GROUP = "openam.header.admingroupname";

	public static final String KEY_TOOLS_USER = "tools.login.user";
	public static final String KEY_TOOLS_CREDENTIALS = "tools.login.pw";

	public static final String KEY_JIRA_POOL_SIZE = "jira.connectionpool.size";
	public static final String KEY_JIRA_CONNECTION_WAIT_TIME = "jira.connection.wait.seconds";
	public static final String KEY_JIRA_URL = "jira.server.url";

	private static Properties properties;
	private static final String PROPERTIES_FILENAME = FileSystemManager.getConfig("openstorefront.properties").getPath();

	public static String getValue(String key)
	{
		return getProperties().getProperty(key);
	}

	public static String getValue(String key, String defaultValue)
	{
		return getProperties().getProperty(key, defaultValue);
	}

	public static void setProperty(String key, String value)
	{
		getProperties().setProperty(value, value);
		saveProperties();
	}

	private static Properties getProperties()
	{
		if (properties == null) {
			loadProperties();
		}
		return properties;
	}

	private static void loadProperties()
	{
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try {
			if (Paths.get(PROPERTIES_FILENAME).toFile().createNewFile()) {
				log.log(Level.WARNING, "Open Catalog properties file was missing from location a new file was created.  Location: {0}", PROPERTIES_FILENAME);
			}
			try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(PROPERTIES_FILENAME))) {
				properties = new Properties();
				properties.load(bin);
			} catch (IOException e) {
				throw new OpenStorefrontRuntimeException(e);
			}
		} catch (IOException e) {
			throw new OpenStorefrontRuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	private static void saveProperties()
	{
		ReentrantLock lock = new ReentrantLock();
		lock.lock();
		try (BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(PROPERTIES_FILENAME))) {
			properties.store(bout, "openstorefront Properties");
		} catch (IOException e) {
			throw new OpenStorefrontRuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

}
