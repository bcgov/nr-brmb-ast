create or replace function farms_export_pkg.f20(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select max(ac.participant_pin)participant_pin,
               max(py.year) program_year,
               max(pyv.municipality_code) municipality_code,
               (
                   case
                       when max(pi.production_insurance_id) is not null
                            and (max(pi.production_insurance_id))::text <> '' then 'Y'
                       else 'N'
                   end
               ) production_insurance_ind,
               max(pyv.combined_farm_ind) combined_farm_ind
        from farms.farm_agristability_clients ac
        join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        join(
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
        join farms.farm_farming_operations op on op.program_year_version_id = t.program_year_version_id
        left outer join farms.farm_production_insurances pi on op.farming_operation_id = pi.farming_operation_id
        where py.non_participant_ind = 'N'
        group by pyv.program_year_version_id
        order by participant_pin;

    return cur;
end;
$$;
