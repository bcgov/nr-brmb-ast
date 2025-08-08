CREATE TABLE farms.farm_structure_group_attributs(
    structure_group_attrib_id    numeric(10, 0)    NOT NULL,
    structure_group_code            varchar(10)       NOT NULL,
    rollup_structure_group_code     varchar(10),
    revision_count                  numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                     varchar(30)       NOT NULL,
    when_created                     timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                     varchar(30),
    when_updated                     timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_structure_group_attributs.structure_group_attrib_id IS 'STRUCTURE GROUP ATTRIBUTE ID is a surrogate unique identifier for STRUCTURE GROUP ATTRIBUTEs.'
;
COMMENT ON COLUMN farms.farm_structure_group_attributs.structure_group_code IS 'STRUCTURE GROUP CODE identifies the type of structure group.'
;
COMMENT ON COLUMN farms.farm_structure_group_attributs.rollup_structure_group_code IS 'STRUCTURE GROUP CODE identifies the type of structure group.'
;
COMMENT ON COLUMN farms.farm_structure_group_attributs.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_structure_group_attributs.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_structure_group_attributs.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_structure_group_attributs.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_structure_group_attributs.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_structure_group_attributs IS 'STRUCTURE GROUP ATTRIBUTE is additional information about an INVENTORY ITEM CODE.'
;


CREATE INDEX ix_sga_rsgc ON farms.farm_structure_group_attributs(rollup_structure_group_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_sga_sgc ON farms.farm_structure_group_attributs(structure_group_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_structure_group_attributs ADD 
    CONSTRAINT pk_sga PRIMARY KEY (structure_group_attrib_id) USING INDEX TABLESPACE pg_default 
;
