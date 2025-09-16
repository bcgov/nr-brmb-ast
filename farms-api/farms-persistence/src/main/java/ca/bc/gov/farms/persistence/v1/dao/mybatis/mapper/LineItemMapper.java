package ca.bc.gov.farms.persistence.v1.dao.mybatis.mapper;

import java.util.List;
import java.util.Map;

import ca.bc.gov.farms.persistence.v1.dto.LineItemDto;

public interface LineItemMapper {

    LineItemDto fetch(Map<String, Object> parameters);

    List<LineItemDto> fetchByLineItem(Map<String, Object> parameters);

    int insertLineItem(Map<String, Object> parameters);

    int updateLineItem(Map<String, Object> parameters);

    int deleteLineItem(Map<String, Object> parameters);
}
