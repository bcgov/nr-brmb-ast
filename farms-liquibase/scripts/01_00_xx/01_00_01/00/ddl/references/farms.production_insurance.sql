ALTER TABLE farms.farm_production_insurances ADD CONSTRAINT fk_pi_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farm_farming_operations(farming_operation_id)
;
