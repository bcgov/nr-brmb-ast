CREATE TABLE scenario_configuration_parameter(
    scenario_configuration_parameter_id    numeric(10, 0)    NOT NULL,
    parameter_name                         varchar(200)      NOT NULL,
    parameter_value                        varchar(4000)     NOT NULL,
    agristability_scenario_id              numeric(10, 0)    NOT NULL,
    revision_count                         numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                            varchar(30)       NOT NULL,
    create_date                            timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                            varchar(30),
    update_date                            timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN scenario_configuration_parameter.scenario_configuration_parameter_id IS 'SCENARIO CONFIGURATION PARAMETER ID is a surrogate unique identifier for SCENARIO CONFIGURATION PARAMETER.'
;
COMMENT ON COLUMN scenario_configuration_parameter.parameter_name IS 'PARAMETER NAME is the name of the configuration parameter.'
;
COMMENT ON COLUMN scenario_configuration_parameter.parameter_value IS 'PARAMETER VALUE is the value of the configuration parameter.'
;
COMMENT ON COLUMN scenario_configuration_parameter.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN scenario_configuration_parameter.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN scenario_configuration_parameter.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN scenario_configuration_parameter.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN scenario_configuration_parameter.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN scenario_configuration_parameter.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE scenario_configuration_parameter IS 'SCENARIO CONFIGURATION PARAMETER contains parameters that change the behaviour of the FARM application.'
;

