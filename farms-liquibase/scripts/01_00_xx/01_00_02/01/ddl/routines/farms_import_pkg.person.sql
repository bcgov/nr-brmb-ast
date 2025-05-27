create or replace function farms_import_pkg.person(
    in in_version_id numeric,
    inout in_person_id farms.person.person_id%type,
    in in_address_1 farms.person.address_line_1%type,
    in in_address_2 farms.person.address_line_2%type,
    in in_city farms.person.city%type,
    in in_corp_name farms.person.corp_name%type,
    in in_daytime_phone farms.person.daytime_phone%type,
    in in_evening_phone farms.person.evening_phone%type,
    in in_fax_number farms.person.fax_number%type,
    in in_cell_number farms.person.cell_number%type,
    in in_first_name farms.person.first_name%type,
    in in_last_name farms.person.last_name%type,
    in in_postal_code farms.person.postal_code%type,
    in in_province_state farms.person.province_state%type,
    in in_country farms.person.country%type,
    in in_email_address farms.person.email_address%type,
    in out_address_1 farms.person.address_line_1%type,
    in out_address_2 farms.person.address_line_2%type,
    in out_city farms.person.city%type,
    in out_corp_name farms.person.corp_name%type,
    in out_daytime_phone farms.person.daytime_phone%type,
    in out_evening_phone farms.person.evening_phone%type,
    in out_fax_number farms.person.fax_number%type,
    in out_cell_number farms.person.cell_number%type,
    in out_first_name farms.person.first_name%type,
    in out_last_name farms.person.last_name%type,
    in out_postal_code farms.person.postal_code%type,
    in out_province_state farms.person.province_state%type,
    in out_country farms.person.country%type,
    in out_email_address farms.person.email_address%type,
    in in_user farms.person.create_user%type,
    inout in_out_activity numeric,
    in in_agristability_client_id farms.agristability_client.agristability_client_id%type,
    inout in_change_contact_client_ids numeric[],
    out out_result varchar
)
language plpgsql
as $$
declare
    tmp varchar(4000);
    person_changed boolean := false;
    cnt numeric;
