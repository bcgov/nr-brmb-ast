CREATE TABLE farms.farm_office_municipality_xref (
	office_municipality_xref_id bigint NOT NULL,
	municipality_code varchar(10) NOT NULL,
	regional_office_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_office_municipality_xref IS E'OFFICE MUNICIPALITY XREF is a mapping between a MUNICIPALITY CODE and a REGIONAL OFFICE CODE. A municipality can only mapped to one office.';
COMMENT ON COLUMN farms.farm_office_municipality_xref.municipality_code IS E'MUNICIPALITY CODE denotes the municipality of the FARMSTEAD.';
COMMENT ON COLUMN farms.farm_office_municipality_xref.office_municipality_xref_id IS E'OFFICE MUNICIPALITY XREF ID is a surrogate unique identifier for an OFFICE MUNICIPALITY XREF.';
COMMENT ON COLUMN farms.farm_office_municipality_xref.regional_office_code IS E'REGIONAL OFFICE CODE is a unique code used to identify an Agristability regional office.  Examples are: ABS (Abbotsford), OLI (Oliver).';
COMMENT ON COLUMN farms.farm_office_municipality_xref.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_office_municipality_xref.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_office_municipality_xref.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_office_municipality_xref.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_office_municipality_xref.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_omx_farm_mc_fk_i ON farms.farm_office_municipality_xref (municipality_code);
CREATE INDEX farm_omx_farm_roc_fk_i ON farms.farm_office_municipality_xref (regional_office_code);
ALTER TABLE farms.farm_office_municipality_xref ADD CONSTRAINT farm_omx_pk PRIMARY KEY (office_municipality_xref_id);
ALTER TABLE farms.farm_office_municipality_xref ALTER COLUMN office_municipality_xref_id SET NOT NULL;
ALTER TABLE farms.farm_office_municipality_xref ALTER COLUMN municipality_code SET NOT NULL;
ALTER TABLE farms.farm_office_municipality_xref ALTER COLUMN regional_office_code SET NOT NULL;
ALTER TABLE farms.farm_office_municipality_xref ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_office_municipality_xref ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_office_municipality_xref ALTER COLUMN when_created SET NOT NULL;
