create or replace procedure farms_staging_pkg.insert_z21(
   in in_inventory_key farms.farm_z21_participant_suppls.inventory_key%type,
   in in_participant_pin farms.farm_z21_participant_suppls.participant_pin%type,
   in in_program_year farms.farm_z21_participant_suppls.program_year%type,
   in in_operation_number farms.farm_z21_participant_suppls.operation_number%type,
   in in_inventory_type_code farms.farm_z21_participant_suppls.inventory_type_code%type,
   in in_inventory_code farms.farm_z21_participant_suppls.inventory_code%type,
   in in_crop_unit_type farms.farm_z21_participant_suppls.crop_unit_type%type,
   in in_crop_on_farm_acres farms.farm_z21_participant_suppls.crop_on_farm_acres%type,
   in in_crop_qty_produced farms.farm_z21_participant_suppls.crop_qty_produced%type,
   in in_quantity_end farms.farm_z21_participant_suppls.quantity_end%type,
   in in_end_of_year_price farms.farm_z21_participant_suppls.end_of_year_price%type,
   in in_end_of_year_amount farms.farm_z21_participant_suppls.end_of_year_amount%type,
   in in_crop_unseedable_acres farms.farm_z21_participant_suppls.crop_unseedable_acres%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_z21_participant_suppls (
        inventory_key,
        participant_pin,
        program_year,
        operation_number,
        inventory_type_code,
        inventory_code,
        crop_unit_type,
        crop_on_farm_acres,
        crop_qty_produced,
        quantity_end,
        end_of_year_price,
        end_of_year_amount,
        crop_unseedable_acres,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
    ) values (
        in_inventory_key,
        in_participant_pin,
        in_program_year,
        in_operation_number,
        in_inventory_type_code,
        in_inventory_code,
        case in_crop_unit_type
            when '67' then '10'
            when '70' then '15'
            else in_crop_unit_type
        end,
        in_crop_on_farm_acres,
        in_crop_qty_produced,
        in_quantity_end,
        in_end_of_year_price,
        in_end_of_year_amount,
        in_crop_unseedable_acres,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
