create or replace procedure farms_staging_pkg.insert_z05(
   in in_partner_info_key farms.farm_z05_partner_infos.partner_info_key%type,
   in in_participant_pin farms.farm_z05_partner_infos.participant_pin%type,
   in in_program_year farms.farm_z05_partner_infos.program_year%type,
   in in_operation_number farms.farm_z05_partner_infos.operation_number%type,
   in in_partnership_pin farms.farm_z05_partner_infos.partnership_pin%type,
   in in_partner_first_name farms.farm_z05_partner_infos.partner_first_name%type,
   in in_partner_last_name farms.farm_z05_partner_infos.partner_last_name%type,
   in in_partner_corp_name farms.farm_z05_partner_infos.partner_corp_name%type,
   in in_partner_sin_ctn_bn farms.farm_z05_partner_infos.partner_sin_ctn_bn%type,
   in in_partner_percent farms.farm_z05_partner_infos.partner_percent%type,
   in in_partner_pin farms.farm_z05_partner_infos.partner_pin%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_z05_partner_infos (
        partner_info_key,
        participant_pin,
        program_year,
        operation_number,
        partnership_pin,
        partner_first_name,
        partner_last_name,
        partner_corp_name,
        partner_sin_ctn_bn,
        partner_percent,
        partner_pin,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
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
