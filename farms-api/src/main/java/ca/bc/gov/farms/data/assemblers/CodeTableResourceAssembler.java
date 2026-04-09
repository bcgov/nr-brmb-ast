package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import ca.bc.gov.farms.data.entities.CodeEntity;
import ca.bc.gov.farms.data.models.CodeRsrc;
import ca.bc.gov.farms.data.models.CodeTableModel;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CodeTableResourceAssembler extends BaseResourceAssembler {

    @SuppressWarnings("null")
    public CodeTableModel getCodeTable(String tableName, List<CodeEntity> entities) {

        URI baseUri = getBaseURI();

        CodeTableModel resource = new CodeTableModel();
        resource.setCodeTableName(tableName);
        resource.setCodeTableDescriptiveName(tableName);
        resource.setCodes(entities.stream().filter(Objects::nonNull).map(e -> {
            CodeRsrc r = new CodeRsrc();
            BeanUtils.copyProperties(e, r);
            return r;
        }).collect(Collectors.toList()));

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getCodeTableName(), resource, baseUri);

        return resource;
    }
}
