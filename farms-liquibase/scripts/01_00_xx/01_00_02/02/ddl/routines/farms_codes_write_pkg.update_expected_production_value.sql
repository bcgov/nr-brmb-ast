create or replace procedure farms_codes_write_pkg.update_expected_production_value(
   in in_expected_production_id farms.farm_expected_productions.expected_production_id%type,
   in in_expected_production_value farms.farm_expected_productions.expected_prodctn_per_prod_unit%type,
   in in_farm_user farms.farm_expected_productions.who_updated%type
)
language plpgsql
as $$
begin

    update farms.farm_expected_productions c
    set c.expected_prodctn_per_prod_unit = in_expected_production_value,
        c.who_updated = in_farm_user,
        c.when_updated = current_timestamp,
        c.revision_count = c.revision_count + 1
    where c.expected_production_id = in_expected_production_id;

end;
$$;
