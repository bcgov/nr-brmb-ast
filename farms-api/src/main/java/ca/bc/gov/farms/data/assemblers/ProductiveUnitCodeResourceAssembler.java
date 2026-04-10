package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.ProductiveUnitCodeEntity;
import ca.bc.gov.farms.data.models.ProductiveUnitCodeListRsrc;
import ca.bc.gov.farms.data.models.ProductiveUnitCodeModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductiveUnitCodeResourceAssembler extends BaseResourceAssembler {

    public ProductiveUnitCodeListRsrc getProductiveUnitCodeList(List<ProductiveUnitCodeEntity> entities) {

        URI baseUri = getBaseURI();

        ProductiveUnitCodeListRsrc result = null;

        @SuppressWarnings("null")
        List<ProductiveUnitCodeModel> resources = entities.stream().filter(Objects::nonNull).map(entity -> {
            ProductiveUnitCodeModel resource = new ProductiveUnitCodeModel();
            BeanUtils.copyProperties(entity, resource);
            setSelfLink(entity.getCode(), resource, baseUri);
            return resource;
        }).collect(Collectors.toList());

        result = new ProductiveUnitCodeListRsrc();
        result.setProductiveUnitCodeList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }
}
