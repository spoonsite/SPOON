/*
 * Copyright 2016 Space Dynamics Laboratory - Utah State University Research Foundation.
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
package edu.usu.sdl.openstorefront.core.api;

import edu.usu.sdl.openstorefront.core.entity.FeedbackTicket;

/**
 * Handles feedback
 * 
 * @author dshurtleff
 */
public interface FeedbackService
	extends AsyncService
{
	
	/**
	 * Save and handle feedbacking according to application settings
	 * 
	 * @param ticket 
	 * @return submitted ticket
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public FeedbackTicket submitFeedback(FeedbackTicket ticket);
	
	/**
	 * Marks that the ticket was handled by updating status
	 * 
	 * @param ticketId 
	 * @return  changed ticket or null if nothing changed
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public FeedbackTicket markAsComplete(String ticketId);
	
	/**
	 * Unmarks that the ticket was handled by updating status
	 * 
	 * @param ticketId 
	 * @return  changed ticket or null if nothing changed
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public FeedbackTicket markAsOutstanding(String ticketId);
	
	/**
	 * Delete feedback (hard delete)
	 * 
	 * @param ticketId 
	 */
	@ServiceInterceptor(TransactionInterceptor.class)
	public void deleteFeedback(String ticketId);
	
}
