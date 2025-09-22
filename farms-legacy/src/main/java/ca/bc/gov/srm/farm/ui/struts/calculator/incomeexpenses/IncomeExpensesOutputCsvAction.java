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
package ca.bc.gov.srm.farm.ui.struts.calculator.incomeexpenses;

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

import ca.bc.gov.srm.farm.calculator.IncomeExpenseCalculator;
import ca.bc.gov.srm.farm.domain.MarginTotal;
import ca.bc.gov.srm.farm.domain.Scenario;
import ca.bc.gov.srm.farm.exception.ServiceException;
import ca.bc.gov.srm.farm.list.ListView;
import ca.bc.gov.srm.farm.ui.struts.calculator.AdjustmentGridItemData;
import ca.bc.gov.srm.farm.util.IOUtils;

/**
 * @author mmichaelis
 * @created June 24, 2014
 */
public class IncomeExpensesOutputCsvAction extends IncomeExpensesViewAction {

  private Logger logger = LoggerFactory.getLogger(getClass());

  final String INCOME_RADIO_TYPE = "income";
  final String EXPENSES_RADIO_TYPE = "expense";

  final String ELIGIBLE_RADIO_TYPE = "eligible";
  final String INELIGIBLE_RADIO_TYPE = "ineligible";

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

    logger.debug("Output Income/Expense Csv...");

    File outFile = null;
    final String OUTPUT_CSV_FILE = "farm_export_Income_and_Expenses_";
    final String CSV_SUFFIX = ".csv";
    String fileName = OUTPUT_CSV_FILE;
    String fileNameSuffix = CSV_SUFFIX;

    IncomeExpensesForm form = (IncomeExpensesForm) actionForm;
    Scenario scenario = getScenario(form);

    setReadOnlyFlag(request, form, scenario);
    populateForm(form, scenario, true);

    boolean expensesSelected = EXPENSES_RADIO_TYPE.equals(form.getIncomeExpenseRadio());
    boolean eligibleSelected = ELIGIBLE_RADIO_TYPE.equals(form.getEligibilityRadio());
    List<String> requiredYears = form.getRequiredYears();

    String headerLine = getMetadata(form);
    
    List<String> outputLines = new ArrayList<>();
    outputLines.add(getFirstLine(form.getFinancialViewRadio(), requiredYears));

    if (ADJUSTED_FINANCIAL_VIEW.equals(form.getFinancialViewRadio())) {
      outputLines.addAll(loadSummaryTable(form.getMarginTotalMap(), expensesSelected, requiredYears));
    }

    Map<String, AdjustmentGridItemData> items = form.getItems();
    if (items != null) {
      List<String> lineKeys = form.getLineKeys();

      for (String itemKey: lineKeys) {
        IncomeExpenseFormData ieFormData = (IncomeExpenseFormData)items.get(itemKey);
        boolean expense = ieFormData.getExpense().booleanValue();
        boolean eligible = ieFormData.getEligible().booleanValue();
        if ((expensesSelected == expense) && (eligibleSelected == eligible)) {

          StringBuilder line = new StringBuilder();

          line.append(ieFormData.getLineCode());
          line.append(",\"").append(ieFormData.getLineCodeDescription()).append("\"");

          String financialView = form.getFinancialViewRadio();
          Map<String, String> values = new HashMap<>();

          if (ADJUSTED_FINANCIAL_VIEW.equals(financialView)) {
            values = ieFormData.getAdjustedValues();
          } else if (ADJUSTMENTS_FINANCIAL_VIEW.equals(financialView)) {
            values = ieFormData.getAdjustmentValues();
          } else if (CRA_FINANCIAL_VIEW.equals(financialView)) {
            values = ieFormData.getCraValues();
          }

          for (String year : requiredYears) {
            line.append(",");
            line.append(formatCurrency(values.get(year)));
          }

          if (ADJUSTED_FINANCIAL_VIEW.equals(financialView)) {
            line.append(",").append(currencyOutputFormat.format(ieFormData.getAdjustedFiveYearAverage()));
          }

          outputLines.add(line.toString());
        }
      }
    } else {
      logger.debug("NO items!");
    }

    try {  
      outFile = IOUtils.writeTempFile(fileName, fileNameSuffix, headerLine, outputLines);  
    } catch (IOException e) {
      throw new ServiceException("", e);
    }

    IOUtils.writeFileToResponse(response, outFile, IOUtils.CONTENT_TYPE_CSV);
    outFile.delete();

