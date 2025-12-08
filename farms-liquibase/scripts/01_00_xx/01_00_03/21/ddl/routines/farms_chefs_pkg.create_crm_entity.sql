create or replace procedure farms_chefs_pkg.create_crm_entity(
    in in_crm_entity_guid farms.farm_chef_submssn_crm_entities.crm_entity_guid%type,
    in in_crm_entity_type_code farms.farm_chef_submssn_crm_entities.crm_entity_type_code%type,
    in in_chef_submission_id farms.farm_chef_submssn_crm_entities.chef_submission_id%type,
    in in_user farms.farm_chef_submssn_crm_entities.who_updated%type
)
language plpgsql
as
$$
begin

    insert into farms.farm_chef_submssn_crm_entities (
        chef_submssn_crm_entity_id,
        crm_entity_guid,
        crm_entity_type_code,
        chef_submission_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_csce_seq'),
        in_crm_entity_guid,
        in_crm_entity_type_code,
        in_chef_submission_id,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
