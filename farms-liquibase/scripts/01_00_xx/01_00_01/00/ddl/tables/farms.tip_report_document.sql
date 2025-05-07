CREATE TABLE tip_report_document(
    tip_report_document_id    numeric(10, 0)    NOT NULL,
    alignment_key             varchar(2)        NOT NULL,
    generation_date           date              NOT NULL,
    document                  bytea             NOT NULL,
    program_year_id           numeric(10, 0)    NOT NULL,
    farming_operation_id      numeric(10, 0)    NOT NULL,
    revision_count            numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user               varchar(30)       NOT NULL,
    create_date               timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user               varchar(30),
    update_date               timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN tip_report_document.tip_report_document_id IS 'TIP REPORT DOCUMENT ID is a surrogate unique identifier for TIP REPORT DOCUMENTs.'
;
COMMENT ON COLUMN tip_report_document.alignment_key IS 'ALIGNMENT KEY is used to align the same FARMING OPERATION across multiple years.'
;
COMMENT ON COLUMN tip_report_document.generation_date IS 'GENERATION DATE is the date the document was generated.'
;
COMMENT ON COLUMN tip_report_document.document IS 'DOCUMENT is the PDF for the ''TIP Report'' sent to the grower.'
;
COMMENT ON COLUMN tip_report_document.program_year_id IS 'PROGRAM YEAR ID is a surrogate unique identifier for PROGRAM YEARS.'
;
COMMENT ON COLUMN tip_report_document.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN tip_report_document.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN tip_report_document.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN tip_report_document.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN tip_report_document.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN tip_report_document.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE tip_report_document IS 'TIP REPORT DOCUMENT is the TIP Report that will be provided to a farming business.'
;

