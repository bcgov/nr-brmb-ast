CREATE TABLE reported_inventory(
    reported_inventory_id              numeric(10, 0)    NOT NULL,
    price_start                        numeric(13, 2),
    price_end                          numeric(13, 2),
    end_year_producer_price            numeric(13, 2),
    start_of_year_amount               numeric(14, 3),
    end_of_year_amount                 numeric(14, 3),
    quantity_start                     numeric(14, 3),
    quantity_end                       numeric(14, 3),
    quantity_produced                  numeric(14, 3),
    accept_producer_price_indicator    varchar(1),
    aarm_reference_p1_price            numeric(13, 2),
    aarm_reference_p2_price            numeric(13, 2),
    on_farm_acres                      numeric(13, 3),
    unseedable_acres                   numeric(13, 3),
    import_comment                     varchar(128),
    crop_unit_code                     varchar(10),
    agristabilty_commodity_xref_id     numeric(10, 0)    NOT NULL,
    farming_operation_id               numeric(10, 0)    NOT NULL,
    agristability_scenario_id          numeric(10, 0),
    cra_reported_inventory_id          numeric(10, 0),
    revision_count                     numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                        varchar(30)       NOT NULL,
    create_date                        timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                        varchar(30),
    update_date                        timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN reported_inventory.reported_inventory_id IS 'REPORTED INVENTORY ID is a surrogate unique identifier for REPORTED INVENTORY.'
;
COMMENT ON COLUMN reported_inventory.price_start IS 'PRICE START is the opening price used when calculating the benefit for the reference year.'
;
COMMENT ON COLUMN reported_inventory.price_end IS 'PRICE END is the End of Year (P2) inventory price reported by the participant.'
;
COMMENT ON COLUMN reported_inventory.end_year_producer_price IS 'END YEAR PRODUCER PRICE is the price supplied by the participant for the end of the year.'
;
COMMENT ON COLUMN reported_inventory.start_of_year_amount IS 'START OF YEAR AMOUNT is the start of year dollar amount for Purchased Inputs, Deferred Income/receivables or Accounts Payable.'
;
COMMENT ON COLUMN reported_inventory.end_of_year_amount IS 'END OF YEAR AMOUNT is the end of year dollar amount for Purchased Inputs, Deferred Income/receivables or Accounts Payable.'
;
COMMENT ON COLUMN reported_inventory.quantity_start IS 'QUANTITY START is the start of year quantity of inventory.'
;
COMMENT ON COLUMN reported_inventory.quantity_end IS 'QUANTITY END is the end of year quantity of inventory.'
;
COMMENT ON COLUMN reported_inventory.quantity_produced IS 'QUANTITY PRODUCED is the quantity of a crop produced - section 8 column e.'
;
COMMENT ON COLUMN reported_inventory.accept_producer_price_indicator IS 'ACCEPT PRODUCER PRICE INDICATOR indicates if the P2 Producer price was used, even if it was outside FMV bands for the reference year.'
;
COMMENT ON COLUMN reported_inventory.aarm_reference_p1_price IS 'AARM REFERENCE P1 PRICE identifies the start of year prices for 2007 payment processing. When processing 2007 payments, the start of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the start of year price has been over-ridden. Overrides the start year price when using this year as a reference margin.'
;
COMMENT ON COLUMN reported_inventory.aarm_reference_p2_price IS 'AARM REFERENCE P2 PRICE identifies the start of year prices for 2007 payment processing. When processing 2007 payments, the start of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the start of year price has been over-ridden. Overrides the end year price when using this year as a reference margin.'
;
COMMENT ON COLUMN reported_inventory.on_farm_acres IS 'ON FARM ACRES is the number of acres grown of the crop.'
;
COMMENT ON COLUMN reported_inventory.unseedable_acres IS 'UNSEEDABLE ACRES is the number of unseedable acres of the crop - section 8 column e.'
;
COMMENT ON COLUMN reported_inventory.import_comment IS 'IMPORT COMMENT is a system generated comment about how the original data might have been modified to enable the data to be inserted into the operational tables. Usually this involves changing an invalid code.'
;
COMMENT ON COLUMN reported_inventory.crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN reported_inventory.agristabilty_commodity_xref_id IS 'AGRISTABILTY COMMODITY XREF ID is a surrogate unique identifier for an AGRISTABILTY COMMODITY XREF.'
;
COMMENT ON COLUMN reported_inventory.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN reported_inventory.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN reported_inventory.cra_reported_inventory_id IS 'REPORTED INVENTORY ID is a surrogate unique identifier for REPORTED INVENTORY.'
;
COMMENT ON COLUMN reported_inventory.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN reported_inventory.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN reported_inventory.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN reported_inventory.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN reported_inventory.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE reported_inventory IS 'REPORTED INVENTORY information contains production details for agricultural commodities, including Production Unit (i.e. quantity and values of livestock and crops) as well as accrual data. REPORTED INVENTORY data originates from the federal data imports (collected via "harmonized forms"). REPORTED INVENTORY is associated with each FARMING OPERATION however REPORTED INCOME EXPENSE data must also exist for each FARMING OPERATION (see Requirements). REPORTED INVENTORY data may have associated Form Data Adjustments.'
;

