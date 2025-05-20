ALTER TABLE farms.agristability_client ADD CONSTRAINT fk_ac1_p 
    FOREIGN KEY (person_id_client_contacted_by)
    REFERENCES farms.person(person_id)
;

ALTER TABLE farms.agristability_client ADD CONSTRAINT fk_ac1_p1 
    FOREIGN KEY (person_id)
    REFERENCES farms.person(person_id)
;

ALTER TABLE farms.agristability_client ADD CONSTRAINT fk_ac1_pcc 
    FOREIGN KEY (participant_class_code)
    REFERENCES farms.participant_class_code(participant_class_code)
;

ALTER TABLE farms.agristability_client ADD CONSTRAINT fk_ac1_plc 
    FOREIGN KEY (participant_language_code)
    REFERENCES farms.participant_language_code(participant_language_code)
;
