CREATE TABLE farms.farm_year_configuration_params (
	year_configuration_param_id bigint NOT NULL,
	program_year smallint NOT NULL,
	parameter_name varchar(200) NOT NULL,
	parameter_value varchar(4000) NOT NULL,
	config_param_type_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_year_configuration_params IS E'YEAR CONFIGURATION PARAM contains parameters that change the behaviour of the FARM application.';
COMMENT ON COLUMN farms.farm_year_configuration_params.config_param_type_code IS E'CONFIG PARAM TYPE CODE is a unique code for the object CONFIG PARAM TYPE CODE. Examples of codes and descriptions are CURRENCY - Currency, PERCENT - Percent, INTEGER - Integer, STRING - String.';
COMMENT ON COLUMN farms.farm_year_configuration_params.parameter_name IS E'PARAMETER NAME is the name of the configuration parameter.';
COMMENT ON COLUMN farms.farm_year_configuration_params.parameter_value IS E'PARAMETER VALUE is the value of the configuration parameter.';
COMMENT ON COLUMN farms.farm_year_configuration_params.program_year IS E'PROGRAM YEAR is the year that this parameter configures.';
COMMENT ON COLUMN farms.farm_year_configuration_params.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_year_configuration_params.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_year_configuration_params.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_year_configuration_params.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_year_configuration_params.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_year_configuration_params.year_configuration_param_id IS E'YEAR CONFIGURATION PARAM ID is a surrogate unique identifier for YEAR CONFIGURATION PARAM.';
CREATE INDEX farm_ycp_farm_cptc_fk_i ON farms.farm_year_configuration_params (config_param_type_code);
ALTER TABLE farms.farm_year_configuration_params ADD CONSTRAINT farm_ycp_uk UNIQUE (program_year,parameter_name);
ALTER TABLE farms.farm_year_configuration_params ADD CONSTRAINT farm_ycp_pk PRIMARY KEY (year_configuration_param_id);
ALTER TABLE farms.farm_year_configuration_params ALTER COLUMN year_configuration_param_id SET NOT NULL;
ALTER TABLE farms.farm_year_configuration_params ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_year_configuration_params ALTER COLUMN parameter_name SET NOT NULL;
ALTER TABLE farms.farm_year_configuration_params ALTER COLUMN parameter_value SET NOT NULL;
ALTER TABLE farms.farm_year_configuration_params ALTER COLUMN config_param_type_code SET NOT NULL;
ALTER TABLE farms.farm_year_configuration_params ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_year_configuration_params ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_year_configuration_params ALTER COLUMN when_created SET NOT NULL;
