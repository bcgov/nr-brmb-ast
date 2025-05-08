CREATE TABLE z29_inventory_code_reference(
    inventory_code                 numeric(5, 0)    NOT NULL,
    inventory_type_code            numeric(4, 0)    NOT NULL,
    inventory_description          varchar(254)     NOT NULL,
    inventory_type_description     varchar(254)     NOT NULL,
    inventory_group_code           numeric(4, 0),
    inventory_group_description    varchar(254),
    market_commodity_indicator     varchar(1)       DEFAULT 'Y' NOT NULL,
    revision_count                 numeric(5, 0)    DEFAULT 1 NOT NULL,
    create_user                    varchar(30)      NOT NULL,
    create_date                    timestamp(6)     DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user                    varchar(30),
    update_date                    timestamp(6)     DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN z29_inventory_code_reference.inventory_code IS 'INVENTORY CODE is a numeric code used to uniquely identify an inventory item.'
;
COMMENT ON COLUMN z29_inventory_code_reference.inventory_type_code IS 'INVENTORY TYPE CODE is a numeric code indicating an inventory type. Valid values are 1 - Crops Inventory, 2 - Livestock  Inventory, 3 - Purchased Inputs, 4 - Deferred Income and Receivables, 5 - Accounts Payable.'
;
COMMENT ON COLUMN z29_inventory_code_reference.inventory_description IS 'INVENTORY DESCRIPTION is the english description of the Inventory. An English text description of an inventory item.'
;
COMMENT ON COLUMN z29_inventory_code_reference.inventory_type_description IS 'INVENTORY TYPE DESCRIPTION is a description of the inventory type.'
;
COMMENT ON COLUMN z29_inventory_code_reference.inventory_group_code IS 'INVENTORY GROUP CODE is a number assigned to a group of inventory items.'
;
COMMENT ON COLUMN z29_inventory_code_reference.inventory_group_description IS 'INVENTORY GROUP DESCRIPTION is a description for the inventory group code.'
;
COMMENT ON COLUMN z29_inventory_code_reference.market_commodity_indicator IS 'MARKET COMMODITY INDICATOR indicates whether the commodity is a market commodity. An example of a non-market commodity are animals that are only used for breeding.'
;
COMMENT ON COLUMN z29_inventory_code_reference.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN z29_inventory_code_reference.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN z29_inventory_code_reference.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN z29_inventory_code_reference.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN z29_inventory_code_reference.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE z29_inventory_code_reference IS 'Z29 INVENTORY REFERENCE identifies a list of inventory (commodity) codes, and their associated descriptions used by FIPD. This file is created by FIPD. This is a staging object used to load temporary data set before being merged into the operational data.'
;


ALTER TABLE z29_inventory_code_reference ADD 
    CONSTRAINT pk_zicr PRIMARY KEY (inventory_code, inventory_type_code) USING INDEX TABLESPACE pg_default 
;
