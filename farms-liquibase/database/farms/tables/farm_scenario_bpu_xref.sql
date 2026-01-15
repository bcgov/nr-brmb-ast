CREATE TABLE farms.farm_scenario_bpu_xref (
	scenario_bpu_xref_id bigint NOT NULL,
	agristability_scenario_id bigint NOT NULL,
	benchmark_per_unit_id bigint NOT NULL,
	scenario_bpu_purpose_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_scenario_bpu_xref IS E'SCENARIO BPU XREF is the benchmark data that was used to determine that the REPORTED INCOME EXPENSEs for a FARMING OPERATION are resonable.';
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.benchmark_per_unit_id IS E'BENCHMARK PER UNIT ID is a surrogate unique identifier for BENCHMARK PER UNITs.';
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.scenario_bpu_purpose_code IS E'SCENARIO BPU PURPOSE CODE is a unique code for the object SCENARIO BPU PURPOS CODE. Examples of codes and descriptions are STANDARD - Standard, CN_ADDITNL - Coverage Notice Additional.';
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.scenario_bpu_xref_id IS E'SCENARIO BPU XREF ID is a surrogate unique identifier for an SCENARIO BPU XREF.';
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_scenario_bpu_xref.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_bpux_farm_as_fk_i ON farms.farm_scenario_bpu_xref (agristability_scenario_id);
CREATE INDEX farm_bpux_farm_bpu_fk_i ON farms.farm_scenario_bpu_xref (benchmark_per_unit_id);
CREATE INDEX farm_bpux_farm_sbpc_fk_i ON farms.farm_scenario_bpu_xref (scenario_bpu_purpose_code);
ALTER TABLE farms.farm_scenario_bpu_xref ADD CONSTRAINT farm_bpux_pk PRIMARY KEY (scenario_bpu_xref_id);
ALTER TABLE farms.farm_scenario_bpu_xref ALTER COLUMN scenario_bpu_xref_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_bpu_xref ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_bpu_xref ALTER COLUMN benchmark_per_unit_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_bpu_xref ALTER COLUMN scenario_bpu_purpose_code SET NOT NULL;
ALTER TABLE farms.farm_scenario_bpu_xref ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_scenario_bpu_xref ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_scenario_bpu_xref ALTER COLUMN when_created SET NOT NULL;
