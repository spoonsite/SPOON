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

import com.fasterxml.jackson.core.JsonProcessingException;
import edu.usu.sdl.openstorefront.common.util.OpenStorefrontConstant;
import java.io.IOException;
import static java.lang.Thread.sleep;
import java.security.Key;
import java.util.Arrays;
import java.util.Base64;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
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
	public void testEncyption() throws JsonProcessingException, IOException 
	{
		AesCipherService cipherService = new AesCipherService();
		
		Key key = cipherService.generateNewKey();
		byte[] keyBytes = key.getEncoded();		
		
		ByteSource encyptedBytes = cipherService.encrypt("Storefront TEST".getBytes(), keyBytes);
		
		System.out.println("encrypted: " + encyptedBytes.toBase64());
		
		ByteSource decrypted = cipherService.decrypt(encyptedBytes.getBytes(), keyBytes);
		
		System.out.println("decrypted: " + new String(decrypted.getBytes()));
		
		
		System.out.println("Key: " + Base64.getUrlEncoder().encodeToString(keyBytes));		

	}
	
	@Test 
	public void testLocks(){
		
		ExecutorService executor = Executors.newFixedThreadPool(3);
		
//		for (int i=0; i<10; i++) {
//			executor.submit(()->{
//				ReentrantLock lock = new ReentrantLock();
//				lock.lock();
//				try{
//					System.out.println("Lock");
//					sleep(1);
//				} catch (InterruptedException ex) {
//					Logger.getLogger(EncryptUseCase.class.getName()).log(Level.SEVERE, null, ex);
//				} finally {
//					lock.unlock();
//				}		
//			});
//		}
		
		ReentrantLock lock = new ReentrantLock();
		for (int i=0; i<10; i++) {
			executor.submit(()->{				
				lock.lock();
				try{
					System.out.println("Lock");
					sleep(1);
					System.out.println("Done with Wait");
				} catch (InterruptedException ex) {
					Logger.getLogger(EncryptUseCase.class.getName()).log(Level.SEVERE, null, ex);
				} finally {
					lock.unlock();
				}
				System.out.println("Fell through");
			});
		}		
		
		
		executor.shutdown();
		
	}
	
	@Test
	public void testSplit() 
	{
		String test = "A-b:::c-d-dsa";
		
		String codeParts[] = test.split(OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + 
				OpenStorefrontConstant.GENERAL_KEY_SEPARATOR + 
				OpenStorefrontConstant.GENERAL_KEY_SEPARATOR );
		
		Arrays.stream(codeParts).forEach(part->{
			System.out.println(part);
		});
		
		
	}
	
}
