CREATE TABLE inventory_item_code(
    inventory_item_code    varchar(10)      NOT NULL,
    description            varchar(256)     NOT NULL,
    established_date       date             NOT NULL,
    expiry_date            date             NOT NULL,
    revision_count         numeric(5, 0)    NOT NULL,
    create_user            varchar(30)      NOT NULL,
    create_date            timestamp(6)     NOT NULL,
    update_user            varchar(30),
    update_date            timestamp(6)
) TABLESPACE pg_default
;



COMMENT ON COLUMN inventory_item_code.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN inventory_item_code.description IS 'DESCRIPTION is a textual description of the code value.'
;
COMMENT ON COLUMN inventory_item_code.established_date IS 'ESTABLISHED DATE identifies the effective date of the record.'
;
COMMENT ON COLUMN inventory_item_code.expiry_date IS 'EXPIRY DATE identifies the date this record is no longer valid.'
;
COMMENT ON COLUMN inventory_item_code.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN inventory_item_code.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN inventory_item_code.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN inventory_item_code.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN inventory_item_code.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE inventory_item_code IS 'INVENTORY ITEM CODE is a numeric code used to uniquely identify an inventory item.'
;

ALTER TABLE inventory_item_code ADD 
    CONSTRAINT pk_iic PRIMARY KEY (inventory_item_code) USING INDEX TABLESPACE pg_default 
;
