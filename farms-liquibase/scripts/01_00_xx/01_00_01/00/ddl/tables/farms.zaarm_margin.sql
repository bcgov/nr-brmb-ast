CREATE TABLE farms.farm_zaarm_margins(
    zaarm_margin_id            numeric(10, 0)    NOT NULL,
    participant_pin            numeric(9, 0)     NOT NULL,
    program_year               numeric(4, 0)     NOT NULL,
    operation_number           numeric(4, 0)     NOT NULL,
    partner_percent            numeric(7, 4),
    inventory_type_code        numeric(4, 0)     NOT NULL,
    inventory_code             numeric(5, 0)     NOT NULL,
    inventory_desc      varchar(256),
    production_unit            numeric(4, 0),
    aarm_reference_p1_price    numeric(13, 2),
    aarm_reference_p2_price    numeric(13, 2),
    quantity_start             numeric(14, 3),
    quantity_end               numeric(14, 3),
    revision_count             numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                varchar(30)       NOT NULL,
    when_created                timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                varchar(30),
    when_updated                timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_zaarm_margins.zaarm_margin_id IS 'ZAARM MARGIN ID is a sequential number given in the first column of the AARM CSV file.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.program_year IS 'PROGRAM YEAR is the year this data pertains to.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.partner_percent IS 'PARTNER PERCENT is the partners percentage share.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.inventory_type_code IS 'INVENTORY TYPE CODE is a numeric code indicating an inventory type. Valid values are 1 - Crops Inventory, 2 - Livestock  Inventory, 3 - Purchased Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.inventory_code IS 'INVENTORY CODE is a numeric code used to uniquely identify an inventory item.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.inventory_desc IS 'INVENTORY DESCRIPTION is the english description of the Inventory. An English text description of an inventory item.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.production_unit IS 'PRODUCTION UNIT is the unit of measure code.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.aarm_reference_p1_price IS 'AARM REFERENCE P1 PRICE identifies that when processing 2007 payments, the start of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the start of year price has been over-ridden.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.aarm_reference_p2_price IS 'AARM REFERENCE P2 PRICE identifies that when processing 2007 payments, the end of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the end of year price has been over-ridden.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.quantity_start IS 'QUANTITY START is the Start of year quantity of inventory. For livestock this will always be # of Head.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.quantity_end IS 'QUANTITY END is the ending inventory for livestock (section 7 column c) or crop (section 8 column f).'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_zaarm_margins.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;


CREATE INDEX ix_zm_arp1p_arp2p ON farms.farm_zaarm_margins(aarm_reference_p1_price, aarm_reference_p2_price)
 TABLESPACE pg_default
;
CREATE INDEX ix_zm_pp_py_on_itc_ic ON farms.farm_zaarm_margins(participant_pin, program_year, operation_number, inventory_type_code, inventory_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_zaarm_margins ADD 
    CONSTRAINT pk_zm PRIMARY KEY (zaarm_margin_id) USING INDEX TABLESPACE pg_default 
;
