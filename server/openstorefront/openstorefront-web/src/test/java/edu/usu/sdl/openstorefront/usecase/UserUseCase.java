/*
 * Copyright 2015 Space Dynamics Laboratory - Utah State University Research Foundation.
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

import com.fasterxml.jackson.core.type.TypeReference;
import edu.usu.sdl.openstorefront.common.util.Convert;
import edu.usu.sdl.openstorefront.common.util.StringProcessor;
import edu.usu.sdl.openstorefront.core.view.UserProfileView;
import edu.usu.sdl.openstorefront.core.view.UserProfileWrapper;
import java.io.File;
import java.io.IOException;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class UserUseCase
{
	
	@Test
	public void testUser() throws IOException {
		
		UserProfileWrapper userProfileWrapper = StringProcessor.defaultObjectMapper().readValue(new File("/temp/allusers.txt"), new TypeReference<UserProfileWrapper>(){});
		
		for (UserProfileView view : userProfileWrapper.getData()) {
			if (StringUtils.isNotBlank(view.getEmail())) {
				if (Convert.toBoolean(view.getNotifyOfNew())) {
					System.out.println(view.getEmail());
				}
			}
		}
		
	}
	
	@Test
	public void testGroupParse() 
	{
		String adGroup = "CN=STORE-Admin,OU=Groups,OU=DI2E-F,DC=basef,DC=dev,DC=lab";
		
		String groupFragments[] = adGroup.split(",");				
		for (String fragment : groupFragments) {
			String keyValue[] = fragment.split("=");
			if ("CN".equals(keyValue[0])) {
				System.out.println(keyValue[1]);
			}
		}
		
	}
	
}
