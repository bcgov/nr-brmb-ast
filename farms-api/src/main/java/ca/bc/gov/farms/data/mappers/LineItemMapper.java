package ca.bc.gov.farms.data.mappers;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import ca.bc.gov.farms.data.entities.LineItemEntity;

@Mapper
public interface LineItemMapper {

    LineItemEntity fetch(Long LineItemId);

    List<LineItemEntity> fetchByProgramYear(Integer programYear);

    int insertLineItem(LineItemEntity dto, String userId);

    int updateLineItem(LineItemEntity dto, String userId);

    int deleteLineItem(Long lineItemId);
}
