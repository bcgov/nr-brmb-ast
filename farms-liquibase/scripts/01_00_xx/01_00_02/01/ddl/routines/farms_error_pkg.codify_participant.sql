create or replace function farms_error_pkg.codify_participant(
    in msg varchar,
    in participant_pin farms.farm_agristability_clients.participant_pin%type,
    in participant_class_code farms.farm_agristability_clients.participant_class_code%type,
    in participant_lang_code farms.farm_agristability_clients.participant_lang_code%type
)
returns varchar
language plpgsql
as $$
begin
    if msg like '%fk_ac1_pcc%' then
        return 'The specified Participant Class Code (' || participant_class_code || ') was not found for this Client';
    elsif msg like '%fk_ac1_plc%' then
        return 'The specified Participant Language Code (' || participant_lang_code || ') was not found for this Client';
    else
        return farms_error_pkg.codify(msg);
    end if;
end;
$$;
