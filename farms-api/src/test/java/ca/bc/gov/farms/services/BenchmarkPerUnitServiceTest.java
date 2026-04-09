package ca.bc.gov.farms.services;

import java.math.BigDecimal;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitListModel;
import ca.bc.gov.farms.data.models.BenchmarkPerUnitRsrc;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BenchmarkPerUnitServiceTest {

    @Autowired
    private BenchmarkPerUnitService benchmarkPerUnitService;

    private static Long benchmarkPerUnitId;

    @Test
    @Order(1)
    public void testCreateBenchmarkPerUnit() {
        BenchmarkPerUnitRsrc resource = new BenchmarkPerUnitRsrc();
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

        BenchmarkPerUnitRsrc newResource = benchmarkPerUnitService.createBenchmarkPerUnit(resource);
        benchmarkPerUnitId = newResource.getBenchmarkPerUnitId();

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

    @Test
    @Order(2)
    public void testGetBenchmarkPerUnitsByProgramYear() {
        BenchmarkPerUnitListModel resources = benchmarkPerUnitService.getBenchmarkPerUnitsByProgramYear(2024);
        assertThat(resources).isNotNull();
        assertThat(resources.getBenchmarkPerUnitList()).isNotEmpty();
        assertThat(resources.getBenchmarkPerUnitList().size()).isEqualTo(1);

        BenchmarkPerUnitRsrc resource = resources.getBenchmarkPerUnitList().iterator().next();
        assertThat(resource.getProgramYear()).isEqualTo(2024);
        assertThat(resource.getUnitComment()).isEqualTo("Alfalfa Dehy");
        assertThat(resource.getExpiryDate()).isNull();
        assertThat(resource.getMunicipalityCode()).isEqualTo("41");
        assertThat(resource.getMunicipalityDesc()).isEqualTo("Cariboo");
        assertThat(resource.getInventoryItemCode()).isEqualTo("5560");
        assertThat(resource.getInventoryItemDesc()).isEqualTo("Alfalfa Dehy");
        assertThat(resource.getStructureGroupCode()).isNull();
        assertThat(resource.getStructureGroupDesc()).isNull();
        assertThat(resource.getInventoryCode()).isEqualTo("5560");
        assertThat(resource.getInventoryDesc()).isEqualTo("Alfalfa Dehy");
        assertThat(resource.getYearMinus6Margin()).isEqualByComparingTo(new BigDecimal("106.43"));
        assertThat(resource.getYearMinus5Margin()).isEqualByComparingTo(new BigDecimal("128.79"));
        assertThat(resource.getYearMinus4Margin()).isEqualByComparingTo(new BigDecimal("127.41"));
        assertThat(resource.getYearMinus3Margin()).isEqualByComparingTo(new BigDecimal("109.64"));
        assertThat(resource.getYearMinus2Margin()).isEqualByComparingTo(new BigDecimal("95.13"));
        assertThat(resource.getYearMinus1Margin()).isEqualByComparingTo(new BigDecimal("0.00"));
        assertThat(resource.getYearMinus6Expense()).isEqualByComparingTo(new BigDecimal("151.44"));
        assertThat(resource.getYearMinus5Expense()).isEqualByComparingTo(new BigDecimal("156.59"));
        assertThat(resource.getYearMinus4Expense()).isEqualByComparingTo(new BigDecimal("140.79"));
        assertThat(resource.getYearMinus3Expense()).isEqualByComparingTo(new BigDecimal("186.58"));
        assertThat(resource.getYearMinus2Expense()).isEqualByComparingTo(new BigDecimal("258.28"));
        assertThat(resource.getYearMinus1Expense()).isEqualByComparingTo(new BigDecimal("258.28"));
        assertThat(resource.getUrlId()).isEqualTo(1);
        assertThat(resource.getUrl()).isEqualTo("https://google.com");
    }

    @Test
    @Order(3)
    public void testUpdateBenchmarkPerUnit() {
        BenchmarkPerUnitRsrc resource;
        try {
            resource = benchmarkPerUnitService.getBenchmarkPerUnit(benchmarkPerUnitId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setProgramYear(2024);
        resource.setUnitComment("Greenfeed");
        resource.setExpiryDate(null);
        resource.setMunicipalityCode("43");
        resource.setInventoryCode("5562");
        resource.setYearMinus6Margin(new BigDecimal("1106.43"));
        resource.setYearMinus5Margin(new BigDecimal("1128.79"));
        resource.setYearMinus4Margin(new BigDecimal("1127.41"));
        resource.setYearMinus3Margin(new BigDecimal("1109.64"));
        resource.setYearMinus2Margin(new BigDecimal("1095.13"));
        resource.setYearMinus1Margin(new BigDecimal("1000.00"));
        resource.setYearMinus6Expense(new BigDecimal("1151.44"));
        resource.setYearMinus5Expense(new BigDecimal("1156.59"));
        resource.setYearMinus4Expense(new BigDecimal("1140.79"));
        resource.setYearMinus3Expense(new BigDecimal("1186.58"));
        resource.setYearMinus2Expense(new BigDecimal("1258.28"));
        resource.setYearMinus1Expense(new BigDecimal("1258.28"));
        resource.setUrlId(2L);
        resource.setUserEmail("testUser");

        BenchmarkPerUnitRsrc updatedResource;
        try {
            updatedResource = benchmarkPerUnitService.updateBenchmarkPerUnit(benchmarkPerUnitId,
                    resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getProgramYear()).isEqualTo(2024);
        assertThat(updatedResource.getUnitComment()).isEqualTo("Greenfeed");
        assertThat(updatedResource.getExpiryDate()).isNull();
        assertThat(updatedResource.getMunicipalityCode()).isEqualTo("43");
        assertThat(updatedResource.getMunicipalityDesc()).isEqualTo("Mount Waddington (Island part)");
        assertThat(updatedResource.getInventoryItemCode()).isEqualTo("5562");
        assertThat(updatedResource.getInventoryItemDesc()).isEqualTo("Greenfeed");
        assertThat(updatedResource.getStructureGroupCode()).isNull();
        assertThat(updatedResource.getStructureGroupDesc()).isNull();
        assertThat(updatedResource.getInventoryCode()).isEqualTo("5562");
        assertThat(updatedResource.getInventoryDesc()).isEqualTo("Greenfeed");
        assertThat(updatedResource.getYearMinus6Margin()).isEqualByComparingTo(new BigDecimal("1106.43"));
        assertThat(updatedResource.getYearMinus5Margin()).isEqualByComparingTo(new BigDecimal("1128.79"));
        assertThat(updatedResource.getYearMinus4Margin()).isEqualByComparingTo(new BigDecimal("1127.41"));
        assertThat(updatedResource.getYearMinus3Margin()).isEqualByComparingTo(new BigDecimal("1109.64"));
        assertThat(updatedResource.getYearMinus2Margin()).isEqualByComparingTo(new BigDecimal("1095.13"));
        assertThat(updatedResource.getYearMinus1Margin()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(updatedResource.getYearMinus6Expense()).isEqualByComparingTo(new BigDecimal("1151.44"));
        assertThat(updatedResource.getYearMinus5Expense()).isEqualByComparingTo(new BigDecimal("1156.59"));
        assertThat(updatedResource.getYearMinus4Expense()).isEqualByComparingTo(new BigDecimal("1140.79"));
        assertThat(updatedResource.getYearMinus3Expense()).isEqualByComparingTo(new BigDecimal("1186.58"));
        assertThat(updatedResource.getYearMinus2Expense()).isEqualByComparingTo(new BigDecimal("1258.28"));
        assertThat(updatedResource.getYearMinus1Expense()).isEqualByComparingTo(new BigDecimal("1258.28"));
        assertThat(updatedResource.getUrlId()).isEqualTo(2);
        assertThat(updatedResource.getUrl()).isEqualTo("https://microsoft.com");
    }

    @Test
    @Order(4)
    public void testDeleteBenchmarkPerUnit() {
        assertThatNoException().isThrownBy(() -> {
            benchmarkPerUnitService.deleteBenchmarkPerUnit(benchmarkPerUnitId);
        });
    }
}
