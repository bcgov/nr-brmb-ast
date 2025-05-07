CREATE TABLE sector_detail_xref(
    sector_detail_xref_id    numeric(10, 0)    NOT NULL,
    sector_code              varchar(10)       NOT NULL,
    sector_detail_code       varchar(10)       NOT NULL,
    revision_count           numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user              varchar(30)       NOT NULL,
    create_date              timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user              varchar(30),
    update_date              timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN sector_detail_xref.sector_detail_xref_id IS 'SECTOR DETAIL XREF ID is a surrogate unique identifier for a SECTOR DETAIL XREF.'
;
COMMENT ON COLUMN sector_detail_xref.sector_code IS 'SECTOR CODE is a unique code for the object SECTOR CODE .  Examples of codes and descriptions are FDR_CATL - Feeder Cattle, FRUIT - Fruit, GRAIN - Grain and Oilseeds.'
;
COMMENT ON COLUMN sector_detail_xref.sector_detail_code IS 'SECTOR DETAIL CODE is a unique code for the object SECTOR DETAIL CODE described as a character code used to uniquely identify the detailed sector of the AGRIBILITY SCENARIO. Examples of codes and descriptions are APPLES - Apples, BERRIES - Berries, BLUEB - Blueberries, and CATTLE - Cattle.'
;
COMMENT ON COLUMN sector_detail_xref.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN sector_detail_xref.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN sector_detail_xref.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN sector_detail_xref.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN sector_detail_xref.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;


CREATE INDEX ix_sdx_sc ON sector_detail_xref(sector_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_sdx_sdc ON sector_detail_xref(sector_detail_code)
 TABLESPACE pg_default
;
