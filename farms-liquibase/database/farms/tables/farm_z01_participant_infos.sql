CREATE TABLE farms.farm_z01_participant_infos (
	participant_pin integer NOT NULL,
	sin_ctn_bn varchar(15),
	sin varchar(9),
	business_number varchar(15),
	trust_number varchar(9),
	participant_type_code smallint NOT NULL,
	participant_language smallint NOT NULL,
	first_name varchar(30),
	last_name varchar(30),
	corp_name varchar(50),
	address_1 varchar(30),
	address_2 varchar(30),
	city varchar(26),
	province varchar(2),
	postal_code varchar(6),
	country varchar(3),
	participant_fax varchar(10),
	participant_phone_day varchar(10),
	participant_phone_evening varchar(10),
	participant_cell_number varchar(10),
	participant_email_address varchar(320),
	contact_first_name varchar(40),
	contact_last_name varchar(40),
	contact_business_name varchar(40),
	contact_address_1 varchar(30),
	contact_address_2 varchar(30),
	contact_city varchar(26),
	contact_province varchar(2),
	contact_postal_code varchar(6),
	contact_phone_day varchar(10),
	contact_fax_number varchar(10),
	contact_cell_number varchar(10),
	public_office_ind smallint,
	ident_effective_date varchar(8) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z01_participant_infos IS E'Z01 PARTICIPANT INFO lists AgriStablity Participant information, from Tax return and page 1 of the Harmonized t1273 form. There will be 1 row in this file per participant. This is a staging object used to load temporary data set before being merged into the operational data';
COMMENT ON COLUMN farms.farm_z01_participant_infos.address_1 IS E'ADDRESS 1 is the Mailing Address Line 1.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.address_2 IS E'ADDRESS 2 is the Mailing Address Line 2.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.business_number IS E'BUSINESS NUMBER is the participant Business Number . A Business Number is 9 numbers followed by RC followed by 4 more numbers (total 15 characters).';
COMMENT ON COLUMN farms.farm_z01_participant_infos.city IS E'CITY is the Mailing Address City.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_address_1 IS E'CONTACT ADDRESS 1 is the Contact Mailing Address line 1.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_address_2 IS E'CONTACT ADDRESS 2 is the Contact Mailing Address line 2.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_business_name IS E'CONTACT BUSINESS NAME is the business name of the contact as provided by the participant.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_cell_number IS E'CONTACT CELL NUMBER is the cell phone number of the contact.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_city IS E'CONTACT CITY is the Contact Mailng Address City.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_fax_number IS E'CONTACT FAX NUMBER is the fax number of the contact.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_first_name IS E'CONTACT FIRST NAME is the first name of the contact.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_last_name IS E'CONTACT LAST NAME is the last name of the contact.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_phone_day IS E'CONTACT PHONE DAY is the day time telephone number of the contact.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_postal_code IS E'CONTACT POSTAL CODE is the postal code of the contact.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.contact_province IS E'CONTACT PROVINCE is the contact mailing address province.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.corp_name IS E'CORP NAME is the corporation/commune name. (only for non-individuals).';
COMMENT ON COLUMN farms.farm_z01_participant_infos.country IS E'COUNTRY is the mailing address country.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.first_name IS E'FIRST NAME is the First name for Individuals, blank for non-individuals.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.ident_effective_date IS E'IDENT EFFECTIVE DATE is the date the address and name information was last updated. This may apply to the participant information or the contact.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.last_name IS E'LAST NAME is the surname for Individuals, blank for non-individuals.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.participant_cell_number IS E'PARTICIPANT CELL NUMBER is the cell phone number of the participant.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.participant_email_address IS E'PARTICIPANT EMAIL ADDRESS is the email address of the participant.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.participant_fax IS E'PARTICIPANT FAX is the fax number of the participant.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.participant_language IS E'PARTICIPANT LANGUAGE is the preferred language of the participant.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.participant_phone_day IS E'PARTICIPANT PHONE DAY is the day time telephone number of the participant.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.participant_phone_evening IS E'PARTICIPANT PHONE EVENING is the evening time telephone number of the participant.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.participant_type_code IS E'PARTICIPANT TYPE CODE identifies the type of participant. Must be either 1-Individual, or 2-Entity.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.postal_code IS E'POSTAL CODE is the Mailing Address zip code.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.province IS E'PROVINCE is the Mailing Address Province.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.public_office_ind IS E'PUBLIC OFFICE IND is the Public Office / AAFC Employee Indicator. Allowable Values are 0 - No box checked, 1 - Checked Yes, 2 - Checked No.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.sin IS E'SIN is the participant Social Insurance Number. A SIN is 9 numbers without an extension.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.sin_ctn_bn IS E'SIN CTN BN is the participant Social Insurance Number, Business Number or Trust Number. A SIN/SBRN is 9 numbers followed by a SBRN extension of 6 numbers (total 15 numbers).  A Trust number will be the character T followed by 8 numbers. The corporate tax number (CTN) is 8 numbers. A SIN for an individual is 9 numbers without the extension.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.trust_number IS E'TRUST NUMBER is the participant Trust Number. A Trust Number is the character T followed by 8 numbers.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z01_participant_infos.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_z01_participant_infos ADD CONSTRAINT farm_z01_pk PRIMARY KEY (participant_pin);
ALTER TABLE farms.farm_z01_participant_infos ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z01_participant_infos ALTER COLUMN participant_type_code SET NOT NULL;
ALTER TABLE farms.farm_z01_participant_infos ALTER COLUMN participant_language SET NOT NULL;
ALTER TABLE farms.farm_z01_participant_infos ALTER COLUMN ident_effective_date SET NOT NULL;
ALTER TABLE farms.farm_z01_participant_infos ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z01_participant_infos ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z01_participant_infos ALTER COLUMN when_created SET NOT NULL;
