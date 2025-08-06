ALTER TABLE farms.farm_chef_submissions ADD CONSTRAINT fk_cs_cftc 
    FOREIGN KEY (chef_form_type_code)
    REFERENCES farms.farm_chef_form_type_codes(chef_form_type_code)
;

ALTER TABLE farms.farm_chef_submissions ADD CONSTRAINT fk_cs_cssc 
    FOREIGN KEY (chef_submssn_status_code)
    REFERENCES farms.farm_chef_submssn_status_codes(chef_submssn_status_code)
;
