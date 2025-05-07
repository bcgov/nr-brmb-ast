CREATE TABLE reasonability_production_grain_result(
    reasonability_production_grain_result_id    numeric(10, 0)    NOT NULL,
    productive_capacity_amount                  numeric(14, 3),
    reported_quantity_produced                  numeric(19, 3),
    expected_production_per_unit                numeric(14, 3),
    expected_quantity_produced                  numeric(19, 3),
    quantity_produced_variance                  numeric(19, 3),
    quantity_produced_within_limit_indicator    varchar(1),
    inventory_item_code                         varchar(10)       NOT NULL,
    quantity_produced_crop_unit_code            varchar(10)       NOT NULL,
    reasonability_test_result_id                numeric(10, 0)    NOT NULL,
    revision_count                              numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                                 varchar(30)       NOT NULL,
    create_date                                 timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                                 varchar(30),
    update_date                                 timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN reasonability_production_grain_result.reasonability_production_grain_result_id IS 'REASONABILITY PRODUCTION GRAIN RESULT ID is a surrogate unique identifier for REASONABILITY PRODUCTION GRAIN RESULT.'
;
COMMENT ON COLUMN reasonability_production_grain_result.productive_capacity_amount IS 'PRODUCTIVE CAPACITY AMOUNT is the quantity entered in section 9 for this inventory/bpu code.'
;
COMMENT ON COLUMN reasonability_production_grain_result.reported_quantity_produced IS 'REPORTED QUANTITY PRODUCED is the amount produced for this crop, in the QUANTITY PRODUCED CROP UNIT CODE.'
;
COMMENT ON COLUMN reasonability_production_grain_result.expected_production_per_unit IS 'EXPECTED PRODUCTION PER UNIT is the amount typically produced per productive unit for this crop, in the QUANTITY PRODUCED CROP UNIT CODE.'
;
COMMENT ON COLUMN reasonability_production_grain_result.expected_quantity_produced IS 'EXPECTED QUANTITY PRODUCED is the amount that the farm can be expected to produce for this crop based on the number of productive units and the EXPECTED PRODUCTION PER UNIT, in the QUANTITY PRODUCED CROP UNIT CODE.'
;
COMMENT ON COLUMN reasonability_production_grain_result.quantity_produced_variance IS 'QUANTITY PRODUCED VARIANCE is the percent difference between EXPECTED QUANTITY PRODUCED and REPORTED QUANTITY PRODUCED ((RQP / EQP) - 1)'
;
COMMENT ON COLUMN reasonability_production_grain_result.quantity_produced_within_limit_indicator IS 'QUANTITY PRODUCED WITHIN LIMIT INDICATOR indicates if the QUANTITY PRODUCED VARIANCE was within the QUANTITY PRODUCED LIMIT.'
;
COMMENT ON COLUMN reasonability_production_grain_result.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN reasonability_production_grain_result.quantity_produced_crop_unit_code IS 'CROP UNIT CODE is a unique code for the object UNIT CODE described as a numeric code used to uniquely identify the unit of measure for the crop inventory .  Examples of codes and descriptions are 1-Pounds, 2-Tonnes, 3-Dozen, 4-Bushels.'
;
COMMENT ON COLUMN reasonability_production_grain_result.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN reasonability_production_grain_result.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN reasonability_production_grain_result.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN reasonability_production_grain_result.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN reasonability_production_grain_result.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN reasonability_production_grain_result.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE reasonability_production_grain_result IS 'REASONABILITY PRODUCTION GRAIN RESULT contains the calculated amounts for the Reasonability Test: Grains Production Test run against the scenario, by GRAIN TYPE CODE.'
;

