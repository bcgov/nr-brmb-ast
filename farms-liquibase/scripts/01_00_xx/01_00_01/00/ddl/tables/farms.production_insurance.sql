CREATE TABLE production_insurance(
    production_insurance_id        numeric(10, 0)    NOT NULL,
    production_insurance_number    varchar(12)       NOT NULL,
    locally_updated_indicator      varchar(1)        DEFAULT 'N' NOT NULL,
    farming_operation_id           numeric(10, 0)    NOT NULL,
    revision_count                 numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                    varchar(30)       NOT NULL,
    create_date                    timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                    varchar(30),
    update_date                    timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN production_insurance.production_insurance_id IS 'PRODUCTION INSURANCE ID is a surrogate unique identifier for PRODUCTION INSURANCE.'
;
COMMENT ON COLUMN production_insurance.production_insurance_number IS 'PRODUCTION INSURANCE NUMBER is the contract number provided by the participant on the supplemental page of the AgriStability application.'
;
COMMENT ON COLUMN production_insurance.locally_updated_indicator IS 'LOCALLY UPDATED INDICATOR identifies if the record was updated after being imported into the system. If this value is ''Y'', the client has updated the PRODUCTION INSURANCE information for a given year; as a result, the FARM system will not allow the overwriting of that particular PRODUCTION INSURANCE data by subsequent imports for the same year.'
;
COMMENT ON COLUMN production_insurance.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN production_insurance.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN production_insurance.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN production_insurance.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN production_insurance.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN production_insurance.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE production_insurance IS 'PRODUCTION INSURANCE provides the production Insurance contract numbers provided by the participant on the supplemental page of the AgriStability application.'
;


CREATE INDEX ix_pi_foi ON production_insurance(farming_operation_id)
;
