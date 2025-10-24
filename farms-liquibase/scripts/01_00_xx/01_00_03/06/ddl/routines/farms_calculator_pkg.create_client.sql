create or replace function farms_calculator_pkg.create_client(
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type,
    in in_sin farms.farm_agristability_clients.sin%type,
    in in_business_number farms.farm_agristability_clients.business_number%type,
    in in_trust_number farms.farm_agristability_clients.trust_number%type,
    in in_participant_class_code farms.farm_agristability_clients.participant_class_code%type,
    in in_owner_person_id farms.farm_agristability_clients.person_id%type,
    in in_contact_person_id farms.farm_agristability_clients.person_id_client_contacted_by%type,
    in in_user farms.farm_agristability_clients.who_created%type
) returns farms.farm_agristability_clients.agristability_client_id%type
language plpgsql
as
$$
declare

    v_new_client_id farms.farm_program_years.program_year_id%type;

begin
    select nextval('farms.farm_asc_seq')
    into v_new_client_id;

    insert into farms.farm_agristability_clients (
        agristability_client_id,
        participant_pin,
        sin,
        business_number,
        trust_number,
        federal_identifier,
        ident_effective_date,
        participant_class_code,
        participant_lang_code,
        public_office_ind,
        person_id,
        person_id_client_contacted_by,
        locally_updated_ind,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        v_new_client_id,
        in_participant_pin,
        in_sin,
        in_business_number,
        in_trust_number,
        coalesce(in_sin, coalesce(in_business_number, in_trust_number)),
		current_timestamp::date,
        in_participant_class_code,
        '1', -- english
        'N',
        in_owner_person_id,
        in_contact_person_id,
        'N',
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

    return v_new_client_id;
end;
$$;
