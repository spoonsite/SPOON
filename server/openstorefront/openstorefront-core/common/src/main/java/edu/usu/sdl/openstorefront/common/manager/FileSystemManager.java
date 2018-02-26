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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

	private static final Logger LOG = Logger.getLogger(FileSystemManager.class.getName());

	public static final String MAIN_PERM_DIR = "/perm";
	public static final String MAIN_TEMP_DIR = "/temp";

	public static final String SYSTEM_TEMP_DIR = System.getProperty("java.io.tmpdir");

	public static final String CONFIG_DIR = "/config";
	public static final String IMPORT_DIR = "/import";
	public static final String IMPORT_HISTORY_DIR = "/import/history";
	public static final String IMPORT_LOOKUP_DIR = "/import/lookup";
	public static final String IMPORT_ARTICLE_DIR = "/import/article";
	public static final String IMPORT_HIGHLIGHT_DIR = "/import/highlights";
	public static final String IMPORT_COMPONENT_DIR = "/import/component";
	public static final String ARTICLE_DIR = MAIN_PERM_DIR + "/article";
	public static final String MEDIA_DIR = MAIN_PERM_DIR + "/media";
	public static final String ATTACHMENT_DIR = MAIN_PERM_DIR + "/attachment";
	public static final String ARCHIVE_DIR = MAIN_PERM_DIR + "/archive";
	public static final String GENERAL_MEDIA_DIR = MAIN_PERM_DIR + "/generalmedia";
	public static final String SUPPORT_MEDIA_DIR = MAIN_PERM_DIR + "/supportmedia";
	public static final String TEMPORARY_MEDIA_DIR = MAIN_PERM_DIR + "/temporarymedia";
	public static final String ERROR_TICKET_DIR = MAIN_TEMP_DIR + "/errorticket";
	public static final String RESOURCE_DIR = MAIN_PERM_DIR + "/resource";
	public static final String ORGANIZATION_DIR = MAIN_PERM_DIR + "/organization";
	public static final String REPORT_DIR = MAIN_PERM_DIR + "/report";
	public static final String PLUGIN_DIR = MAIN_PERM_DIR + "/plugins";
	public static final String COMPONENT_VERSION_DIR = MAIN_PERM_DIR + "/componentversion";
	public static final String PLUGIN_UNINSTALLED_DIR = MAIN_PERM_DIR + "/plugins/uninstalled";
	public static final String PLUGIN_FAILED_DIR = MAIN_PERM_DIR + "/plugins/failed";
	public static final String DB_DIR = "/db";

	private static final int BUFFER_SIZE = 8192;

	private AtomicBoolean started = new AtomicBoolean(false);
	private String baseDirectory;

	protected static FileSystemManager singleton = null;

	public static FileSystemManager getInstance()
	{
		if (singleton == null) {
			singleton = new FileSystemManager();
		}
		return singleton;
	}

	protected FileSystemManager()
	{
		setBaseDirectory(System.getProperty("application.datadir", "/var/openstorefront"));
	}

	public List<String> getTopLevelDirectories(Set<String> exclude)
	{
		if (exclude == null) {
			exclude = new HashSet<>();
		}

		List<String> directories = new ArrayList<>();

		File mainDir = new File(getBaseDirectory());
		File files[] = mainDir.listFiles();
		if (files != null) {
			for (File file : files) {
				if (file.isDirectory()) {
					String path = file.getPath();
					if (!exclude.contains(path)) {
						directories.add(file.getPath());
					}
				}
			}
		}
		directories.sort(null);
		return directories;
	}

	/**
	 * This get the relative path and create directories as needed
	 *
	 * @param directory
	 * @return
	 */
	public File getDir(String directory)
	{
		Objects.requireNonNull(directory);

		File dir;
		if (!SYSTEM_TEMP_DIR.equals(directory)) {
			dir = new File(getBaseDirectory() + directory);
			if (dir.mkdirs()) {
				LOG.log(Level.FINEST, "Not all directories were created. Highly likely directories already exist.  If not, Check permission and Disk Space");
			}
		} else {
			dir = new File(directory);
		}
		return dir;
	}

	public File getConfig(String configFilename)
	{
		Objects.requireNonNull(configFilename);
		return getFileDir(configFilename, CONFIG_DIR, "/");
	}

	public File getImportLookup(String configFilename)
	{
		return getImportLookup(configFilename, null);
	}

	public File getImportLookup(String configFilename, NewFileHandler newFileHandler)
	{
		return getFileDir(configFilename, IMPORT_LOOKUP_DIR, "/data/lookup/", newFileHandler);
	}

	private File getFileDir(String configFilename, String directory, String resourceDir)
	{
		return getFileDir(configFilename, directory, resourceDir, null);
	}

	private File getFileDir(String configFilename, String directory, String resourceDir, NewFileHandler newFileHandler)
	{
		File configFile = new File(getDir(directory) + "/" + configFilename);
		if (configFile.exists() == false) {
			LOG.log(Level.INFO, MessageFormat.format("Trying to copy: {0}{1} to {2}", new Object[]{resourceDir, configFilename, configFile}));

			URL resourceUrl = new FileSystemManager().getClass().getResource(resourceDir + configFilename);
			if (resourceUrl != null) {
				try (InputStream in = new FileSystemManager().getClass().getResourceAsStream(resourceDir + configFilename)) {
					Files.copy(in, Paths.get(getBaseDirectory() + directory + "/" + configFilename), StandardCopyOption.REPLACE_EXISTING);
				} catch (IOException ex) {
					throw new OpenStorefrontRuntimeException(ex);
				}
			} else {
				throw new OpenStorefrontRuntimeException(
						MessageFormat.format("Unable to find resource: {0}{1}", new Object[]{resourceDir, configFilename}),
						"Check name and jar/packaging"
				);
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
	public InputStream getApplicationResourceFile(String resource)
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
	public long copy(InputStream source, OutputStream sink)
			throws IOException
	{
		Objects.requireNonNull(source);
		Objects.requireNonNull(sink);

		long nread = 0L;
		byte[] buf = new byte[BUFFER_SIZE];
		int n;
		while ((n = source.read(buf)) > 0) {
			sink.write(buf, 0, n);
			nread += n;
		}
		return nread;
	}

	@Override
	public void initialize()
	{
		FileSystemManager fileSystemManager = FileSystemManager.getInstance();

		//setup Dirs
		fileSystemManager.getDir(FileSystemManager.MEDIA_DIR);
		fileSystemManager.getDir(FileSystemManager.RESOURCE_DIR);
		fileSystemManager.getDir(FileSystemManager.REPORT_DIR);
		fileSystemManager.getDir(FileSystemManager.IMPORT_HISTORY_DIR);

		started.set(true);
	}

	@Override
	public void shutdown()
	{
		started.set(false);
	}

	@Override
	public boolean isStarted()
	{
		return started.get();
	}

	public String getBaseDirectory()
	{
		return baseDirectory;
	}

	public void setBaseDirectory(String baseDirectory)
	{
		this.baseDirectory = baseDirectory;
	}

}
