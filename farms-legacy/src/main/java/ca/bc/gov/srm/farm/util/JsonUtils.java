package ca.bc.gov.srm.farm.util;

import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * Used to marshal and unmarshal objects to the JSON format.
 */
public final class JsonUtils {
  
  private static ObjectMapper jsonObjectMapper = new ObjectMapper();
  
  public static ObjectMapper getJsonObjectMapper() {
    return jsonObjectMapper;
  }


  public static void logObjectAsJsonAtDebug(Logger logger, Object object, String jsonObjectName) {
    if(logger.isDebugEnabled()) {
      
      try {
        String json = jsonObjectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
        logger.debug(jsonObjectName + " JSON: \n" + json);
      } catch (JsonProcessingException ex) {
        logger.debug("Error converting " + jsonObjectName + " to JSON for debug logging: ", ex);
      }
    }
  }

}
