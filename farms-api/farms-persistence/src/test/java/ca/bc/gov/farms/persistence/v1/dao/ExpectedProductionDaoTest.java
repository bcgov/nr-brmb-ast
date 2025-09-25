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
import ca.bc.gov.farms.persistence.v1.dto.ExpectedProductionDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ExpectedProductionDaoTest {

    @Autowired
    private ExpectedProductionDao expectedProductionDao;

    private static Long expectedProductionId;

    @Test
    @Order(1)
    public void testInsert() {
        ExpectedProductionDto dto = new ExpectedProductionDto();
        dto.setExpectedProductionPerProdUnit(new BigDecimal("0.907"));
        dto.setInventoryItemCode("73");
        dto.setCropUnitCode("1");

        ExpectedProductionDto result = null;
        try {
            expectedProductionDao.insert(dto, "testUser");
            expectedProductionId = dto.getExpectedProductionId();
            result = expectedProductionDao.fetch(expectedProductionId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getExpectedProductionPerProdUnit()).isEqualTo(new BigDecimal("0.907"));
        assertThat(result.getInventoryItemCode()).isEqualTo("73");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(result.getCropUnitCode()).isEqualTo("1");
        assertThat(result.getCropUnitDesc()).isEqualTo("Pounds");
    }

    @Test
    @Order(2)
    public void testFetchByInventoryItemCode() {
        ExpectedProductionDto dto = null;
        try {
            dto = expectedProductionDao.fetchByInventoryItemCode("73");
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(dto).isNotNull();
        assertThat(dto.getExpectedProductionPerProdUnit()).isEqualTo(new BigDecimal("0.907"));
        assertThat(dto.getInventoryItemCode()).isEqualTo("73");
        assertThat(dto.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(dto.getCropUnitCode()).isEqualTo("1");
        assertThat(dto.getCropUnitDesc()).isEqualTo("Pounds");
    }

    @Test
    @Order(3)
    public void testUpdate() {
        ExpectedProductionDto dto = null;
        try {
            dto = expectedProductionDao.fetch(expectedProductionId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setExpectedProductionPerProdUnit(new BigDecimal("5113.000"));

        ExpectedProductionDto result = null;
        try {
            expectedProductionDao.update(dto, "testUser");
            result = expectedProductionDao.fetch(expectedProductionId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getExpectedProductionPerProdUnit()).isEqualTo(new BigDecimal("5113.000"));
        assertThat(result.getInventoryItemCode()).isEqualTo("73");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(result.getCropUnitCode()).isEqualTo("1");
        assertThat(result.getCropUnitDesc()).isEqualTo("Pounds");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            expectedProductionDao.delete(expectedProductionId);
        });
    }
}
