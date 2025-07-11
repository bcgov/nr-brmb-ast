create or replace procedure farms_staging_pkg.insert_z42(
   in in_productive_capacity_key farms.z42_participant_reference_year.productive_capacity_key%type,
   in in_ref_operation_number farms.z42_participant_reference_year.operation_number%type,
   in in_participant_pin farms.z42_participant_reference_year.participant_pin%type,
   in in_program_year farms.z42_participant_reference_year.program_year%type,
   in in_productive_type_code farms.z42_participant_reference_year.productive_type_code%type,
   in in_productive_code farms.z42_participant_reference_year.productive_code%type,
   in in_productive_capacity_units farms.z42_participant_reference_year.productive_capacity_units%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z42_participant_reference_year (
        productive_capacity_key,
        operation_number,
        participant_pin,
        program_year,
        productive_type_code,
        productive_code,
        productive_capacity_units,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_productive_capacity_key,
        in_ref_operation_number,
        in_participant_pin,
        in_program_year,
        in_productive_type_code,
        in_productive_code,
        in_productive_capacity_units,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
