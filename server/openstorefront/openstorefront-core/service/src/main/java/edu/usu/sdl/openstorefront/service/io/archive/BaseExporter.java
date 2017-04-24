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
package edu.usu.sdl.openstorefront.service.io.archive;

import edu.usu.sdl.openstorefront.common.exception.OpenStorefrontRuntimeException;
import edu.usu.sdl.openstorefront.core.entity.SystemArchive;
import edu.usu.sdl.openstorefront.service.ServiceProxy;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sourceforge.stripes.util.ResolverUtil;

/**
 *
 * @author dshurtleff
 */
public abstract class BaseExporter
{

	private static final Logger LOG = Logger.getLogger(BaseExporter.class.getName());

	protected ServiceProxy service = new ServiceProxy();
	protected SystemArchive archive;
	protected String archiveBasePath;
	private boolean addedError = false;

	public BaseExporter()
	{
	}

	public void init(SystemArchive archive, String archiveBasePath)
	{
		this.archive = archive;
		this.archiveBasePath = archiveBasePath;
	}

	public void exporterInit()
	{
	}

	/**
	 * This should be unique between export so order is maintain no matter where
	 * the user starts
	 *
	 * @return
	 */
	public abstract int getPriority();

	public abstract String getExporterSupportEntity();

	public static BaseExporter getExporter(String entity)
	{
		BaseExporter primaryExporter = null;

		ResolverUtil resolverUtil = new ResolverUtil();
		try {
			resolverUtil.find(new ResolverUtil.IsA(BaseExporter.class), "edu.usu.sdl.openstorefront.service.io.archive.export");
		} catch (Exception e) {
			LOG.log(Level.WARNING, "Unable resolve all exporter cases; may have partial results.");
		}
		for (Object testObject : resolverUtil.getClasses()) {
			Class testClass = (Class) testObject;
			try {
				BaseExporter exporter = (BaseExporter) testClass.newInstance();
				if (exporter.getExporterSupportEntity().equals(entity)) {
					primaryExporter = exporter;
				}
			} catch (InstantiationException | IllegalAccessException ex) {
				throw new OpenStorefrontRuntimeException(ex);
			}
		}

		return primaryExporter;
	}

	public abstract List<BaseExporter> getAllRequiredExports();

	public abstract long getTotalRecords();

	public abstract void exportRecords();

	public abstract void importRecords();

	protected void addError(String message)
	{
		addedError = true;
		service.getSystemArchiveServicePrivate().addErrorMessage(archive.getArchiveId(), message);
	}

	public boolean addedError()
	{
		return addedError;
	}

}
