CREATE TABLE document_template(
    document_template_id    numeric(10, 0)    NOT NULL,
    template_name           varchar(100)      NOT NULL,
    template_content        text              NOT NULL,
    revision_count          numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user             varchar(30)       NOT NULL,
    create_date             timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user             varchar(30),
    update_date             timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN document_template.document_template_id IS 'DOCUMENT TEMPLATE ID is a surrogate unique identifier for DOCUMENT TEMPLATE.'
;
COMMENT ON COLUMN document_template.template_name IS 'TEMPLATE NAME is the name of the document template.'
;
COMMENT ON COLUMN document_template.template_content IS 'TEMPLATE CONTENT is content of the document template.'
;
COMMENT ON COLUMN document_template.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN document_template.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN document_template.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN document_template.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN document_template.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE document_template IS 'DOCUMENT TEMPLATE contains text content used as templates to create documents such as Verification Notes.'
;


CREATE UNIQUE INDEX uk_dt_tn ON document_template(template_name)
 TABLESPACE pg_default
;

ALTER TABLE document_template ADD 
    CONSTRAINT pk_dt PRIMARY KEY (document_template_id) USING INDEX TABLESPACE pg_default 
;
