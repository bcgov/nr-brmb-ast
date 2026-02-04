package ca.bc.gov.srm.farm.ui.struts.codes.tips;

import java.util.Date;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.domain.codes.FarmType3;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;
import ca.bc.gov.srm.farm.util.DataParseUtils;
import ca.bc.gov.srm.farm.util.DateUtils;
import ca.bc.gov.srm.farm.util.JsonUtils;

public abstract class TipsFarmType3Action extends SecureAction {
  protected static ObjectMapper jsonObjectMapper = JsonUtils.getJsonObjectMapper();
  
  /**
   * @param form form
   * @return InventoryItemCode
   * @throws Exception On Exception
   */
  protected FarmType3 getFarmTypeCodeFromForm(TipsFarmType3Form form)
  throws Exception {

	FarmType3 code = new FarmType3(form.getFarmTypeName());
	code.setFarmTypeId(form.getId());
    code.setEstablishedDate(new Date());
    code.setExpiryDate(DataParseUtils.parseDate(DateUtils.NEVER_EXPIRES_DATE_STRING));
    code.setRevisionCount(form.getRevisionCount());
    
    return code;
  }
}
