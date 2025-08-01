ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT farm_asc_farm_pcc_fk FOREIGN KEY (participant_class_code) REFERENCES farm_participant_class_codes(participant_class_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT farm_asc_farm_piccb_fk FOREIGN KEY (person_id_client_contacted_by) REFERENCES farm_persons(person_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT farm_asc_farm_plc_fk FOREIGN KEY (participant_lang_code) REFERENCES farm_participant_lang_codes(participant_lang_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT farm_asc_pi_fk FOREIGN KEY (person_id) REFERENCES farm_persons(person_id) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
