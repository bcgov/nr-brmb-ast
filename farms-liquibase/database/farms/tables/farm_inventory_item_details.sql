CREATE TABLE farms.farm_inventory_item_details (
	inventory_item_detail_id bigint NOT NULL,
	program_year smallint NOT NULL,
	eligibility_ind varchar(1) NOT NULL,
	line_item smallint,
	insurable_value decimal(13,3),
	premium_rate decimal(13,4),
	inventory_item_code varchar(10),
	commodity_type_code varchar(10),
	fruit_veg_type_code varchar(10),
	multi_stage_commdty_code varchar(10),
	url_id bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_inventory_item_details IS E'INVENTORY ITEM DETAIL is additional information about an INVENTORY ITEM CODE.';
COMMENT ON COLUMN farms.farm_inventory_item_details.commodity_type_code IS E'COMMODITY TYPE CODE is a unique code for the object COMMODITY TYPE CODE. Examples of codes and descriptions are GRAIN - Grain, FORAGE - Forage, FORAGESEED - Forage Seed, CATTLE - Cattle.';
COMMENT ON COLUMN farms.farm_inventory_item_details.eligibility_ind IS E'ELIGIBILITY IND identifies if the INVENTORY ITEM CODE is eligible in the program year.';
COMMENT ON COLUMN farms.farm_inventory_item_details.fruit_veg_type_code IS E'FRUIT VEG TYPE CODE is a unique code for the object FRUIT VEG TYPE CODE. Examples of codes and descriptions are APPLE - Apples, BEAN - Beans,  POTATO - Potatoes.';
COMMENT ON COLUMN farms.farm_inventory_item_details.insurable_value IS E'INSURABLE VALUE is the dollar amount of insurance per unit that the AgriStability program requires the participant to carry.';
COMMENT ON COLUMN farms.farm_inventory_item_details.inventory_item_code IS E'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).';
COMMENT ON COLUMN farms.farm_inventory_item_details.inventory_item_detail_id IS E'INVENTORY ITEM DETAIL ID is a surrogate unique identifier for INVENTORY ITEM DETAILs.';
COMMENT ON COLUMN farms.farm_inventory_item_details.line_item IS E'LINE ITEM is income or expense item for Agristability.';
COMMENT ON COLUMN farms.farm_inventory_item_details.multi_stage_commdty_code IS E'MULTI STAGE COMMDTY CODE is a unique code for the object MULTI STAGE COMMDTY CODE. Examples of codes and descriptions are APPLE - Apples, GRAPE - Grapes, CRANBERRY - Cranberries.';
COMMENT ON COLUMN farms.farm_inventory_item_details.premium_rate IS E'PREMIUM RATE is the main portion of the premium the participant pays for insurance of their production it is the percentage of the total dollar value they have insured for this INVENTORY ITEM CODE.';
COMMENT ON COLUMN farms.farm_inventory_item_details.program_year IS E'PROGRAM YEAR is the applicable PROGRAM YEAR  for this record.';
COMMENT ON COLUMN farms.farm_inventory_item_details.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_inventory_item_details.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_inventory_item_details.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_inventory_item_details.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_inventory_item_details.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
COMMENT ON COLUMN farms.farm_inventory_item_details.url_id IS E'URL ID is a foreign key to FARM_URLS.';
CREATE INDEX farm_iid_elig_ind_i ON farms.farm_inventory_item_details (program_year, eligibility_ind);
CREATE INDEX farm_iid_farm_ctc_fk_i ON farms.farm_inventory_item_details (commodity_type_code);
CREATE INDEX farm_iid_farm_fvtc_fk_i ON farms.farm_inventory_item_details (fruit_veg_type_code);
CREATE INDEX farm_iid_farm_ic_fk_i ON farms.farm_inventory_item_details (inventory_item_code);
CREATE INDEX farm_iid_farm_mscc_fk_i ON farms.farm_inventory_item_details (multi_stage_commdty_code);
CREATE INDEX farm_iid_line_item_i ON farms.farm_inventory_item_details (program_year, line_item);
ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_pk PRIMARY KEY (inventory_item_detail_id);
ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_uk UNIQUE (program_year,inventory_item_code);
ALTER TABLE farms.farm_inventory_item_details ADD CONSTRAINT farm_iid_elig_chk CHECK (eligibility_ind in ('N', 'Y'));
ALTER TABLE farms.farm_inventory_item_details ALTER COLUMN inventory_item_detail_id SET NOT NULL;
ALTER TABLE farms.farm_inventory_item_details ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_inventory_item_details ALTER COLUMN eligibility_ind SET NOT NULL;
ALTER TABLE farms.farm_inventory_item_details ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_inventory_item_details ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_inventory_item_details ALTER COLUMN when_created SET NOT NULL;
