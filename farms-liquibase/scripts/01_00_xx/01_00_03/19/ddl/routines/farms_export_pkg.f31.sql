create or replace function farms_export_pkg.f31(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select ac.participant_pin,
               py.year program_year,
               sc.scenario_number,
               py2.year prior_year,
               ttls.unadjusted_production_margin,
               case
                   when cl.structural_change_code != 'NONE' and ttls.structural_change_notable_ind = 'Y' then ttls.structural_change_adjs
                   else 0
               end structural_change_adjs,
               ttls.production_marg_aft_str_changs,
               ttls.production_marg_accr_adjs,
               ttls.expense_accr_adjs total_eligible_expense,
               case
                   when cl.expense_structural_change_code != 'NONE' and ttls.structural_change_notable_ind = 'Y' then ttls.expense_structural_chg_adjs
                   else 0
               end expense_structural_chg_adjs, -- ratio/additive method expense structural adjustment
               case
                   when cl.adjusted_reference_margin > cl.reference_margin_limit then 1
                   else 0
               end lrm_indicator
        from farms.farm_agristability_clients ac
        join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        join (
            /* this subquery is duplicated in F01, F02, F03, F20, F21, F30, F31, F40, F60.
             * if it is modified here, it should be modified there as well.
             */
            select m.program_year_version_id,
                   sc.agristability_scenario_id,
                   m.participant_pin,
                   m.year program_year,
                   sc.combined_farm_number,
                   ssa.scenario_state_audit_id,
                   min(sc.agristability_scenario_id) keep(dense_rank first order by
                       case when(sc.scenario_state_code = 'COMP') then 0 else 1 end,
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
                   ) over (partition by m.program_year_id) latest_sc_id,
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
            where m.year = in_program_year
        ) t on pyv.program_year_version_id = t.program_year_version_id
            and t.agristability_scenario_id = t.latest_sc_id
            and (t.scenario_state_audit_id = t.verified_state_id or coalesce(t.verified_state_id::text, '') = '')
        join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = t.agristability_scenario_id
        join farms.farm_agristability_claims cl on cl.agristability_scenario_id = sc.agristability_scenario_id
        join farms.farm_reference_scenarios rf on sc.agristability_scenario_id = rf.for_agristability_scenario_id
        join farms.farm_agristability_scenarios sc2 on rf.agristability_scenario_id = sc2.agristability_scenario_id
        join farms.farm_program_year_versions pyv2 on sc2.program_year_version_id = pyv2.program_year_version_id
        join farms.farm_program_years py2 on py2.program_year_id = pyv2.program_year_id
        join farms.farm_benefit_calc_totals ttls on ttls.agristability_scenario_id = rf.agristability_scenario_id
        where t.non_participant_ind = 'N'
        and py.year = in_program_year
        order by ac.participant_pin,
                 prior_year;

    return cur;
end;
$$;
