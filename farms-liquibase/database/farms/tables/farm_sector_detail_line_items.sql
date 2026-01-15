CREATE TABLE farms.farm_sector_detail_line_items (
	sector_detail_line_item_id bigint NOT NULL,
	program_year smallint NOT NULL,
	line_item smallint NOT NULL,
	sector_detail_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON COLUMN farms.farm_sector_detail_line_items.line_item IS E'LINE ITEM is an income or expense item for Agristability.';
COMMENT ON COLUMN farms.farm_sector_detail_line_items.program_year IS E'PROGRAM YEAR is the applicable PROGRAM YEAR  for this record.';
COMMENT ON COLUMN farms.farm_sector_detail_line_items.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_sector_detail_line_items.sector_detail_code IS E'SECTOR DETAIL CODE is a unique code for the object SECTOR DETAIL CODE described as a character code used to uniquely identify the detailed sector of the AGRIBILITY SCENARIO. Examples of codes and descriptions are APPLES - Apples, BERRIES - Berries, BLUEB - Blueberries, and CATTLE - Cattle.';
COMMENT ON COLUMN farms.farm_sector_detail_line_items.sector_detail_line_item_id IS E'SECTOR DETAIL LINE ITEM ID is a surrogate unique identifier for a SECTOR DETAIL LINE ITEM.';
COMMENT ON COLUMN farms.farm_sector_detail_line_items.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_sector_detail_line_items.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_sector_detail_line_items.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_sector_detail_line_items.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_sdli_farm_sdc_fk_i ON farms.farm_sector_detail_line_items (sector_detail_code);
CREATE INDEX farm_sdli_year_i ON farms.farm_sector_detail_line_items (program_year);
ALTER TABLE farms.farm_sector_detail_line_items ADD CONSTRAINT farm_sdli_pk PRIMARY KEY (sector_detail_line_item_id);
ALTER TABLE farms.farm_sector_detail_line_items ADD CONSTRAINT farm_sdli_uk UNIQUE (line_item,program_year,sector_detail_code);
ALTER TABLE farms.farm_sector_detail_line_items ALTER COLUMN sector_detail_line_item_id SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_line_items ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_line_items ALTER COLUMN line_item SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_line_items ALTER COLUMN sector_detail_code SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_line_items ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_line_items ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_line_items ALTER COLUMN when_created SET NOT NULL;
