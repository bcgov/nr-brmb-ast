package ca.bc.gov.srm.farm.ui.struts.codes.reasonability;

import java.text.NumberFormat;

import ca.bc.gov.srm.farm.domain.codes.FruitVegTypeCode;
import ca.bc.gov.srm.farm.ui.struts.SecureAction;


public abstract class FruitVegCodeAction extends SecureAction {
  
  /**
   * @param form form
   * @return InventoryItemCode
   * @throws Exception On Exception
   */
  protected FruitVegTypeCode getFruitVegCodeFromForm(FruitVegCodeForm form)
  throws Exception {

    FruitVegTypeCode code = new FruitVegTypeCode();
    code.setName(form.getName());
    code.setDescription(form.getDescription());
    code.setVarianceLimit(NumberFormat.getInstance().parse(form.getVarianceLimit()).doubleValue());
    return code;
  } 
}