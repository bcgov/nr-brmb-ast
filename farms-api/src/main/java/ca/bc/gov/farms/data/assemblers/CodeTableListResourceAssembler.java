package ca.bc.gov.farms.data.assemblers;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.farms.data.entities.CodeEntity;
import ca.bc.gov.farms.data.models.CodeRsrc;
import ca.bc.gov.farms.data.models.CodeTableListModel;
import ca.bc.gov.farms.data.models.CodeTableModel;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CodeTableListResourceAssembler extends BaseResourceAssembler {

    @SuppressWarnings("null")
    public CodeTableListModel getCodeTableList(Map<String, List<CodeEntity>> entitiesMap) {

        URI baseUri = getBaseURI();

        CodeTableListModel resource = new CodeTableListModel();
        resource.setCodeTableList(entitiesMap.entrySet().stream()
                .map(entry -> {
                    String tableName = entry.getKey();
                    List<CodeEntity> entities = entry.getValue();

                    CodeTableModel codeTableResource = new CodeTableModel();
                    codeTableResource.setCodeTableName(tableName);
                    codeTableResource.setCodeTableDescriptiveName(tableName);
                    codeTableResource.setCodes(entities.stream().filter(Objects::nonNull).map(e -> {
                        CodeRsrc r = new CodeRsrc();
                        BeanUtils.copyProperties(e, r);
                        return r;
                    }).collect(Collectors.toList()));

                    String eTag = getEtag(codeTableResource);
                    codeTableResource.setETag(eTag);

                    setSelfLink(codeTableResource.getCodeTableName(), codeTableResource, baseUri);

                    return codeTableResource;
                })
                .toList());

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        String selfUri = UriBuilder.fromUri(baseUri)
                .path("codeTables")
                .build().toString();
        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));

        return resource;
    }
}
