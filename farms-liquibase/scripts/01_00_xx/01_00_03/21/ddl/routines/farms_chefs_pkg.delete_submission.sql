create or replace procedure farms_chefs_pkg.delete_submission(
    in in_chef_submission_guid farms.farm_chef_submissions.chef_submission_guid%type
)
language plpgsql
as
$$
begin

    update farms.farm_agristability_scenarios
    set chef_submission_id = null
    where chef_submission_id = (
        select chef_submission_id
        from farms.farm_chef_submissions fs
        where fs.chef_submission_guid = in_chef_submission_guid
    );

    delete from farms.farm_chef_submssn_crm_entities
    where chef_submission_id = (
        select chef_submission_id
        from farms.farm_chef_submissions fs
        where fs.chef_submission_guid = in_chef_submission_guid
    );

    delete from farms.farm_chef_submissions
    where chef_submission_guid = in_chef_submission_guid;

end;
$$;
