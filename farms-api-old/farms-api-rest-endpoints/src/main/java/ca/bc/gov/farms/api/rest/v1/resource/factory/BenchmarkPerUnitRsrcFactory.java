package ca.bc.gov.farms.api.rest.v1.resource.factory;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import jakarta.ws.rs.core.UriBuilder;

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
        resource.setMunicipalityDesc(dto.getMunicipalityDesc());
        resource.setInventoryItemCode(dto.getInventoryItemCode());
        resource.setInventoryItemDesc(dto.getInventoryItemDesc());
        resource.setStructureGroupCode(dto.getStructureGroupCode());
        resource.setStructureGroupDesc(dto.getStructureGroupDesc());
        resource.setInventoryCode(dto.getInventoryCode());
        resource.setInventoryDesc(dto.getInventoryDesc());
        resource.setYearMinus6Margin(dto.getYearMinus6Margin());
        resource.setYearMinus5Margin(dto.getYearMinus5Margin());
        resource.setYearMinus4Margin(dto.getYearMinus4Margin());
        resource.setYearMinus3Margin(dto.getYearMinus3Margin());
        resource.setYearMinus2Margin(dto.getYearMinus2Margin());
        resource.setYearMinus1Margin(dto.getYearMinus1Margin());
        resource.setYearMinus6Expense(dto.getYearMinus6Expense());
        resource.setYearMinus5Expense(dto.getYearMinus5Expense());
        resource.setYearMinus4Expense(dto.getYearMinus4Expense());
        resource.setYearMinus3Expense(dto.getYearMinus3Expense());
        resource.setYearMinus2Expense(dto.getYearMinus2Expense());
        resource.setYearMinus1Expense(dto.getYearMinus1Expense());
        resource.setUrlId(dto.getUrlId());
        resource.setUrl(dto.getUrl());
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
        result.setMunicipalityDesc(dto.getMunicipalityDesc());
        result.setInventoryItemCode(dto.getInventoryItemCode());
        result.setInventoryItemDesc(dto.getInventoryItemDesc());
        result.setStructureGroupCode(dto.getStructureGroupCode());
        result.setStructureGroupDesc(dto.getStructureGroupDesc());
        result.setInventoryCode(dto.getInventoryCode());
        result.setInventoryDesc(dto.getInventoryDesc());
        result.setYearMinus6Margin(dto.getYearMinus6Margin());
        result.setYearMinus5Margin(dto.getYearMinus5Margin());
        result.setYearMinus4Margin(dto.getYearMinus4Margin());
        result.setYearMinus3Margin(dto.getYearMinus3Margin());
        result.setYearMinus2Margin(dto.getYearMinus2Margin());
        result.setYearMinus1Margin(dto.getYearMinus1Margin());
        result.setYearMinus6Expense(dto.getYearMinus6Expense());
        result.setYearMinus5Expense(dto.getYearMinus5Expense());
        result.setYearMinus4Expense(dto.getYearMinus4Expense());
        result.setYearMinus3Expense(dto.getYearMinus3Expense());
        result.setYearMinus2Expense(dto.getYearMinus2Expense());
        result.setYearMinus1Expense(dto.getYearMinus1Expense());
        result.setUrlId(dto.getUrlId());
        result.setUrl(dto.getUrl());

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
        dto.setInventoryCode(model.getInventoryCode());
        dto.setInventoryDesc(model.getInventoryDesc());
        dto.setYearMinus6Margin(model.getYearMinus6Margin());
        dto.setYearMinus5Margin(model.getYearMinus5Margin());
        dto.setYearMinus4Margin(model.getYearMinus4Margin());
        dto.setYearMinus3Margin(model.getYearMinus3Margin());
        dto.setYearMinus2Margin(model.getYearMinus2Margin());
        dto.setYearMinus1Margin(model.getYearMinus1Margin());
        dto.setYearMinus6Expense(model.getYearMinus6Expense());
        dto.setYearMinus5Expense(model.getYearMinus5Expense());
        dto.setYearMinus4Expense(model.getYearMinus4Expense());
        dto.setYearMinus3Expense(model.getYearMinus3Expense());
        dto.setYearMinus2Expense(model.getYearMinus2Expense());
        dto.setYearMinus1Expense(model.getYearMinus1Expense());
        dto.setUrlId(model.getUrlId());
        dto.setUrl(model.getUrl());
    }
}
