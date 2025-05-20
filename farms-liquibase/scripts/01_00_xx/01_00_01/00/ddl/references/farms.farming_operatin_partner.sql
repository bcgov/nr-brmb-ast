ALTER TABLE farms.farming_operatin_partner ADD CONSTRAINT fk_fop_fo 
    FOREIGN KEY (farming_operation_id)
    REFERENCES farms.farming_operation(farming_operation_id)
;
