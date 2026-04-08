CREATE TABLE farms.farm_federal_accounting_codes (
	federal_accounting_code varchar(10) NOT NULL,
	description varchar(256) NOT NULL,
	established_date timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	expiry_date timestamp(0) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_federal_accounting_codes IS E'The FEDERAL ACCOUNTING CODE is the method of accounting to Revenue Canada & Taxation (RCT) and the federal Agristability application (NISA).';
COMMENT ON COLUMN farms.farm_federal_accounting_codes.description IS E'DESCRIPTION is a textual description of the code value.';
COMMENT ON COLUMN farms.farm_federal_accounting_codes.established_date IS E'ESTABLISHED DATE identifies the effective date of the record.';
COMMENT ON COLUMN farms.farm_federal_accounting_codes.expiry_date IS E'EXPIRY DATE identifies the date this record is no longer valid.';
COMMENT ON COLUMN farms.farm_federal_accounting_codes.federal_accounting_code IS E'FEDERAL ACCOUNTING CODE is a unique code for the object FEDERAL ACCOUNTING CODE described as a numeric code used to uniquely identify the method of Accounting to RCT and NISA.  Examples of codes and descriptions are 1- Accrual, 2-Cash.';
COMMENT ON COLUMN farms.farm_federal_accounting_codes.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_federal_accounting_codes.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_federal_accounting_codes.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_federal_accounting_codes.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_federal_accounting_codes.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_federal_accounting_codes ADD CONSTRAINT farm_aac_pk PRIMARY KEY (federal_accounting_code);
ALTER TABLE farms.farm_federal_accounting_codes ALTER COLUMN federal_accounting_code SET NOT NULL;
ALTER TABLE farms.farm_federal_accounting_codes ALTER COLUMN description SET NOT NULL;
ALTER TABLE farms.farm_federal_accounting_codes ALTER COLUMN established_date SET NOT NULL;
ALTER TABLE farms.farm_federal_accounting_codes ALTER COLUMN expiry_date SET NOT NULL;
ALTER TABLE farms.farm_federal_accounting_codes ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_federal_accounting_codes ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_federal_accounting_codes ALTER COLUMN when_created SET NOT NULL;
