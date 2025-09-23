package ca.bc.gov.farms.persistence.v1.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.fail;

import java.math.BigDecimal;
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
import ca.bc.gov.farms.persistence.v1.dto.MarketRatePremiumDto;
import ca.bc.gov.farms.persistence.v1.spring.PersistenceSpringConfig;

@ExtendWith(SpringExtension.class)
@ComponentScan(basePackages = "ca.bc.gov.farms")
@ContextConfiguration(classes = { EndpointsSpringConfigTest.class, PersistenceSpringConfig.class })
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MarketRatePremiumDaoTest {

    @Autowired
    private MarketRatePremiumDao marketRatePremiumDao;

    private static Long marketRatePremiumId;

    @Test
    @Order(1)
    public void testInsert() {
        MarketRatePremiumDto dto = new MarketRatePremiumDto();
        dto.setMinTotalPremiumAmount(new BigDecimal("0.00"));
        dto.setMaxTotalPremiumAmount(new BigDecimal("1.00"));
        dto.setRiskChargeFlatAmount(new BigDecimal("2.00"));
        dto.setRiskChargePctPremium(new BigDecimal("3.00"));
        dto.setAdjustChargeFlatAmount(new BigDecimal("4.00"));

        MarketRatePremiumDto result = null;
        try {
            marketRatePremiumDao.insert(dto, "testUser");
            marketRatePremiumId = dto.getMarketRatePremiumId();
            result = marketRatePremiumDao.fetch(marketRatePremiumId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getMinTotalPremiumAmount()).isEqualTo(new BigDecimal("0.00"));
        assertThat(result.getMaxTotalPremiumAmount()).isEqualTo(new BigDecimal("1.00"));
        assertThat(result.getRiskChargeFlatAmount()).isEqualTo(new BigDecimal("2.00"));
        assertThat(result.getRiskChargePctPremium()).isEqualTo(new BigDecimal("3.00"));
        assertThat(result.getAdjustChargeFlatAmount()).isEqualTo(new BigDecimal("4.00"));
    }

    @Test
    @Order(2)
    public void testFetchAll() {
        List<MarketRatePremiumDto> dtos = null;
        try {
            dtos = marketRatePremiumDao.fetchAll();
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dtos).isNotNull();
        assertThat(dtos).isNotEmpty();
        assertThat(dtos.size()).isEqualTo(1);

        MarketRatePremiumDto fetchedDto = dtos.get(0);
        assertThat(fetchedDto.getMinTotalPremiumAmount()).isEqualTo(new BigDecimal("0.00"));
        assertThat(fetchedDto.getMaxTotalPremiumAmount()).isEqualTo(new BigDecimal("1.00"));
        assertThat(fetchedDto.getRiskChargeFlatAmount()).isEqualTo(new BigDecimal("2.00"));
        assertThat(fetchedDto.getRiskChargePctPremium()).isEqualTo(new BigDecimal("3.00"));
        assertThat(fetchedDto.getAdjustChargeFlatAmount()).isEqualTo(new BigDecimal("4.00"));
    }

    @Test
    @Order(3)
    public void testUpdate() {
        MarketRatePremiumDto dto = null;
        try {
            dto = marketRatePremiumDao.fetch(marketRatePremiumId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(dto).isNotNull();

        dto.setMinTotalPremiumAmount(new BigDecimal("1.00"));
        dto.setMaxTotalPremiumAmount(new BigDecimal("2.00"));
        dto.setRiskChargeFlatAmount(new BigDecimal("3.00"));
        dto.setRiskChargePctPremium(new BigDecimal("4.00"));
        dto.setAdjustChargeFlatAmount(new BigDecimal("5.00"));

        MarketRatePremiumDto result = null;
        try {
            marketRatePremiumDao.update(dto, "testUser");
            result = marketRatePremiumDao.fetch(marketRatePremiumId);
        } catch (DaoException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(result.getMinTotalPremiumAmount()).isEqualTo(new BigDecimal("1.00"));
        assertThat(result.getMaxTotalPremiumAmount()).isEqualTo(new BigDecimal("2.00"));
        assertThat(result.getRiskChargeFlatAmount()).isEqualTo(new BigDecimal("3.00"));
        assertThat(result.getRiskChargePctPremium()).isEqualTo(new BigDecimal("4.00"));
        assertThat(result.getAdjustChargeFlatAmount()).isEqualTo(new BigDecimal("5.00"));
    }

    @Test
    @Order(4)
    public void testDelete() {
        assertThatNoException().isThrownBy(() -> {
            marketRatePremiumDao.delete(marketRatePremiumId);
        });
    }
}
