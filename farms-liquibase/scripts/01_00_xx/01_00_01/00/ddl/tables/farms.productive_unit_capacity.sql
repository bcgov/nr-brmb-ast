CREATE TABLE productive_unit_capacity(
    productve_unit_capacity_id         numeric(10, 0)    NOT NULL,
    productive_capacity_amount         numeric(13, 3)    NOT NULL,
    import_comment                     varchar(128),
    inventory_item_code                varchar(10),
    structure_group_code               varchar(10),
    participant_data_source_code       varchar(10)       DEFAULT 'CRA' NOT NULL,
    farming_operation_id               numeric(10, 0)    NOT NULL,
    agristability_scenario_id          numeric(10, 0),
    cra_productive_unit_capacity_id    numeric(10, 0),
    revision_count                     numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                        varchar(30)       NOT NULL,
    create_date                        timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                        varchar(30),
    update_date                        timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN productive_unit_capacity.productve_unit_capacity_id IS 'PRODUCTIVE UNIT CAPACITY ID is a surrogate unique identifier for PRODUCTIVE UNIT CAPACITIES.'
;
COMMENT ON COLUMN productive_unit_capacity.productive_capacity_amount IS 'PRODUCTIVE CAPACITY AMOUNT is the quantity entered in section 9 for this inventory/bpu code.'
;
COMMENT ON COLUMN productive_unit_capacity.import_comment IS 'IMPORT COMMENT is a system generated comment about how the original data might have been modified to enable the data to be inserted into the operational tables. Usually this involves changing an invalid code.'
;
COMMENT ON COLUMN productive_unit_capacity.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN productive_unit_capacity.structure_group_code IS 'STRUCTURE GROUP CODE identifies the type of structure group.'
;
COMMENT ON COLUMN productive_unit_capacity.participant_data_source_code IS 'PARTICIPANT DATA SOURCE CODE is a unique code for the object PARTICIPANT DATA SOURCE CODE. Examples of codes and descriptions are CRA - CRA, LOCAL - Local.'
;
COMMENT ON COLUMN productive_unit_capacity.farming_operation_id IS 'FARMING OPERATION ID is a surrogate unique identifier for FARMING OPERATIONs.'
;
COMMENT ON COLUMN productive_unit_capacity.agristability_scenario_id IS 'AGRISTABILITY SCENARIO ID is a surrogate unique identifier for AGRISTABILITY SCENARIO.'
;
COMMENT ON COLUMN productive_unit_capacity.cra_productive_unit_capacity_id IS 'PRODUCTIVE UNIT CAPACITY ID is a surrogate unique identifier for PRODUCTIVE UNIT CAPACITIES.'
;
COMMENT ON COLUMN productive_unit_capacity.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN productive_unit_capacity.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN productive_unit_capacity.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN productive_unit_capacity.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN productive_unit_capacity.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE productive_unit_capacity IS 'PRODUCTIVE UNIT CAPACITY is a measure of the productive capacity for a given AGRASTABILITY COMMODITY. A PRODUCTIVE UNIT CAPACITY associates with a specific AGRASTABILITY COMMODITY. A PRODUCTIVE UNIT CAPACITY may have more than one PRODUCTIVE UNIT CAPACITY ADJUSTMENT. A PRODUCTIVE UNIT CAPACITY is received via federal imports and through provincial data imports.'
;


CREATE INDEX ix_puc_asi ON productive_unit_capacity(agristability_scenario_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_puc_foi ON productive_unit_capacity(farming_operation_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_puc_iic ON productive_unit_capacity(inventory_item_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_puc_pdsc ON productive_unit_capacity(participant_data_source_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_puc_cpuci ON productive_unit_capacity(cra_productive_unit_capacity_id)
 TABLESPACE pg_default
;
CREATE INDEX ix_puc_sgc ON productive_unit_capacity(structure_group_code)
 TABLESPACE pg_default
;

ALTER TABLE productive_unit_capacity ADD 
    CONSTRAINT pk_puc PRIMARY KEY (productve_unit_capacity_id) USING INDEX TABLESPACE pg_default 
;
