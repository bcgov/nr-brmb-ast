create or replace function farms_calculator_pkg.create_person(
    in in_address_1 farms.farm_persons.address_line_1%type,
    in in_address_2 farms.farm_persons.address_line_2%type,
    in in_city farms.farm_persons.city%type,
    in in_corp_name farms.farm_persons.corp_name%type,
    in in_country farms.farm_persons.country%type,
    in in_daytime_phone farms.farm_persons.daytime_phone%type,
    in in_evening_phone farms.farm_persons.evening_phone%type,
    in in_fax_number farms.farm_persons.fax_number%type,
    in in_cell_number farms.farm_persons.cell_number%type,
    in in_first_name farms.farm_persons.first_name%type,
    in in_last_name farms.farm_persons.last_name%type,
    in in_postal_code farms.farm_persons.postal_code%type,
    in in_province_state farms.farm_persons.province_state%type,
    in in_email_address farms.farm_persons.email_address%type,
    in in_user farms.farm_persons.who_created%type
)
returns farms.farm_persons.person_id%type
language plpgsql
as
$$
declare

    v_new_person_id farms.farm_program_years.program_year_id%type;

begin
    select nextval('farms.farm_per_seq')
    into v_new_person_id;

    insert into farms.farm_persons (
        person_id,
        address_line_1,
        address_line_2,
        city,
        corp_name,
        daytime_phone,
        evening_phone,
        fax_number,
        cell_number,
        first_name,
        last_name,
        postal_code,
        province_state,
        country,
        email_address,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        v_new_person_id,
        in_address_1,
        in_address_2,
        in_city,
        in_corp_name,
        in_daytime_phone,
        in_evening_phone,
        in_fax_number,
        in_cell_number,
        in_first_name,
        in_last_name,
        in_postal_code,
        in_province_state,
        in_country,
        in_email_address,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );

    return v_new_person_id;
end;
$$;
