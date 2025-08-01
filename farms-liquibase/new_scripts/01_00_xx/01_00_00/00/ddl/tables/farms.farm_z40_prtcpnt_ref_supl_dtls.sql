ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ADD CONSTRAINT farm_z40_farm_z03_fk FOREIGN KEY (participant_pin,program_year,operation_number) REFERENCES farms.farm_z03_statement_infos(participant_pin,program_year,operation_number) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ADD CONSTRAINT farm_z40_farm_z28_fk FOREIGN KEY (production_unit) REFERENCES farms.farm_z28_prod_insurance_refs(production_unit) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
