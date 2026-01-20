CREATE TABLE farms.farm_scenario_state_audits (
	scenario_state_audit_id bigint NOT NULL,
	state_change_reason varchar(2000),
	agristability_scenario_id bigint NOT NULL,
	scenario_state_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_scenario_state_audits IS E'SCENARIO STATE AUDIT tracks changes to the state of the AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_scenario_state_audits.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_scenario_state_audits.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_scenario_state_audits.scenario_state_audit_id IS E'SCENARIO STATE AUDT ID is a unique identifier for SCENARIO STATE AUDITs.';
COMMENT ON COLUMN farms.farm_scenario_state_audits.scenario_state_code IS E'AGRISTABILITY STATE CODE is a unique code for the object AGRISTABILITY STATE CODE described as a character code used to uniquely identify the state of the PROGRAM YEAR VERSION. Examples of codes and descriptions are : INP - IN PROGRESS,  APP - APPROVED, PD - PAID.';
COMMENT ON COLUMN farms.farm_scenario_state_audits.state_change_reason IS E'STATE CHANGE REASON describes the reason for changing the AGRISTABILITY STATE CODE of the PROGRAM YEAR VERSION.';
COMMENT ON COLUMN farms.farm_scenario_state_audits.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_scenario_state_audits.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_scenario_state_audits.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_scenario_state_audits.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_ssa_farm_as_fk_i ON farms.farm_scenario_state_audits (agristability_scenario_id);
CREATE INDEX farm_ssa_farm_ssc_fk_i ON farms.farm_scenario_state_audits (scenario_state_code);
ALTER TABLE farms.farm_scenario_state_audits ADD CONSTRAINT farm_ssa_pk PRIMARY KEY (scenario_state_audit_id);
ALTER TABLE farms.farm_scenario_state_audits ALTER COLUMN scenario_state_audit_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_state_audits ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_state_audits ALTER COLUMN scenario_state_code SET NOT NULL;
ALTER TABLE farms.farm_scenario_state_audits ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_scenario_state_audits ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_scenario_state_audits ALTER COLUMN when_created SET NOT NULL;
