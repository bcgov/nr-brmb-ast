insert into farms.farm_year_configuration_params (
	year_configuration_param_id,
	program_year,
	parameter_name,
	parameter_value,
	config_param_type_code,
	revision_count,
	who_created,
	when_created,
	who_updated,
	when_updated
) values (
    nextval('farms.farm_ycp_seq'),
    2024,
    'Negative Margin Purchase Requirement',
    '70',
    'DECIMAL',
    1,
    'FARM_02_43_00-00',
    current_timestamp,
    'FARM_02_43_00-00',
    current_timestamp
), (
    nextval('farms.farm_ycp_seq'),
    2025,
    'Negative Margin Purchase Requirement',
    '80',
    'DECIMAL',
    1,
    'FARM_02_43_00-00',
    current_timestamp,
    'FARM_02_43_00-00',
    current_timestamp
), (
    nextval('farms.farm_ycp_seq'),
    2026,
    'Negative Margin Purchase Requirement',
    '80',
    'DECIMAL',
    1,
    'FARM_02_43_00-00',
    current_timestamp,
    'FARM_02_43_00-00',
    current_timestamp
);
