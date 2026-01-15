CREATE TABLE farms.farm_negative_margins (
	negative_margin_id bigint NOT NULL,
	farming_operation_id bigint NOT NULL,
	inventory_item_code varchar(10) NOT NULL,
	agristability_scenario_id bigint NOT NULL,
	deductible_percentage decimal(13,2),
	required_insurable_value decimal(13,3),
	insurable_value_purchased decimal(13,3),
	reported_quantity decimal(13,3),
	guaranteed_prod_value decimal(13,3),
	premiums_paid decimal(13,2),
	premium_rate decimal(13,4),
	required_premium decimal(13,2),
	market_rate_premium decimal(13,2),
	claims_received decimal(13,2),
	claims_calculation decimal(13,2),
	deemed_premium decimal(13,2),
	deemed_received decimal(13,2),
	deemed_pi_value decimal(13,2),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_negative_margins IS E'NEGATIVE MARGINS is the negative margins for the farming operation.';
COMMENT ON COLUMN farms.farm_negative_margins.agristability_scenario_id IS E'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.';
COMMENT ON COLUMN farms.farm_negative_margins.claims_calculation IS E'CLAIMS CALCULATION is the amount that would have been received if the participant had purchased the required amount of insurance.';
COMMENT ON COLUMN farms.farm_negative_margins.claims_received IS E'CLAIMS RECEIVED is the claims received.';
COMMENT ON COLUMN farms.farm_negative_margins.deductible_percentage IS E'DEDUCTIBLE PERCENTAGE is the percentage of the deductible.';
COMMENT ON COLUMN farms.farm_negative_margins.deemed_pi_value IS E'DEEMED PI VALUE is the additional amount that would have been received if the participant had purchased the required amount of insurance. This is the same as DEEMED RECEIVED unless the DEDUCTIBLE PERCENTAGE is 30 and the INSURABLE VALUE PURCHASED equals the REQUIRED INSURABLE VALUE.';
COMMENT ON COLUMN farms.farm_negative_margins.deemed_premium IS E'DEEMED PREMIUM is the premium amount that would have been paid if the participant had purchased the required amount of insurance.';
COMMENT ON COLUMN farms.farm_negative_margins.deemed_received IS E'DEEMED RECEIVED is the amount that would have been received minus the DEEMED PREMIUM minus CLAIMS RECEIVED, if the participant had purchased the required amount of insurance.';
COMMENT ON COLUMN farms.farm_negative_margins.farming_operation_id IS E'FARMING OPERATION ID is a surrogate unique identifier for the farming operation.';
COMMENT ON COLUMN farms.farm_negative_margins.guaranteed_prod_value IS E'GUARANTEED PROD VALUE is the guaranteed production value.';
COMMENT ON COLUMN farms.farm_negative_margins.insurable_value_purchased IS E'INSURABLE VALUE PURCHASED is the insurable value purchased.';
COMMENT ON COLUMN farms.farm_negative_margins.inventory_item_code IS E'INVENTORY ITEM CODE is the code for the inventory item.';
COMMENT ON COLUMN farms.farm_negative_margins.market_rate_premium IS E'MARKET RATE PREMIUM is the additional risk charge based on the size of the premium for the required amount of insurance. This is calculated using the PREMIUM and MARKET RATE PREMIUM table.';
COMMENT ON COLUMN farms.farm_negative_margins.negative_margin_id IS E'NEGATIVE MARGIN ID is a surrogate unique identifier for NEGATIVE MARGINS.';
COMMENT ON COLUMN farms.farm_negative_margins.premium_rate IS E'PREMIUMS RATE is the cost per unit insured/guaranteed for the required insurance.';
COMMENT ON COLUMN farms.farm_negative_margins.premiums_paid IS E'PREMIUMS PAID is the premiums paid.';
COMMENT ON COLUMN farms.farm_negative_margins.reported_quantity IS E'REPORTED QUANTITY is the reported quantity produced for this FARMING OPERATION.';
COMMENT ON COLUMN farms.farm_negative_margins.required_insurable_value IS E'REQUIRED INSURABLE VALUE is the amount of insurance the participant is required to purchase per unit of quantity.';
COMMENT ON COLUMN farms.farm_negative_margins.required_premium IS E'REQUIRED PREMIUM is the base cost for the required insurance (the product of INSURABLE VALUE and GUARANTEED PROD VALUE), not including MARKET RATE PREMIUM (MRP).';
COMMENT ON COLUMN farms.farm_negative_margins.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_negative_margins.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_negative_margins.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_negative_margins.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_negative_margins.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_nm_farm_fo_fk_i ON farms.farm_negative_margins (farming_operation_id);
CREATE INDEX farm_nm_farm_ic_fk_i ON farms.farm_negative_margins (inventory_item_code);
ALTER TABLE farms.farm_negative_margins ADD CONSTRAINT farm_nm_uk UNIQUE (agristability_scenario_id,farming_operation_id,inventory_item_code);
ALTER TABLE farms.farm_negative_margins ADD CONSTRAINT farm_nm_pk PRIMARY KEY (negative_margin_id);
ALTER TABLE farms.farm_negative_margins ALTER COLUMN negative_margin_id SET NOT NULL;
ALTER TABLE farms.farm_negative_margins ALTER COLUMN farming_operation_id SET NOT NULL;
ALTER TABLE farms.farm_negative_margins ALTER COLUMN inventory_item_code SET NOT NULL;
ALTER TABLE farms.farm_negative_margins ALTER COLUMN agristability_scenario_id SET NOT NULL;
ALTER TABLE farms.farm_negative_margins ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_negative_margins ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_negative_margins ALTER COLUMN when_created SET NOT NULL;
