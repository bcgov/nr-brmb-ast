create or replace function farms_calculator_pkg.copy_forward_year_config(
    in in_to_year farms.farm_program_years.year%type,
    in in_user farms.farm_program_years.who_created%type
) returns bigint
language plpgsql
as
$$
begin

    return farms_codes_write_pkg.copy_forward_year_config(in_to_year, in_user);

end;
$$;
