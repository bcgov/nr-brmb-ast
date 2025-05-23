CREATE TABLE farms.usr(
    user_id               numeric(10, 0)    NOT NULL,
    user_guid             varchar(32)       NOT NULL,
    source_directory      varchar(64)       NOT NULL,
    account_name          varchar(64),
    email_address         varchar(128),
    verifier_indicator    varchar(1)        DEFAULT 'N' NOT NULL,
    deleted_indicator     varchar(1)        DEFAULT 'N' NOT NULL,
    revision_count        numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user           varchar(30)       NOT NULL,
    create_date           timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user           varchar(30),
    update_date           timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.usr.user_id IS 'USER ID is a surrogate unique identifier for a USER.'
;
COMMENT ON COLUMN farms.usr.user_guid IS 'USER GUID is a surrogate unique identifier for the USER in the BCeID Web Service.'
;
COMMENT ON COLUMN farms.usr.source_directory IS 'SOURCE DIRECTORY is the user directory of this user.'
;
COMMENT ON COLUMN farms.usr.account_name IS 'ACCOUNT NAME is the username of the user, for example, JOHNTDOE.'
;
COMMENT ON COLUMN farms.usr.email_address IS 'EMAIL ADDRESS is the email address of the user. In the microsoft clound, including CRM, this is used as the username.'
;
COMMENT ON COLUMN farms.usr.verifier_indicator IS 'VERIFIER INDICATOR identifies if the user has verifier permissions in FARM. This is automatically determined based on the roles of the user.'
;
COMMENT ON COLUMN farms.usr.deleted_indicator IS 'DELETED INDICATOR identifies if the user is no longer active and should not be listed as a choice in the application.'
;
COMMENT ON COLUMN farms.usr.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.usr.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.usr.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.usr.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.usr.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.usr IS 'USER is a user who has access to the FARM application. Users data is pulled from the BCeID WebService using the WebADE Java API.'
;


CREATE UNIQUE INDEX uk_u_ug ON farms.usr(user_guid)
 TABLESPACE pg_default
;

ALTER TABLE farms.usr ADD 
    CONSTRAINT pk_u PRIMARY KEY (user_id) USING INDEX TABLESPACE pg_default 
;
