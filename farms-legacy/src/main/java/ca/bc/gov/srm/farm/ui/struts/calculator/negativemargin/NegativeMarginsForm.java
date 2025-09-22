/**
 * Copyright (c) 2025,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.negativemargin;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.ui.struts.calculator.search.CalculatorSearchForm;

/**
 * @author hwang
 */
public class NegativeMarginsForm extends CalculatorSearchForm {

  private static final long serialVersionUID = 1L;
  
  private Map<String, NegativeMarginFormLine> negativeMargins = new HashMap<>();
  private List<String> inventoryItemCodes = new ArrayList<>();
  
  private BigDecimal payablePercent;
  private BigDecimal partnershipPercent;
  
  private BigDecimal subtotal;
  private BigDecimal totalPayable;

  public Map<String, NegativeMarginFormLine> getNegativeMargins() {
    return negativeMargins;
  }

  public void setNegativeMargins(Map<String, NegativeMarginFormLine> negativeMargins) {
    this.negativeMargins = negativeMargins;
  }

  public List<String> getInventoryItemCodes() {
    return inventoryItemCodes;
  }

  public void setInventoryItemCodes(List<String> inventoryItemCodes) {
    this.inventoryItemCodes = inventoryItemCodes;
  }

  public NegativeMarginFormLine getNegativeMargin(String inventoryItemCode) {
    NegativeMarginFormLine line = negativeMargins.get(inventoryItemCode);
    if (line == null) {
      line = new NegativeMarginFormLine();
      negativeMargins.put(inventoryItemCode, line);
    }
    return line;
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }
  
  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public BigDecimal getTotalForThisPartner() {
    BigDecimal result = null;
    if(getSubtotal() != null) {
      result = getSubtotal().multiply(partnershipPercent);
    }
    return result;
  }

  public BigDecimal getPayablePercent() {
    return payablePercent;
  }

  public void setPayablePercent(BigDecimal payablePercent) {
    this.payablePercent = payablePercent;
  }

  public BigDecimal getPartnershipPercent() {
    return partnershipPercent;
  }

  public void setPartnershipPercent(BigDecimal partnershipPercent) {
    this.partnershipPercent = partnershipPercent;
  }

  public BigDecimal getTotalPayable() {
    return totalPayable;
  }

  public void setTotalPayable(BigDecimal totalPayable) {
    this.totalPayable = totalPayable;
  }

  @Override
  public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
    ActionErrors errors = super.validate(mapping, request);

    for (NegativeMarginFormLine line : negativeMargins.values()) {
      errors.add(line.validate(mapping, request));
    }

    return errors;
  }
}
