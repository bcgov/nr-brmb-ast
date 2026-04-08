CREATE TABLE farms.farm_crop_unit_conversn_fctrs (
	crop_unit_conversion_factor_id bigint NOT NULL,
	conversion_factor decimal(14,6) NOT NULL,
	inventory_item_code varchar(10) NOT NULL,
	target_crop_unit_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_crop_unit_conversn_fctrs IS E'CROP UNIT CONVERSN FCTR is the ratio used to convert FAIR MARKET VALUE for the CROP UNIT DEFAULT to the FAIR MARKET VALUE for other CROP UNIT CODEs. FAIR MARKET VALUE records may only be imported for the CROP UNIT DEFAULTs. The conversion occurs on import. FAIR MARKET VALUE records are created from this conversion.';
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.conversion_factor IS E'CONVERSION FACTOR is the ratio used to convert the FAIR MARKET VALUE for the CROP UNIT DEFAULT to the FAIR MARKET VALUE for the TARGET CROP UNIT CODE. This conversion occurs on import. FAIR MARKET VALUE records are created from this conversion.';
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.crop_unit_conversion_factor_id IS E'CROP UNIT CONVERSION FACTOR ID is a surrogate unique identifier for CROP UNIT CONVERSION FACTORs.';
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.target_crop_unit_code IS E'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.';
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_crop_unit_conversn_fctrs.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_cucf_farm_cuc_fk_i ON farms.farm_crop_unit_conversn_fctrs (target_crop_unit_code);
CREATE INDEX farm_cucf_farm_ic_fk_i ON farms.farm_crop_unit_conversn_fctrs (inventory_item_code);
ALTER TABLE farms.farm_crop_unit_conversn_fctrs ADD CONSTRAINT farm_cucf_uk UNIQUE (inventory_item_code,target_crop_unit_code);
ALTER TABLE farms.farm_crop_unit_conversn_fctrs ADD CONSTRAINT farm_cucf_pk PRIMARY KEY (crop_unit_conversion_factor_id);
ALTER TABLE farms.farm_crop_unit_conversn_fctrs ALTER COLUMN crop_unit_conversion_factor_id SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_conversn_fctrs ALTER COLUMN conversion_factor SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_conversn_fctrs ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_conversn_fctrs ALTER COLUMN target_crop_unit_code SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_conversn_fctrs ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_conversn_fctrs ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_conversn_fctrs ALTER COLUMN when_created SET NOT NULL;
