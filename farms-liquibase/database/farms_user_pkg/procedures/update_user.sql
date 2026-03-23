create or replace procedure farms_user_pkg.update_user(
   in in_user_id farms.farm_users.user_id%type,
   in in_user_guid farms.farm_users.user_guid%type,
   in in_email_address farms.farm_users.email_address%type,
   in in_verifier_ind farms.farm_users.verifier_ind%type,
   in in_deleted_ind farms.farm_users.deleted_ind%type,
   in in_user farms.farm_users.who_updated%type
)
language plpgsql
as $$
begin

    update farms.farm_users
    set user_guid = in_user_guid,
        email_address = in_email_address,
        verifier_ind = in_verifier_ind,
        deleted_ind = in_deleted_ind,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where user_id = in_user_id;

end;
$$;
