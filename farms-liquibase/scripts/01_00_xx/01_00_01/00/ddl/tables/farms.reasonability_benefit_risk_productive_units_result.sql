CREATE TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts(
    rsn_bnft_rsk_prd_un_rslts_id    numeric(10, 0)    NOT NULL,
    reported_productive_capacity                  numeric(14, 3)    NOT NULL,
    consumed_productive_capacity                  numeric(14, 3),
    net_productive_capacity                       numeric(14, 3)    NOT NULL,
    bnft_rsk_bpu_calculated                   numeric(13, 2)    NOT NULL,
    bnft_rsk_estimated_income                 numeric(13, 2)    NOT NULL,
    inventory_item_code                           varchar(10),
    structure_group_code                          varchar(10),
    reasonability_test_result_id                  numeric(10, 0)    NOT NULL,
    revision_count                                numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                   varchar(30)       NOT NULL,
    when_created                                   timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                   varchar(30),
    when_updated                                   timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.rsn_bnft_rsk_prd_un_rslts_id IS 'RSN BENEFIT RISK PRODUCTIVE UNITS RESULTS ID is a surrogate unique identifier for RSN BENEFIT RISK PRODUCTIVE UNITS RESULTS.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.reported_productive_capacity IS 'REPORTED PRODUCTIVE CAPACITY is the quantity entered in section 9 for this inventory/bpu code.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.consumed_productive_capacity IS 'CONSUMED PRODUCTIVE CAPACITY is the quantity consumed by (fed out to) cattle.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.net_productive_capacity IS 'NET PRODUCTIVE CAPACITY is the quantity entered in section 9 for this inventory/bpu code minus the amount fed to cattle (forage only).'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.bnft_rsk_bpu_calculated IS 'BENEFIT RISK BPU CALCULATED is the base price per unit. This is calculated using the BPU values for the three years used in the benefit calculation and taking lead/lag into account.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.bnft_rsk_estimated_income IS 'BENEFIT RISK ESTIMATED INCOME is the amount of income estimated for this productive unit code. It is the product of PRODUCTIVE CAPACITY AMOUNT and BPU CALCULATED.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.structure_group_code IS 'STRUCTURE GROUP CODE identifies the type of structure group.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_bnft_rsk_prd_un_rslts.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts IS 'REASONABILITY BENEFIT RISK PRODUCTIVE UNITS RESULTS contains the calculated amounts for productive units for the Benefit Risk Assessment reasonability test run against the scenario.'
;


CREATE INDEX ix_rbrpur_iic ON farms.farm_rsn_bnft_rsk_prd_un_rslts(inventory_item_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_rbrpur_sgc ON farms.farm_rsn_bnft_rsk_prd_un_rslts(structure_group_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_rbrpur_rtri_iic_sgc ON farms.farm_rsn_bnft_rsk_prd_un_rslts(reasonability_test_result_id, inventory_item_code, structure_group_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_rsn_bnft_rsk_prd_un_rslts ADD 
    CONSTRAINT pk_rbrpur PRIMARY KEY (rsn_bnft_rsk_prd_un_rslts_id) USING INDEX TABLESPACE pg_default 
;
