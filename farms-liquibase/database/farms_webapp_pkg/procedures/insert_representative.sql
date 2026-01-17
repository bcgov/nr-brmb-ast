create or replace procedure farms_webapp_pkg.insert_representative(
    inout io_id farms.farm_agristability_represntves.agristability_represntve_id%type,
    in in_guid farms.farm_agristability_represntves.user_guid%type,
    in in_userid farms.farm_agristability_represntves.userid%type
)
language plpgsql
as
$$
begin
    insert into farms.farm_agristability_represntves(
        agristability_represntve_id,
        user_guid,
        userid,
        revision_count,
        who_created,
        when_created
    ) values (
        nextval('farms.farm_asr_seq'),
        in_guid,
        in_userid,
        farms_types_pkg.revision_count_increment(),
        in_userid,
        current_timestamp
    )
    returning agristability_represntve_id into io_id;

end;
$$;
