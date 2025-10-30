create or replace function farms_webapp_pkg.read_contact_transfer(
    in in_client_ids numeric[]
) returns refcursor
language plpgsql
as
$$
declare

    return_cur refcursor;

begin

    open return_cur for
        select
            ac.agristability_client_id,
            ac.participant_pin,
            ac.participant_class_code,
            ac.sin,
            ac.business_number,
            ac.trust_number,
            pcc.description participant_class_code_desc,
            ac.federal_identifier,
            ac.ident_effective_date,

            o.corp_name owner_corp_name,
            o.first_name owner_first_name,
            o.last_name owner_last_name,
            o.address_line_1 owner_address_line_1,
            o.address_line_2 owner_address_line_2,
            o.city owner_city,
            o.province_state owner_province_state,
            o.country owner_country,
            o.postal_code owner_postal_code,
            o.daytime_phone owner_daytime_phone,
            o.evening_phone owner_evening_phone,
            o.fax_number owner_fax_number,
            o.cell_number owner_cell_number,
            o.email_address owner_email_address,

            c.corp_name contact_corp_name,
            c.first_name contact_first_name,
            c.last_name contact_last_name,
            c.address_line_1 contact_address_line_1,
            c.address_line_2 contact_address_line_2,
            c.city contact_city,
            c.province_state contact_province_state,
            c.country contact_country,
            c.postal_code contact_postal_code,
            c.daytime_phone contact_daytime_phone,
            c.evening_phone contact_evening_phone,
            c.fax_number contact_fax_number,
            c.cell_number contact_cell_number,
            c.email_address contact_email_address
        from farms.farm_agristability_clients ac
        left outer join farms.farm_persons o on o.person_id = ac.person_id
        left outer join farms.farm_persons c on c.person_id = ac.person_id_client_contacted_by
        left outer join farms.farm_participant_class_codes pcc on pcc.participant_class_code = ac.participant_class_code
        where ac.agristability_client_id = any(in_client_ids);

    return return_cur;

end;
$$;
