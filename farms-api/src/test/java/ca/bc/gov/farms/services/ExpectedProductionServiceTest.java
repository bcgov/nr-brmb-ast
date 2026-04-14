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

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ServiceException;
import ca.bc.gov.farms.data.models.ExpectedProductionListRsrc;
import ca.bc.gov.farms.data.models.ExpectedProductionRsrc;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExpectedProductionServiceTest {

    @Autowired
    private ExpectedProductionService expectedProductionService;

    private static Long expectedProductionId;

    @Test
    @Order(1)
    public void testCreateExpectedProduction() {
        ExpectedProductionRsrc resource = new ExpectedProductionRsrc();
        resource.setExpectedProductionPerProdUnit(new BigDecimal("0.907"));
        resource.setInventoryItemCode("73");
        resource.setCropUnitCode("1");
        resource.setUserEmail("testUser");

        ExpectedProductionRsrc newResource = expectedProductionService.createExpectedProduction(resource);
        expectedProductionId = newResource.getExpectedProductionId();

        assertThat(newResource.getExpectedProductionPerProdUnit()).isEqualTo(new BigDecimal("0.907"));
        assertThat(newResource.getInventoryItemCode()).isEqualTo("73");
        assertThat(newResource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(newResource.getCropUnitCode()).isEqualTo("1");
        assertThat(newResource.getCropUnitDesc()).isEqualTo("Pounds");
    }

    @Test
    @Order(2)
    public void testGetAllExpectedProductions() {
        ExpectedProductionListRsrc resources = expectedProductionService.getAllExpectedProductions();
        assertThat(resources).isNotNull();
        assertThat(resources.getExpectedProductionList()).isNotEmpty();
        assertThat(resources.getExpectedProductionList().size()).isEqualTo(1);

        ExpectedProductionRsrc resource = resources.getExpectedProductionList().iterator().next();
        assertThat(resource.getExpectedProductionPerProdUnit()).isEqualTo(new BigDecimal("0.907"));
        assertThat(resource.getInventoryItemCode()).isEqualTo("73");
        assertThat(resource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(resource.getCropUnitCode()).isEqualTo("1");
        assertThat(resource.getCropUnitDesc()).isEqualTo("Pounds");
    }

    @Test
    @Order(3)
    public void testUpdateExpectedProduction() {
        ExpectedProductionRsrc resource = null;
        try {
            resource = expectedProductionService.getExpectedProduction(expectedProductionId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setExpectedProductionPerProdUnit(new BigDecimal("5113.000"));
        resource.setUserEmail("testUser");

        ExpectedProductionRsrc updatedResource = null;
        try {
            updatedResource = expectedProductionService.updateExpectedProduction(expectedProductionId, resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getExpectedProductionPerProdUnit()).isEqualTo(new BigDecimal("5113.000"));
        assertThat(updatedResource.getInventoryItemCode()).isEqualTo("73");
        assertThat(updatedResource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(updatedResource.getCropUnitCode()).isEqualTo("1");
        assertThat(updatedResource.getCropUnitDesc()).isEqualTo("Pounds");
    }

    @Test
    @Order(4)
    public void testDeleteExpectedProduction() {
        assertThatNoException().isThrownBy(() -> {
            expectedProductionService.deleteExpectedProduction(expectedProductionId);
        });
    }
}
