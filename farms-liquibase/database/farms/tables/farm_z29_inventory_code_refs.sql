CREATE TABLE farms.farm_z29_inventory_code_refs (
	inventory_code integer NOT NULL,
	inventory_type_code smallint NOT NULL,
	inventory_desc varchar(254) NOT NULL,
	inventory_type_description varchar(254) NOT NULL,
	inventory_group_code smallint,
	inventory_group_description varchar(254),
	market_commodity_ind varchar(1) NOT NULL DEFAULT 'Y',
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z29_inventory_code_refs IS E'Z29 INVENTORY REF identifies a list of inventory (commodity) codes, and their associted descriptions used by FIPD. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.inventory_code IS E'INVENTORY CODE is a numeric code used to uniquely identify an inventory item.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.inventory_desc IS E'INVENTORY DESC is the english description of the Inventory. An English text description of an inventory item.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.inventory_group_code IS E'INVENTORY GROUP CODE is a number assigned to a group of inventory items.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.inventory_group_description IS E'INVENTORY GROUP DESCRIPTION is a description for the inventory group code.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.inventory_type_code IS E'INVENTORY TYPE CODE is a numeric code indicating an inventory type. Valid values are 1 - Crops Inventory, 2 - Livestock  Inventory, 3 - Purchased Inputs, 4 - Deferred Income and Receivables, 5 - Accounts Payable.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.inventory_type_description IS E'INVENTORY TYPE DESCRIPTION is a description of the inventory type.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.market_commodity_ind IS E'MARKET COMMODITY IND indicates whether the commodity is a market commodity. An example of a non-market commodity are animals that are only used for breeding.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z29_inventory_code_refs.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_z29_inventory_code_refs ADD CONSTRAINT farm_z29_pk PRIMARY KEY (inventory_code,inventory_type_code);
ALTER TABLE farms.farm_z29_inventory_code_refs ADD CONSTRAINT avcon_1299283225_marke_000 CHECK (market_commodity_ind in ('N', 'Y'));
ALTER TABLE farms.farm_z29_inventory_code_refs ALTER COLUMN inventory_code SET NOT NULL;
ALTER TABLE farms.farm_z29_inventory_code_refs ALTER COLUMN inventory_type_code SET NOT NULL;
ALTER TABLE farms.farm_z29_inventory_code_refs ALTER COLUMN inventory_desc SET NOT NULL;
ALTER TABLE farms.farm_z29_inventory_code_refs ALTER COLUMN inventory_type_description SET NOT NULL;
ALTER TABLE farms.farm_z29_inventory_code_refs ALTER COLUMN market_commodity_ind SET NOT NULL;
ALTER TABLE farms.farm_z29_inventory_code_refs ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z29_inventory_code_refs ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z29_inventory_code_refs ALTER COLUMN when_created SET NOT NULL;
