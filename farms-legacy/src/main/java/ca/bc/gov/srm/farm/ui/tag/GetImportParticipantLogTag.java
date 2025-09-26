/**
 * $RCSfile$
 * Copyright (c) 2009, B.C. Ministry of Agriculture and Lands.
 * All rights reserved.
 */
package ca.bc.gov.srm.farm.ui.tag;

import java.util.Iterator;

import ca.bc.gov.srm.farm.cache.Cache;
import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.ui.domain.resultsimport.IMPORTLOG;
import ca.bc.gov.srm.farm.ui.domain.resultsimport.PARTICIPANTType;

import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;


/**
 * GetImportParticipantLogTag.
 */
public class GetImportParticipantLogTag extends VarTagSupport {

  /** serialVersionUID. */
  private static final long serialVersionUID = 2261821241481744643L;

  private String pin;

  /** Creates a new GetImportParticipantLogTag object. */
  public GetImportParticipantLogTag() {
    super();
  }
  
  
  /**
	 * @return the pin
	 */
	public String getPin() {
		return pin;
	}

	/**
	 * @param pin the pin to set
	 */
	public void setPin(String pin) {
		this.pin = pin;
	}



	/**
   * doStartTag.
   *
   * @return  The return value.
   *
   * @throws  JspException  On exception.
   */
  @Override
  public int doStartTag() throws JspException {
    String varName = EvalHelper.evalString("var", getVar(), this, pageContext);

    if (StringUtils.isBlank(varName)) {
      throw new JspException("var must be non-blank");
    }
    
    if (StringUtils.isBlank(pin)) {
      throw new JspException("pin must be non-blank");
    }

    Cache cache = CacheFactory.getUserCache();
    IMPORTLOG log = (IMPORTLOG) cache.getItem(CacheKeys.IMPORT_LOG);
    
    PARTICIPANTType foundParticipant = null;
    Iterator iter = log.getPARTICIPANTS().getPARTICIPANT().iterator();
    String pinToLookFor = EvalHelper.evalString("pin", pin, this, pageContext);
    
	  while(iter.hasNext()) {
	  	PARTICIPANTType logObj = (PARTICIPANTType) iter.next();
	  	String currentPin = logObj.getPin().toString();
	  	
	  	if(pinToLookFor.equals(currentPin)) {
	  		foundParticipant = logObj;
	  		break;
	  	}
	  }
	  
	  store(foundParticipant);

    return SKIP_BODY;
  }

}
