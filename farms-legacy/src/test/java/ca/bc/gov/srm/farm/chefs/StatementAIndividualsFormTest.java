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
  void mapsCurrentIndividualsForm() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    StatementAIndividualsSubmissionDataResource data;
    try (InputStream input = getClass().getResourceAsStream("/chefs/statement-a-individuals-test-1.json")) {
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
    assertEquals(1000.0, operation.getNetFarmIncome());
    assertEquals(data.getFiscalYearStartDate(), operation.getFiscalYearStart());
    assertEquals("2", operation.getAccountingCode());
    JsonNode pdf = mapper.valueToTree(data);
    assertEquals(data.getParticipantName(), pdf.get("participantName").asText());
  }

  @Test
  void usesLine9944ForNetFarmIncome() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    StatementAIndividualsSubmissionDataResource data = mapper.readValue(
        "{\"netIncomeAfterAdjustments\":1000,\"netFarmingIncome\":999999,"
        + "\"fiscalYearStart\":1704067200000,\"fiscalYearEnd\":1735603200000}",
        StatementAIndividualsSubmissionDataResource.class);
    FarmingOperation operation = new FarmingOperation();
    StatementAOperationMapper.populate(data, operation);
    assertEquals(1000.0, operation.getNetFarmIncome());
  }

  @Test
  void readsEarlierIndividualsPayloadWithoutChangingCorporateNames() throws Exception {
    String previous = "{\"corporationName\":\"Existing name\","
        + "\"fiscalYearStart\":1704067200000,\"fiscalYearEnd\":1735603200000}";
    ObjectMapper mapper = new ObjectMapper();
    StatementAIndividualsSubmissionDataResource individual = mapper.readValue(previous, StatementAIndividualsSubmissionDataResource.class);
    assertEquals("Existing name", individual.getParticipantName());
    StatementASubmissionDataResource corporate = mapper.readValue(previous, StatementASubmissionDataResource.class);
    assertEquals("Existing name", corporate.getCorporationName());
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
  void bothFormsRetainAdditionalPdfFields() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    String payload = "{\"shouldBeCombinedCurrentYear\":\"yes\",\"wasCombinedLast3Years\":\"no\",\"total\":100,"
        + "\"farmingOperationPercentage\":60,\"unableToCompleteBecauseOfDisaster\":\"yes\",\"decreasedParticipant\":\"no\","
        + "\"authorizeThirdParty\":\"yes\",\"thirdPartyFirstName\":\"CONTACT-FIRST\",\"thirdPartyLastName\":\"CONTACT-LAST\","
        + "\"thirdPartyBusinessName\":\"SAMPLE BUSINESS\",\"thirdPartyAddress\":\"456 Proxy Road\",\"thirdPartyTownCity\":\"Contact Town\","
        + "\"thirdPartyProvince\":\"BC\",\"thirdPartyPostalCode\":\"V1V 1V1\",\"thirdPartyTelephone\":\"250-555-0222\","
        + "\"thirdPartyFax\":\"250-555-0333\",\"thirdPartyEmail\":\"contact.sample@example.com\",\"copyOfCOB\":\"yes\","
        + "\"totalAllowableIncome\":1100,\"totalNonAllowableIncome\":220,\"totalAllowableExpenses\":300,\"totalNonAllowableExpenses\":40,"
        + "\"receivablesGrid\":[{\"specify\":\"RECEIVABLE-DETAIL\",\"incomeReceivedAfterYearEnd\":50}]}";
    JsonNode expected = mapper.readTree(payload);
    for (Class<? extends StatementASubmissionDataResource> resource : java.util.Arrays.asList(
        StatementASubmissionDataResource.class, StatementAIndividualsSubmissionDataResource.class)) {
      StatementASubmissionDataResource data = mapper.readValue(payload, resource);
      data.setFiscalYearStart(new java.util.Date(1704067200000L));
      data.setFiscalYearEnd(new java.util.Date(1735603200000L));
      assertEquals("yes", data.getShouldBeCombinedCurrentYear());
      assertEquals("no", data.getWasCombinedLast3Years());
      assertEquals(100.0, data.getTotal());
      JsonNode actual = mapper.valueToTree(data);
      java.util.Iterator<String> fields = expected.fieldNames();
      while (fields.hasNext()) {
        String field = fields.next();
        if (!expected.get(field).isContainerNode()) {
          // Compare numbers by value: Double properties serialize integer inputs as decimals.
          if (expected.get(field).isNumber()) assertEquals(expected.get(field).asDouble(), actual.path(field).asDouble(), resource.getSimpleName() + ": " + field);
          else assertEquals(expected.get(field), actual.get(field), resource.getSimpleName() + ": " + field);
        }
      }
      assertEquals("RECEIVABLE-DETAIL", actual.at("/receivablesGrid/0/specify").asText());
    }
  }

  @Test
  @SuppressWarnings("unchecked")
  void preservesSubmittedCattleAndVegetableQuantitiesInTheOperation() throws Exception {
    ObjectMapper mapper = new ObjectMapper();
    StatementAIndividualsSubmissionDataResource data;
    try (InputStream input = getClass().getResourceAsStream("/chefs/statement-a-individuals-test-1.json")) {
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
