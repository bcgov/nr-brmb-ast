create or replace procedure farms_staging_pkg.insert_z23(
   in in_productive_capacity_key farms.z23_livestock_production_capacity.productive_capacity_key%type,
   in in_participant_pin farms.z23_livestock_production_capacity.participant_pin%type,
   in in_program_year farms.z23_livestock_production_capacity.program_year%type,
   in in_operation_number farms.z23_livestock_production_capacity.operation_number%type,
   in in_inventory_code farms.z23_livestock_production_capacity.inventory_code%type,
   in in_productive_capacity_amount farms.z23_livestock_production_capacity.productive_capacity_amount%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z23_livestock_production_capacity (
        productive_capacity_key,
        participant_pin,
        program_year,
        operation_number,
        inventory_code,
        productive_capacity_amount,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_productive_capacity_key,
        in_participant_pin,
        in_program_year,
        in_operation_number,
        in_inventory_code,
        in_productive_capacity_amount,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
