package ca.bc.gov.farms.persistence.v1.dto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.brmb.common.persistence.dto.BaseDto;
import ca.bc.gov.brmb.common.persistence.utils.DtoUtils;

public class ProductiveUnitCodeDto extends BaseDto<ProductiveUnitCodeDto> {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = LoggerFactory.getLogger(ProductiveUnitCodeDto.class);

    private String code;
    private String description;

    public ProductiveUnitCodeDto() {
    }

    public ProductiveUnitCodeDto(ProductiveUnitCodeDto dto) {
        this.code = dto.code;
        this.description = dto.description;
    }

    @Override
    public ProductiveUnitCodeDto copy() {
        return new ProductiveUnitCodeDto(this);
    }

    @Override
    public Logger getLogger() {
        return logger;
    }

    @Override
    public boolean equalsBK(ProductiveUnitCodeDto other) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public boolean equalsAll(ProductiveUnitCodeDto other) {
        boolean result = false;

        if (other != null) {
            result = true;
            DtoUtils dtoUtils = new DtoUtils(getLogger());
            result = result && dtoUtils.equals("code", this.code, other.code);
            result = result && dtoUtils.equals("description", this.description, other.description);
        }

        return result;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
