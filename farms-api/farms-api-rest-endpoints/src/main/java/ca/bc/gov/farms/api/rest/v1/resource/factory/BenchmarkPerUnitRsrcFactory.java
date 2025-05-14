package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.UriBuilder;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.rest.resource.RelLink;
import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.endpoints.BenchmarkPerUnitEndpoints;
import ca.bc.gov.farms.api.rest.v1.resource.BenchmarkPerUnitListRsrc;
import ca.bc.gov.farms.api.rest.v1.resource.BenchmarkPerUnitRsrc;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnit;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnitList;
import ca.bc.gov.farms.persistence.v1.dto.BenchmarkPerUnitDto;
import ca.bc.gov.farms.service.api.v1.model.factory.BenchmarkPerUnitFactory;

public class BenchmarkPerUnitRsrcFactory extends BaseResourceFactory implements BenchmarkPerUnitFactory {

    @Override
    public BenchmarkPerUnit getBenchmarkPerUnit(BenchmarkPerUnitDto dto, FactoryContext context) {

        URI baseUri = getBaseURI(context);

        BenchmarkPerUnitRsrc resource = new BenchmarkPerUnitRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

        setSelfLink(resource.getBenchmarkPerUnitId(), resource, baseUri);

        return resource;
    }

    private void populateDefaultResource(BenchmarkPerUnitRsrc resource, BenchmarkPerUnitDto dto) {
        resource.setBenchmarkPerUnitId(dto.getBenchmarkPerUnitId());
        resource.setProgramYear(dto.getProgramYear());
        resource.setUnitComment(dto.getUnitComment());
        resource.setExpiryDate(dto.getExpiryDate());
        resource.setMunicipalityCode(dto.getMunicipalityCode());
        resource.setInventoryItemCode(dto.getInventoryItemCode());
        resource.setStructureGroupCode(dto.getStructureGroupCode());
    }

    @Override
    public BenchmarkPerUnitList<? extends BenchmarkPerUnit> getBenchmarkPerUnitList(List<BenchmarkPerUnitDto> dtos,
            FactoryContext context) {

        URI baseUri = getBaseURI(context);

        BenchmarkPerUnitListRsrc result = null;

        List<BenchmarkPerUnitRsrc> resources = new ArrayList<>();

        for (BenchmarkPerUnitDto dto : dtos) {
            BenchmarkPerUnitRsrc resource = populate(dto);
            setSelfLink(dto.getBenchmarkPerUnitId(), resource, baseUri);
            resources.add(resource);
        }

        result = new BenchmarkPerUnitListRsrc();
        result.setBenchmarkPerUnitList(resources);

        String eTag = getEtag(result);
        result.setETag(eTag);

        setSelfLink(result, baseUri);

        return result;
    }

    static BenchmarkPerUnitRsrc populate(BenchmarkPerUnitDto dto) {

        BenchmarkPerUnitRsrc result = new BenchmarkPerUnitRsrc();

        result.setBenchmarkPerUnitId(dto.getBenchmarkPerUnitId());
        result.setProgramYear(dto.getProgramYear());
        result.setUnitComment(dto.getUnitComment());
        result.setExpiryDate(dto.getExpiryDate());
        result.setMunicipalityCode(dto.getMunicipalityCode());
        result.setInventoryItemCode(dto.getInventoryItemCode());
        result.setStructureGroupCode(dto.getStructureGroupCode());

        return result;
    }

    public static void setSelfLink(Long benchmarkPerUnitId, BenchmarkPerUnitRsrc resource, URI baseUri) {

        String selfUri = getBenchmarkPerUnitSelfUri(benchmarkPerUnitId, baseUri);

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    public static String getBenchmarkPerUnitSelfUri(Long benchmarkPerUnitId, URI baseUri) {

        String result = UriBuilder.fromUri(baseUri)
                .path(BenchmarkPerUnitEndpoints.class)
                .build(benchmarkPerUnitId).toString();

        return result;
    }

    public static void setSelfLink(BenchmarkPerUnitListRsrc resource, URI baseUri) {

        String selfUri = UriBuilder.fromUri(baseUri)
                .path(BenchmarkPerUnitEndpoints.class)
                .build().toString();

        resource.getLinks().add(new RelLink(BaseResourceTypes.SELF, selfUri, "GET"));
    }

    @Override
    public void updateBenchmarkPerUnit(BenchmarkPerUnitDto dto, BenchmarkPerUnit model) {
        dto.setBenchmarkPerUnitId(model.getBenchmarkPerUnitId());
        dto.setProgramYear(model.getProgramYear());
        dto.setUnitComment(model.getUnitComment());
        dto.setExpiryDate(model.getExpiryDate());
        dto.setMunicipalityCode(model.getMunicipalityCode());
        dto.setInventoryItemCode(model.getInventoryItemCode());
        dto.setStructureGroupCode(model.getStructureGroupCode());
    }
}
