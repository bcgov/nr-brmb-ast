package ca.bc.gov.farms.api.rest.v1.resource.factory;

import ca.bc.gov.brmb.common.rest.endpoints.resource.factory.BaseResourceFactory;
import ca.bc.gov.brmb.common.service.api.model.factory.FactoryContext;
import ca.bc.gov.farms.api.rest.v1.resource.BenchmarkPerUnitRsrc;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnit;
import ca.bc.gov.farms.persistence.v1.dto.BenchmarkPerUnitDto;
import ca.bc.gov.farms.service.api.v1.model.factory.BenchmarkPerUnitFactory;

public class BenchmarkPerUnitRsrcFactory extends BaseResourceFactory implements BenchmarkPerUnitFactory {

    @Override
    public BenchmarkPerUnit getBenchmarkPerUnit(BenchmarkPerUnitDto dto, FactoryContext context) {

        BenchmarkPerUnitRsrc resource = new BenchmarkPerUnitRsrc();

        populateDefaultResource(resource, dto);

        String eTag = getEtag(resource);
        resource.setETag(eTag);

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
