CREATE TABLE farms.farm_rsn_rev_hog_inventories(
    rsn_rev_hog_inventory_id    numeric(10, 0)    NOT NULL,
    quantity_start                            numeric(19, 3),
    quantity_end                              numeric(19, 3),
    fmv_price                                 numeric(13, 2),
    inventory_item_code                       varchar(10)       NOT NULL,
    rsn_rev_hog_result_id       numeric(10, 0)    NOT NULL,
    revision_count                            numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                               varchar(30)       NOT NULL,
    when_created                               timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                               varchar(30),
    when_updated                               timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.rsn_rev_hog_inventory_id IS 'REASONABILITY REVENUE HOG INVENTORY ID is a surrogate unique identifier for REASONABILITY REVENUE HOG INVENTORY.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.quantity_start IS 'QUANTITY START is the opening quantity at the beginning of the year from REPORTED INVENTORY in CROP UNIT CODE.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.quantity_end IS 'QUANTITY END is the closing quantity at the beginning of the year from REPORTED INVENTORY in CROP UNIT CODE.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.fmv_price IS 'FMV PRICE is the average market price obtained for a livestock for a given month.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.rsn_rev_hog_result_id IS 'REASONABILITY REVENUE HOG RESULT ID is a surrogate unique identifier for REASONABILITY REVENUE HOG RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_hog_inventories.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_rsn_rev_hog_inventories IS 'REASONABILITY REVENUE HOG INVENTORY contains the calculated amounts for the Reasonability Test: Hogs Revenue Test run against the scenario, by INVENTORY ITEM CODE.'
;


CREATE INDEX ix_rrhi_iic ON farms.farm_rsn_rev_hog_inventories(inventory_item_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_rrhi_rrhri_iic ON farms.farm_rsn_rev_hog_inventories(rsn_rev_hog_result_id, inventory_item_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_rsn_rev_hog_inventories ADD 
    CONSTRAINT pk_rrhi PRIMARY KEY (rsn_rev_hog_inventory_id) USING INDEX TABLESPACE pg_default 
;
