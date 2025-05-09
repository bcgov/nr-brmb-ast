CREATE TABLE tip_farm_type_3_lookup(
    tip_farm_type_3_lookup_id    numeric(10, 0)    NOT NULL,
    farm_type_3_name             varchar(256)      NOT NULL,
    revision_count               numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                  varchar(30)       NOT NULL,
    create_date                  timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                  varchar(30),
    update_date                  timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN tip_farm_type_3_lookup.tip_farm_type_3_lookup_id IS 'TIP FARM TYPE 3 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 3 LOOKUP.'
;
COMMENT ON COLUMN tip_farm_type_3_lookup.farm_type_3_name IS 'FARM TYPE 3 NAME is a textual description of the third (and highest) level of TIP farm types.'
;
COMMENT ON COLUMN tip_farm_type_3_lookup.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN tip_farm_type_3_lookup.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN tip_farm_type_3_lookup.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN tip_farm_type_3_lookup.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN tip_farm_type_3_lookup.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE tip_farm_type_3_lookup IS 'TIP FARM TYPE 3 LOOKUP indicates how a farm gets the majority of its income. There are three levels of farm type. This is the top level. The two lower levels are TIP FARM TYPE 2 LOOKUP, and TIP FARM TYPE 1 LOOKUP. A farm''''s type is determined by summing up its REPORTED INCOME EXPENSE items and mapping the income LINE ITEM number to a FARM TYPE CODE. Examples of names are are Dairy, "Poultry, Chicken",  "Swine".'
;


CREATE UNIQUE INDEX uk_tft3l_ft3n ON tip_farm_type_3_lookup(farm_type_3_name)
 TABLESPACE pg_default
;

ALTER TABLE tip_farm_type_3_lookup ADD 
    CONSTRAINT pk_tft3l PRIMARY KEY (tip_farm_type_3_lookup_id) USING INDEX TABLESPACE pg_default 
;
