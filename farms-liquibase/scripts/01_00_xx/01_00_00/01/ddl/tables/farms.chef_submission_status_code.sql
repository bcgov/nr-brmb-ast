CREATE TABLE chef_submission_status_code(
    chef_submission_status_code    varchar(10)      NOT NULL,
    description                    varchar(256)     NOT NULL,
    effective_date                 date             NOT NULL,
    expiry_date                    date             NOT NULL,
    revision_count                 numeric(5, 0)    NOT NULL,
    create_user                    varchar(30)      NOT NULL,
    create_date                    timestamp(6)     NOT NULL,
    update_user                    varchar(30),
    update_date                    timestamp(6)
) TABLESPACE pg_default
;



COMMENT ON COLUMN chef_submission_status_code.chef_submission_status_code IS 'CHEF SUBMISSION STATUS CODE is a unique code for the object CHEF SUBMISSION STATUS CODE. Examples of codes and descriptions are ADMIN - Waiting for Admin, PROCESSED - Processed, INVALID - Validation Failed.'
;
COMMENT ON COLUMN chef_submission_status_code.description IS 'DESCRIPTION is a textual description of the code value.'
;
COMMENT ON COLUMN chef_submission_status_code.effective_date IS 'EFFECTIVE DATE identifies the effective date of the record.'
;
COMMENT ON COLUMN chef_submission_status_code.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN chef_submission_status_code.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN chef_submission_status_code.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN chef_submission_status_code.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN chef_submission_status_code.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN chef_submission_status_code.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE chef_submission_status_code IS 'CHEF SUBMISSION STATUS CODE indicates the status of a CHEF SUBMISSION created in the form system (CHEFS).'
;

ALTER TABLE chef_submission_status_code ADD 
    CONSTRAINT pk_cssc PRIMARY KEY (chef_submission_status_code) USING INDEX TABLESPACE pg_default 
;
