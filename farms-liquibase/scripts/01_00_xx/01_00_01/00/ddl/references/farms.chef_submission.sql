ALTER TABLE chef_submission ADD CONSTRAINT fk_cs_cftc 
    FOREIGN KEY (chef_form_type_code)
    REFERENCES chef_form_type_code(chef_form_type_code)
;

ALTER TABLE chef_submission ADD CONSTRAINT fk_cs_cssc 
    FOREIGN KEY (chef_submission_status_code)
    REFERENCES chef_submission_status_code(chef_submission_status_code)
;
