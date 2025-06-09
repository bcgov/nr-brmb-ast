create or replace procedure farms_codes_write_pkg.delete_farm_type_2(
   in in_farm_type_id farms.tip_farm_type_2_lookup.tip_farm_type_2_lookup_id%type
)
language plpgsql
as $$
begin

    delete from farms.tip_income_range tir
    where tir.tip_farm_type_2_lookup_id = in_farm_type_id;

    delete from farms.tip_farm_type_2_lookup c
    where c.tip_farm_type_2_lookup_id = in_farm_type_id;

end;
$$;
