create or replace procedure farms_aarm_pkg.insert_staging_row(
   in in_aarm_margin_id farms.farm_zaarm_margins.zaarm_margin_id%type,
   in in_participant_pin farms.farm_zaarm_margins.participant_pin%type,
   in in_program_year farms.farm_zaarm_margins.program_year%type,
   in in_operation_number farms.farm_zaarm_margins.operation_number%type,
   in in_partner_percent farms.farm_zaarm_margins.partner_percent%type,
   in in_inventory_type_code farms.farm_zaarm_margins.inventory_type_code%type,
   in in_inventory_code farms.farm_zaarm_margins.inventory_code%type,
   in in_inventory_description farms.farm_zaarm_margins.inventory_description%type,
   in in_production_unit farms.farm_zaarm_margins.production_unit%type,
   in in_aarm_reference_p1_price farms.farm_zaarm_margins.aarm_reference_p1_price%type,
   in in_aarm_reference_p2_price farms.farm_zaarm_margins.aarm_reference_p2_price%type,
   in in_quantity_start farms.farm_zaarm_margins.quantity_start%type,
   in in_quantity_end farms.farm_zaarm_margins.quantity_end%type,
   in in_user text)
language plpgsql
as $$
declare

    v_inventory_code farms.farm_zaarm_margins.inventory_code%type := in_inventory_code;


begin
    --
    -- for some reason the csv files don't have these two inventory codes
    --
    if coalesce(v_inventory_code::text, '') = '' then
      if upper(in_inventory_description) = 'CWB RECEIVABLE ADJUSTMENT - BARLEY' then
        v_inventory_code := 4011;
      elsif upper(in_inventory_description) = 'CWB RECEIVABLE ADJUSTMENT - WHEAT' then
        v_inventory_code := 4012;
      end if;
    end if;

    insert into farms.farm_zaarm_margins(
      zaarm_margin_id,
      participant_pin,
      program_year,
      operation_number,
      partner_percent,
      inventory_type_code,
      inventory_code,
      inventory_description,
      production_unit,
      aarm_reference_p1_price,
      aarm_reference_p2_price,
      quantity_start,
      quantity_end,
      revision_count,
      who_created,
      when_created,
      who_updated,
      when_updated
    ) values (
      in_aarm_margin_id,
      in_participant_pin,
      in_program_year,
      in_operation_number,
      in_partner_percent,
      in_inventory_type_code,
      v_inventory_code,
      in_inventory_description,
      in_production_unit,
      in_aarm_reference_p1_price,
      in_aarm_reference_p2_price,
      in_quantity_start,
      in_quantity_end,
      1,
      in_user,
      current_timestamp,
      in_user,
      current_timestamp
    );
end;
$$;
