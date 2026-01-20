CREATE TABLE farms.farm_z40_prtcpnt_ref_supl_dtls (
	prior_year_supplemental_key bigint NOT NULL,
	participant_pin integer NOT NULL,
	program_year smallint NOT NULL,
	operation_number smallint NOT NULL,
	production_unit smallint,
	inventory_type_code smallint NOT NULL,
	inventory_code integer NOT NULL,
	quantity_start decimal(14,3),
	quantity_end decimal(14,3),
	starting_price decimal(13,2),
	crop_on_farm_acres decimal(13,3),
	crop_qty_produced decimal(14,3),
	end_year_producer_price decimal(13,2),
	accept_producer_price_ind varchar(1),
	end_year_price decimal(13,2),
	aarm_reference_p1_price decimal(13,2),
	aarm_reference_p2_price decimal(13,2),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_z40_prtcpnt_ref_supl_dtls IS E'Z40 PRTCPNT REF SUPL DTL identifies reference year crop and livestock inventory data, after it has been adjusted. Includes AARM price over-rides used during 2007 processing. Reference year Purchased Inputs, Deferred Income & Receivables, and Accounts Payable The Inventory Type code describes which part of the form each row came from. This file will have multiple rows per participant and farming operation. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.aarm_reference_p1_price IS E'AARM REFERENCE P1 PRICE identifies that when processing 2007 payments, the start of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the start of year price has been over-ridden.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.aarm_reference_p2_price IS E'AARM REFERENCE P2 PRICE identifies that when processing 2007 payments, the end of year prices for each reference year could be manipulated for AARM purposes, to adjust the calculated margin for that year. This would affect processing of the PROGRAM YEAR (2007), but not affect processing of the year being adjusted. This field will only be populated if the end of year price has been over-ridden.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.accept_producer_price_ind IS E'ACCEPT PRODUCER PRICE IND iIndicates if the P2 Producer price was used, even if it was outside FMV bands for the reference year. Allowable values are Y - Yes, N - No.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.crop_on_farm_acres IS E'CROP ON FARM ACRES is the number of acres grown of the crop - section 8 column d.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.crop_qty_produced IS E'CROP QTY PRODUCED is the quantity of a crop produced - section 8 column e.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.end_year_price IS E'END YEAR PRICE is the actual opening price used by when calcuting the reference year benefit.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.end_year_producer_price IS E'END YEAR PRODUCER PRICE identifies the end of year price supplied by the participant.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.inventory_code IS E'INVENTORY CODE is a numeric code used to uniquely identify an inventory item.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.inventory_type_code IS E'INVENTORY TYPE CODE is a numeric code indicating an inventory type. Valid values are 1 - Crops Inventory, 2 - Livestock  Inventory, 3 - Purchased Inputs, 4 - Deferred Income & Receivables, 5 - Accounts Payable.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given stab year. Operations may have different statement numbers in different program years.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.participant_pin IS E'PARTICIPANT PIN is the unique AgriStability/AgriInvest pin for this prodcuer. Was previous CAIS Pin and NISA Pin.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.prior_year_supplemental_key IS E'PRIOR YEAR SUPPLEMENTAL KEY is the primary key for the file. Provides each row with a unique identifier over the whole file.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.production_unit IS E'PRODUCTION UNIT is the unit of measure code.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.program_year IS E'PROGRAM YEAR is the stabilization year for this record.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.quantity_end IS E'QUANTITY END is the ending inventory for livestock (section 7 column c) or crop (section 8 column f).';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.quantity_start IS E'QUANTITY START is the Start of year quantity of inventory. For livestock this will always be # of Head.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.starting_price IS E'STARTING PRICE is the opening price used when calculating the benefit for the reference year.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_z40_prtcpnt_ref_supl_dtls.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_z40_farm_z03_fk_i ON farms.farm_z40_prtcpnt_ref_supl_dtls (participant_pin, program_year, operation_number);
CREATE INDEX farm_z40_farm_z28_fk_i ON farms.farm_z40_prtcpnt_ref_supl_dtls (production_unit);
CREATE INDEX farm_z40_farm_z29_fk_i ON farms.farm_z40_prtcpnt_ref_supl_dtls (inventory_code, inventory_type_code);
ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ADD CONSTRAINT farm_z40_pk PRIMARY KEY (prior_year_supplemental_key);
ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ALTER COLUMN prior_year_supplemental_key SET NOT NULL;
ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ALTER COLUMN participant_pin SET NOT NULL;
ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ALTER COLUMN program_year SET NOT NULL;
ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ALTER COLUMN inventory_type_code SET NOT NULL;
ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ALTER COLUMN inventory_code SET NOT NULL;
ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_z40_prtcpnt_ref_supl_dtls ALTER COLUMN when_created SET NOT NULL;
