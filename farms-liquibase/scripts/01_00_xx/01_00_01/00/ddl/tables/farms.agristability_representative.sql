CREATE TABLE agristability_representative(
    agristability_representative_id    numeric(10, 0)    NOT NULL,
    user_guid                          varchar(32)       NOT NULL,
    userid                             varchar(64)       NOT NULL,
    revision_count                     numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                        varchar(30)       NOT NULL,
    create_date                        timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                        varchar(30),
    update_date                        timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN agristability_representative.agristability_representative_id IS 'AGRISTABILITY REPRESENTATIVE ID is a surrogate unique identifier for AGRISTABILITY REPRESENTATIVEs.'
;
COMMENT ON COLUMN agristability_representative.user_guid IS 'USER GUID is the Globally Unique Identifier of the person. The user ID is not guaranteed to be unique.'
;
COMMENT ON COLUMN agristability_representative.userid IS 'USERID is the user ID associated with the GUID that a user would use to log on with.'
;
COMMENT ON COLUMN agristability_representative.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN agristability_representative.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN agristability_representative.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN agristability_representative.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN agristability_representative.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE agristability_representative IS 'AGRISTABILITY REPRESENTATIVE is a type of user who can access AgriStability information data on behalf of specified clients . An AGRISTABILITY REPRESENTATIVE can be associated with one or more clients. An AGRISTABILITY REPRESENTATIVE may have an associated PERSON. An AGRISTABILITY REPRESENTATIVE will be identified from federal tax return data or could be supplied by BC staff.'
;

