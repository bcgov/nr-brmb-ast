create or replace function farms_enrolment_read_pkg.read_staging_results()
returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
        select z.participant_pin,
               z.failed_to_generate_ind,
               z.error_ind,
               z.failed_reason
      from farms.farm_zprogram_enrolments z
      order by z.participant_pin;

    return cur;

end;
$$;
