create or replace procedure farms_codes_write_pkg.update_expected_production_value(
   in in_expected_production_id farms.expected_production.expected_production_id%type,
   in in_expected_production_value farms.expected_production.expected_production_per_productive_unit%type,
   in in_farm_user farms.expected_production.update_user%type
)
language plpgsql
as $$
begin

    update farms.expected_production c
    set c.expected_production_per_productive_unit = in_expected_production_value,
        c.update_user = in_farm_user,
        c.update_date = current_timestamp,
        c.revision_count = c.revision_count + 1
    where c.expected_production_id = in_expected_production_id;

end;
$$;
