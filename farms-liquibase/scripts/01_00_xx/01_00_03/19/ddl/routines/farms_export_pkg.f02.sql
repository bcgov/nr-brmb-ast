create or replace function farms_export_pkg.f02(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        with ranked as (
            /* this subquery is duplicated in F01, F02, F03, F20, F21, F30, F31, F40, F60.
             * if it is modified here, it should be modified there as well.
             */
            select m.program_year_version_id,
                   sc.agristability_scenario_id,
                   m.program_year_id,
                   m.participant_pin,
                   m.year program_year,
                   sc.combined_farm_number,
                   ssa.scenario_state_audit_id,
                   dense_rank() over(
                       partition by m.program_year_id
                       order by case when(sc.scenario_state_code = 'COMP') then 0 else 1 end,
                                case when(sc.scenario_state_code = 'AMEND') then 0 else 1 end,
                                case when(sc.scenario_class_code in ('CRA','LOCAL')) then 0 else 1 end,
                                case when(sc.scenario_category_code in ('FIN','AADJ','PADJ')) then 0 else 1 end,
                                case when(sc.scenario_category_code = 'INT') then 0 else 1 end,
                                case
                                    when sc.scenario_state_code in ('COMP', 'AMEND')
                                         and sc.scenario_class_code = 'USER'
                                         and m.year = in_program_year then 3
                                    when sc.scenario_state_code in ('COMP', 'AMEND')
                                         and sc.scenario_class_code = 'REF'
                                         and m_parent.year = in_program_year then 2
                                    when sc.scenario_class_code in ('CRA','LOCAL') then 1
                                    else 0
                                end desc,
                                ssa.when_created desc,
                                sc.scenario_number desc
                   ) as rnk,
                   first_value(ssa.scenario_state_audit_id) over (
                       partition by sc.agristability_scenario_id
                       order by ssa.when_created desc nulls last
                   ) verified_state_id,
                   first_value(py.non_participant_ind) over (
                       partition by m.agristability_client_id
                       order by m.year desc
                   ) non_participant_ind
            from farms.farm_agri_scenarios_vw m
            join farms.farm_agristability_scenarios sc on m.agristability_scenario_id = sc.agristability_scenario_id
            join farms.farm_program_years py on py.program_year_id = m.program_year_id
            left outer join farms.farm_reference_scenarios rf on rf.agristability_scenario_id = sc.agristability_scenario_id
            left outer join farms.farm_agri_scenarios_vw m_parent on m_parent.agristability_scenario_id = rf.for_agristability_scenario_id
            left outer join farms.farm_scenario_state_audits ssa on ssa.agristability_scenario_id = sc.agristability_scenario_id
                                                           and ssa.scenario_state_code = 'COMP'
            where m.year between(in_program_year - 5) and in_program_year
        ), chosen as (
            select distinct on (program_year_id)
                   program_year_id,
                   min(agristability_scenario_id) as latest_sc_id
            from ranked
            where rnk = 1
            group by program_year_id
        )
        select ac.participant_pin,
               in_program_year "Year",
               py.year prior_year,
               fos.operation_number,
               fos.partnership_pin,
               fos.partnership_percent,
               fos.fiscal_year_start,
               fos.fiscal_year_end,
               fos.federal_accounting_code,
               fos.landlord_ind,
               fos.crop_share_ind,
               fos.feeder_member_ind,
               fos.gross_income,
               fos.expenses,
               fos.net_income_before_adj,
               fos.other_deductions,
               fos.inventory_adjustments,
               fos.net_income_after_adj,
               fos.business_use_home_expense,
               fos.net_farm_income
        from farms.farm_agristability_clients ac
        join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        join farms.farm_farming_operations fos on pyv.program_year_version_id = fos.program_year_version_id
        join (
            select r.*
            from ranked r
            join chosen c on r.program_year_id = c.program_year_id
                          and r.agristability_scenario_id = c.latest_sc_id
        ) t on pyv.program_year_version_id = t.program_year_version_id
            and (t.scenario_state_audit_id = t.verified_state_id or coalesce(t.verified_state_id::text, '') = '')
            and t.non_participant_ind = 'N'
        order by ac.participant_pin,
                 prior_year,
                 fos.operation_number;

    return cur;
end;
$$;
