create or replace function farms_calculator_pkg.pin_exists(
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type
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
        where ac.participant_pin = in_participant_pin
    ) then 1 else 0 end
    into v_pin_exists;

    return v_pin_exists;
end;
$$;
