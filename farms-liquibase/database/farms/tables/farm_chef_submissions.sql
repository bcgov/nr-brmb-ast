CREATE TABLE farms.farm_chef_submissions (
	chef_submission_id bigint NOT NULL,
	chef_submission_guid varchar(100) NOT NULL,
	validation_task_guid varchar(100),
	main_task_guid varchar(100),
	bceid_form_ind varchar(1),
	chef_form_type_code varchar(10) NOT NULL,
	chef_submssn_status_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_chef_submissions IS E'CHEF SUBMISSION is the sumbission of a form by a user.';
COMMENT ON COLUMN farms.farm_chef_submissions.bceid_form_ind IS E'BCEID FORM IND identifies if the form submission came from the BCeID version of the CHEF FORM TYPE CODE rather than the IDIR version.';
COMMENT ON COLUMN farms.farm_chef_submissions.chef_form_type_code IS E'CHEF FORM TYPE CODE is a unique code for the object FORM TYPE CODE. Examples of codes and descriptions are NOL - Notice of Loss, NPP - New Participant Profile, INTERIM - Interim.';
COMMENT ON COLUMN farms.farm_chef_submissions.chef_submission_guid IS E'CHEF SUBMISSION GUID is a surrogate unique identifier for the CHEF SUBMISSION in the forms system (CHEFS).';
COMMENT ON COLUMN farms.farm_chef_submissions.chef_submission_id IS E'CHEF SUBMISSION ID is a surrogate unique identifier for an CHEF SUBMISSION.';
COMMENT ON COLUMN farms.farm_chef_submissions.chef_submssn_status_code IS E'CHEF SUBMSSN STATUS CODE is a unique code for the object CHEF SUBMSSN STATUS CODE. Examples of codes and descriptions are ADMIN - Waiting for Admin, PROCESSED - Processed, INVALID - Validation Failed.';
COMMENT ON COLUMN farms.farm_chef_submissions.main_task_guid IS E'MAIN TASK GUID is a surrogate unique identifier for the main task in the CRM. A non-null value indicates that there were validation errors detected when processing the form.';
COMMENT ON COLUMN farms.farm_chef_submissions.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_chef_submissions.validation_task_guid IS E'VALIDATION TASK GUID is a surrogate unique identifier for the validation task in the CRM. A non-null value indicates that there were validation errors detected when processing the form.';
COMMENT ON COLUMN farms.farm_chef_submissions.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_chef_submissions.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_chef_submissions.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_chef_submissions.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_cs_farm_fssc_fk_i ON farms.farm_chef_submissions (chef_submssn_status_code);
CREATE INDEX farm_cs_farm_ftc_fk_i ON farms.farm_chef_submissions (chef_form_type_code);
ALTER TABLE farms.farm_chef_submissions ADD CONSTRAINT farm_cs_pk PRIMARY KEY (chef_submission_id);
ALTER TABLE farms.farm_chef_submissions ADD CONSTRAINT farm_cs_fsg_uk UNIQUE (chef_submission_guid);
ALTER TABLE farms.farm_chef_submissions ALTER COLUMN chef_submission_id SET NOT NULL;
ALTER TABLE farms.farm_chef_submissions ALTER COLUMN chef_submission_guid SET NOT NULL;
ALTER TABLE farms.farm_chef_submissions ALTER COLUMN chef_form_type_code SET NOT NULL;
ALTER TABLE farms.farm_chef_submissions ALTER COLUMN chef_submssn_status_code SET NOT NULL;
ALTER TABLE farms.farm_chef_submissions ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_chef_submissions ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_chef_submissions ALTER COLUMN when_created SET NOT NULL;
