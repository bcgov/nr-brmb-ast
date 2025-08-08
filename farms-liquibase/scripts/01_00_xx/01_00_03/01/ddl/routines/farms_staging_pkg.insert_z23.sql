create or replace procedure farms_staging_pkg.insert_z23(
   in in_productive_capacity_key farms.farm_z23_livestock_prod_cpcts.productive_capacity_key%type,
   in in_participant_pin farms.farm_z23_livestock_prod_cpcts.participant_pin%type,
   in in_program_year farms.farm_z23_livestock_prod_cpcts.program_year%type,
   in in_operation_number farms.farm_z23_livestock_prod_cpcts.operation_number%type,
   in in_inventory_code farms.farm_z23_livestock_prod_cpcts.inventory_code%type,
   in in_productive_capacity_amount farms.farm_z23_livestock_prod_cpcts.productive_capacity_amount%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_z23_livestock_prod_cpcts (
        productive_capacity_key,
        participant_pin,
        program_year,
        operation_number,
        inventory_code,
        productive_capacity_amount,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
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
