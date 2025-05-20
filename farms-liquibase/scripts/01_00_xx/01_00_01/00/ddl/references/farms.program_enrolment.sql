ALTER TABLE farms.program_enrolment ADD CONSTRAINT fk_pe_ac1 
    FOREIGN KEY (agristability_client_id)
    REFERENCES farms.agristability_client(agristability_client_id)
;

ALTER TABLE farms.program_enrolment ADD CONSTRAINT fk_pe_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.agristability_scenario(agristability_scenario_id)
;
