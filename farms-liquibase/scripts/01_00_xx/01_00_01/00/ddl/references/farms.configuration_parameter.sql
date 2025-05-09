ALTER TABLE configuration_parameter ADD CONSTRAINT fk_cp_cptc 
    FOREIGN KEY (configuration_parameter_type_code)
    REFERENCES configuration_parameter_type_code(configuration_parameter_type_code)
;
