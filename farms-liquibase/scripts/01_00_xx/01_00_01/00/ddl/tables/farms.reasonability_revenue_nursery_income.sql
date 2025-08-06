CREATE TABLE farms.farm_rsn_rev_nursery_incomes(
    rsn_rev_nursery_income_id    numeric(10, 0)    NOT NULL,
    line_item                                  numeric(4, 0)     NOT NULL,
    reported_revenue                           numeric(13, 2)    NOT NULL,
    rsn_rev_nursery_result_id    numeric(10, 0)    NOT NULL,
    revision_count                             numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                                varchar(30)       NOT NULL,
    when_created                                timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                                varchar(30),
    when_updated                                timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.rsn_rev_nursery_income_id IS 'REASONABILITY REVENUE NURSERY INCOME ID is a surrogate unique identifier for REASONABILITY REVENUE NURSERY INCOME.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.line_item IS 'LINE ITEM is income or expense item for Agristability.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.reported_revenue IS 'REPORTED REVENUE is the total income for this LINE ITEM. This is the sum of the reported income for line items associated with this type plus the change in value for receivable accruals for inventory codes associated with this type.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.rsn_rev_nursery_result_id IS 'REASONABILITY REVENUE NURSERY RESULT ID is a surrogate unique identifier for REASONABILITY REVENUE NURSERY RESULT.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_rsn_rev_nursery_incomes.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_rsn_rev_nursery_incomes IS 'REASONABILITY REVENUE NURSERY INCOME contains the calculated amounts for the Reasonability Test: Grains, Forage, and Forage Seed Revenue Test run against the scenario, by LINE ITEM.'
;


CREATE UNIQUE INDEX uk_rrni_rrnri_li ON farms.farm_rsn_rev_nursery_incomes(rsn_rev_nursery_result_id, line_item)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_rsn_rev_nursery_incomes ADD 
    CONSTRAINT pk_rrni PRIMARY KEY (rsn_rev_nursery_income_id) USING INDEX TABLESPACE pg_default 
;
