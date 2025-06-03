create or replace procedure farms_codes_write_pkg.delete_expected_production(
   in in_expected_production_id farms.expected_production.expected_production_id%type
)
language plpgsql
as $$
begin

    delete from farms.expected_production c
    where c.expected_production_id = in_expected_production_id;

end;
$$;
