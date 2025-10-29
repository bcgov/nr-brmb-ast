create or replace function farms_webapp_pkg.get_representative(
    in in_guid farms.farm_agristability_represntves.user_guid%type
) returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select agristability_represntve_id,
               user_guid,
               userid
        from farms.farm_agristability_represntves
        where user_guid = in_guid;
    return v_cursor;
end;
$$;
