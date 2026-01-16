create or replace function farms_enrolment_read_pkg.read_transfer_partners(
    in in_enrolment_ids numeric[]
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
        select distinct pe.program_enrolment_id,
               fo.partnership_name,
               fo.partnership_percent,
               fo.partnership_pin
        from farms.farm_program_enrolments pe
        join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = pe.agristability_scenario_id
        join farms.farm_farming_operations fo on fo.program_year_version_id = sc.program_year_version_id
        where pe.program_enrolment_id = any(in_enrolment_ids)
        and (
            (fo.partnership_name is not null and fo.partnership_name::text <> '')
            or fo.partnership_pin != 0
            or fo.partnership_percent != 1
        )
        order by pe.program_enrolment_id,
                 fo.partnership_name;

    return cur;

end;
$$;
