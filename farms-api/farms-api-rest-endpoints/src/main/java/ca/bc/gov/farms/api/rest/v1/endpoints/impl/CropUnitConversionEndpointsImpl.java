package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.rest.resource.MessageListRsrc;
import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.brmb.common.service.api.ValidationFailureException;
import ca.bc.gov.farms.api.rest.v1.endpoints.CropUnitConversionEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.CropUnitConversionListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.CropUnitConversionRsrc;
import ca.bc.gov.farms.service.api.v1.CropUnitConversionService;

public class CropUnitConversionEndpointsImpl extends BaseEndpointsImpl implements CropUnitConversionEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(CropUnitConversionEndpointsImpl.class);

    @Autowired
    private CropUnitConversionService service;

    @Override
    public Response getAllCropUnitConversions(String inventoryItemCode) {
        logger.debug("<getAllCropUnitConversions");

        Response response = null;

        logRequest();

        CropUnitConversionListRsrc result = null;

        try {
            if (StringUtils.isBlank(inventoryItemCode)) {
                result = (CropUnitConversionListRsrc) service.getAllCropUnitConversions(getFactoryContext());
            } else {
                result = (CropUnitConversionListRsrc) service
                        .getCropUnitConversionsByInventoryItemCode(inventoryItemCode, getFactoryContext());
            }
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getAllCropUnitConversions " + response);
        return response;
    }

    @Override
    public Response getCropUnitConversion(Long cropUnitDefaultId) {
        logger.debug("<getCropUnitConversion");
        Response response = null;

        logRequest();

        try {
            CropUnitConversionRsrc result = (CropUnitConversionRsrc) service.getCropUnitConversion(cropUnitDefaultId,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getCropUnitConversion " + response);
        return response;
    }

    @Override
    public Response createCropUnitConversion(CropUnitConversionRsrc cropUnitConversionRsrc) {
        logger.debug("<createCropUnitConversion");

        Response response = null;

        logRequest();

        try {
            CropUnitConversionRsrc result = (CropUnitConversionRsrc) service
                    .createCropUnitConversion(cropUnitConversionRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createCropUnitConversion " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteCropUnitConversion(Long cropUnitDefaultId) {
        logger.debug("<deleteCropUnitConversion");

        Response response = null;

        logRequest();

        try {
            service.deleteCropUnitConversion(cropUnitDefaultId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteCropUnitConversion " + response.getStatus());
        return response;
    }

    @Override
    public Response updateCropUnitConversion(Long cropUnitDefaultId, CropUnitConversionRsrc cropUnitConversionRsrc) {
        logger.debug("<updateCropUnitConversion");

        Response response = null;

        logRequest();

        try {
            CropUnitConversionRsrc result = (CropUnitConversionRsrc) service
                    .updateCropUnitConversion(cropUnitDefaultId, cropUnitConversionRsrc, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (ValidationFailureException e) {
            response = Response.status(Status.BAD_REQUEST).entity(new MessageListRsrc(e.getValidationErrors())).build();
        } catch (NotFoundException e) {
            response = Response.status(Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">updateCropUnitConversion " + response.getStatus());
        return response;
    }

}
