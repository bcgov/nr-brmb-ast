CREATE TABLE farms.farm_crop_unit_conversn_fctrs(
    crop_unit_conversion_factor_id    numeric(10, 0)    NOT NULL,
    conversion_factor                 numeric(14, 6)    NOT NULL,
    inventory_item_code               varchar(10)       NOT NULL,
    target_crop_unit_code             varchar(10)       NOT NULL,
    revision_count                    numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                       varchar(30)       NOT NULL,
    when_created                       timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                       varchar(30),
    when_updated                       timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.crop_unit_conversion_factor_id IS 'CROP UNIT CONVERSION FACTOR ID is a surrogate unique identifier for CROP UNIT CONVERSION FACTORs.'
;
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.conversion_factor IS 'CONVERSION FACTOR is the ratio used to convert the FAIR MARKET VALUE for the CROP UNIT DEFAULT to the FAIR MARKET VALUE for the TARGET CROP UNIT CODE. This conversion occurs on import. FAIR MARKET VALUE records are created from this conversion.'
;
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.target_crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_crop_unit_conversn_fctrs IS 'CROP UNIT CONVERSION FACTOR is the ratio used to convert FAIR MARKET VALUE for the CROP UNIT DEFAULT to the FAIR MARKET VALUE for other CROP UNIT CODEs. FAIR MARKET VALUE records may only be imported for the CROP UNIT DEFAULTs. The conversion occurs on import. FAIR MARKET VALUE records are created from this conversion.'
;


CREATE INDEX ix_cucf_tcuc ON farms.farm_crop_unit_conversn_fctrs(target_crop_unit_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_cucf_iic ON farms.farm_crop_unit_conversn_fctrs(inventory_item_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_cucf_iic_tcuc ON farms.farm_crop_unit_conversn_fctrs(inventory_item_code, target_crop_unit_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_crop_unit_conversn_fctrs ADD 
    CONSTRAINT pk_cucf PRIMARY KEY (crop_unit_conversion_factor_id) USING INDEX TABLESPACE pg_default 
;
