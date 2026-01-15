CREATE TABLE farms.farm_import_logs (
	import_log_id bigint NOT NULL,
	log_message varchar(4000) NOT NULL,
	import_version_id bigint NOT NULL,
	program_year_version_id bigint
) ;
COMMENT ON TABLE farms.farm_import_logs IS E'IMPORT LOG lists all the audit messages generated during an import. It is used to minimize a CLOB append performance problem. Every time an import is done all the IMPORT LOGs will be deleted.';
COMMENT ON COLUMN farms.farm_import_logs.import_log_id IS E'FARMING OPERATIN PRTNR ID is a surrogate unique identifier for FARMING OPERATION PARTNERs.';
COMMENT ON COLUMN farms.farm_import_logs.import_version_id IS E'IMPORT VERSION ID is a surrogate unique identifier for IMPORT VERSIONs.';
COMMENT ON COLUMN farms.farm_import_logs.log_message IS E'USER COMMENT are notes that may be optionally provided by the user about the PROGRAM YEAR for the given client.';
COMMENT ON COLUMN farms.farm_import_logs.program_year_version_id IS E'PROGRAM YEAR VERSION ID is a surrogate unique identifier for PROGRAM YEAR VERSIONS.';
CREATE INDEX farm_ilg_farm_iv_fk_i ON farms.farm_import_logs (import_version_id);
CREATE INDEX farm_ilg_farm_pyv_fk_i ON farms.farm_import_logs (program_year_version_id);
CREATE INDEX farm_ilg_iv_pyv_i ON farms.farm_import_logs (import_version_id, program_year_version_id);
ALTER TABLE farms.farm_import_logs ADD CONSTRAINT farm_ilg_pk PRIMARY KEY (import_log_id);
ALTER TABLE farms.farm_import_logs ALTER COLUMN import_log_id SET NOT NULL;
ALTER TABLE farms.farm_import_logs ALTER COLUMN log_message SET NOT NULL;
ALTER TABLE farms.farm_import_logs ALTER COLUMN import_version_id SET NOT NULL;
