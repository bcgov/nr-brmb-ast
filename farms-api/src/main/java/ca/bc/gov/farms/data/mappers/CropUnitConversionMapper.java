package ca.bc.gov.farms.data.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.ConversionUnitEntity;
import ca.bc.gov.farms.data.entities.CropUnitConversionEntity;

@Mapper
public interface CropUnitConversionMapper {

    CropUnitConversionEntity fetch(Long cropUnitDefaultId);

    List<CropUnitConversionEntity> fetchAll();

    List<CropUnitConversionEntity> fetchByInventoryItemCode(String inventoryItemCode);

    int insertCropUnitDefault(CropUnitConversionEntity dto, String userId);
    int insertCropUnitConversionFactor(String inventoryItemCode, ConversionUnitEntity dto, String userId);

    int updateCropUnitDefault(CropUnitConversionEntity dto, String userId);
    int updateCropUnitConversionFactor(String inventoryItemCode, ConversionUnitEntity dto, String userId);

    int deleteCropUnitDefault(Long cropUnitDefaultId);
    int deleteCropUnitConversionFactor(Long cropUnitConversionFactorId);
}
