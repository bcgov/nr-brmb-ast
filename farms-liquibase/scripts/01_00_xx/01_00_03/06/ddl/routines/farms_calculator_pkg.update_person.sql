create or replace procedure farms_calculator_pkg.update_person(
    in in_person_id farms.farm_persons.person_id%type,
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
    in in_revision_count farms.farm_persons.revision_count%type,
    in in_user farms.farm_persons.who_created%type
)
language plpgsql
as
$$
declare
    ora2pg_rowcount int;
begin

    update farms.farm_persons
    set address_line_1 = in_address_1,
        address_line_2 = in_address_2,
        city = in_city,
        corp_name = in_corp_name,
        daytime_phone = in_daytime_phone,
        evening_phone = in_evening_phone,
        fax_number = in_fax_number,
        cell_number = in_cell_number,
        first_name = in_first_name,
        last_name = in_last_name,
        postal_code = in_postal_code,
        province_state = in_province_state,
        country = in_country,
        email_address = in_email_address,
        revision_count = revision_count,
        who_created = in_user,
        when_created = current_timestamp,
        who_updated = in_user,
        when_updated = current_timestamp
    where person_id = in_person_id
    and revision_count = in_revision_count;

    get diagnostics ora2pg_rowcount = row_count;
    if ora2pg_rowcount <> 1 then
        raise exception '%', farms_types_pkg.invalid_revision_count_msg()
        using errcode = farms_types_pkg.invalid_revision_count_code()::text;
    end if;

end;
$$;
