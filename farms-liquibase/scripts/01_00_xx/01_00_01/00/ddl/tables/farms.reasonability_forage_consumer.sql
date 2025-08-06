CREATE TABLE farms.farm_rsn_forage_consumers(
    rsn_forage_consumer_id    numeric(10, 0)    NOT NULL,
    productive_capacity_amount          numeric(13, 3)    NOT NULL,
    quantity_consumed_per_unit          numeric(13, 3)    NOT NULL,
    quantity_consumed                   numeric(19, 3)    NOT NULL,
    structure_group_code                varchar(10)       NOT NULL,
    reasonability_test_result_id        numeric(10, 0)    NOT NULL,
    revision_count                      numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                         varchar(30)       NOT NULL,
    when_created                         timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                         varchar(30),
    when_updated                         timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_rsn_forage_consumers.rsn_forage_consumer_id IS 'REASONABILITY FORAGE CONSUMER ID is a surrogate unique identifier for REASONABILITY FORAGE CONSUMER.'
;
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.productive_capacity_amount IS 'PRODUCTIVE CAPACITY AMOUNT is the quantity entered in section 9 for this inventory/bpu code.'
;
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.quantity_consumed_per_unit IS 'QUANTITY CONSUMED PER UNIT is the amount of forage inventory consumed by (fed out to) this type of cattle per productive unit (PRODUCTIVE CAPACITY AMOUNT).'
;
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.quantity_consumed IS 'QUANTITY CONSUMED is the amount of forage inventory consumed by (fed out to) this type of cattle.'
;
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.structure_group_code IS 'STRUCTURE GROUP CODE identifies the type of structure group.'
;
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_forage_consumers.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_rsn_forage_consumers IS 'REASONABILITY FORAGE CONSUMER contains the calculated amounts of forage inventory consumed by (fed out to) cattle for the Reasonability Test: Grains, Forage, and Forage Seed Revenue Test run against the scenario, by STRUCTURE GROUP CODE (the type of cattle).'
;


CREATE INDEX ix_rfc_sgc ON farms.farm_rsn_forage_consumers(structure_group_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_rfc_rtri_sgc ON farms.farm_rsn_forage_consumers(reasonability_test_result_id, structure_group_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_rsn_forage_consumers ADD 
    CONSTRAINT pk_rfc PRIMARY KEY (rsn_forage_consumer_id) USING INDEX TABLESPACE pg_default 
;
