/**
 * Copyright (c) 2024,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.participant;

import static ca.bc.gov.srm.farm.ui.struts.message.MessageConstants.*;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.CalculatorService;
import ca.bc.gov.srm.farm.service.ServiceFactory;

public class ParticipantValidator {

  private ActionMessages errors = new ActionMessages();

  private ParticipantValidator() {
    // private
  }

  public static final ParticipantValidator getInstance() {
    return new ParticipantValidator();
  }

  public ActionMessages validate(Integer participantPin) throws ServiceException {

    CalculatorService calculatorService = ServiceFactory.getCalculatorService();

    boolean pinExists = calculatorService.pinExists(participantPin);
    if (pinExists) {
      addError(ERRORS_NEW_PARTICIPANT_PIN_EXISTS, participantPin.toString());
    }

    return errors;
  }

  private void addError(String messageKey, Object... values) {
    errors.add("", new ActionMessage(messageKey, values));
  }

}
