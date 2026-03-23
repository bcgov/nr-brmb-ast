create or replace function farms_user_pkg.get_users()
returns refcursor
language plpgsql
as $$
declare

    cur refcursor;

begin
    open cur for
        select fu.email_address,
               fu.user_id
        from farms.farm_users fu
        where fu.verifier_ind = 'Y' and fu.deleted_ind = 'N'
        order by fu.email_address asc;
    return cur;
end;
$$;
