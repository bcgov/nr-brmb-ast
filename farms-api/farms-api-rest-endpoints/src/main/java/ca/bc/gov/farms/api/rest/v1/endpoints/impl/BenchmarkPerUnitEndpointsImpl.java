package ca.bc.gov.farms.api.rest.v1.endpoints.impl;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ca.bc.gov.brmb.common.service.api.NotFoundException;
import ca.bc.gov.farms.api.rest.v1.endpoints.BenchmarkPerUnitEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.BenchmarkPerUnitListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.BenchmarkPerUnitRsrc;
import ca.bc.gov.farms.service.api.v1.BenchmarkPerUnitService;

public class BenchmarkPerUnitEndpointsImpl extends BaseEndpointsImpl implements BenchmarkPerUnitEndpoints {

    private static final Logger logger = LoggerFactory.getLogger(BenchmarkPerUnitEndpointsImpl.class);

    @Autowired
    private BenchmarkPerUnitService service;

    @Override
    public Response getBenchmarkPerUnitsByProgramYear(Integer programYear) {
        logger.debug("<getBenchmarkPerUnitsByProgramYear");

        Response response = null;

        logRequest();

        try {
            BenchmarkPerUnitListRsrc result = (BenchmarkPerUnitListRsrc) service
                    .getBenchmarkPerUnitsByProgramYear(programYear, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getBenchmarkPerUnitsByProgramYear " + response);
        return response;
    }

    @Override
    public Response getBenchmarkPerUnit(Long benchmarkPerUnitId) {
        logger.debug("<getBenchmarkPerUnit");
        Response response = null;

        logRequest();

        try {
            BenchmarkPerUnitRsrc result = (BenchmarkPerUnitRsrc) service.getBenchmarkPerUnit(benchmarkPerUnitId,
                    getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">getBenchmarkPerUnit " + response);
        return response;
    }

    @Override
    public Response createBenchmarkPerUnit(Long benchmarkPerUnitId, BenchmarkPerUnitRsrc benchmarkPerUnitRsrc) {
        logger.debug("<createBenchmarkPerUnit");

        Response response = null;

        logRequest();

        try {
            BenchmarkPerUnitRsrc result = (BenchmarkPerUnitRsrc) service
                    .createBenchmarkPerUnit(benchmarkPerUnitRsrc, getFactoryContext());
            response = Response.status(Status.CREATED).entity(result).tag(result.getUnquotedETag()).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">createBenchmarkPerUnit " + response.getStatus());
        return response;
    }

    @Override
    public Response deleteBenchmarkPerUnit(Long benchmarkPerUnitId) {
        logger.debug("<deleteBenchmarkPerUnit");

        Response response = null;

        logRequest();

        try {
            service.deleteBenchmarkPerUnit(benchmarkPerUnitId);
            response = Response.status(Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">deleteBenchmarkPerUnit " + response.getStatus());
        return response;
    }

    @Override
    public Response updateBenchmarkPerUnit(Long benchmarkPerUnitId, BenchmarkPerUnitRsrc benchmarkPerUnitRsrc) {
        logger.debug("<updateBenchmarkPerUnit");

        Response response = null;

        logRequest();

        try {
            BenchmarkPerUnitRsrc result = (BenchmarkPerUnitRsrc) service
                    .updateBenchmarkPerUnit(benchmarkPerUnitId, benchmarkPerUnitRsrc, getFactoryContext());
            response = Response.ok(result).tag(result.getUnquotedETag()).build();
        } catch (NotFoundException e) {
            response = Response.status(Response.Status.NOT_FOUND).build();
        } catch (Throwable t) {
            response = getInternalServerErrorResponse(t);
        }

        logResponse(response);

        logger.debug(">updateBenchmarkPerUnit " + response.getStatus());
        return response;
    }

}
