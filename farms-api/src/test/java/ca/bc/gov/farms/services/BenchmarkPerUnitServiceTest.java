package ca.bc.gov.farms.services;

import java.math.BigDecimal;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitModel;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BenchmarkPerUnitServiceTest {

    @Autowired
    private BenchmarkPerUnitService benchmarkPerUnitService;

    @Test
    @Order(1)
    public void testCreateBenchmarkPerUnit() {
        BenchmarkPerUnitModel resource = new BenchmarkPerUnitModel();
        resource.setProgramYear(2024);
        resource.setUnitComment("Alfalfa Dehy");
        resource.setExpiryDate(null);
        resource.setMunicipalityCode("41");
        resource.setInventoryCode("5560");
        resource.setYearMinus6Margin(new BigDecimal("106.43"));
        resource.setYearMinus5Margin(new BigDecimal("128.79"));
        resource.setYearMinus4Margin(new BigDecimal("127.41"));
        resource.setYearMinus3Margin(new BigDecimal("109.64"));
        resource.setYearMinus2Margin(new BigDecimal("95.13"));
        resource.setYearMinus1Margin(new BigDecimal("0.00"));
        resource.setYearMinus6Expense(new BigDecimal("151.44"));
        resource.setYearMinus5Expense(new BigDecimal("156.59"));
        resource.setYearMinus4Expense(new BigDecimal("140.79"));
        resource.setYearMinus3Expense(new BigDecimal("186.58"));
        resource.setYearMinus2Expense(new BigDecimal("258.28"));
        resource.setYearMinus1Expense(new BigDecimal("258.28"));
        resource.setUrlId(1L);
        resource.setUserEmail("testUser");

        BenchmarkPerUnitModel newResource = benchmarkPerUnitService.createBenchmarkPerUnit(resource);

        assertThat(newResource.getProgramYear()).isEqualTo(2024);
        assertThat(newResource.getUnitComment()).isEqualTo("Alfalfa Dehy");
        assertThat(newResource.getExpiryDate()).isNull();
        assertThat(newResource.getMunicipalityCode()).isEqualTo("41");
        assertThat(newResource.getMunicipalityDesc()).isEqualTo("Cariboo");
        assertThat(newResource.getInventoryItemCode()).isEqualTo("5560");
        assertThat(newResource.getInventoryItemDesc()).isEqualTo("Alfalfa Dehy");
        assertThat(newResource.getStructureGroupCode()).isNull();
        assertThat(newResource.getStructureGroupDesc()).isNull();
        assertThat(newResource.getInventoryCode()).isEqualTo("5560");
        assertThat(newResource.getInventoryDesc()).isEqualTo("Alfalfa Dehy");
        assertThat(newResource.getYearMinus6Margin()).isEqualByComparingTo(new BigDecimal("106.43"));
        assertThat(newResource.getYearMinus5Margin()).isEqualByComparingTo(new BigDecimal("128.79"));
        assertThat(newResource.getYearMinus4Margin()).isEqualByComparingTo(new BigDecimal("127.41"));
        assertThat(newResource.getYearMinus3Margin()).isEqualByComparingTo(new BigDecimal("109.64"));
        assertThat(newResource.getYearMinus2Margin()).isEqualByComparingTo(new BigDecimal("95.13"));
        assertThat(newResource.getYearMinus1Margin()).isEqualByComparingTo(new BigDecimal("0.00"));
        assertThat(newResource.getYearMinus6Expense()).isEqualByComparingTo(new BigDecimal("151.44"));
        assertThat(newResource.getYearMinus5Expense()).isEqualByComparingTo(new BigDecimal("156.59"));
        assertThat(newResource.getYearMinus4Expense()).isEqualByComparingTo(new BigDecimal("140.79"));
        assertThat(newResource.getYearMinus3Expense()).isEqualByComparingTo(new BigDecimal("186.58"));
        assertThat(newResource.getYearMinus2Expense()).isEqualByComparingTo(new BigDecimal("258.28"));
        assertThat(newResource.getYearMinus1Expense()).isEqualByComparingTo(new BigDecimal("258.28"));
        assertThat(newResource.getUrlId()).isEqualTo(1);
        assertThat(newResource.getUrl()).isEqualTo("https://google.com");
    }
}
