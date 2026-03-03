create or replace function farms_user_pkg.get_all_users(
   in in_deleted_ind farms.farm_users.deleted_ind%type
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
        where coalesce(in_deleted_ind::text, '') = ''
        or fu.deleted_ind = in_deleted_ind;
    return cur;

end;
$$;
