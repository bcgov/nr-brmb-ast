CREATE TABLE farms.farm_crop_unit_defaults (
	crop_unit_default_id bigint NOT NULL,
	inventory_item_code varchar(10) NOT NULL,
	crop_unit_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_crop_unit_defaults IS E'CROP UNIT DEFAULT is the base unit used when importing FAIR MARKET VALUE records. It will then be used with CROP UNIT CONVERSN FCTR to calculate and create FAIR MARKET VALUE records for other units. FAIR MARKET VALUE records may only be imported for the CROP UNIT DEFAULTs.';
COMMENT ON COLUMN farms.farm_crop_unit_defaults.crop_unit_code IS E'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.';
COMMENT ON COLUMN farms.farm_crop_unit_defaults.crop_unit_default_id IS E'CROP UNIT DEFAULT ID is a surrogate unique identifier for FMV DEFAULT UNITs.';
COMMENT ON COLUMN farms.farm_crop_unit_defaults.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_crop_unit_defaults.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_crop_unit_defaults.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_crop_unit_defaults.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_crop_unit_defaults.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_crop_unit_defaults.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_cud_farm_cuc_fk_i ON farms.farm_crop_unit_defaults (crop_unit_code);
ALTER TABLE farms.farm_crop_unit_defaults ADD CONSTRAINT farm_cud_uk UNIQUE (inventory_item_code);
ALTER TABLE farms.farm_crop_unit_defaults ADD CONSTRAINT farm_cud_pk PRIMARY KEY (crop_unit_default_id);
ALTER TABLE farms.farm_crop_unit_defaults ALTER COLUMN crop_unit_default_id SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_defaults ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_defaults ALTER COLUMN crop_unit_code SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_defaults ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_defaults ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_crop_unit_defaults ALTER COLUMN when_created SET NOT NULL;
