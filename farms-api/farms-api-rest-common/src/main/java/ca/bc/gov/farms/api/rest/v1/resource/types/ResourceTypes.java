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

	// Inventory Item Detail
	public static final String INVENTORY_ITEM_DETAIL_NAME = "inventoryItemDetail";
	public static final String INVENTORY_ITEM_DETAIL_LIST_NAME = "inventoryItemDetails";

	// Inventory Type Xref
	public static final String INVENTORY_TYPE_XREF_NAME = "inventoryTypeXref";
	public static final String INVENTORY_TYPE_XREF_LIST_NAME = "inventoryTypeXrefs";

	// Inventory Item Attribute
	public static final String INVENTORY_ITEM_ATTRIBUTE_NAME = "inventoryItemAttribute";
	public static final String INVENTORY_ITEM_ATTRIBUTE_LIST_NAME = "inventoryItemAttributes";

	// Structure Group Attribute
	public static final String STRUCTURE_GROUP_ATTRIBUTE_NAME = "structureGroupAttribute";
	public static final String STRUCTURE_GROUP_ATTRIBUTE_LIST_NAME = "structureGroupAttributes";

	// Configuration Parameter
	public static final String CONFIGURATION_PARAMETER_NAME = "configurationParameter";
	public static final String CONFIGURATION_PARAMETER_LIST_NAME = "ConfigurationParameters";

	// Line Item
	public static final String LINE_ITEM_NAME = "lineItem";
	public static final String LINE_ITEM_LIST_NAME = "lineItems";

	// Fruit Veg Type Detail
	public static final String FRUIT_VEG_TYPE_DETAIL_NAME = "fruitVegTypeDetail";
	public static final String FRUIT_VEG_TYPE_DETAIL_LIST_NAME = "fruitVegTypeDetails";
}
