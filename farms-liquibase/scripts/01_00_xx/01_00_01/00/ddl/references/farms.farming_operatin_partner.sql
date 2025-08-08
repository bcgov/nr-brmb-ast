ALTER TABLE farms.farm_farming_operatin_prtnrs ADD CONSTRAINT fk_fop_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farm_farming_operations(farming_operation_id)
;
