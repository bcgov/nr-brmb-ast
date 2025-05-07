ALTER TABLE reported_inventory ADD CONSTRAINT fk_ri_acx 
    FOREIGN KEY (agristabilty_commodity_xref_id)
    REFERENCES agristabilty_commodity_xref(agristabilty_commodity_xref_id)
;

ALTER TABLE reported_inventory ADD CONSTRAINT fk_ri_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;

ALTER TABLE reported_inventory ADD CONSTRAINT fk_ri_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES crop_unit_code(crop_unit_code)
;

ALTER TABLE reported_inventory ADD CONSTRAINT fk_ri_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farming_operation(farming_operation_id)
;

ALTER TABLE reported_inventory ADD CONSTRAINT fk_ri_ri 
    FOREIGN KEY (cra_reported_inventory_id)
    REFERENCES reported_inventory(reported_inventory_id)
;
