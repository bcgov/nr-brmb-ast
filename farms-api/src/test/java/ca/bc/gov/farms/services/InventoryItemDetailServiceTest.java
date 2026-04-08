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
import ca.bc.gov.farms.data.models.InventoryItemDetailListModel;
import ca.bc.gov.farms.data.models.InventoryItemDetailModel;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class InventoryItemDetailServiceTest {

    @Autowired
    private InventoryItemDetailService inventoryItemDetailService;

    private static Long inventoryItemDetailId;

    @Test
    @Order(1)
    public void testCreateInventoryItemDetail() {
        InventoryItemDetailModel resource = new InventoryItemDetailModel();
        resource.setProgramYear(2024);
        resource.setEligibilityInd("N");
        resource.setLineItem(null);
        resource.setInsurableValue(new BigDecimal("24000.000"));
        resource.setPremiumRate(new BigDecimal("0.0464"));
        resource.setInventoryItemCode("7208");
        resource.setCommodityTypeCode(null);
        resource.setFruitVegTypeCode(null);
        resource.setMultiStageCommdtyCode(null);
        resource.setUrlId(1L);
        resource.setUserEmail("testUser");

        InventoryItemDetailModel newResource = inventoryItemDetailService.createInventoryItemDetail(resource);
        inventoryItemDetailId = newResource.getInventoryItemDetailId();

        assertThat(newResource.getProgramYear()).isEqualTo(2024);
        assertThat(newResource.getEligibilityInd()).isEqualTo("N");
        assertThat(newResource.getLineItem()).isNull();
        assertThat(newResource.getInsurableValue()).isEqualTo(new BigDecimal("24000.000"));
        assertThat(newResource.getPremiumRate()).isEqualTo(new BigDecimal("0.0464"));
        assertThat(newResource.getInventoryItemCode()).isEqualTo("7208");
        assertThat(newResource.getInventoryItemDesc()).isEqualTo("Daffodils; Fresh cut");
        assertThat(newResource.getCommodityTypeCode()).isNull();
        assertThat(newResource.getCommodityTypeDesc()).isNull();
        assertThat(newResource.getFruitVegTypeCode()).isNull();
        assertThat(newResource.getFruitVegTypeDesc()).isNull();
        assertThat(newResource.getMultiStageCommdtyCode()).isNull();
        assertThat(newResource.getMultiStageCommdtyDesc()).isNull();
        assertThat(newResource.getUrlId()).isEqualTo(1);
        assertThat(newResource.getUrl()).isEqualTo("https://google.com");
    }

    @Test
    @Order(2)
    public void testGetInventoryItemDetailsByInventoryItemCode() {
        InventoryItemDetailListModel resources = inventoryItemDetailService
                .getInventoryItemDetailsByInventoryItemCode("7208");
        assertThat(resources).isNotNull();
        assertThat(resources.getInventoryItemDetailList()).isNotEmpty();
        assertThat(resources.getInventoryItemDetailList().size()).isEqualTo(1);

        InventoryItemDetailModel resource = resources.getInventoryItemDetailList().iterator().next();
        assertThat(resource.getProgramYear()).isEqualTo(2024);
        assertThat(resource.getEligibilityInd()).isEqualTo("N");
        assertThat(resource.getLineItem()).isNull();
        assertThat(resource.getInsurableValue()).isEqualTo(new BigDecimal("24000.000"));
        assertThat(resource.getPremiumRate()).isEqualTo(new BigDecimal("0.0464"));
        assertThat(resource.getInventoryItemCode()).isEqualTo("7208");
        assertThat(resource.getInventoryItemDesc()).isEqualTo("Daffodils; Fresh cut");
        assertThat(resource.getCommodityTypeCode()).isNull();
        assertThat(resource.getCommodityTypeDesc()).isNull();
        assertThat(resource.getFruitVegTypeCode()).isNull();
        assertThat(resource.getFruitVegTypeDesc()).isNull();
        assertThat(resource.getMultiStageCommdtyCode()).isNull();
        assertThat(resource.getMultiStageCommdtyDesc()).isNull();
        assertThat(resource.getUrlId()).isEqualTo(1);
        assertThat(resource.getUrl()).isEqualTo("https://google.com");
    }

    @Test
    @Order(3)
    public void testUpdateInventoryItemDetail() {
        InventoryItemDetailModel resource = null;
        try {
            resource = inventoryItemDetailService.getInventoryItemDetail(inventoryItemDetailId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setProgramYear(2024);
        resource.setEligibilityInd("N");
        resource.setLineItem(null);
        resource.setInsurableValue(new BigDecimal("24000.001"));
        resource.setPremiumRate(new BigDecimal("0.0465"));
        resource.setInventoryItemCode("5562");
        resource.setCommodityTypeCode(null);
        resource.setFruitVegTypeCode(null);
        resource.setMultiStageCommdtyCode(null);
        resource.setUrlId(2L);
        resource.setUserEmail("testUser");

        InventoryItemDetailModel updatedResource = null;
        try {
            updatedResource = inventoryItemDetailService.updateInventoryItemDetail(inventoryItemDetailId, resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getProgramYear()).isEqualTo(2024);
        assertThat(updatedResource.getEligibilityInd()).isEqualTo("N");
        assertThat(updatedResource.getLineItem()).isNull();
        assertThat(updatedResource.getInsurableValue()).isEqualTo(new BigDecimal("24000.001"));
        assertThat(updatedResource.getPremiumRate()).isEqualTo(new BigDecimal("0.0465"));
        assertThat(updatedResource.getInventoryItemCode()).isEqualTo("5562");
        assertThat(updatedResource.getInventoryItemDesc()).isEqualTo("Greenfeed");
        assertThat(updatedResource.getCommodityTypeCode()).isNull();
        assertThat(updatedResource.getCommodityTypeDesc()).isNull();
        assertThat(updatedResource.getFruitVegTypeCode()).isNull();
        assertThat(updatedResource.getFruitVegTypeDesc()).isNull();
        assertThat(updatedResource.getMultiStageCommdtyCode()).isNull();
        assertThat(updatedResource.getMultiStageCommdtyDesc()).isNull();
        assertThat(updatedResource.getUrlId()).isEqualTo(2);
        assertThat(updatedResource.getUrl()).isEqualTo("https://microsoft.com");
    }

    @Test
    @Order(4)
    public void testDeleteInventoryItemDetail() {
        assertThatNoException().isThrownBy(() -> {
            inventoryItemDetailService.deleteInventoryItemDetail(inventoryItemDetailId);
        });
    }
}
