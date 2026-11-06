package ca.bc.gov.srm.farm.chefs.resource.statementA;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ca.bc.gov.srm.farm.domain.FarmingOperation;

/** Maps the amounts for one Statement A operation without trusting the submitted calculated total. */
public final class StatementAOperationMapper {

  private StatementAOperationMapper() {
  }

  public static void populate(StatementASubmissionDataResource data, FarmingOperation operation) {
    operation.setFiscalYearStart(data.getFiscalYearStartDate());
    operation.setFiscalYearEnd(data.getFiscalYearEndDate());
    operation.setGrossIncome(data.getTotalIncome());
    operation.setFarmingExpenses(data.getTotalExpenses());
    operation.setInventoryAdjustments(data.getTotalAdjustments());
    operation.setNetIncomeBeforeAdj(data.getNetIncomeBeforeAdjustments());
    operation.setNetIncomeAfterAdj(data.getNetIncomeAfterAdjustments());
    operation.setAccountingCode("cash".equals(data.getAccountingMethod()) ? "2" : "1");

    if ("partnership".equals(data.getBusinessStructure()) && data.getFarmingOperationPercentage() != null
        && data.getFarmingOperationPercentage() > 0) {
      operation.setPartnershipPercent(data.getFarmingOperationPercentage() / 100.0);
    }

    // Older submissions have no netFarmingIncome field. For those, keep the 0.0 set when the operation was created.
    if (data.getNetFarmingIncome() != null) {
      operation.setNetFarmIncome(amount(data.getNetIncomeAfterAdjustments())
          .setScale(2, RoundingMode.HALF_UP).doubleValue());
    }
  }

  private static BigDecimal amount(Double value) {
    return value == null ? BigDecimal.ZERO : BigDecimal.valueOf(value);
  }
}
