ALTER TABLE farms.farm_year_configuration_params ADD CONSTRAINT fk_ycp_cptc 
    FOREIGN KEY (config_param_type_code)
    REFERENCES farms.farm_config_param_type_codes(config_param_type_code)
;
