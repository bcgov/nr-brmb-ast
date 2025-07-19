create or replace function farms_codes_write_pkg.copy_year_configuration_params(
    in in_to_year farms.program_year.year%type,
    in in_user farms.program_year.create_user%type
)
returns numeric
language plpgsql
as $$
declare
    v_record_count numeric;
    v_rows_inserted numeric := 0;
begin

    select count(*)
    into v_record_count
    from farms.year_configuration_parameter ycp
    where ycp.program_year = in_to_year;

    if v_record_count = 0 then

        insert into farms.year_configuration_parameter (
            year_configuration_parameter_id,
            program_year,
            parameter_name,
            parameter_value,
            configuration_parameter_type_code,
            revision_count,
            create_date,
            create_user,
            update_date,
            update_user
        )
        select nextval('farms.seq_ycp'),
               in_to_year,
               parameter_name,
               parameter_value,
               configuration_parameter_type_code,
               revision_count,
               current_timestamp,
               in_user create_user,
               current_timestamp,
               in_user update_user
        from farms.year_configuration_parameter ycp
        where ycp.program_year = (
            select max(a.program_year)
            from farms.year_configuration_parameter a
            where a.program_year < in_to_year
        );

        get diagnostics v_rows_inserted := row_count;

    end if;

    return v_rows_inserted;

end;
$$;
