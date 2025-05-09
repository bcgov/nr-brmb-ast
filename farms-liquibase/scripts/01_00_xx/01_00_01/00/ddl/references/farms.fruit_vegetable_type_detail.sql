ALTER TABLE farms.fruit_vegetable_type_detail ADD CONSTRAINT fk_fvtd_fvtc 
    FOREIGN KEY (fruit_vegetable_type_code)
    REFERENCES farms.fruit_vegetable_type_code(fruit_vegetable_type_code)
;
