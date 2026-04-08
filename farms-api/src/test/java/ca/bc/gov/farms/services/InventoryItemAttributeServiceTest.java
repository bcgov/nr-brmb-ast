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
import ca.bc.gov.farms.data.models.InventoryItemAttributeModel;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InventoryItemAttributeServiceTest {

    @Autowired
    private InventoryItemAttributeService inventoryItemAttributeService;

    private static Long inventoryItemAttributeId;

    @Test
    @Order(1)
    public void testCreateInventoryItemAttribute() {
        InventoryItemAttributeModel resource = new InventoryItemAttributeModel();
        resource.setInventoryItemCode("73");
        resource.setRollupInventoryItemCode("73");
        resource.setUserEmail("testUser");

        InventoryItemAttributeModel newResource = inventoryItemAttributeService.createInventoryItemAttribute(resource);
        inventoryItemAttributeId = newResource.getInventoryItemAttributeId();

        assertThat(newResource.getInventoryItemCode()).isEqualTo("73");
        assertThat(newResource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(newResource.getRollupInventoryItemCode()).isEqualTo("73");
        assertThat(newResource.getRollupInventoryItemDesc()).isEqualTo("Strawberries");
    }

    @Test
    @Order(2)
    public void testGetInventoryItemAttributeByInventoryItemCode() {
        InventoryItemAttributeModel resource = null;
        try {
            resource = inventoryItemAttributeService.getInventoryItemAttributeByInventoryItemCode("73");
        } catch (ServiceException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(resource).isNotNull();
        assertThat(resource.getInventoryItemCode()).isEqualTo("73");
        assertThat(resource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(resource.getRollupInventoryItemCode()).isEqualTo("73");
        assertThat(resource.getRollupInventoryItemDesc()).isEqualTo("Strawberries");
    }

    @Test
    @Order(3)
    public void testUpdateInventoryItemAttribute() {
        InventoryItemAttributeModel resource = null;
        try {
            resource = inventoryItemAttributeService.getInventoryItemAttribute(inventoryItemAttributeId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setInventoryItemCode("73");
        resource.setRollupInventoryItemCode("5560");
        resource.setUserEmail("testUser");

        InventoryItemAttributeModel updatedResource = null;
        try {
            updatedResource = inventoryItemAttributeService.updateInventoryItemAttribute(inventoryItemAttributeId,
                    resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getInventoryItemCode()).isEqualTo("73");
        assertThat(updatedResource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(updatedResource.getRollupInventoryItemCode()).isEqualTo("5560");
        assertThat(updatedResource.getRollupInventoryItemDesc()).isEqualTo("Alfalfa Dehy");
    }

    @Test
    @Order(4)
    public void testDeleteInventoryItemAttribute() {
        assertThatNoException().isThrownBy(() -> {
            inventoryItemAttributeService.deleteInventoryItemAttribute(inventoryItemAttributeId);
        });
    }
}
