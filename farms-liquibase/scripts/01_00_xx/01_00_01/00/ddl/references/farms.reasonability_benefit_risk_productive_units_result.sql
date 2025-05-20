ALTER TABLE farms.reasonability_benefit_risk_productive_units_result ADD CONSTRAINT fk_rbrpur_iic 
    FOREIGN KEY (inventory_item_code)
    REFERENCES farms.inventory_item_code(inventory_item_code)
;

ALTER TABLE farms.reasonability_benefit_risk_productive_units_result ADD CONSTRAINT fk_rbrpur_rtr 
    FOREIGN KEY (reasonability_test_result_id)
    REFERENCES farms.reasonability_test_result(reasonability_test_result_id)
;

ALTER TABLE farms.reasonability_benefit_risk_productive_units_result ADD CONSTRAINT fk_rbrpur_sgc 
    FOREIGN KEY (structure_group_code)
    REFERENCES farms.structure_group_code(structure_group_code)
;
