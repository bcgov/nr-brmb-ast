create or replace function farms_import_pkg.participant(
    in in_version_id numeric,
    in in_participant_pin numeric,
    out out_agristability_client_id farms.farm_agristability_clients.agristability_client_id%type,
    in in_user varchar,
    inout in_out_activity numeric,
    inout in_changed_contact_client_ids numeric[],
    out errors numeric
)
language plpgsql
as $$
declare
    z01_participant_cursor cursor for
        select z.participant_pin,
               ac.agristability_client_id,
               z.sin_ctn_bn p_in_federal_identifier,
               (case
                    when z.sin is null and trim(translate(z.sin_ctn_bn, '0123456789-,.', ' ')) is null then z.sin_ctn_bn
                    else z.sin
               end) p_in_sin,
               (case
                    when z.business_number is null and z.sin_ctn_bn like '%RC%' then z.sin_ctn_bn
                    else z.business_number
               end) p_in_business_number,
               (case
                    when z.trust_number is null and z.sin_ctn_bn like 'T%' then z.sin_ctn_bn
                    else z.trust_number
               end) p_in_trust_number,
               z.participant_type_code p_in_participant_class_code,
               z.participant_language p_in_participant_language_code,
               (case
                    when z.public_office_ind = 0 then 'N'
                    when z.public_office_ind = 1 then 'Y'
                    when z.public_office_ind = 2 then 'N'
                    else 'N'
               end) p_in_public_office_indicator,
               ac.agristability_client_id p_agristability_client_id,
               ac.sin p_out_sin,
               ac.business_number p_out_business_number,
               ac.trust_number p_out_trust_number,
               ac.participant_class_code p_out_participant_class_code,
               ac.participant_lang_code p_out_participant_language_code,
               ac.public_office_ind p_out_public_office_indicator,
               ac.locally_updated_ind p_locally_updated_indicator,
               --
               to_date(z.ident_effective_date, 'YYYYMMDD') p_in_identity_effective_date,
               ac.ident_effective_date p_out_identity_effective_date,
               --
               z.first_name c_in_first_name,
               z.last_name c_in_last_name,
               z.corp_name c_in_corp_name,
               z.address_1 c_in_address_1,
               z.address_2 c_in_address_2,
               z.city c_in_city,
               z.province c_in_province_state,
               z.postal_code c_in_postal_code,
               z.country c_in_country,
               z.participant_fax c_in_fax_number,
               z.participant_phone_day c_in_daytime_phone,
               z.participant_phone_evening c_in_evening_phone,
               z.participant_cell_number c_in_cell_number,
               z.participant_email_address c_in_email_address,
               ac.person_id c_person_id,
               --
               p.first_name c_out_first_name,
               p.last_name c_out_last_name,
               p.corp_name c_out_corp_name,
               p.address_line_1 c_out_address_1,
               p.address_line_2 c_out_address_2,
               p.city c_out_city,
               p.province_state c_out_province_state,
               p.country c_out_country,
               p.postal_code c_out_postal_code,
               p.fax_number c_out_fax_number,
               p.daytime_phone c_out_daytime_phone,
               p.evening_phone c_out_evening_phone,
               p.cell_number c_out_cell_number,
               p.email_address c_out_email_address,
               --
               z.contact_first_name r_in_first_name,
               z.contact_last_name r_in_last_name,
               z.contact_business_name r_in_corp_name,
               z.contact_address_1 r_in_address_1,
               z.contact_address_2 r_in_address_2,
               z.contact_city r_in_city,
               z.contact_province r_in_province_state,
               null r_in_country, -- no country for contact
               z.contact_postal_code r_in_postal_code,
               z.contact_fax_number r_in_fax_number,
               z.contact_phone_day r_in_daytime_phone,
               z.contact_cell_number r_in_cell_number,
               null r_in_email_address, -- no email for contact
               ac.person_id_client_contacted_by r_person_id,
               q.first_name r_out_first_name,
               q.last_name r_out_last_name,
               q.corp_name r_out_corp_name,
               q.address_line_1 r_out_address_1,
               q.address_line_2 r_out_address_2,
               q.city r_out_city,
               q.province_state r_out_province_state,
               q.country r_out_country,
               q.postal_code r_out_postal_code,
               q.fax_number r_out_fax_number,
               q.cell_number r_out_cell_number,
               q.daytime_phone r_out_daytime_phone,
               q.email_address r_out_email_address
               --
        from farms.farm_z01_participant_infos z
        left outer join farms.farm_agristability_clients ac on ac.participant_pin = z.participant_pin
        left outer join farms.farm_persons p on p.person_id = ac.person_id
        left outer join farms.farm_persons q on q.person_id = ac.person_id_client_contacted_by
        where z.participant_pin = in_participant_pin;
    z01_val record;

    v_participant_class_code farms.farm_z01_participant_infos.participant_type_code%type := null;
    v_participant_language_code farms.farm_z01_participant_infos.participant_language%type := null;

    p1_msg varchar(4000) := null;
    p2_msg varchar(4000) := null;

    person_id numeric := null;
    person_rep_id numeric := null;
