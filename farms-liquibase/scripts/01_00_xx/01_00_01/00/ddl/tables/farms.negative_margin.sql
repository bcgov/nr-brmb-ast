CREATE TABLE farms.negative_margin(
    negative_margin_id             numeric(10, 0)    NOT NULL,
    farming_operation_id           numeric(10, 0)    NOT NULL,
    inventory_item_code            varchar(10)       NOT NULL,
    agristability_scenario_id      numeric(10, 0)    NOT NULL,
    deductible_percentage          numeric(13, 2),
    required_insurable_value       numeric(13, 3),
    insurable_value_purchased      numeric(13, 3),
    reported_quantity              numeric(13, 3),
    guaranteed_production_value    numeric(13, 3),
    premiums_paid                  numeric(13, 2),
    premium_rate                   numeric(13, 4),
    required_premium               numeric(13, 2),
    market_rate_premium            numeric(13, 2),
    claims_received                numeric(13, 2),
    claims_calculation             numeric(13, 2),
    deemed_premium                 numeric(13, 2),
    deemed_received                numeric(13, 2),
    deemed_pi_value                numeric(13, 2),
    revision_count                 numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                    varchar(30)       NOT NULL,
    create_date                    timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                    varchar(30),
    update_date                    timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.negative_margin.negative_margin_id IS 'NEGATIVE MARGIN ID is a surrogate unique identifier for NEGATIVE MARGINS.'
;
COMMENT ON COLUMN farms.negative_margin.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN farms.negative_margin.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN farms.negative_margin.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.negative_margin.deductible_percentage IS 'DEDUCTIBLE PERCENTAGE is the percentage of the deductible.'
;
COMMENT ON COLUMN farms.negative_margin.required_insurable_value IS 'REQUIRED INSURABLE VALUE is the amount of insurance the participant is required to purchase per unit of quantity.'
;
COMMENT ON COLUMN farms.negative_margin.insurable_value_purchased IS 'INSURABLE VALUE PURCHASED is the insurable value purchased.'
;
COMMENT ON COLUMN farms.negative_margin.reported_quantity IS 'REPORTED QUANTITY is the reported quantity produced for this FARMING OPERATION.'
;
COMMENT ON COLUMN farms.negative_margin.guaranteed_production_value IS 'GUARANTEED PRODUCTION VALUE is the guaranteed production value.'
;
COMMENT ON COLUMN farms.negative_margin.premiums_paid IS 'PREMIUMS PAID is the premiums paid.'
;
COMMENT ON COLUMN farms.negative_margin.premium_rate IS 'PREMIUMS RATE is the cost per unit insured/guaranteed for the required insurance.'
;
COMMENT ON COLUMN farms.negative_margin.required_premium IS 'REQUIRED PREMIUM is the base cost for the required insurance (the product of INSURABLE VALUE and GUARANTEED PROD VALUE), not including MARKET RATE PREMIUM (MRP).'
;
COMMENT ON COLUMN farms.negative_margin.market_rate_premium IS 'MARKET RATE PREMIUM is the additional risk charge based on the size of the premium for the required amount of insurance. This is calculated using the PREMIUM and MARKET RATE PREMIUM table.'
;
COMMENT ON COLUMN farms.negative_margin.claims_received IS 'CLAIMS RECEIVED is the claims received.'
;
COMMENT ON COLUMN farms.negative_margin.claims_calculation IS 'CLAIMS CALCULATION is the amount that would have been received if the participant had purchased the required amount of insurance.'
;
COMMENT ON COLUMN farms.negative_margin.deemed_premium IS 'DEEMED PREMIUM is the premium amount that would have been paid if the participant had purchased the required amount of insurance.'
;
COMMENT ON COLUMN farms.negative_margin.deemed_received IS 'DEEMED RECEIVED is the amount that would have been received minus the DEEMED PREMIUM minus CLAIMS RECEIVED, if the participant had purchased the required amount of insurance.'
;
COMMENT ON COLUMN farms.negative_margin.deemed_pi_value IS 'DEEMED PI VALUE is the additional amount that would have been received if the participant had purchased the required amount of insurance. This is the same as DEEMED RECEIVED unless the DEDUCTIBLE PERCENTAGE is 30 and the INSURABLE VALUE PURCHASED equals the REQUIRED INSURABLE VALUE.'
;
COMMENT ON COLUMN farms.negative_margin.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.negative_margin.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.negative_margin.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.negative_margin.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.negative_margin.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.negative_margin IS 'NEGATIVE MARGINS is the negative margins for the farming operation.'
;


CREATE INDEX ix_nm_foi ON farms.negative_margin(farming_operation_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_nm_iic ON farms.negative_margin(inventory_item_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_nm_asi_foi_iic ON farms.negative_margin(agristability_scenario_id, farming_operation_id, inventory_item_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.negative_margin ADD 
    CONSTRAINT pk_nm PRIMARY KEY (negative_margin_id) USING INDEX TABLESPACE pg_default 
;
