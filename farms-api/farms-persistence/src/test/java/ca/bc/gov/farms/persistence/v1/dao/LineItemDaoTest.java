package ca.bc.gov.farms.persistence.v1.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
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
import ca.bc.gov.farms.persistence.v1.dto.LineItemDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class LineItemDaoTest {

    @Autowired
    private LineItemDao lineItemDao;

    private static Long lineItemId;

    @BeforeEach
    void beforeEach() throws InterruptedException {
        Thread.sleep(1000); // 1 second delay before each test
    }

    @Test
    @Order(1)
    public void testInsert() {
        LineItemDto dto = new LineItemDto();
        dto.setProgramYear(2025);
        dto.setLineItem(9798);
        dto.setDescription("Agricultural Contract work");
        dto.setProvince("BC");
        dto.setEligibilityInd("N");
        dto.setEligibilityForRefYearsInd("N");
        dto.setYardageInd("N");
        dto.setProgramPaymentInd("N");
        dto.setContractWorkInd("N");
        dto.setSupplyManagedCommodityInd("N");
        dto.setExcludeFromRevenueCalcInd("N");
        dto.setIndustryAverageExpenseInd("N");
        dto.setCommodityTypeCode(null);
        dto.setFruitVegTypeCode(null);

        LineItemDto result = null;
        try {
            lineItemDao.insert(dto, "testUser");
            lineItemId = dto.getLineItemId();
            Thread.sleep(1000); // 1 second delay after insert
            result = lineItemDao.fetch(lineItemId);
        } catch (DaoException | InterruptedException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getProgramYear()).isEqualTo(2025);
        assertThat(result.getLineItem()).isEqualTo(9798);
        assertThat(result.getDescription()).isEqualTo("Agricultural Contract work");
        assertThat(result.getProvince()).isEqualTo("BC");
        assertThat(result.getEligibilityInd()).isEqualTo("N");
        assertThat(result.getEligibilityForRefYearsInd()).isEqualTo("N");
        assertThat(result.getYardageInd()).isEqualTo("N");
        assertThat(result.getProgramPaymentInd()).isEqualTo("N");
        assertThat(result.getContractWorkInd()).isEqualTo("N");
        assertThat(result.getSupplyManagedCommodityInd()).isEqualTo("N");
        assertThat(result.getExcludeFromRevenueCalcInd()).isEqualTo("N");
        assertThat(result.getIndustryAverageExpenseInd()).isEqualTo("N");
        assertThat(result.getCommodityTypeCode()).isNull();
        assertThat(result.getFruitVegTypeCode()).isNull();
    }

    @Test
    @Order(2)
    public void testFetchByProgramYear() {
        List<LineItemDto> dtos = null;
        try {
            dtos = lineItemDao.fetchByProgramYear(2025);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dtos).isNotNull();
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isEqualTo(1);

        LineItemDto fetchedDto = dtos.get(0);
        assertThat(fetchedDto.getProgramYear()).isEqualTo(2025);
        assertThat(fetchedDto.getLineItem()).isEqualTo(9798);
        assertThat(fetchedDto.getDescription()).isEqualTo("Agricultural Contract work");
        assertThat(fetchedDto.getProvince()).isEqualTo("BC");
        assertThat(fetchedDto.getEligibilityInd()).isEqualTo("N");
        assertThat(fetchedDto.getEligibilityForRefYearsInd()).isEqualTo("N");
        assertThat(fetchedDto.getYardageInd()).isEqualTo("N");
        assertThat(fetchedDto.getProgramPaymentInd()).isEqualTo("N");
        assertThat(fetchedDto.getContractWorkInd()).isEqualTo("N");
        assertThat(fetchedDto.getSupplyManagedCommodityInd()).isEqualTo("N");
        assertThat(fetchedDto.getExcludeFromRevenueCalcInd()).isEqualTo("N");
        assertThat(fetchedDto.getIndustryAverageExpenseInd()).isEqualTo("N");
        assertThat(fetchedDto.getCommodityTypeCode()).isNull();
        assertThat(fetchedDto.getFruitVegTypeCode()).isNull();
    }

    @Test
    @Order(3)
    public void testUpdate() {
        LineItemDto dto = null;
        try {
            dto = lineItemDao.fetch(lineItemId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setDescription("Agricultural Contract works");
        dto.setProvince("AB");
        dto.setEligibilityInd("Y");
        dto.setEligibilityForRefYearsInd("Y");
        dto.setYardageInd("Y");
        dto.setProgramPaymentInd("Y");
        dto.setContractWorkInd("Y");
        dto.setSupplyManagedCommodityInd("Y");
        dto.setExcludeFromRevenueCalcInd("Y");
        dto.setIndustryAverageExpenseInd("Y");
        dto.setCommodityTypeCode("GRAIN");
        dto.setFruitVegTypeCode("APPLE");

        LineItemDto result = null;
        try {
            lineItemDao.update(dto, "testUser");
            result = lineItemDao.fetch(lineItemId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getProgramYear()).isEqualTo(2025);
        assertThat(result.getLineItem()).isEqualTo(9798);
        assertThat(result.getDescription()).isEqualTo("Agricultural Contract works");
        assertThat(result.getProvince()).isEqualTo("AB");
        assertThat(result.getEligibilityInd()).isEqualTo("Y");
        assertThat(result.getEligibilityForRefYearsInd()).isEqualTo("Y");
        assertThat(result.getYardageInd()).isEqualTo("Y");
        assertThat(result.getProgramPaymentInd()).isEqualTo("Y");
        assertThat(result.getContractWorkInd()).isEqualTo("Y");
        assertThat(result.getSupplyManagedCommodityInd()).isEqualTo("Y");
        assertThat(result.getExcludeFromRevenueCalcInd()).isEqualTo("Y");
        assertThat(result.getIndustryAverageExpenseInd()).isEqualTo("Y");
        assertThat(result.getCommodityTypeCode()).isEqualTo("GRAIN");
        assertThat(result.getFruitVegTypeCode()).isEqualTo("APPLE");
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            lineItemDao.delete(lineItemId);
        });
    }
}
