CREATE TABLE farms.farm_inventory_item_attributes(
    inventory_item_attrib_id    numeric(10, 0)    NOT NULL,
    inventory_item_code            varchar(10)       NOT NULL,
    rollup_inventory_item_code     varchar(10),
    revision_count                 numeric(5, 0)     DEFAULT 1 NOT NULL,
    who_created                    varchar(30)       NOT NULL,
    when_created                    timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    who_updated                    varchar(30),
    when_updated                    timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.farm_inventory_item_attributes.inventory_item_attrib_id IS 'INVENTORY ITEM ATTRIBUTE ID is a surrogate unique identifier for INVENTORY ITEM ATTRIBUTEs.'
;
COMMENT ON COLUMN farms.farm_inventory_item_attributes.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN farms.farm_inventory_item_attributes.rollup_inventory_item_code IS 'ROLLUP INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN farms.farm_inventory_item_attributes.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.farm_inventory_item_attributes.who_created IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_inventory_item_attributes.when_created IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.farm_inventory_item_attributes.who_updated IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.farm_inventory_item_attributes.when_updated IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE farms.farm_inventory_item_attributes IS 'INVENTORY ITEM ATTRIBUTE is additional information about an INVENTORY ITEM CODE.'
;


CREATE INDEX ix_iia_riic ON farms.farm_inventory_item_attributes(rollup_inventory_item_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_iia_iic ON farms.farm_inventory_item_attributes(inventory_item_code)
 TABLESPACE pg_default
;

ALTER TABLE farms.farm_inventory_item_attributes ADD 
    CONSTRAINT pk_iia PRIMARY KEY (inventory_item_attrib_id) USING INDEX TABLESPACE pg_default 
;
