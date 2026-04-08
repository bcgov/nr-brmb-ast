package ca.bc.gov.farms.data.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.ProductiveUnitCodeEntity;

@Mapper
public interface ProductiveUnitCodeMapper {

    List<ProductiveUnitCodeEntity> fetchAll();
}
