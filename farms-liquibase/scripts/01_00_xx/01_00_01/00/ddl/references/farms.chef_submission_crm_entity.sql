ALTER TABLE farms.chef_submission_crm_entity ADD CONSTRAINT fk_csce_cetc 
    FOREIGN KEY (crm_entity_type_code)
    REFERENCES farms.crm_entity_type_code(crm_entity_type_code)
;

ALTER TABLE farms.chef_submission_crm_entity ADD CONSTRAINT fk_csce_cs 
    FOREIGN KEY (chef_submission_id)
    REFERENCES farms.chef_submission(chef_submission_id)
;
