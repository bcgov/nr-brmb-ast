create or replace procedure farms_chefs_pkg.create_submission(
    in in_chef_submission_guid farms.farm_chef_submissions.chef_submission_guid%type,
    in in_validation_task_guid farms.farm_chef_submissions.validation_task_guid%type,
    in in_main_task_guid farms.farm_chef_submissions.main_task_guid%type,
    in in_bceid_form_ind farms.farm_chef_submissions.bceid_form_ind%type,
    in in_chef_form_type_code farms.farm_chef_submissions.chef_form_type_code%type,
    in in_chef_submssn_status_code farms.farm_chef_submissions.chef_submssn_status_code%type,
    in in_user farms.farm_chef_submissions.who_updated%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_chef_submissions (
        chef_submission_id,
        chef_submission_guid,
        validation_task_guid,
        main_task_guid,
        bceid_form_ind,
        chef_form_type_code,
        chef_submssn_status_code,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_cs_seq'),
        in_chef_submission_guid,
        in_validation_task_guid,
        in_main_task_guid,
        in_bceid_form_ind,
        in_chef_form_type_code,
        in_chef_submssn_status_code,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
