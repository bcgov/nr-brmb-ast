package ca.bc.gov.farms.data.assemblers;

import org.springframework.beans.BeanUtils;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.CodeEntity;
import ca.bc.gov.farms.data.models.CodeModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CodeResourceAssembler extends BaseResourceAssembler {

    public CodeModel getCode(@NonNull CodeEntity entity) {

        CodeModel resource = new CodeModel();

        BeanUtils.copyProperties(entity, resource);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        return resource;
    }

    public void updateCode(@NonNull CodeModel resource, @NonNull CodeEntity entity) {
        BeanUtils.copyProperties(resource, entity);
    }
}
