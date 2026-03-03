create or replace procedure farms_user_pkg.delete_user(
   in in_user_guid farms.farm_users.user_guid%type
)
language plpgsql
as $$
begin

    delete from farms.farm_users fu
    where fu.user_guid = in_user_guid;

end;
$$;
