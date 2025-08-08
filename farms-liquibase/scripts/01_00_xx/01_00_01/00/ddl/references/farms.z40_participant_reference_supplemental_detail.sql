ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ADD CONSTRAINT fk_zprs_zpir 
    FOREIGN KEY (production_unit)
    REFERENCES farms.farm_z28_prod_insurance_refs(production_unit)
;

ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ADD CONSTRAINT fk_zprsd_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES farms.farm_z03_statement_infos(operation_number, participant_pin, program_year)
;
