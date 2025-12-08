package ca.bc.gov.srm.farm.reasonability;

import java.util.List;

import org.slf4j.Logger;

/**
 * common code for reasonability unit tests
 */
public final class ReasonabilityUnitTestUtils {
	
	/** empty private constructor. */
	private ReasonabilityUnitTestUtils() {
	}
	
  public static void logMessages(Logger logger, List<ReasonabilityTestResultMessage> errorMessages) {
    if(errorMessages.size() > 0) {
      logger.error(errorMessages.get(0).getMessageTypeCode() + " messages in test " + Thread.currentThread().getStackTrace()[2].getMethodName() + ":");
      for(ReasonabilityTestResultMessage errorMessage : errorMessages) {
        logger.error( errorMessage.getMessage());
      }
    }
  }
}
