create or replace function farms_enrolment_read_pkg.read_staging_results()
returns table(
    participant_pin         farms.farm_zprogram_enrolments.participant_pin%type,
    failed_to_generate_ind  farms.farm_zprogram_enrolments.failed_to_generate_ind%type,
    error_ind               farms.farm_zprogram_enrolments.error_ind%type,
    failed_reason           farms.farm_zprogram_enrolments.failed_reason%type
)
language sql
as $$
    select z.participant_pin,
           z.failed_to_generate_ind,
           z.error_ind,
           z.failed_reason
    from farms.farm_zprogram_enrolments z
    order by z.participant_pin;
$$;
