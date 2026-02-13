package ca.bc.gov.srm.farm.util;

import static ca.bc.gov.srm.farm.log.LoggingUtils.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.codes.CodeTables;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.service.ListService;
import ca.bc.gov.srm.farm.service.ServiceFactory;

public final class CacheUtils {
  
  private static Logger logger = LoggerFactory.getLogger(CacheUtils.class);
  
  private CacheUtils() {
    // private constructor
  }
  
  public static void refreshYearConfigLists() throws ServiceException {
    logMethodStart(logger);
    
    ListService listService = ServiceFactory.getListService();
    
    listService.refreshCodeTableList(CodeTables.INVENTORY_ITEM);
    
    ServiceFactory.getConfigurationService().loadYearConfigurationParameters();
    
    logMethodEnd(logger);
  }
}