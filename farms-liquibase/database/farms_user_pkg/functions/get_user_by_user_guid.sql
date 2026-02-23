create or replace function farms_user_pkg.get_user_by_user_guid(
   in in_user_guid farms.farm_users.user_guid%type
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
               fu.source_directory,
               fu.account_name,
               fu.email_address,
               fu.verifier_ind,
               fu.deleted_ind,
               fu.revision_count,
               fu.who_created,
               fu.when_created,
               fu.who_updated,
               fu.when_updated
        from farms.farm_users fu
        where fu.user_guid = in_user_guid;
    return cur;

end;
$$;
