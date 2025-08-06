CREATE TABLE farms.farm_rsn_rev_fruit_veg_invntrs(
    rsn_rev_fruit_veg_invntry_id    numeric(10, 0)    NOT NULL,
    quantity_produced                                      numeric(19, 3),
    expected_price                                         numeric(13, 2),
    expected_revenue                                       numeric(25, 2),
    inventory_item_code                                    varchar(10)       NOT NULL,
    crop_unit_code                                         varchar(10)       NOT NULL,
    reasonability_test_result_id                           numeric(10, 0)    NOT NULL,
    revision_count                                         numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                            varchar(30)       NOT NULL,
    when_created                                            timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                            varchar(30),
    when_updated                                            timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.rsn_rev_fruit_veg_invntry_id IS 'REASONABILITY REVENUE FRUIT VEGETABLES INVENTORY ID is a surrogate unique identifier for REASONABILITY REVENUE FRUIT VEGETABLES INVENTORY.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.quantity_produced IS 'QUANTITY PRODUCED is the quantity produced during the year from REPORTED INVENTORY in CROP UNIT CODE.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.expected_price IS 'EXPECTED PRICE is the benchmark price per CROP UNIT CODE that the farm could be expected to earn under normal circumstances.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.expected_revenue IS 'EXPECTED REVENUE is the amount of revenue, inventory sold multiplied by reported price.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_fruit_veg_invntrs.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_rsn_rev_fruit_veg_invntrs IS 'REASONABILITY REVENUE FRUIT VEGETABLES INVENTORY contains the calculated inventory results for the Reasonability Test: Fruits and Vegetables Revenue Test run against the scenario, by INVENTORY ITEM CODE.'
;


CREATE INDEX ix_rrfvi_cuc ON farms.farm_rsn_rev_fruit_veg_invntrs(crop_unit_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_rrfvi_iic ON farms.farm_rsn_rev_fruit_veg_invntrs(inventory_item_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_rrfvi_rtri_iic ON farms.farm_rsn_rev_fruit_veg_invntrs(reasonability_test_result_id, inventory_item_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_rsn_rev_fruit_veg_invntrs ADD 
    CONSTRAINT pk_rrfvi PRIMARY KEY (rsn_rev_fruit_veg_invntry_id) USING INDEX TABLESPACE pg_default 
;
