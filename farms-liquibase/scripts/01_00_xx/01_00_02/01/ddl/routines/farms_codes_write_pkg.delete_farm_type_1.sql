create or replace procedure farms_codes_write_pkg.delete_farm_type_1(
   in in_farm_type_id farms.tip_farm_type_1_lookup.tip_farm_type_1_lookup_id%type
)
language plpgsql
as $$
begin

    delete from farms.tip_income_range tir
    where tir.tip_farm_type_1_lookup_id = in_farm_type_id;

    delete from farms.tip_farm_type_1_lookup c
    where c.tip_farm_type_1_lookup_id = in_farm_type_id;

end;
$$;
