CREATE TABLE tip_line_item(
    tip_line_item_id                       numeric(10, 0)    NOT NULL,
    line_item                              numeric(4, 0)     NOT NULL,
    operating_cost_indicator               varchar(1)        DEFAULT 'N' NOT NULL,
    direct_expense_indicator               varchar(1)        DEFAULT 'N' NOT NULL,
    machinery_expense_indicator            varchar(1)        DEFAULT 'N' NOT NULL,
    land_and_building_expense_indicator    varchar(1)        DEFAULT 'N' NOT NULL,
    overhead_expense_indicator             varchar(1)        DEFAULT 'N' NOT NULL,
    program_payment_for_tips_indicator     varchar(1)        DEFAULT 'N' NOT NULL,
    other_indicator                        varchar(1)        DEFAULT 'N' NOT NULL,
    tip_farm_type_1_lookup_id              numeric(10, 0),
    revision_count                         numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                            varchar(30)       NOT NULL,
    create_date                            timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                            varchar(30),
    update_date                            timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN tip_line_item.tip_line_item_id IS 'TIP LINE ITEM ID is a surrogate unique identifier for a TIP LINE ITEM.'
;
COMMENT ON COLUMN tip_line_item.line_item IS 'LINE ITEM is an income or expense item for Agristability.'
;
COMMENT ON COLUMN tip_line_item.operating_cost_indicator IS 'OPERATING COST INDICATOR identifies if the LINE ITEM is used in the calculation of the Operating Ratio Cost on the TIP report.'
;
COMMENT ON COLUMN tip_line_item.direct_expense_indicator IS 'DIRECT EXPENSE INDICATOR identifies if the LINE ITEM is used in the calculation of the Direct Expense Ratio on the TIP report.'
;
COMMENT ON COLUMN tip_line_item.machinery_expense_indicator IS 'MACHINERY EXPENSE INDICATOR identifies if the LINE ITEM is used in the calculation of the Machinery Expense Ratio on the TIP report.'
;
COMMENT ON COLUMN tip_line_item.land_and_building_expense_indicator IS 'LAND AND BUILDING EXPENSE INDICATOR identifies if the LINE ITEM is used in the calculation of the Land and Building Expense Ratio on the TIP report.'
;
COMMENT ON COLUMN tip_line_item.overhead_expense_indicator IS 'OVERHEAD EXPENSE INDICATOR identifies if the LINE ITEM is used in the calculation of the Overhead Expense Ratio on the TIP report.'
;
COMMENT ON COLUMN tip_line_item.program_payment_for_tips_indicator IS 'PROGRAM PAYMENT FOR TIPS IND identifies if the LINE ITEM is considered a program payment for the purposes of the TIP reports. This is not to be confused with PROGRAM PAYMENT INDICATOR in the LINE ITEM table which is used in the benefit calculation. In theory, both should mean the same thing but PROGRAM PAYMENT IND does not match up with the Program Payment Lists A and B in the AgriStability harmonized guide.'
;
COMMENT ON COLUMN tip_line_item.other_indicator IS 'OTHER INDICATOR identifies if the LINE ITEM is converted to non-allowable code 9896 - Other, on TIP reports.'
;
COMMENT ON COLUMN tip_line_item.tip_farm_type_1_lookup_id IS 'TIP FARM TYPE 1 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 1 LOOKUP.'
;
COMMENT ON COLUMN tip_line_item.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN tip_line_item.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN tip_line_item.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN tip_line_item.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN tip_line_item.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE tip_line_item IS 'TIP LINE ITEM maps a LINE ITEM to a TIP FARM TYPE 1 LOOKUP for the purposes of generating a TIP (Towards Increased Profits) report for each client.'
;


CREATE INDEX ix_tli_tft1li ON tip_line_item(tip_farm_type_1_lookup_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_tli_li ON tip_line_item(line_item)
 TABLESPACE pg_default
;

ALTER TABLE tip_line_item ADD 
    CONSTRAINT pk_tli PRIMARY KEY (tip_line_item_id) USING INDEX TABLESPACE pg_default 
;
