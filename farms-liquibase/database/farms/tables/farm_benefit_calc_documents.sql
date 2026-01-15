CREATE TABLE farms.farm_benefit_calc_documents (
	benefit_calc_document_id bigint NOT NULL,
	generation_date TIMESTAMP(6) NOT NULL,
	document bytea NOT NULL,
	agristability_scenario_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON COLUMN farms.farm_benefit_calc_documents.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_benefit_calc_documents.benefit_calc_document_id IS E'BENEFIT CALC DOCUMENT ID is a surrogate unique identifier for BENEFIT CALC DOCUMENTs.';
COMMENT ON COLUMN farms.farm_benefit_calc_documents.document IS E'DOCUMENT is the PDF for the ''Calculation of Benefits'' notice sent to the grower.';
COMMENT ON COLUMN farms.farm_benefit_calc_documents.generation_date IS E'GENERATION DATE is the date the document was generated.';
COMMENT ON COLUMN farms.farm_benefit_calc_documents.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_benefit_calc_documents.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_documents.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_documents.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_benefit_calc_documents.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_benefit_calc_documents ADD CONSTRAINT farm_bcd_pk PRIMARY KEY (benefit_calc_document_id);
ALTER TABLE farms.farm_benefit_calc_documents ADD CONSTRAINT farm_bcd_as_uk UNIQUE (agristability_scenario_id);
ALTER TABLE farms.farm_benefit_calc_documents ALTER COLUMN benefit_calc_document_id SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_documents ALTER COLUMN generation_date SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_documents ALTER COLUMN document SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_documents ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_documents ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_documents ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_benefit_calc_documents ALTER COLUMN when_created SET NOT NULL;
