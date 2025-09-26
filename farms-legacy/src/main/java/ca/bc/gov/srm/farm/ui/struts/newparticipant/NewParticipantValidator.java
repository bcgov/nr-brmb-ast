/**
 * Copyright (c) 2022,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.newparticipant;

import static ca.bc.gov.srm.farm.ui.struts.message.MessageConstants.*;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;

/**
 * @author awilkinson
 */
public class NewParticipantValidator {

  private ActionMessages errors = new ActionMessages();
  
  private NewParticipantValidator() {
    // private
  }
  
  public static final NewParticipantValidator getInstance() {
    return new NewParticipantValidator();
  }

  public ActionMessages validate(NewParticipantFormData participant) throws ServiceException {

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();
    
    String pinString = participant.getParticipantPin();
    Integer pin = Integer.parseInt(pinString);
    
    boolean pinExists = calculatorService.pinExists(pin);
    if(pinExists) {
      addError(ERRORS_NEW_PARTICIPANT_PIN_EXISTS, pinString);
    }

    return errors;
  }

  
  private void addError(String messageKey, Object... values) {
    errors.add("", new ActionMessage(messageKey, values));
  }

}
