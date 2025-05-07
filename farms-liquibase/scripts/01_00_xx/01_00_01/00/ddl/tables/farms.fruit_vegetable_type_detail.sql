CREATE TABLE fruit_vegetable_type_detail(
    fruit_vegetable_type_detail_id    numeric(10, 0)    NOT NULL,
    revenue_variance_limit            numeric(16, 3)    NOT NULL,
    fruit_vegetable_type_code         varchar(10)       NOT NULL,
    revision_count                    numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                       varchar(30)       NOT NULL,
    create_date                       timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                       varchar(30),
    update_date                       timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN fruit_vegetable_type_detail.fruit_vegetable_type_detail_id IS 'FRUIT VEGETABLE TYPE DETAIL ID is a surrogate unique identifier for a FRUIT VEGETABLE TYPE DETAIL.'
;
COMMENT ON COLUMN fruit_vegetable_type_detail.revenue_variance_limit IS 'REVENUE VARIANCE LIMIT is the variance limit on the revenue of a FRUIT VEGETABLE TYPE CODE when comparing it to the EXPECTED PRICE PER POUND. For use with the Reasonability Test: Fruits and Vegetables Revenue Test. If a scenario has a variance greater than the defined limit for a FRUIT VEGETABLE TYPE CODE, the test will fail.'
;
COMMENT ON COLUMN fruit_vegetable_type_detail.fruit_vegetable_type_code IS 'FRUIT VEGETABLE TYPE CODE is a unique code for the object FRUIT VEGETABLE TYPE CODE. Examples of codes and descriptions are APPLE - Apples, BEAN - Beans,  POTATO - Potatoes.'
;
COMMENT ON COLUMN fruit_vegetable_type_detail.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN fruit_vegetable_type_detail.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN fruit_vegetable_type_detail.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN fruit_vegetable_type_detail.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN fruit_vegetable_type_detail.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE fruit_vegetable_type_detail IS 'FRUIT VEGETABLE TYPE DETAIL defines details of a FRUIT VEGETABLE TYPE CODE for use with the Reasonability Test: Fruits and Vegetables Revenue Test.'
;


CREATE UNIQUE INDEX uk_fvtd_fvtc ON fruit_vegetable_type_detail(fruit_vegetable_type_code)
 TABLESPACE pg_default
;

ALTER TABLE fruit_vegetable_type_detail ADD 
    CONSTRAINT pk_fvtd PRIMARY KEY (fruit_vegetable_type_detail_id) USING INDEX TABLESPACE pg_default 
;
