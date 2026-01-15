CREATE TABLE farms.farm_farming_operations (
	farming_operation_id bigint NOT NULL,
	business_use_home_expense decimal(13,2),
	expenses decimal(13,2),
	fiscal_year_end timestamp(0),
	fiscal_year_start timestamp(0),
	gross_income decimal(13,2),
	inventory_adjustments decimal(13,2),
	crop_share_ind varchar(1) NOT NULL,
	feeder_member_ind varchar(1) NOT NULL,
	landlord_ind varchar(1) NOT NULL,
	net_farm_income decimal(13,2),
	net_income_after_adj decimal(13,2),
	net_income_before_adj decimal(13,2),
	other_deductions decimal(13,2),
	partnership_name varchar(42),
	partnership_percent decimal(7,4) NOT NULL,
	partnership_pin integer NOT NULL,
	operation_number smallint NOT NULL,
	crop_disaster_ind varchar(1),
	livestock_disaster_ind varchar(1),
	locally_updated_ind varchar(1) NOT NULL DEFAULT 'N',
	locally_generated_ind varchar(1) NOT NULL DEFAULT 'N',
	alignment_key varchar(2) NOT NULL,
	federal_accounting_code varchar(10),
	program_year_version_id bigint NOT NULL,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_farming_operations IS E'FARMING OPERATION refers to revenue and expense information for an AGRISTABILITY CLIENT- i.e. corresponding to tax return statements. An AGRISTABILITY CLIENT can have multiple instances of a FARMING OPERATION in a given Program Year. FARMING OPERATION data will originate from federal data imports. Updates to FARMING OPERATION data may be received for past years. FARMING OPERATION data will only exist from 2003 forward (i.e. the origin of the CAIS/AgriStability program).';
COMMENT ON COLUMN farms.farm_farming_operations.alignment_key IS E'ALIGNMENT KEY is used to align the same FARMING OPERATION across multiple years.';
COMMENT ON COLUMN farms.farm_farming_operations.business_use_home_expense IS E'BUSINESS USE HOME EXPENSE is the allowable portion of Business-use-of-home expenses (9934).';
COMMENT ON COLUMN farms.farm_farming_operations.crop_disaster_ind IS E'CROP DISASTER IND identifies if the productive capacity decreased due to disaster circumstances.';
COMMENT ON COLUMN farms.farm_farming_operations.crop_share_ind IS E'CROP SHARE IND denotes if this participant participated in a crop share as a tenant.';
COMMENT ON COLUMN farms.farm_farming_operations.expenses IS E'EXPENSES are the Total Expenses for the FARMING OPERATION (9968).';
COMMENT ON COLUMN farms.farm_farming_operations.farming_operation_id IS E'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.';
COMMENT ON COLUMN farms.farm_farming_operations.federal_accounting_code IS E'FEDERAL ACCOUNTING CODE is a unique code for the object FEDERAL ACCOUNTING CODE described as a numeric code used to uniquely identify the method of Accounting to RCT and NISA.  Examples of codes and descriptions are 1- Accrual, 2-Cash.';
COMMENT ON COLUMN farms.farm_farming_operations.feeder_member_ind IS E'FEEDER MEMBER IND denotes if this participant was a member of a feeder association.';
COMMENT ON COLUMN farms.farm_farming_operations.fiscal_year_end IS E'FISCAL YEAR END is the Operation at the end of the Fiscal Year  (yyyymmdd - may be blank).';
COMMENT ON COLUMN farms.farm_farming_operations.fiscal_year_start IS E'FISCAL YEAR START is the Operation at the start of the Fiscal year  (yyyymmdd - may be blank).';
COMMENT ON COLUMN farms.farm_farming_operations.gross_income IS E'GROSS INCOME is the income before taxes or deductions for the FARMING OPERATION. Uses form field number (9959) .';
COMMENT ON COLUMN farms.farm_farming_operations.inventory_adjustments IS E'INVENTORY ADJUSTMENTS  are the changes to reported inventory  quantities. Curr Year (9941 and 9942).';
COMMENT ON COLUMN farms.farm_farming_operations.landlord_ind IS E'LANDLORD IND denotes if this Participant carried on a farming business as a landlord.';
COMMENT ON COLUMN farms.farm_farming_operations.livestock_disaster_ind IS E'LIVESTOCK DISASTER IND identifies if productive capacity decreased due to disaster circumstances.';
COMMENT ON COLUMN farms.farm_farming_operations.locally_generated_ind IS E'LOCALLY GENERATED IND identifies if the record was created by a user. If this value is ''Y'', the client created this FARMING OPERATION information for a given year.';
COMMENT ON COLUMN farms.farm_farming_operations.locally_updated_ind IS E'LOCALLY UPDATED IND identifies if the record was updated after being imported into the system. If this value is ''Y'', the client has updated the FARMING OPERATION information for a given year; as a result, the FARM system will not allow the overwriting of that particular FARMING OPERATION data by subsequent imports for the same year.';
COMMENT ON COLUMN farms.farm_farming_operations.net_farm_income IS E'NET FARM INCOME denotes the Net Farming Income after adjustments (9946).';
COMMENT ON COLUMN farms.farm_farming_operations.net_income_after_adj IS E'NET INCOME AFTER ADJ is the Net Income after adjustments amount (line 9944).';
COMMENT ON COLUMN farms.farm_farming_operations.net_income_before_adj IS E'NET INCOME BEFORE ADJ is the Net Income before adjustment (9969).';
COMMENT ON COLUMN farms.farm_farming_operations.operation_number IS E'OPERATION NUMBER identifies each operation a participant reports for a given year. Operations may have different OPERATION NUMBER in different years.';
COMMENT ON COLUMN farms.farm_farming_operations.other_deductions IS E'OTHER DEDUCTIONS to the net income amount (line 9940).';
COMMENT ON COLUMN farms.farm_farming_operations.partnership_name IS E'PARTNERSHIP NAME is the name of the partnership for the FARMING OPERATION.';
COMMENT ON COLUMN farms.farm_farming_operations.partnership_percent IS E'PARTNERSHIP PERCENT is the Percentage of the  Partnership (100% will be stored as 1.0).';
COMMENT ON COLUMN farms.farm_farming_operations.partnership_pin IS E'PARTNERSHIP PIN uniquely identifies the partnership. If both the partners in an operation file applications, the same PARTNERSHIP PIN will show up under both pins. Partnership pins represent the same operation if/when they are used in different stab years.';
COMMENT ON COLUMN farms.farm_farming_operations.program_year_version_id IS E'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.';
COMMENT ON COLUMN farms.farm_farming_operations.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_farming_operations.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_farming_operations.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_farming_operations.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_farming_operations.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_fo_farm_aac_fk_i ON farms.farm_farming_operations (federal_accounting_code);
CREATE INDEX farm_fo_farm_pyv_fk_i ON farms.farm_farming_operations (program_year_version_id);
CREATE INDEX farm_fo_id_pyv_i ON farms.farm_farming_operations (farming_operation_id, program_year_version_id);
CREATE INDEX farm_fo_id_pyv_num_i ON farms.farm_farming_operations (farming_operation_id, operation_number, program_year_version_id);
ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT farm_fo_number_uk UNIQUE (program_year_version_id,operation_number);
ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT farm_fo_pk PRIMARY KEY (farming_operation_id);
ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT avcon_1299283225_crop__000 CHECK (crop_share_ind in ('N', 'Y'));
ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT avcon_1299283225_crop__001 CHECK (crop_disaster_ind in ('N', 'Y'));
ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT avcon_1299283225_feede_000 CHECK (feeder_member_ind in ('N', 'Y'));
ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT avcon_1299283225_landl_000 CHECK (landlord_ind in ('N', 'Y'));
ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT avcon_1299283225_lives_000 CHECK (livestock_disaster_ind in ('N', 'Y'));
ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT avcon_1299283225_local_003 CHECK (locally_updated_ind in ('N', 'Y'));
ALTER TABLE farms.farm_farming_operations ADD CONSTRAINT avcon_1299283225_local_005 CHECK (locally_generated_ind in ('N', 'Y'));
ALTER TABLE farms.farm_farming_operations ALTER COLUMN farming_operation_id SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN crop_share_ind SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN feeder_member_ind SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN landlord_ind SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN partnership_percent SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN partnership_pin SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN operation_number SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN locally_updated_ind SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN locally_generated_ind SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN alignment_key SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN program_year_version_id SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_farming_operations ALTER COLUMN when_created SET NOT NULL;
