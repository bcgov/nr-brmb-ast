create or replace procedure farms_codes_write_pkg.create_bpu(
   out out_benchmark_per_unit_id farms.farm_benchmark_per_units.benchmark_per_unit_id%type,
   in in_program_year farms.farm_benchmark_per_units.program_year%type,
   in in_inv_or_sg_code varchar,
   in in_municipality_code farms.farm_benchmark_per_units.municipality_code%type,
   in in_user farms.farm_benchmark_per_units.who_created%type
)
language plpgsql
as $$
declare
    v_inventory_item_code farms.farm_benchmark_per_units.inventory_item_code%type;
    v_structure_group_code farms.farm_benchmark_per_units.structure_group_code%type;
begin
    --
    -- structure_group_codes trump inventory_item_codes
    --
    if farms_codes_write_pkg.is_structure_group_code(in_inv_or_sg_code) then
        v_inventory_item_code := null;
        v_structure_group_code := in_inv_or_sg_code;
    else
        v_inventory_item_code := in_inv_or_sg_code;
        v_structure_group_code := null;
    end if;

    insert into farms.farm_benchmark_per_units (
        benchmark_per_unit_id,
        program_year,
        municipality_code,
        inventory_item_code,
        structure_group_code,
        revision_count,
        who_created,
        when_created
    ) values (
        nextval('farms.farm_bpu_seq'),
        in_program_year,
        in_municipality_code,
        v_inventory_item_code,
        v_structure_group_code,
        1,
        in_user,
        current_timestamp
    ) returning benchmark_per_unit_id into out_benchmark_per_unit_id;
end;
$$;
