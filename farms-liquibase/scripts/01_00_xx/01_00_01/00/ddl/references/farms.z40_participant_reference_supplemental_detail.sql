ALTER TABLE farms.z40_participant_reference_supplemental_detail ADD CONSTRAINT fk_zprs_zpir 
    FOREIGN KEY (production_unit)
    REFERENCES farms.z28_production_insurance_reference(production_unit)
;

ALTER TABLE farms.z40_participant_reference_supplemental_detail ADD CONSTRAINT fk_zprsd_zsi 
    FOREIGN KEY (operation_number, participant_pin, program_year)
    REFERENCES farms.z03_statement_information(operation_number, participant_pin, program_year)
;
