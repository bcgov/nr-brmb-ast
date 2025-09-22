package ca.bc.gov.srm.farm.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Iterator;

import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.slf4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.factory.ObjectFactory;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.transaction.TransactionProvider;
import ca.bc.gov.srm.farm.webade.WebADERequest;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.WebADEApplicationUtils;
import ca.bc.gov.webade.WebADEException;

/**
 * common test code
 */
public final class TestUtils {
	
  private static Application app;
  
	/** empty private constructor. */
	private TestUtils() {
	}
        
	public static Connection openConnection() throws SQLException {
    String user = "farm";
    String server = "oradb19c.vividsolutions.com";
    String port = "1521";
    String sid = "dev";
    String pwd = "password";

    Connection con = null;
    String driver = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@" + server + ":" + port + ":" + sid;

    try {
      Class.forName(driver);
    } catch (java.lang.ClassNotFoundException e) {
      e.printStackTrace();
    }

    con = DriverManager.getConnection(url, user, pwd);
    con.setAutoCommit(false);

    return con;
  }

	public static Connection openQAConnection() throws SQLException {
    String user = "farm";
    String server = "oracledb.vividsolutions.com";
    String port = "1521";
    String sid = "qa";
    String pwd = "password";

    Connection con = null;
    String driver = "oracle.jdbc.driver.OracleDriver";
    String url = "jdbc:oracle:thin:@" + server + ":" + port + ":" + sid;

    try {
      Class.forName(driver);
    } catch (java.lang.ClassNotFoundException e) {
      e.printStackTrace();
    }

    con = DriverManager.getConnection(url, user, pwd);
    con.setAutoCommit(false);

    return con;
  }
	
	
  public static void closeConnection(Connection conn) {
		try {
			if (conn != null) {
	      conn.close();
	    }
		} catch(Exception ex) {
			// ignore
		    ex.toString();
		}
	}
  
  
  public static void useTestTransaction() {
    ObjectFactory.setImplementingClass(TransactionProvider.class, "ca.bc.gov.srm.farm.transaction.TestTransactionProvider");
  }
  
  public static Scenario loadScenarioFromJsonFile(String fileName) throws IOException, JsonParseException, JsonMappingException {
    String scenarioString = loadFileAsString(fileName);
    
    Scenario scenario = JsonUtils.getJsonObjectMapper().readValue(scenarioString, Scenario.class);
    return scenario;
  }

  public static String loadFileAsString(String fileName) {
    String data = null;
    try {
      data = IOUtils.loadResourceFileAsString(fileName);
    } catch (IOException | URISyntaxException e) {
      fail();
      e.printStackTrace();
    }
    return data;
  }

  public static String currencyAsString(Double value) {
    String result;
    if(value == null) {
      result = "null";
    } else {
      result = String.valueOf(MathUtils.roundCurrency(value));
    }
    return result;
  }
  
  public static void standardTestSetUp() throws ServiceException {
    try {
      loadWebADEApplication();
    } catch (WebADEException e) {
      e.printStackTrace();
      throw new ServiceException("Error loading WebADEApplication", e);
    }
    
    useTestTransaction();
    overrideServices();
    setBusinessActionToSystem();
  }
  
  public static void overrideServices() throws ServiceException {
    
    // this beats changing the implementation.properties file
    String overrideResourcePath = "implementation_override.properties";
    ServiceFactory.getInstance().setOverrideResourcePath(overrideResourcePath);
    ObjectFactory.getInstance().setOverrideResourcePath(overrideResourcePath);
    
    ServiceFactory.getConfigurationService().loadConfigurationParameters();
    ServiceFactory.getConfigurationService().loadYearConfigurationParameters();
    ServiceFactory.getListService().loadLists();
  }
  
  public static Application loadWebADEApplication() throws WebADEException {
    if(app == null) {
      app = WebADEApplicationUtils.createApplication("FARM");
      WebADERequest.getInstance().setApplication(app);
    }
    return app;
  }

  public static void setBusinessActionToSystem() {
    CacheFactory.getRequestCache().addItem(CacheKeys.CURRENT_BUSINESS_ACTION, BusinessAction.system());
  }
  
  public static void logMessages(Logger logger, ActionMessages messages) {
    StackTraceElement stackTraceElement = Thread.currentThread().getStackTrace()[2];
    logger.debug("ActionMessages in test " + stackTraceElement.getMethodName() + ":" + " at line: " + stackTraceElement.getLineNumber());
    logger.debug(String.format("Message count: %d", messages.size()));
    
    if(messages.size() > 0) {
      
      for(@SuppressWarnings("unchecked") Iterator<ActionMessage> mi = messages.get(); mi.hasNext(); ) {
        ActionMessage msg = mi.next();
        logger.debug("Message: [" + msg.getKey() + "], values: " + Arrays.toString(msg.getValues()));
      }
    }
  }
}
