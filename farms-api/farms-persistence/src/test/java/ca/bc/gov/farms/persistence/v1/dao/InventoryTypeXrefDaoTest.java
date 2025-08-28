package ca.bc.gov.farms.persistence.v1.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

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
import ca.bc.gov.farms.persistence.v1.dto.InventoryTypeXrefDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryTypeXrefDaoTest {

    @Autowired
    private InventoryTypeXrefDao inventoryTypeXrefDao;

    private static Long agristabilityCommodityXrefId;

    @Test
    @Order(1)
    public void testInsert() {
        InventoryTypeXrefDto dto = new InventoryTypeXrefDto();
        dto.setMarketCommodityInd("Y");
        dto.setInventoryItemCode("73");
        dto.setInventoryGroupCode("3");
        dto.setInventoryClassCode("4");

        InventoryTypeXrefDto result = null;
        try {
            inventoryTypeXrefDao.insert(dto, "testUser");
            agristabilityCommodityXrefId = dto.getAgristabilityCommodityXrefId();
            result = inventoryTypeXrefDao.fetch(agristabilityCommodityXrefId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getMarketCommodityInd()).isEqualTo("Y");
        assertThat(result.getInventoryItemCode()).isEqualTo("73");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(result.getInventoryGroupCode()).isEqualTo("3");
        assertThat(result.getInventoryGroupDesc()).isEqualTo("Berries");
        assertThat(result.getInventoryClassCode()).isEqualTo("4");
        assertThat(result.getInventoryClassDesc()).isEqualTo("Deferred Income and Receivables");
    }

    @Test
    @Order(2)
    public void testFetchByInventoryClassCode() {
        List<InventoryTypeXrefDto> dtos = null;
        try {
            dtos = inventoryTypeXrefDao.fetchByInventoryClassCode("4");
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dtos).isNotNull();
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isEqualTo(1);

        InventoryTypeXrefDto fetchedDto = dtos.get(0);
        assertThat(fetchedDto.getMarketCommodityInd()).isEqualTo("Y");
        assertThat(fetchedDto.getInventoryItemCode()).isEqualTo("73");
        assertThat(fetchedDto.getInventoryItemDesc()).isEqualTo("Strawberries");
        assertThat(fetchedDto.getInventoryGroupCode()).isEqualTo("3");
        assertThat(fetchedDto.getInventoryGroupDesc()).isEqualTo("Berries");
        assertThat(fetchedDto.getInventoryClassCode()).isEqualTo("4");
        assertThat(fetchedDto.getInventoryClassDesc()).isEqualTo("Deferred Income and Receivables");
    }

    @Test
    @Order(3)
    public void testUpdate() {
        InventoryTypeXrefDto dto = null;
        try {
            dto = inventoryTypeXrefDao.fetch(agristabilityCommodityXrefId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setMarketCommodityInd("N");
        dto.setInventoryItemCode("5560");
        dto.setInventoryGroupCode("4");
        dto.setInventoryClassCode("5");

        InventoryTypeXrefDto result = null;
        try {
            inventoryTypeXrefDao.update(dto, "testUser");
            result = inventoryTypeXrefDao.fetch(agristabilityCommodityXrefId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getMarketCommodityInd()).isEqualTo("N");
        assertThat(result.getInventoryItemCode()).isEqualTo("5560");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Alfalfa Dehy");
        assertThat(result.getInventoryGroupCode()).isEqualTo("4");
        assertThat(result.getInventoryGroupDesc()).isEqualTo("Buckwheat");
        assertThat(result.getInventoryClassCode()).isEqualTo("5");
        assertThat(result.getInventoryClassDesc()).isEqualTo("Accounts Payable");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            inventoryTypeXrefDao.delete(agristabilityCommodityXrefId);
        });
    }
}
