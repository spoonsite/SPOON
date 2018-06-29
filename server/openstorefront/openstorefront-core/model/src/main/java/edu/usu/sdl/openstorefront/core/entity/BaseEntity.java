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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.api.Service;
import edu.usu.sdl.openstorefront.core.api.ServiceProxyFactory;
import edu.usu.sdl.openstorefront.core.api.query.QueryByExample;
import edu.usu.sdl.openstorefront.core.util.EntityUtil;
import edu.usu.sdl.openstorefront.validation.ValidationModel;
import edu.usu.sdl.openstorefront.validation.ValidationResult;
import edu.usu.sdl.openstorefront.validation.ValidationUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Version;

/**
 *
 * @author dshurtleff
 * @param <T>
 */
@APIDescription("This defines a storable entity")
public abstract class BaseEntity<T>
		implements Serializable, Comparable<T>
{

	@Version
	private String storageVersion;

	@SuppressWarnings({"squid:S2637", "squid:S1186"})
	public BaseEntity()
	{
	}

	/**
	 * Perform a query by example using this object as an example
	 *
	 * @return Entity found or null if not found
	 */
	public T find()
	{
		Service serviceProxy = ServiceProxyFactory.getServiceProxy();
		return (T) serviceProxy.getPersistenceService().queryOneByExample(this);
	}

	/**
	 * Perform a query by example using this object as an example
	 *
	 * @return List of entities found or empty list if not found
	 */
	public List<T> findByExample()
	{
		Service serviceProxy = ServiceProxyFactory.getServiceProxy();
		return (List<T>) serviceProxy.getPersistenceService().queryByExample(this);
	}

	/**
	 * Perform a query by example using this object as an example
	 *
	 * @return Entity found or null if not found (Proxy Version matched with DB)
	 */
	public T findProxy()
	{
		Service serviceProxy = ServiceProxyFactory.getServiceProxy();

		QueryByExample queryByExample = new QueryByExample(this);
		queryByExample.setReturnNonProxied(false);
		return (T) serviceProxy.getPersistenceService().queryOneByExample(queryByExample);
	}

	/**
	 * Perform a query by example using this object as an example
	 *
	 * @return List of entities found or empty list if not found (Proxy Version
	 * matched with DB)
	 */
	public List<T> findByExampleProxy()
	{
		Service serviceProxy = ServiceProxyFactory.getServiceProxy();

		QueryByExample queryByExample = new QueryByExample(this);
		queryByExample.setReturnNonProxied(false);
		return (List<T>) serviceProxy.getPersistenceService().queryByExample(queryByExample);
	}

	public ValidationResult validate()
	{
		return validate(true);
	}

	public ValidationResult validate(boolean consumeFieldsOnly)
	{
		ValidationModel validationModel = new ValidationModel(this);
		validationModel.setConsumeFieldsOnly(consumeFieldsOnly);
		return ValidationUtil.validate(validationModel);
	}

	@Override
	public int compareTo(T o)
	{
		if (o != null) {
			return this.equals(o) ? 0 : 1;
		} else {
			return -1;
		}
	}

	public void applyDefaultValues()
	{
		EntityUtil.setDefaultsOnFields(this);
	}

	public String getStorageVersion()
	{
		return storageVersion;
	}

	public void setStorageVersion(String storageVersion)
	{
		this.storageVersion = storageVersion;
	}

}
