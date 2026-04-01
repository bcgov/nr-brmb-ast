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
import ca.bc.gov.farms.data.models.InventoryTypeXrefListModel;
import ca.bc.gov.farms.data.models.InventoryTypeXrefModel;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InventoryTypeXrefServiceTest {

    @Autowired
    private InventoryTypeXrefService inventoryTypeXrefService;

    private static Long agristabilityCommodityXrefId;

    @Test
    @Order(1)
    public void testCreateInventoryTypeXref() {
        InventoryTypeXrefModel resource = new InventoryTypeXrefModel();
        resource.setMarketCommodityInd("Y");
        resource.setInventoryItemCode("73");
        resource.setInventoryGroupCode("3");
        resource.setInventoryClassCode("4");
        resource.setUserEmail("testUser");

        InventoryTypeXrefModel newResource = inventoryTypeXrefService.createInventoryTypeXref(resource);
        agristabilityCommodityXrefId = newResource.getAgristabilityCommodityXrefId();

        assertThat(newResource.getMarketCommodityInd()).isEqualTo("Y");
        assertThat(newResource.getInventoryItemCode()).isEqualTo("73");
        assertThat(newResource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(newResource.getInventoryGroupCode()).isEqualTo("3");
        assertThat(newResource.getInventoryGroupDesc()).isEqualTo("Berries");
        assertThat(newResource.getInventoryClassCode()).isEqualTo("4");
        assertThat(newResource.getInventoryClassDesc()).isEqualTo("Deferred Income and Receivables");
    }

    @Test
    @Order(2)
    public void testGetInventoryTypeXrefsByInventoryClassCode() {
        InventoryTypeXrefListModel resources = inventoryTypeXrefService.getInventoryTypeXrefsByInventoryClassCode("4");
        assertThat(resources).isNotNull();
        assertThat(resources.getInventoryTypeXrefList()).isNotEmpty();
        assertThat(resources.getInventoryTypeXrefList().size()).isEqualTo(1);

        InventoryTypeXrefModel resource = resources.getInventoryTypeXrefList().iterator().next();
        assertThat(resource.getMarketCommodityInd()).isEqualTo("Y");
        assertThat(resource.getInventoryItemCode()).isEqualTo("73");
        assertThat(resource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(resource.getInventoryGroupCode()).isEqualTo("3");
        assertThat(resource.getInventoryGroupDesc()).isEqualTo("Berries");
        assertThat(resource.getInventoryClassCode()).isEqualTo("4");
        assertThat(resource.getInventoryClassDesc()).isEqualTo("Deferred Income and Receivables");
    }

    @Test
    @Order(3)
    public void testUpdateInventoryTypeXref() {
        InventoryTypeXrefModel resource = null;
        try {
            resource = inventoryTypeXrefService.getInventoryTypeXref(agristabilityCommodityXrefId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setMarketCommodityInd("N");
        resource.setInventoryItemCode("5560");
        resource.setInventoryGroupCode("4");
        resource.setInventoryClassCode("5");
        resource.setUserEmail("testUser");

        InventoryTypeXrefModel updatedResource = null;
        try {
            updatedResource = inventoryTypeXrefService.updateInventoryTypeXref(agristabilityCommodityXrefId, resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getMarketCommodityInd()).isEqualTo("N");
        assertThat(updatedResource.getInventoryItemCode()).isEqualTo("5560");
        assertThat(updatedResource.getInventoryItemDesc()).isEqualTo("Alfalfa Dehy");
        assertThat(updatedResource.getInventoryGroupCode()).isEqualTo("4");
        assertThat(updatedResource.getInventoryGroupDesc()).isEqualTo("Buckwheat");
        assertThat(updatedResource.getInventoryClassCode()).isEqualTo("5");
        assertThat(updatedResource.getInventoryClassDesc()).isEqualTo("Accounts Payable");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            inventoryTypeXrefService.deleteInventoryTypeXref(agristabilityCommodityXrefId);
        });
    }
}
