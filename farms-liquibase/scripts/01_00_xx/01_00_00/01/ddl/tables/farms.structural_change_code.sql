CREATE TABLE farms.farm_structural_change_codes(
    structural_change_code    varchar(10)      NOT NULL,
    description               varchar(256)     NOT NULL,
    established_date            date             NOT NULL,
    expiry_date               date             NOT NULL,
    revision_count            numeric(5, 0)    NOT NULL,
    who_created               varchar(30)      NOT NULL,
    when_created               timestamp(6)     NOT NULL,
    who_updated               varchar(30),
    when_updated               timestamp(6)
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_structural_change_codes.structural_change_code IS 'STRUCTURAL CHANGE CODE denotes the method used to create the structural change.'
;
COMMENT ON COLUMN farms.farm_structural_change_codes.description IS 'DESCRIPTION is a textual description of the code value.'
;
COMMENT ON COLUMN farms.farm_structural_change_codes.established_date IS 'EFFECTIVE DATE identifies the effective date of the record.'
;
COMMENT ON COLUMN farms.farm_structural_change_codes.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN farms.farm_structural_change_codes.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_structural_change_codes.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_structural_change_codes.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_structural_change_codes.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_structural_change_codes.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_structural_change_codes IS 'STRUCTURAL CHANGE CODE denotes the method used to create a structural change. Example codes and descriptions are: RAT-Ratio, ADD-Additive.'
;

ALTER TABLE farms.farm_structural_change_codes ADD 
    CONSTRAINT pk_scc2 PRIMARY KEY (structural_change_code) USING INDEX TABLESPACE pg_default 
;
