/**
 *
 * Copyright (c) 2011,
 * Government of British Columbia,
 * Canada
 *
 * All rights reserved.
 * This information contained herein may not be used in whole or in part
 * without the express written consent of the Government of British
 * Columbia, Canada.
 */
package ca.bc.gov.srm.farm.ui.struts.calculator.accruals;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.IOUtils;

/**
 * @author mmichaelis
 * @created June 24, 2014
 */
public class AccrualsOutputCsvAction extends AccrualsViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  final String INPUT_RADIO_TYPE = "input";
  final String RECEIVABLE_RADIO_TYPE = "receivable";
  final String PAYABLE_RADIO_TYPE = "payable";
  final String ADJUSTED_FINANCIAL_VIEW = "adjusted";
  final String ADJUSTMENTS_FINANCIAL_VIEW = "adjustments";
  final String CRA_FINANCIAL_VIEW = "cra";

  /**
   * @param mapping mapping
   * @param actionForm actionForm
   * @param request request
   * @param response response
   * @return The ActionForward
   * @throws Exception On Exception
   */
  @Override
  protected ActionForward doExecute(
      final ActionMapping mapping,
      final ActionForm actionForm,
      final HttpServletRequest request,
      final HttpServletResponse response) throws Exception {

    logger.debug("Output Accruals Csv...");

    File outFile = null;
    final String OUTPUT_CSV_FILE = "farm_export_Accruals_";
    final String CSV_SUFFIX = ".csv";
    String fileName = OUTPUT_CSV_FILE;
    String fileNameSuffix = CSV_SUFFIX;

    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    AccrualsForm form = (AccrualsForm) actionForm;
    Scenario scenario = getScenario(form);

    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario, true);

    String financialView = form.getFinancialViewRadio();
    String selectedItemType = form.getItemTypeRadio();

    StringBuilder headerLine = new StringBuilder(getMetadata(form));

    List<String> outputLines = new ArrayList<>();
    outputLines.add(getFirstLine(financialView));
    
    Map<String, AccrualFormData> items = form.getItems();
    if (items != null) {
      List<String> lineKeys = form.getLineKeys();

      String selectedItemTypeName = "";
      if (INPUT_RADIO_TYPE.equals(selectedItemType)) {
        selectedItemTypeName = AccrualFormData.getInputType();
      } else if (RECEIVABLE_RADIO_TYPE.equals(selectedItemType)) {
        selectedItemTypeName = AccrualFormData.getReceivableType();
      } else if (PAYABLE_RADIO_TYPE.equals(selectedItemType)) {
        selectedItemTypeName = AccrualFormData.getPayableType();
      }

      for (String itemKey: lineKeys) {
        AccrualFormData aFormData = items.get(itemKey);

        if (aFormData != null) {	
          String lineItemtype = aFormData.getItemType();

          if (selectedItemTypeName.equals(lineItemtype)) {
            String line = getInputLineItem(aFormData, financialView);
            outputLines.add(line.toString());
          }
        }
      }

      if (ADJUSTED_FINANCIAL_VIEW.equals(financialView)) {
        Map<String, Double> totalValues = form.getTotalValues();
        if (selectedItemTypeName.equals(AccrualFormData.getInputType())) {
          outputLines.add(",,,Total Change in Value of Purchased Inputs," + currencyOutputFormat.format(totalValues.get(AccrualFormData.getInputType())));
        } else if (selectedItemTypeName.equals(AccrualFormData.getReceivableType())) {
          outputLines.add(",,,Total Change in Value of Receivables," + currencyOutputFormat.format(totalValues.get(AccrualFormData.getReceivableType())));
        } else {
          outputLines.add(",,,Total Change in Value of Payables," + currencyOutputFormat.format(totalValues.get(AccrualFormData.getPayableType())));
        }
      }

    } else {
      logger.debug("NO items!");
    }

    try {  
      outFile = IOUtils.writeTempFile(fileName, fileNameSuffix, headerLine.toString(), outputLines);  
    } catch (IOException e) {
      throw new ServiceException("", e);
    }

    IOUtils.writeFileToResponse(response, outFile, IOUtils.CONTENT_TYPE_CSV);
    outFile.delete();

    return forward;
  }


  private String getFirstLine(String financialView) {
    logger.debug("getFirstLine");

    StringBuilder line = new StringBuilder();

    if (ADJUSTED_FINANCIAL_VIEW.equals(financialView)) {
      line.append("Code,Description,Start Value,End Value,Change in Value");
    } else if ((ADJUSTMENTS_FINANCIAL_VIEW.equals(financialView)) || 
        (CRA_FINANCIAL_VIEW.equals(financialView))) {
      line.append("Code,Description,Start Value,End Value");
    } else {
      logger.error("Unexpected Financial View: " + financialView);
      throw new IllegalArgumentException("Financial View: " + financialView);
    }
    return line.toString();
  }


  private String getInputLineItem(AccrualFormData aFormData, String financialView) {
    logger.debug("getInputLineItem");

    StringBuilder line = new StringBuilder();  		

    line.append(aFormData.getLineCode());
    line.append(",\"").append(aFormData.getLineCodeDescription()).append("\"");

    if (ADJUSTED_FINANCIAL_VIEW.equals(financialView)) {
      line.append(",").append(formatCurrency(aFormData.getTotalStartOfYearAmount()));
      line.append(",").append(formatCurrency(aFormData.getTotalEndOfYearAmount()));
      line.append(",").append(currencyOutputFormat.format(aFormData.getChangeInValue()));
    } else if (ADJUSTMENTS_FINANCIAL_VIEW.equals(financialView)) {
      line.append(",").append(formatCurrency(aFormData.getAdjStartOfYearAmount()));
      line.append(",").append(formatCurrency(aFormData.getAdjEndOfYearAmount()));
    } else if (CRA_FINANCIAL_VIEW.equals(financialView)) {
      line.append(",").append(formatCurrency(aFormData.getReportedStartOfYearAmount()));
      line.append(",").append(formatCurrency(aFormData.getReportedEndOfYearAmount()));
    } else {
      logger.error("Unexpected Financial View: " + financialView);
      throw new IllegalArgumentException("Financial View: " + financialView);
    }
    return line.toString();
  }

  
  private String getMetadata (AccrualsForm form) {
    logger.debug("getMetadata");
    
    StringBuilder line = new StringBuilder();
    line.append("PIN:,").append(form.getPin()).append("\n");
    line.append("Program Year:,").append(form.getYear()).append("\n");
    line.append("Scenario Number:,").append(form.getScenarioNumber()).append("\n");
    line.append("Selected Year:,").append(form.getFarmViewYear()).append("\n");

    String farmViewValue = form.getFarmView();
    String farmViewLabel = new String();
    if (farmViewValue != null) {
      for (ListView farmViewEntry: form.getFarmViewOptions()) {
        if (farmViewValue.equals(farmViewEntry.getValue())) {
          farmViewLabel = farmViewEntry.getLabel();
          break;
        }
      }
    }
    line.append("Farm View:,").append(farmViewLabel).append("\n");

    // Capitalize first letter of item type
    String itemTypeLabel = WordUtils.capitalize(form.getItemTypeRadio());
    line.append("Item Type:,").append(itemTypeLabel).append("\n");
   
    // Capitalize first letter of financial view, or all for CRA
    String financialViewValue = form.getFinancialViewRadio();
    String financialViewLabel = new String();
    if (financialViewValue != null) {
      switch (financialViewValue) {
        case "adjusted": financialViewLabel = "Adjusted";
          break;
        case "adjustments": financialViewLabel = "Adjustments";
          break;
        case "cra": financialViewLabel = "CRA";
          break;
        default:
          break;
      }
    }

    line.append("Financial View:,").append(financialViewLabel).append("\n");
    
    line.append("Exported:,").append(new Date());
    line.append("\n");

    return line.toString();
  }

}
