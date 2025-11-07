create or replace function farms_enrolment_read_pkg.read_transfer_combined_farm_owners(
    in in_enrolment_ids numeric[]
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
        select pe.program_enrolment_id,
               cfac.participant_pin,
               cfcl.applied_benefit_percent combined_farm_percent
        from farms.farm_program_enrolments pe
        join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = pe.agristability_scenario_id
        join farms.farm_agristability_scenarios cfsc on cfsc.combined_farm_number = sc.combined_farm_number
        join farms.farm_program_year_versions cfpyv on cfpyv.program_year_version_id = cfsc.program_year_version_id
        join farms.farm_program_years cfpy on cfpy.program_year_id = cfpyv.program_year_id
        join farms.farm_agristability_clients cfac on cfac.agristability_client_id = cfpy.agristability_client_id
        left outer join farms.farm_agristability_claims cfcl on cfcl.agristability_scenario_id = cfsc.agristability_scenario_id
                                                             and sc.combined_farm_number is not null
                                                             and sc.combined_farm_number::text <> ''
        where pe.program_enrolment_id = any(in_enrolment_ids)
        order by pe.program_enrolment_id,
                 cfac.participant_pin;

    return cur;

end;
$$;
