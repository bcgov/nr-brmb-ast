CREATE TABLE farms.farm_tip_rating_codes(
    tip_rating_code     varchar(10)      NOT NULL,
    description         varchar(256)     NOT NULL,
    established_date      date             NOT NULL,
    expiry_date         date             NOT NULL,
    revision_count      numeric(5, 0)    NOT NULL,
    who_created         varchar(30)      NOT NULL,
    when_created         timestamp(6)     NOT NULL,
    who_updated         varchar(30),
    when_updated         timestamp(6)
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_tip_rating_codes.tip_rating_code IS 'TIP RATING CODE is a unique code for the object TIP RATING CODE. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern'
;
COMMENT ON COLUMN farms.farm_tip_rating_codes.description IS 'DESCRIPTION is a textual description of the code value.'
;
COMMENT ON COLUMN farms.farm_tip_rating_codes.established_date IS 'EFFECTIVE DATE identifies the effective date of the record.'
;
COMMENT ON COLUMN farms.farm_tip_rating_codes.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN farms.farm_tip_rating_codes.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_tip_rating_codes.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_tip_rating_codes.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_tip_rating_codes.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_tip_rating_codes.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_tip_rating_codes IS 'TIP RATING CODE indicates how well the farming operation is doing compared to an industry benchmark. Examples of codes and descriptions are GOOD - Good, CAUTION - Caution, CONCERN - Concern.'
;

ALTER TABLE farms.farm_tip_rating_codes ADD 
    CONSTRAINT pk_trc PRIMARY KEY (tip_rating_code) USING INDEX TABLESPACE pg_default 
;
