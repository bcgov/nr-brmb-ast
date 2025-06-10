create or replace function farms_import_pkg.supplemental_date(
    in in_program_year_version_id farms.program_year_version.program_year_version_id%type
)
returns varchar
language plpgsql
as $$
declare
    v_post_mark_date farms.program_year_version.post_mark_date%type;
    v_received_date farms.program_year_version.received_date%type;
    v_cra_income_expense_received_date farms.agristability_scenario.cra_income_expense_received_date%type;
    v_cra_supplemental_received_date farms.agristability_scenario.cra_supplemental_received_date%type;
begin

    select pyv.post_mark_date,
           pyv.received_date
    into v_post_mark_date,
         v_received_date
    from farms.program_year_version pyv
    where pyv.program_year_version_id = in_program_year_version_id;

    select first_value(pyv_all.post_mark_date) over (
        order by pyv_all.program_year_version_number
        rows between unbounded preceding and unbounded following
    ) cra_income_expense_received_date
    into v_cra_income_expense_received_date
    from farms.program_year_version pyv_cur
    join farms.program_year_version pyv_all on pyv_all.program_year_id = pyv_cur.program_year_id
    join farms.agristability_scenario sc on sc.program_year_version_id = pyv_all.program_year_version_id
    where pyv_cur.program_year_version_id = in_program_year_version_id
    and sc.scenario_class_code = 'CRA'
    and pyv_all.post_mark_date is not null
    limit 1;

    select (case
               when cur_pyv_has_suppl_data = 'Y' then cra_suppl_received_date
           end) cra_suppl_received_date
    into v_cra_supplemental_received_date
    from (
        select min(case
                   when pyv_all.program_year_version_id = pyv_cur.program_year_version_id then 'Y'
               end) over (
                   order by pyv_all.program_year_version_number
                   rows between unbounded preceding and unbounded following
               ) cur_pyv_has_suppl_data,
               first_value(pyv_all.post_mark_date) over (
                   order by pyv_all.program_year_version_number
                   rows between unbounded preceding and unbounded following
               ) cra_suppl_received_date
        from farms.program_year_version pyv_cur
        join farms.program_year_version pyv_all on pyv_all.program_year_id = pyv_cur.program_year_id
        join farms.agristability_scenario sc on sc.program_year_version_id = pyv_all.program_year_version_id
        where pyv_cur.program_year_version_id = in_program_year_version_id
        and sc.scenario_class_code = 'CRA'
        and pyv_all.post_mark_date is not null
        and (
            exists (
                select *
                from farms.farming_operation fo
                join farms.reported_inventory ri on ri.farming_operation_id = fo.farming_operation_id
                join farms.agristabilty_commodity_xref x on x.agristabilty_commodity_xref_id = ri.agristabilty_commodity_xref_id
                where fo.program_year_version_id = pyv_all.program_year_version_id
                and ri.agristability_scenario_id is null
                and (
                    -- crops
                    (x.inventory_class_code = '1' and (
                        ri.quantity_end is not null or
                        ri.price_end is null or
                        ri.on_farm_acres is not null or
                        ri.unseedable_acres is not null or
                        ri.quantity_produced is not null
                    )) or
                    -- livestock
                    (x.inventory_class_code = '2' and (
                        ri.quantity_end is not null or
                        ri.price_end is not null
                    )) or
                    -- accruals
                    (x.inventory_class_code in ('3', '4', '5') and
                        ri.end_of_year_amount is not null
                    )
                )
            ) or exists (
                select *
                from farm.farming_operation fo
                join farms.productive_unit_capacity puc on puc.farming_operation_id = fo.farming_operation_id
                where fo.program_year_version_id = pyv_all.program_year_version_id
                and puc.agristability_scenario_id is null
                and puc.productive_capacity_amount is not null
            )
        )
        limit 1
    ) t;

    if v_post_mark_date is null or v_received_date is null then
        return 'Must include both post mark date and file received date';
    end if;

    update farms.agristability_scenario s
    set s.cra_income_expense_received_date = v_cra_income_expense_received_date,
        s.cra_supplemental_received_date = v_cra_supplemental_received_date
    where s.program_year_version_id = in_program_year_version_id;

    return null;
exception
    when others then
        return farms_import_pkg.scrub(farms_error_pkg.codify_scenario(
            sqlerrm,
            in_program_year_version_id
        ));
end;
$$;
