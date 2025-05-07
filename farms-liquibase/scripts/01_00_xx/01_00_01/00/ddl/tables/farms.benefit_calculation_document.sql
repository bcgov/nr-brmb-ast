CREATE TABLE benefit_calculation_document(
    benefit_calculation_document_id    numeric(10, 0)    NOT NULL,
    generation_date                    timestamp(6)      NOT NULL,
    document                           bytea             NOT NULL,
    agristability_scenario_id          numeric(10, 0)    NOT NULL,
    revision_count                     numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                        varchar(30)       NOT NULL,
    create_date                        timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                        varchar(30),
    update_date                        timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN benefit_calculation_document.benefit_calculation_document_id IS 'BENEFIT CALCULATION DOCUMENT ID is a surrogate unique identifier for BENEFIT CALCULATION DOCUMENTs.'
;
COMMENT ON COLUMN benefit_calculation_document.generation_date IS 'GENERATION DATE is the date the document was generated.'
;
COMMENT ON COLUMN benefit_calculation_document.document IS 'DOCUMENT is the PDF for the ''Calculation of Benefits'' notice sent to the grower.'
;
COMMENT ON COLUMN benefit_calculation_document.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN benefit_calculation_document.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN benefit_calculation_document.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN benefit_calculation_document.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN benefit_calculation_document.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN benefit_calculation_document.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;


CREATE UNIQUE INDEX ix_bcd_asi ON benefit_calculation_document(agristability_scenario_id)
 TABLESPACE pg_default
;
