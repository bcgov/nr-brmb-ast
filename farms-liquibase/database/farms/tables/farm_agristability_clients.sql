CREATE TABLE farms.farm_agristability_clients (
	agristability_client_id bigint NOT NULL,
	federal_identifier varchar(15),
	participant_pin integer NOT NULL,
	sin varchar(9),
	business_number varchar(15),
	trust_number varchar(9),
	ident_effective_date timestamp(0) NOT NULL,
	public_office_ind varchar(1) NOT NULL,
	tip_participant_ind varchar(1) NOT NULL DEFAULT 'N',
	locally_updated_ind varchar(1) NOT NULL DEFAULT 'N',
	participant_lang_code varchar(10) NOT NULL,
	person_id bigint NOT NULL,
	person_id_client_contacted_by bigint NOT NULL,
	participant_class_code varchar(10) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_agristability_clients IS E'AGRISTABILITY CLIENT is the taxable legal-entity (e.g. individuals, companies, etc.) registered with the AgriStability program. This object refers to identification and validation information, including program PIN.  AGRISTABILITY CLIENT data will originate from and be updated via federal data imports - specifically tax return data. An AGRISTABILITY CLIENT will always be associated with one or more PROGRAM YEARs. An AGRISTABILITY CLIENT may have associated PERSONS. AGRISTABILITY CLIENT may be associated with an  AGRISTABILITY REPRESENTATIVE. AGRISTABILITY CLIENT information may be updated by Provinical AgriStability Staff.';
COMMENT ON COLUMN farms.farm_agristability_clients.agristability_client_id IS E'AGRISTABILITY CLIENT ID is a surrogate unique identifier for AGRI STABILITY CLIENTs.';
COMMENT ON COLUMN farms.farm_agristability_clients.business_number IS E'BUSINESS NUMBER is the participant Business Number . A Business Number is 9 numbers followed by RC followed by 4 more numbers (total 15 characters).';
COMMENT ON COLUMN farms.farm_agristability_clients.federal_identifier IS E'FEDERAL IDENTIFIER captures the participant Social Insurance Number, BN, or Trust Number- A CTN/SBRN is 9 numbers followed by a SBRN extension of 6 numbers (total 15 numbers).- If a T is the first of the 9 characters this  identifies a trust or a commune.- The corporate tax number (CTN) is 8 numbers.- A Social Insurance Number for an individual is 9 numbers without the extension.';
COMMENT ON COLUMN farms.farm_agristability_clients.ident_effective_date IS E'IDENT EFFECTIVE DATE is the date the address and name information was last updated. This may apply to the participant information or the contact.';
COMMENT ON COLUMN farms.farm_agristability_clients.locally_updated_ind IS E'LOCALLY UPDATED IND identifies if the record was updated after being imported into the system. If this value is ''Y'', the client has updated the AGRISTABILITY CLIENT  information; as a result, the FARM system will not allow the overwriting of that particular AGRISTABILITY CLIENT data by subsequent imports.';
COMMENT ON COLUMN farms.farm_agristability_clients.participant_class_code IS E'PARTICIPANT CLASS CODE is a unique code for the object PARTICIPANT CLASS CODE described as a numeric code used to uniquely identify the classification of the farming operation.  Examples of codes and descriptions are 1 =  INDV, 2  = CORP, or 3 = COOP.';
COMMENT ON COLUMN farms.farm_agristability_clients.participant_lang_code IS E'PARTICIPANT LANG CODE is a unique code for the object PARTICIPANT LANGUAGE CODE described as a numeric code used to uniquely identify the preferred language of the AGRISTABILITY CLIENT. Examples of codes and descriptions are  1 - English, 2 - French.';
COMMENT ON COLUMN farms.farm_agristability_clients.participant_pin IS E'PARTICIPANT PIN (Personal Information Number) is the unique AgriStability/AgriInvest pin for this producuer. Was previous CAIS PIN and NISA PIN.';
COMMENT ON COLUMN farms.farm_agristability_clients.person_id IS E'PERSON ID is a surrogate unique identifier for PERSONs.';
COMMENT ON COLUMN farms.farm_agristability_clients.person_id_client_contacted_by IS E'PERSON ID is a surrogate unique identifier for PERSONs.';
COMMENT ON COLUMN farms.farm_agristability_clients.public_office_ind IS E'PUBLIC OFFICE IND is the Public Office / AAFC Employee Indicator.';
COMMENT ON COLUMN farms.farm_agristability_clients.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_agristability_clients.sin IS E'SIN is the participant Social Insurance Number. A SIN is 9 numbers without an extension.';
COMMENT ON COLUMN farms.farm_agristability_clients.tip_participant_ind IS E'TIP PARTICIPANT IND is "Y" if the participant has requested to receive TIP reports, otherwise "N".';
COMMENT ON COLUMN farms.farm_agristability_clients.trust_number IS E'TRUST NUMBER is the participant Trust Number. A Trust Number is the character T followed by 8 numbers.';
COMMENT ON COLUMN farms.farm_agristability_clients.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_agristability_clients.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_agristability_clients.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_agristability_clients.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_asc_farm_pcc_fk_i ON farms.farm_agristability_clients (participant_class_code);
CREATE INDEX farm_asc_farm_piccb_fk_i ON farms.farm_agristability_clients (person_id_client_contacted_by);
CREATE INDEX farm_asc_farm_plc_fk_i ON farms.farm_agristability_clients (participant_lang_code);
CREATE INDEX farm_asc_pi_i ON farms.farm_agristability_clients (person_id);
ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT farm_asc_pin_uk UNIQUE (participant_pin);
ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT farm_asc_pk PRIMARY KEY (agristability_client_id);
ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT farm_asc_locally_updated_chk CHECK (locally_updated_ind in ('N', 'Y'));
ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT farm_asc_public_office_chk CHECK (public_office_ind in ('N', 'Y'));
ALTER TABLE farms.farm_agristability_clients ADD CONSTRAINT farm_asc_tip_participant_chk CHECK (tip_participant_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN agristability_client_id SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN ident_effective_date SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN public_office_ind SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN tip_participant_ind SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN locally_updated_ind SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN participant_lang_code SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN person_id SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN person_id_client_contacted_by SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN participant_class_code SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_agristability_clients ALTER COLUMN when_created SET NOT NULL;
