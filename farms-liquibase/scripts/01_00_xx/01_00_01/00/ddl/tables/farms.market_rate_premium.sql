CREATE TABLE farms.farm_market_rate_premium(
    market_rate_premium_id          numeric(10, 0)    NOT NULL,
    min_total_premium_amount    numeric(13, 2)    NOT NULL,
    max_total_premium_amount    numeric(13, 2)    NOT NULL,
    risk_charge_flat_amount         numeric(13, 2)    NOT NULL,
    risk_charge_pct_premium     numeric(13, 2)    NOT NULL,
    adjust_charge_flat_amount       numeric(13, 2)    NOT NULL,
    revision_count                  numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                     varchar(30)       NOT NULL,
    when_created                     timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                     varchar(30),
    when_updated                     timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_market_rate_premium.market_rate_premium_id IS 'MARKET RATE PREMIUM ID is a surrogate unique identifier for MARKET RATE PREMIUM.'
;
COMMENT ON COLUMN farms.farm_market_rate_premium.min_total_premium_amount IS 'MINIMUM TOTAL PREMIUM AMOUNT is the minimum total premium amount.'
;
COMMENT ON COLUMN farms.farm_market_rate_premium.max_total_premium_amount IS 'MAXIMUM TOTAL PREMIUM AMOUNT is the maximum total premium amount.'
;
COMMENT ON COLUMN farms.farm_market_rate_premium.risk_charge_flat_amount IS 'RISK CHARGE FLAT AMOUNT is the flat amount of the risk charge.'
;
COMMENT ON COLUMN farms.farm_market_rate_premium.risk_charge_pct_premium IS 'RISK CHARGE PERCENT PREMIUM is the percentage of the premium.'
;
COMMENT ON COLUMN farms.farm_market_rate_premium.adjust_charge_flat_amount IS 'ADJUST CHARGE FLAT AMOUNT is the flat amount of the adjust charge.'
;
COMMENT ON COLUMN farms.farm_market_rate_premium.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_market_rate_premium.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_market_rate_premium.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_market_rate_premium.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_market_rate_premium.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_market_rate_premium IS 'MARKET RATE PREMIUM is the premium rate for the market rate premium.'
;


ALTER TABLE farms.farm_market_rate_premium ADD 
    CONSTRAINT pk_mrp PRIMARY KEY (market_rate_premium_id) USING INDEX TABLESPACE pg_default 
;
