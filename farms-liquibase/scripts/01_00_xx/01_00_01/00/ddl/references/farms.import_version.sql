ALTER TABLE farms.farm_import_versions ADD CONSTRAINT fk_iv_icc 
    FOREIGN KEY (import_class_code)
    REFERENCES farms.farm_import_class_codes(import_class_code)
;

ALTER TABLE farms.farm_import_versions ADD CONSTRAINT fk_iv_isc 
    FOREIGN KEY (import_state_code)
    REFERENCES farms.farm_import_state_codes(import_state_code)
;
