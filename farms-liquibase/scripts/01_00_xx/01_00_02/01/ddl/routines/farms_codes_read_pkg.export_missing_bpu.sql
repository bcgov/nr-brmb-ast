create or replace function farms_codes_read_pkg.export_missing_bpu(
    in in_program_year farms.benchmark_per_unit.program_year%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        with pucs as (
            select m.municipality_code,
                   puc.structure_group_code,
                   puc.inventory_item_code,
                   m.year ref_year,
                   count(distinct m.participant_pin) affected_pin_count
            from farms.operations_vw m
            join farms.productive_unit_capacity puc on puc.farming_operation_id = m.farming_operation_id
            where (puc.inventory_item_code is null or puc.inventory_item_code != '-1')
            and (puc.structure_group_code is null or puc.structure_group_code != '-1')
            and m.municipality_code not in ('-1', '0')
            and puc.productive_capacity_amount != 0
            and m.year between in_program_year - 5 and in_program_year - 1
            group by m.municipality_code,
                     puc.inventory_item_code,
                     puc.structure_group_code,
                     m.year
        )
        select in_program_year ||
               ',' || coalesce(pucs.structure_group_code, pucs.inventory_item_code) ||
               ',"' || (case when pucs.structure_group_code is not null then sgc.description else iic.description end) || '"' ||
               ',' || pucs.municipality_code ||
               ',' || pucs.ref_year ||
               ',' || pucs.affected_pin_count missing_bpu_export
        from pucs
        left join farms.structure_group_code sgc on sgc.structure_group_code = pucs.structure_group_code
        left join farms.inventory_item_code iic on iic.inventory_item_code = pucs.inventory_item_code
        where not exists (
            select null
            from farms.benchmark_per_unit bpu
            join farms.benchmark_year y on y.benchmark_per_unit_id = bpu.benchmark_per_unit_id
            where bpu.program_year = in_program_year
            and (bpu.inventory_item_code = pucs.inventory_item_code or bpu.structure_group_code = pucs.structure_group_code)
            and (bpu.municipality_code = pucs.municipality_code or bpu.municipality_code = '0')
            and y.benchmark_year = pucs.ref_year
            and y.average_margin != 0
            and (y.average_expense != 0 or in_program_year < 2013)
            and bpu.expiry_date is null
        )
        order by coalesce(pucs.structure_group_code, pucs.inventory_item_code),
                 pucs.municipality_code,
                 pucs.ref_year;

    return cur;
end;
$$;
