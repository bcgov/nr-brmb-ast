CREATE TABLE farms.farm_year_configuration_params(
    year_configuration_param_id      numeric(10, 0)    NOT NULL,
    program_year                         numeric(4, 0)     NOT NULL,
    parameter_name                       varchar(200)      NOT NULL,
    parameter_value                      varchar(4000)     NOT NULL,
    config_param_type_code    varchar(10)       NOT NULL,
    revision_count                       numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                          varchar(30)       NOT NULL,
    when_created                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                          varchar(30),
    when_updated                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_year_configuration_params.year_configuration_param_id IS 'YEAR CONFIGURATION PARAMETER ID is a surrogate unique identifier for YEAR CONFIGURATION PARAMETER.'
;
COMMENT ON COLUMN farms.farm_year_configuration_params.program_year IS 'PROGRAM YEAR is the year that this parameter configures.'
;
COMMENT ON COLUMN farms.farm_year_configuration_params.parameter_name IS 'PARAMETER NAME is the name of the configuration parameter.'
;
COMMENT ON COLUMN farms.farm_year_configuration_params.parameter_value IS 'PARAMETER VALUE is the value of the configuration parameter.'
;
COMMENT ON COLUMN farms.farm_year_configuration_params.config_param_type_code IS 'CONFIGURATION PARAMETER TYPE CODE is a unique code for the object CONFIGURATION PARAMETER TYPE CODE. Examples of codes and descriptions are CURRENCY - Currency, PERCENT - Percent, INTEGER - Integer, STRING - String.'
;
COMMENT ON COLUMN farms.farm_year_configuration_params.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_year_configuration_params.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_year_configuration_params.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_year_configuration_params.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_year_configuration_params.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_year_configuration_params IS 'YEAR CONFIGURATION PARAMETER contains parameters that change the behaviour of the FARM application.'
;


CREATE INDEX ix_ycp_cptc ON farms.farm_year_configuration_params(config_param_type_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_ycp_py_pn ON farms.farm_year_configuration_params(program_year, parameter_name)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_year_configuration_params ADD 
    CONSTRAINT pk_ycp PRIMARY KEY (year_configuration_param_id) USING INDEX TABLESPACE pg_default 
;
