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
import ca.bc.gov.farms.persistence.v1.dto.ConfigurationParameterDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ConfigurationParameterDaoTest {

    @Autowired
    private ConfigurationParameterDao configurationParameterDao;

    private static Long configurationParameterId;

    @Test
    @Order(1)
    public void testInsert() {
        ConfigurationParameterDto dto = new ConfigurationParameterDto();
        dto.setParameterName("CDOGS - Api Version");
        dto.setParameterValue("2");
        dto.setSensitiveDataInd("N");
        dto.setConfigParamTypeCode("STRING");

        ConfigurationParameterDto result = null;
        try {
            configurationParameterDao.insert(dto, "testUser");
            configurationParameterId = dto.getConfigurationParameterId();
            result = configurationParameterDao.fetch(configurationParameterId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getParameterName()).isEqualTo("CDOGS - Api Version");
        assertThat(result.getParameterValue()).isEqualTo("2");
        assertThat(result.getSensitiveDataInd()).isEqualTo("N");
        assertThat(result.getConfigParamTypeCode()).isEqualTo("STRING");
    }

    @Test
    @Order(2)
    public void testFetchAll() {
        List<ConfigurationParameterDto> dtos = null;
        try {
            dtos = configurationParameterDao.fetchAll();
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dtos).isNotNull();
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isEqualTo(1);

        ConfigurationParameterDto fetchedDto = dtos.get(0);
        assertThat(fetchedDto.getParameterName()).isEqualTo("CDOGS - Api Version");
        assertThat(fetchedDto.getParameterValue()).isEqualTo("2");
        assertThat(fetchedDto.getSensitiveDataInd()).isEqualTo("N");
        assertThat(fetchedDto.getConfigParamTypeCode()).isEqualTo("STRING");
    }

    @Test
    @Order(3)
    public void testUpdate() {
        ConfigurationParameterDto dto = null;
        try {
            dto = configurationParameterDao.fetch(configurationParameterId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setParameterValue("3");

        ConfigurationParameterDto result = null;
        try {
            configurationParameterDao.update(dto, "testUser");
            result = configurationParameterDao.fetch(configurationParameterId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getParameterName()).isEqualTo("CDOGS - Api Version");
        assertThat(result.getParameterValue()).isEqualTo("3");
        assertThat(result.getSensitiveDataInd()).isEqualTo("N");
        assertThat(result.getConfigParamTypeCode()).isEqualTo("STRING");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            configurationParameterDao.delete(configurationParameterId);
        });
    }
}
