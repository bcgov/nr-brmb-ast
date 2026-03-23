create or replace function farms_webapp_pkg.get_verifiers()
returns refcursor
language plpgsql
as
$$
declare

    v_cursor refcursor;

begin
    open v_cursor for
        select fu.email_address,
               fu.user_id
        from farms.farm_users fu
        where fu.verifier_ind = 'Y' and fu.deleted_ind = 'N'
        order by fu.email_address asc;
    return v_cursor;
end;
$$;
