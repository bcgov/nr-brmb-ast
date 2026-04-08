package ca.bc.gov.farms.persistence.v1.dao;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.persistence.v1.dto.BenchmarkPerUnitDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BenchmarkPerUnitDaoTest {

    @Autowired
    private BenchmarkPerUnitDao benchmarkPerUnitDao;

    private static Long benchmarkPerUnitId;

    @Test
    @Order(1)
    public void testInsert() {
        BenchmarkPerUnitDto dto = new BenchmarkPerUnitDto();
        dto.setProgramYear(2024);
        dto.setUnitComment("Alfalfa Dehy");
        dto.setExpiryDate(null);
        dto.setMunicipalityCode("41");
        dto.setInventoryCode("5560");
        dto.setYearMinus6Margin(new BigDecimal("106.43"));
        dto.setYearMinus5Margin(new BigDecimal("128.79"));
        dto.setYearMinus4Margin(new BigDecimal("127.41"));
        dto.setYearMinus3Margin(new BigDecimal("109.64"));
        dto.setYearMinus2Margin(new BigDecimal("95.13"));
        dto.setYearMinus1Margin(new BigDecimal("0.00"));
        dto.setYearMinus6Expense(new BigDecimal("151.44"));
        dto.setYearMinus5Expense(new BigDecimal("156.59"));
        dto.setYearMinus4Expense(new BigDecimal("140.79"));
        dto.setYearMinus3Expense(new BigDecimal("186.58"));
        dto.setYearMinus2Expense(new BigDecimal("258.28"));
        dto.setYearMinus1Expense(new BigDecimal("258.28"));
        dto.setUrlId(1L);

        BenchmarkPerUnitDto result = null;
        try {
            benchmarkPerUnitDao.insert(dto, "testUser");
            benchmarkPerUnitId = dto.getBenchmarkPerUnitId();
            result = benchmarkPerUnitDao.fetch(benchmarkPerUnitId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getProgramYear()).isEqualTo(2024);
        assertThat(result.getUnitComment()).isEqualTo("Alfalfa Dehy");
        assertThat(result.getExpiryDate()).isNull();
        assertThat(result.getMunicipalityCode()).isEqualTo("41");
        assertThat(result.getMunicipalityDesc()).isEqualTo("Cariboo");
        assertThat(result.getInventoryItemCode()).isEqualTo("5560");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Alfalfa Dehy");
        assertThat(result.getStructureGroupCode()).isNull();
        assertThat(result.getStructureGroupDesc()).isNull();
        assertThat(result.getInventoryCode()).isEqualTo("5560");
        assertThat(result.getInventoryDesc()).isEqualTo("Alfalfa Dehy");
        assertThat(result.getYearMinus6Margin()).isEqualByComparingTo(new BigDecimal("106.43"));
        assertThat(result.getYearMinus5Margin()).isEqualByComparingTo(new BigDecimal("128.79"));
        assertThat(result.getYearMinus4Margin()).isEqualByComparingTo(new BigDecimal("127.41"));
        assertThat(result.getYearMinus3Margin()).isEqualByComparingTo(new BigDecimal("109.64"));
        assertThat(result.getYearMinus2Margin()).isEqualByComparingTo(new BigDecimal("95.13"));
        assertThat(result.getYearMinus1Margin()).isEqualByComparingTo(new BigDecimal("0.00"));
        assertThat(result.getYearMinus6Expense()).isEqualByComparingTo(new BigDecimal("151.44"));
        assertThat(result.getYearMinus5Expense()).isEqualByComparingTo(new BigDecimal("156.59"));
        assertThat(result.getYearMinus4Expense()).isEqualByComparingTo(new BigDecimal("140.79"));
        assertThat(result.getYearMinus3Expense()).isEqualByComparingTo(new BigDecimal("186.58"));
        assertThat(result.getYearMinus2Expense()).isEqualByComparingTo(new BigDecimal("258.28"));
        assertThat(result.getYearMinus1Expense()).isEqualByComparingTo(new BigDecimal("258.28"));
        assertThat(result.getUrlId()).isEqualTo(1);
        assertThat(result.getUrl()).isEqualTo("https://google.com");
    }

    @Test
    @Order(2)
    public void testFetchByProgramYear() {
        List<BenchmarkPerUnitDto> dtos = null;
        try {
            dtos = benchmarkPerUnitDao.fetchByProgramYear(2024);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dtos).isNotNull();
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isEqualTo(1);

        BenchmarkPerUnitDto dto = dtos.iterator().next();
        assertThat(dto.getProgramYear()).isEqualTo(2024);
        assertThat(dto.getUnitComment()).isEqualTo("Alfalfa Dehy");
        assertThat(dto.getExpiryDate()).isNull();
        assertThat(dto.getMunicipalityCode()).isEqualTo("41");
        assertThat(dto.getMunicipalityDesc()).isEqualTo("Cariboo");
        assertThat(dto.getInventoryItemCode()).isEqualTo("5560");
        assertThat(dto.getInventoryItemDesc()).isEqualTo("Alfalfa Dehy");
        assertThat(dto.getStructureGroupCode()).isNull();
        assertThat(dto.getStructureGroupDesc()).isNull();
        assertThat(dto.getInventoryCode()).isEqualTo("5560");
        assertThat(dto.getInventoryDesc()).isEqualTo("Alfalfa Dehy");
        assertThat(dto.getYearMinus6Margin()).isEqualByComparingTo(new BigDecimal("106.43"));
        assertThat(dto.getYearMinus5Margin()).isEqualByComparingTo(new BigDecimal("128.79"));
        assertThat(dto.getYearMinus4Margin()).isEqualByComparingTo(new BigDecimal("127.41"));
        assertThat(dto.getYearMinus3Margin()).isEqualByComparingTo(new BigDecimal("109.64"));
        assertThat(dto.getYearMinus2Margin()).isEqualByComparingTo(new BigDecimal("95.13"));
        assertThat(dto.getYearMinus1Margin()).isEqualByComparingTo(new BigDecimal("0.00"));
        assertThat(dto.getYearMinus6Expense()).isEqualByComparingTo(new BigDecimal("151.44"));
        assertThat(dto.getYearMinus5Expense()).isEqualByComparingTo(new BigDecimal("156.59"));
        assertThat(dto.getYearMinus4Expense()).isEqualByComparingTo(new BigDecimal("140.79"));
        assertThat(dto.getYearMinus3Expense()).isEqualByComparingTo(new BigDecimal("186.58"));
        assertThat(dto.getYearMinus2Expense()).isEqualByComparingTo(new BigDecimal("258.28"));
        assertThat(dto.getYearMinus1Expense()).isEqualByComparingTo(new BigDecimal("258.28"));
        assertThat(dto.getUrlId()).isEqualTo(1);
        assertThat(dto.getUrl()).isEqualTo("https://google.com");
    }

    @Test
    @Order(3)
    public void testUpdate() {
        BenchmarkPerUnitDto dto = null;
        try {
            dto = benchmarkPerUnitDao.fetch(benchmarkPerUnitId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setProgramYear(2024);
        dto.setUnitComment("Greenfeed");
        dto.setExpiryDate(null);
        dto.setMunicipalityCode("43");
        dto.setInventoryCode("5562");
        dto.setYearMinus6Margin(new BigDecimal("1106.43"));
        dto.setYearMinus5Margin(new BigDecimal("1128.79"));
        dto.setYearMinus4Margin(new BigDecimal("1127.41"));
        dto.setYearMinus3Margin(new BigDecimal("1109.64"));
        dto.setYearMinus2Margin(new BigDecimal("1095.13"));
        dto.setYearMinus1Margin(new BigDecimal("1000.00"));
        dto.setYearMinus6Expense(new BigDecimal("1151.44"));
        dto.setYearMinus5Expense(new BigDecimal("1156.59"));
        dto.setYearMinus4Expense(new BigDecimal("1140.79"));
        dto.setYearMinus3Expense(new BigDecimal("1186.58"));
        dto.setYearMinus2Expense(new BigDecimal("1258.28"));
        dto.setYearMinus1Expense(new BigDecimal("1258.28"));
        dto.setUrlId(2L);

        BenchmarkPerUnitDto result = null;
        try {
            benchmarkPerUnitDao.update(dto, "testUser");
            result = benchmarkPerUnitDao.fetch(benchmarkPerUnitId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getProgramYear()).isEqualTo(2024);
        assertThat(result.getUnitComment()).isEqualTo("Greenfeed");
        assertThat(result.getExpiryDate()).isNull();
        assertThat(result.getMunicipalityCode()).isEqualTo("43");
        assertThat(result.getMunicipalityDesc()).isEqualTo("Mount Waddington (Island part)");
        assertThat(result.getInventoryItemCode()).isEqualTo("5562");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Greenfeed");
        assertThat(result.getStructureGroupCode()).isNull();
        assertThat(result.getStructureGroupDesc()).isNull();
        assertThat(result.getInventoryCode()).isEqualTo("5562");
        assertThat(result.getInventoryDesc()).isEqualTo("Greenfeed");
        assertThat(result.getYearMinus6Margin()).isEqualByComparingTo(new BigDecimal("1106.43"));
        assertThat(result.getYearMinus5Margin()).isEqualByComparingTo(new BigDecimal("1128.79"));
        assertThat(result.getYearMinus4Margin()).isEqualByComparingTo(new BigDecimal("1127.41"));
        assertThat(result.getYearMinus3Margin()).isEqualByComparingTo(new BigDecimal("1109.64"));
        assertThat(result.getYearMinus2Margin()).isEqualByComparingTo(new BigDecimal("1095.13"));
        assertThat(result.getYearMinus1Margin()).isEqualByComparingTo(new BigDecimal("1000.00"));
        assertThat(result.getYearMinus6Expense()).isEqualByComparingTo(new BigDecimal("1151.44"));
        assertThat(result.getYearMinus5Expense()).isEqualByComparingTo(new BigDecimal("1156.59"));
        assertThat(result.getYearMinus4Expense()).isEqualByComparingTo(new BigDecimal("1140.79"));
        assertThat(result.getYearMinus3Expense()).isEqualByComparingTo(new BigDecimal("1186.58"));
        assertThat(result.getYearMinus2Expense()).isEqualByComparingTo(new BigDecimal("1258.28"));
        assertThat(result.getYearMinus1Expense()).isEqualByComparingTo(new BigDecimal("1258.28"));
        assertThat(result.getUrlId()).isEqualTo(2);
        assertThat(result.getUrl()).isEqualTo("https://microsoft.com");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            benchmarkPerUnitDao.delete(benchmarkPerUnitId);
        });
    }
}
