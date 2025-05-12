CREATE TABLE farms.reasonability_revenue_hog_result(
    reasonability_revenue_hog_result_id     numeric(10, 0)    NOT NULL,
    has_hogs_indicator                      varchar(1)        NOT NULL,
    hogs_pass_indicator                     varchar(1)        NOT NULL,
    farrow_to_finish_operation_indicator    varchar(1)        NOT NULL,
    feeder_operation_indicator              varchar(1)        NOT NULL,
    reported_expenses                       numeric(13, 2)    NOT NULL,
    total_quantity_start                    numeric(14, 3)    NOT NULL,
    total_quantity_end                      numeric(14, 3)    NOT NULL,
    farrow_sows_breeding                    numeric(13, 3)    NOT NULL,
    farrow_births_per_cycle                 numeric(4, 2)     NOT NULL,
    farrow_birth_cycles_per_year            numeric(3, 2)     NOT NULL,
    farrow_total_births_per_cycle           numeric(16, 3)    NOT NULL,
    farrow_births_all_cycles                numeric(16, 3)    NOT NULL,
    farrow_death_rate                       numeric(3, 3)     NOT NULL,
    farrow_deaths                           numeric(14, 3)    NOT NULL,
    farrow_boar_purchase_count              numeric(13, 2)    NOT NULL,
    farrow_boar_purchase_price              numeric(13, 2),
    farrow_boar_purchase_expense            numeric(13, 2)    NOT NULL,
    farrow_sow_purchase_expense             numeric(13, 2)    NOT NULL,
    farrow_sow_purchase_count               numeric(13, 2)    NOT NULL,
    farrow_sow_purchase_price               numeric(13, 2),
    feeder_productive_units                 numeric(13, 3)    NOT NULL,
    feeder_weanling_purchase_expense        numeric(13, 2)    NOT NULL,
    feeder_weanling_purchase_price          numeric(13, 2),
    feeder_weanling_purchase_count          numeric(13, 2)    NOT NULL,
    total_purchase_count                    numeric(13, 2)    NOT NULL,
    expected_sold                           numeric(17, 3)    NOT NULL,
    heaviest_hog_price                      numeric(13, 2),
    reported_revenue                        numeric(13, 2)    NOT NULL,
    expected_revenue                        numeric(25, 2),
    revenue_variance                        numeric(16, 3),
    revenue_variance_limit                  numeric(16, 3),
    reasonability_test_result_id            numeric(10, 0)    NOT NULL,
    revision_count                          numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                             varchar(30)       NOT NULL,
    create_date                             timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                             varchar(30),
    update_date                             timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.reasonability_revenue_hog_result.reasonability_revenue_hog_result_id IS 'REASONABILITY REVENUE HOG RESULT ID is a surrogate unique identifier for REASONABILITY REVENUE HOG RESULT.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.has_hogs_indicator IS 'HAS HOGS INDICATOR denotes whether this operation has hogs.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.hogs_pass_indicator IS 'HOGS PASS INDICATOR denotes whether Revenue Risk - Hogs subtest passed.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_to_finish_operation_indicator IS 'FARROW TO FINISH OPERATION INDICATOR denotes whether this is a Farrow to Finish Operation, meaning that they breed pigs. Determined if there is PRODUCTIVE UNIT CAPACITY for code 123.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.feeder_operation_indicator IS 'FEEDER OPERATION INDICATOR denotes whether this is a Feeder Operation, meaning that they buy young weanling pigs and feed them until they are large enough to sell (finished weight). Determined if there is not PRODUCTIVE UNIT CAPACITY for code 123, but there is for code 124.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.reported_expenses IS 'REPORTED EXPENSES is the total expense reported for REPORTED INCOME EXPENSE items with COMMODITY TYPE CODE: HOG.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.total_quantity_start IS 'TOTAL QUANTITY START is the total of QUANTITY START for REPORTED INVENTORY items with COMMODITY TYPE CODE: HOG.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.total_quantity_end IS 'TOTAL QUANTITY END is the total of QUANTITY END for REPORTED INVENTORY items with COMMODITY TYPE CODE: HOG.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_sows_breeding IS 'FARROW SOWS BREEDING is the number of female pigs breeding. This is the productive units from PRODUCTIVE UNIT CAPACITY with STRUCTURE GROUP CODE 123.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_births_per_cycle IS 'FARROW BIRTHS PER CYCLE is the configured average number of births per sow per cycle.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_birth_cycles_per_year IS 'FARROW BIRTH CYCLES PER YEAR is the configured average number of birth cycles per sow per year.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_total_births_per_cycle IS 'FARROW TOTAL BIRTHS PER CYCLE is the total births for all sows per cycle.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_births_all_cycles IS 'FARROW BIRTHS ALL CYCLES is the expected total number of births per year for all sows.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_death_rate IS 'FARROW DEATH RATE is the configured average percentage of deaths of pigs born.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_deaths IS 'FARROW DEATHS is the number of deaths of pigs born.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_boar_purchase_count IS 'FARROW BOAR PURCHASE COUNT is the expected number of boars purchased for breeding for a Farrow to Finish operation. This is estimated based on an inventory increase for boars, QUANTITY END minus QUANTITY START.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_boar_purchase_price IS 'FARROW BOAR PURCHASE PRICE is the reported price per boar purchased. This is the Price End from REPORTED INVENTORY for the Boar inventory code, 8752.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_boar_purchase_expense IS 'FARROW BOAR PURCHASE EXPENSE is the total expected expense of boar purchases. This is the FARROW BOAR PURCHASE COUNT multiplied by the FARROW BOAR PURCHASE PRICE.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_sow_purchase_expense IS 'FARROW SOW PURCHASE EXPENSE is the total expected expense of sow purchases. This is estimated based on the expense amount in REPORTED INCOME EXPENSE with COMMODITY TYPE CODE: HOG, minus the FARROW BOAR PURCHASE EXPENSE.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_sow_purchase_count IS 'FARROW SOW PURCHASE COUNT is the expected number of sows purchased for breeding for a Farrow to Finish operation. This is the FARROW SOW PURCHASE EXPENSE divided by the FARROW SOW PURCHASE PRICE.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.farrow_sow_purchase_price IS 'FARROW SOW PURCHASE PRICE is the reported price per sow purchased. This is the Price End from REPORTED INVENTORY for the Sow inventory code, 8754.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.feeder_productive_units IS 'FEEDER PRODUCTIVE UNITS is the number of pigs being fed until they reach finishing weight. This is the productive units from PRODUCTIVE UNIT CAPACITY with STRUCTURE GROUP CODE 124.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.feeder_weanling_purchase_expense IS 'FEEDER WEANLING PURCHASE EXPENSE is the total expense for a Feeder Operation of weanlings (young pigs) purchased.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.feeder_weanling_purchase_price IS 'FEEDER WEANLING PURCHASE PRICE is the reported price per weanling (young pig) by a Feeder Operation. This is the Price Start  from REPORTED INVENTORY for the Hogs; Feeder; Births inventory code, 8763.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.feeder_weanling_purchase_count IS 'FEEDER WEANLING PURCHASE COUNT is the expected number of weanlings (young pigs) purchased by a Feeder Operation. This is the FEEDER WEANLING PURCHASE EXPENSE divided by the FEEDER WEANLING PURCHASE PRICE.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.total_purchase_count IS 'TOTAL PURCHASE COUNT is the total number of pigs purchased. This is the sum of FARROW BOAR PURCHASE COUNT, FARROW SOW PURCHASE COUNT, and FEEDER WEANLING PURCHASE COUNT.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.expected_sold IS 'EXPECTED SOLD is the expected number of hogs sold.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.heaviest_hog_price IS 'HEAVIEST HOG PRICE is the End Price of the heaviest hog in the inventory of this farm.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.reported_revenue IS 'REPORTED REVENUE is the total revenue reported for REPORTED INCOME EXPENSE items with COMMODITY TYPE CODE: HOG.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.expected_revenue IS 'EXPECTED REVENUE is the total revenue expected based on REPORTED INVENTORY items with COMMODITY TYPE CODE: NURSERY.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.revenue_variance IS 'REVENUE VARIANCE is the percent difference between REPORTED REVENUE and EXPECTED REVENUE.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.revenue_variance_limit IS 'REVENUE VARIANCE LIMIT is the configured limit for the percentage difference between REPORTED REVENUE and EXPECTED REVENUE. If it is outside this limit, the test fails.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.reasonability_test_result_id IS 'REASONABILITY TEST RESULT ID is a surrogate unique identifier for REASONABILITY TEST RESULT.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.reasonability_revenue_hog_result.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.reasonability_revenue_hog_result IS 'REASONABILITY REVENUE HOG RESULT contains the results of the Revenue Risk - Hogs Subtest of the scenario.'
;


CREATE UNIQUE INDEX uk_rrhr_rtri ON farms.reasonability_revenue_hog_result(reasonability_test_result_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.reasonability_revenue_hog_result ADD 
    CONSTRAINT pk_rrhr PRIMARY KEY (reasonability_revenue_hog_result_id) USING INDEX TABLESPACE pg_default 
;
