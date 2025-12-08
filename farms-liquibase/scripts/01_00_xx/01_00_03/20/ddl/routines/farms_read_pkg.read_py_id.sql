create or replace function farms_read_pkg.read_py_id(
    in ppin farms.farm_agristability_clients.participant_pin%type,
    in pyear farms.farm_program_years.year%type,
    in sc_num numeric,
    in alg varchar
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin

    open cur for
        select x.year program_year,
               coalesce(
                   max(case when grp='MAIN' then program_year_id end),
                   max(case when grp='REF' then program_year_id end),
                   max(case when grp='DEFAULT' then program_year_id end)
               ) program_year_id,
               coalesce(
                   max(case when grp='MAIN' then program_year_version_id end),
                   max(case when grp='REF' then program_year_version_id end),
                   max(case when grp='DEFAULT' then program_year_version_id end)
               ) program_year_version_id,
               coalesce(
                   max(case when grp='MAIN' then agristability_scenario_id end),
                   max(case when grp='REF' then agristability_scenario_id end),
                   max(case when grp='DEFAULT' then agristability_scenario_id end)
               ) agristability_scenario_id
        from (
            select cur.program_year_id,
                   cur.year,
                   cur.program_year_version_id,
                   cur.agristability_scenario_id,
                   'MAIN' as grp
            from (
                select py.program_year_id,
                       pyv.program_year_version_id,
                       sc.agristability_scenario_id,
                       py.year,
                       first_value(sc.agristability_scenario_id) over (order by
                           case when (sc_num is null or sc_num = sc.scenario_number) then 0 else 1 end,
                           case when (sc.default_ind = 'Y') then 0 else 1 end,
                           case when sc.scenario_class_code != 'REF' then 0 else 1 end,
                           sc.scenario_number desc
                       ) sc_id_def,
                       first_value(sc.agristability_scenario_id) over (order by
                           case when (sc_num is null or sc_num = sc.scenario_number) then 0 else 1 end,
                           case when (sc.scenario_state_code in ('COMP','AMEND') and sc.scenario_class_code = 'USER') then 0 else 1 end,
                           case when (sc.default_ind = 'Y') then 0 else 1 end,
                           case when sc.scenario_class_code != 'REF' then 0 else 1 end,
                           sc.scenario_number desc
                       ) sc_id_app,
                       first_value(sc.agristability_scenario_id) over (order by
                           case when (sc_num is null or sc_num = sc.scenario_number) then 0 else 1 end,
                           case when (sc.scenario_category_code = 'ENW' and sc.scenario_state_code in ('IP','EN_COMP') and sc.scenario_class_code = 'USER') then 0 else 1 end,
                           case when (sc.scenario_category_code in ('FIN','PADJ','AADJ') and sc.scenario_state_code in ('COMP','AMEND') and sc.scenario_class_code = 'USER') then 0 else 1 end,
                           case when sc.scenario_class_code in ('CRA','LOCAL','CHEF') then 0 else 1 end,
                           case when sc.scenario_class_code = 'GEN' then 0 else 1 end,
                           case when sc.default_ind = 'Y' then 0 else 1 end,
                           sc.scenario_number desc
                       ) sc_id_enrol
                from farms.farm_program_years py
                join farms.farm_agristability_clients ac on py.agristability_client_id = ac.agristability_client_id
                join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
                join farms.farm_agristability_scenarios sc on pyv.program_year_version_id = sc.program_year_version_id
                where ac.participant_pin = ppin
                and py.year = pyear
            ) cur
            where (
                (alg = 'DEF' and cur.agristability_scenario_id = cur.sc_id_def) or
                (alg = 'COMP' and cur.agristability_scenario_id = cur.sc_id_app) or
                (alg = 'ENROL' and cur.agristability_scenario_id = cur.sc_id_enrol)
            )
            union all
            select py2.program_year_id,
                   py2.year,
                   sc2.program_year_version_id,
                   ref.agristability_scenario_id ref_sc_id,
                   'REF' as grp
            from (
                select py.program_year_id,
                       pyv.program_year_version_id,
                       sc.agristability_scenario_id,
                       py.year,
                       first_value(sc.agristability_scenario_id) over (order by
                           case when (sc_num is null or sc_num = sc.scenario_number) then 0 else 1 end,
                           case when (sc.default_ind = 'Y') then 0 else 1 end,
                           case when sc.scenario_class_code != 'REF' then 0 else 1 end,
                           sc.scenario_number desc
                       ) sc_id_def,
                       first_value(sc.agristability_scenario_id) over (order by
                           case when (sc_num is null or sc_num = sc.scenario_number) then 0 else 1 end,
                           case when (sc.scenario_state_code = 'COMP' and sc.scenario_class_code = 'USER') then 0 else 1 end,
                           case when (sc.default_ind = 'Y') then 0 else 1 end,
                           case when sc.scenario_class_code != 'REF' then 0 else 1 end,
                           sc.scenario_number desc
                       ) sc_id_app,
                       first_value(sc.agristability_scenario_id) over (order by
                           case when (sc_num is null or sc_num = sc.scenario_number) then 0 else 1 end,
                           case when (sc.scenario_category_code = 'ENW' and sc.scenario_state_code in ('IP','EN_COMP') and sc.scenario_class_code = 'USER') then 0 else 1 end,
                           case when (sc.scenario_category_code in ('FIN','PADJ','AADJ') and sc.scenario_state_code in ('COMP','AMEND') and sc.scenario_class_code = 'USER') then 0 else 1 end,
                           case when sc.scenario_class_code in ('CRA','LOCAL','CHEF') then 0 else 1 end,
                           case when sc.scenario_class_code = 'GEN' then 0 else 1 end,
                           case when sc.default_ind = 'Y' then 0 else 1 end,
                           sc.scenario_number desc
                       ) sc_id_enrol
                from farms.farm_program_years py
                join farms.farm_agristability_clients ac on py.agristability_client_id = ac.agristability_client_id
                join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
                join farms.farm_agristability_scenarios sc on pyv.program_year_version_id = sc.program_year_version_id
                where ac.participant_pin = ppin
                and py.year = pyear
            ) cur
            join farms.farm_reference_scenarios ref on ref.for_agristability_scenario_id = cur.agristability_scenario_id
            join farms.farm_agristability_scenarios sc2 on ref.agristability_scenario_id = sc2.agristability_scenario_id
            join farms.farm_program_year_versions pyv2 on pyv2.program_year_version_id = sc2.program_year_version_id
            join farms.farm_program_years py2 on pyv2.program_year_id = py2.program_year_id
            where (
                (alg = 'DEF' and cur.agristability_scenario_id = cur.sc_id_def) or
                (alg = 'COMP' and cur.agristability_scenario_id = cur.sc_id_app) or
                (alg = 'ENROL' and cur.agristability_scenario_id = cur.sc_id_enrol)
            )
            union all
            -- latest for ref years
            select t.program_year_id,
                   t.year,
                   t.program_year_version_id,
                   t.agristability_scenario_id,
                   'DEFAULT' as grp
            from (
                select py.program_year_id,
                       pyv.program_year_version_id,
                       sc.agristability_scenario_id,
                       py.year,
                       -- Get most recent Verified (USER or REF) scenario (by Verified date).
                       -- If no verified, get most recent CRA or GEN scenario.
                       -- Will never get a USER/REF scenario that is not Verified.
                       -- sc_Id_def and sc_Id_app logic are the same for this part of the query
                       first_value(sc.agristability_scenario_id) over (partition by py.program_year_id order by
                           case when sc.scenario_state_code = 'COMP' then 0 else 1 end,
                           coalesce(comp.when_created, pcomp.when_created) desc,
                           case when (sc.scenario_class_code in ('CRA','GEN','LOCAL','CHEF') or psc.scenario_class_code in ('CRA','GEN','LOCAL','CHEF')) then 0 else 1 end,
                           pyv.program_year_version_number desc
                       ) sc_id_def,
                       first_value(sc.agristability_scenario_id) over (partition by py.program_year_id order by
                           case when sc.scenario_state_code = 'COMP' then 0 else 1 end,
                           coalesce(comp.when_created, pcomp.when_created) desc,
                           case when (sc.scenario_class_code in ('CRA','GEN','LOCAL','CHEF') or psc.scenario_class_code in ('CRA','GEN','LOCAL','CHEF')) then 0 else 1 end,
                           pyv.program_year_version_number desc
                       ) sc_id_app,
                       first_value(sc.agristability_scenario_id) over (partition by py.program_year_id order by
                           case when sc.scenario_category_code in ('FIN','PADJ','AADJ') and sc.scenario_state_code in ('COMP','AMEND') and sc.scenario_class_code in ('USER','REF') then 0 else 1 end,
                           coalesce(comp.when_created, pcomp.when_created) desc,
                           case when (sc.scenario_class_code in ('CRA','GEN','LOCAL','CHEF') or psc.scenario_class_code in ('CRA','GEN','LOCAL','CHEF')) then 0 else 1 end,
                           pyv.program_year_version_number desc
                       ) sc_id_enrol
                from farms.farm_program_years py
                join farms.farm_agristability_clients ac on py.agristability_client_id = ac.agristability_client_id
                join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
                join farms.farm_agristability_scenarios sc on pyv.program_year_version_id = sc.program_year_version_id
                left outer join farms.farm_reference_scenarios rs on rs.agristability_scenario_id = sc.agristability_scenario_id
                left outer join farms.farm_agristability_scenarios psc on psc.agristability_scenario_id = rs.for_agristability_scenario_id
                left outer join farms.farm_scenario_state_audits comp on comp.agristability_scenario_id = sc.agristability_scenario_id
                                                                and comp.scenario_state_code = 'COMP'
                left outer join farms.farm_scenario_state_audits pcomp on pcomp.agristability_scenario_id = psc.agristability_scenario_id
                                                                and pcomp.scenario_state_code = 'COMP'
                where ac.participant_pin = ppin
                and py.year between (pyear - 5) and (pyear - 1)
            ) t
            where (
                (alg = 'DEF' and t.agristability_scenario_id = t.sc_id_def) or
                (alg = 'COMP' and t.agristability_scenario_id = t.sc_id_app) or
                (alg = 'ENROL' and t.agristability_scenario_id = t.sc_id_enrol)
            )
        ) x
        group by x.year
        order by x.year desc;

    return cur;

end;
$$;
