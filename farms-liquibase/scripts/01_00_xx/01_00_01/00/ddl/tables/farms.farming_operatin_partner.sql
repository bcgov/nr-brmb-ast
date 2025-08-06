CREATE TABLE farms.farm_farming_operatin_prtnrs(
    farming_operatin_prtnr_id    numeric(10, 0)    NOT NULL,
    partner_percent                 numeric(6, 4),
    partnership_pin                 numeric(9, 0),
    partner_sin                     varchar(15),
    first_name                      varchar(100),
    last_name                       varchar(100),
    corp_name                       varchar(100),
    farming_operation_id            numeric(10, 0)    NOT NULL,
    revision_count                  numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                     varchar(30)       NOT NULL,
    when_created                     timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                     varchar(30),
    when_updated                     timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.farming_operatin_prtnr_id IS 'FARMING OPERATION PARTNER ID is a surrogate unique identifier for FARMING OPERATION PARTNERs.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.partner_percent IS 'PARTNER PERCENT is the Partner''s Percentage Share.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.partnership_pin IS 'PARTNERSHIP PIN uniquely identifies the partnership. If both the partners in an operation file applications, the same PARTNERSHIP PIN will show up under both pins. PARTNERSHIP PINs represent the same operation if/when they are used in different PROGRAM YEARS.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.partner_sin IS 'PARTNER SIN or CTN or BN - see participant SIN/CTN/BN for valid formats.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.first_name IS 'FIRST NAME of the contact.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.last_name IS 'LAST NAME of the contact.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.corp_name IS 'CORP NAME is the Contact Business Name, as provided by the participant.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_farming_operatin_prtnrs.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_farming_operatin_prtnrs IS 'FARMING OPERATION PARTNER lists all the farming partners the producer has entered in section 6 of the Harmonized form. This should not include the participant. Even if the participant is in a partnership, there is no requirement to submit a list of participants, so this file may not have any data for that statement. This file will be sent to the provinces by FIPD.'
;


CREATE INDEX ix_fop_foi ON farms.farm_farming_operatin_prtnrs(farming_operation_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_farming_operatin_prtnrs ADD 
    CONSTRAINT pk_fop PRIMARY KEY (farming_operatin_prtnr_id) USING INDEX TABLESPACE pg_default 
;
