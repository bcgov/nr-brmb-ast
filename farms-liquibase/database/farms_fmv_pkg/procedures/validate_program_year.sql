create or replace procedure farms_fmv_pkg.validate_program_year(
   in in_import_version_id numeric
)
language plpgsql
as $$
declare
    v_msg varchar(200) := 'Input file has more than one program year';
    v_num_years numeric := 0;
begin
    select count(distinct program_year)
    into v_num_years
    from farms.farm_zfmv_fair_market_values;

    if v_num_years > 1 then
        call farms_fmv_pkg.insert_error(
            in_import_version_id,
            0,
            v_msg
        );
    end if;
end;
$$;
