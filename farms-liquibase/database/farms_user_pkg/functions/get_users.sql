create or replace function farms_user_pkg.get_users()
returns refcursor
language plpgsql
as $$
declare

    cur refcursor;

begin
    open cur for
        select fu.account_name,
               fu.user_id
        from farms.farm_users fu
        where fu.verifier_ind = 'Y' and fu.deleted_ind = 'N'
        order by fu.account_name asc;
    return cur;
end;
$$
