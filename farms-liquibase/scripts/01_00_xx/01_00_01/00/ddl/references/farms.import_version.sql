ALTER TABLE farms.import_version ADD CONSTRAINT fk_iv_icc 
    FOREIGN KEY (import_class_code)
    REFERENCES farms.import_class_code(import_class_code)
;

ALTER TABLE farms.import_version ADD CONSTRAINT fk_iv_isc 
    FOREIGN KEY (import_state_code)
    REFERENCES farms.import_state_code(import_state_code)
;
