create or replace function farms_user_pkg.get_user_by_user_id(
   in in_user_id farms.farm_users.user_id%type
)
returns refcursor
language plpgsql
as $$
declare

    cur refcursor;

begin
    open cur for
        select fu.user_id,
               fu.user_guid,
               fu.email_address,
               fu.verifier_ind,
               fu.deleted_ind,
               fu.revision_count,
               fu.who_created,
               fu.when_created,
               fu.who_updated,
               fu.when_updated
        from farms.farm_users fu
        where fu.user_id = in_user_id;
    return cur;

end;
$$;
