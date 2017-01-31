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
package edu.usu.sdl.core;

import java.security.Key;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import org.junit.Test;

/**
 *
 * @author dshurtleff
 */
public class EncryptUseCase
{
	@Test
	public void testEncyption() 
	{
		AesCipherService cipherService = new AesCipherService();
		
		Key key = cipherService.generateNewKey();
		byte[] keyBytes = key.getEncoded();		
		
		ByteSource encyptedBytes = cipherService.encrypt("Storefront TEST".getBytes(), keyBytes);
		
		System.out.println("encrypted: " + encyptedBytes.toBase64());
		
		ByteSource decrypted = cipherService.decrypt(encyptedBytes.getBytes(), keyBytes);
		
		System.out.println("decrypted: " + new String(decrypted.getBytes()));
		
		
	}
	
}
