CREATE TABLE farms.farm_inventory_item_attributes (
	inventory_item_attrib_id bigint NOT NULL,
	inventory_item_code varchar(10) NOT NULL,
	rollup_inventory_item_code varchar(10),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_inventory_item_attributes IS E'INVENTORY ITEM ATTRIBUTE is additional information about an INVENTORY ITEM CODE.';
COMMENT ON COLUMN farms.farm_inventory_item_attributes.inventory_item_attrib_id IS E'INVENTORY ITEM ATTRIB ID is a surrogate unique identifier for INVENTORY ITEM ATTRIBUTEs.';
COMMENT ON COLUMN farms.farm_inventory_item_attributes.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_inventory_item_attributes.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_inventory_item_attributes.rollup_inventory_item_code IS E'ROLLUP INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_inventory_item_attributes.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_inventory_item_attributes.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_inventory_item_attributes.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_inventory_item_attributes.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_iia_farm_ic_fk_i ON farms.farm_inventory_item_attributes (rollup_inventory_item_code);
ALTER TABLE farms.farm_inventory_item_attributes ADD CONSTRAINT farm_iia_pk PRIMARY KEY (inventory_item_attrib_id);
ALTER TABLE farms.farm_inventory_item_attributes ADD CONSTRAINT farm_iia_uk UNIQUE (inventory_item_code);
ALTER TABLE farms.farm_inventory_item_attributes ALTER COLUMN inventory_item_attrib_id SET NOT NULL;
ALTER TABLE farms.farm_inventory_item_attributes ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_inventory_item_attributes ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_inventory_item_attributes ALTER COLUMN when_created SET NOT NULL;
ALTER TABLE farms.farm_inventory_item_attributes ALTER COLUMN inventory_item_code SET NOT NULL;
