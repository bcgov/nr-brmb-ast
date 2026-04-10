package ca.bc.gov.farms.services;

import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import ca.bc.gov.brmb.common.service.api.ConflictException;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.models.FairMarketValueListRsrc;
import ca.bc.gov.farms.data.models.FairMarketValueRsrc;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FairMarketValueServiceTest {

    @Autowired
    private FairMarketValueService fairMarketValueService;

    private static String fairMarketValueId;

    @Test
    @Order(1)
    public void testCreateFairMarketValue() {
        FairMarketValueRsrc resource = new FairMarketValueRsrc();
        resource.setProgramYear(2024);
        resource.setInventoryItemCode("5562");
        resource.setMunicipalityCode("43");
        resource.setCropUnitCode("2");
        resource.setPeriod01Price(new BigDecimal("216.05"));
        resource.setPeriod02Price(new BigDecimal("216.05"));
        resource.setPeriod03Price(new BigDecimal("216.05"));
        resource.setPeriod04Price(new BigDecimal("198.42"));
        resource.setPeriod05Price(new BigDecimal("198.42"));
        resource.setPeriod06Price(new BigDecimal("198.42"));
        resource.setPeriod07Price(new BigDecimal("176.37"));
        resource.setPeriod08Price(new BigDecimal("176.37"));
        resource.setPeriod09Price(new BigDecimal("176.37"));
        resource.setPeriod10Price(new BigDecimal("190.04"));
        resource.setPeriod11Price(new BigDecimal("190.04"));
        resource.setPeriod12Price(new BigDecimal("190.04"));
        resource.setPeriod01Variance(new BigDecimal("45.00"));
        resource.setPeriod02Variance(new BigDecimal("45.00"));
        resource.setPeriod03Variance(new BigDecimal("45.00"));
        resource.setPeriod04Variance(new BigDecimal("45.00"));
        resource.setPeriod05Variance(new BigDecimal("45.00"));
        resource.setPeriod06Variance(new BigDecimal("45.00"));
        resource.setPeriod07Variance(new BigDecimal("45.00"));
        resource.setPeriod08Variance(new BigDecimal("45.00"));
        resource.setPeriod09Variance(new BigDecimal("45.00"));
        resource.setPeriod10Variance(new BigDecimal("45.00"));
        resource.setPeriod11Variance(new BigDecimal("45.00"));
        resource.setPeriod12Variance(new BigDecimal("45.00"));
        resource.setUrlId(1L);
        resource.setUserEmail("testUser");

        FairMarketValueRsrc newResource;
        try {
            newResource = fairMarketValueService.createFairMarketValue(resource);
        } catch (ConstraintViolationException | ServiceException | ConflictException e) {
            fail(e.getMessage());
            return;
        }
        fairMarketValueId = newResource.getFairMarketValueId();

        assertThat(newResource.getProgramYear()).isEqualTo(2024);
        assertThat(newResource.getInventoryItemCode()).isEqualTo("5562");
        assertThat(newResource.getInventoryItemDesc()).isEqualTo("Greenfeed");
        assertThat(newResource.getMunicipalityCode()).isEqualTo("43");
        assertThat(newResource.getMunicipalityDesc()).isEqualTo("Mount Waddington (Island part)");
        assertThat(newResource.getCropUnitCode()).isEqualTo("2");
        assertThat(newResource.getCropUnitDesc()).isEqualTo("Tonnes");
        assertThat(newResource.getPeriod01Price()).isEqualByComparingTo(new BigDecimal("216.05"));
        assertThat(newResource.getPeriod02Price()).isEqualByComparingTo(new BigDecimal("216.05"));
        assertThat(newResource.getPeriod03Price()).isEqualByComparingTo(new BigDecimal("216.05"));
        assertThat(newResource.getPeriod04Price()).isEqualByComparingTo(new BigDecimal("198.42"));
        assertThat(newResource.getPeriod05Price()).isEqualByComparingTo(new BigDecimal("198.42"));
        assertThat(newResource.getPeriod06Price()).isEqualByComparingTo(new BigDecimal("198.42"));
        assertThat(newResource.getPeriod07Price()).isEqualByComparingTo(new BigDecimal("176.37"));
        assertThat(newResource.getPeriod08Price()).isEqualByComparingTo(new BigDecimal("176.37"));
        assertThat(newResource.getPeriod09Price()).isEqualByComparingTo(new BigDecimal("176.37"));
        assertThat(newResource.getPeriod10Price()).isEqualByComparingTo(new BigDecimal("190.04"));
        assertThat(newResource.getPeriod11Price()).isEqualByComparingTo(new BigDecimal("190.04"));
        assertThat(newResource.getPeriod12Price()).isEqualByComparingTo(new BigDecimal("190.04"));
        assertThat(newResource.getPeriod01Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod02Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod03Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod04Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod05Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod06Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod07Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod08Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod09Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod10Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod11Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getPeriod12Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(newResource.getUrlId()).isEqualTo(1);
        assertThat(newResource.getUrl()).isEqualTo("https://google.com");
    }

    @Test
    @Order(2)
    public void testGetFairMarketValuesByProgramYear() {
        FairMarketValueListRsrc resources = fairMarketValueService.getFairMarketValuesByProgramYear(2024);
        assertThat(resources).isNotNull();
        assertThat(resources.getFairMarketValueList()).isNotEmpty();
        assertThat(resources.getFairMarketValueList().size()).isEqualTo(1);

        FairMarketValueRsrc resource = resources.getFairMarketValueList().iterator().next();
        assertThat(resource.getProgramYear()).isEqualTo(2024);
        assertThat(resource.getInventoryItemCode()).isEqualTo("5562");
        assertThat(resource.getInventoryItemDesc()).isEqualTo("Greenfeed");
        assertThat(resource.getMunicipalityCode()).isEqualTo("43");
        assertThat(resource.getMunicipalityDesc()).isEqualTo("Mount Waddington (Island part)");
        assertThat(resource.getCropUnitCode()).isEqualTo("2");
        assertThat(resource.getCropUnitDesc()).isEqualTo("Tonnes");
        assertThat(resource.getPeriod01Price()).isEqualByComparingTo(new BigDecimal("216.05"));
        assertThat(resource.getPeriod02Price()).isEqualByComparingTo(new BigDecimal("216.05"));
        assertThat(resource.getPeriod03Price()).isEqualByComparingTo(new BigDecimal("216.05"));
        assertThat(resource.getPeriod04Price()).isEqualByComparingTo(new BigDecimal("198.42"));
        assertThat(resource.getPeriod05Price()).isEqualByComparingTo(new BigDecimal("198.42"));
        assertThat(resource.getPeriod06Price()).isEqualByComparingTo(new BigDecimal("198.42"));
        assertThat(resource.getPeriod07Price()).isEqualByComparingTo(new BigDecimal("176.37"));
        assertThat(resource.getPeriod08Price()).isEqualByComparingTo(new BigDecimal("176.37"));
        assertThat(resource.getPeriod09Price()).isEqualByComparingTo(new BigDecimal("176.37"));
        assertThat(resource.getPeriod10Price()).isEqualByComparingTo(new BigDecimal("190.04"));
        assertThat(resource.getPeriod11Price()).isEqualByComparingTo(new BigDecimal("190.04"));
        assertThat(resource.getPeriod12Price()).isEqualByComparingTo(new BigDecimal("190.04"));
        assertThat(resource.getPeriod01Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod02Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod03Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod04Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod05Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod06Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod07Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod08Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod09Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod10Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod11Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getPeriod12Variance()).isEqualByComparingTo(new BigDecimal("45.00"));
        assertThat(resource.getUrlId()).isEqualTo(1);
        assertThat(resource.getUrl()).isEqualTo("https://google.com");
    }

    @Test
    @Order(3)
    public void testUpdateFairMarketValue() {
        FairMarketValueRsrc resource = null;
        try {
            resource = fairMarketValueService.getFairMarketValue(fairMarketValueId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setProgramYear(2024);
        resource.setInventoryItemCode("5562");
        resource.setMunicipalityCode("43");
        resource.setCropUnitCode("2");
        resource.setPeriod01Price(new BigDecimal("1216.05"));
        resource.setPeriod02Price(new BigDecimal("1216.05"));
        resource.setPeriod03Price(new BigDecimal("1216.05"));
        resource.setPeriod04Price(new BigDecimal("1198.42"));
        resource.setPeriod05Price(new BigDecimal("1198.42"));
        resource.setPeriod06Price(new BigDecimal("1198.42"));
        resource.setPeriod07Price(new BigDecimal("1176.37"));
        resource.setPeriod08Price(new BigDecimal("1176.37"));
        resource.setPeriod09Price(new BigDecimal("1176.37"));
        resource.setPeriod10Price(new BigDecimal("1190.04"));
        resource.setPeriod11Price(new BigDecimal("1190.04"));
        resource.setPeriod12Price(new BigDecimal("1190.04"));
        resource.setPeriod01Variance(new BigDecimal("55.00"));
        resource.setPeriod02Variance(new BigDecimal("55.00"));
        resource.setPeriod03Variance(new BigDecimal("55.00"));
        resource.setPeriod04Variance(new BigDecimal("55.00"));
        resource.setPeriod05Variance(new BigDecimal("55.00"));
        resource.setPeriod06Variance(new BigDecimal("55.00"));
        resource.setPeriod07Variance(new BigDecimal("55.00"));
        resource.setPeriod08Variance(new BigDecimal("55.00"));
        resource.setPeriod09Variance(new BigDecimal("55.00"));
        resource.setPeriod10Variance(new BigDecimal("55.00"));
        resource.setPeriod11Variance(new BigDecimal("55.00"));
        resource.setPeriod12Variance(new BigDecimal("55.00"));
        resource.setUrlId(2L);
        resource.setUserEmail("testUser");

        FairMarketValueRsrc updatedResource = null;
        try {
            updatedResource = fairMarketValueService.updateFairMarketValue(fairMarketValueId, resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getProgramYear()).isEqualTo(2024);
        assertThat(updatedResource.getInventoryItemCode()).isEqualTo("5562");
        assertThat(updatedResource.getInventoryItemDesc()).isEqualTo("Greenfeed");
        assertThat(updatedResource.getMunicipalityCode()).isEqualTo("43");
        assertThat(updatedResource.getMunicipalityDesc()).isEqualTo("Mount Waddington (Island part)");
        assertThat(updatedResource.getCropUnitCode()).isEqualTo("2");
        assertThat(updatedResource.getCropUnitDesc()).isEqualTo("Tonnes");
        assertThat(updatedResource.getPeriod01Price()).isEqualByComparingTo(new BigDecimal("1216.05"));
        assertThat(updatedResource.getPeriod02Price()).isEqualByComparingTo(new BigDecimal("1216.05"));
        assertThat(updatedResource.getPeriod03Price()).isEqualByComparingTo(new BigDecimal("1216.05"));
        assertThat(updatedResource.getPeriod04Price()).isEqualByComparingTo(new BigDecimal("1198.42"));
        assertThat(updatedResource.getPeriod05Price()).isEqualByComparingTo(new BigDecimal("1198.42"));
        assertThat(updatedResource.getPeriod06Price()).isEqualByComparingTo(new BigDecimal("1198.42"));
        assertThat(updatedResource.getPeriod07Price()).isEqualByComparingTo(new BigDecimal("1176.37"));
        assertThat(updatedResource.getPeriod08Price()).isEqualByComparingTo(new BigDecimal("1176.37"));
        assertThat(updatedResource.getPeriod09Price()).isEqualByComparingTo(new BigDecimal("1176.37"));
        assertThat(updatedResource.getPeriod10Price()).isEqualByComparingTo(new BigDecimal("1190.04"));
        assertThat(updatedResource.getPeriod11Price()).isEqualByComparingTo(new BigDecimal("1190.04"));
        assertThat(updatedResource.getPeriod12Price()).isEqualByComparingTo(new BigDecimal("1190.04"));
        assertThat(updatedResource.getPeriod01Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod02Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod03Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod04Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod05Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod06Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod07Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod08Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod09Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod10Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod11Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getPeriod12Variance()).isEqualByComparingTo(new BigDecimal("55.00"));
        assertThat(updatedResource.getUrlId()).isEqualTo(2);
        assertThat(updatedResource.getUrl()).isEqualTo("https://microsoft.com");
    }

    @Test
    @Order(4)
    public void testDeleteFairMarketValue() {
        assertThatNoException().isThrownBy(() -> {
            fairMarketValueService.deleteFairMarketValue(fairMarketValueId);
        });
    }
}
