create or replace function farms_chefs_pkg.read_existing_submissions_for_pin_and_year(
    in in_chef_form_type_code farms.farm_chef_submissions.chef_form_type_code%type,
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type,
    in in_program_year farms.farm_program_years.year%type,
    in in_new_chef_submission_guid farms.farm_chef_submissions.chef_submission_guid%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
        select sub.chef_submission_guid
        from farms.farm_chef_submissions sub
        join farms.farm_scenarios_vw sv on sv.chef_submission_guid = sub.chef_submission_guid
        where sub.chef_submssn_status_code not in ('CANCELLED')
        and sub.chef_form_type_code = in_chef_form_type_code
        and sub.chef_submission_guid != in_new_chef_submission_guid
        and sv.participant_pin = in_participant_pin
        and sv.year = in_program_year;

    return cur;
end;
$$;
