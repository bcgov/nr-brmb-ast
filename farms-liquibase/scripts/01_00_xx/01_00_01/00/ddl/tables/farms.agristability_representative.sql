CREATE TABLE farms.farm_agristability_represntves(
    agristability_represntve_id    numeric(10, 0)    NOT NULL,
    user_guid                          varchar(32)       NOT NULL,
    userid                             varchar(64)       NOT NULL,
    revision_count                     numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                        varchar(30)       NOT NULL,
    when_created                        timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                        varchar(30),
    when_updated                        timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_agristability_represntves.agristability_represntve_id IS 'AGRISTABILITY REPRESENTATIVE ID is a surrogate unique identifier for AGRISTABILITY REPRESENTATIVEs.'
;
COMMENT ON COLUMN farms.farm_agristability_represntves.user_guid IS 'USER GUID is the Globally Unique Identifier of the person. The user ID is not guaranteed to be unique.'
;
COMMENT ON COLUMN farms.farm_agristability_represntves.userid IS 'USERID is the user ID associated with the GUID that a user would use to log on with.'
;
COMMENT ON COLUMN farms.farm_agristability_represntves.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_agristability_represntves.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_agristability_represntves.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_agristability_represntves.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_agristability_represntves.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_agristability_represntves IS 'AGRISTABILITY REPRESENTATIVE is a type of user who can access AgriStability information data on behalf of specified clients . An AGRISTABILITY REPRESENTATIVE can be associated with one or more clients. An AGRISTABILITY REPRESENTATIVE may have an associated PERSON. An AGRISTABILITY REPRESENTATIVE will be identified from federal tax return data or could be supplied by BC staff.'
;


CREATE UNIQUE INDEX uk_ar_ug ON farms.farm_agristability_represntves(user_guid)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_ar_u ON farms.farm_agristability_represntves(userid)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_agristability_represntves ADD 
    CONSTRAINT pk_ar PRIMARY KEY (agristability_represntve_id) USING INDEX TABLESPACE pg_default 
;
