CREATE TABLE farms.chef_submission(
    chef_submission_id             numeric(10, 0)    NOT NULL,
    chef_submission_guid           varchar(100)      NOT NULL,
    validation_task_guid           varchar(100),
    main_task_guid                 varchar(100),
    bceid_form_indicator           varchar(1),
    chef_form_type_code            varchar(10)       NOT NULL,
    chef_submission_status_code    varchar(10)       NOT NULL,
    revision_count                 numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                    varchar(30)       NOT NULL,
    create_date                    timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                    varchar(30),
    update_date                    timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.chef_submission.chef_submission_id IS 'CHEF SUBMISSION ID is a surrogate unique identifier for an CHEF SUBMISSION.'
;
COMMENT ON COLUMN farms.chef_submission.chef_submission_guid IS 'CHEF SUBMISSION GUID is a surrogate unique identifier for the CHEF SUBMISSION in the forms system (CHEFS).'
;
COMMENT ON COLUMN farms.chef_submission.validation_task_guid IS 'VALIDATION TASK GUID is a surrogate unique identifier for the validation task in the CRM. A non-null value indicates that there were validation errors detected when processing the form.'
;
COMMENT ON COLUMN farms.chef_submission.main_task_guid IS 'MAIN TASK GUID is a surrogate unique identifier for the main task in the CRM. A non-null value indicates that there were validation errors detected when processing the form.'
;
COMMENT ON COLUMN farms.chef_submission.bceid_form_indicator IS 'BCEID FORM INDICATOR identifies if the form submission came from the BCeID version of the CHEF FORM TYPE CODE rather than the IDIR version.'
;
COMMENT ON COLUMN farms.chef_submission.chef_form_type_code IS 'CHEF FORM TYPE CODE is a unique code for the object CHEF FORM TYPE CODE. Examples of codes and descriptions are NOL - Notice of Loss, NPP - New Participant Profile, INTERIM - Interim.'
;
COMMENT ON COLUMN farms.chef_submission.chef_submission_status_code IS 'CHEF SUBMISSION STATUS CODE is a unique code for the object CHEF SUBMISSION STATUS CODE. Examples of codes and descriptions are ADMIN - Waiting for Admin, PROCESSED - Processed, INVALID - Validation Failed.'
;
COMMENT ON COLUMN farms.chef_submission.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.chef_submission.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.chef_submission.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.chef_submission.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.chef_submission.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.chef_submission IS 'CHEF SUBMISSION is the submission of a form by a user.'
;


CREATE INDEX ix_cs_cssc ON farms.chef_submission(chef_submission_status_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_cs_cftc ON farms.chef_submission(chef_form_type_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_cs_csg ON farms.chef_submission(chef_submission_guid)
 TABLESPACE pg_default
;

ALTER TABLE farms.chef_submission ADD 
    CONSTRAINT pk_cs PRIMARY KEY (chef_submission_id) USING INDEX TABLESPACE pg_default 
;
