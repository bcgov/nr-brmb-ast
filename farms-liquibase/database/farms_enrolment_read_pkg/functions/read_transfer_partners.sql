create or replace function farms_enrolment_read_pkg.read_transfer_partners(
    in in_enrolment_ids bigint[]
)
returns table(
    program_enrolment_id    farms.farm_program_enrolments.program_enrolment_id%type,
    partnership_name        farms.farm_farming_operations.partnership_name%type,
    partnership_percent     farms.farm_farming_operations.partnership_percent%type,
    partnership_pin         farms.farm_farming_operatin_prtnrs.partnership_pin%type
)
language sql
as $$
    select distinct pe.program_enrolment_id,
           fo.partnership_name,
           fo.partnership_percent,
           fop.partnership_pin
    from farms.farm_program_enrolments pe
    join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = pe.agristability_scenario_id
    join farms.farm_farming_operations fo on fo.program_year_version_id = sc.program_year_version_id
    join farms.farm_farming_operatin_prtnrs fop on fop.farming_operation_id = fo.farming_operation_id
    where pe.program_enrolment_id = any(in_enrolment_ids)
    and (
        fo.partnership_name is not null
        or fo.partnership_pin != 0
        or fo.partnership_percent != 1
    )
    and fop.partnership_pin is not null
    and fop.partnership_pin != 0
    order by pe.program_enrolment_id,
             fo.partnership_name;
$$;
