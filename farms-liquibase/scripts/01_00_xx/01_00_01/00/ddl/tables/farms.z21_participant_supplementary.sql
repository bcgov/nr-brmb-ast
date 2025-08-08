CREATE TABLE farms.farm_z21_participant_suppls(
    inventory_key             numeric(10, 0)    NOT NULL,
    participant_pin           numeric(9, 0)     NOT NULL,
    program_year              numeric(4, 0)     NOT NULL,
    operation_number          numeric(4, 0)     NOT NULL,
    inventory_type_code       numeric(4, 0)     NOT NULL,
    inventory_code            numeric(5, 0)     NOT NULL,
    crop_unit_type            numeric(4, 0),
    crop_on_farm_acres        numeric(13, 3),
    crop_qty_produced    numeric(14, 3),
    quantity_end              numeric(14, 3),
    end_of_year_price         numeric(13, 2),
    end_of_year_amount        numeric(13, 2),
    crop_unseedable_acres     numeric(13, 3),
    revision_count            numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created               varchar(30)       NOT NULL,
    when_created               timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated               varchar(30),
    when_updated               timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_z21_participant_suppls.inventory_key IS 'INVENTORY KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.inventory_type_code IS 'INVENTORY TYPE CODE is a numeric code indicating an inventory type. Valid values are 1 - Crops Inventory, 2 - Livestock  Inventory, 3 - Purchased Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.inventory_code IS 'INVENTORY CODE is a numeric code used to uniquely identify an inventory item.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.crop_unit_type IS 'CROP UNIT TYPE is a numeric value assigned to a unit type for the crop inventory, section 8 column c. Foreign key to file 28.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.crop_on_farm_acres IS 'CROP ON FARM ACRES is the number of acres grown of the crop - section 8 column d.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.crop_qty_produced IS 'CROP QUANTITY PRODUCED is the quantity  of a crop produced  - section 8 column e.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.quantity_end IS 'QUANTITY END is the ending inventory for livestock (section 7 column c) or crop (section 8 column f).'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.end_of_year_price IS 'END OF YEAR PRICE is the END OF YEAR PRICE for crop or livestock inventory entries.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.end_of_year_amount IS 'END OF YEAR AMOUNT is the the end of year dollar amount for Purchased Inputs, Deferred income/receivables or Accounts Payable.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.crop_unseedable_acres IS 'CROP UNSEEDABLE ACRES is the number of unseedable acres of the crop - section 8 column e.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_z21_participant_suppls.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_z21_participant_suppls IS 'Z21 PARTICIPANT SUPPLEMENTARY identifies Inventory information from parts 7, 8, 10, 11, 12 of the AgriStability Supplementary Form. Totals are not included, but can be calculated. The Inventory Type code describes which part of the form each row came from. This file will have multiple rows per participant and farming operation, for the current (latest) program year. This file is created by FIPD.  This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zps_pp_py_on ON farms.farm_z21_participant_suppls(participant_pin, program_year, operation_number)
 TABLESPACE pg_default
;
CREATE INDEX ix_zps_ic_itc ON farms.farm_z21_participant_suppls(inventory_code, inventory_type_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_z21_participant_suppls ADD 
    CONSTRAINT pk_zps PRIMARY KEY (inventory_key, participant_pin, program_year, operation_number) USING INDEX TABLESPACE pg_default 
;
