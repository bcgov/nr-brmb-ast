package ca.bc.gov.farms.api.rest.v1.resource;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.XmlSeeAlso;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import ca.bc.gov.brmb.common.rest.resource.BaseResource;
import ca.bc.gov.farms.api.rest.v1.resource.types.ResourceTypes;
import ca.bc.gov.farms.model.v1.BenchmarkPerUnitList;

@XmlRootElement(namespace = ResourceTypes.NAMESPACE, name = ResourceTypes.BENCHMARK_PER_UNIT_LIST_NAME)
@XmlSeeAlso({ BenchmarkPerUnitListRsrc.class })
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type")
public class BenchmarkPerUnitListRsrc extends BaseResource implements BenchmarkPerUnitList<BenchmarkPerUnitRsrc> {

    private static final long serialVersionUID = 1L;

    private List<BenchmarkPerUnitRsrc> benchmarkPerUnitList;

    public BenchmarkPerUnitListRsrc() {
        this.benchmarkPerUnitList = new ArrayList<>();
    }

    @Override
    public List<BenchmarkPerUnitRsrc> getBenchmarkPerUnitList() {
        return benchmarkPerUnitList;
    }

    @Override
    public void setBenchmarkPerUnitList(List<BenchmarkPerUnitRsrc> benchmarkPerUnitList) {
        this.benchmarkPerUnitList = benchmarkPerUnitList;
    }

}
