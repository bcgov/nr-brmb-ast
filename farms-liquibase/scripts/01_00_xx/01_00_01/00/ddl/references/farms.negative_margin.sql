ALTER TABLE farms.farm_negative_margins ADD CONSTRAINT fk_nm_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES farms.farm_agristability_scenarios(agristability_scenario_id)
;

ALTER TABLE farms.farm_negative_margins ADD CONSTRAINT fk_nm_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farm_farming_operations(farming_operation_id)
;

ALTER TABLE farms.farm_negative_margins ADD CONSTRAINT fk_nm_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.farm_inventory_item_codes(inventory_item_code)
;
