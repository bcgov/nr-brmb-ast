/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.j2ee;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.agent.ChefsAgent;
import ca.bc.gov.srm.farm.agent.BenefitTriageAgent;
import ca.bc.gov.srm.farm.agent.ImportAgent;
import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;
import ca.bc.gov.srm.farm.io.FileUtility;
import ca.bc.gov.srm.farm.message.MessageUtility;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.webade.WebADERequest;
import ca.bc.gov.webade.Application;
import ca.bc.gov.webade.http.HttpRequestUtils;


/**
 * DataServiceContextListener.
 *
 * @author awilkinson
 */
public class DataServiceContextListener implements ServletContextListener {

  /** log. */
  private Logger logger = LoggerFactory.getLogger(getClass());

  
  /**
   * contextDestroyed.
   *
   * @param  sce  The parameter value.
   */
  @Override
  public void contextDestroyed(final ServletContextEvent sce) {
  	try {
  	  ImportAgent.getInstance().shutdown();
  	  
      boolean chefsAgentEnabled = isChefsAgentEnabled();
      if(chefsAgentEnabled) {
        ChefsAgent.getInstance().shutdown();
      }
      
      boolean benefitTriageAgentEnabled = isBenefitTriageAgentEnabled();
      if(benefitTriageAgentEnabled) {
        BenefitTriageAgent.getInstance().shutdown();
      }
      
  	} catch (Throwable e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
    }
  }

  /**
   * contextInitialized.
   *
   * @param  sce  The parameter value.
   */
  @Override
  public void contextInitialized(final ServletContextEvent sce) {
    logger.debug("> contextInitialized()");

    ServletContext context = sce.getServletContext();
    Application app = HttpRequestUtils.getApplication(context);

    //we need to initialize this so that webade implementations
    //of utilities and providers can get at webade application.
    logger.debug("set webade request instance...");

    WebADERequest.getInstance().setApplication(app);

    //let's load up the message resources
    MessageUtility.getInstance();

    //add the server level cache (store data in servlet context).
    CacheFactory.createApplicationCache(context);
    
    try {
    	
      // load all static lists...
      ServiceFactory.getListService().loadLists();
      ServiceFactory.getConfigurationService().loadConfigurationParameters();
      ServiceFactory.getConfigurationService().loadYearConfigurationParameters();

      // and make the list service available to the UI
      context.setAttribute("listService", ServiceFactory.getListService());

      // this will clean out our temp file location
      FileUtility.getInstance().cleanDirectory();
      
      boolean importAgentEnabled = isImportAgentEnabled();
      boolean chefsAgentEnabled = isChefsAgentEnabled();
      boolean benefitTriageAgentEnabled = isBenefitTriageAgentEnabled();
      
      if(importAgentEnabled) {
        ImportAgent.getInstance().initialize(app);
      }
      
      if(chefsAgentEnabled) {
        ChefsAgent.getInstance().initialize(app);
      }

      if(benefitTriageAgentEnabled) {
        BenefitTriageAgent.getInstance().initialize(app);
      }

    } catch (Throwable e) {
      e.printStackTrace();
      logger.error("Unexpected error: ", e);
      throw new RuntimeException(e);
    }

    logger.debug("< contextInitialized()");
  }


  private boolean isImportAgentEnabled() {
    String importAgentEnabledInd = System.getProperty("import.agent.enabled");
    boolean importAgentEnabled = ! "N".equals(importAgentEnabledInd);
    return importAgentEnabled;
  }


  private boolean isChefsAgentEnabled() {
    ConfigurationUtility configUtil = ConfigurationUtility.getInstance();
    boolean chefsAgentEnabled = configUtil.getBoolean(ConfigurationKeys.CHEFS_AGENT_ENABLED);
    return chefsAgentEnabled;
  }


  private boolean isBenefitTriageAgentEnabled() {
    ConfigurationUtility configUtil = ConfigurationUtility.getInstance();
    boolean chefsAgentEnabled = configUtil.getBoolean(ConfigurationKeys.BENEFIT_TRIAGE_AGENT_ENABLED);
    return chefsAgentEnabled;
  }
}
