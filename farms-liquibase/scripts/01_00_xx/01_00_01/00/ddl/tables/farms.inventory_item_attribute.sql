CREATE TABLE inventory_item_attribute(
    inventory_item_attribute_id    numeric(10, 0)    NOT NULL,
    inventory_item_code            varchar(10)       NOT NULL,
    rollup_inventory_item_code     varchar(10),
    revision_count                 numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                    varchar(30)       NOT NULL,
    create_date                    timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                    varchar(30),
    update_date                    timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN inventory_item_attribute.inventory_item_attribute_id IS 'INVENTORY ITEM ATTRIBUTE ID is a surrogate unique identifier for INVENTORY ITEM ATTRIBUTEs.'
;
COMMENT ON COLUMN inventory_item_attribute.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN inventory_item_attribute.rollup_inventory_item_code IS 'ROLLUP INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN inventory_item_attribute.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN inventory_item_attribute.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN inventory_item_attribute.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN inventory_item_attribute.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN inventory_item_attribute.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE inventory_item_attribute IS 'INVENTORY ITEM ATTRIBUTE is additional information about an INVENTORY ITEM CODE.'
;

