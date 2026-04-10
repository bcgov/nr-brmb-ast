package ca.bc.gov.farms.data.assemblers;

import java.net.URI;

import org.springframework.stereotype.Component;

import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.farms.data.models.TopLevelRsrc;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TopLevelResourceAssembler extends BaseResourceAssembler {

    public TopLevelRsrc getTopLevel() {

        URI baseUri = getBaseURI();

        TopLevelRsrc resource = new TopLevelRsrc();
        resource.setReleaseVersion("1.0.0-SNAPSHOT");

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        String selfUri = UriBuilder.fromUri(baseUri)
                .build().toString();
        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));

        return resource;
    }
}
