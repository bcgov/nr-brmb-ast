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
import ca.bc.gov.farms.data.models.LineItemListRsrc;
import ca.bc.gov.farms.data.models.LineItemModel;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class LineItemServiceTest {

    @Autowired
    private LineItemService lineItemService;

    private static Long lineItemId;

    @Test
    @Order(1)
    public void testCreateLineItem() {
        LineItemModel resource = new LineItemModel();
        resource.setProgramYear(2025);
        resource.setLineItem(9798);
        resource.setDescription("Agricultural Contract work");
        resource.setProvince("BC");
        resource.setEligibilityInd("N");
        resource.setEligibilityForRefYearsInd("N");
        resource.setYardageInd("N");
        resource.setProgramPaymentInd("N");
        resource.setContractWorkInd("N");
        resource.setSupplyManagedCommodityInd("N");
        resource.setExcludeFromRevenueCalcInd("N");
        resource.setIndustryAverageExpenseInd("N");
        resource.setCommodityTypeCode(null);
        resource.setFruitVegTypeCode(null);
        resource.setUserEmail("testUser");

        LineItemModel newResource = lineItemService.createLineItem(resource);
        lineItemId = newResource.getLineItemId();

        assertThat(newResource.getProgramYear()).isEqualTo(2025);
        assertThat(newResource.getLineItem()).isEqualTo(9798);
        assertThat(newResource.getDescription()).isEqualTo("Agricultural Contract work");
        assertThat(newResource.getProvince()).isEqualTo("BC");
        assertThat(newResource.getEligibilityInd()).isEqualTo("N");
        assertThat(newResource.getEligibilityForRefYearsInd()).isEqualTo("N");
        assertThat(newResource.getYardageInd()).isEqualTo("N");
        assertThat(newResource.getProgramPaymentInd()).isEqualTo("N");
        assertThat(newResource.getContractWorkInd()).isEqualTo("N");
        assertThat(newResource.getSupplyManagedCommodityInd()).isEqualTo("N");
        assertThat(newResource.getExcludeFromRevenueCalcInd()).isEqualTo("N");
        assertThat(newResource.getIndustryAverageExpenseInd()).isEqualTo("N");
        assertThat(newResource.getCommodityTypeCode()).isNull();
        assertThat(newResource.getFruitVegTypeCode()).isNull();
    }

    @Test
    @Order(2)
    public void testGetLineItemsByProgramYear() {
        LineItemListRsrc resources = lineItemService.getLineItemsByProgramYear(2025);
        assertThat(resources).isNotNull();
        assertThat(resources.getLineItemList()).isNotEmpty();
        assertThat(resources.getLineItemList().size()).isEqualTo(1);

        LineItemModel resource = resources.getLineItemList().iterator().next();
        assertThat(resource.getProgramYear()).isEqualTo(2025);
        assertThat(resource.getLineItem()).isEqualTo(9798);
        assertThat(resource.getDescription()).isEqualTo("Agricultural Contract work");
        assertThat(resource.getProvince()).isEqualTo("BC");
        assertThat(resource.getEligibilityInd()).isEqualTo("N");
        assertThat(resource.getEligibilityForRefYearsInd()).isEqualTo("N");
        assertThat(resource.getYardageInd()).isEqualTo("N");
        assertThat(resource.getProgramPaymentInd()).isEqualTo("N");
        assertThat(resource.getContractWorkInd()).isEqualTo("N");
        assertThat(resource.getSupplyManagedCommodityInd()).isEqualTo("N");
        assertThat(resource.getExcludeFromRevenueCalcInd()).isEqualTo("N");
        assertThat(resource.getIndustryAverageExpenseInd()).isEqualTo("N");
        assertThat(resource.getCommodityTypeCode()).isNull();
        assertThat(resource.getFruitVegTypeCode()).isNull();
    }

    @Test
    @Order(3)
    public void testUpdateLineItem() {
        LineItemModel resource = null;
        try {
            resource = lineItemService.getLineItem(lineItemId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setDescription("Agricultural Contract works");
        resource.setProvince("AB");
        resource.setEligibilityInd("Y");
        resource.setEligibilityForRefYearsInd("Y");
        resource.setYardageInd("Y");
        resource.setProgramPaymentInd("Y");
        resource.setContractWorkInd("Y");
        resource.setSupplyManagedCommodityInd("Y");
        resource.setExcludeFromRevenueCalcInd("Y");
        resource.setIndustryAverageExpenseInd("Y");
        resource.setCommodityTypeCode("GRAIN");
        resource.setFruitVegTypeCode("APPLE");
        resource.setUserEmail("testUser");

        LineItemModel updatedResource = null;
        try {
            updatedResource = lineItemService.updateLineItem(lineItemId, resource);
            lineItemId = updatedResource.getLineItemId();
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getProgramYear()).isEqualTo(2025);
        assertThat(updatedResource.getLineItem()).isEqualTo(9798);
        assertThat(updatedResource.getDescription()).isEqualTo("Agricultural Contract works");
        assertThat(updatedResource.getProvince()).isEqualTo("AB");
        assertThat(updatedResource.getEligibilityInd()).isEqualTo("Y");
        assertThat(updatedResource.getEligibilityForRefYearsInd()).isEqualTo("Y");
        assertThat(updatedResource.getYardageInd()).isEqualTo("Y");
        assertThat(updatedResource.getProgramPaymentInd()).isEqualTo("Y");
        assertThat(updatedResource.getContractWorkInd()).isEqualTo("Y");
        assertThat(updatedResource.getSupplyManagedCommodityInd()).isEqualTo("Y");
        assertThat(updatedResource.getExcludeFromRevenueCalcInd()).isEqualTo("Y");
        assertThat(updatedResource.getIndustryAverageExpenseInd()).isEqualTo("Y");
        assertThat(updatedResource.getCommodityTypeCode()).isEqualTo("GRAIN");
        assertThat(updatedResource.getFruitVegTypeCode()).isEqualTo("APPLE");
    }

    @Test
    @Order(4)
    public void testDeleteLineItem() {
        assertThatNoException().isThrownBy(() -> {
            lineItemService.deleteLineItem(lineItemId);
        });
    }
}
