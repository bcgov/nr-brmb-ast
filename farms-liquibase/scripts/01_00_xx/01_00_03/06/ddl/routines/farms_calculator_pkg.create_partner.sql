create or replace procedure farms_calculator_pkg.create_partner(
    in in_partner_percent farms.farm_farming_operatin_prtnrs.partner_percent%type,
    in in_partnership_pin farms.farm_farming_operatin_prtnrs.partnership_pin%type,
    in in_partner_sin farms.farm_farming_operatin_prtnrs.partner_sin%type,
    in in_first_name farms.farm_farming_operatin_prtnrs.first_name%type,
    in in_last_name farms.farm_farming_operatin_prtnrs.last_name%type,
    in in_corp_name farms.farm_farming_operatin_prtnrs.corp_name%type,
    in in_farming_operation_id farms.farm_farming_operatin_prtnrs.farming_operation_id%type,
    in in_user farms.farm_farming_operatin_prtnrs.who_created %type
)
language plpgsql
as
$$
begin

    insert into farms.farm_farming_operatin_prtnrs (
        farming_operatin_prtnr_id,
        partner_percent,
        partnership_pin,
        partner_sin,
        first_name,
        last_name,
        corp_name,
        farming_operation_id,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        nextval('farms.farm_fop_seq'),
        in_partner_percent,
        in_partnership_pin,
        in_partner_sin,
        in_first_name,
        in_last_name,
        in_corp_name,
        in_farming_operation_id,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp);

end;
$$;
