create or replace procedure farms_codes_write_pkg.delete_farm_type_1(
   in in_farm_type_id farms.farm_tip_farm_type_1_lookups.tip_farm_type_1_lookup_id%type
)
language plpgsql
as $$
begin

    delete from farms.farm_tip_income_ranges tir
    where tir.tip_farm_type_1_lookup_id = in_farm_type_id;

    delete from farms.farm_tip_farm_type_1_lookups c
    where c.tip_farm_type_1_lookup_id = in_farm_type_id;

end;
$$;
