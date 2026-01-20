CREATE TABLE farms.farm_document_templates (
	document_template_id bigint NOT NULL,
	template_name varchar(100) NOT NULL,
	template_content text NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_document_templates IS E'DOCUMENT TEMPLATE contains text content used as templates to create documents such as Verification Notes.';
COMMENT ON COLUMN farms.farm_document_templates.document_template_id IS E'DOCUMENT TEMPLATE ID is a surrogate unique identifier for DOCUMENT TEMPLATE.';
COMMENT ON COLUMN farms.farm_document_templates.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_document_templates.template_content IS E'TEMPLATE CONTENT is content of the document template.';
COMMENT ON COLUMN farms.farm_document_templates.template_name IS E'TEMPLATE NAME is the name of the document template.';
COMMENT ON COLUMN farms.farm_document_templates.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_document_templates.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_document_templates.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_document_templates.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_document_templates ADD CONSTRAINT farm_dt_uk UNIQUE (template_name);
ALTER TABLE farms.farm_document_templates ADD CONSTRAINT farm_dt_pk PRIMARY KEY (document_template_id);
ALTER TABLE farms.farm_document_templates ALTER COLUMN document_template_id SET NOT NULL;
ALTER TABLE farms.farm_document_templates ALTER COLUMN template_name SET NOT NULL;
ALTER TABLE farms.farm_document_templates ALTER COLUMN template_content SET NOT NULL;
ALTER TABLE farms.farm_document_templates ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_document_templates ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_document_templates ALTER COLUMN when_created SET NOT NULL;
