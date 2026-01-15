ALTER TABLE farms.farm_import_versions ADD CONSTRAINT farm_iv_farm_iscc_fk FOREIGN KEY (import_class_code) REFERENCES farms.farm_import_class_codes(import_class_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_import_versions ADD CONSTRAINT farm_iv_farm_isc_fk FOREIGN KEY (import_state_code) REFERENCES farms.farm_import_state_codes(import_state_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
