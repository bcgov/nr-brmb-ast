create or replace function farms_codes_read_pkg.export_bpu(
    in in_program_year farms.farm_benchmark_per_units.program_year%type,
    in in_inventory_code_filter varchar,
    in in_inventory_desc_filter varchar,
    in in_municipality_desc_filter varchar
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select program_year ||
               ',' || municipality_code ||
               ',' || inventory_code ||
               ',"' || unit_comment || '"' ||
               ',"' || year_minus_6_margin || '"' ||
               ',"' || year_minus_5_margin || '"' ||
               ',"' || year_minus_4_margin || '"' ||
               ',"' || year_minus_3_margin || '"' ||
               ',"' || year_minus_2_margin || '"' ||
               ',"' || year_minus_1_margin || '"' ||
               ',"' || year_minus_6_expense || '"' ||
               ',"' || year_minus_5_expense || '"' ||
               ',"' || year_minus_4_expense || '"' ||
               ',"' || year_minus_3_expense || '"' ||
               ',"' || year_minus_2_expense || '"' ||
               ',"' || year_minus_1_expense || '"' bpu_export
        from (
            select bpus.program_year,
                   bpus.municipality_code,
                   coalesce(bpus.structure_group_code, bpus.inventory_item_code) inventory_code,
                   bpus.unit_comment,
                   max(case when yrs.benchmark_year = (bpus.program_year - 6) then yrs.average_margin else null end) year_minus_6_margin,
                   max(case when yrs.benchmark_year = (bpus.program_year - 5) then yrs.average_margin else null end) year_minus_5_margin,
                   max(case when yrs.benchmark_year = (bpus.program_year - 4) then yrs.average_margin else null end) year_minus_4_margin,
                   max(case when yrs.benchmark_year = (bpus.program_year - 3) then yrs.average_margin else null end) year_minus_3_margin,
                   max(case when yrs.benchmark_year = (bpus.program_year - 2) then yrs.average_margin else null end) year_minus_2_margin,
                   max(case when yrs.benchmark_year = (bpus.program_year - 1) then yrs.average_margin else null end) year_minus_1_margin,
                   max(case when yrs.benchmark_year = (bpus.program_year - 6) then yrs.average_expense else null end) year_minus_6_expense,
                   max(case when yrs.benchmark_year = (bpus.program_year - 5) then yrs.average_expense else null end) year_minus_5_expense,
                   max(case when yrs.benchmark_year = (bpus.program_year - 4) then yrs.average_expense else null end) year_minus_4_expense,
                   max(case when yrs.benchmark_year = (bpus.program_year - 3) then yrs.average_expense else null end) year_minus_3_expense,
                   max(case when yrs.benchmark_year = (bpus.program_year - 2) then yrs.average_expense else null end) year_minus_2_expense,
                   max(case when yrs.benchmark_year = (bpus.program_year - 1) then yrs.average_expense else null end) year_minus_1_expense
            from farms.farm_benchmark_per_units bpus
            join farms.farm_benchmark_years yrs on yrs.benchmark_per_unit_id = bpus.benchmark_per_unit_id
            join farms.farm_municipality_codes mc on mc.municipality_code = bpus.municipality_code
            left join farms.farm_inventory_item_codes iic on iic.inventory_item_code = bpus.inventory_item_code
            left join farms.farm_structure_group_codes sgc on sgc.structure_group_code = bpus.structure_group_code
            where bpus.expiry_date is null
            and bpus.program_year = in_program_year
            and (
                (in_inventory_code_filter is null and in_inventory_desc_filter is null) or
                (in_inventory_code_filter is not null and (
                    bpus.inventory_item_code like in_inventory_code_filter || '%' or
                    bpus.structure_group_code like in_inventory_code_filter || '%'
                )) or
                (in_inventory_desc_filter is not null and (
                    lower(iic.description) like lower(in_inventory_desc_filter || '%') or
                    lower(sgc.description) like lower(in_inventory_desc_filter || '%')
                ))
            )
            and (
                in_municipality_desc_filter is null or
                (in_municipality_desc_filter is not null and lower(mc.description) like lower(in_municipality_desc_filter || '%'))
            )
            group by bpus.program_year,
                     bpus.unit_comment,
                     coalesce(bpus.structure_group_code, bpus.inventory_item_code),
                     bpus.municipality_code
        ) t
        order by to_number(inventory_code),
                 to_number(municipality_code);
    return cur;
end;
$$;
