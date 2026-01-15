CREATE TABLE farms.farm_tip_report_documents (
	tip_report_document_id bigint NOT NULL,
	alignment_key varchar(2) NOT NULL,
	generation_date timestamp(0) NOT NULL,
	document bytea NOT NULL,
	program_year_id bigint NOT NULL,
	farming_operation_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_tip_report_documents IS E'TIP REPORT DOCUMENT is the TIP Report that will be provided to a farming business.';
COMMENT ON COLUMN farms.farm_tip_report_documents.alignment_key IS E'ALIGNMENT KEY is used to align the same FARMING OPERATION across multiple years.';
COMMENT ON COLUMN farms.farm_tip_report_documents.document IS E'DOCUMENT is the PDF for the ''TIP Report'' sent to the grower.';
COMMENT ON COLUMN farms.farm_tip_report_documents.farming_operation_id IS E'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATION.';
COMMENT ON COLUMN farms.farm_tip_report_documents.generation_date IS E'GENERATION DATE is the date the document was generated.';
COMMENT ON COLUMN farms.farm_tip_report_documents.program_year_id IS E'PROGRAM YEAR ID is a surrogate unique identifier for PROGRAM YEAR.';
COMMENT ON COLUMN farms.farm_tip_report_documents.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_tip_report_documents.tip_report_document_id IS E'TIP REPORT DOCUMENT ID is a surrogate unique identifier for TIP REPORT DOCUMENTs.';
COMMENT ON COLUMN farms.farm_tip_report_documents.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_tip_report_documents.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_tip_report_documents.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_tip_report_documents.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_tip_report_documents ADD CONSTRAINT farm_trd_fo_uk UNIQUE (farming_operation_id);
ALTER TABLE farms.farm_tip_report_documents ADD CONSTRAINT farm_trd_py_ak_uk UNIQUE (program_year_id,alignment_key);
ALTER TABLE farms.farm_tip_report_documents ADD CONSTRAINT farm_trd_pk PRIMARY KEY (tip_report_document_id);
ALTER TABLE farms.farm_tip_report_documents ALTER COLUMN tip_report_document_id SET NOT NULL;
ALTER TABLE farms.farm_tip_report_documents ALTER COLUMN alignment_key SET NOT NULL;
ALTER TABLE farms.farm_tip_report_documents ALTER COLUMN generation_date SET NOT NULL;
ALTER TABLE farms.farm_tip_report_documents ALTER COLUMN document SET NOT NULL;
ALTER TABLE farms.farm_tip_report_documents ALTER COLUMN program_year_id SET NOT NULL;
ALTER TABLE farms.farm_tip_report_documents ALTER COLUMN farming_operation_id SET NOT NULL;
ALTER TABLE farms.farm_tip_report_documents ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_tip_report_documents ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_tip_report_documents ALTER COLUMN when_created SET NOT NULL;
