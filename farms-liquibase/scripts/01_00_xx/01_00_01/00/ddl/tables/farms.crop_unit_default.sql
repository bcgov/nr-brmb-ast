CREATE TABLE farms.crop_unit_default(
    crop_unit_default_id    numeric(10, 0)    NOT NULL,
    inventory_item_code     varchar(10)       NOT NULL,
    crop_unit_code          varchar(10)       NOT NULL,
    revision_count          numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user             varchar(30)       NOT NULL,
    create_date             timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user             varchar(30),
    update_date             timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.crop_unit_default.crop_unit_default_id IS 'CROP UNIT DEFAULT ID is a surrogate unique identifier for FMV DEFAULT UNITs.'
;
COMMENT ON COLUMN farms.crop_unit_default.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN farms.crop_unit_default.crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN farms.crop_unit_default.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.crop_unit_default.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.crop_unit_default.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.crop_unit_default.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.crop_unit_default.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.crop_unit_default IS 'CROP UNIT DEFAULT is the base unit used when importing FAIR MARKET VALUE records. It will then be used with CROP UNIT CONVERSION FACTOR to calculate and create FAIR MARKET VALUE records for other units. FAIR MARKET VALUE records may only be imported for the CROP UNIT DEFAULTs.'
;


CREATE INDEX ix_cud_cuc ON farms.crop_unit_default(crop_unit_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_cud_iic ON farms.crop_unit_default(inventory_item_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.crop_unit_default ADD 
    CONSTRAINT pk_cud PRIMARY KEY (crop_unit_default_id) USING INDEX TABLESPACE pg_default 
;
