create or replace procedure farms_codes_write_pkg.create_bpu(
   out out_benchmark_per_unit_id farms.benchmark_per_unit.benchmark_per_unit_id%type,
   in in_program_year farms.benchmark_per_unit.program_year%type,
   in in_inv_or_sg_code varchar,
   in in_municipality_code farms.benchmark_per_unit.municipality_code%type,
   in in_user farms.benchmark_per_unit.create_user%type
)
language plpgsql
as $$
declare
    v_inventory_item_code farms.benchmark_per_unit.inventory_item_code%type;
    v_structure_group_code farms.benchmark_per_unit.structure_group_code%type;
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

    insert into farms.benchmark_per_unit (
        benchmark_per_unit_id,
        program_year,
        municipality_code,
        inventory_item_code,
        structure_group_code,
        revision_count,
        create_user,
        create_date
    ) values (
        nextval('farms.seq_bpu'),
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
