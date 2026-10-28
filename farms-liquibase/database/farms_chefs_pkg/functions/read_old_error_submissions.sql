create or replace function farms_chefs_pkg.read_old_error_submissions(
    in in_months_old integer,
    in in_max_rows integer
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    -- Submissions created more than in_months_old months ago that are still in an error status,
    -- oldest first. SUBMITTED is not included because those are meant to be processed normally.
    open cur for
        select fs.chef_submission_id,
               fs.chef_submission_guid,
               fs.validation_task_guid,
               fs.main_task_guid,
               fs.chef_form_type_code,
               ftc.description chef_form_type_description,
               fs.chef_submssn_status_code,
               fs.revision_count,
               fs.bceid_form_ind,
               fs.when_created,
               fs.when_updated
        from farms.farm_chef_submissions fs
        join farms.farm_chef_form_type_codes ftc on ftc.chef_form_type_code = fs.chef_form_type_code
        where fs.chef_submssn_status_code in ('INVALID', 'SYSTEM_ERR', 'PARSE_ERR')
          and fs.when_created < current_timestamp - make_interval(months => in_months_old)
        order by fs.when_created
        limit in_max_rows;

    return cur;
end;
$$;
