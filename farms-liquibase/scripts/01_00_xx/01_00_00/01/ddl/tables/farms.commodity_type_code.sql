CREATE TABLE farms.commodity_type_code(
    commodity_type_code    varchar(10)      NOT NULL,
    description            varchar(256)     NOT NULL,
    effective_date         date             NOT NULL,
    expiry_date            date             NOT NULL,
    revision_count         numeric(5, 0)    NOT NULL,
    create_user            varchar(30)      NOT NULL,
    create_date            timestamp(6)     NOT NULL,
    update_user            varchar(30),
    update_date            timestamp(6)
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.commodity_type_code.commodity_type_code IS 'COMMODITY TYPE CODE is a unique code for the object COMMODITY TYPE CODE. Examples of codes and descriptions are GRAIN - Grain, FORAGE - Forage, FORAGESEED - Forage Seed, CATTLE - Cattle.'
;
COMMENT ON COLUMN farms.commodity_type_code.description IS 'DESCRIPTION is a textual description of the code value.'
;
COMMENT ON COLUMN farms.commodity_type_code.effective_date IS 'EFFECTIVE DATE identifies the effective date of the record.'
;
COMMENT ON COLUMN farms.commodity_type_code.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN farms.commodity_type_code.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.commodity_type_code.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.commodity_type_code.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.commodity_type_code.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.commodity_type_code.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.commodity_type_code IS 'COMMODITY TYPE CODE indicates which type of commodity an INVENTORY ITEM CODE or LINE ITEM belongs to. Examples of codes and descriptions are GRAIN - Grain, FORAGE - Forage, FORAGESEED - Forage Seed, CATTLE - Cattle.'
;

ALTER TABLE farms.commodity_type_code ADD 
    CONSTRAINT pk_ctc PRIMARY KEY (commodity_type_code) USING INDEX TABLESPACE pg_default 
;
