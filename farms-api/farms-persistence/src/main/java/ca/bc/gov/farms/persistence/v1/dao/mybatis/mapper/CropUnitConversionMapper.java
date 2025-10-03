package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.CropUnitConversionDto;

public interface CropUnitConversionMapper {

    CropUnitConversionDto fetch(Map<String, Object> parameters);

    List<CropUnitConversionDto> fetchAll();

    List<CropUnitConversionDto> fetchBy(Map<String, Object> parameters);

    int insertCropUnitDefault(Map<String, Object> parameters);
    int insertCropUnitConversionFactor(Map<String, Object> parameters);

    int updateCropUnitDefault(Map<String, Object> parameters);
    int updateCropUnitConversionFactor(Map<String, Object> parameters);

    int deleteCropUnitDefault(Map<String, Object> parameters);
    int deleteCropUnitConversionFactor(Map<String, Object> parameters);
}
