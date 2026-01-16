create or replace function farms_export_pkg.f01_statement_a(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select sta.participant_pin participant_oid,
               sta.participant_pin part_pin,
               ac.sin participant_sin,
               ac.business_number part_bn,
               ac.trust_number part_trust_num,
               to_char(ac.ident_effective_date, 'YYYYMMDD') part_name_eff_date,
               case when ac.participant_class_code = '1' then ow.first_name else null end part_gname,
               case when ac.participant_class_code = '1' then ow.last_name else null end part_sname,
               case when ac.participant_class_code = '3' then ow.corp_name else null end corp_name,
               to_char(ac.ident_effective_date, 'YYYYMMDD') part_addr_eff_date,
               ow.address_line_1 part_line1_addr,
               ow.address_line_2 part_line2_addr,
               ow.city part_city,
               ow.province_state part_prov,
               ow.postal_code part_pstl_code,
               ow.country part_cntry_code,
               ac.participant_lang_code part_lang_code,
               ow.fax_number part_fax_num,
               ow.daytime_phone part_day_tel_num,
               cn.corp_name cntct_name, -- format spreadsheet doesn't match
               cn.address_line_1 cntct_line1_addr,
               cn.address_line_2 cntct_line2_addr,
               cn.city cntct_city,
               cn.province_state cntct_prov,
               cn.postal_code cntct_pstl_code,
               cn.daytime_phone cntct_day_tel_num,
               cn.fax_number cntct_fax_num,
               cn.first_name rct_cntct_first_name, -- part_contact.rct_cntct_first_name???
               cn.last_name rct_cntct_last_name, -- part_contact.rct_cntct_last_name???
               ow.email_address part_email_address,
               ow.cell_number part_cell_num,
               cn.cell_number cntct_cell_num
        from farms.farm_chef_statement_a_years_vw sta
        join farms.farm_agristability_clients ac on ac.agristability_client_id = sta.agristability_client_id
        join farms.farm_persons ow on ow.person_id = ac.person_id
        left outer join farms.farm_persons cn on cn.person_id = ac.person_id_client_contacted_by
        where sta.year = in_program_year
        order by participant_oid;

    return cur;
end;
$$;
