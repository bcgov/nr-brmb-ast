CREATE TABLE farms.farm_scenario_config_params(
    scenario_config_param_id    numeric(10, 0)    NOT NULL,
    parameter_name                         varchar(200)      NOT NULL,
    parameter_value                        varchar(4000)     NOT NULL,
    agristability_scenario_id              numeric(10, 0)    NOT NULL,
    revision_count                         numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                            varchar(30)       NOT NULL,
    when_created                            timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                            varchar(30),
    when_updated                            timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_scenario_config_params.scenario_config_param_id IS 'SCENARIO CONFIGURATION PARAMETER ID is a surrogate unique identifier for SCENARIO CONFIGURATION PARAMETER.'
;
COMMENT ON COLUMN farms.farm_scenario_config_params.parameter_name IS 'PARAMETER NAME is the name of the configuration parameter.'
;
COMMENT ON COLUMN farms.farm_scenario_config_params.parameter_value IS 'PARAMETER VALUE is the value of the configuration parameter.'
;
COMMENT ON COLUMN farms.farm_scenario_config_params.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_scenario_config_params.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_scenario_config_params.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_scenario_config_params.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_scenario_config_params.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_scenario_config_params.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_scenario_config_params IS 'SCENARIO CONFIGURATION PARAMETER contains parameters that change the behaviour of the FARM application.'
;


CREATE INDEX ix_scp_pn ON farms.farm_scenario_config_params(parameter_name)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_scp_asi_pn ON farms.farm_scenario_config_params(agristability_scenario_id, parameter_name)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_scenario_config_params ADD 
    CONSTRAINT pk_scp PRIMARY KEY (scenario_config_param_id) USING INDEX TABLESPACE pg_default 
;
