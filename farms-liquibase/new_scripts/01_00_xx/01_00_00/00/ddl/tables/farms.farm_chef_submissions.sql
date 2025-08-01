ALTER TABLE farms.farm_chef_submissions ADD CONSTRAINT farm_cs_farm_fssc_fk FOREIGN KEY (chef_submssn_status_code) REFERENCES farms.farm_chef_submssn_status_codes(chef_submssn_status_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE farms.farm_chef_submissions ADD CONSTRAINT farm_cs_farm_ftc_fk FOREIGN KEY (chef_form_type_code) REFERENCES farms.farm_chef_form_type_codes(chef_form_type_code) ON DELETE NO ACTION NOT DEFERRABLE INITIALLY IMMEDIATE;
