package ca.bc.gov.farms.data.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.FruitVegTypeDetailEntity;

@Mapper
public interface FruitVegTypeDetailMapper {

    FruitVegTypeDetailEntity fetch(String fruitVegTypeCode);

    List<FruitVegTypeDetailEntity> fetchAll();

    int insertFruitVegTypeCode(FruitVegTypeDetailEntity dto, String userId);
    int insertFruitVegTypeDetail(FruitVegTypeDetailEntity dto, String userId);

    int updateFruitVegTypeCode(FruitVegTypeDetailEntity dto, String userId);
    int updateFruitVegTypeDetail(FruitVegTypeDetailEntity dto, String userId);

    int deleteFruitVegTypeDetail(String fruitVegTypeCode);
}
