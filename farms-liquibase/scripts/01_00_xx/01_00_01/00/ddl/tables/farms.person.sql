CREATE TABLE person(
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
    create_user       varchar(30)       NOT NULL,
    create_date       timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user       varchar(30),
    update_date       timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN person.person_id IS 'PERSON ID is a surrogate unique identifier for PERSONs.'
;
COMMENT ON COLUMN person.address_line_1 IS 'ADDRESS LINE 1 is the first line of the Contact Mailing Address.'
;
COMMENT ON COLUMN person.address_line_2 IS 'ADDRESS LINE 2 is the second line of the Contact Mailing Address.'
;
COMMENT ON COLUMN person.city IS 'CITY is the Contact Mailing Address City.'
;
COMMENT ON COLUMN person.province_state IS 'PROVINCE STATE for the contact mailing address.'
;
COMMENT ON COLUMN person.postal_code IS 'POSTAL CODE for the Contact Mailing Address.'
;
COMMENT ON COLUMN person.country IS 'COUNTRY is the mailing address country.'
;
COMMENT ON COLUMN person.daytime_phone IS 'DAYTIME PHONE is the day time telephone number of the participant.'
;
COMMENT ON COLUMN person.evening_phone IS 'EVENING PHONE is the evening telephone number of the participant.'
;
COMMENT ON COLUMN person.fax_number IS 'FAX NUMBER is the FAX NUMBER of the participant.'
;
COMMENT ON COLUMN person.cell_number IS 'CELL NUMBER is the cell phone number of the PERSON.'
;
COMMENT ON COLUMN person.corp_name IS 'CORP NAME is the Contact Business Name, as provided by the participant.'
;
COMMENT ON COLUMN person.first_name IS 'FIRST NAME of the contact.'
;
COMMENT ON COLUMN person.last_name IS 'LAST NAME of the contact.'
;
COMMENT ON COLUMN person.email_address IS 'EMAIL ADDRESS of the contact.'
;
COMMENT ON COLUMN person.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN person.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN person.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN person.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN person.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE person IS 'PERSON contains address and contact information for an AGRISTABILITY CLIENT or AGRISTABILITY REPRESENTATIVE.'
;


CREATE INDEX ix_p_c ON person(city)
 TABLESPACE pg_default
;
CREATE INDEX ix_p_pc ON person(postal_code)
 TABLESPACE pg_default
;
