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

import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import edu.usu.sdl.openstorefront.core.annotation.APIDescription;
import edu.usu.sdl.openstorefront.core.annotation.PK;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author dshurtleff
 */
@APIDescription("Security account; used in the builtin security realm.")
public class UserSecurity
{
	@PK
	@NotNull
	@Size(min = 1, max = OpenStorefrontConstant.FIELD_SIZE_USERNAME)
	private String username;
		
	@Size(min = 0, max = OpenStorefrontConstant.FIELD_SIZE_80)
	@APIDescription("Only Applicatble when using internal security")	
	private transient String password;	
	
	private transient String userSalt;
	
}
