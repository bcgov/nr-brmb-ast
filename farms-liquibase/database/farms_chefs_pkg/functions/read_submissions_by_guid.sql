create or replace function farms_chefs_pkg.read_submissions_by_guid(
    in in_submission_guids varchar[]
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
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
        where fs.chef_submission_guid = any(in_submission_guids)
        order by fs.when_created;

    return cur;
end;
$$;
