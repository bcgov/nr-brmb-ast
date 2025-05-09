ALTER TABLE line_item ADD CONSTRAINT fk_cli_ctc 
    FOREIGN KEY (commodity_type_code)
    REFERENCES commodity_type_code(commodity_type_code)
;

ALTER TABLE line_item ADD CONSTRAINT fk_li_fvtc 
    FOREIGN KEY (fruit_vegetable_type_code)
    REFERENCES fruit_vegetable_type_code(fruit_vegetable_type_code)
;
