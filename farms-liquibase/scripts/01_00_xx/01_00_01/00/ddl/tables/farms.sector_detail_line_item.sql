CREATE TABLE farms.farm_sector_detail_line_items(
    sector_detail_line_item_id    numeric(10, 0)    NOT NULL,
    program_year                  numeric(4, 0)     NOT NULL,
    line_item                     numeric(4, 0)     NOT NULL,
    sector_detail_code            varchar(10)       NOT NULL,
    revision_count                numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                   varchar(30)       NOT NULL,
    when_created                   timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                   varchar(30),
    when_updated                   timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_sector_detail_line_items.sector_detail_line_item_id IS 'SECTOR DETAIL LINE ITEM ID is a surrogate unique identifier for a SECTOR DETAIL LINE ITEM.'
;
COMMENT ON COLUMN farms.farm_sector_detail_line_items.program_year IS 'PROGRAM YEAR is the applicable PROGRAM YEAR  for this record.'
;
COMMENT ON COLUMN farms.farm_sector_detail_line_items.line_item IS 'LINE ITEM is an income or expense item for Agristability.'
;
COMMENT ON COLUMN farms.farm_sector_detail_line_items.sector_detail_code IS 'SECTOR DETAIL CODE is a unique code for the object SECTOR DETAIL CODE described as a character code used to uniquely identify the detailed sector of the AGRIBILITY SCENARIO. Examples of codes and descriptions are APPLES - Apples, BERRIES - Berries, BLUEB - Blueberries, and CATTLE - Cattle.'
;
COMMENT ON COLUMN farms.farm_sector_detail_line_items.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_sector_detail_line_items.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_sector_detail_line_items.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_sector_detail_line_items.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_sector_detail_line_items.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;


CREATE INDEX ix_sdli_sdc ON farms.farm_sector_detail_line_items(sector_detail_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_sdli_py ON farms.farm_sector_detail_line_items(program_year)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_sdli_li_py_sdc ON farms.farm_sector_detail_line_items(line_item, program_year, sector_detail_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_sector_detail_line_items ADD 
    CONSTRAINT pk_sdli PRIMARY KEY (sector_detail_line_item_id) USING INDEX TABLESPACE pg_default 
;
