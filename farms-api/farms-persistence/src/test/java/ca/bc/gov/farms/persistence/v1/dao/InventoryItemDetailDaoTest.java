package ca.bc.gov.farms.persistence.v1.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;

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
import ca.bc.gov.farms.persistence.v1.dto.InventoryItemDetailDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class InventoryItemDetailDaoTest {

    @Autowired
    private InventoryItemDetailDao inventoryItemDetailDao;

    private static Long inventoryItemDetailId;

    @Test
    @Order(1)
    public void testInsert() {
        InventoryItemDetailDto dto = new InventoryItemDetailDto();
        dto.setProgramYear(2024);
        dto.setEligibilityInd("N");
        dto.setLineItem(null);
        dto.setInsurableValue(new BigDecimal("24000.000"));
        dto.setPremiumRate(new BigDecimal("0.0464"));
        dto.setInventoryItemCode("7208");
        dto.setCommodityTypeCode(null);
        dto.setFruitVegTypeCode(null);
        dto.setMultiStageCommdtyCode(null);

        InventoryItemDetailDto result = null;
        try {
            inventoryItemDetailDao.insert(dto, "testUser");
            inventoryItemDetailId = dto.getInventoryItemDetailId();
            result = inventoryItemDetailDao.fetch(inventoryItemDetailId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getProgramYear()).isEqualTo(2024);
        assertThat(result.getEligibilityInd()).isEqualTo("N");
        assertThat(result.getLineItem()).isNull();
        assertThat(result.getInsurableValue()).isEqualTo(new BigDecimal("24000.000"));
        assertThat(result.getPremiumRate()).isEqualTo(new BigDecimal("0.0464"));
        assertThat(result.getInventoryItemCode()).isEqualTo("7208");
        assertThat(result.getInventoryItemDesc()).isEqualTo("Daffodils; Fresh cut");
        assertThat(result.getCommodityTypeCode()).isNull();
        assertThat(result.getCommodityTypeDesc()).isNull();
        assertThat(result.getFruitVegTypeCode()).isNull();
        assertThat(result.getFruitVegTypeDesc()).isNull();
        assertThat(result.getMultiStageCommdtyCode()).isNull();
        assertThat(result.getMultiStageCommdtyDesc()).isNull();
    }
}
