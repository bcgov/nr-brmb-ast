CREATE TABLE inventory_item_detail(
    inventory_item_detail_id         numeric(10, 0)    NOT NULL,
    program_year                     numeric(4, 0)     NOT NULL,
    eligibility_indicator            varchar(1)        NOT NULL,
    line_item                        numeric(4, 0),
    insurable_value                  numeric(13, 3),
    premium_rate                     numeric(13, 4),
    inventory_item_code              varchar(10),
    commodity_type_code              varchar(10),
    fruit_vegetable_type_code        varchar(10),
    multiple_stage_commodity_code    varchar(10),
    revision_count                   numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                      varchar(30)       NOT NULL,
    create_date                      timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                      varchar(30),
    update_date                      timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN inventory_item_detail.inventory_item_detail_id IS 'INVENTORY ITEM DETAIL ID is a surrogate unique identifier for INVENTORY ITEM DETAILs.'
;
COMMENT ON COLUMN inventory_item_detail.program_year IS 'PROGRAM YEAR is the applicable PROGRAM YEAR  for this record.'
;
COMMENT ON COLUMN inventory_item_detail.eligibility_indicator IS 'ELIGIBILITY INDICATOR identifies if the INVENTORY ITEM CODE is eligible in the program year.'
;
COMMENT ON COLUMN inventory_item_detail.line_item IS 'LINE ITEM is income or expense item for Agristability.'
;
COMMENT ON COLUMN inventory_item_detail.insurable_value IS 'INSURABLE VALUE is the dollar amount of insurance per unit that the AgriStability program requires the participant to carry.'
;
COMMENT ON COLUMN inventory_item_detail.premium_rate IS 'PREMIUM RATE is the main portion of the premium the participant pays for insurance of their production it is the percentage of the total dollar value they have insured for this INVENTORY ITEM CODE.'
;
COMMENT ON COLUMN inventory_item_detail.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN inventory_item_detail.commodity_type_code IS 'COMMODITY TYPE CODE is a unique code for the object COMMODITY TYPE CODE. Examples of codes and descriptions are GRAIN - Grain, FORAGE - Forage, FORAGESEED - Forage Seed, CATTLE - Cattle.'
;
COMMENT ON COLUMN inventory_item_detail.fruit_vegetable_type_code IS 'FRUIT VEGETABLE TYPE CODE is a unique code for the object FRUIT VEGETABLE TYPE CODE. Examples of codes and descriptions are APPLE - Apples, BEAN - Beans,  POTATO - Potatoes.'
;
COMMENT ON COLUMN inventory_item_detail.multiple_stage_commodity_code IS 'MULTIPLE STAGE COMMODITY CODE is a unique code for the object MULTIPLE STAGE COMMODITY CODE. Examples of codes and descriptions are APPLE - Apples, GRAPE - Grapes, CRANBERRY - Cranberries.'
;
COMMENT ON COLUMN inventory_item_detail.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN inventory_item_detail.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN inventory_item_detail.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN inventory_item_detail.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN inventory_item_detail.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE inventory_item_detail IS 'INVENTORY ITEM DETAIL is additional information about an INVENTORY ITEM CODE.'
;


CREATE INDEX ix_iid_py_ei ON inventory_item_detail(program_year, eligibility_indicator)
 TABLESPACE pg_default
;
CREATE INDEX ix_iid_ctc ON inventory_item_detail(commodity_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_iid_fvtc ON inventory_item_detail(fruit_vegetable_type_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_iid_iic ON inventory_item_detail(inventory_item_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_iid_mscc ON inventory_item_detail(multiple_stage_commodity_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_iid_py_li ON inventory_item_detail(program_year, line_item)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_iid_py_iic ON inventory_item_detail(program_year, inventory_item_code)
 TABLESPACE pg_default
;

ALTER TABLE inventory_item_detail ADD 
    CONSTRAINT pk_iid PRIMARY KEY (inventory_item_detail_id) USING INDEX TABLESPACE pg_default 
;
