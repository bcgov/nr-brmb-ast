create or replace function farms_read_pkg.read_client(
    in ppin farms.agristability_client.participant_pin%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select ac.agristability_client_id,
               ac.federal_identifier,
               ac.sin,
               ac.business_number,
               ac.trust_number,
               ac.participant_pin,
               ac.identity_effective_date,
               ac.public_office_indicator,
               ac.locally_updated_indicator,
               ac.participant_language_code,
               plc.description as participant_language_description,
               ac.participant_class_code,
               pcc.description as participant_class_description,
               ac.revision_count,

               cl.person_id cl_person_id,
               cl.address_line_1 cl_address_line_1,
               cl.address_line_2 cl_address_line_2,
               cl.city cl_city,
               cl.corp_name cl_corp_name,
               cl.daytime_phone cl_daytime_phone,
               cl.evening_phone cl_evening_phone,
               cl.fax_number cl_fax_number,
               cl.cell_number cl_cell_number,
               cl.first_name cl_first_name,
               cl.last_name cl_last_name,
               cl.postal_code cl_postal_code,
               cl.province_state cl_province_state,
               cl.country cl_country,
               cl.email_address cl_email_address,
               cl.revision_count cl_revision_count,

               rp.person_id rp_person_id,
               rp.address_line_1 rp_address_line_1,
               rp.address_line_2 rp_address_line_2,
               rp.city rp_city,
               rp.corp_name rp_corp_name,
               rp.daytime_phone rp_daytime_phone,
               rp.evening_phone rp_evening_phone,
               rp.fax_number rp_fax_number,
               rp.cell_number rp_cell_number,
               rp.first_name rp_first_name,
               rp.last_name rp_last_name,
               rp.postal_code rp_postal_code,
               rp.province_state rp_province_state,
               rp.country rp_country,
               rp.email_address rp_email_address,
               rp.revision_count rp_revision_count
        from farms.agristability_client ac
        left outer join farms.participant_language_code plc on ac.participant_language_code = plc.participant_language_code
        left outer join farms.participant_class_code pcc on ac.participant_class_code = pcc.participant_class_code
        left outer join farms.person cl on ac.person_id = cl.person_id
        left outer join farms.person rp on ac.person_id_client_contacted_by = rp.person_id
        where ac.participant_pin = ppin;

    return cur;

end;
$$;
