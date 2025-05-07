CREATE TABLE structure_group_attribute(
    structure_group_attribute_id    numeric(10, 0)    NOT NULL,
    structure_group_code            varchar(10)       NOT NULL,
    rollup_structure_group_code     varchar(10),
    revision_count                  numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                     varchar(30)       NOT NULL,
    create_date                     timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                     varchar(30),
    update_date                     timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN structure_group_attribute.structure_group_attribute_id IS 'STRUCTURE GROUP ATTRIBUTE ID is a surrogate unique identifier for STRUCTURE GROUP ATTRIBUTEs.'
;
COMMENT ON COLUMN structure_group_attribute.structure_group_code IS 'STRUCTURE GROUP CODE identifies the type of structure group.'
;
COMMENT ON COLUMN structure_group_attribute.rollup_structure_group_code IS 'STRUCTURE GROUP CODE identifies the type of structure group.'
;
COMMENT ON COLUMN structure_group_attribute.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN structure_group_attribute.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN structure_group_attribute.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN structure_group_attribute.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN structure_group_attribute.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE structure_group_attribute IS 'STRUCTURE GROUP ATTRIBUTE is additional information about an INVENTORY ITEM CODE.'
;


CREATE INDEX ix_sga_rsgc ON structure_group_attribute(rollup_structure_group_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_sga_sgc ON structure_group_attribute(structure_group_code)
 TABLESPACE pg_default
;
