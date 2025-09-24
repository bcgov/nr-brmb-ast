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

    private static Long cropUnitConversionFactorId;

    @Test
    @Order(1)
    public void testInsert() {
        CropUnitConversionDto dto = new CropUnitConversionDto();
        dto.setInventoryItemCode("73");
        dto.setCropUnitCode("1");
        dto.setConversionFactor(new BigDecimal("1.2345"));
        dto.setTargetCropUnitCode("2");

        CropUnitConversionDto result = null;
        try {
            cropUnitConversionDao.insert(dto, "testUser");
            cropUnitConversionFactorId = dto.getCropUnitConversionFactorId();
            result = cropUnitConversionDao.fetch(cropUnitConversionFactorId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getInventoryItemCode()).isEqualTo("73");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(result.getCropUnitCode()).isEqualTo("1");
        assertThat(result.getCropUnitDesc()).isEqualTo("Pounds");
        assertThat(result.getConversionFactor()).isEqualByComparingTo(new BigDecimal("1.2345"));
        assertThat(result.getTargetCropUnitCode()).isEqualTo("2");
        assertThat(result.getTargetCropUnitDesc()).isEqualTo("Tonnes");
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
        assertThat(dto.getConversionFactor()).isEqualByComparingTo(new BigDecimal("1.2345"));
        assertThat(dto.getTargetCropUnitCode()).isEqualTo("2");
        assertThat(dto.getTargetCropUnitDesc()).isEqualTo("Tonnes");
    }

    @Test
    @Order(3)
    public void testUpdate() {
        CropUnitConversionDto dto = null;
        try {
            dto = cropUnitConversionDao.fetch(cropUnitConversionFactorId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setInventoryItemCode("73");
        dto.setCropUnitCode("2");
        dto.setConversionFactor(new BigDecimal("5.4321"));
        dto.setTargetCropUnitCode("1");

        CropUnitConversionDto result = null;
        try {
            cropUnitConversionDao.update(dto, "testUser");
            result = cropUnitConversionDao.fetch(cropUnitConversionFactorId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getInventoryItemCode()).isEqualTo("73");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(result.getCropUnitCode()).isEqualTo("2");
        assertThat(result.getCropUnitDesc()).isEqualTo("Tonnes");
        assertThat(result.getConversionFactor()).isEqualByComparingTo(new BigDecimal("5.4321"));
        assertThat(result.getTargetCropUnitCode()).isEqualTo("1");
        assertThat(result.getTargetCropUnitDesc()).isEqualTo("Pounds");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            cropUnitConversionDao.delete(cropUnitConversionFactorId);
        });
    }
}
