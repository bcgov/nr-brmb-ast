CREATE TABLE farms.farm_tip_line_items (
	tip_line_item_id bigint NOT NULL,
	line_item smallint NOT NULL,
	operating_cost_ind varchar(1) NOT NULL DEFAULT 'N',
	direct_expense_ind varchar(1) NOT NULL DEFAULT 'N',
	machinery_expense_ind varchar(1) NOT NULL DEFAULT 'N',
	land_and_building_expense_ind varchar(1) NOT NULL DEFAULT 'N',
	overhead_expense_ind varchar(1) NOT NULL DEFAULT 'N',
	program_payment_for_tips_ind varchar(1) NOT NULL DEFAULT 'N',
	other_ind varchar(1) NOT NULL DEFAULT 'N',
	tip_farm_type_1_lookup_id bigint,
	revision_count integer NOT NULL DEFAULT 1,
	who_created varchar(30) NOT NULL,
	when_created timestamp(0) NOT NULL DEFAULT statement_timestamp(),
	who_updated varchar(30),
	when_updated timestamp(0) DEFAULT statement_timestamp()
) ;
COMMENT ON TABLE farms.farm_tip_line_items IS E'TIP LINE ITEM maps a LINE ITEM to a TIP FARM TYPE 1 LOOKUP for the purposes of generating a TIP (Towards Increased Profits) report for each client.';
COMMENT ON COLUMN farms.farm_tip_line_items.direct_expense_ind IS E'DIRECT EXPENSE IND identifies if the LINE ITEM is used in the calculation of the Direct Expense Ratio on the TIP report.';
COMMENT ON COLUMN farms.farm_tip_line_items.land_and_building_expense_ind IS E'LAND AND BUILDING EXPENSE IND identifies if the LINE ITEM is used in the calculation of the Land and Building Expense Ratio on the TIP report.';
COMMENT ON COLUMN farms.farm_tip_line_items.line_item IS E'LINE ITEM is an income or expense item for Agristability.';
COMMENT ON COLUMN farms.farm_tip_line_items.machinery_expense_ind IS E'MACHINERY EXPENSE IND identifies if the LINE ITEM is used in the calculation of the Machinery Expense Ratio on the TIP report.';
COMMENT ON COLUMN farms.farm_tip_line_items.operating_cost_ind IS E'OPERATING COST IND identifies if the LINE ITEM is used in the calculation of the Operating Ratio Cost on the TIP report.';
COMMENT ON COLUMN farms.farm_tip_line_items.other_ind IS E'OTHER IND identifies if the LINE ITEM is converted to non-allowable code 9896 - Other, on TIP reports.';
COMMENT ON COLUMN farms.farm_tip_line_items.overhead_expense_ind IS E'OVERHEAD EXPENSE IND identifies if the LINE ITEM is used in the calculation of the Overhead Expense Ratio on the TIP report.';
COMMENT ON COLUMN farms.farm_tip_line_items.program_payment_for_tips_ind IS E'PROGRAM PAYMENT FOR TIPS IND identifies if the LINE ITEM is considered a program payment for the purposes of the TIP reports. This is not to be confused with PROGRAM PAYMENT IND in the LINE ITEM table which is used in the benefit calculation. In theory, both should mean the same thing but PROGRAM PAYMENT IND does not match up with the Program Payment Lists A and B in the AgriStability harmonized guide.';
COMMENT ON COLUMN farms.farm_tip_line_items.revision_count IS E'REVISION COUNT is a counter identifying the number of times this record as been modified. Used in the web page access to determine if the record as been modified since the data was first retrieved.';
COMMENT ON COLUMN farms.farm_tip_line_items.tip_farm_type_1_lookup_id IS E'TIP FARM TYPE 1 LOOKUP ID is a surrogate unique identifier for a TIP FARM TYPE 1 LOOKUP.';
COMMENT ON COLUMN farms.farm_tip_line_items.tip_line_item_id IS E'TIP LINE ITEM ID is a surrogate unique identifier for a TIP LINE ITEM.';
COMMENT ON COLUMN farms.farm_tip_line_items.when_created IS E'WHEN CREATED indicates when the physical record was created in the database.';
COMMENT ON COLUMN farms.farm_tip_line_items.when_updated IS E'WHEN UPDATED indicates when the physical record was updated in the database.';
COMMENT ON COLUMN farms.farm_tip_line_items.who_created IS E'WHO CREATED indicates the user that created the physical record in the database.';
COMMENT ON COLUMN farms.farm_tip_line_items.who_updated IS E'WHO UPDATED indicates the user that updated the physical record in the database.';
CREATE INDEX farm_tli_farm_tft1l_fk_i ON farms.farm_tip_line_items (tip_farm_type_1_lookup_id);
ALTER TABLE farms.farm_tip_line_items ADD CONSTRAINT farm_tli_uk UNIQUE (line_item);
ALTER TABLE farms.farm_tip_line_items ADD CONSTRAINT farm_tli_pk PRIMARY KEY (tip_line_item_id);
ALTER TABLE farms.farm_tip_line_items ADD CONSTRAINT farm_tli_de_chk CHECK (direct_expense_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_line_items ADD CONSTRAINT farm_tli_lbe_chk CHECK (land_and_building_expense_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_line_items ADD CONSTRAINT farm_tli_me_chk CHECK (machinery_expense_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_line_items ADD CONSTRAINT farm_tli_oc_chk CHECK (operating_cost_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_line_items ADD CONSTRAINT farm_tli_oe_chk CHECK (overhead_expense_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_line_items ADD CONSTRAINT farm_tli_o_chk CHECK (other_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_line_items ADD CONSTRAINT farm_tli_ppft_chk CHECK (program_payment_for_tips_ind in ('N', 'Y'));
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN tip_line_item_id SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN line_item SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN operating_cost_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN direct_expense_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN machinery_expense_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN land_and_building_expense_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN overhead_expense_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN program_payment_for_tips_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN other_ind SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN revision_count SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN who_created SET NOT NULL;
ALTER TABLE farms.farm_tip_line_items ALTER COLUMN when_created SET NOT NULL;
