create or replace function farms_calculator_pkg.pin_checked_out_by_user(
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type,
    in in_user_guid farms.farm_program_years.assigned_to_user_guid%type
) returns bigint
language plpgsql
as
$$
declare

    v_pin_exists bigint;

begin
    select case when exists (
        select null
        from farms.farm_agristability_clients ac
        join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
        where ac.participant_pin = in_participant_pin
        and py.assigned_to_user_guid = in_user_guid
    ) then 1 else 0 end
    into v_pin_exists;

    return v_pin_exists;
end;
$$;
