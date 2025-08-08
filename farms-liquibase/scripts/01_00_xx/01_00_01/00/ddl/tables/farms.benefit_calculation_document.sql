CREATE TABLE farms.farm_benefit_calc_documents(
    benefit_calc_document_id    numeric(10, 0)    NOT NULL,
    generation_date                    timestamp(6)      NOT NULL,
    document                           bytea             NOT NULL,
    agristability_scenario_id          numeric(10, 0)    NOT NULL,
    revision_count                     numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                        varchar(30)       NOT NULL,
    when_created                        timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                        varchar(30),
    when_updated                        timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_benefit_calc_documents.benefit_calc_document_id IS 'BENEFIT CALCULATION DOCUMENT ID is a surrogate unique identifier for BENEFIT CALCULATION DOCUMENTs.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_documents.generation_date IS 'GENERATION DATE is the date the document was generated.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_documents.document IS 'DOCUMENT is the PDF for the ''Calculation of Benefits'' notice sent to the grower.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_documents.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_documents.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_documents.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_documents.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_documents.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_benefit_calc_documents.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;


CREATE UNIQUE INDEX ix_bcd_asi ON farms.farm_benefit_calc_documents(agristability_scenario_id)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_benefit_calc_documents ADD 
    CONSTRAINT pk_bcd PRIMARY KEY (benefit_calc_document_id) USING INDEX TABLESPACE pg_default 
;
