package ca.bc.gov.farms.api.rest.v1.resource.types;

import ca.bc.gov.brmb.common.rest.resource.types.BaseResourceTypes;

public class ResourceTypes extends BaseResourceTypes {
	
	public static final String NAMESPACE = "http://farms.gov.bc.ca/v1/";

	public static final String ENDPOINTS_NAME = "endpoints";
	public static final String ENDPOINTS = NAMESPACE + ENDPOINTS_NAME;

	// Benchmark Per Unit
	public static final String BENCHMARK_PER_UNIT_NAME = "benchmarkPerUnit";
	public static final String BENCHMARK_PER_UNIT_LIST_NAME = "benchmarkPerUnits";

	// Fair Market Value
	public static final String FAIR_MARKET_VALUE_NAME = "fairMarketValue";
	public static final String FAIR_MARKET_VALUE_LIST_NAME = "fairMarketValues";
}
