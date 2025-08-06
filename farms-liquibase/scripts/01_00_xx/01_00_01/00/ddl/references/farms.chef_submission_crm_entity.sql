ALTER TABLE farms.farm_chef_submssn_crm_entities ADD CONSTRAINT fk_csce_cetc 
    FOREIGN KEY (crm_entity_type_code)
    REFERENCES farms.farm_crm_entity_type_codes(crm_entity_type_code)
;

ALTER TABLE farms.farm_chef_submssn_crm_entities ADD CONSTRAINT fk_csce_cs 
    FOREIGN KEY (chef_submission_id)
    REFERENCES farms.farm_chef_submissions(chef_submission_id)
;
