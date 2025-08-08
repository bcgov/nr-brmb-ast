CREATE TABLE farms.farm_document_templates(
    document_template_id    numeric(10, 0)    NOT NULL,
    template_name           varchar(100)      NOT NULL,
    template_content        text              NOT NULL,
    revision_count          numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created             varchar(30)       NOT NULL,
    when_created             timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated             varchar(30),
    when_updated             timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_document_templates.document_template_id IS 'DOCUMENT TEMPLATE ID is a surrogate unique identifier for DOCUMENT TEMPLATE.'
;
COMMENT ON COLUMN farms.farm_document_templates.template_name IS 'TEMPLATE NAME is the name of the document template.'
;
COMMENT ON COLUMN farms.farm_document_templates.template_content IS 'TEMPLATE CONTENT is content of the document template.'
;
COMMENT ON COLUMN farms.farm_document_templates.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_document_templates.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_document_templates.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_document_templates.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_document_templates.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_document_templates IS 'DOCUMENT TEMPLATE contains text content used as templates to create documents such as Verification Notes.'
;


CREATE UNIQUE INDEX uk_dt_tn ON farms.farm_document_templates(template_name)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_document_templates ADD 
    CONSTRAINT pk_dt PRIMARY KEY (document_template_id) USING INDEX TABLESPACE pg_default 
;
