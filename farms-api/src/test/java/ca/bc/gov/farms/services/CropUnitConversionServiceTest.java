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
import ca.bc.gov.farms.data.models.ConversionUnitRsrc;
import ca.bc.gov.farms.data.models.CropUnitConversionListRsrc;
import ca.bc.gov.farms.data.models.CropUnitConversionRsrc;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CropUnitConversionServiceTest {

    @Autowired
    private CropUnitConversionService cropUnitConversionService;

    private static Long cropUnitDefaultId;

    @Test
    @Order(1)
    public void testCreateCropUnitConversion() {
        CropUnitConversionRsrc resource = new CropUnitConversionRsrc();
        resource.setInventoryItemCode("73");
        resource.setCropUnitCode("1");
        ConversionUnitRsrc conversionUnit = new ConversionUnitRsrc();
        conversionUnit.setConversionFactor(new BigDecimal("1.2345"));
        conversionUnit.setTargetCropUnitCode("2");
        resource.getConversionUnits().add(conversionUnit);
        resource.setUserEmail("testUser");

        CropUnitConversionRsrc newResource = cropUnitConversionService.createCropUnitConversion(resource);
        cropUnitDefaultId = newResource.getCropUnitDefaultId();

        assertThat(newResource.getInventoryItemCode()).isEqualTo("73");
        assertThat(newResource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(newResource.getCropUnitCode()).isEqualTo("1");
        assertThat(newResource.getCropUnitDesc()).isEqualTo("Pounds");
        assertThat(newResource.getConversionUnits().get(0).getConversionFactor())
                .isEqualByComparingTo(new BigDecimal("1.2345"));
        assertThat(newResource.getConversionUnits().get(0).getTargetCropUnitCode()).isEqualTo("2");
        assertThat(newResource.getConversionUnits().get(0).getTargetCropUnitDesc()).isEqualTo("Tonnes");
    }

    @Test
    @Order(2)
    public void testGetCropUnitConversionsByInventoryItemCode() {
        CropUnitConversionListRsrc resources = cropUnitConversionService
                .getCropUnitConversionsByInventoryItemCode("73");
        assertThat(resources).isNotNull();
        assertThat(resources.getCropUnitConversionList()).isNotEmpty();
        assertThat(resources.getCropUnitConversionList().size()).isEqualTo(1);

        CropUnitConversionRsrc resource = resources.getCropUnitConversionList().iterator().next();
        assertThat(resource.getInventoryItemCode()).isEqualTo("73");
        assertThat(resource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(resource.getCropUnitCode()).isEqualTo("1");
        assertThat(resource.getCropUnitDesc()).isEqualTo("Pounds");
        assertThat(resource.getConversionUnits().get(0).getConversionFactor())
                .isEqualByComparingTo(new BigDecimal("1.2345"));
        assertThat(resource.getConversionUnits().get(0).getTargetCropUnitCode()).isEqualTo("2");
        assertThat(resource.getConversionUnits().get(0).getTargetCropUnitDesc()).isEqualTo("Tonnes");
    }

    @Test
    @Order(3)
    public void testUpdateCropUnitConversion() {
        CropUnitConversionRsrc resource;
        try {
            resource = cropUnitConversionService.getCropUnitConversion(cropUnitDefaultId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setInventoryItemCode("73");
        resource.setCropUnitCode("2");
        resource.getConversionUnits().get(0).setConversionFactor(new BigDecimal("5.4321"));
        resource.getConversionUnits().get(0).setTargetCropUnitCode("1");
        resource.setUserEmail("testUser");

        CropUnitConversionRsrc updatedResource;
        try {
            updatedResource = cropUnitConversionService.updateCropUnitConversion(cropUnitDefaultId, resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getInventoryItemCode()).isEqualTo("73");
        assertThat(updatedResource.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(updatedResource.getCropUnitCode()).isEqualTo("2");
        assertThat(updatedResource.getCropUnitDesc()).isEqualTo("Tonnes");
        assertThat(updatedResource.getConversionUnits().get(0).getConversionFactor())
                .isEqualByComparingTo(new BigDecimal("5.4321"));
        assertThat(updatedResource.getConversionUnits().get(0).getTargetCropUnitCode()).isEqualTo("1");
        assertThat(updatedResource.getConversionUnits().get(0).getTargetCropUnitDesc()).isEqualTo("Pounds");
    }

    @Test
    @Order(4)
    public void testDeleteCropUnitConversion() {
        assertThatNoException().isThrownBy(() -> {
            cropUnitConversionService.deleteCropUnitConversion(cropUnitDefaultId);
        });
    }
}
