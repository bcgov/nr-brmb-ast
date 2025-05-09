CREATE TABLE import_log(
    import_log_id              numeric(10, 0)    NOT NULL,
    log_message                varchar(4000)     NOT NULL,
    import_version_id          numeric(10, 0)    NOT NULL,
    program_year_version_id    numeric(10, 0)
) TABLESPACE pg_default
;



COMMENT ON COLUMN import_log.import_log_id IS 'IMPORT LOG ID is a surrogate unique identifier for IMPORT LOGs.'
;
COMMENT ON COLUMN import_log.log_message IS 'USER COMMENT are notes that may be optionally provided by the user about the PROGRAM YEAR for the given client.'
;
COMMENT ON COLUMN import_log.import_version_id IS 'IMPORT VERSION ID is a surrogate unique identifier for IMPORT VERSIONs.'
;
COMMENT ON COLUMN import_log.program_year_version_id IS 'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.'
;
COMMENT ON TABLE import_log IS 'IMPORT LOG lists all the audit messages generated during an import. It is used to minimize a CLOB append performance problem. Every time an import is done all the IMPORT LOGs will be deleted.'
;


CREATE INDEX ix_il_ivi ON import_log(import_version_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_il_pyvi ON import_log(program_year_version_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_il_ivi_pyvi ON import_log(import_version_id, program_year_version_id)
 TABLESPACE pg_default
;

ALTER TABLE import_log ADD 
    CONSTRAINT pk_il PRIMARY KEY (import_log_id) USING INDEX TABLESPACE pg_default 
;
