CREATE TABLE agristability_client(
    agristability_client_id          numeric(10, 0)    NOT NULL,
    federal_identifier               varchar(15),
    participant_pin                  numeric(9, 0)     NOT NULL,
    sin                              varchar(9),
    business_number                  varchar(15),
    trust_number                     varchar(9),
    identity_effective_date          date              NOT NULL,
    public_office_indicator          varchar(1)        NOT NULL,
    tip_participant_indicator        varchar(1)        DEFAULT 'N' NOT NULL,
    locally_updated_indicator        varchar(1)        DEFAULT 'N' NOT NULL,
    participant_language_code        varchar(10)       NOT NULL,
    person_id                        numeric(10, 0)    NOT NULL,
    person_id_client_contacted_by    numeric(10, 0)    NOT NULL,
    participant_class_code           varchar(10)       NOT NULL,
    revision_count                   numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                      varchar(30)       NOT NULL,
    create_date                      timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                      varchar(30),
    update_date                      timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN agristability_client.agristability_client_id IS 'AGRISTABILITY CLIENT ID is a surrogate unique identifier for AGRI STABILITY CLIENTs.'
;
COMMENT ON COLUMN agristability_client.federal_identifier IS 'FEDERAL IDENTIFIER captures the participant Social Insurance Number, BN, or Trust Number- A CTN/SBRN is 9 numbers followed by a SBRN extension of 6 numbers (total 15 numbers).- If a T is the first of the 9 characters this  identifies a trust or a commune.- The corporate tax number (CTN) is 8 numbers.- A Social Insurance Number for an individual is 9 numbers without the extension.'
;
COMMENT ON COLUMN agristability_client.participant_pin IS 'PARTICIPANT PIN (Personal Information Number) is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS PIN and NISA PIN.'
;
COMMENT ON COLUMN agristability_client.sin IS 'SIN is the participant Social Insurance Number. A SIN is 9 numbers without an extension.'
;
COMMENT ON COLUMN agristability_client.business_number IS 'BUSINESS NUMBER is the participant Business Number . A Business Number is 9 numbers followed by RC followed by 4 more numbers (total 15 characters).'
;
COMMENT ON COLUMN agristability_client.trust_number IS 'TRUST NUMBER is the participant Trust Number. A Trust Number is the character T followed by 8 numbers.'
;
COMMENT ON COLUMN agristability_client.identity_effective_date IS 'IDENTITY EFFECTIVE DATE is the date the address and name information was last updated. This may apply to the participant information or the contact.'
;
COMMENT ON COLUMN agristability_client.public_office_indicator IS 'PUBLIC OFFICE INDICATOR is the Public Office / AAFC Employee Indicator.'
;
COMMENT ON COLUMN agristability_client.tip_participant_indicator IS 'TIP PARTICIPANT INDICATOR is "Y" if the participant has requested to receive TIP reports, otherwise "N".'
;
COMMENT ON COLUMN agristability_client.locally_updated_indicator IS 'LOCALLY UPDATED INDICATOR identifies if the record was updated after being imported into the system. If this value is ''Y'', the client has updated the AGRISTABILITY CLIENT  information; as a result, the FARM system will not allow the overwriting of that particular AGRISTABILITY CLIENT data by subsequent imports.'
;
COMMENT ON COLUMN agristability_client.participant_language_code IS 'PARTICIPANT LANGUAGE CODE is a unique code for the object PARTICIPANT LANGUAGE CODE described as a numeric code used to uniquely identify the preferred language of the AGRISTABILITY CLIENT. Examples of codes and descriptions are  1 - English, 2 - French.'
;
COMMENT ON COLUMN agristability_client.person_id IS 'PERSON ID is a surrogate unique identifier for PERSONs.'
;
COMMENT ON COLUMN agristability_client.person_id_client_contacted_by IS 'PERSON ID is a surrogate unique identifier for PERSONs.'
;
COMMENT ON COLUMN agristability_client.participant_class_code IS 'PARTICIPANT CLASS CODE is a unique code for the object PARTICIPANT CLASS CODE described as a numeric code used to uniquely identify the classification of the farming operation.  Examples of codes and descriptions are 1 =  INDV, 2  = CORP, or 3 = COOP.'
;
COMMENT ON COLUMN agristability_client.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN agristability_client.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN agristability_client.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN agristability_client.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN agristability_client.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE agristability_client IS 'AGRISTABILITY CLIENT is the taxable legal-entity (e.g. individuals, companies, etc.) registered with the AgriStability program. This object refers to identification and validation information, including program PIN.  AGRISTABILITY CLIENT data will originate from and be updated via federal data imports - specifically tax return data. An AGRISTABILITY CLIENT will always be associated with one or more PROGRAM YEARs. An AGRISTABILITY CLIENT may have associated PERSONS. AGRISTABILITY CLIENT may be associated with an  AGRISTABILITY REPRESENTATIVE. AGRISTABILITY CLIENT information may be updated by Provincial AgriStability Staff.'
;


CREATE INDEX ix_ac1_pcc ON agristability_client(participant_class_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_ac1_piccb ON agristability_client(person_id_client_contacted_by)
 TABLESPACE pg_default
;
CREATE INDEX ix_ac1_plc ON agristability_client(participant_language_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_ac1_pi ON agristability_client(person_id)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_ac1_pp ON agristability_client(participant_pin)
 TABLESPACE pg_default
;
