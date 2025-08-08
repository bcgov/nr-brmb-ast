CREATE TABLE farms.farm_subscription_status_codes(
    subscription_status_code    varchar(10)      NOT NULL,
    description                 varchar(256)     NOT NULL,
    established_date              date             NOT NULL,
    expiry_date                 date             NOT NULL,
    revision_count              numeric(5, 0)    NOT NULL,
    who_created                 varchar(30)      NOT NULL,
    when_created                 timestamp(6)     NOT NULL,
    who_updated                 varchar(30),
    when_updated                 timestamp(6)
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_subscription_status_codes.subscription_status_code IS 'SUBSCRIPTION STATUS CODE identifies the status of an AGRISTABILITY SUBSCRIPTION .  Possible Values: GEN - Generated, ACT - Activated, INV - Invalidated, EXP - Expired.'
;
COMMENT ON COLUMN farms.farm_subscription_status_codes.description IS 'DESCRIPTION is a textual description of the code value.'
;
COMMENT ON COLUMN farms.farm_subscription_status_codes.established_date IS 'EFFECTIVE DATE identifies the effective date of the record.'
;
COMMENT ON COLUMN farms.farm_subscription_status_codes.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN farms.farm_subscription_status_codes.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_subscription_status_codes.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_subscription_status_codes.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_subscription_status_codes.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_subscription_status_codes.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_subscription_status_codes IS 'SUBSCRIPTION STATUS CODE identifies the status of an AGRISTABILITY SUBSCRIPTION .  Possible Values: GEN - Generated, ACT - Activated, INV - Invalidated, EXP - Expired.'
;

ALTER TABLE farms.farm_subscription_status_codes ADD 
    CONSTRAINT pk_ssc1 PRIMARY KEY (subscription_status_code) USING INDEX TABLESPACE pg_default 
;
