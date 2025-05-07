CREATE TABLE office_municipality_xref(
    office_municipality_xref_id    numeric(10, 0)    NOT NULL,
    municipality_code              varchar(10)       NOT NULL,
    regional_office_code           varchar(10)       NOT NULL,
    revision_count                 numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                    varchar(30)       NOT NULL,
    create_date                    timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                    varchar(30),
    update_date                    timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN office_municipality_xref.office_municipality_xref_id IS 'OFFICE MUNICIPALITY XREF ID is a surrogate unique identifier for an OFFICE MUNICIPALITY XREF.'
;
COMMENT ON COLUMN office_municipality_xref.municipality_code IS 'MUNICIPALITY CODE denotes the municipality of the FARMSTEAD.'
;
COMMENT ON COLUMN office_municipality_xref.regional_office_code IS 'REGIONAL OFFICE CODE is a unique code used to identify an Agristability regional office.  Examples are: ABS (Abbotsford), OLI (Oliver).'
;
COMMENT ON COLUMN office_municipality_xref.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN office_municipality_xref.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN office_municipality_xref.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN office_municipality_xref.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN office_municipality_xref.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE office_municipality_xref IS 'OFFICE MUNICIPALITY XREF is a mapping between a MUNICIPALITY CODE and a REGIONAL OFFICE CODE. A municipality can only mapped to one office.'
;


CREATE INDEX ix_omx_mc ON office_municipality_xref(municipality_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_omx_roc ON office_municipality_xref(regional_office_code)
 TABLESPACE pg_default
;

ALTER TABLE office_municipality_xref ADD 
    CONSTRAINT pk_omx PRIMARY KEY (office_municipality_xref_id) USING INDEX TABLESPACE pg_default 
;
