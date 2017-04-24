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
package edu.usu.sdl.openstorefront.common.manager;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.MessageFormat;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Handles storage and retrieval of files
 *
 * @author dshurtleff
 */
public class FileSystemManager
		implements Initializable
{
	
	private static final Logger log = Logger.getLogger(FileSystemManager.class.getName());

	public static final String MAIN_DIR = System.getProperty("application.datadir", "/var/openstorefront");
	public static final String MAIN_PERM_DIR = MAIN_DIR + "/perm";
	public static final String MAIN_TEMP_DIR = MAIN_DIR + "/temp";
	public static final String SYSTEM_TEMP_DIR = System.getProperty("java.io.tmpdir");
	public static final String CONFIG_DIR = MAIN_DIR + "/config";
	public static final String IMPORT_DIR = MAIN_DIR + "/import";
	public static final String IMPORT_HISTORY_DIR = MAIN_DIR + "/import/history";
	public static final String IMPORT_LOOKUP_DIR = MAIN_DIR + "/import/lookup";
	public static final String IMPORT_ARTICLE_DIR = MAIN_DIR + "/import/article";
	public static final String IMPORT_HIGHLIGHT_DIR = MAIN_DIR + "/import/highlights";
	public static final String IMPORT_COMPONENT_DIR = MAIN_DIR + "/import/component";
	public static final String ARTICLE_DIR = MAIN_PERM_DIR + "/article";
	public static final String MEDIA_DIR = MAIN_PERM_DIR + "/media";
	public static final String ATTACHMENT_DIR = MAIN_PERM_DIR + "/attachment";
	public static final String GENERAL_MEDIA_DIR = MAIN_PERM_DIR + "/generalmedia";
	public static final String TEMPORARY_MEDIA_DIR = MAIN_PERM_DIR + "/temporarymedia";
	public static final String ERROR_TICKET_DIR = MAIN_TEMP_DIR + "/errorticket";
	public static final String RESOURCE_DIR = MAIN_PERM_DIR + "/resource";
	public static final String REPORT_DIR = MAIN_PERM_DIR + "/report";
	public static final String PLUGIN_DIR = MAIN_PERM_DIR + "/plugins";
	public static final String COMPONENT_VERSION_DIR = MAIN_PERM_DIR + "/componentversion";
	public static final String PLUGIN_UNINSTALLED_DIR = MAIN_PERM_DIR + "/plugins/uninstalled";
        public static final String PLUGIN_FAILED_DIR = MAIN_PERM_DIR + "/plugins/failed";
	public static final String DB_DIR = MAIN_DIR + "/db";

	private static AtomicBoolean started = new AtomicBoolean(false);
	
	private static final int BUFFER_SIZE = 8192;

	public static File getDir(String directory)
	{
		File dir = new File(directory);
		if (dir.mkdirs()) {
			log.log(Level.FINEST, "Not all directories were created. Highly likely directories already exist.  If not, Check permission and Disk Space");
		}
		return dir;
	}

	public static File getConfig(String configFilename)
	{
		return getFileDir(configFilename, CONFIG_DIR, "/");
	}

	public static File getImportLookup(String configFilename)
	{
		return getImportLookup(configFilename, null);
	}

	public static File getImportLookup(String configFilename, NewFileHandler newFileHandler)
	{
		return getFileDir(configFilename, IMPORT_LOOKUP_DIR, "/data/lookup/", newFileHandler);
	}

	private static File getFileDir(String configFilename, String directory, String resourceDir)
	{
		return getFileDir(configFilename, directory, resourceDir, null);
	}

	private static File getFileDir(String configFilename, String directory, String resourceDir, NewFileHandler newFileHandler)
	{
		File configFile = new File(getDir(directory) + "/" + configFilename);
		if (configFile.exists() == false) {
			log.log(Level.INFO, MessageFormat.format("Trying to copy: {0}{1} to {2}", new Object[]{resourceDir, configFilename, configFile}));

			URL resourceUrl = new FileSystemManager().getClass().getResource(resourceDir + configFilename);
			if (resourceUrl != null) {
				try (InputStream in = new FileSystemManager().getClass().getResourceAsStream(resourceDir + configFilename)) {
					Files.copy(in, Paths.get(directory + "/" + configFilename), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException(ex);
				}
			} else {
				log.log(Level.WARNING, MessageFormat.format("Unable to find resource: {0}{1}", new Object[]{resourceDir, configFilename}));
			}

			if (newFileHandler != null) {
				if (configFile.exists()) {
					newFileHandler.handleNewFile(configFile);
				}
			}
		}
		return configFile;
	}

	/**
	 * Gets a resource from the application war
	 *
	 * @param resource
	 * @return inputstream to resource (It's up to the caller to close the
	 * stream)
	 */
	public static InputStream getApplicationResourceFile(String resource)
	{
		InputStream in = null;
		URL resourceUrl = new FileSystemManager().getClass().getResource(resource);
		if (resourceUrl != null) {
			in = new FileSystemManager().getClass().getResourceAsStream(resource);
		} else {
			throw new OpenStorefrontRuntimeException("Unable to find internal resource file: " + resource, "This likely a programming issue or an issue with the environment.");
		}
		return in;
	}

	/**
	 * copy from input to output Note: it doesn't close either stream
	 *
	 * @param source
	 * @param sink
	 * @return
	 * @throws IOException
	 */
	public static long copy(InputStream source, OutputStream sink)
			throws IOException
	{
		long nread = 0L;
		byte[] buf = new byte[BUFFER_SIZE];
		int n;
		while ((n = source.read(buf)) > 0) {
			sink.write(buf, 0, n);
			nread += n;
		}
		return nread;
	}

	public static void init()
	{
		//setup Dirs
		FileSystemManager.getDir(FileSystemManager.MEDIA_DIR);
		FileSystemManager.getDir(FileSystemManager.RESOURCE_DIR);
		FileSystemManager.getDir(FileSystemManager.REPORT_DIR);
		FileSystemManager.getDir(FileSystemManager.IMPORT_HISTORY_DIR);
		
		started.set(true);		
	}

	public static void cleanup()
	{
		//Nothing to do for now
		started.set(false);
	}

	@Override
	public void initialize()
	{
		FileSystemManager.init();
	}

	@Override
	public void shutdown()
	{
		FileSystemManager.cleanup();
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

}
