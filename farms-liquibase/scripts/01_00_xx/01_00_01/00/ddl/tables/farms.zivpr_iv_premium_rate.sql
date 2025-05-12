CREATE TABLE farms.zivpr_iv_premium_rate(
    line_number            numeric(10, 0)    NOT NULL,
    program_year           numeric(4, 0)     NOT NULL,
    inventory_item_code    varchar(10)       NOT NULL,
    insurable_value        numeric(13, 3)    NOT NULL,
    premium_rate           numeric(13, 4)    NOT NULL,
    revision_count         numeric(5, 0)     DEFAULT 1 NOT NULL,
    create_user            varchar(30)       NOT NULL,
    create_date            timestamp(6)      DEFAULT CURRENT_TIMESTAMP NOT NULL,
    update_user            varchar(30),
    update_date            timestamp(6)      DEFAULT CURRENT_TIMESTAMP
) TABLESPACE pg_default
;



COMMENT ON COLUMN farms.zivpr_iv_premium_rate.line_number IS 'LINE NUMBER is the number of a line in a CSV file. Everytime an IVPR CSV file is loaded, all the IVPR staging data will be deleted. Also used for generating errors to the user about data problems in the input file.'
;
COMMENT ON COLUMN farms.zivpr_iv_premium_rate.program_year IS 'PROGRAM YEAR is the PROGRAM YEAR this data pertains to.'
;
COMMENT ON COLUMN farms.zivpr_iv_premium_rate.inventory_item_code IS 'INVENTORY ITEM CODE is a unique code for the object INVENTORY ITEM CODE described as a numeric code used to uniquely identify an inventory item. Examples of codes and descriptions are 2 - Canadian Wheat Board Payments, 3 - Barley (seed), 4 - Beans (Dry Edible).'
;
COMMENT ON COLUMN farms.zivpr_iv_premium_rate.insurable_value IS 'INSURABLE VALUE is the dollar amount of insurance per unit that the AgriStability program requires the participant to carry.'
;
COMMENT ON COLUMN farms.zivpr_iv_premium_rate.premium_rate IS 'PREMIUM RATE is the main portion of the premium the participant pays for insurance of their production it is the percentage of the total dollar value they have insured for this INVENTORY ITEM CODE.'
;
COMMENT ON COLUMN farms.zivpr_iv_premium_rate.revision_count IS 'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.'
;
COMMENT ON COLUMN farms.zivpr_iv_premium_rate.create_user IS 'CREATE USER indicates the user that created the physical record in the database.'
;
COMMENT ON COLUMN farms.zivpr_iv_premium_rate.create_date IS 'CREATE DATE indicates when the physical record was created in the database.'
;
COMMENT ON COLUMN farms.zivpr_iv_premium_rate.update_user IS 'UPDATE USER indicates the user that updated the physical record in the database.'
;
COMMENT ON COLUMN farms.zivpr_iv_premium_rate.update_date IS 'UPDATE DATE indicates when the physical record was updated in the database.'
;

