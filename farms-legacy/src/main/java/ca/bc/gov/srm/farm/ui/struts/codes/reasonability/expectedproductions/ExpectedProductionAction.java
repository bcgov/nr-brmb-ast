package ca.bc.gov.srm.farm.ui.struts.codes.reasonability.expectedproductions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.cache.CacheFactory;
import ca.bc.gov.srm.farm.cache.CacheKeys;
import ca.bc.gov.srm.farm.domain.codes.CropUnitCodes;
import ca.bc.gov.srm.farm.domain.codes.ExpectedProduction;
import ca.bc.gov.srm.farm.domain.codes.InventoryClassCodes;
import ca.bc.gov.srm.farm.service.CodesService;
import ca.bc.gov.srm.farm.service.ServiceFactory;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;

public abstract class ExpectedProductionAction extends SecureAction {
  private Logger logger = LoggerFactory.getLogger(getClass());

  /**
   * @param form form
   */
  protected void syncFilterContextWithForm(ExpectedProductionsForm form) {
    String cacheKey = CacheKeys.EXPECTED_PRODUCTION_ITEM_CODE_FILTER_CONTEXT;
    ExpectedProductionsFilterContext filterContext =
      (ExpectedProductionsFilterContext) CacheFactory.getUserCache().getItem(cacheKey);

    if(filterContext == null) {
      // if context does not exist, create it
      logger.debug("Expected Productions filter context does not exist. Creating...");
      filterContext = new ExpectedProductionsFilterContext();
      CacheFactory.getUserCache().addItem(cacheKey, filterContext);
    }
    
    if(form.isSetFilterContext()) {
      filterContext.setInventoryCodeFilter(form.getInventoryCodeFilter());
      filterContext.setInventoryDescFilter(form.getInventoryDescFilter());
      filterContext.setCropUnitFilter(form.getCropUnitFilter());
    } else {
      form.setInventoryCodeFilter(filterContext.getInventoryCodeFilter());
      form.setInventoryDescFilter(filterContext.getInventoryDescFilter());
      form.setCropUnitFilter(filterContext.getCropUnitFilter());
    }
  }
    

  protected ExpectedProduction getExpectedProductionItemFromForm(ExpectedProductionsForm form)
  throws Exception {
    
    ExpectedProduction item = new ExpectedProduction();
    item.setId(form.getExpectedProductionId());
    
    try {
      item.setExpectedProductionPerAcre(Double.valueOf(form.getExpectedProductionPerAcre()));
    } catch (Exception e) {
      // No need to actually handle this
    }
        
    item.setInventoryItemCode(form.getInventoryItemCode());
    
    if(InventoryClassCodes.LIVESTOCK.equals(form.getInventoryType())) {
      item.setCropUnitCode(CropUnitCodes.getLivestockUnitCode(form.getInventoryItemCode()));
    } else {
      item.setCropUnitCode(form.getCropUnitCode());
    }    
    
    return item;
  }
  
  /**
   * @param form form
   * @throws Exception On Exception
   */
  protected void populateForm(ExpectedProductionsForm form) throws Exception {
    
    syncFilterContextWithForm(form);
    
    CodesService codesService = ServiceFactory.getCodesService();
    ExpectedProduction code = codesService.getExpectedProductionItem(form.getExpectedProductionId());
    
    if (code == null) {
      logger.debug("Could not find the Expected Production Item");
      return;
    }
    
    form.setExpectedProductionPerAcre(code.getExpectedProductionPerAcre().toString());   
    form.setInventoryItemCode(code.getInventoryItemCode());
    form.setInventoryItemCodeDescription(code.getInventoryItemCodeDescription());
    form.setCropUnitCode(code.getCropUnitCode());
    form.setCropUnitCodeDescription(code.getCropUnitCodeDescription());
    form.setMunicipalityCodeDescription(code.getMunicipalityCodeDescription());
  }  
}
