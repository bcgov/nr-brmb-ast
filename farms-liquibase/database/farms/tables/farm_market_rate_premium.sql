CREATE TABLE farms.farm_market_rate_premium (
	market_rate_premium_id bigint NOT NULL,
	min_total_premium_amount decimal(13,2) NOT NULL,
	max_total_premium_amount decimal(13,2) NOT NULL,
	risk_charge_flat_amount decimal(13,2) NOT NULL,
	risk_charge_pct_premium decimal(13,2) NOT NULL,
	adjust_charge_flat_amount decimal(13,2) NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_market_rate_premium IS E'MARKET RATE PREMIUM is the premium rate for the market rate premium.';
COMMENT ON COLUMN farms.farm_market_rate_premium.adjust_charge_flat_amount IS E'ADJUST CHARGE FLAT AMOUNT is the flat amount of the adjust charge.';
COMMENT ON COLUMN farms.farm_market_rate_premium.market_rate_premium_id IS E'MARKET RATE PREMIUM ID is a surrogate unique identifier for MARKET RATE PREMIUM.';
COMMENT ON COLUMN farms.farm_market_rate_premium.max_total_premium_amount IS E'MAX TOTAL PREMIUM AMOUNT is the maximum total premium amount.';
COMMENT ON COLUMN farms.farm_market_rate_premium.min_total_premium_amount IS E'MIN TOTAL PREMIUM AMOUNT is the minimum total premium amount.';
COMMENT ON COLUMN farms.farm_market_rate_premium.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_market_rate_premium.risk_charge_flat_amount IS E'RISK CHARGE FLAT AMOUNT is the flat amount of the risk charge.';
COMMENT ON COLUMN farms.farm_market_rate_premium.risk_charge_pct_premium IS E'RISK CHARGE PCT PREMIUM is the percentage of the premium.';
COMMENT ON COLUMN farms.farm_market_rate_premium.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_market_rate_premium.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_market_rate_premium.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_market_rate_premium.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
ALTER TABLE farms.farm_market_rate_premium ADD CONSTRAINT farm_mrp_pk PRIMARY KEY (market_rate_premium_id);
ALTER TABLE farms.farm_market_rate_premium ALTER COLUMN market_rate_premium_id SET NOT NULL;
ALTER TABLE farms.farm_market_rate_premium ALTER COLUMN min_total_premium_amount SET NOT NULL;
ALTER TABLE farms.farm_market_rate_premium ALTER COLUMN max_total_premium_amount SET NOT NULL;
ALTER TABLE farms.farm_market_rate_premium ALTER COLUMN risk_charge_flat_amount SET NOT NULL;
ALTER TABLE farms.farm_market_rate_premium ALTER COLUMN risk_charge_pct_premium SET NOT NULL;
ALTER TABLE farms.farm_market_rate_premium ALTER COLUMN adjust_charge_flat_amount SET NOT NULL;
ALTER TABLE farms.farm_market_rate_premium ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_market_rate_premium ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_market_rate_premium ALTER COLUMN when_created SET NOT NULL;
