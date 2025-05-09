ALTER TABLE farms.production_insurance ADD CONSTRAINT fk_pi_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farming_operation(farming_operation_id)
;
