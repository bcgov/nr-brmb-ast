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
import ca.bc.gov.farms.data.models.MarketRatePremiumListModel;
import ca.bc.gov.farms.data.models.MarketRatePremiumModel;
import jakarta.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MarketRatePremiumServiceTest {

    @Autowired
    private MarketRatePremiumService marketRatePremiumService;

    private static Long marketRatePremiumId;

    @Test
    @Order(1)
    public void testCreateMarketRatePremium() {
        MarketRatePremiumModel resource = new MarketRatePremiumModel();
        resource.setMinTotalPremiumAmount(new BigDecimal("0.00"));
        resource.setMaxTotalPremiumAmount(new BigDecimal("1.00"));
        resource.setRiskChargeFlatAmount(new BigDecimal("2.00"));
        resource.setRiskChargePctPremium(new BigDecimal("3.00"));
        resource.setAdjustChargeFlatAmount(new BigDecimal("4.00"));
        resource.setUserEmail("testUser");

        MarketRatePremiumModel newResource = marketRatePremiumService.createMarketRatePremium(resource);
        marketRatePremiumId = newResource.getMarketRatePremiumId();

        assertThat(newResource.getMinTotalPremiumAmount()).isEqualTo(new BigDecimal("0.00"));
        assertThat(newResource.getMaxTotalPremiumAmount()).isEqualTo(new BigDecimal("1.00"));
        assertThat(newResource.getRiskChargeFlatAmount()).isEqualTo(new BigDecimal("2.00"));
        assertThat(newResource.getRiskChargePctPremium()).isEqualTo(new BigDecimal("3.00"));
        assertThat(newResource.getAdjustChargeFlatAmount()).isEqualTo(new BigDecimal("4.00"));
    }

    @Test
    @Order(2)
    public void testGetAllMarketRatePremiums() {
        MarketRatePremiumListModel resources = marketRatePremiumService.getAllMarketRatePremiums();
        assertThat(resources).isNotNull();
        assertThat(resources.getMarketRatePremiumList()).isNotEmpty();
        assertThat(resources.getMarketRatePremiumList().size()).isEqualTo(1);

        MarketRatePremiumModel resource = resources.getMarketRatePremiumList().iterator().next();
        assertThat(resource.getMinTotalPremiumAmount()).isEqualTo(new BigDecimal("0.00"));
        assertThat(resource.getMaxTotalPremiumAmount()).isEqualTo(new BigDecimal("1.00"));
        assertThat(resource.getRiskChargeFlatAmount()).isEqualTo(new BigDecimal("2.00"));
        assertThat(resource.getRiskChargePctPremium()).isEqualTo(new BigDecimal("3.00"));
        assertThat(resource.getAdjustChargeFlatAmount()).isEqualTo(new BigDecimal("4.00"));
    }

    @Test
    @Order(3)
    public void testUpdateMarketRatePremium() {
        MarketRatePremiumModel resource = null;
        try {
            resource = marketRatePremiumService.getMarketRatePremium(marketRatePremiumId);
        } catch (ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }
        assertThat(resource).isNotNull();

        resource.setMinTotalPremiumAmount(new BigDecimal("1.00"));
        resource.setMaxTotalPremiumAmount(new BigDecimal("2.00"));
        resource.setRiskChargeFlatAmount(new BigDecimal("3.00"));
        resource.setRiskChargePctPremium(new BigDecimal("4.00"));
        resource.setAdjustChargeFlatAmount(new BigDecimal("5.00"));
        resource.setUserEmail("testUser");

        MarketRatePremiumModel updatedResource = null;
        try {
            updatedResource = marketRatePremiumService.updateMarketRatePremium(marketRatePremiumId, resource);
        } catch (ConstraintViolationException | ServiceException | NotFoundException e) {
            fail(e.getMessage());
            return;
        }

        assertThat(updatedResource.getMinTotalPremiumAmount()).isEqualTo(new BigDecimal("1.00"));
        assertThat(updatedResource.getMaxTotalPremiumAmount()).isEqualTo(new BigDecimal("2.00"));
        assertThat(updatedResource.getRiskChargeFlatAmount()).isEqualTo(new BigDecimal("3.00"));
        assertThat(updatedResource.getRiskChargePctPremium()).isEqualTo(new BigDecimal("4.00"));
        assertThat(updatedResource.getAdjustChargeFlatAmount()).isEqualTo(new BigDecimal("5.00"));
    }

    @Test
    @Order(4)
    public void testDeleteMarketRatePremium() {
        assertThatNoException().isThrownBy(() -> {
            marketRatePremiumService.deleteMarketRatePremium(marketRatePremiumId);
        });
    }
}
