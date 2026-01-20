CREATE TABLE farms.farm_farming_operatin_prtnrs (
	farming_operatin_prtnr_id bigint NOT NULL,
	partner_percent decimal(6,4),
	partnership_pin integer,
	partner_sin varchar(15),
	first_name varchar(100),
	last_name varchar(100),
	corp_name varchar(100),
	farming_operation_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_farming_operatin_prtnrs IS E'FARMING OPERATION PARTNER lists all the farming partners the producer has entered in section 6 of the Harmonized form. This should not include the participant. Even if the participant is in a partnerhip, there is no requirement to submit a list of participants, so this file may not have any data for that statement. This file will be sent to the provinces by FIPD.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.corp_name IS E'CORP NAME is the Contact Business Name, as provided by the participant.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.farming_operatin_prtnr_id IS E'FARMING OPERATIN PRTNR ID is a surrogate unique identifier for FARMING OPERATION PARTNERs.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.farming_operation_id IS E'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.first_name IS E'FIRST NAME of the contact.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.last_name IS E'LAST NAME of the contact.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.partner_percent IS E'PARTNER PERCENT is the Partner''s Percentage Share.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.partner_sin IS E'PARTNER SIN or CTN or BN - see participant SIN/CTN/BN for valid formats.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.partnership_pin IS E'PARTNERSHIP PIN uniquely identifies the partnership. If both the partners in an operation file applications, the same PARTNERSHIP PIN will show up under both pins. PARTNERSHIP PINs represent the same operation if/when they are used in different PROGRAM YEARS.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_fop_farm_fo_fk_i ON farms.farm_farming_operatin_prtnrs (farming_operation_id);
ALTER TABLE farms.farm_farming_operatin_prtnrs ADD CONSTRAINT farm_fop_pk PRIMARY KEY (farming_operatin_prtnr_id);
ALTER TABLE farms.farm_farming_operatin_prtnrs ALTER COLUMN farming_operatin_prtnr_id SET NOT NULL;
ALTER TABLE farms.farm_farming_operatin_prtnrs ALTER COLUMN farming_operation_id SET NOT NULL;
ALTER TABLE farms.farm_farming_operatin_prtnrs ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_farming_operatin_prtnrs ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_farming_operatin_prtnrs ALTER COLUMN when_created SET NOT NULL;
