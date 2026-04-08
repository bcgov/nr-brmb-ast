package ca.bc.gov.farms.service.api.v1.model.factory;

import java.util.List;

import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.model.v1.LineItem;
import ca.bc.gov.farms.model.v1.LineItemList;
import ca.bc.gov.farms.persistence.v1.dto.LineItemDto;

public interface LineItemFactory {

    LineItem getLineItem(LineItemDto dto, FactoryContext context);

    LineItemList<? extends LineItem> getLineItemList(
            List<LineItemDto> dtos, FactoryContext context);

    void updateLineItem(LineItemDto dto, LineItem model);
}
