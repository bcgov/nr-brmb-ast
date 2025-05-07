ALTER TABLE reasonability_benefit_risk_productive_units_result ADD CONSTRAINT fk_rbrpur_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES inventory_item_code(inventory_item_code)
;

ALTER TABLE reasonability_benefit_risk_productive_units_result ADD CONSTRAINT fk_rbrpur_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES reasonability_test_result(reasonability_test_result_id)
;

ALTER TABLE reasonability_benefit_risk_productive_units_result ADD CONSTRAINT fk_rbrpur_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES structure_group_code(structure_group_code)
;
