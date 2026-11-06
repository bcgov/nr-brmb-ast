package ca.bc.gov.srm.farm.chefs;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Method;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementAOperationMapper;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.supplemental.SupplementalBaseDataResource;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.IncomeExpense;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.service.impl.ChefsSubmissionProcessorServiceImpl;

public class StatementAIndividualsMappingTest {

  @Test
  public void deserializesNewAmounts() throws Exception {
    StatementASubmissionDataResource data = new ObjectMapper().readValue(
        "{\"netFarmingIncome\":925}",
        StatementASubmissionDataResource.class);
    assertEquals(925.0, data.getNetFarmingIncome());
  }

  @Test
  public void acceptsMultipleOperationsYesWithoutAdditionalInput() throws Exception {
    StatementASubmissionDataResource data = new ObjectMapper().readValue(
        "{\"doYouHaveMultipleOperations\":\"yes\"}", StatementASubmissionDataResource.class);
    assertEquals("yes", data.getDoYouHaveMultipleOperations());
    assertNull(data.getNetFarmingIncome());
  }

  @Test
  public void mapsAmountsAndRecalculatesNetIncome() {
    StatementASubmissionDataResource first = new StatementASubmissionDataResource();
    first.setNetIncomeAfterAdjustments(1000.0);
    first.setNetFarmingIncome(999999.0); // Ignore a stale or edited client-side total.
    first.setAccountingMethod("cash");
    FarmingOperation op1 = new FarmingOperation();
    StatementAOperationMapper.populate(first, op1);
    assertEquals(1000.0, op1.getNetFarmIncome());
    assertEquals(1000.0, op1.getNetIncomeAfterAdj());
    assertEquals("2", op1.getAccountingCode());

    StatementASubmissionDataResource second = new StatementASubmissionDataResource();
    second.setNetIncomeAfterAdjustments(-500.0);
    second.setNetFarmingIncome(-500.0);
    second.setBusinessStructure("partnership");
    second.setFarmingOperationPercentage(40.0);
    FarmingOperation op2 = new FarmingOperation();
    StatementAOperationMapper.populate(second, op2);
    assertEquals(-500.0, op2.getNetFarmIncome());
    assertEquals(0.4, op2.getPartnershipPercent());
    assertEquals(1000.0, op1.getNetFarmIncome());
  }

  @Test
  public void roundsAmountsAndPreservesLegacyDefaults() {
    StatementASubmissionDataResource data = new StatementASubmissionDataResource();
    data.setNetIncomeAfterAdjustments(100.105);
    data.setNetFarmingIncome(100.105);
    FarmingOperation operation = new FarmingOperation();
    StatementAOperationMapper.populate(data, operation);
    assertEquals(100.11, operation.getNetFarmIncome());
    data = new StatementASubmissionDataResource();
    operation.setNetFarmIncome(0.0);
    StatementAOperationMapper.populate(data, operation);
    assertEquals(0.0, operation.getNetFarmIncome());
  }

  @Test
  public void credentialsRequireBothValuesAndNeverAcceptPlaceholder() {
    assertFalse(new ChefsFormCredentials(null, "secret").isConfigured());
    assertFalse(new ChefsFormCredentials("form", " ").isConfigured());
    assertFalse(new ChefsFormCredentials("form", "placeholder").isConfigured());
    assertFalse(new ChefsFormCredentials("placeholder", "secret").isConfigured());
    assertTrue(new ChefsFormCredentials("form", "secret").isConfigured());
  }

  @Test
  public void mapsSubmittedIncomeExpensesInventoryAndUnitsToOneOperation() throws Exception {
    StatementASubmissionDataResource data = new ObjectMapper().readValue(
        "{\"fiscalYearStart\":\"2024-01-01T00:00:00-08:00\",\"fiscalYearEnd\":\"2024-12-31T00:00:00-08:00\","
        + "\"doYouHaveMultipleOperations\":\"yes\","
        + "\"allowableIncomeGrid\":[{\"category\":{\"value\":\"9420\"},\"amount\":1000}],"
        + "\"allowableExpensesGrid\":[{\"category\":{\"value\":\"9661\"},\"amount\":200}],"
        + "\"grainGrid\":[{\"commodity\":{\"value\":\"5000\"},\"acres\":10,\"quantityProduced\":20}]}",
        StatementASubmissionDataResource.class);
    ChefsSubmissionProcessorServiceImpl service = new ChefsSubmissionProcessorServiceImpl();
    FarmingOperation operation = new FarmingOperation();
    StatementAOperationMapper.populate(data, operation);
    assertEquals(2024, data.getYear());
    assertEquals(data.getFiscalYearStartDate(), operation.getFiscalYearStart());

    List<IncomeExpense> income = mapList(service, "createStatementAIncomeExpensesList",
        StatementASubmissionDataResource.class, data, operation);
    assertEquals(2, income.size());
    assertEquals(1000.0, income.get(0).getAdjAmount());
    assertEquals(200.0, income.get(1).getAdjAmount());
    assertTrue(income.get(1).getIsExpense());
    for (IncomeExpense item : income) assertSame(operation, item.getFarmingOperation());

    List<InventoryItem> inventory = mapList(service, "createSupplementalInventoryList",
        SupplementalBaseDataResource.class, data, operation);
    assertEquals(1, inventory.size());
    assertEquals(20.0, ((CropItem) inventory.get(0)).getAdjQuantityProduced());
    assertSame(operation, inventory.get(0).getFarmingOperation());

    List<ProductiveUnitCapacity> units = mapList(service, "createPucList",
        SupplementalBaseDataResource.class, data, operation);
    assertEquals(1, units.size());
    assertEquals(10.0, units.get(0).getAdjAmount());
    assertSame(operation, units.get(0).getFarmingOperation());
  }

  @SuppressWarnings("unchecked")
  private <T> List<T> mapList(ChefsSubmissionProcessorServiceImpl service, String methodName, Class<?> dataType,
      StatementASubmissionDataResource data, FarmingOperation operation) throws Exception {
    Method method = ChefsSubmissionProcessorServiceImpl.class.getDeclaredMethod(methodName, dataType, FarmingOperation.class);
    method.setAccessible(true);
    return (List<T>) method.invoke(service, data, operation);
  }
}
