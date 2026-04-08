package ca.bc.gov.farms.services;

import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.models.ConfigurationParameterListModel;
import ca.bc.gov.farms.data.models.ConfigurationParameterModel;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ConfigurationParameterServiceTest {

    @Autowired
    private ConfigurationParameterService configurationParameterService;

    private static Long configurationParameterId;

    @Test
    @Order(1)
    public void testCreateConfigurationParameter() {
        ConfigurationParameterModel resource = new ConfigurationParameterModel();
        resource.setParameterName("CDOGS - Api Version");
        resource.setParameterValue("2");
        resource.setSensitiveDataInd("N");
        resource.setConfigParamTypeCode("STRING");
        resource.setUserEmail("testUser");

        ConfigurationParameterModel newResource = configurationParameterService.createConfigurationParameter(resource);
        configurationParameterId = newResource.getConfigurationParameterId();

        assertThat(newResource.getParameterName()).isEqualTo("CDOGS - Api Version");
        assertThat(newResource.getParameterValue()).isEqualTo("2");
        assertThat(newResource.getSensitiveDataInd()).isEqualTo("N");
        assertThat(newResource.getConfigParamTypeCode()).isEqualTo("STRING");
    }

    @Test
    @Order(2)
    public void testGetAllConfigurationParameters() {
        ConfigurationParameterListModel resources = configurationParameterService.getAllConfigurationParameters();
        assertThat(resources).isNotNull();
        assertThat(resources.getConfigurationParameterList()).isNotEmpty();
        assertThat(resources.getConfigurationParameterList().size()).isEqualTo(1);

        ConfigurationParameterModel resource = resources.getConfigurationParameterList().iterator().next();
        assertThat(resource.getParameterName()).isEqualTo("CDOGS - Api Version");
        assertThat(resource.getParameterValue()).isEqualTo("2");
        assertThat(resource.getSensitiveDataInd()).isEqualTo("N");
        assertThat(resource.getConfigParamTypeCode()).isEqualTo("STRING");
    }

    @Test
    @Order(3)
    public void testUpdateConfigurationParameter() {
        ConfigurationParameterModel resource = null;
        try {
            resource = configurationParameterService.getConfigurationParameter(configurationParameterId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setParameterValue("3");
        resource.setUserEmail("testUser");

        ConfigurationParameterModel updatedResource = null;
        try {
            updatedResource = configurationParameterService.updateConfigurationParameter(configurationParameterId,
                    resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getParameterName()).isEqualTo("CDOGS - Api Version");
        assertThat(updatedResource.getParameterValue()).isEqualTo("3");
        assertThat(updatedResource.getSensitiveDataInd()).isEqualTo("N");
        assertThat(updatedResource.getConfigParamTypeCode()).isEqualTo("STRING");
    }

    @Test
    @Order(4)
    public void testDeleteConfigurationParameter() {
        assertThatNoException().isThrownBy(() -> {
            configurationParameterService.deleteConfigurationParameter(configurationParameterId);
        });
    }
}
