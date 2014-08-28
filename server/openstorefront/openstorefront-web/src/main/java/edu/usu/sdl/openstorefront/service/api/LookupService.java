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
package edu.usu.sdl.openstorefront.service.api;

import edu.usu.sdl.openstorefront.service.ServiceInterceptor;
import edu.usu.sdl.openstorefront.service.TransactionInterceptor;
import edu.usu.sdl.openstorefront.storage.model.BaseEntity;
import edu.usu.sdl.openstorefront.storage.model.LookupEntity;
import java.util.List;

/**
 * This service handles system tables
 *
 * @author dshurtleff
 */
public interface LookupService
{

	/**
	 * This return only active
	 *
	 * @see findLookup(Class<T> lookTableClass, boolean all);
	 * @param <T>
	 * @param lookTableClass
	 * @return
	 */
	public <T extends LookupEntity> List<T> findLookup(Class<T> lookTableClass);

	/**
	 * Find items for a given Look up resource
	 *
	 * @param <T>
	 * @param lookTableClass
	 * @param all - default to just active
	 * @return
	 */
	public <T extends LookupEntity> List<T> findLookup(Class<T> lookTableClass, boolean all);

	/**
	 * Add or Updates a lookup code
	 *
	 * @param lookupEntity
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void saveLookupValue(LookupEntity lookupEntity);
	
	/**
	 * In-activates code if code is not found this will still succeed
	 * @param <T>
	 * @param lookTableClass
	 * @param code 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public <T extends LookupEntity> void removeValue(Class<T> lookTableClass, String code);

	/**
	 * This with add or inactive existing codes. Any existing value not in this
	 * list will be in-activated. Other existing items will be just updated
	 *
	 * @param <T>
	 * @param lookupClass
	 * @param lookupValues
	 */
	public <T extends LookupEntity> void syncLookupImport(Class<T> lookupClass, List<LookupEntity> lookupValues);

}
