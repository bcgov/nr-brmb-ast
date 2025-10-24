create or replace procedure farms_calculator_pkg.update_client(
    in in_agristability_client_id farms.farm_agristability_clients.agristability_client_id%type,
    in in_participant_class_code farms.farm_agristability_clients.participant_class_code%type,
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type,
    in in_sin farms.farm_agristability_clients.sin%type,
    in in_business_number farms.farm_agristability_clients.business_number%type,
    in in_trust_number farms.farm_agristability_clients.trust_number%type,
    in in_revision_count farms.farm_agristability_clients.revision_count%type,
    in in_user farms.farm_agristability_clients.who_updated%type
)
language plpgsql
as
$$
declare
    ora2pg_rowcount int;
begin
    update farms.farm_agristability_clients ac
    set ac.participant_class_code = in_participant_class_code,
        ac.participant_pin = in_participant_pin,
        ac.sin = in_sin,
        ac.business_number = in_business_number,
        ac.trust_number = in_trust_number,
        ac.locally_updated_ind = 'Y',
        ac.revision_count = ac.revision_count + 1,
        ac.who_updated = in_user,
        ac.when_updated = current_timestamp
    where ac.agristability_client_id = in_agristability_client_id
    and ac.revision_count = in_revision_count;

    get diagnostics ora2pg_rowcount = row_count;
    if ora2pg_rowcount <> 1 then
        raise exception '%', farms_types_pkg.invalid_revision_count_msg()
        using errcode = farms_types_pkg.invalid_revision_count_code()::text;
    end if;
end;
$$;
