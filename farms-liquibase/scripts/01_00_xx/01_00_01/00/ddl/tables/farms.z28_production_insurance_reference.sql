CREATE TABLE farms.farm_z28_prod_insurance_refs(
    production_unit                numeric(4, 0)    NOT NULL,
    production_unit_description    varchar(256)     NOT NULL,
    revision_count                 numeric(5, 0)    DEFAULT 1 NOT NULL,
    who_created                    varchar(30)      NOT NULL,
    when_created                    timestamp(6)     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                    varchar(30),
    when_updated                    timestamp(6)     DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.production_unit IS 'PRODUCTION UNIT is the unit of measure code.'
;
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.production_unit_description IS 'PRODUCTION UNIT DESCRIPTION is the unit of measure description.'
;
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_z28_prod_insurance_refs IS 'Z28 PRODUCTION INSURANCE REFERENCE identifies the reference file containing a list of the units of measure, and associated descriptions. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


ALTER TABLE farms.farm_z28_prod_insurance_refs ADD 
    CONSTRAINT pk_zpir PRIMARY KEY (production_unit) USING INDEX TABLESPACE pg_default 
;
