ALTER TABLE import_version ADD CONSTRAINT fk_iv_icc 
    FOREIGN KEY (import_class_code)
    REFERENCES import_class_code(import_class_code)
;

ALTER TABLE import_version ADD CONSTRAINT fk_iv_isc 
    FOREIGN KEY (import_state_code)
    REFERENCES import_state_code(import_state_code)
;
