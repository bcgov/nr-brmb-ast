CREATE TABLE farms.farm_rsn_prdctn_frut_veg_rslts(
    rsn_prdctn_frut_veg_rslt_id    numeric(10, 0)    NOT NULL,
    productive_capacity_amount                             numeric(14, 3),
    reported_quantity_produced                             numeric(19, 3),
    expected_quantity_produced                             numeric(19, 3),
    quantity_produced_variance                             numeric(19, 3),
    quantity_produced_wthn_lmt_ind               varchar(1),
    fruit_veg_type_code                              varchar(10)       NOT NULL,
    qty_produced_crop_unit_code                       varchar(10)       NOT NULL,
    reasonability_test_result_id                           numeric(10, 0)    NOT NULL,
    revision_count                                         numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                            varchar(30)       NOT NULL,
    when_created                                            timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                            varchar(30),
    when_updated                                            timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.rsn_prdctn_frut_veg_rslt_id IS 'REASONABILITY PRODUCTION FRUIT VEGETABLES RESULT ID is a surrogate unique identifier for REASONABILITY PRODUCTION FRUIT VEGETABLES RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.productive_capacity_amount IS 'PRODUCTIVE CAPACITY AMOUNT is the quantity entered in section 9 for this inventory/bpu code.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.reported_quantity_produced IS 'REPORTED QUANTITY PRODUCED is the amount produced for this crop, in the QTY PRODUCED CROP UNIT CODE.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.expected_quantity_produced IS 'EXPECTED QUANTITY PRODUCED is the amount that the farm can be expected to produce for this crop based on the number of productive units and the EXPECTED PRODUCTION PER UNIT, in the QUANTITY PRODUCED CROP UNIT CODE.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.quantity_produced_variance IS 'QUANTITY PRODUCED VARIANCE is the percent difference between EXPECTED QUANTITY PRODUCED and REPORTED QUANTITY PRODUCED ((RQP / EQP) - 1)'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.quantity_produced_wthn_lmt_ind IS 'QUANTITY PRODUCED WITHIN LIMIT IND indicates if the QUANTITY PRODUCED VARIANCE was within the QUANTITY PRODUCED LIMIT.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.fruit_veg_type_code IS 'FRUIT VEGETABLE TYPE CODE is a unique code for the object FRUIT VEGETABLE TYPE CODE. Examples of codes and descriptions are APPLE - Apples, BEAN - Beans,  POTATO - Potatoes.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.qty_produced_crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_prdctn_frut_veg_rslts.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_rsn_prdctn_frut_veg_rslts IS 'REASONABILITY PRODUCTION FRUIT VEGETABLES RESULT contains the calculated amounts for the Reasonability Test: Perishables Production Test run against the scenario, by FRUIT VEGETABLE TYPE CODE.'
;


CREATE INDEX ix_rpfvr_qpcuc ON farms.farm_rsn_prdctn_frut_veg_rslts(qty_produced_crop_unit_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_rpfvr_fvtc ON farms.farm_rsn_prdctn_frut_veg_rslts(fruit_veg_type_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX ix_rpfvr_rtri_fvtc ON farms.farm_rsn_prdctn_frut_veg_rslts(reasonability_test_result_id, fruit_veg_type_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_rsn_prdctn_frut_veg_rslts ADD 
    CONSTRAINT pk_rpfvr PRIMARY KEY (rsn_prdctn_frut_veg_rslt_id) USING INDEX TABLESPACE pg_default 
;
