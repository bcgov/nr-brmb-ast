CREATE TABLE multiple_stage_commodity_code(
    multiple_stage_commodity_code    varchar(10)      NOT NULL,
    description                      varchar(256)     NOT NULL,
    established_date                 date             NOT NULL,
    expiry_date                      date             NOT NULL,
    revision_count                   numeric(5, 0)    NOT NULL,
    create_user                      varchar(30)      NOT NULL,
    create_date                      timestamp(6)     NOT NULL,
    update_user                      varchar(30),
    update_date                      timestamp(6)
) TABLESPACE pg_default
;



COMMENT ON COLUMN multiple_stage_commodity_code.multiple_stage_commodity_code IS 'MULTIPLE STAGE COMMODITY CODE is a unique code for the object MULTIPLE STAGE COMMODITY CODE. Examples of codes and descriptions are APPLE - Apples, GRAPE - Grapes, CRANBERRY - Cranberries.'
;
COMMENT ON COLUMN multiple_stage_commodity_code.description IS 'DESCRIPTION is a textual description of the code value.'
;
COMMENT ON COLUMN multiple_stage_commodity_code.established_date IS 'ESTABLISHED DATE identifies the effective date of the record.'
;
COMMENT ON COLUMN multiple_stage_commodity_code.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN multiple_stage_commodity_code.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN multiple_stage_commodity_code.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN multiple_stage_commodity_code.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN multiple_stage_commodity_code.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN multiple_stage_commodity_code.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE multiple_stage_commodity_code IS 'MULTIPLE STAGE COMMODITY CODE indicates which type of multistage commodity an INVENTORY ITEM CODE is, for commodities that have separate INVENTORY ITEM CODEs at different stages of their development/growth.'
;

ALTER TABLE multiple_stage_commodity_code ADD 
    CONSTRAINT pk_mscc PRIMARY KEY (multiple_stage_commodity_code) USING INDEX TABLESPACE pg_default 
;
