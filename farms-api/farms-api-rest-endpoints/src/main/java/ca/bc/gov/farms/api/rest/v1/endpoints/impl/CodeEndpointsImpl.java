package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.farms.api.rest.v1.endpoints.CodeEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.CodeRsrc;
import ca.bc.gov.farms.service.api.v1.code.CodeService;
import ca.bc.gov.nrs.wfone.common.service.api.NotFoundException;

public class CodeEndpointsImpl extends BaseEndpointsImpl implements CodeEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(CodeEndpointsImpl.class);

    @Autowired
    private CodeService service;

    @Override
    public Response getCode(String codeTableName, String codeName) {
        logger.debug("<getCode");
        Response response = null;

        logRequest();

        try {

            CodeRsrc result = (CodeRsrc) service.getCode(codeTableName, codeName, getFactoryContext());

            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();

        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getCode " + response);
        return response;
    }

    @Override
    public Response createCode(String codeTableName, CodeRsrc codeRsrc) {
        logger.debug("<createCode");

        Response response = null;

        logRequest();

        try {

            String optimisticLock = getIfMatchHeader();

            CodeRsrc result = (CodeRsrc) service.createCode(codeTableName, optimisticLock, codeRsrc,
                    getFactoryContext());

            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createCode " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteCode(String codeTableName, String codeName) {
        logger.debug("<deleteCode");

        Response response = null;

        logRequest();

        try {

            String optimisticLock = getIfMatchHeader();

            service.deleteCode(codeTableName, optimisticLock, codeName, getFactoryContext());

            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteCode " + response.getStatus());
        return response;
    }
}
