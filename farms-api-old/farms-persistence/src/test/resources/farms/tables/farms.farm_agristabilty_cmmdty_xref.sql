CREATE TABLE farms.farm_agristabilty_cmmdty_xref (
	agristabilty_cmmdty_xref_id bigint NOT NULL,
	market_commodity_ind varchar(1) NOT NULL DEFAULT 'Y',
	inventory_item_code varchar(10) NOT NULL,
	inventory_group_code varchar(10),
	inventory_class_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_agristabilty_cmmdty_xref IS E'AGRISTABILTY COMMDTY XREF is the unique production units of livestock and crops. AGRISTABILTY COMMDTY XREF is the unit by which supplementary data is described. This object refers to codified data describing the unique types of an AGRISTABILTY COMMDTY XREF.  An AGRISTABILTY COMMDTY XREF dataset originates from provincial data imports, although it will be compiled from various sources, including federal.';
COMMENT ON COLUMN farms.farm_agristabilty_cmmdty_xref.agristabilty_cmmdty_xref_id IS E'AGRISTABILTY COMMDTY XREF ID is a surrogate unique identifier for an AGRISTABILTY COMMDTY XREF.';
COMMENT ON COLUMN farms.farm_agristabilty_cmmdty_xref.inventory_class_code IS E'INVENTORY CLASS CODE is a unique code for the object INVENTORY CLASS CODE described as a numeric code used to uniquely identify an inventory type. Examples of codes and descriptions are 1 - Crops Inventory, 2 - Livestock Inventory, 3 - Purchased Inputs, 4 - Deferred Income and Receivables, 5 - Accounts Payables.';
COMMENT ON COLUMN farms.farm_agristabilty_cmmdty_xref.inventory_group_code IS E'INVENTORY GROUP CODE is a unique code for the object INVENTORY GROUP CODE described as a numeric code used to uniquely identify an inventory item group. Examples of codes and descriptions are 1 - Apples, 10 - Dry Beans, 11 - Edible Horticulture, 12 - Fadabeans, 13 - Field Peas.';
COMMENT ON COLUMN farms.farm_agristabilty_cmmdty_xref.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_agristabilty_cmmdty_xref.market_commodity_ind IS E'MARKET COMMODITY IND indicates whether the commodity is a market commodity. An example of a non-market commodity are animals that are only used for breeding.';
COMMENT ON COLUMN farms.farm_agristabilty_cmmdty_xref.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_agristabilty_cmmdty_xref.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_agristabilty_cmmdty_xref.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_agristabilty_cmmdty_xref.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_agristabilty_cmmdty_xref.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_acx_farm_icc_fk_i ON farms.farm_agristabilty_cmmdty_xref (inventory_class_code);
CREATE INDEX farm_acx_farm_ic_fk_i ON farms.farm_agristabilty_cmmdty_xref (inventory_item_code);
CREATE INDEX farm_acx_farm_igc_fk_i ON farms.farm_agristabilty_cmmdty_xref (inventory_group_code);
ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT farm_acx_uk UNIQUE (inventory_class_code,inventory_item_code);
ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT farm_acx_pk PRIMARY KEY (agristabilty_cmmdty_xref_id);
ALTER TABLE farms.farm_agristabilty_cmmdty_xref ADD CONSTRAINT avcon_1299283225_marke_001 CHECK (market_commodity_ind in ('N', 'Y'));
ALTER TABLE farms.farm_agristabilty_cmmdty_xref ALTER COLUMN agristabilty_cmmdty_xref_id SET NOT NULL;
ALTER TABLE farms.farm_agristabilty_cmmdty_xref ALTER COLUMN market_commodity_ind SET NOT NULL;
ALTER TABLE farms.farm_agristabilty_cmmdty_xref ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_agristabilty_cmmdty_xref ALTER COLUMN inventory_class_code SET NOT NULL;
ALTER TABLE farms.farm_agristabilty_cmmdty_xref ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_agristabilty_cmmdty_xref ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_agristabilty_cmmdty_xref ALTER COLUMN when_created SET NOT NULL;
