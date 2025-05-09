CREATE TABLE tip_farm_type_1_lookup(
    tip_farm_type_1_lookup_id    numeric(10, 0)    NOT NULL,
    farm_type_1_name             varchar(256)      NOT NULL,
    tip_farm_type_2_lookup_id    numeric(10, 0)    NOT NULL,
    revision_count               numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                  varchar(30)       NOT NULL,
    create_date                  timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                  varchar(30),
    update_date                  timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN tip_farm_type_1_lookup.tip_farm_type_1_lookup_id IS 'TIP FARM TYPE 1 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 1 LOOKUP.'
;
COMMENT ON COLUMN tip_farm_type_1_lookup.farm_type_1_name IS 'FARM TYPE 1 NAME is a textual description of the lowest level of TIP farm types.'
;
COMMENT ON COLUMN tip_farm_type_1_lookup.tip_farm_type_2_lookup_id IS 'TIP FARM TYPE 2 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 2 LOOKUP.'
;
COMMENT ON COLUMN tip_farm_type_1_lookup.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN tip_farm_type_1_lookup.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN tip_farm_type_1_lookup.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN tip_farm_type_1_lookup.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN tip_farm_type_1_lookup.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE tip_farm_type_1_lookup IS 'TIP FARM TYPE 1 LOOKUP indicates how a farm gets the majority of its income. There are three levels of farm type. This is the lowest. The top level is TIP FARM TYPE CODE. The third level is TIP FARM SUB TYPE B. A farm''''s type is determined by summing up its REPORTED INCOME EXPENSE items and mapping the income LINE ITEM number to a TIP FARM TYPE 1 LOOKUP.'
;


CREATE INDEX ix_tft1l_tft2li ON tip_farm_type_1_lookup(tip_farm_type_2_lookup_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_tft1l_ft1n ON tip_farm_type_1_lookup(farm_type_1_name)
 TABLESPACE pg_default
;

ALTER TABLE tip_farm_type_1_lookup ADD 
    CONSTRAINT pk_tft1l PRIMARY KEY (tip_farm_type_1_lookup_id) USING INDEX TABLESPACE pg_default 
;
