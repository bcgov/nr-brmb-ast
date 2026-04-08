CREATE TABLE farms.farm_structure_group_attributs (
	structure_group_attrib_id bigint NOT NULL,
	structure_group_code varchar(10) NOT NULL,
	rollup_structure_group_code varchar(10),
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_structure_group_attributs IS E'STRUCTURE_GROUP ATTRIBUT is additional information about an INVENTORY ITEM CODE.';
COMMENT ON COLUMN farms.farm_structure_group_attributs.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_structure_group_attributs.rollup_structure_group_code IS E'ROLLUP STRUCTURE GROUP CODE identifies the type of structure group.';
COMMENT ON COLUMN farms.farm_structure_group_attributs.structure_group_attrib_id IS E'STRUCTURE GROUP ATTRIB ID is a surrogate unique identifier for STRUCTURE_GROUP ATTRIBUTs.';
COMMENT ON COLUMN farms.farm_structure_group_attributs.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_structure_group_attributs.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_structure_group_attributs.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_structure_group_attributs.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_sga_farm_ic_fk_i ON farms.farm_structure_group_attributs (rollup_structure_group_code);
ALTER TABLE farms.farm_structure_group_attributs ADD CONSTRAINT farm_sga_pk PRIMARY KEY (structure_group_attrib_id);
ALTER TABLE farms.farm_structure_group_attributs ADD CONSTRAINT farm_sga_uk UNIQUE (structure_group_code);
ALTER TABLE farms.farm_structure_group_attributs ALTER COLUMN structure_group_attrib_id SET NOT NULL;
ALTER TABLE farms.farm_structure_group_attributs ALTER COLUMN structure_group_code SET NOT NULL;
ALTER TABLE farms.farm_structure_group_attributs ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_structure_group_attributs ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_structure_group_attributs ALTER COLUMN when_created SET NOT NULL;
