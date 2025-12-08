create or replace procedure farms_calculator_pkg.update_partner(
    in in_farming_operatin_prtnr_id farms.farm_farming_operatin_prtnrs.farming_operatin_prtnr_id%type,
    in in_partner_percent farms.farm_farming_operatin_prtnrs.partner_percent%type,
    in in_partnership_pin farms.farm_farming_operatin_prtnrs.partnership_pin%type,
    in in_partner_sin farms.farm_farming_operatin_prtnrs.partner_sin%type,
    in in_first_name farms.farm_farming_operatin_prtnrs.first_name%type,
    in in_last_name farms.farm_farming_operatin_prtnrs.last_name%type,
    in in_corp_name farms.farm_farming_operatin_prtnrs.corp_name%type,
    in in_farming_operation_id farms.farm_farming_operatin_prtnrs.farming_operation_id%type,
    in in_revision_count farms.farm_farming_operatin_prtnrs.revision_count%type,
    in in_user farms.farm_farming_operatin_prtnrs.who_created%type
)
language plpgsql
as
$$
declare
    ora2pg_rowcount int;
begin

    update farms.farm_farming_operatin_prtnrs
    set partner_percent = in_partner_percent,
        partnership_pin = in_partnership_pin,
        partner_sin = in_partner_sin,
        first_name = in_first_name,
        last_name = in_last_name,
        corp_name = in_corp_name,
        farming_operation_id = in_farming_operation_id,
        revision_count = revision_count + 1,
        who_updated = in_user,
        when_updated = current_timestamp
    where farming_operatin_prtnr_id = in_farming_operatin_prtnr_id
    and revision_count = in_revision_count;

    get diagnostics ora2pg_rowcount = row_count;
    if ora2pg_rowcount <> 1 then
        raise exception '%', farms_types_pkg.invalid_revision_count_msg()
        using errcode = farms_types_pkg.invalid_revision_count_code()::text;
    end if;

end;
$$;
