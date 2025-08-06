ALTER TABLE farms.farm_configuration_parameters ADD CONSTRAINT fk_cp_cptc 
    FOREIGN KEY (config_param_type_code)
    REFERENCES farms.farm_config_param_type_codes(config_param_type_code)
;
