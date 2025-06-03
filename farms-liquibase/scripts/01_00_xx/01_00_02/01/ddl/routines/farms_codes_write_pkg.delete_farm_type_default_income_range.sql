create or replace procedure farms_codes_write_pkg.delete_farm_type_default_income_range()
language plpgsql
as $$
begin

    delete from farms.tip_income_range
    where tip_farm_type_3_lookup_id is null
    and tip_farm_type_2_lookup_id is null
    and tip_farm_type_1_lookup_id is null;

end;
$$;
