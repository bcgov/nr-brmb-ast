ALTER TABLE negative_margin ADD CONSTRAINT fk_nm_as 
    FOREIGN KEY (agristability_scenario_id)
    REFERENCES agristability_scenario(agristability_scenario_id)
;

ALTER TABLE negative_margin ADD CONSTRAINT fk_nm_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farming_operation(farming_operation_id)
;

ALTER TABLE negative_margin ADD CONSTRAINT fk_nm_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;
