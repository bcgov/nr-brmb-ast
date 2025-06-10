create or replace function farms_import_pkg.is_claim_changed(
    in in_program_year_id farms.program_year.program_year_id%type,
    in in_program_year_vrsn_prev_id farms.program_year_version.program_year_version_id%type
)
returns boolean
language plpgsql
as $$
declare
    v_scenario_id numeric := null;
    cnt numeric := null;
begin
    -- find previous scenario id
    select first_value(s.agristability_scenario_id) over (order by s.scenario_number desc) s_id
    into v_scenario_id
    from farms.agristability_scenario s
    where s.scenario_class_code = 'CRA'
    and s.program_year_version_id = in_program_year_vrsn_prev_id;

    -- check Claim
    select count(*) cnt
    into cnt
    from (
        select *
        from (
            select clm.unadjusted_reference_margin,
                   clm.adjusted_reference_margin,
                   clm.program_year_margin,
                   clm.federal_contributions,
                   clm.provincial_contributions,
                   clm.interim_contributions,
                   clm.total_benefit,
                   clm.producer_share
            from farms.agristability_client ac
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
            join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            join farms.agristability_scenario scp on pyv.program_year_version_id = scp.program_year_version_id
            join farms.agristability_claim clm on scp.agristability_scenario_id = clm.agristability_scenario_id
            where scp.agristability_scenario_id = v_scenario_id
            except
            select coalesce(z50.unadjusted_reference_margin, 0) unadj_reference_margin,
                   coalesce(z50.adjusted_reference_margin, 0) adjusted_reference_margin,
                   coalesce(z50.program_margin, 0) program_year_margin,
                   z51.federal_contributions,
                   z51.provincial_contributions,
                   z51.interim_contributions,
                   coalesce(z51.federal_contributions, 0) + coalesce(z51.provincial_contributions, 0) + coalesce(z51.interim_contributions, 0) total_benefit,
                   z51.producer_share
            from farms.z50_participant_benefit_calculation z50
            full outer join farms.z51_participant_contribution z51 on z50.participant_pin = z51.participant_pin
                                                                   and z50.program_year = z51.program_year
            join farms.agristability_client ac on z50.participant_pin = ac.participant_pin
                                               and z51.participant_pin = ac.participant_pin
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                       and (z50.program_year = py.year or z51.program_year = py.year)
            left outer join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            left outer join farms.agristability_scenario sc on pyv.program_year_version_id = sc.program_year_version_id
            where py.program_year_id = in_program_year_id
            and (in_program_year_vrsn_prev_id is null
                or pyv.program_year_version_id is null
                or pyv.program_year_version_id = in_program_year_vrsn_prev_id)
            and (v_scenario_id is null
                or sc.agristability_scenario_id is null
                or sc.agristability_scenario_id = v_scenario_id)
        ) t1
        union all
        (
            select coalesce(z50.unadjusted_reference_margin, 0) unadj_reference_margin,
                   coalesce(z50.adjusted_reference_margin, 0) adjusted_reference_margin,
                   coalesce(z50.program_margin, 0) program_year_margin,
                   z51.federal_contributions,
                   z51.provincial_contributions,
                   z51.interim_contributions,
                   coalesce(z51.federal_contributions, 0) + coalesce(z51.provincial_contributions, 0) + coalesce(z51.interim_contributions, 0) total_benefit,
                   z51.producer_share
            from farms.z50_participant_benefit_calculation z50
            full outer join farms.z51_participant_contribution z51 on z50.participant_pin = z51.participant_pin
                                                                   and z50.program_year = z51.program_year
            join farms.agristability_client ac on z50.participant_pin = ac.participant_pin
                                               and z51.participant_pin = ac.participant_pin
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                       and (z50.program_year = py.year or z51.program_year = py.year)
            left outer join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            left outer join farms.agristability_scenario sc on pyv.program_year_version_id = sc.program_year_version_id
            where py.program_year_id = in_program_year_id
            and (in_program_year_vrsn_prev_id is null
                or pyv.program_year_version_id is null
                or pyv.program_year_version_id = in_program_year_vrsn_prev_id)
            and (v_scenario_id is null
                or sc.agristability_scenario_id is null
                or sc.agristability_scenario_id = v_scenario_id)
            except
            select clm.unadjusted_reference_margin,
                   clm.adjusted_reference_margin,
                   clm.program_year_margin,
                   clm.federal_contributions,
                   clm.provincial_contributions,
                   clm.interim_contributions,
                   clm.total_benefit,
                   clm.producer_share
            from farms.agristability_client ac
            join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
            join farms.program_year_version pyv on py.program_year_id = pyv.program_year_id
            join farms.agristability_scenario scp on pyv.program_year_version_id = scp.program_year_version_id
            join farms.agristability_claim clm on scp.agristability_scenario_id = clm.agristability_scenario_id
            where scp.agristability_scenario_id = v_scenario_id
        )
    ) t2;

    if cnt > 0 then
        return true;
    end if;

    return false;
end;
$$;
