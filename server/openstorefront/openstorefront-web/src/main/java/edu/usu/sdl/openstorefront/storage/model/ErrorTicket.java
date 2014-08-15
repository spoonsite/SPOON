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

package edu.usu.sdl.openstorefront.storage.model;

import java.util.Date;

/**
 *
 * @author jlaw
 */
public class ErrorTicket {
    private String errorTicketId;
    private String user;
    private String calledAction;
    private String input;
    private Date createDts;
    private String ticketFile;
    private String clientIp;
    private String errorTypeCode;

    /**
     * @return the errorTicketId
     */
    public String getErrorTicketId() {
        return errorTicketId;
    }

    /**
     * @param errorTicketId the errorTicketId to set
     */
    public void setErrorTicketId(String errorTicketId) {
        this.errorTicketId = errorTicketId;
    }

    /**
     * @return the user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return the calledAction
     */
    public String getCalledAction() {
        return calledAction;
    }

    /**
     * @param calledAction the calledAction to set
     */
    public void setCalledAction(String calledAction) {
        this.calledAction = calledAction;
    }

    /**
     * @return the input
     */
    public String getInput() {
        return input;
    }

    /**
     * @param input the input to set
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * @return the createDts
     */
    public Date getCreateDts() {
        return createDts;
    }

    /**
     * @param createDts the createDts to set
     */
    public void setCreateDts(Date createDts) {
        this.createDts = createDts;
    }

    /**
     * @return the ticketFile
     */
    public String getTicketFile() {
        return ticketFile;
    }

    /**
     * @param ticketFile the ticketFile to set
     */
    public void setTicketFile(String ticketFile) {
        this.ticketFile = ticketFile;
    }

    /**
     * @return the clientIp
     */
    public String getClientIp() {
        return clientIp;
    }

    /**
     * @param clientIp the clientIp to set
     */
    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    /**
     * @return the errorTypeCode
     */
    public String getErrorTypeCode() {
        return errorTypeCode;
    }

    /**
     * @param errorTypeCode the errorTypeCode to set
     */
    public void setErrorTypeCode(String errorTypeCode) {
        this.errorTypeCode = errorTypeCode;
    }
}
