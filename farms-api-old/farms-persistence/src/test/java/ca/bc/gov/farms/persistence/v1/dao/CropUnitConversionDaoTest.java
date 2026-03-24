package ca.bc.gov.farms.persistence.v1.dao;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import ca.bc.gov.brmb.common.persistence.dao.DaoException;
import ca.bc.gov.farms.api.rest.v1.spring.EndpointsSpringConfigTest;
import ca.bc.gov.farms.persistence.v1.dto.ConversionUnitDto;
import ca.bc.gov.farms.persistence.v1.dto.CropUnitConversionDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
import java.util.List;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CropUnitConversionDaoTest {

    @Autowired
    private CropUnitConversionDao cropUnitConversionDao;

    private static Long cropUnitDefaultId;

    @Test
    @Order(1)
    public void testInsert() {
        CropUnitConversionDto dto = new CropUnitConversionDto();
        dto.setInventoryItemCode("73");
        dto.setCropUnitCode("1");
        ConversionUnitDto conversionUnitDto = new ConversionUnitDto();
        conversionUnitDto.setConversionFactor(new BigDecimal("1.2345"));
        conversionUnitDto.setTargetCropUnitCode("2");
        dto.getConversionUnits().add(conversionUnitDto);

        CropUnitConversionDto result = null;
        try {
            cropUnitConversionDao.insert(dto, "testUser");
            cropUnitDefaultId = dto.getCropUnitDefaultId();
            result = cropUnitConversionDao.fetch(cropUnitDefaultId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getInventoryItemCode()).isEqualTo("73");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(result.getCropUnitCode()).isEqualTo("1");
        assertThat(result.getCropUnitDesc()).isEqualTo("Pounds");
        assertThat(result.getConversionUnits().get(0).getConversionFactor()).isEqualByComparingTo(new BigDecimal("1.2345"));
        assertThat(result.getConversionUnits().get(0).getTargetCropUnitCode()).isEqualTo("2");
        assertThat(result.getConversionUnits().get(0).getTargetCropUnitDesc()).isEqualTo("Tonnes");
    }

    @Test
    @Order(2)
    public void testFetchByInventoryItemCode() {
        List<CropUnitConversionDto> dtos = null;
        try {
            dtos = cropUnitConversionDao.fetchByInventoryItemCode("73");
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dtos).isNotNull();
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isEqualTo(1);

        CropUnitConversionDto dto = dtos.iterator().next();
        assertThat(dto.getInventoryItemCode()).isEqualTo("73");
        assertThat(dto.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(dto.getCropUnitCode()).isEqualTo("1");
        assertThat(dto.getCropUnitDesc()).isEqualTo("Pounds");
        assertThat(dto.getConversionUnits().get(0).getConversionFactor())
                .isEqualByComparingTo(new BigDecimal("1.2345"));
        assertThat(dto.getConversionUnits().get(0).getTargetCropUnitCode()).isEqualTo("2");
        assertThat(dto.getConversionUnits().get(0).getTargetCropUnitDesc()).isEqualTo("Tonnes");
    }

    @Test
    @Order(3)
    public void testUpdate() {
        CropUnitConversionDto dto = null;
        try {
            dto = cropUnitConversionDao.fetch(cropUnitDefaultId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setInventoryItemCode("73");
        dto.setCropUnitCode("2");
        dto.getConversionUnits().get(0).setConversionFactor(new BigDecimal("5.4321"));
        dto.getConversionUnits().get(0).setTargetCropUnitCode("1");

        CropUnitConversionDto result = null;
        try {
            cropUnitConversionDao.update(dto, "testUser");
            result = cropUnitConversionDao.fetch(cropUnitDefaultId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getInventoryItemCode()).isEqualTo("73");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(result.getCropUnitCode()).isEqualTo("2");
        assertThat(result.getCropUnitDesc()).isEqualTo("Tonnes");
        assertThat(result.getConversionUnits().get(0).getConversionFactor())
                .isEqualByComparingTo(new BigDecimal("5.4321"));
        assertThat(result.getConversionUnits().get(0).getTargetCropUnitCode()).isEqualTo("1");
        assertThat(result.getConversionUnits().get(0).getTargetCropUnitDesc()).isEqualTo("Pounds");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            cropUnitConversionDao.delete(cropUnitDefaultId);
        });
    }
}
