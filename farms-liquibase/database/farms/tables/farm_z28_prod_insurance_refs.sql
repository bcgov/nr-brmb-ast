CREATE TABLE farms.farm_z28_prod_insurance_refs (
	production_unit smallint NOT NULL,
	production_unit_description varchar(256) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z28_prod_insurance_refs IS E'Z28 PROD INSURANCE REF identifies the reference file containing a list of the units of measure, and associated descriptions. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.production_unit IS E'PRODUCTION UNIT is the unit of measure code.';
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.production_unit_description IS E'PRODUCTION UNIT DESCRIPTION is the unit of measure description.';
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z28_prod_insurance_refs.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_z28_prod_insurance_refs ADD CONSTRAINT farm_z28_pk PRIMARY KEY (production_unit);
ALTER TABLE farms.farm_z28_prod_insurance_refs ALTER COLUMN production_unit SET NOT NULL;
ALTER TABLE farms.farm_z28_prod_insurance_refs ALTER COLUMN production_unit_description SET NOT NULL;
ALTER TABLE farms.farm_z28_prod_insurance_refs ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z28_prod_insurance_refs ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z28_prod_insurance_refs ALTER COLUMN when_created SET NOT NULL;
