create or replace procedure farms_chefs_pkg.update_submission(
    in in_chef_submission_id farms.farm_chef_submissions.chef_submission_id%type,
    in in_validation_task_guid farms.farm_chef_submissions.validation_task_guid%type,
    in in_main_task_guid farms.farm_chef_submissions.main_task_guid%type,
    in in_bceid_form_ind farms.farm_chef_submissions.bceid_form_ind%type,
    in in_chef_submssn_status_code farms.farm_chef_submissions.chef_submssn_status_code%type,
    in in_user farms.farm_chef_submissions.who_updated%type
)
language plpgsql
as
$$
begin

    update farms.farm_chef_submissions
    set chef_submssn_status_code = in_chef_submssn_status_code,
        validation_task_guid = in_validation_task_guid,
        main_task_guid = in_main_task_guid,
        bceid_form_ind = in_bceid_form_ind,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where chef_submission_id = in_chef_submission_id;

end;
$$;