    return null;
  }


  private String getFirstLine(String financialViewRadio, List<String> requiredYears) {
    logger.debug("getFirstLine");

    StringBuilder line = new StringBuilder();

    /* depending on financial view, headerline could be for the summary table or for
     * the line items 
     */
    if (ADJUSTED_FINANCIAL_VIEW.equals(financialViewRadio)) {
      line.append(",Description,");
      for (String year : requiredYears) {
        line.append(year).append(",");
      }
      line.append("5 Year");
    } else {
      line.append("Code,Description");
      for (String year : requiredYears) {
        line.append(",").append(year);
      }
    }
    return line.toString();
  }


  private List<String> loadSummaryTable(Map<String, MarginTotal> marginTotals, boolean viewExpense, List<String> requiredYears) {
    logger.debug("loadSummaryTable");

    List<String> summaryLines = new ArrayList<>();

    if (viewExpense) {
      StringBuilder unadjustedEligibleExpensesLine = new StringBuilder();
      StringBuilder lessYardageLine = new StringBuilder();
      StringBuilder lessContractWorkLine = new StringBuilder();
      StringBuilder totalEligibleExpensesLine = new StringBuilder();
      StringBuilder totalIneligibleExpensesLine = new StringBuilder();

      unadjustedEligibleExpensesLine.append(",Unadjusted Eligible Expenses");
      lessYardageLine.append(",Less: Yardage");
      lessContractWorkLine.append(",Less: Contract Work");
      totalEligibleExpensesLine.append(",Total Eligible Expenses");
      totalIneligibleExpensesLine.append(",Total Ineligible Expenses");

      for (String year : requiredYears) {
        MarginTotal marginTotal = marginTotals.get(year);

        loadMarginTotalForExpense (marginTotal,
            unadjustedEligibleExpensesLine,
            lessYardageLine,
            lessContractWorkLine,
            totalEligibleExpensesLine,
            totalIneligibleExpensesLine);
      }

      MarginTotal fiveYearAverage = marginTotals.get(IncomeExpenseCalculator.MARGIN_TOTAL_5_YEAR_AVERAGE_KEY);
      loadMarginTotalForExpense (fiveYearAverage,
          unadjustedEligibleExpensesLine,
          lessYardageLine,
          lessContractWorkLine,
          totalEligibleExpensesLine,
          totalIneligibleExpensesLine);

      summaryLines.add(unadjustedEligibleExpensesLine.toString());
      summaryLines.add(lessYardageLine.toString());
      summaryLines.add(lessContractWorkLine.toString());
      summaryLines.add(totalEligibleExpensesLine.toString());
      summaryLines.add(totalIneligibleExpensesLine.toString());

    } else {
      StringBuilder supplyManagedCommodities = new StringBuilder();
      StringBuilder unadjustedEligibleIncome = new StringBuilder();
      StringBuilder lessYardageLine = new StringBuilder();
      StringBuilder lessProgramPayments = new StringBuilder();
      StringBuilder totalEligibleIncome = new StringBuilder();
      StringBuilder totalIneligibleIncome = new StringBuilder();

      supplyManagedCommodities.append(",Supply Managed Commodities");
      unadjustedEligibleIncome.append(",Unadjusted Eligible Income");
      lessYardageLine.append(",Less: Yardage");
      lessProgramPayments.append(",Less: Program Payments");
      totalEligibleIncome.append(",Total Eligible Income");
      totalIneligibleIncome.append(",Total Ineligible Income");

      for (String year : requiredYears) {
        MarginTotal marginTotal = marginTotals.get(year);

        loadMarginTotalForIncome (marginTotal,
            supplyManagedCommodities,
            unadjustedEligibleIncome,
            lessYardageLine,
            lessProgramPayments,
            totalEligibleIncome,
            totalIneligibleIncome);
      }

      MarginTotal fiveYearAverage = marginTotals.get(IncomeExpenseCalculator.MARGIN_TOTAL_5_YEAR_AVERAGE_KEY);
      loadMarginTotalForIncome (fiveYearAverage,
          supplyManagedCommodities,
          unadjustedEligibleIncome,
          lessYardageLine,
          lessProgramPayments,
          totalEligibleIncome,
          totalIneligibleIncome);

      summaryLines.add(supplyManagedCommodities.toString());
      summaryLines.add(unadjustedEligibleIncome.toString());
      summaryLines.add(lessYardageLine.toString());
      summaryLines.add(lessProgramPayments.toString());
      summaryLines.add(totalEligibleIncome.toString());
      summaryLines.add(totalIneligibleIncome.toString());
    }
    summaryLines.add(""); // add a separator between the summary and the line items

    // add title line for the line items now that the summary is built
    StringBuilder itemTitleLine = new StringBuilder("Code,Description");
    for (String year : requiredYears) {
      itemTitleLine.append(",").append(year);
    }
    itemTitleLine.append(",5 Year");
    summaryLines.add(itemTitleLine.toString());

    return summaryLines;
  }


  private void loadMarginTotalForExpense (MarginTotal marginTotal,
      StringBuilder unadjustedEligibleExpensesLine,
      StringBuilder lessYardageLine,
      StringBuilder lessContractWorkLine,
      StringBuilder totalEligibleExpensesLine,
      StringBuilder totalIneligibleExpensesLine) {

    logger.debug("loadMarginTotalForExpense");

    unadjustedEligibleExpensesLine.append(",");
    lessYardageLine.append(",");
    lessContractWorkLine.append(",");
    totalEligibleExpensesLine.append(",");
    totalIneligibleExpensesLine.append(",");

    if (marginTotal != null) {
      unadjustedEligibleExpensesLine.append(currencyOutputFormat(marginTotal.getUnadjustedAllowableExpenses()));
      lessYardageLine.append(currencyOutputFormat(marginTotal.getYardageExpenses()));
      lessContractWorkLine.append(currencyOutputFormat(marginTotal.getContractWorkExpenses()));
      totalEligibleExpensesLine.append(currencyOutputFormat(marginTotal.getTotalAllowableExpenses()));
      totalIneligibleExpensesLine.append(currencyOutputFormat(marginTotal.getTotalUnallowableExpenses()));
    }
  }

  private void loadMarginTotalForIncome (MarginTotal marginTotal,
      StringBuilder supplyManagedCommodities,
      StringBuilder unadjustedEligibleIncome,
      StringBuilder lessYardageLine,
      StringBuilder lessProgramPayments,
      StringBuilder totalEligibleIncome,
      StringBuilder totalIneligibleIncome) {

    logger.debug("loadMarginTotalForIncome");

    supplyManagedCommodities.append(",");
    unadjustedEligibleIncome.append(",");
    lessYardageLine.append(",");
    lessProgramPayments.append(",");
    totalEligibleIncome.append(",");
    totalIneligibleIncome.append(",");

    if (marginTotal != null) {
      supplyManagedCommodities.append(currencyOutputFormat(marginTotal.getSupplyManagedCommodityIncome()));
      unadjustedEligibleIncome.append(currencyOutputFormat(marginTotal.getUnadjustedAllowableIncome()));
      lessYardageLine.append(currencyOutputFormat(marginTotal.getYardageIncome()));
      lessProgramPayments.append(currencyOutputFormat(marginTotal.getProgramPaymentIncome()));
      totalEligibleIncome.append(currencyOutputFormat(marginTotal.getTotalAllowableIncome()));
      totalIneligibleIncome.append(currencyOutputFormat(marginTotal.getTotalUnallowableIncome()));
    }
  }
  
  
  private String getMetadata (IncomeExpensesForm form) {
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
    
    // Capitalize first letter of income/expenses
    String incomeExpensesLabel = WordUtils.capitalize(form.getIncomeExpenseRadio());
    line.append("Income/Expenses,").append(incomeExpensesLabel).append("\n");
    
    // Capitalize first letter of eligible/ineligible
    String eligibleIneligibleLabel = WordUtils.capitalize(form.getEligibilityRadio());
    line.append("Eligible/Ineligible:,").append(eligibleIneligibleLabel).append("\n");
    
    // Capitalize first letter of financial view, or all for CRA
    String financialViewValue = form.getFinancialViewRadio();
    String financialViewLabel = new String();
    if (financialViewValue != null) {
      switch (financialViewValue) {
        case "adjusted":
          financialViewLabel = "Adjusted";
          break;
        case "adjustments":
          financialViewLabel = "Adjustments";
          break;
        case "cra": financialViewLabel = "CRA";
          break;
      default:
        financialViewLabel = null;
        break;
      }
    }
    line.append("Financial View:,").append(financialViewLabel).append("\n");
    
    line.append("Exported:,").append(new Date());
    line.append("\n");
    
    return line.toString();
  }
  
  private String currencyOutputFormat(Double value) {
    if (value == null) {
      return "";
    }
    return currencyOutputFormat.format(value);
  }

}
