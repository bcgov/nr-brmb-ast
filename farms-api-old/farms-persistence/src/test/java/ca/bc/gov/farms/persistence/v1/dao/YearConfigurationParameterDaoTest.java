package ca.bc.gov.farms.persistence.v1.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

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
import ca.bc.gov.farms.persistence.v1.dto.YearConfigurationParameterDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class YearConfigurationParameterDaoTest {

    @Autowired
    private YearConfigurationParameterDao yearConfigurationParameterDao;

    private static Long yearConfigurationParameterId;

    @Test
    @Order(1)
    public void testInsert() {
        YearConfigurationParameterDto dto = new YearConfigurationParameterDto();
        dto.setProgramYear(2023);
        dto.setParameterName("Payment Limitation - Percentage of Total Margin Decline");
        dto.setParameterValue("70");
        dto.setConfigParamTypeCode("DECIMAL");

        YearConfigurationParameterDto result = null;
        try {
            yearConfigurationParameterDao.insert(dto, "testUser");
            yearConfigurationParameterId = dto.getYearConfigurationParameterId();
            result = yearConfigurationParameterDao.fetch(yearConfigurationParameterId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getProgramYear()).isEqualTo(2023);
        assertThat(result.getParameterName()).isEqualTo("Payment Limitation - Percentage of Total Margin Decline");
        assertThat(result.getParameterValue()).isEqualTo("70");
        assertThat(result.getConfigParamTypeCode()).isEqualTo("DECIMAL");
    }

    @Test
    @Order(2)
    public void testFetchAll() {
        List<YearConfigurationParameterDto> dtos = null;
        try {
            dtos = yearConfigurationParameterDao.fetchAll();
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dtos).isNotNull();
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isEqualTo(1);

        YearConfigurationParameterDto fetchedDto = dtos.get(0);
        assertThat(fetchedDto.getProgramYear()).isEqualTo(2023);
        assertThat(fetchedDto.getParameterName()).isEqualTo("Payment Limitation - Percentage of Total Margin Decline");
        assertThat(fetchedDto.getParameterValue()).isEqualTo("70");
        assertThat(fetchedDto.getConfigParamTypeCode()).isEqualTo("DECIMAL");
    }

    @Test
    @Order(3)
    public void testUpdate() {
        YearConfigurationParameterDto dto = null;
        try {
            dto = yearConfigurationParameterDao.fetch(yearConfigurationParameterId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setParameterValue("700");

        YearConfigurationParameterDto result = null;
        try {
            yearConfigurationParameterDao.update(dto, "testUser");
            result = yearConfigurationParameterDao.fetch(yearConfigurationParameterId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getProgramYear()).isEqualTo(2023);
        assertThat(result.getParameterName()).isEqualTo("Payment Limitation - Percentage of Total Margin Decline");
        assertThat(result.getParameterValue()).isEqualTo("700");
        assertThat(result.getConfigParamTypeCode()).isEqualTo("DECIMAL");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            yearConfigurationParameterDao.delete(yearConfigurationParameterId);
        });
    }
}
