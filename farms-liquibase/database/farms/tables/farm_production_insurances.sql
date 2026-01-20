CREATE TABLE farms.farm_production_insurances (
	production_insurance_id bigint NOT NULL,
	production_insurance_number varchar(12) NOT NULL,
	locally_updated_ind varchar(1) NOT NULL DEFAULT 'N',
	farming_operation_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_production_insurances IS E'PRODUCTION INSURANCE provides the production Insurance contract numbers provided by the participant on the supplemental page of the AgriStability application.';
COMMENT ON COLUMN farms.farm_production_insurances.farming_operation_id IS E'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.';
COMMENT ON COLUMN farms.farm_production_insurances.locally_updated_ind IS E'LOCALLY UPDATED IND identifies if the record was updated after being imported into the system. If this value is ''Y'', the client has updated the PRODUCTION INSUREANCE information for a given year; as a result, the FARM system will not allow the overwriting of that particular PRODUCTION INSUREANCE data by subsequent imports for the same year.';
COMMENT ON COLUMN farms.farm_production_insurances.production_insurance_id IS E'PRODUCTION INSURANCE ID is a surrogate unique identifier for PRODUCTION INSURANCE.';
COMMENT ON COLUMN farms.farm_production_insurances.production_insurance_number IS E'PRODUCTION INSURANCE NUMBER is the contract number provided by the participant on the supplemental page of the AgriStability application.';
COMMENT ON COLUMN farms.farm_production_insurances.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_production_insurances.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_production_insurances.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_production_insurances.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_production_insurances.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_pi_farm_fo_fk_i ON farms.farm_production_insurances (farming_operation_id);
ALTER TABLE farms.farm_production_insurances ADD CONSTRAINT farm_pi_pk PRIMARY KEY (production_insurance_id);
ALTER TABLE farms.farm_production_insurances ADD CONSTRAINT avcon_1260470137_local_002 CHECK (locally_updated_ind IN ('N', 'Y'));
ALTER TABLE farms.farm_production_insurances ALTER COLUMN production_insurance_id SET NOT NULL;
ALTER TABLE farms.farm_production_insurances ALTER COLUMN production_insurance_number SET NOT NULL;
ALTER TABLE farms.farm_production_insurances ALTER COLUMN locally_updated_ind SET NOT NULL;
ALTER TABLE farms.farm_production_insurances ALTER COLUMN farming_operation_id SET NOT NULL;
ALTER TABLE farms.farm_production_insurances ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_production_insurances ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_production_insurances ALTER COLUMN when_created SET NOT NULL;
