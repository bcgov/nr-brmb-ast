CREATE TABLE reasonability_revenue_nursery_inventory(
    reasonability_revenue_nursery_inventory_id    numeric(10, 0)    NOT NULL,
    quantity_produced                             numeric(19, 3),
    quantity_start                                numeric(19, 3),
    quantity_end                                  numeric(19, 3),
    quantity_sold                                 numeric(19, 3),
    fmv_price                                     numeric(13, 2),
    expected_revenue                              numeric(25, 2),
    inventory_item_code                           varchar(10)       NOT NULL,
    crop_unit_code                                varchar(10),
    reasonability_revenue_nursery_result_id       numeric(10, 0)    NOT NULL,
    revision_count                                numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                   varchar(30)       NOT NULL,
    create_date                                   timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                   varchar(30),
    update_date                                   timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN reasonability_revenue_nursery_inventory.reasonability_revenue_nursery_inventory_id IS 'REASONABILITY REVENUE NURSERY INVENTORY ID is a surrogate unique identifier for REASONABILITY REVENUE NURSERY INVENTORY.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.quantity_produced IS 'QUANTITY PRODUCED is the quantity produced during the year from REPORTED INVENTORY in CROP UNIT CODE.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.quantity_start IS 'QUANTITY START is the opening quantity at the beginning of the year from REPORTED INVENTORY in CROP UNIT CODE.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.quantity_end IS 'QUANTITY END is the closing quantity at the beginning of the year from REPORTED INVENTORY in CROP UNIT CODE.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.quantity_sold IS 'QUANTITY SOLD is the amount sold during the year from REPORTED INVENTORY in CROP UNIT CODE.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.fmv_price IS 'FMV PRICE is the average market price obtained for a crop for a given month.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.expected_revenue IS 'EXPECTED REVENUE is the amount of revenue, inventory sold multiplied by reported price.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.reasonability_revenue_nursery_result_id IS 'REASONABILITY REVENUE NURSERY RESULT ID is a surrogate unique identifier for REASONABILITY REVENUE NURSERY RESULT.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN reasonability_revenue_nursery_inventory.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE reasonability_revenue_nursery_inventory IS 'REASONABILITY REVENUE NURSERY INVENTORY contains the calculated inventory amounts for the Revenue Risk - Nursery Subtest run against the scenario, by INVENTORY ITEM CODE.'
;


CREATE INDEX ix_rrni1_cuc ON reasonability_revenue_nursery_inventory(crop_unit_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_rrni1_iic ON reasonability_revenue_nursery_inventory(inventory_item_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_rrni1_rrnri ON reasonability_revenue_nursery_inventory(reasonability_revenue_nursery_result_id)
 TABLESPACE pg_default
;

ALTER TABLE reasonability_revenue_nursery_inventory ADD 
    CONSTRAINT pk_rrni1 PRIMARY KEY (reasonability_revenue_nursery_inventory_id) USING INDEX TABLESPACE pg_default 
;