begin
    errors := 0;

    -- participant person info
    for z01_val in z01_participant_cursor
    loop

        v_participant_class_code := z01_val.p_in_participant_class_code;
        v_participant_language_code := z01_val.p_in_participant_language_code;
        -- ag client info
        if z01_val.p_locally_updated_indicator is null then
            select nextval('farms.farm_asc_seq')
            into out_agristability_client_id;
            in_out_activity := in_out_activity + 1;

            -- insert row + log
            call farms_import_pkg.append_imp1(in_version_id,
                '<AGRISTATIBLITY_CLIENT action="add" participant_pin="' ||
                farms_import_pkg.scrub(z01_val.participant_pin::varchar) || '">' ||
                '<ATTR name="federal_identifier" new="' ||
                farms_import_pkg.scrub(z01_val.p_in_federal_identifier) || '"/>' ||
                '<ATTR name="sin" new="' ||
                farms_import_pkg.scrub(z01_val.p_in_sin) || '"/>' ||
                '<ATTR name="business_number" new="' ||
                farms_import_pkg.scrub(z01_val.p_in_business_number) || '"/>' ||
                '<ATTR name="trust_number" new="' ||
                farms_import_pkg.scrub(z01_val.p_in_trust_number) || '"/>' ||
                '<ATTR name="participant_class_code" new="' ||
                farms_import_pkg.scrub(z01_val.p_in_participant_class_code::varchar) || '"/>' ||
                '<ATTR name="participant_lang_code" new="' ||
                farms_import_pkg.scrub(z01_val.p_in_participant_language_code::varchar) || '"/>' ||
                '<ATTR name="public_office_ind" new="' ||
                farms_import_pkg.scrub(z01_val.p_in_public_office_indicator::varchar) || '"/>' ||
                '<ATTR name="ident_effective_date" new="' ||
                to_char(z01_val.p_in_identity_effective_date, 'YYYYMMDD') || '"/>');

        elsif not farms_import_pkg.text_equal(z01_val.p_in_sin, z01_val.p_out_sin)
            or not farms_import_pkg.text_equal(z01_val.p_in_business_number, z01_val.p_out_business_number)
            or not farms_import_pkg.text_equal(z01_val.p_in_trust_number, z01_val.p_out_trust_number)
            or not farms_import_pkg.text_equal(z01_val.p_in_participant_class_code::varchar, z01_val.p_out_participant_class_code)
            or not farms_import_pkg.text_equal(z01_val.p_in_participant_language_code::varchar, z01_val.p_out_participant_language_code)
            or not farms_import_pkg.text_equal(z01_val.p_in_public_office_indicator, z01_val.p_out_public_office_indicator)
            or not farms_import_pkg.text_equal(to_char(z01_val.p_in_identity_effective_date, 'YYYYMMDD'), to_char(z01_val.p_out_identity_effective_date, 'YYYYMMDD'))
            or not farms_import_pkg.numbers_equal(z01_val.c_person_id, person_id)
            or not farms_import_pkg.numbers_equal(z01_val.r_person_id, person_rep_id) then
            in_out_activity := in_out_activity + 1;
            if z01_val.p_locally_updated_indicator = 'Y' then
                out_agristability_client_id := z01_val.agristability_client_id;
                -- log changes with a warning
                call farms_import_pkg.append_imp1(in_version_id,
                    '<AGRISTATIBLITY_CLIENT action="locally_updated" participant_pin="' ||
                    farms_import_pkg.scrub(z01_val.participant_pin::varchar) || '">');
            else
                -- update and log
                out_agristability_client_id := z01_val.agristability_client_id;

                call farms_import_pkg.append_imp1(in_version_id,
                    '<AGRISTATIBLITY_CLIENT action="update" participant_pin="' ||
                    z01_val.participant_pin || '">' ||
                    (case
                        when not farms_import_pkg.text_equal(z01_val.p_in_sin, z01_val.p_out_sin) then
                            '<ATTR name="sin" old="' ||
                            farms_import_pkg.scrub(z01_val.p_out_sin) || '" new="' ||
                            farms_import_pkg.scrub(z01_val.p_in_sin) || '"/>'
                        else
                            ''
                    end) ||
                    (case
                        when not farms_import_pkg.text_equal(z01_val.p_in_business_number, z01_val.p_out_business_number) then
                            '<ATTR name="business_number" old="' ||
                            farms_import_pkg.scrub(z01_val.p_out_business_number) || '" new="' ||
                            farms_import_pkg.scrub(z01_val.p_in_business_number) || '"/>'
                        else
                            ''
                    end) ||
                    (case
                        when not farms_import_pkg.text_equal(z01_val.p_in_trust_number, z01_val.p_out_trust_number) then
                            '<ATTR name="trust_number" old="' ||
                            farms_import_pkg.scrub(z01_val.p_out_trust_number) || '" new="' ||
                            farms_import_pkg.scrub(z01_val.p_in_trust_number) || '"/>'
                        else
                            ''
                    end) ||
                    (case
                        when not farms_import_pkg.text_equal(z01_val.p_in_participant_class_code::varchar, z01_val.p_out_participant_class_code) then
                            '<ATTR name="participant_class_code" old="' ||
                            farms_import_pkg.scrub(z01_val.p_out_participant_class_code::varchar) || '" new="' ||
                            farms_import_pkg.scrub(z01_val.p_in_participant_class_code::varchar) || '"/>'
                        else
                            ''
                    end) ||
                    (case
                        when not farms_import_pkg.text_equal(z01_val.p_in_participant_language_code::varchar, z01_val.p_out_participant_language_code) then
                            '<ATTR name="participant_lang_code" old="' ||
                            farms_import_pkg.scrub(z01_val.p_out_participant_language_code::varchar) || '" new="' ||
                            farms_import_pkg.scrub(z01_val.p_in_participant_language_code::varchar) || '"/>'
                        else
                            ''
                    end) ||
                    (case
                        when not farms_import_pkg.text_equal(z01_val.p_in_public_office_indicator, z01_val.p_out_public_office_indicator) then
                            '<ATTR name="public_office_ind" old="' ||
                            farms_import_pkg.scrub(z01_val.p_out_public_office_indicator::varchar) || '" new="' ||
                            farms_import_pkg.scrub(z01_val.p_in_public_office_indicator::varchar) || '"/>'
                        else
                            ''
                    end) ||
                    (case
                        when not farms_import_pkg.text_equal(to_char(z01_val.p_in_identity_effective_date, 'YYYYMMDD'), to_char(z01_val.p_out_identity_effective_date, 'YYYYMMDD')) then
                            '<ATTR name="ident_effective_date" old="' ||
                            to_char(z01_val.p_out_identity_effective_date, 'YYYYMMDD') || '" new="' ||
                            to_char(z01_val.p_in_identity_effective_date, 'YYYYMMDD') || '"/>'
                        else
                            ''
                    end));
            end if;
        else
            out_agristability_client_id := z01_val.agristability_client_id;
            call farms_import_pkg.append_imp1(in_version_id,
                '<AGRISTATIBLITY_CLIENT action="update" participant_pin="' ||
                farms_import_pkg.scrub(z01_val.participant_pin::varchar) || '"/>');
        end if;
        person_id := z01_val.c_person_id;
        person_rep_id := z01_val.r_person_id;

        if (z01_val.p_out_identity_effective_date is null
            or z01_val.p_out_identity_effective_date <> z01_val.p_in_identity_effective_date) then

            select * into person_id, in_out_activity, in_changed_contact_client_ids, p1_msg from farms_import_pkg.person(
                in_version_id,
                person_id,
                z01_val.c_in_address_1,
                z01_val.c_in_address_2,
                z01_val.c_in_city,
                z01_val.c_in_corp_name,
                z01_val.c_in_daytime_phone,
                z01_val.c_in_evening_phone,
                z01_val.c_in_fax_number,
                z01_val.c_in_cell_number,
                z01_val.c_in_first_name,
                z01_val.c_in_last_name,
                z01_val.c_in_postal_code,
                z01_val.c_in_province_state,
                z01_val.c_in_country,
                z01_val.c_in_email_address,
                z01_val.c_out_address_1,
                z01_val.c_out_address_2,
                z01_val.c_out_city,
                z01_val.c_out_corp_name,
                z01_val.c_out_daytime_phone,
                z01_val.c_out_evening_phone,
                z01_val.c_out_fax_number,
                z01_val.c_out_cell_number,
                z01_val.c_out_first_name,
                z01_val.c_out_last_name,
                z01_val.c_out_postal_code,
                z01_val.c_out_province_state,
                z01_val.c_out_country,
                z01_val.c_out_email_address,
                in_user,
                in_out_activity,
                out_agristability_client_id,
                in_changed_contact_client_ids
            );

            select * into person_rep_id, in_out_activity, in_changed_contact_client_ids, p2_msg from farms_import_pkg.person(
                in_version_id,
                person_rep_id,
                z01_val.r_in_address_1,
                z01_val.r_in_address_2,
                z01_val.r_in_city,
                z01_val.r_in_corp_name,
                z01_val.r_in_daytime_phone,
                z01_val.r_in_daytime_phone, -- evening
                z01_val.r_in_fax_number,
                z01_val.r_in_cell_number,
                z01_val.r_in_first_name,
                z01_val.r_in_last_name,
                z01_val.r_in_postal_code,
                z01_val.r_in_province_state,
                z01_val.r_in_country,
                z01_val.r_in_email_address,
                z01_val.r_out_address_1,
                z01_val.r_out_address_2,
                z01_val.r_out_city,
                z01_val.r_out_corp_name,
                z01_val.r_out_daytime_phone,
                z01_val.r_out_daytime_phone, -- evening
                z01_val.r_out_fax_number,
                z01_val.r_out_cell_number,
                z01_val.r_out_first_name,
                z01_val.r_out_last_name,
                z01_val.r_out_postal_code,
                z01_val.r_out_province_state,
                z01_val.r_out_country,
                z01_val.r_out_email_address,
                in_user,
                in_out_activity,
                out_agristability_client_id,
                in_changed_contact_client_ids
            );
        end if;

        -- ag client info
        if z01_val.p_locally_updated_indicator is null then
            -- insert row + log
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
                out_agristability_client_id,
                z01_val.participant_pin,
                z01_val.p_in_sin,
                z01_val.p_in_business_number,
                z01_val.p_in_trust_number,
                z01_val.p_in_federal_identifier,
                z01_val.p_in_identity_effective_date,
                z01_val.p_in_participant_class_code::varchar,
                z01_val.p_in_participant_language_code::varchar,
                z01_val.p_in_public_office_indicator,
                person_id,
                person_rep_id,
                'N',
                1,
                in_user,
                current_timestamp,
                in_user,
                current_timestamp
            );

        elsif z01_val.p_in_sin <> z01_val.p_out_sin
            or z01_val.p_in_business_number <> z01_val.p_out_business_number
            or z01_val.p_in_trust_number <> z01_val.p_out_trust_number
            or z01_val.p_in_participant_class_code::varchar <> z01_val.p_out_participant_class_code
            or z01_val.p_in_participant_language_code::varchar <> z01_val.p_out_participant_language_code
            or z01_val.p_in_public_office_indicator <> z01_val.p_out_public_office_indicator
            or z01_val.p_in_identity_effective_date <> z01_val.p_out_identity_effective_date
            or z01_val.c_person_id <> person_id
            or z01_val.r_person_id <> person_rep_id then
            if z01_val.p_locally_updated_indicator <> 'Y' then
                -- update
                update farms.farm_agristability_clients
                set federal_identifier = z01_val.p_in_federal_identifier,
                    sin = z01_val.p_in_sin,
                    business_number = z01_val.p_in_business_number,
                    trust_number = z01_val.p_in_trust_number,
                    participant_class_code = z01_val.p_in_participant_class_code::varchar,
                    participant_lang_code = z01_val.p_in_participant_language_code::varchar,
                    public_office_ind = z01_val.p_in_public_office_indicator,
                    ident_effective_date = z01_val.p_in_identity_effective_date,
                    person_id = person_id,
                    person_id_client_contacted_by = person_rep_id,
                    revision_count = revision_count + 1,
                    who_updated = in_user,
                    when_updated = current_timestamp
                where agristability_client_id = z01_val.p_agristability_client_id;
            end if;
        end if;

        call farms_import_pkg.append_imp1(in_version_id, '</AGRISTATIBLITY_CLIENT>');
    end loop;

    if p1_msg is not null or p2_msg is not null then
        raise exception 'Participant % has warnings: % %',
            in_participant_pin,
            coalesce(p1_msg, ''),
            coalesce(p2_msg, '');
    end if;
exception
    when others then
        if p1_msg is not null or p2_msg is not null then
            call farms_import_pkg.append_imp1(in_version_id,
                '<AGRISTATIBLITY_CLIENT action="error" participant_pin="' ||
                farms_import_pkg.scrub(z01_val.participant_pin::varchar) || '">');
            if p1_msg is not null then
                call farms_import_pkg.append_imp1(in_version_id, p1_msg);
            end if;
            if p2_msg is not null then
                call farms_import_pkg.append_imp1(in_version_id, p2_msg);
            end if;
            call farms_import_pkg.append_imp1(in_version_id, '</AGRISTATIBLITY_CLIENT>');
        else
            call farms_import_pkg.append_imp1(in_version_id,
                '<AGRISTATIBLITY_CLIENT action="error"><ERROR>' ||
                farms_import_pkg.scrub(farms_error_pkg.codify_participant(
                    sqlerrm,
                    in_participant_pin,
                    v_participant_class_code::varchar,
                    v_participant_language_code::varchar)) ||
                '</ERROR></AGRISTATIBLITY_CLIENT>');
        end if;
end;
$$;
