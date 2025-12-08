package ca.bc.gov.srm.farm.agent;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.security.BusinessAction;
import ca.bc.gov.srm.farm.security.SecurityRule;
import ca.bc.gov.srm.farm.security.SecurityUtility;
import ca.bc.gov.srm.farm.service.ImportService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.webade.Action;
import ca.bc.gov.webade.Application;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;

import java.util.Date;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.timer.Timer;


/**
 * Object called by a JMX timer to do scheduled imports.
 */
public final class ImportAgent implements NotificationListener {

  private Logger logger = LoggerFactory.getLogger(getClass());

  private Application application;

  private Timer timer = new Timer();

  private static ImportAgent instance = new ImportAgent();

  /** constructor. */
  private ImportAgent() {
  }

  /**
   * Use a static instance so that there is always an listener available for the
   * timer.
   *
   * @return  ImportAgent
   */
  public static ImportAgent getInstance() {
    return instance;
  }

  /**
   * @return  Timer
   */
  public Timer getTimer() {
    return timer;
  }


  /**
   * @param   app  app
   *
   * @throws  Exception  on exception
   */
  public void initialize(Application app) throws Exception {
    logger.debug("> initialize");

    application = app;
    checkForInProgessImports();
    startTimer();

    logger.debug("< initialize");
  }
  
  
  
  /**
   *
   * @throws  Exception  on exception
   */
  public void shutdown() throws Exception {
    logger.info("> shutdown");

    stopTimer();

    logger.info("< shutdown");
  }


  /**
   * Note that the timer waits for this function to finish before it schedules
   * the next notification.
   *
   * @param  notification  notification
   * @param  data          data
   */
  @Override
  public void handleNotification(Notification notification, Object data) {
    logger.debug("> handleNotification: ");

    checkForScheduledImport();

    logger.debug("< handleNotification");
  }


  /** See if there is an scheduled import job to process. */
  private void checkForScheduledImport() {
    logger.debug("> checkForScheduledImport");

    ImportService service = ServiceFactory.getImportService();

    try(Connection connection = openConnection();) {
      try {
        service.checkForScheduledImport(connection);
        connection.commit();
      } catch (Exception ex) {
        rollbackConnection(connection);
      }
    } catch (Exception e) {
    	if(isDuringBusinessHours()) {
        e.printStackTrace();
        logger.error("Unexpected error: ", e);
      } else {
      	//
      	// The infrastructure personnel didn't want to see error messages 
      	// generated if the database was down for backups, so only log
      	// at the ERROR level if this happens between 7 AM and 7 PM
      	//
      	logger.info("Unexpected error: ", e);
      }
    }

    logger.debug("< checkForScheduledImport");
  }


  /** Upon startup look for imports that are stuck in an "In Progress" state. */
  private void checkForInProgessImports() {
    logger.debug("> checkForInProgessImports");

    ImportService service = ServiceFactory.getImportService();

    try(Connection connection = openConnection();) {
      try {
        service.checkForInProgessImports(connection);
        connection.commit();
      } catch (Exception ex) {
        rollbackConnection(connection);
      }
    } catch (Exception e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
    }

    logger.debug("< checkForInProgessImports");
  }


  /**
   * Use a timer to call handleNotification.
   *
   * <p>Note that the timer uses a delayed notification, so it will wait for
   * handleNotification to finish before the next invocation is scheduled.</p>
   *
   * @throws  Exception  on exception
   */
  private void startTimer() throws Exception {
    logger.debug("> startTimer");

    //
    // OAS forces us to use the OAS application name as the domain. It
    // is case-sensitive.
    //
    MBeanServer server = MBeanServerFactory.createMBeanServer();
    final String domain = server.getDefaultDomain();

    logger.debug("server.getDefaultDomain(): " + domain);

    final long freq = getTimerFrequency();
    ObjectName timerName = new ObjectName(domain + ":type=Timer");
    Date startTime = new Date(System.currentTimeMillis() + freq);
    timer.addNotification("import", "Import", null, startTime, freq);

    if (server.isRegistered(timerName)) {
      //
      // This only seems to happen with OAS
      //
      logger.debug("unregistering timer ");
      server.unregisterMBean(timerName);
    }

    server.registerMBean(timer, timerName);
    server.addNotificationListener(timerName, this, null, null);
    timer.start();

    logger.debug("< startTimer");
  }
  
  
  /**
   * There seems to be an issue with OAS that it takes a long
   * time to restart the webapp because the JMX timer is 
   * hanging around.
   * 
   * @throws  Exception  on exception
   */
  private void stopTimer() throws Exception {
  	timer.stop();
  	
  	// unregister the timer if it exists
  	MBeanServer server = MBeanServerFactory.createMBeanServer();
  	final String domain = server.getDefaultDomain();
  	ObjectName timerName = new ObjectName(domain + ":type=Timer");
  	
  	if (server.isRegistered(timerName)) {
  	  server.unregisterMBean(timerName);
  	}
  }


  /**
   * @return  frequency in milliseconds to run the timer
   */
  private long getTimerFrequency() {
    String timerKey = ConfigurationKeys.IMPORT_TIMER_FREQ;
    String timerValue = ConfigurationUtility.getInstance().getValue(timerKey);
    int freqInSeconds = Integer.parseInt(timerValue);

    return freqInSeconds * Timer.ONE_SECOND;
  }


  /**
   * @return  Webade Connection
   *
   * @throws  Exception  on exception
   */
  private Connection openConnection() throws Exception {
    BusinessAction ba = BusinessAction.system();
    SecurityRule rule = SecurityUtility.getInstance().getSecurityRule(ba);
    Action action = new Action(rule.getRuleName());
    Connection conn = application.getConnectionByPriviledgedAction(action);

    conn.setAutoCommit(false);

    return conn;
  }


  /**
   * @param  connection  connection
   */
  private void rollbackConnection(Connection connection) {
    if (connection != null) {
      try {
        connection.rollback();
      } catch (Exception e) {
        logger.error("Unexpected error: ", e);
      }
    }
  }

  
  /**
   * @return if now is between 7 AM and 7 PM
   */
  private static boolean isDuringBusinessHours() {
  	ConfigurationUtility cu = ConfigurationUtility.getInstance();
    String startTimeStr = cu.getValue(ConfigurationKeys.BUSINESS_START_TIME);
    String endTimeStr = cu.getValue(ConfigurationKeys.BUSINESS_START_TIME);
    
    Date now = new Date();
    Date startTime = DateUtils.setTime(now, startTimeStr);
    Date stopTime = DateUtils.setTime(now, endTimeStr);
    
    boolean inWindow = DateUtils.isBetween(now, startTime, stopTime);
  	
  	return inWindow;
  }
  
}
