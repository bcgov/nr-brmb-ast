CREATE TABLE farms.farm_reference_scenarios (
	reference_scenario_id bigint NOT NULL,
	used_in_calc_ind varchar(1) NOT NULL DEFAULT 'N',
	deemed_farming_year_ind varchar(1) NOT NULL DEFAULT 'Y',
	agristability_scenario_id bigint NOT NULL,
	for_agristability_scenario_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_reference_scenarios IS E'REFERENCE SCENARIO records the previous five years of claims used for calculating a given year''s claim.';
COMMENT ON COLUMN farms.farm_reference_scenarios.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_reference_scenarios.deemed_farming_year_ind IS E'DEEMED FARMING YEAR IND identifies if the REFERENCE SCENARIO had enough data to be comparable to other REFERENCE SCENARIOs.';
COMMENT ON COLUMN farms.farm_reference_scenarios.for_agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_reference_scenarios.reference_scenario_id IS E'REFERENCE SCENARIO ID is a surrogate unique identifier for REFERENCE SCENARIO.';
COMMENT ON COLUMN farms.farm_reference_scenarios.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_reference_scenarios.used_in_calc_ind IS E'USED IN CALC IND identifies if the AGRISTABILITY CLAIM record was used in the calculation of the claim.';
COMMENT ON COLUMN farms.farm_reference_scenarios.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_reference_scenarios.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_reference_scenarios.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_reference_scenarios.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_rs_farm_as_fk_i ON farms.farm_reference_scenarios (for_agristability_scenario_id);
CREATE INDEX farm_rthe_referencing_scena__i ON farms.farm_reference_scenarios (agristability_scenario_id);
ALTER TABLE farms.farm_reference_scenarios ADD CONSTRAINT farm_rs_pk PRIMARY KEY (reference_scenario_id);
ALTER TABLE farms.farm_reference_scenarios ADD CONSTRAINT avcon_1299283225_deeme_000 CHECK (deemed_farming_year_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reference_scenarios ADD CONSTRAINT avcon_1299283225_used__000 CHECK (used_in_calc_ind in ('N', 'Y'));
ALTER TABLE farms.farm_reference_scenarios ALTER COLUMN reference_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_reference_scenarios ALTER COLUMN used_in_calc_ind SET NOT NULL;
ALTER TABLE farms.farm_reference_scenarios ALTER COLUMN deemed_farming_year_ind SET NOT NULL;
ALTER TABLE farms.farm_reference_scenarios ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_reference_scenarios ALTER COLUMN for_agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_reference_scenarios ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_reference_scenarios ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_reference_scenarios ALTER COLUMN when_created SET NOT NULL;
