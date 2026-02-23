create or replace procedure farms_user_pkg.create_user(
   in in_user_guid farms.farm_users.user_guid%type,
   in in_source_directory farms.farm_users.source_directory%type,
   in in_account_name farms.farm_users.account_name%type,
   in in_email_address farms.farm_users.email_address%type,
   in in_verifier_ind farms.farm_users.verifier_ind%type,
   in in_deleted_ind farms.farm_users.deleted_ind%type,
   in in_user farms.farm_users.who_updated%type
)
language plpgsql
as $$
begin

    insert into farms.farm_users(
        user_id,
        user_guid,
        source_directory,
        account_name,
        email_address,
        verifier_ind,
        deleted_ind,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_u_seq'),
        in_user_guid,
        in_source_directory,
        in_account_name,
        in_email_address,
        in_verifier_ind,
        in_deleted_ind,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

end;
$$;
