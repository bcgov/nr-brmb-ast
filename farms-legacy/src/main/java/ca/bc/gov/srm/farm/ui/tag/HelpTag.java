/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Government
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import javax.servlet.jsp.JspException;

import org.apache.commons.lang.StringUtils;

import ca.bc.gov.srm.farm.configuration.ConfigurationKeys;
import ca.bc.gov.srm.farm.configuration.ConfigurationUtility;


/**
 * HelpTag.
 */
public class HelpTag extends TextTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 6135131369226798445L;
  
  /** DOCUMENT ME! */
  private String screenNumber;

  /** Creates a new HelpTag object. */
  public HelpTag() {
    super();
  }

  /**
	 * @return the screenNumber
	 */
	public String getScreenNumber() {
		return screenNumber;
	}

	/**
	 * @param screenNumber the screenNumber to set
	 */
	public void setScreenNumber(String screenNumber) {
		this.screenNumber = screenNumber;
	}



	/**
   * 
   *
   * @return  The return value.
   * @throws  JspException  On exception.
   */
  @Override
  public String text() throws JspException {
  	if (StringUtils.isBlank(getScreenNumber())) {
      throw new JspException("The screenNumber attribute must be specified");
    }
  	
    ConfigurationUtility config = ConfigurationUtility.getInstance();
    
    String urlBase = config.getValue(ConfigurationKeys.HELP_URL);
    String prefix = config.getValue(ConfigurationKeys.HELP_FILE_PREFIX);
    String suffix = config.getValue(ConfigurationKeys.HELP_FILE_SUFFIX);
    
    if(!urlBase.endsWith("/")) {
    	urlBase += "/";
    }
    
    if(!suffix.startsWith(".")) {
    	suffix = "." + suffix;
    }
    
    String url = urlBase + prefix + getScreenNumber() + suffix; 

    return url;
  }

}
