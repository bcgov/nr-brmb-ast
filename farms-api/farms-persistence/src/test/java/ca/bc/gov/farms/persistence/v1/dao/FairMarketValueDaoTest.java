package ca.bc.gov.farms.persistence.v1.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

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
import ca.bc.gov.farms.persistence.v1.dto.FairMarketValueDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FairMarketValueDaoTest {

    @Autowired
    private FairMarketValueDao fairMarketValueDao;

    private static String fairMarketValueId;

    @Test
    @Order(1)
    public void testInsert() {
        FairMarketValueDto dto = new FairMarketValueDto();
        dto.setProgramYear(2024);
        dto.setInventoryItemCode("5562");
        dto.setMunicipalityCode("43");
        dto.setCropUnitCode("2");
        dto.setPeriod01Price(new BigDecimal("216.05"));
        dto.setPeriod02Price(new BigDecimal("216.05"));
        dto.setPeriod03Price(new BigDecimal("216.05"));
        dto.setPeriod04Price(new BigDecimal("198.42"));
        dto.setPeriod05Price(new BigDecimal("198.42"));
        dto.setPeriod06Price(new BigDecimal("198.42"));
        dto.setPeriod07Price(new BigDecimal("176.37"));
        dto.setPeriod08Price(new BigDecimal("176.37"));
        dto.setPeriod09Price(new BigDecimal("176.37"));
        dto.setPeriod10Price(new BigDecimal("190.04"));
        dto.setPeriod11Price(new BigDecimal("190.04"));
        dto.setPeriod12Price(new BigDecimal("190.04"));
        dto.setPeriod01Variance(new BigDecimal("45.00"));
        dto.setPeriod02Variance(new BigDecimal("45.00"));
        dto.setPeriod03Variance(new BigDecimal("45.00"));
        dto.setPeriod04Variance(new BigDecimal("45.00"));
        dto.setPeriod05Variance(new BigDecimal("45.00"));
        dto.setPeriod06Variance(new BigDecimal("45.00"));
        dto.setPeriod07Variance(new BigDecimal("45.00"));
        dto.setPeriod08Variance(new BigDecimal("45.00"));
        dto.setPeriod09Variance(new BigDecimal("45.00"));
        dto.setPeriod10Variance(new BigDecimal("45.00"));
        dto.setPeriod11Variance(new BigDecimal("45.00"));
        dto.setPeriod12Variance(new BigDecimal("45.00"));

        FairMarketValueDto result = null;
        try {
            fairMarketValueDao.insert(dto, "testUser");
            fairMarketValueId = dto.getFairMarketValueId();
            result = fairMarketValueDao.fetch(2024, fairMarketValueId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getProgramYear()).isEqualTo(2024);
        assertThat(result.getInventoryItemCode()).isEqualTo("5562");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Greenfeed");
        assertThat(result.getMunicipalityCode()).isEqualTo("43");
        assertThat(result.getMunicipalityDesc()).isEqualTo("Mount Waddington (Island part)");
        assertThat(result.getCropUnitCode()).isEqualTo("2");
        assertThat(result.getCropUnitDesc()).isEqualTo("Tonnes");
        assertThat(result.getPeriod01Price()).isEqualByComparingTo(new BigDecimal("216.05"));
        assertThat(result.getPeriod02Price()).isEqualByComparingTo(new BigDecimal("216.05"));
        assertThat(result.getPeriod03Price()).isEqualByComparingTo(new BigDecimal("216.05"));
        assertThat(result.getPeriod04Price()).isEqualByComparingTo(new BigDecimal("198.42"));
        assertThat(result.getPeriod05Price()).isEqualByComparingTo(new BigDecimal("198.42"));
        assertThat(result.getPeriod06Price()).isEqualByComparingTo(new BigDecimal("198.42"));
        assertThat(result.getPeriod07Price()).isEqualByComparingTo(new BigDecimal("176.37"));
        assertThat(result.getPeriod08Price()).isEqualByComparingTo(new BigDecimal("176.37"));
        assertThat(result.getPeriod09Price()).isEqualByComparingTo(new BigDecimal("176.37"));
        assertThat(result.getPeriod10Price()).isEqualByComparingTo(new BigDecimal("190.04"));
        assertThat(result.getPeriod11Price()).isEqualByComparingTo(new BigDecimal("190.04"));
        assertThat(result.getPeriod12Price()).isEqualByComparingTo(new BigDecimal("190.04"));
        assertThat(result.getPeriod01Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod02Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod03Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod04Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod05Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod06Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod07Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod08Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod09Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod10Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod11Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(result.getPeriod12Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
    }
}
