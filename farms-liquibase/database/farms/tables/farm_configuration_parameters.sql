CREATE TABLE farms.farm_configuration_parameters (
	configuration_parameter_id bigint NOT NULL,
	parameter_name varchar(200) NOT NULL,
	parameter_value varchar(4000) NOT NULL,
	sensitive_data_ind varchar(1) NOT NULL,
	config_param_type_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_configuration_parameters IS E'CONFIGURATION PARAMETER contains parameters that change the behaviour of the FARM application.';
COMMENT ON COLUMN farms.farm_configuration_parameters.config_param_type_code IS E'CONFIG PARAM TYPE CODE is a unique code for the object CONFIG PARAM TYPE CODE. Examples of codes and descriptions are CURRENCY - Currency, PERCENT - Percent, INTEGER - Integer, STRING - String.';
COMMENT ON COLUMN farms.farm_configuration_parameters.configuration_parameter_id IS E'CONFIGURATION PARAMETER ID is a surrogate unique identifier for CONFIGURATION PARAMETER.';
COMMENT ON COLUMN farms.farm_configuration_parameters.parameter_name IS E'PARAMETER NAME is the name of the configuration parameter.';
COMMENT ON COLUMN farms.farm_configuration_parameters.parameter_value IS E'PARAMETER VALUE is the value of the configuration parameter.';
COMMENT ON COLUMN farms.farm_configuration_parameters.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_configuration_parameters.sensitive_data_ind IS E'SENSITIVE DATA IND denotes if this data should be masked/hidden, such as a password.';
COMMENT ON COLUMN farms.farm_configuration_parameters.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_configuration_parameters.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_configuration_parameters.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_configuration_parameters.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_cp_farm_cptc_fk_i ON farms.farm_configuration_parameters (config_param_type_code);
ALTER TABLE farms.farm_configuration_parameters ADD CONSTRAINT farm_cp_pk PRIMARY KEY (configuration_parameter_id);
ALTER TABLE farms.farm_configuration_parameters ADD CONSTRAINT farm_cp_uk UNIQUE (parameter_name);
ALTER TABLE farms.farm_configuration_parameters ADD CONSTRAINT farm_cp_sd_chk CHECK (sensitive_data_ind in ('N', 'Y'));
ALTER TABLE farms.farm_configuration_parameters ALTER COLUMN configuration_parameter_id SET NOT NULL;
ALTER TABLE farms.farm_configuration_parameters ALTER COLUMN parameter_name SET NOT NULL;
ALTER TABLE farms.farm_configuration_parameters ALTER COLUMN parameter_value SET NOT NULL;
ALTER TABLE farms.farm_configuration_parameters ALTER COLUMN sensitive_data_ind SET NOT NULL;
ALTER TABLE farms.farm_configuration_parameters ALTER COLUMN config_param_type_code SET NOT NULL;
ALTER TABLE farms.farm_configuration_parameters ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_configuration_parameters ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_configuration_parameters ALTER COLUMN when_created SET NOT NULL;
