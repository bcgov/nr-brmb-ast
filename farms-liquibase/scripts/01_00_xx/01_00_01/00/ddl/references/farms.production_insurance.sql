ALTER TABLE production_insurance ADD CONSTRAINT fk_pi_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farming_operation(farming_operation_id)
;
