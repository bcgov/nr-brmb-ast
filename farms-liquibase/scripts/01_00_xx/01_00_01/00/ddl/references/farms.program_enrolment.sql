ALTER TABLE farms.farm_program_enrolments ADD CONSTRAINT fk_pe_ac1 
    FOREIGN KEY (agristability_client_id)
    REFERENCES farms.farm_agristability_clients(agristability_client_id)
;

ALTER TABLE farms.farm_program_enrolments ADD CONSTRAINT fk_pe_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;
