package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;

import ca.bc.gov.farms.persistence.v1.dto.ProductiveUnitCodeDto;

public interface ProductiveUnitCodeMapper {

    List<ProductiveUnitCodeDto> fetchAll();
}
