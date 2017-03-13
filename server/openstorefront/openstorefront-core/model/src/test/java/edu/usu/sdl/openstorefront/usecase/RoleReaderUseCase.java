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
package edu.usu.sdl.openstorefront.usecase;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class RoleReaderUseCase
{
	@Test
	public void testReadRoles() throws IOException
	{
		List<String> roles = Files.readAllLines(Paths.get("C:\\test\\roles.txt"));
		roles.stream().forEach(role->{
			System.out.println("public static final String " + role.replace("-", "_") + " = \"" + role + "\";");			
		});
		
		roles.stream().forEach(role->{
			String key = role.replace("-", "_");
			System.out.println("codeMap.put("+ key +", newLookup(SecurityPermission.class, "+ key +", \""+StringUtils.capitalize(key.replace("_", " ").toLowerCase() )+"\"));");
			
		});
		
	}
	
}
