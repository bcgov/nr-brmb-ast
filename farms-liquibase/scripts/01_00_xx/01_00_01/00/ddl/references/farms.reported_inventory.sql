ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT fk_ri_acx 
    FOREIGN KEY (agristabilty_cmmdty_xref_id)
    REFERENCES farms.farm_agristabilty_cmmdty_xref(agristabilty_cmmdty_xref_id)
;

ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT fk_ri_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;

ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT fk_ri_cuc 
    FOREIGN KEY (crop_unit_code)
    REFERENCES farms.farm_crop_unit_codes(crop_unit_code)
;

ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT fk_ri_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farm_farming_operations(farming_operation_id)
;

ALTER TABLE farms.farm_reported_inventories ADD CONSTRAINT fk_ri_ri 
    FOREIGN KEY (cra_reported_inventory_id)
    REFERENCES farms.farm_reported_inventories(reported_inventory_id)
;
