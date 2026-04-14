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
import ca.bc.gov.farms.data.models.YearConfigurationParameterListRsrc;
import ca.bc.gov.farms.data.models.YearConfigurationParameterRsrc;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class YearConfigurationParameterServiceTest {

    @Autowired
    private YearConfigurationParameterService yearConfigurationParameterService;

    private static Long yearConfigurationParameterId;

    @Test
    @Order(1)
    public void testCreateYearConfigurationParameter() {
        YearConfigurationParameterRsrc resource = new YearConfigurationParameterRsrc();
        resource.setProgramYear(2023);
        resource.setParameterName("Payment Limitation - Percentage of Total Margin Decline");
        resource.setParameterValue("70");
        resource.setConfigParamTypeCode("DECIMAL");
        resource.setUserEmail("testUser");

        YearConfigurationParameterRsrc newResource = yearConfigurationParameterService
                .createYearConfigurationParameter(resource);
        yearConfigurationParameterId = newResource.getYearConfigurationParameterId();

        assertThat(newResource.getProgramYear()).isEqualTo(2023);
        assertThat(newResource.getParameterName()).isEqualTo("Payment Limitation - Percentage of Total Margin Decline");
        assertThat(newResource.getParameterValue()).isEqualTo("70");
        assertThat(newResource.getConfigParamTypeCode()).isEqualTo("DECIMAL");
    }

    @Test
    @Order(2)
    public void testGetAllYearConfigurationParameters() {
        YearConfigurationParameterListRsrc resources = yearConfigurationParameterService
                .getAllYearConfigurationParameters();
        assertThat(resources).isNotNull();
        assertThat(resources.getYearConfigurationParameterList()).isNotEmpty();
        assertThat(resources.getYearConfigurationParameterList().size()).isEqualTo(1);

        YearConfigurationParameterRsrc resource = resources.getYearConfigurationParameterList().iterator().next();
        assertThat(resource.getProgramYear()).isEqualTo(2023);
        assertThat(resource.getParameterName()).isEqualTo("Payment Limitation - Percentage of Total Margin Decline");
        assertThat(resource.getParameterValue()).isEqualTo("70");
        assertThat(resource.getConfigParamTypeCode()).isEqualTo("DECIMAL");
    }

    @Test
    @Order(3)
    public void testUpdateYearConfigurationParameter() {
        YearConfigurationParameterRsrc resource = null;
        try {
            resource = yearConfigurationParameterService.getYearConfigurationParameter(yearConfigurationParameterId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setParameterValue("700");
        resource.setUserEmail("testUser");

        YearConfigurationParameterRsrc updatedResource = null;
        try {
            updatedResource = yearConfigurationParameterService
                    .updateYearConfigurationParameter(yearConfigurationParameterId, resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getProgramYear()).isEqualTo(2023);
        assertThat(updatedResource.getParameterName())
                .isEqualTo("Payment Limitation - Percentage of Total Margin Decline");
        assertThat(updatedResource.getParameterValue()).isEqualTo("700");
        assertThat(updatedResource.getConfigParamTypeCode()).isEqualTo("DECIMAL");
    }

    @Test
    @Order(4)
    public void testDeleteYearConfigurationParameter() {
        assertThatNoException().isThrownBy(() -> {
            yearConfigurationParameterService.deleteYearConfigurationParameter(yearConfigurationParameterId);
        });
    }
}
