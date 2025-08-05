create or replace function farms_codes_write_pkg.copy_year_configuration_params(
    in in_to_year farms.farm_program_years.year%type,
    in in_user farms.farm_program_years.who_created%type
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
    from farms.farm_year_configuration_params ycp
    where ycp.year = in_to_year;

    if v_record_count = 0 then

        insert into farms.farm_year_configuration_params (
            year_configuration_param_id,
            program_year,
            parameter_name,
            parameter_value,
            config_param_type_code,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        )
        select nextval('farms.farm_ycp_seq'),
               in_to_year,
               parameter_name,
               parameter_value,
               config_param_type_code,
               revision_count,
               in_user who_created,
               current_timestamp,
               in_user who_updated,
               current_timestamp
        from farms.farm_year_configuration_params ycp
        where ycp.program_year = (
            select max(a.program_year)
            from farms.farm_year_configuration_params a
            where a.program_year < in_to_year
        );

        v_rows_inserted := sql%rowcount;

    end if;

    return v_rows_inserted;

end;
$$;
