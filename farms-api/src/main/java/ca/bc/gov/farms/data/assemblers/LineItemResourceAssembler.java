package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.LineItemEntity;
import ca.bc.gov.farms.data.models.LineItemListRsrc;
import ca.bc.gov.farms.data.models.LineItemRsrc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class LineItemResourceAssembler extends BaseResourceAssembler {

    public LineItemRsrc getLineItem(@NonNull LineItemEntity entity) {

        URI baseUri = getBaseURI();

        LineItemRsrc resource = new LineItemRsrc();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getLineItemId(), resource, baseUri);

        return resource;
    }

    public LineItemListRsrc getLineItemList(List<LineItemEntity> entities) {

        URI baseUri = getBaseURI();

        LineItemListRsrc result = null;

        @SuppressWarnings("null")
        List<LineItemRsrc> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            LineItemRsrc resource = new LineItemRsrc();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getLineItemId(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new LineItemListRsrc();
        result.setLineItemList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    public void updateLineItem(@NonNull LineItemRsrc resource, @NonNull LineItemEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
