package ca.bc.gov.farms.data.assemblers;

import java.net.URI;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.CodeEntity;
import ca.bc.gov.farms.data.models.CodeRsrc;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CodeResourceAssembler extends BaseResourceAssembler {

    public CodeRsrc getCode(@NonNull CodeEntity entity) {

        URI baseUri = getBaseURI();

        CodeRsrc resource = new CodeRsrc();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getCode(), resource, baseUri);

        return resource;
    }

    public void updateCode(@NonNull CodeRsrc resource, @NonNull CodeEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
