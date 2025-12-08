package ca.bc.gov.srm.farm.reasonability;

import java.io.Serializable;

public class ReasonabilityTestResultMessage implements Serializable {
  
  private static final long serialVersionUID = 1L;
  
  private String message;
  private String messageTypeCode;
  
  public ReasonabilityTestResultMessage(String message, String messageTypeCode, Object... parameters) {
    super();
    this.message = java.text.MessageFormat.format(message, parameters);
    this.messageTypeCode = messageTypeCode;
  }

  public String getMessage() {
    return message;
  }
  
  public void setMessage(String message) {
    this.message = message;
  }
  
  public String getMessageTypeCode() {
    return messageTypeCode;
  }
  
  public void setMessageTypeCode(String messageTypeCode) {
    this.messageTypeCode = messageTypeCode;
  }
  
  public static String createMessageWithParameters(String errorMessage, Object... parameters) {
    if (errorMessage == null) {
      return null;
    }
    return java.text.MessageFormat.format(errorMessage, parameters);
  }
  
  @Override
  public boolean equals(Object object) {
    if(!(object instanceof ReasonabilityTestResultMessage)) {
      return false;
    }
    
    ReasonabilityTestResultMessage other = (ReasonabilityTestResultMessage) object;
    return this.messageTypeCode.equals(other.messageTypeCode)
        && this.message.equals(other.message);
  }

  @Override
  public int hashCode() {
    return this.messageTypeCode.hashCode() + this.message.hashCode();
  }

  @Override
  public String toString() {
    return "ReasonabilityTestResultMessage [message=" + message + ", messageTypeCode=" + messageTypeCode + "]";
  }
}