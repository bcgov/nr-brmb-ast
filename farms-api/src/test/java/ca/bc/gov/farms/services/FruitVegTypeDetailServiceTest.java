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
import ca.bc.gov.farms.data.models.FruitVegTypeDetailListRsrc;
import ca.bc.gov.farms.data.models.FruitVegTypeDetailRsrc;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class FruitVegTypeDetailServiceTest {

    @Autowired
    private FruitVegTypeDetailService fruitVegTypeDetailService;

    private static String fruitVegTypeCode;

    @Test
    @Order(1)
    public void testCreateFruitVegTypeDetail() {
        FruitVegTypeDetailRsrc resource = new FruitVegTypeDetailRsrc();
        resource.setFruitVegTypeCode("LYCHEE");
        resource.setFruitVegTypeDesc("Tropical Fruit");
        resource.setRevenueVarianceLimit(new BigDecimal("20.000"));
        resource.setUserEmail("testUser");

        FruitVegTypeDetailRsrc newResource = fruitVegTypeDetailService.createFruitVegTypeDetail(resource);
        fruitVegTypeCode = newResource.getFruitVegTypeCode();

        assertThat(newResource.getFruitVegTypeCode()).isEqualTo("LYCHEE");
        assertThat(newResource.getFruitVegTypeDesc()).isEqualTo("Tropical Fruit");
        assertThat(newResource.getRevenueVarianceLimit()).isEqualTo(new BigDecimal("20.000"));
    }

    @Test
    @Order(2)
    public void testGetAllFruitVegTypeDetails() {
        FruitVegTypeDetailListRsrc resources = fruitVegTypeDetailService.getAllFruitVegTypeDetails();
        assertThat(resources).isNotNull();
        assertThat(resources.getFruitVegTypeDetailList()).isNotEmpty();
        assertThat(resources.getFruitVegTypeDetailList().size()).isEqualTo(2);

        FruitVegTypeDetailRsrc resource = resources.getFruitVegTypeDetailList().get(1);
        assertThat(resource.getFruitVegTypeCode()).isEqualTo("LYCHEE");
        assertThat(resource.getFruitVegTypeDesc()).isEqualTo("Tropical Fruit");
        assertThat(resource.getRevenueVarianceLimit()).isEqualTo(new BigDecimal("20.000"));
    }

    @Test
    @Order(3)
    public void testUpdateFruitVegTypeDetail() {
        FruitVegTypeDetailRsrc resource = null;
        try {
            resource = fruitVegTypeDetailService.getFruitVegTypeDetail(fruitVegTypeCode);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setFruitVegTypeDesc("King of Fruits");
        resource.setRevenueVarianceLimit(new BigDecimal("30.000"));
        resource.setUserEmail("testUser");

        FruitVegTypeDetailRsrc updatedResource = null;
        try {
            updatedResource = fruitVegTypeDetailService.updateFruitVegTypeDetail(fruitVegTypeCode, resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getFruitVegTypeCode()).isEqualTo("LYCHEE");
        assertThat(updatedResource.getFruitVegTypeDesc()).isEqualTo("King of Fruits");
        assertThat(updatedResource.getRevenueVarianceLimit()).isEqualTo(new BigDecimal("30.000"));
    }

    @Test
    @Order(4)
    public void testDeleteFruitVegTypeDetail() {
        assertThatNoException().isThrownBy(() -> {
            fruitVegTypeDetailService.deleteFruitVegTypeDetail(fruitVegTypeCode);
        });
    }
}
