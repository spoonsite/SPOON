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
package edu.usu.sdl.openstorefront.service.manager;

/**
 *
 * @author dshurtleff
 */
public interface PooledResourceManager<T>
{

	/**
	 * Gets a Client from the pool
	 *
	 * @return
	 */
	public T getClient();

	/**
	 * Releases client back to pool
	 *
	 * @param client
	 */
	public void releaseClient(T client);

	/**
	 * Gets max pool size
	 *
	 * @return size
	 */
	public int getMaxConnections();

	/**
	 * Gets available clients
	 *
	 * @return
	 */
	public int getAvailableConnections();

	/**
	 * This will make the connect pool empty
	 */
	public void shutdownPool();

}
