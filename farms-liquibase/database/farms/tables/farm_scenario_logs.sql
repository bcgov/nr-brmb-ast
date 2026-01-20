CREATE TABLE farms.farm_scenario_logs (
	scenario_log_id bigint NOT NULL,
	log_message varchar(2000) NOT NULL,
	agristability_scenario_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON COLUMN farms.farm_scenario_logs.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_scenario_logs.log_message IS E'LOG MESSAGE is a system generated log about changes in the AGRISTABILITY SCENARIO. For example, logs will be generated when adjustments are made, when the scenario is assigned to someone, etc...';
COMMENT ON COLUMN farms.farm_scenario_logs.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_scenario_logs.scenario_log_id IS E'SCENARIO LOG ID is a surrogate unique identifier for SCENARIO LOG.';
COMMENT ON COLUMN farms.farm_scenario_logs.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_scenario_logs.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_scenario_logs.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_scenario_logs.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_sl_farm_as_fk_i ON farms.farm_scenario_logs (agristability_scenario_id);
ALTER TABLE farms.farm_scenario_logs ADD CONSTRAINT farm_sl_pk PRIMARY KEY (scenario_log_id);
ALTER TABLE farms.farm_scenario_logs ALTER COLUMN scenario_log_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_logs ALTER COLUMN log_message SET NOT NULL;
ALTER TABLE farms.farm_scenario_logs ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_logs ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_scenario_logs ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_scenario_logs ALTER COLUMN when_created SET NOT NULL;
