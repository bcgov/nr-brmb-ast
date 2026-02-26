create or replace procedure farms_tip_user_pkg.update_tip_participant_flag(
   in in_participant_pins numeric[],
   in in_tip_participant_ind farms.farm_agristability_clients.tip_participant_ind%type,
   in in_user farms.farm_agristability_clients.who_created%type
)
language plpgsql
as $$
begin

    update farms.farm_agristability_clients ac
    set ac.tip_participant_ind = in_tip_participant_ind,
        ac.revision_count = ac.revision_count + 1,
        ac.who_updated = in_user,
        ac.when_updated = current_timestamp
    where ac.participant_pin = any(in_participant_pins);

end;
$$;
