CREATE TABLE farms.farm_productve_unit_capacities (
	productve_unit_capacity_id bigint NOT NULL,
	productive_capacity_amount decimal(13,3) NOT NULL,
	import_comment varchar(128),
	inventory_item_code varchar(10),
	structure_group_code varchar(10),
	participnt_data_src_code varchar(10) NOT NULL DEFAULT 'CRA',
	farming_operation_id bigint NOT NULL,
	agristability_scenario_id bigint,
	cra_productve_unit_capacity_id bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_productve_unit_capacities IS E'PRODUCTIVE UNIT CAPACITY is a measure of the productive capacity for a given AGRASTABILITY COMMODITY. A PRODUCTIVE UNIT CAPACITY associates with a specific AGRASTABILITY COMMODITY. A PRODUCTIVE UNIT CAPACITY may have more than one PRODUCTIVE UNIT CAPACITY ADJUSTMENT. A PRODUCTIVE UNIT CAPACITY is received via federal imports and through provincial data imports.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.cra_productve_unit_capacity_id IS E'PRODUCTVE UNIT CAPACITY ID is a surrogate unique identifier for PRODUCTIVE UNIT CAPACITIES.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.farming_operation_id IS E'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.import_comment IS E'IMPORT COMMENT is a system generated comment about how the original data might have been modified to enable the data to be inserted into the operational tables. Usually this involves changing an invalid code.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.participnt_data_src_code IS E'PARTICIPNT DATA SRC CODE indicates the source of the participant''s PRODUCTIVE UNIT CAPACITY data.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.productive_capacity_amount IS E'PRODUCTIVE CAPACITY AMOUNT is the quantity entered in section 9 for this inventory/bpu code.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.productve_unit_capacity_id IS E'PRODUCTVE UNIT CAPACITY ID is a surrogate unique identifier for PRODUCTIVE UNIT CAPACITIES.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.structure_group_code IS E'STRUCTURE GROUP CODE identifies the type of structure group.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_productve_unit_capacities.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_puc_farm_as_fk_i ON farms.farm_productve_unit_capacities (agristability_scenario_id);
CREATE INDEX farm_puc_farm_fo_fk_i ON farms.farm_productve_unit_capacities (farming_operation_id);
CREATE INDEX farm_puc_farm_ic_fk_i ON farms.farm_productve_unit_capacities (inventory_item_code);
CREATE INDEX farm_puc_farm_pdsc_fk_i ON farms.farm_productve_unit_capacities (participnt_data_src_code);
CREATE INDEX farm_puc_farm_puc_fk_i ON farms.farm_productve_unit_capacities (cra_productve_unit_capacity_id);
CREATE INDEX farm_puc_farm_sgc_fk_i ON farms.farm_productve_unit_capacities (structure_group_code);
ALTER TABLE farms.farm_productve_unit_capacities ADD CONSTRAINT farm_puc_pk PRIMARY KEY (productve_unit_capacity_id);
ALTER TABLE farms.farm_productve_unit_capacities ALTER COLUMN productve_unit_capacity_id SET NOT NULL;
ALTER TABLE farms.farm_productve_unit_capacities ALTER COLUMN productive_capacity_amount SET NOT NULL;
ALTER TABLE farms.farm_productve_unit_capacities ALTER COLUMN participnt_data_src_code SET NOT NULL;
ALTER TABLE farms.farm_productve_unit_capacities ALTER COLUMN farming_operation_id SET NOT NULL;
ALTER TABLE farms.farm_productve_unit_capacities ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_productve_unit_capacities ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_productve_unit_capacities ALTER COLUMN when_created SET NOT NULL;
