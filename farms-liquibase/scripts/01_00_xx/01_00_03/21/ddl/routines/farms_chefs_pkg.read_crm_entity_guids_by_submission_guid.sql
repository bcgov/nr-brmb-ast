create or replace function farms_chefs_pkg.read_crm_entity_guids_by_submission_guid(
    in in_chef_submission_guid farms.farm_chef_submissions.chef_submission_guid%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
        select fcsce.chef_submssn_crm_entity_id,
               fcsce.crm_entity_guid,
               fcsce.chef_submission_id,
               fcsce.crm_entity_type_code,
               fcsce.revision_count,
               fcsce.when_created,
               fcsce.when_updated
        from farms.farm_chef_submssn_crm_entities fcsce
        join farms.farm_chef_submissions fcs on fcs.chef_submission_id = fcsce.chef_submission_id
        where coalesce(in_chef_submission_guid::text, '') = ''
        or fcs.chef_submission_guid = in_chef_submission_guid 
        order by fcsce.when_created desc;

    return cur;
end;
$$;
