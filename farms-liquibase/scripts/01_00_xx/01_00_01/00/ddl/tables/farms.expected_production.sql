CREATE TABLE farms.farm_expected_productions(
    expected_production_id                     numeric(10, 0)    NOT NULL,
    expected_prodctn_per_prod_unit    numeric(13, 3)    NOT NULL,
    inventory_item_code                        varchar(10)       NOT NULL,
    crop_unit_code                             varchar(10)       NOT NULL,
    revision_count                             numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                varchar(30)       NOT NULL,
    when_created                                timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                varchar(30),
    when_updated                                timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_expected_productions.expected_production_id IS 'EXPECTED PRODUCTION ID is a surrogate unique identifier for an EXPECTED PRODUCTION.'
;
COMMENT ON COLUMN farms.farm_expected_productions.expected_prodctn_per_prod_unit IS 'EXPECTED PRODUCTION PER PRODUCTIVE UNIT is the amount typically produced per productive unit for this crop, in the CROP UNIT CODE specified.'
;
COMMENT ON COLUMN farms.farm_expected_productions.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN farms.farm_expected_productions.crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN farms.farm_expected_productions.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_expected_productions.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_expected_productions.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_expected_productions.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_expected_productions.when_updated IS 'WHEN UPDATED indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_expected_productions IS 'EXPECTED PRODUCTION is the amount of a particular crop that a farm can typically produce. The amount varies between municipalities. The EXPECTED PRODUCTION is used in a reasonability test as a basis for comparison with a producer''s reported quantity produced in REPORTED INVENTORY.'
;


CREATE INDEX ix_ep_cuc ON farms.farm_expected_productions(crop_unit_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_ep_iic ON farms.farm_expected_productions(inventory_item_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_expected_productions ADD 
    CONSTRAINT pk_ep PRIMARY KEY (expected_production_id) USING INDEX TABLESPACE pg_default 
;
