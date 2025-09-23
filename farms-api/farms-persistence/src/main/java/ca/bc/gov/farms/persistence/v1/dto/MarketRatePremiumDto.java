package ca.bc.gov.farms.persistence.v1.dto;

import java.math.BigDecimal;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class MarketRatePremiumDto extends BaseDto<MarketRatePremiumDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(MarketRatePremiumDto.class);

    private Long marketRatePremiumId;
    private BigDecimal minTotalPremiumAmount;
    private BigDecimal maxTotalPremiumAmount;
    private BigDecimal riskChargeFlatAmount;
    private BigDecimal riskChargePctPremium;
    private BigDecimal adjustChargeFlatAmount;

    private Integer revisionCount;
    private String createUser;
    private Date createDate;
    private String updateUser;
    private Date updateDate;

    public MarketRatePremiumDto() {
    }

    public MarketRatePremiumDto(MarketRatePremiumDto dto) {
        this.marketRatePremiumId = dto.marketRatePremiumId;
        this.minTotalPremiumAmount = dto.minTotalPremiumAmount;
        this.maxTotalPremiumAmount = dto.maxTotalPremiumAmount;
        this.riskChargeFlatAmount = dto.riskChargeFlatAmount;
        this.riskChargePctPremium = dto.riskChargePctPremium;
        this.adjustChargeFlatAmount = dto.adjustChargeFlatAmount;

        this.revisionCount = dto.revisionCount;
        this.createUser = dto.createUser;
        this.createDate = dto.createDate;
        this.updateUser = dto.updateUser;
        this.updateDate = dto.updateDate;
    }

    @Override
    public MarketRatePremiumDto copy() {
        return new MarketRatePremiumDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(MarketRatePremiumDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(MarketRatePremiumDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("marketRatePremiumId", this.marketRatePremiumId,
                    other.marketRatePremiumId);
            result = result && dtoUtils.equals("minTotalPremiumAmount", this, other);
            result = result && dtoUtils.equals("maxTotalPremiumAmount", this, other);
            result = result && dtoUtils.equals("riskChargeFlatAmount", this, other);
            result = result && dtoUtils.equals("riskChargePctPremium", this, other);
            result = result && dtoUtils.equals("adjustChargeFlatAmount", this, other);
        }

        return result;
    }

    public Long getMarketRatePremiumId() {
        return marketRatePremiumId;
    }

    public void setMarketRatePremiumId(Long marketRatePremiumId) {
        this.marketRatePremiumId = marketRatePremiumId;
    }

    public BigDecimal getMinTotalPremiumAmount() {
        return minTotalPremiumAmount;
    }

    public void setMinTotalPremiumAmount(BigDecimal minTotalPremiumAmount) {
        this.minTotalPremiumAmount = minTotalPremiumAmount;
    }

    public BigDecimal getMaxTotalPremiumAmount() {
        return maxTotalPremiumAmount;
    }

    public void setMaxTotalPremiumAmount(BigDecimal maxTotalPremiumAmount) {
        this.maxTotalPremiumAmount = maxTotalPremiumAmount;
    }

    public BigDecimal getRiskChargeFlatAmount() {
        return riskChargeFlatAmount;
    }

    public void setRiskChargeFlatAmount(BigDecimal riskChargeFlatAmount) {
        this.riskChargeFlatAmount = riskChargeFlatAmount;
    }

    public BigDecimal getRiskChargePctPremium() {
        return riskChargePctPremium;
    }

    public void setRiskChargePctPremium(BigDecimal riskChargePctPremium) {
        this.riskChargePctPremium = riskChargePctPremium;
    }

    public BigDecimal getAdjustChargeFlatAmount() {
        return adjustChargeFlatAmount;
    }

    public void setAdjustChargeFlatAmount(BigDecimal adjustChargeFlatAmount) {
        this.adjustChargeFlatAmount = adjustChargeFlatAmount;
    }

    public Integer getRevisionCount() {
        return revisionCount;
    }

    public void setRevisionCount(Integer revisionCount) {
        this.revisionCount = revisionCount;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

}
