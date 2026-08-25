create or replace function farms_enrolment_read_pkg.read_transfer_partners(
    in in_enrolment_ids bigint[]
)
returns table(
    program_enrolment_id    farms.farm_program_enrolments.program_enrolment_id%type,
    partner_name            varchar,
    partner_percent         farms.farm_farming_operatin_prtnrs.partner_percent%type,
    partner_pin             farms.farm_farming_operatin_prtnrs.partnership_pin%type
)
language sql
as $$
    -- Every partner column comes from the partner record rather than the farming
    -- operation, and the name is assembled from the corporate name or the individual's
    -- name. The aliases are partner_* to match what EnrolmentReadDAO reads.
    select distinct pe.program_enrolment_id,
           coalesce(fop.corp_name, fop.last_name || ', ' || fop.first_name) as partner_name,
           fop.partner_percent,
           fop.partnership_pin as partner_pin
    from farms.farm_program_enrolments pe
    join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = pe.agristability_scenario_id
    join farms.farm_farming_operations fo on fo.program_year_version_id = sc.program_year_version_id
    join farms.farm_farming_operatin_prtnrs fop on fop.farming_operation_id = fo.farming_operation_id
    where pe.program_enrolment_id = any(in_enrolment_ids)
    and (
        fop.corp_name is not null
        or fop.first_name is not null
        or fop.last_name is not null
        or fop.partnership_pin is not null
        or fop.partner_percent is not null
    )
    and fo.partnership_percent != 1
    order by pe.program_enrolment_id,
             partner_name,
             fop.partner_percent,
             partner_pin;
$$;
