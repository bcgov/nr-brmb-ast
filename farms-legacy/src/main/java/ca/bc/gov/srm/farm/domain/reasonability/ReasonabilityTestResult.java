/**
 *
 * Copyright (c) 2018, 
 * Government of British Columbia, 
 * Canada
 *
 * All rights reserved. 
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.domain.reasonability;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import ca.bc.gov.srm.farm.domain.codes.MessageTypeCodes;
import ca.bc.gov.srm.farm.reasonability.ReasonabilityTestResultMessage;

/**
 * @author awilkinson
 */
public abstract class ReasonabilityTestResult implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private Map<String, List<ReasonabilityTestResultMessage>> messages;
  
  private Boolean result;
  
  public Boolean getResult() {
    return result;
  }

  public void setResult(Boolean result) {
    this.result = result;
  }
  
  public void copy(ReasonabilityTestResult o) {
    result = o.result;
    messages = o.getMessages();
  }
  
  public Map<String, List<ReasonabilityTestResultMessage>> getMessages() {
    if(messages == null) {
      messages = new HashMap<>();
    }
    return messages;
  }

  public void setMessages(Map<String, List<ReasonabilityTestResultMessage>> messages) {
    this.messages = messages;
  }
  
  public List<ReasonabilityTestResultMessage> getErrorMessages() {
    return getMessages(MessageTypeCodes.ERROR);
  }
  
  public List<ReasonabilityTestResultMessage> getWarningMessages() {
    return getMessages(MessageTypeCodes.WARNING);
  }
  
  public List<ReasonabilityTestResultMessage> getInfoMessages() {
    return getMessages(MessageTypeCodes.INFO);
  }

  private List<ReasonabilityTestResultMessage> getMessages(String messageType) {
    List<ReasonabilityTestResultMessage> list = getMessages().get(messageType);
    if(list == null) {
      list = new ArrayList<>();
      getMessages().put(messageType, list);
    }
    return list;
  }
  
  public ReasonabilityTestResultMessage addErrorMessage(String message, Object... parameters) {
    ReasonabilityTestResultMessage msg = new ReasonabilityTestResultMessage(message, MessageTypeCodes.ERROR, parameters);
    getErrorMessages().add(msg);
    return msg;
  }
  
  public ReasonabilityTestResultMessage addWarningMessage(String message, Object... parameters) {
    ReasonabilityTestResultMessage msg = new ReasonabilityTestResultMessage(message, MessageTypeCodes.WARNING, parameters);
    getWarningMessages().add(msg);
    return msg;
  }
  
  public ReasonabilityTestResultMessage addInfoMessage(String message, Object... parameters) {
    ReasonabilityTestResultMessage msg = new ReasonabilityTestResultMessage(message, MessageTypeCodes.INFO, parameters);
    getInfoMessages().add(msg);
    return msg;
  }

  @JsonIgnore
  public abstract String getName();
}