CREATE TABLE farms.farm_persons(
    person_id         numeric(10, 0)    NOT NULL,
    address_line_1    varchar(80),
    address_line_2    varchar(80),
    city              varchar(80),
    province_state    varchar(2),
    postal_code       varchar(6),
    country           varchar(3),
    daytime_phone     varchar(20),
    evening_phone     varchar(20),
    fax_number        varchar(20),
    cell_number       varchar(20),
    corp_name         varchar(100),
    first_name        varchar(100),
    last_name         varchar(100),
    email_address     varchar(320),
    revision_count    numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created       varchar(30)       NOT NULL,
    when_created       timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated       varchar(30),
    when_updated       timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_persons.person_id IS 'PERSON ID is a surrogate unique identifier for PERSONs.'
;
COMMENT ON COLUMN farms.farm_persons.address_line_1 IS 'ADDRESS LINE 1 is the first line of the Contact Mailing Address.'
;
COMMENT ON COLUMN farms.farm_persons.address_line_2 IS 'ADDRESS LINE 2 is the second line of the Contact Mailing Address.'
;
COMMENT ON COLUMN farms.farm_persons.city IS 'CITY is the Contact Mailing Address City.'
;
COMMENT ON COLUMN farms.farm_persons.province_state IS 'PROVINCE STATE for the contact mailing address.'
;
COMMENT ON COLUMN farms.farm_persons.postal_code IS 'POSTAL CODE for the Contact Mailing Address.'
;
COMMENT ON COLUMN farms.farm_persons.country IS 'COUNTRY is the mailing address country.'
;
COMMENT ON COLUMN farms.farm_persons.daytime_phone IS 'DAYTIME PHONE is the day time telephone number of the participant.'
;
COMMENT ON COLUMN farms.farm_persons.evening_phone IS 'EVENING PHONE is the evening telephone number of the participant.'
;
COMMENT ON COLUMN farms.farm_persons.fax_number IS 'FAX NUMBER is the FAX NUMBER of the participant.'
;
COMMENT ON COLUMN farms.farm_persons.cell_number IS 'CELL NUMBER is the cell phone number of the PERSON.'
;
COMMENT ON COLUMN farms.farm_persons.corp_name IS 'CORP NAME is the Contact Business Name, as provided by the participant.'
;
COMMENT ON COLUMN farms.farm_persons.first_name IS 'FIRST NAME of the contact.'
;
COMMENT ON COLUMN farms.farm_persons.last_name IS 'LAST NAME of the contact.'
;
COMMENT ON COLUMN farms.farm_persons.email_address IS 'EMAIL ADDRESS of the contact.'
;
COMMENT ON COLUMN farms.farm_persons.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_persons.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_persons.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_persons.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_persons.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_persons IS 'PERSON contains address and contact information for an AGRISTABILITY CLIENT or AGRISTABILITY REPRESENTATIVE.'
;


CREATE INDEX ix_p_c ON farms.farm_persons(city)
 TABLESPACE pg_default
;
CREATE INDEX ix_p_pc ON farms.farm_persons(postal_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_persons ADD 
    CONSTRAINT pk_p PRIMARY KEY (person_id) USING INDEX TABLESPACE pg_default 
;
