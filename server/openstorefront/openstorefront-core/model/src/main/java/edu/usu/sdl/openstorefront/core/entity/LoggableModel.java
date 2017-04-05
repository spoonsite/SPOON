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
package edu.usu.sdl.openstorefront.core.entity;

import edu.usu.sdl.openstorefront.core.model.FieldChangeModel;
import java.util.List;

/**
 * Used to support the change log
 *
 * @author dshurtleff
 * @param <T>
 */
public interface LoggableModel<T>
{

	/**
	 * Find fields that have changed between this model and an updated version
	 * This should be fields that are user changeable for the most part.
	 *
	 * @param updated entity
	 * @return
	 */
	public List<FieldChangeModel> findChanges(T updated);

	/**
	 * This should be message that identifies the record to a user.
	 *
	 * @return Field Message
	 */
	public String addRemoveComment();

	/**
	 * Set parent entity
	 *
	 * @param changeLog
	 */
	public void setChangeParent(ChangeLog changeLog);

}
