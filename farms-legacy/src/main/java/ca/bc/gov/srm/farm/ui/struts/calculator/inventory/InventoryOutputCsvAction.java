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
package ca.bc.gov.srm.farm.ui.struts.calculator.inventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
public class InventoryOutputCsvAction extends InventoryViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  final String CROP_RADIO_TYPE = "crop";
  final String ADJUSTED_FINANCIAL_VIEW = "adjusted";
  final String ADJUSTMENTS_FINANCIAL_VIEW = "adjustments";
  final String CRA_FINANCIAL_VIEW = "cra";
  
  static final HashMap<Boolean, Character> BOOLEAN_MAP = new HashMap<>();
  static {
    BOOLEAN_MAP.put(Boolean.TRUE, Character.valueOf('Y'));
    BOOLEAN_MAP.put(Boolean.FALSE, Character.valueOf('N'));
  }


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

    logger.debug("Output Inventory Csv...");

    File outFile = null;
    final String OUTPUT_CSV_FILE = "farm_export_Inventory_";
    final String CSV_SUFFIX = ".csv";
    String fileName = OUTPUT_CSV_FILE;
    String fileNameSuffix = CSV_SUFFIX;


    ActionForward forward = mapping.findForward(ActionConstants.SUCCESS);
    InventoryForm form = (InventoryForm) actionForm;
    Scenario scenario = getScenario(form);

    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario, true);

    String financialView = form.getFinancialViewRadio();
    String selectedItemType = form.getItemTypeRadio();

    StringBuilder headerLine = new StringBuilder(getMetadata(form));

    List<String> outputLines = new ArrayList<>();
    outputLines.add(getFirstLine(selectedItemType,financialView));
    
    Map<String, InventoryFormData> items = form.getItems();
    if (items != null) {
      List<String> lineKeys = form.getLineKeys();

      String itemTypeName = "";
      // create the item comparison string
      if (CROP_RADIO_TYPE.equals(selectedItemType)) {
        itemTypeName = InventoryFormData.getCropType();
      } else {
        itemTypeName = InventoryFormData.getLivestockType();
      }

      for (String itemKey: lineKeys) {
        InventoryFormData iFormData = items.get(itemKey);

        if (iFormData != null) {	
          if (itemTypeName.equals(iFormData.getItemType())) {
            String line = "";
            if (itemTypeName.equals(InventoryFormData.getCropType())) {
              line = getCropLineItem(iFormData,financialView);
            } else {
              line = getLiveStockLineItem(iFormData,financialView);
            }
            outputLines.add(line.toString());
          }
        }	
      }

      if (ADJUSTED_FINANCIAL_VIEW.equals(financialView)) {
        Map<String, Double> totalValues = form.getTotalValues();

        if (itemTypeName.equals(InventoryFormData.getCropType())) {
          outputLines.add(",,,,,,,,,Total Change in Value of Crop Inventory," + currencyOutputFormat.format(totalValues.get(InventoryFormData.getCropType())));
        } else {
          outputLines.add(",,,,,,Total Change in Value of LiveStock Inventory," + currencyOutputFormat.format(totalValues.get(InventoryFormData.getLivestockType())));
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


  private String getFirstLine(String itemType, String financialView) {
    logger.debug("getFirstLine");

    StringBuilder line = new StringBuilder();

    if (ADJUSTED_FINANCIAL_VIEW.equals(financialView)) {
      if (CROP_RADIO_TYPE.equals(itemType)) {	
        line.append("Code,Description,Units,On Farm Acres,Unseedable Acres,Qty Produced,Qty Start,Price Start,Qty End,Price End,Change in Value");
      } else {
        line.append("Code,Description,Market Commodity,Qty Start,Price Start,Qty End,Price End,Change in Value");
      }
    } else if ((ADJUSTMENTS_FINANCIAL_VIEW.equals(financialView)) || 
        (CRA_FINANCIAL_VIEW.equals(financialView))) {
      if (CROP_RADIO_TYPE.equals(itemType)) {	
        line.append("Code,Description,Units,On Farm Acres,Unseedable Acres,Qty Produced,Qty Start,Price Start,Qty End,Price End");
      } else {
        line.append("Code,Description,Market Commodity,Qty Start,Price Start,Qty End,Price End");
      }
    }
    if (line.length() == 0) {
      logger.debug("No header line! itemType: " + itemType + " financialView: " + financialView);
    }
    return line.toString();
  }


  private String getCropLineItem(InventoryFormData iFormData, String financialView) {
    logger.debug("getCropLineItem");

    StringBuilder line = new StringBuilder();  		

    line.append(iFormData.getLineCode());
    line.append(",\"").append(iFormData.getLineCodeDescription()).append("\"");
    line.append(",\"").append(iFormData.getCropUnitCodeDescription()).append("\"");

    if (ADJUSTED_FINANCIAL_VIEW.equals(financialView)) {
      line.append(",").append(iFormData.getOnFarmAcres());
      line.append(",").append(iFormData.getUnseedableAcres());
      line.append(",").append(iFormData.getTotalQuantityProduced());
      line.append(",").append(iFormData.getTotalQuantityStart());
      line.append(",").append(formatCurrency(iFormData.getTotalPriceStart()));
      line.append(",").append(iFormData.getTotalQuantityEnd());
      line.append(",").append(formatCurrency(iFormData.getTotalPriceEnd()));
      line.append(",").append(currencyOutputFormat.format(iFormData.getChangeInValue()));

    } else if (ADJUSTMENTS_FINANCIAL_VIEW.equals(financialView)) {
      line.append(",,");
      line.append(",").append(iFormData.getAdjQuantityProduced());
      line.append(",").append(iFormData.getAdjQuantityStart());
      line.append(",").append(formatCurrency(iFormData.getAdjPriceStart()));
      line.append(",").append(iFormData.getAdjQuantityEnd());
      line.append(",").append(formatCurrency(iFormData.getAdjPriceEnd()));

    } else if (CRA_FINANCIAL_VIEW.equals(financialView)) {
      line.append(",").append(iFormData.getOnFarmAcres());
      line.append(",").append(iFormData.getUnseedableAcres());
      line.append(",").append(iFormData.getReportedQuantityProduced());
      line.append(",").append(iFormData.getReportedQuantityStart());
      line.append(",").append(formatCurrency(iFormData.getReportedPriceStart()));
      line.append(",").append(iFormData.getReportedQuantityEnd());
      line.append(",").append(formatCurrency(iFormData.getReportedPriceEnd()));

    } else {
      logger.error("Unexpected Financial View: " + financialView);
      throw new IllegalArgumentException("Financial View: " + financialView);
    }
    return line.toString();
  } 


  private String getLiveStockLineItem(InventoryFormData iFormData, String financialView) {
    logger.debug("getLiveStockLineItem");

    StringBuilder line = new StringBuilder();  		

    line.append(iFormData.getLineCode());
    line.append(",\"").append(iFormData.getLineCodeDescription()).append("\"");
    line.append(",\"").append(BOOLEAN_MAP.get(iFormData.getMarketCommodity())).append("\"");

    // if any element is null, adding 0 will ensure that 0 appears in the csv
    if (ADJUSTED_FINANCIAL_VIEW.equals(financialView)) {
      line.append(",").append(iFormData.getTotalQuantityStart());
      line.append(",").append(formatCurrency(iFormData.getTotalPriceStart()));
      line.append(",").append(iFormData.getTotalQuantityEnd());
      line.append(",").append(formatCurrency(iFormData.getTotalPriceEnd()));
      line.append(",").append(currencyOutputFormat.format(iFormData.getChangeInValue()));

    } else if (ADJUSTMENTS_FINANCIAL_VIEW.equals(financialView)) {
      line.append(",").append(iFormData.getAdjQuantityStart());
      line.append(",").append(formatCurrency(iFormData.getAdjPriceStart()));
      line.append(",").append(iFormData.getAdjQuantityEnd());
      line.append(",").append(formatCurrency(iFormData.getAdjPriceEnd()));

    } else if (CRA_FINANCIAL_VIEW.equals(financialView)) {
      line.append(",").append(iFormData.getReportedQuantityStart());
      line.append(",").append(formatCurrency(iFormData.getReportedPriceStart()));
      line.append(",").append(iFormData.getReportedQuantityEnd());
      line.append(",").append(formatCurrency(iFormData.getReportedPriceEnd()));

    } else {
      logger.error("Unexpected Financial View: " + financialView);
      throw new IllegalArgumentException("Financial View: " + financialView);
    }
    return line.toString();
  }
  
  
  private String getMetadata (InventoryForm form) {
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
