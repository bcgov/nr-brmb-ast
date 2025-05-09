ALTER TABLE agristability_client ADD CONSTRAINT fk_ac1_p 
    FOREIGN KEY (person_id_client_contacted_by)
    REFERENCES person(person_id)
;

ALTER TABLE agristability_client ADD CONSTRAINT fk_ac1_p1 
    FOREIGN KEY (person_id)
    REFERENCES person(person_id)
;

ALTER TABLE agristability_client ADD CONSTRAINT fk_ac1_pcc 
    FOREIGN KEY (participant_class_code)
    REFERENCES participant_class_code(participant_class_code)
;

ALTER TABLE agristability_client ADD CONSTRAINT fk_ac1_plc 
    FOREIGN KEY (participant_language_code)
    REFERENCES participant_language_code(participant_language_code)
;
