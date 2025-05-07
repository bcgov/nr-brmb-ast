CREATE TABLE agristabilty_commodity_xref(
    agristabilty_commodity_xref_id    numeric(10, 0)    NOT NULL,
    market_commodity_indicator        varchar(1)        DEFAULT 'Y' NOT NULL,
    inventory_item_code               varchar(10)       NOT NULL,
    inventory_group_code              varchar(10),
    inventory_class_code              varchar(10)       NOT NULL,
    revision_count                    numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user                       varchar(30)       NOT NULL,
    create_date                       timestamp(6)      DEFAULT systimestamp NOT NULL,
    update_user                       varchar(30),
    update_date                       timestamp(6)      DEFAULT systimestamp
) TABLESPACE pg_default
;



COMMENT ON COLUMN agristabilty_commodity_xref.agristabilty_commodity_xref_id IS 'AGRISTABILTY COMMODITY XREF ID is a surrogate unique identifier for an AGRISTABILTY COMMODITY XREF.'
;
COMMENT ON COLUMN agristabilty_commodity_xref.market_commodity_indicator IS 'MARKET COMMODITY INDICATOR indicates whether the commodity is a market commodity. An example of a non-market commodity are animals that are only used for breeding.'
;
COMMENT ON COLUMN agristabilty_commodity_xref.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN agristabilty_commodity_xref.inventory_group_code IS 'INVENTORY GROUP CODE is a unique code for the object INVENTORY GROUP CODE described as a numeric code used to uniquely identify an inventory item group. Examples of codes and descriptions are 1 - Apples, 10 - Dry Beans, 11 - Edible Horticulture, 12 - Fadabeans, 13 - Field Peas.'
;
COMMENT ON COLUMN agristabilty_commodity_xref.inventory_class_code IS 'INVENTORY CLASS CODE is a unique code for the object INVENTORY CLASS CODE described as a numeric code used to uniquely identify an inventory type. Examples of codes and descriptions are 1 - Crops Inventory, 2 - Livestock Inventory, 3 - Purchased Inputs, 4 - Deferred Income and Receivables, 5 - Accounts Payables.'
;
COMMENT ON COLUMN agristabilty_commodity_xref.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN agristabilty_commodity_xref.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN agristabilty_commodity_xref.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN agristabilty_commodity_xref.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN agristabilty_commodity_xref.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;
COMMENT ON TABLE agristabilty_commodity_xref IS 'AGRISTABILTY COMMODITY XREF is the unique production units of livestock and crops. AGRISTABILTY COMMODITY XREF is the unit by which supplementary data is described. This object refers to codified data describing the unique types of an AGRISTABILTY COMMODITY XREF.  An AGRISTABILTY COMMODITY XREF dataset originates from provincial data imports, although it will be compiled from various sources, including federal.'
;


CREATE INDEX ix_acx_icc ON agristabilty_commodity_xref(inventory_class_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_acx_iic ON agristabilty_commodity_xref(inventory_item_code)
 TABLESPACE pg_default
;
CREATE INDEX ix_acx_igc ON agristabilty_commodity_xref(inventory_group_code)
 TABLESPACE pg_default
;
CREATE UNIQUE INDEX uk_acx_icc_iic ON agristabilty_commodity_xref(inventory_class_code, inventory_item_code)
 TABLESPACE pg_default
;

ALTER TABLE agristabilty_commodity_xref ADD 
    CONSTRAINT pk_acx PRIMARY KEY (agristabilty_commodity_xref_id) USING INDEX TABLESPACE pg_default 
;
