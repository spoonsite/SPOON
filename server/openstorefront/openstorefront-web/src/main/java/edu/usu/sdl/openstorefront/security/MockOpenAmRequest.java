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
package edu.usu.sdl.openstorefront.security;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Used to add open am headers.
 *
 * @author dshurtleff
 */
public class MockOpenAmRequest
		extends HttpServletRequestWrapper
{

	public MockOpenAmRequest(HttpServletRequest request)
	{
		super(request);
	}

	@Override
	public String getHeader(String name)
	{
		Map<String, String> fakeHeaders = new HashMap<>();

		fakeHeaders.put("sAMAccountName", "openam.testaccount");
		fakeHeaders.put("givenname", "Test");
		fakeHeaders.put("sn", "Account");
		fakeHeaders.put("mail", "test@test.com");
		fakeHeaders.put("telephonenumber", "555-255-5555");
		//fakeHeaders.put("memberOf", "CN=STORE-Admin, OU=USU");
		fakeHeaders.put("memberid", "55555");

		if (fakeHeaders.containsKey(name)) {
			return fakeHeaders.get(name);
		}
		return super.getHeader(name);
	}

	@Override
	public Enumeration<String> getHeaders(String name)
	{
		StringTokenizer tokenizer = new StringTokenizer("CN=STORE-Admin,OU=Groups,OU=DI2E-F,DC=basef,DC=dev,DC=lab | "
				+ "CN=VPN Users,OU=Groups,OU=DI2E-F,DC=basef,DC=dev,DC=lab |"
				+ "CN=USU Users,OU=Groups,OU=USU,DC=basef,DC=dev,DC=lab | "
				+ "CN=Atlassian Users,OU=Groups,OU=DI2E-F,DC=basef,DC=dev,DC=lab | "
				+ "CN=USU SysAdmin,OU=Groups,OU=USU,DC=basef,DC=dev,DC=lab | "
				+ "CN=Storefront SysAdmins,OU=Groups,OU=DI2E-F,DC=basef,DC=dev,DC=lab | "
				+ "CN=STORE-Eval,OU=Groups,OU=DI2E-F,DC=basef,DC=dev,DC=lab | "
				+ "CN=SWASe_Eval-ReadWrite,OU=Groups,OU=SWASe,OU=ProjectGroups,OU=External,DC=basef,DC=dev,DC=lab |", "|");

		if ("memberOf".equals(name)) {
			return (Enumeration) tokenizer;
		}
		return super.getHeaders(name);
	}

}
