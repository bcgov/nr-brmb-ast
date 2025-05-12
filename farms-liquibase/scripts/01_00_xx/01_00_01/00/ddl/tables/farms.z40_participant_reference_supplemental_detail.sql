CREATE TABLE farms.z40_participant_reference_supplemental_detail(
    prior_year_supplemental_key        numeric(10, 0)    NOT NULL,
    participant_pin                    numeric(9, 0)     NOT NULL,
    program_year                       numeric(4, 0)     NOT NULL,
    operation_number                   numeric(4, 0)     NOT NULL,
    production_unit                    numeric(4, 0),
    inventory_type_code                numeric(4, 0)     NOT NULL,
    inventory_code                     numeric(5, 0)     NOT NULL,
    quantity_start                     numeric(14, 3),
    quantity_end                       numeric(14, 3),
    starting_price                     numeric(13, 2),
    crop_on_farm_acres                 numeric(13, 3),
    crop_quantity_produced             numeric(14, 3),
    end_year_producer_price            numeric(13, 2),
    accept_producer_price_indicator    varchar(1),
    end_year_price                     numeric(13, 2),
    aarm_reference_p1_price            numeric(13, 2),
    aarm_reference_p2_price            numeric(13, 2),
    revision_count                     numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                        varchar(30)       NOT NULL,
    create_date                        timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                        varchar(30),
    update_date                        timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.prior_year_supplemental_key IS 'PRIOR YEAR SUPPLEMENTAL KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.participant_pin IS 'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this producer. Was previous CAIS Pin and NISA Pin.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.program_year IS 'PROGRAM YEAR is the stabilization year for this record.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.operation_number IS 'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.production_unit IS 'PRODUCTION UNIT is the unit of measure code.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.inventory_type_code IS 'INVENTORY TYPE CODE is a numeric code indicating an inventory type. Valid values are 1 - Crops Inventory, 2 - Livestock  Inventory, 3 - Purchased Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.inventory_code IS 'INVENTORY CODE is a numeric code used to uniquely identify an inventory item.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.quantity_start IS 'QUANTITY START is the Start of year quantity of inventory. For livestock this will always be # of Head.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.quantity_end IS 'QUANTITY END is the ending inventory for livestock (section 7 column c) or crop (section 8 column f).'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.starting_price IS 'STARTING PRICE is the opening price used when calculating the benefit for the reference year.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.crop_on_farm_acres IS 'CROP ON FARM ACRES is the number of acres grown of the crop - section 8 column d.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.crop_quantity_produced IS 'CROP QUANTITY PRODUCED is the quantity of a crop produced - section 8 column e.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.end_year_producer_price IS 'END YEAR PRODUCER PRICE identifies the end of year price supplied by the participant.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.accept_producer_price_indicator IS 'ACCEPT PRODUCER PRICE INDICATOR indicates if the P2 Producer price was used, even if it was outside FMV bands for the reference year. Allowable values are Y - Yes, N - No.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.end_year_price IS 'END YEAR PRICE is the actual opening price used by when calcuting the reference year benefit.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.aarm_reference_p1_price IS 'AARM REFERENCE P1 PRICE identifies that when processing 2007 payments, the start of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the start of year price has been over-ridden.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.aarm_reference_p2_price IS 'AARM REFERENCE P2 PRICE identifies that when processing 2007 payments, the end of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the end of year price has been over-ridden.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.z40_participant_reference_supplemental_detail.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.z40_participant_reference_supplemental_detail IS 'Z40 PARTICIPANT REFERENCE SUPPLEMENTAL DETAIL identifies reference year crop and livestock inventory data, after it has been adjusted. Includes AARM price over-rides used during 2007 processing. Reference year Purchased Inputs, Deferred Income & Receivables, and Accounts Payable The Inventory Type code describes which part of the form each row came from. This file will have multiple rows per participant and farming operation. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


CREATE INDEX ix_zprsd_pp_py_on ON farms.z40_participant_reference_supplemental_detail(participant_pin, program_year, operation_number)
 TABLESPACE pg_default
;
CREATE INDEX ix_zprsd_pu ON farms.z40_participant_reference_supplemental_detail(production_unit)
 TABLESPACE pg_default
;
CREATE INDEX ix_zprsd_ic_itc ON farms.z40_participant_reference_supplemental_detail(inventory_code, inventory_type_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.z40_participant_reference_supplemental_detail ADD 
    CONSTRAINT pk_zprsd PRIMARY KEY (prior_year_supplemental_key) USING INDEX TABLESPACE pg_default 
;
