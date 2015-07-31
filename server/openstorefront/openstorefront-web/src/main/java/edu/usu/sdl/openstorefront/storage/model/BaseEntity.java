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
package edu.usu.sdl.openstorefront.storage.model;

import edu.usu.sdl.openstorefront.service.ServiceProxy;
import edu.usu.sdl.openstorefront.service.query.QueryByExample;
import edu.usu.sdl.openstorefront.util.ReflectionUtil;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Version;

/**
 *
 * @author dshurtleff
 * @param <T>
 */
public abstract class BaseEntity<T>
		implements Serializable, Comparable<T>
{

	@Version
	private String storageVersion;

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
		ServiceProxy serviceProxy = new ServiceProxy();
		return (T) serviceProxy.getPersistenceService().queryOneByExample(this.getClass(), this);
	}

	/**
	 * Perform a query by example using this object as an example
	 *
	 * @return List of entities found or empty list if not found
	 */
	public List<T> findByExample()
	{
		ServiceProxy serviceProxy = new ServiceProxy();
		return (List<T>) serviceProxy.getPersistenceService().queryByExample(this.getClass(), this);
	}

	/**
	 * Perform a query by example using this object as an example
	 *
	 * @return Entity found or null if not found (Proxy Version matched with DB)
	 */
	public T findProxy()
	{
		ServiceProxy serviceProxy = new ServiceProxy();

		QueryByExample queryByExample = new QueryByExample(this);
		queryByExample.setReturnNonProxied(false);
		return (T) serviceProxy.getPersistenceService().queryOneByExample(this.getClass(), queryByExample);
	}

	/**
	 * Perform a query by example using this object as an example
	 *
	 * @return List of entities found or empty list if not found (Proxy Version
	 * matched with DB)
	 */
	public List<T> findByExampleProxy()
	{
		ServiceProxy serviceProxy = new ServiceProxy();

		QueryByExample queryByExample = new QueryByExample(this);
		queryByExample.setReturnNonProxied(false);
		return (List<T>) serviceProxy.getPersistenceService().queryByExample(this.getClass(), queryByExample);
	}

	@Override
	public int compareTo(T o)
	{
		if (o != null) {
			return 1;
		} else {
			return -1;
		}
	}

	public void applyDefaultValues()
	{
		ReflectionUtil.setDefaultsOnFields(this);
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
