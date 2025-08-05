create or replace function farms_error_pkg.codify_operation(
    in msg varchar,
    in operation_number farms.farm_farming_operations.operation_number%type,
    in federal_accounting_code farms.farm_farming_operations.federal_accounting_code%type,
    in program_year_version_id farms.farm_farming_operations.program_year_version_id%type
)
returns varchar
language plpgsql
as $$
begin
    if msg like '%farm_farming_operations_program_year_version_id_operation_n_key%' then
        return 'Farming Operation Number (' || operation_number || ') combination is not unique within the Program Year';
    elsif msg like '%farm_fo_farm_aac_fk%' then
        return 'The specified Federal Accounting Code (' || federal_accounting_code || ') was not found for this Operation';
    elsif msg like '%farm_fo_farm_pyv_fk%' then
        return 'The specified Program Year Version was not found for this Operation';
    else
        return farms_error_pkg.codify(msg);
    end if;
end;
$$;
