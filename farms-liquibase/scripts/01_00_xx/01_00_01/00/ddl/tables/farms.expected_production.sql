CREATE TABLE expected_production(
    expected_production_id                     numeric(10, 0)    NOT NULL,
    expected_production_per_productive_unit    numeric(13, 3)    NOT NULL,
    inventory_item_code                        varchar(10)       NOT NULL,
    crop_unit_code                             varchar(10)       NOT NULL,
    revision_count                             numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                varchar(30)       NOT NULL,
    create_date                                timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                varchar(30),
    update_date                                timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN expected_production.expected_production_id IS 'EXPECTED PRODUCTION ID is a surrogate unique identifier for an EXPECTED PRODUCTION.'
;
COMMENT ON COLUMN expected_production.expected_production_per_productive_unit IS 'EXPECTED PRODUCTION PER PRODUCTIVE UNIT is the amount typically produced per productive unit for this crop, in the CROP UNIT CODE specified.'
;
COMMENT ON COLUMN expected_production.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN expected_production.crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN expected_production.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN expected_production.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN expected_production.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN expected_production.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN expected_production.update_date IS 'WHEN UPDATED indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE expected_production IS 'EXPECTED PRODUCTION is the amount of a particular crop that a farm can typically produce. The amount varies between municipalities. The EXPECTED PRODUCTION is used in a reasonability test as a basis for comparison with a producer''s reported quantity produced in REPORTED INVENTORY.'
;

