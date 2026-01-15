CREATE TABLE farms.farm_scenario_config_params (
	scenario_config_param_id bigint NOT NULL,
	parameter_name varchar(200) NOT NULL,
	parameter_value varchar(4000) NOT NULL,
	agristability_scenario_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_scenario_config_params IS E'SCENARIO CONFIG PARAM contains parameters that change the behaviour of the FARM application.';
COMMENT ON COLUMN farms.farm_scenario_config_params.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_scenario_config_params.parameter_name IS E'PARAMETER NAME is the name of the configuration parameter.';
COMMENT ON COLUMN farms.farm_scenario_config_params.parameter_value IS E'PARAMETER VALUE is the value of the configuration parameter.';
COMMENT ON COLUMN farms.farm_scenario_config_params.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_scenario_config_params.scenario_config_param_id IS E'SCENARIO CONFIG PARAM ID is a surrogate unique identifier for SCENARIO CONFIG PARAM.';
COMMENT ON COLUMN farms.farm_scenario_config_params.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_scenario_config_params.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_scenario_config_params.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_scenario_config_params.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_scp_farm_pn_i ON farms.farm_scenario_config_params (parameter_name);
ALTER TABLE farms.farm_scenario_config_params ADD CONSTRAINT farm_scp_pk PRIMARY KEY (scenario_config_param_id);
ALTER TABLE farms.farm_scenario_config_params ADD CONSTRAINT farm_scp_uk UNIQUE (agristability_scenario_id,parameter_name);
ALTER TABLE farms.farm_scenario_config_params ALTER COLUMN scenario_config_param_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_config_params ALTER COLUMN parameter_name SET NOT NULL;
ALTER TABLE farms.farm_scenario_config_params ALTER COLUMN parameter_value SET NOT NULL;
ALTER TABLE farms.farm_scenario_config_params ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_scenario_config_params ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_scenario_config_params ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_scenario_config_params ALTER COLUMN when_created SET NOT NULL;
