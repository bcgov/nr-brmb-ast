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
package ca.bc.gov.srm.farm.ui.struts.calculator.productiveunits;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.ActionConstants;
import ca.bc.gov.srm.farm.util.IOUtils;

/**
 * @author mmichaelis
 * @created June 24, 2014
 */
public class ProductiveUnitsOutputCsvAction extends ProductiveUnitsViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

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

    logger.debug("Output Productive Units Csv...");

    File outFile = null;
    final String OUTPUT_CSV_FILE = "farm_export_Productive_Units_";
    final String CSV_SUFFIX = ".csv";
    String fileName = OUTPUT_CSV_FILE;
    String fileNameSuffix = CSV_SUFFIX;
    
    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    ProductiveUnitsForm form = (ProductiveUnitsForm) actionForm;
    Scenario scenario = getScenario(form);

    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario, true);

    List<String> requiredYears = form.getRequiredYears();

    StringBuilder headerLine = new StringBuilder(getMetadata(form));

    List<String> outputLines = new ArrayList<>();
    outputLines.add(getFirstLine(requiredYears));

    Map<String, ProductiveUnitFormLine> items = form.getItems();
    if (items != null) {
      List<String> lineKeys = form.getLineKeys();

      for (String itemKey: lineKeys) {
        ProductiveUnitFormLine item = items.get(itemKey);
        if (item != null) {	
          String csvLine = getLineItem(item, form);
          outputLines.add(csvLine.toString());
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


  private String getFirstLine(List<String> requiredYears) {
    logger.debug("getFirstLine");

    StringBuilder line = new StringBuilder();

    line.append("Code");
    line.append(",Description");

    for (String year : requiredYears) {
      line.append(",").append(year);
    }
    return line.toString();
  }


  private String getLineItem(ProductiveUnitFormLine item, ProductiveUnitsForm form) {
    logger.debug("getLineItem");

    String financialView = form.getFinancialViewRadio();
    String unitsView = form.getUnitsViewRadio();
    String dataSetView = form.getViewDataSetRadio();
    Map<String, List<String>> participantDataSrcCodesByYear = form.getParticipantDataSrcCodesByYear();
    List<String> requiredYears = form.getRequiredYears();
    
    Map<String, String> values = new HashMap<>();
    StringBuilder line = new StringBuilder();  		

    line.append(item.getLineCode());
    line.append(",\"").append(item.getLineCodeDescription()).append("\"");
    
    for (String year : requiredYears) {
      String participantDataSrcCode;
      if(year.equals(String.valueOf(form.getYear()))) { // if it is the program year
        participantDataSrcCode = dataSetView;
      } else {
        Map<String, List<String>> participantDataSrcCodes = participantDataSrcCodesByYear;
        if(participantDataSrcCodes.get(year).size() == 1) {
          participantDataSrcCode = participantDataSrcCodes.get(year).get(0);
        } else {
          String message = String.format("Expecting only one participant data source code for a reference year. For %s found: %s",
              year, participantDataSrcCodes.toString());
          logger.error(message);
          throw new IllegalArgumentException(message);
        }
      }
      
      ProductiveUnitFormRecord record = item.getRecord(participantDataSrcCode);
      
      if (ProductiveUnitsForm.UNITS_VIEW_ROLLUP.equals(unitsView)) {
        if (ProductiveUnitsForm.FINANCIAL_VIEW_ADJUSTED.equals(financialView)) {
          values.put(year, record.getAdjustedValues().get(year));
        } else if (ProductiveUnitsForm.FINANCIAL_VIEW_ADJUSTMENTS.equals(financialView)) {
          values.put(year, record.getAdjustmentValues().get(year));
        } else if (ProductiveUnitsForm.FINANCIAL_VIEW_CRA.equals(financialView)) {
          values.put(year, record.getCraValues().get(year));
        } else {
          logger.error("Unexpected Financial View: " + financialView);
          throw new IllegalArgumentException("Financial View: " + financialView);
        }
      } else if (ProductiveUnitsForm.UNITS_VIEW_ON_FARM.equals(unitsView)) {
        values = item.getOnFarmAcresValues();
      } else if (ProductiveUnitsForm.UNITS_VIEW_UNSEEDABLE.equals(unitsView)) {
        values = item.getUnseedableAcresValues();
      } else {
        logger.error("Unexpected Units View: " + unitsView);
        throw new IllegalArgumentException("Units View: " + unitsView);
      }
    }

    for (String year : requiredYears) {
      line.append(",");
      String value = values.get(year);
      if (value != null && !value.equals("")) {
        line.append(value);
      }
    }
    return line.toString();
  } 
  
  
  private String getMetadata (ProductiveUnitsForm form) {
    logger.debug("getMetadata");
    
    StringBuilder line = new StringBuilder();
    line.append("PIN:,").append(form.getPin()).append("\n");
    line.append("Program Year:,").append(form.getYear()).append("\n");
    line.append("Scenario Number:,").append(form.getScenarioNumber()).append("\n");

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

    String financialViewValue = form.getFinancialViewRadio();
    String financialViewLabel = new String();
    if (financialViewValue != null) {
      switch (financialViewValue) {
        case ProductiveUnitsForm.FINANCIAL_VIEW_ADJUSTED:
          financialViewLabel = "Adjusted";
          break;
        case ProductiveUnitsForm.FINANCIAL_VIEW_ADJUSTMENTS:
          financialViewLabel = "Adjustments";
          break;
        case ProductiveUnitsForm.FINANCIAL_VIEW_CRA:
          financialViewLabel = "CRA";
          break;
        default:
          break;
      }
    }
    line.append("Financial View:,").append(financialViewLabel).append("\n");
    
    String unitsViewValue = form.getUnitsViewRadio();
    String unitsViewLabel = new String();
    if (unitsViewValue != null) {
      switch (unitsViewValue) {
      case ProductiveUnitsForm.UNITS_VIEW_ROLLUP:
        unitsViewLabel = "Rollup";
        break;
      case ProductiveUnitsForm.UNITS_VIEW_ON_FARM:
        unitsViewLabel = "On Farm";
        break;
      case ProductiveUnitsForm.UNITS_VIEW_UNSEEDABLE:
        unitsViewLabel = "Unseedable";
        break;
      default:
        break;
      }
    }
    line.append("Units View:,").append(unitsViewLabel).append("\n");

    line.append("Exported:,").append(new Date());
    line.append("\n");
   
    return line.toString();
  }
}
