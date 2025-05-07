CREATE TABLE z28_production_insurance_reference(
    production_unit                numeric(4, 0)    NOT NULL,
    production_unit_description    varchar(256)     NOT NULL,
    revision_count                 numeric(5, 0)    DEFAULT 1 NOT NULL,
    create_user                    varchar(30)      NOT NULL,
    create_date                    timestamp(6)     DEFAULT systimestamp NOT NULL,
    update_user                    varchar(30),
    update_date                    timestamp(6)     DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN z28_production_insurance_reference.production_unit IS 'PRODUCTION UNIT is the unit of measure code.'
;
COMMENT ON COLUMN z28_production_insurance_reference.production_unit_description IS 'PRODUCTION UNIT DESCRIPTION is the unit of measure description.'
;
COMMENT ON COLUMN z28_production_insurance_reference.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN z28_production_insurance_reference.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN z28_production_insurance_reference.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN z28_production_insurance_reference.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN z28_production_insurance_reference.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE z28_production_insurance_reference IS 'Z28 PRODUCTION INSURANCE REFERENCE identifies the reference file containing a list of the units of measure, and associated descriptions. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


ALTER TABLE z28_production_insurance_reference ADD 
    CONSTRAINT pk_zpir PRIMARY KEY (production_unit) USING INDEX TABLESPACE pg_default 
;
