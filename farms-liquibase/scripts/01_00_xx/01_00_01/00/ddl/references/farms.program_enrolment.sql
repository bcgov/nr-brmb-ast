ALTER TABLE program_enrolment ADD CONSTRAINT fk_pe_ac1 
    FOREIGN KEY (agristability_client_id)
    REFERENCES agristability_client(agristability_client_id)
;

ALTER TABLE program_enrolment ADD CONSTRAINT fk_pe_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;