begin
    if in_person_id is null then
        -- insert row + log
        select nextval('farms.seq_p')
        into in_person_id;

        in_out_activity := in_out_activity + 1;
        person_changed := true;

        insert into farms.person(
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
            create_user,
            create_date,
            update_user,
            update_date
        ) values (
            in_person_id,
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

        select '<PERSON action="add">' ||
            coalesce('<ATTR name="first_name" new="' || farms_import_pkg.scrub(in_first_name) || '"/>', '') ||
            coalesce('<ATTR name="last_name" new="' || farms_import_pkg.scrub(in_last_name) || '"/>', '') ||
            coalesce('<ATTR name="corp_name" new="' || farms_import_pkg.scrub(in_corp_name) || '"/>', '') ||
            coalesce('<ATTR name="address_line_1" new="' || farms_import_pkg.scrub(in_address_1) || '"/>', '') ||
            coalesce('<ATTR name="address_line_2" new="' || farms_import_pkg.scrub(in_address_2) || '"/>', '') ||
            coalesce('<ATTR name="city" new="' || farms_import_pkg.scrub(in_city) || '"/>', '') ||
            coalesce('<ATTR name="postal_code" new="' || farms_import_pkg.scrub(in_postal_code) || '"/>', '') ||
            coalesce('<ATTR name="province_state" new="' || farms_import_pkg.scrub(in_province_state) || '"/>', '') ||
            coalesce('<ATTR name="country" new="' || farms_import_pkg.scrub(in_country) || '"/>', '') ||
            coalesce('<ATTR name="daytime_phone" new="' || farms_import_pkg.scrub(in_daytime_phone) || '"/>', '') ||
            coalesce('<ATTR name="evening_phone" new="' || farms_import_pkg.scrub(in_evening_phone) || '"/>', '') ||
            coalesce('<ATTR name="fax_number" new="' || farms_import_pkg.scrub(in_fax_number) || '"/>', '') ||
            coalesce('<ATTR name="cell_number" new="' || farms_import_pkg.scrub(in_cell_number) || '"/>', '') ||
            coalesce('<ATTR name="email_address" new="' || farms_import_pkg.scrub(in_email_address) || '"/>', '') ||
            '</PERSON>'
        into tmp;
    else

        in_out_activity := in_out_activity + 1;
        person_changed := true;

        update farms.person
        set first_name = in_first_name,
            last_name = in_last_name,
            corp_name = in_corp_name,
            address_line_1 = in_address_1,
            address_line_2 = in_address_2,
            city = in_city,
            postal_code = in_postal_code,
            province_state = in_province_state,
            country = in_country,
            daytime_phone = in_daytime_phone,
            evening_phone = in_evening_phone,
            fax_number = in_fax_number,
            cell_number = in_cell_number,
            email_address = in_email_address,
            revision_count = revision_count + 1,
            update_user = in_user,
            update_date = current_timestamp
        where person_id = in_person_id;

        tmp := '<PERSON action="update">' ||
            (case when farms_import_pkg.strings_are_different(in_first_name, out_first_name) then
                '<ATTR name="first_name" old="' || farms_import_pkg.scrub(out_first_name) ||
                '" new="' || farms_import_pkg.scrub(in_first_name) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_last_name, out_last_name) then
                '<ATTR name="last_name" old="' || farms_import_pkg.scrub(out_last_name) ||
                '" new="' || farms_import_pkg.scrub(in_last_name) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_corp_name, out_corp_name) then
                '<ATTR name="corp_name" old="' || farms_import_pkg.scrub(out_corp_name) ||
                '" new="' || farms_import_pkg.scrub(in_corp_name) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_address_1, out_address_1) then
                '<ATTR name="address_1" old="' || farms_import_pkg.scrub(out_address_1) ||
                '" new="' || farms_import_pkg.scrub(in_address_1) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_address_2, out_address_2) then
                '<ATTR name="address_2" old="' || farms_import_pkg.scrub(out_address_2) ||
                '" new="' || farms_import_pkg.scrub(in_address_2) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_city, out_city) then
                '<ATTR name="city" old="' || farms_import_pkg.scrub(out_city) ||
                '" new="' || farms_import_pkg.scrub(in_city) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_postal_code, out_postal_code) then
                '<ATTR name="postal_code" old="' || farms_import_pkg.scrub(out_postal_code) ||
                '" new="' || farms_import_pkg.scrub(in_postal_code) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_province_state, out_province_state) then
                '<ATTR name="province_state" old="' || farms_import_pkg.scrub(out_province_state) ||
                '" new="' || farms_import_pkg.scrub(in_province_state) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_country, out_country) then
                '<ATTR name="country" old="' || farms_import_pkg.scrub(out_country) ||
                '" new="' || farms_import_pkg.scrub(in_country) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_daytime_phone, out_daytime_phone) then
                '<ATTR name="daytime_phone" old="' || farms_import_pkg.scrub(out_daytime_phone) ||
                '" new="' || farms_import_pkg.scrub(in_daytime_phone) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_evening_phone, out_evening_phone) then
                '<ATTR name="evening_phone" old="' || farms_import_pkg.scrub(out_evening_phone) ||
                '" new="' || farms_import_pkg.scrub(in_evening_phone) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_fax_number, out_fax_number) then
                '<ATTR name="fax_number" old="' || farms_import_pkg.scrub(out_fax_number) ||
                '" new="' || farms_import_pkg.scrub(in_fax_number) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_cell_number, out_cell_number) then
                '<ATTR name="cell_number" old="' || farms_import_pkg.scrub(out_cell_number) ||
                '" new="' || farms_import_pkg.scrub(in_cell_number) || '"/>'
            else
                ''
            end) ||
            (case when farms_import_pkg.strings_are_different(in_email_address, out_email_address) then
                '<ATTR name="email_address" old="' || farms_import_pkg.scrub(out_email_address) ||
                '" new="' || farms_import_pkg.scrub(in_email_address) || '"/>'
            else
                ''
            end) ||
            '</PERSON>';
    end if;

    if tmp is not null then
        call farms_import_pkg.append_imp1(in_version_id, tmp);
    end if;

    if person_changed then
        select count(*)
        into cnt
        from unnest(in_change_contact_client_ids) as t(id)
        where id = in_agristability_client_id;

        if cnt = 0 then
            in_change_contact_client_ids := in_change_contact_client_ids || in_agristability_client_id;
        end if;
    end if;

    out_result := null;
exception
    when others then
        -- general exceptions
        out_result := '<PERSON action="error"><ERROR>' ||
            farms_import_pkg.scrub(farms_error_pkg.codify(sqlerrm)) ||
            '</ERROR></PERSON>';
end;
$$;
