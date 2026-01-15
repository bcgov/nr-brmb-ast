ALTER TABLE farms.farm_chef_submssn_crm_entities ADD CONSTRAINT farm_csce_farm_cetc_fk FOREIGN KEY (crm_entity_type_code) REFERENCES farms.farm_crm_entity_type_codes(crm_entity_type_code) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_chef_submssn_crm_entities ADD CONSTRAINT farm_csce_farm_cs_fk FOREIGN KEY (chef_submission_id) REFERENCES farms.farm_chef_submissions(chef_submission_id) ON DELETE NO ACTION DEFERRABLE INITIALLY IMMEDIATE;
