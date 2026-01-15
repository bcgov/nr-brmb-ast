CREATE TABLE farms.farm_sector_detail_xref (
	sector_detail_xref_id bigint NOT NULL,
	sector_code varchar(10) NOT NULL,
	sector_detail_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON COLUMN farms.farm_sector_detail_xref.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_sector_detail_xref.sector_code IS E'SECTOR CODE is a unique code for the object SECTOR CODE .  Examples of codes and descriptions are FDR_CATL - Feeder Cattle, FRUIT - Fruit, GRAIN - Grain and Oilseeds.';
COMMENT ON COLUMN farms.farm_sector_detail_xref.sector_detail_code IS E'SECTOR DETAIL CODE is a unique code for the object SECTOR DETAIL CODE described as a character code used to uniquely identify the detailed sector of the AGRIBILITY SCENARIO. Examples of codes and descriptions are APPLES - Apples, BERRIES - Berries, BLUEB - Blueberries, and CATTLE - Cattle.';
COMMENT ON COLUMN farms.farm_sector_detail_xref.sector_detail_xref_id IS E'SECTOR DETAIL XREF ID is a surrogate unique identifier for a SECTOR DETAIL XREF.';
COMMENT ON COLUMN farms.farm_sector_detail_xref.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_sector_detail_xref.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_sector_detail_xref.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_sector_detail_xref.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_sdx_farm_sc_fk_i ON farms.farm_sector_detail_xref (sector_code);
ALTER TABLE farms.farm_sector_detail_xref ADD CONSTRAINT farm_sdx_pk PRIMARY KEY (sector_detail_xref_id);
ALTER TABLE farms.farm_sector_detail_xref ADD CONSTRAINT farm_sdx_detail_uk UNIQUE (sector_detail_code);
ALTER TABLE farms.farm_sector_detail_xref ALTER COLUMN sector_detail_xref_id SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_xref ALTER COLUMN sector_code SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_xref ALTER COLUMN sector_detail_code SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_xref ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_xref ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_sector_detail_xref ALTER COLUMN when_created SET NOT NULL;
