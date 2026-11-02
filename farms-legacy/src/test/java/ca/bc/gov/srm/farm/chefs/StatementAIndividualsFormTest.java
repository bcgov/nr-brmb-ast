package ca.bc.gov.srm.farm.chefs;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import org.junit.jupiter.api.Test;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementAIndividualsSubmissionDataResource;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementAOperationMapper;
import ca.bc.gov.srm.farm.chefs.resource.statementA.StatementASubmissionDataResource;
import ca.bc.gov.srm.farm.domain.FarmingOperation;
import ca.bc.gov.srm.farm.domain.InventoryItem;
import ca.bc.gov.srm.farm.domain.CropItem;
import ca.bc.gov.srm.farm.domain.ProductiveUnitCapacity;
import ca.bc.gov.srm.farm.chefs.resource.common.CattleGrid;
import ca.bc.gov.srm.farm.chefs.resource.supplemental.SupplementalBaseDataResource;
import ca.bc.gov.srm.farm.service.impl.ChefsSubmissionProcessorServiceImpl;

public class StatementAIndividualsFormTest {

  @Test
  void mapsMichelleFieldsAndPreservesCanonicalPdfAmounts() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    StatementAIndividualsSubmissionDataResource data;
    try (InputStream input = getClass().getResourceAsStream("/chefs/statement-a-individuals-michelle.json")) {
      assertNotNull(input);
      data = mapper.readValue(input, StatementAIndividualsSubmissionDataResource.class);
    }
    assertEquals("SAMPLE ONLY - Alex Farmer", data.getParticipantName());
    assertNull(data.getCorporationName());
    assertNull(data.getFarmType());
    assertEquals("SAMPLE ONLY", data.getThirdPartyFirstName());
    assertEquals("sample@example.com", data.getThirdPartyEmail());
    assertEquals("yes", data.getDoYouHaveMultipleOperations());
    FarmingOperation operation = new FarmingOperation();
    StatementAOperationMapper.populate(data, operation);
    assertEquals(-100.0, operation.getBusinessUseHomeExpense());
    assertEquals(925.0, operation.getNetFarmIncome());
    assertEquals(data.getFiscalYearStartDate(), operation.getFiscalYearStart());
    assertEquals("2", operation.getAccountingCode());
    JsonNode pdf = mapper.valueToTree(data);
    assertEquals(data.getParticipantName(), pdf.get("participantName").asText());
    assertEquals(-100.0, pdf.get("businessUseOfHomeAdjustment").asDouble());
    assertEquals(25.0, pdf.get("gstHstRebateForPartners").asDouble());
    assertFalse(pdf.has("adjustmentToBusinessUseOfHomeExpenses"));
  }

  @Test
  void readsEarlierIndividualsPayloadWithoutChangingCorporateNames() throws Exception {
    String previous = "{\"corporationName\":\"Existing name\",\"businessUseOfHomeAdjustment\":-100,\"gstHstRebateForPartners\":25,"
        + "\"fiscalYearStart\":1704067200000,\"fiscalYearEnd\":1735603200000}";
    ObjectMapper mapper = new ObjectMapper();
    StatementAIndividualsSubmissionDataResource individual = mapper.readValue(previous, StatementAIndividualsSubmissionDataResource.class);
    assertEquals("Existing name", individual.getParticipantName());
    assertEquals(-100.0, individual.getBusinessUseOfHomeAdjustment());
    assertEquals(25.0, individual.getGstHstRebateForPartners());
    StatementASubmissionDataResource corporate = mapper.readValue(previous, StatementASubmissionDataResource.class);
    assertEquals("Existing name", corporate.getCorporationName());
    assertEquals(-100.0, corporate.getBusinessUseOfHomeAdjustment());
    assertEquals(25.0, corporate.getGstHstRebateForPartners());
    assertFalse(mapper.valueToTree(corporate).has("participantName"));
  }

  @Test
  void retainsSummaryAndCombiningFieldsFromTheFinalForm() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    StatementAIndividualsSubmissionDataResource data = mapper.readValue(
        "{\"total\":100,\"shouldBeCombinedCurrentYear\":\"yes\",\"wasCombinedLast3Years\":\"no\","
        + "\"SummaryOfIncome\":1000,\"SummaryOfExpenses\":200,"
        + "\"fiscalYearStart\":1704067200000,\"fiscalYearEnd\":1735603200000}",
        StatementAIndividualsSubmissionDataResource.class);
    assertEquals(100.0, data.getTotal());
    assertEquals("yes", data.getShouldBeCombinedCurrentYear());
    assertEquals("no", data.getWasCombinedLast3Years());
    assertEquals(1000.0, data.getSummaryOfIncome());
    assertEquals(200.0, data.getSummaryOfExpenses());
    JsonNode pdf = mapper.valueToTree(data);
    assertEquals("yes", pdf.path("shouldBeCombinedCurrentYear").asText());
    assertEquals("no", pdf.path("wasCombinedLast3Years").asText());
    assertEquals(1000.0, pdf.path("summaryOfIncome").asDouble());
    assertEquals(200.0, pdf.path("summaryOfExpenses").asDouble());
  }

  @Test
  @SuppressWarnings("unchecked")
  void preservesSubmittedCattleAndVegetableQuantitiesInTheOperation() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    StatementAIndividualsSubmissionDataResource data;
    try (InputStream input = getClass().getResourceAsStream("/chefs/statement-a-individuals-michelle.json")) {
      assertNotNull(input);
      data = mapper.readValue(input, StatementAIndividualsSubmissionDataResource.class);
    }
    assertEquals(12.0, data.getCattleGrid().get(0).getEndingInventoryCattle());
    FarmingOperation operation = new FarmingOperation();
    ChefsSubmissionProcessorServiceImpl service = new ChefsSubmissionProcessorServiceImpl();
    Method inventoryMapper = ChefsSubmissionProcessorServiceImpl.class.getDeclaredMethod(
        "createSupplementalInventoryList", SupplementalBaseDataResource.class, FarmingOperation.class);
    inventoryMapper.setAccessible(true);
    List<InventoryItem> inventory = (List<InventoryItem>)inventoryMapper.invoke(service, data, operation);
    assertEquals(3, inventory.size());
    InventoryItem cattle = inventory.stream().filter(item -> "8000".equals(item.getInventoryItemCode())).findFirst().get();
    assertEquals(12.0, cattle.getAdjQuantityEnd());
    assertEquals(1200.0, cattle.getAdjPriceEnd());
    assertSame(operation, cattle.getFarmingOperation());
    CropItem vegetable = (CropItem)inventory.stream().filter(item -> "5001".equals(item.getInventoryItemCode())).findFirst().get();
    assertEquals(40.0, vegetable.getAdjQuantityProduced());
    Method unitMapper = ChefsSubmissionProcessorServiceImpl.class.getDeclaredMethod(
        "createPucList", SupplementalBaseDataResource.class, FarmingOperation.class);
    unitMapper.setAccessible(true);
    List<ProductiveUnitCapacity> units = (List<ProductiveUnitCapacity>)unitMapper.invoke(service, data, operation);
    assertEquals(2, units.size());
    assertTrue(units.stream().anyMatch(unit -> Double.valueOf(3.0).equals(unit.getAdjAmount())));
    for (ProductiveUnitCapacity unit : units) assertSame(operation, unit.getFarmingOperation());
    JsonNode pdf = mapper.valueToTree(data);
    assertEquals(12.0, pdf.path("cattleGrid").get(0).path("endingInventoryCattle").asDouble());
  }

  @Test
  void preservesExistingCattleFieldNameForOtherForms() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    CattleGrid previous = mapper.readValue("{\"endingInventoryCattle\":18}", CattleGrid.class);
    CattleGrid current = mapper.readValue("{\"endingInventory\":12}", CattleGrid.class);
    assertEquals(18.0, previous.getEndingInventoryCattle());
    assertEquals(12.0, current.getEndingInventoryCattle());
    JsonNode pdf = mapper.valueToTree(current);
    assertEquals(12.0, pdf.path("endingInventoryCattle").asDouble());
    assertFalse(pdf.has("endingInventory"));
  }

}
