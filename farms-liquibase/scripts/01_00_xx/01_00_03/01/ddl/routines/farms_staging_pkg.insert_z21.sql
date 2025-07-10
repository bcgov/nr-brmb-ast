create or replace procedure farms_staging_pkg.insert_z21(
   in in_inventory_key farms.z21_participant_supplementary.inventory_key%type,
   in in_participant_pin farms.z21_participant_supplementary.participant_pin%type,
   in in_program_year farms.z21_participant_supplementary.program_year%type,
   in in_operation_number farms.z21_participant_supplementary.operation_number%type,
   in in_inventory_type_code farms.z21_participant_supplementary.inventory_type_code%type,
   in in_inventory_code farms.z21_participant_supplementary.inventory_code%type,
   in in_crop_unit_type farms.z21_participant_supplementary.crop_unit_type%type,
   in in_crop_on_farm_acres farms.z21_participant_supplementary.crop_on_farm_acres%type,
   in in_crop_qty_produced farms.z21_participant_supplementary.crop_quantity_produced%type,
   in in_quantity_end farms.z21_participant_supplementary.quantity_end%type,
   in in_end_of_year_price farms.z21_participant_supplementary.end_of_year_price%type,
   in in_end_of_year_amount farms.z21_participant_supplementary.end_of_year_amount%type,
   in in_crop_unseedable_acres farms.z21_participant_supplementary.crop_unseedable_acres%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z21_participant_supplementary (
        inventory_key,
        participant_pin,
        program_year,
        operation_number,
        inventory_type_code,
        inventory_code,
        crop_unit_type,
        crop_on_farm_acres,
        crop_quantity_produced,
        quantity_end,
        end_of_year_price,
        end_of_year_amount,
        crop_unseedable_acres,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_inventory_key,
        in_participant_pin,
        in_program_year,
        in_operation_number,
        in_inventory_type_code,
        in_inventory_code,
        in_crop_unit_type,
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
