CREATE TABLE farms.farm_persons (
	person_id bigint NOT NULL,
	address_line_1 varchar(80),
	address_line_2 varchar(80),
	city varchar(80),
	province_state varchar(2),
	postal_code varchar(6),
	country varchar(3),
	daytime_phone varchar(20),
	evening_phone varchar(20),
	fax_number varchar(20),
	cell_number varchar(20),
	corp_name varchar(100),
	first_name varchar(100),
	last_name varchar(100),
	email_address varchar(320),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_persons IS E'PERSON contains address and contact information for an AGRISTABILITY CLIENT or AGRISTABILITY REPRESENTATIVE.';
COMMENT ON COLUMN farms.farm_persons.address_line_1 IS E'ADDRESS LINE 1 is the first line of the Contact Mailing Address.';
COMMENT ON COLUMN farms.farm_persons.address_line_2 IS E'ADDRESS LINE 2 is the second line of the Contact Mailing Address.';
COMMENT ON COLUMN farms.farm_persons.cell_number IS E'CELL NUMBER is the cell phone number of the PERSON.';
COMMENT ON COLUMN farms.farm_persons.city IS E'CITY is the Contact Mailing Address City.';
COMMENT ON COLUMN farms.farm_persons.corp_name IS E'CORP NAME is the Contact Business Name, as provided by the participant.';
COMMENT ON COLUMN farms.farm_persons.country IS E'COUNTRY is the mailing address country.';
COMMENT ON COLUMN farms.farm_persons.daytime_phone IS E'DAYTIME PHONE is the day time telephone number of the participant.';
COMMENT ON COLUMN farms.farm_persons.email_address IS E'EMAIL ADDRESS of the contact.';
COMMENT ON COLUMN farms.farm_persons.evening_phone IS E'EVENING PHONE is the evening telephone number of the participant.';
COMMENT ON COLUMN farms.farm_persons.fax_number IS E'FAX NUMBER is the FAX NUMBER of the participant.';
COMMENT ON COLUMN farms.farm_persons.first_name IS E'FIRST NAME of the contact.';
COMMENT ON COLUMN farms.farm_persons.last_name IS E'LAST NAME of the contact.';
COMMENT ON COLUMN farms.farm_persons.person_id IS E'PERSON ID is a surrogate unique identifier for PERSONs.';
COMMENT ON COLUMN farms.farm_persons.postal_code IS E'POSTAL CODE for the Contact Mailing Address.';
COMMENT ON COLUMN farms.farm_persons.province_state IS E'PROVINCE STATE for the contact mailing address.';
COMMENT ON COLUMN farms.farm_persons.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_persons.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_persons.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_persons.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_persons.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_per_city_i ON farms.farm_persons (city);
CREATE INDEX farm_per_pc_i ON farms.farm_persons (postal_code);
ALTER TABLE farms.farm_persons ADD CONSTRAINT farm_per_pk PRIMARY KEY (person_id);
ALTER TABLE farms.farm_persons ALTER COLUMN person_id SET NOT NULL;
ALTER TABLE farms.farm_persons ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_persons ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_persons ALTER COLUMN when_created SET NOT NULL;
