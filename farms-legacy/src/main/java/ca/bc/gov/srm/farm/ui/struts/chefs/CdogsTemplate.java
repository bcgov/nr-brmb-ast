package ca.bc.gov.srm.farm.ui.struts.chefs;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cdogs.CdogsRestApiDao;
import ca.bc.gov.srm.farm.exception.ServiceException;

public class CdogsTemplate {

  private Logger logger = LoggerFactory.getLogger(getClass());
  private String description;
  private String templateGuid;
  private boolean isCached;
  
  public CdogsTemplate(String description, String templateGuid) {
    this.description = description;
    this.templateGuid = templateGuid;
    
    CdogsRestApiDao cdogsDao = new CdogsRestApiDao();
    String response = null;
    try {
      response = cdogsDao.checkTemplateCache(templateGuid);
    } catch (IOException | ServiceException e) {
      logger.error("Error checking CDOGS template", e);
    }
    isCached = "OK".equals(response);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getTemplateGuid() {
    return templateGuid;
  }

  public void setTemplateGuid(String templateGuid) {
    this.templateGuid = templateGuid;
  }

  public boolean getIsCached() {
    return isCached;
  }

  public void setCached(boolean isCached) {
    this.isCached = isCached;
  }

  @Override
  public String toString() {
    return "CdogsTemplate [logger=" + logger + ", description=" + description + ", templateGuid=" + templateGuid + ", isCached=" + isCached + "]";
  }

}
