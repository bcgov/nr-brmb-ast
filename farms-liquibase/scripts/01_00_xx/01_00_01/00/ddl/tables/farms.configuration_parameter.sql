CREATE TABLE farms.configuration_parameter(
    configuration_parameter_id           numeric(10, 0)    NOT NULL,
    parameter_name                       varchar(200)      NOT NULL,
    parameter_value                      varchar(4000)     NOT NULL,
    sensitive_data_indicator             varchar(1)        NOT NULL,
    configuration_parameter_type_code    varchar(10)       NOT NULL,
    revision_count                       numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                          varchar(30)       NOT NULL,
    create_date                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                          varchar(30),
    update_date                          timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.configuration_parameter.configuration_parameter_id IS 'CONFIGURATION PARAMETER ID is a surrogate unique identifier for CONFIGURATION PARAMETER.'
;
COMMENT ON COLUMN farms.configuration_parameter.parameter_name IS 'PARAMETER NAME is the name of the configuration parameter.'
;
COMMENT ON COLUMN farms.configuration_parameter.parameter_value IS 'PARAMETER VALUE is the value of the configuration parameter.'
;
COMMENT ON COLUMN farms.configuration_parameter.sensitive_data_indicator IS 'SENSITIVE DATA INDICATOR denotes if this data should be masked/hidden, such as a password.'
;
COMMENT ON COLUMN farms.configuration_parameter.configuration_parameter_type_code IS 'CONFIGURATION PARAMETER TYPE CODE is a unique code for the object CONFIGURATION PARAMETER TYPE CODE. Examples of codes and descriptions are CURRENCY - Currency, PERCENT - Percent, INTEGER - Integer, STRING - String.'
;
COMMENT ON COLUMN farms.configuration_parameter.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.configuration_parameter.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.configuration_parameter.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.configuration_parameter.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.configuration_parameter.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.configuration_parameter IS 'CONFIGURATION PARAMETER contains parameters that change the behaviour of the FARM application.'
;


CREATE INDEX ix_cp_cptc ON farms.configuration_parameter(configuration_parameter_type_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_cp_pn ON farms.configuration_parameter(parameter_name)
 TABLESPACE pg_default
;

ALTER TABLE farms.configuration_parameter ADD 
    CONSTRAINT pk_cp PRIMARY KEY (configuration_parameter_id) USING INDEX TABLESPACE pg_default 
;
