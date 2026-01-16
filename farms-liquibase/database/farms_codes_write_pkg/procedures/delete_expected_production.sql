create or replace procedure farms_codes_write_pkg.delete_expected_production(
   in in_expected_production_id farms.farm_expected_productions.expected_production_id%type
)
language plpgsql
as $$
begin

    delete from farms.farm_expected_productions c
    where c.expected_production_id = in_expected_production_id;

end;
$$;
