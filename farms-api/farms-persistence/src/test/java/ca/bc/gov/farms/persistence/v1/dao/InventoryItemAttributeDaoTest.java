package ca.bc.gov.farms.persistence.v1.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

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
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemAttributeDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryItemAttributeDaoTest {

    @Autowired
    private InventoryItemAttributeDao inventoryItemAttributeDao;

    private static Long inventoryItemAttributeId;

    @Test
    @Order(1)
    public void testInsert() {
        InventoryItemAttributeDto dto = new InventoryItemAttributeDto();
        dto.setInventoryItemCode("73");
        dto.setRollupInventoryItemCode("73");

        InventoryItemAttributeDto result = null;
        try {
            inventoryItemAttributeDao.insert(dto, "testUser");
            inventoryItemAttributeId = dto.getInventoryItemAttributeId();
            result = inventoryItemAttributeDao.fetch(inventoryItemAttributeId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getInventoryItemCode()).isEqualTo("73");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(result.getRollupInventoryItemCode()).isEqualTo("73");
        assertThat(result.getRollupInventoryItemDesc()).isEqualTo("Strawberries");
    }

    @Test
    @Order(2)
    public void testFetchByInventoryItemCode() {
        InventoryItemAttributeDto dto = null;
        try {
            dto = inventoryItemAttributeDao.fetchByInventoryItemCode("73");
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(dto).isNotNull();
        assertThat(dto.getInventoryItemCode()).isEqualTo("73");
        assertThat(dto.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(dto.getRollupInventoryItemCode()).isEqualTo("73");
        assertThat(dto.getRollupInventoryItemDesc()).isEqualTo("Strawberries");
    }

    @Test
    @Order(3)
    public void testUpdate() {
        InventoryItemAttributeDto dto = null;
        try {
            dto = inventoryItemAttributeDao.fetch(inventoryItemAttributeId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setInventoryItemCode("73");
        dto.setRollupInventoryItemCode("5560");

        InventoryItemAttributeDto result = null;
        try {
            inventoryItemAttributeDao.update(dto, "testUser");
            result = inventoryItemAttributeDao.fetch(inventoryItemAttributeId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getInventoryItemCode()).isEqualTo("73");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(result.getRollupInventoryItemCode()).isEqualTo("5560");
        assertThat(result.getRollupInventoryItemDesc()).isEqualTo("Alfalfa Dehy");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            inventoryItemAttributeDao.delete(inventoryItemAttributeId);
        });
    }
}
