create or replace procedure farms_staging_pkg.insert_z22(
   in in_production_insurance_key farms.z22_production_insurance.production_insurance_key%type,
   in in_participant_pin farms.z22_production_insurance.participant_pin%type,
   in in_program_year farms.z22_production_insurance.program_year%type,
   in in_operation_number farms.z22_production_insurance.operation_number%type,
   in in_production_insurance_number farms.z22_production_insurance.production_insurance_number%type,
   in in_user varchar
)
language plpgsql
as $$
begin
    insert into farms.z22_production_insurance (
        production_insurance_key,
        participant_pin,
        program_year,
        operation_number,
        production_insurance_number,
        revision_count,
        create_user,
        create_date,
        update_user,
        update_date
    ) values (
        in_production_insurance_key,
        in_participant_pin,
        in_program_year,
        in_operation_number,
        in_production_insurance_number,
        1,
        in_user,
        current_timestamp,
        in_user,
        current_timestamp
    );
end;
$$;
