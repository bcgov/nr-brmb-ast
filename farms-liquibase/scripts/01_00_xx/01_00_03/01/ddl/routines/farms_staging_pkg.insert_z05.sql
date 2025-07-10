create or replace procedure farms_staging_pkg.insert_z05(
   in in_partner_info_key farms.z05_partner_information.partner_information_key%type,
   in in_participant_pin farms.z05_partner_information.participant_pin%type,
   in in_program_year farms.z05_partner_information.program_year%type,
   in in_operation_number farms.z05_partner_information.operation_number%type,
   in in_partnership_pin farms.z05_partner_information.partnership_pin%type,
   in in_partner_first_name farms.z05_partner_information.partner_first_name%type,
   in in_partner_last_name farms.z05_partner_information.partner_last_name%type,
   in in_partner_corp_name farms.z05_partner_information.partner_corp_name%type,
   in in_partner_sin_ctn_bn farms.z05_partner_information.partner_sin_ctn_business_number%type,
   in in_partner_percent farms.z05_partner_information.partner_percent%type,
   in in_partner_pin farms.z05_partner_information.partner_pin%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z05_partner_information (
        partner_information_key,
        participant_pin,
        program_year,
        operation_number,
        partnership_pin,
        partner_first_name,
        partner_last_name,
        partner_corp_name,
        partner_sin_ctn_business_number,
        partner_percent,
        partner_pin,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_partner_info_key,
        in_participant_pin,
        in_program_year,
        in_operation_number,
        in_partnership_pin,
        in_partner_first_name,
        in_partner_last_name,
        in_partner_corp_name,
        in_partner_sin_ctn_bn,
        in_partner_percent,
        in_partner_pin,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
