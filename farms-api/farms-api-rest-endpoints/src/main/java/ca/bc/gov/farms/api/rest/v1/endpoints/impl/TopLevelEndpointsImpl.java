package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import java.net.URI;

import javax.ws.rs.GET;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.farms.api.rest.v1.endpoints.TopLevelEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.EndpointsRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.api.rest.code.resource.factory.CodeTableResourceFactory;

public class TopLevelEndpointsImpl extends BaseEndpointsImpl implements TopLevelEndpoints {
    /** Logger. */
    private static final Logger logger = LoggerFactory.getLogger(TopLevelEndpointsImpl.class);

    @Override
    @GET
    @Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
    public Response getTopLevel() {
        Response response = null;

        URI baseUri = this.getBaseUri();

        EndpointsRsrc result = new EndpointsRsrc();

        result.setReleaseVersion(this.getApplicationProperties().getProperty("application.version"));

        try {

            String selfURI = CodeTableResourceFactory.getCodeTablesSelfUri(null, baseUri);
            result.getLinks().add(new RelLink(ResourceTypes.CODE_TABLE_LIST, selfURI, HttpMethod.GET));

            result.setETag(getEtag(result));

            GenericEntity<EndpointsRsrc> entity = new GenericEntity<EndpointsRsrc>(result) {
                /* do nothing */
            };

            response = Response.ok(entity).tag(result.getUnquotedETag()).build();

        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("getTopLevel=" + response);
        }

        return response;
    }
}
