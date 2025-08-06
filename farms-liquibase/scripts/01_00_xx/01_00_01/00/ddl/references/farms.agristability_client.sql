ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT fk_ac1_p 
    FOREIGN KEY (person_id_client_contacted_by)
    REFERENCES farms.farm_persons(person_id)
;

ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT fk_ac1_p1 
    FOREIGN KEY (person_id)
    REFERENCES farms.farm_persons(person_id)
;

ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT fk_ac1_pcc 
    FOREIGN KEY (participant_class_code)
    REFERENCES farms.farm_participant_class_codes(participant_class_code)
;

ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT fk_ac1_plc 
    FOREIGN KEY (participant_lang_code)
    REFERENCES farms.farm_participant_lang_codes(participant_lang_code)
;
