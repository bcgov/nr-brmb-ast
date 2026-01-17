create or replace procedure farms_staging_pkg.insert_z42(
   in in_productive_capacity_key farms.farm_z42_participant_ref_years.productive_capacity_key%type,
   in in_ref_operation_number farms.farm_z42_participant_ref_years.operation_number%type,
   in in_participant_pin farms.farm_z42_participant_ref_years.participant_pin%type,
   in in_program_year farms.farm_z42_participant_ref_years.program_year%type,
   in in_productive_type_code farms.farm_z42_participant_ref_years.productive_type_code%type,
   in in_productive_code farms.farm_z42_participant_ref_years.productive_code%type,
   in in_productive_capacity_units farms.farm_z42_participant_ref_years.productive_capacity_units%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.farm_z42_participant_ref_years (
        productive_capacity_key,
        operation_number,
        participant_pin,
        program_year,
        productive_type_code,
        productive_code,
        productive_capacity_units,
        revision_count,
        who_created,
        when_created,
        who_updated,
        when_updated
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
