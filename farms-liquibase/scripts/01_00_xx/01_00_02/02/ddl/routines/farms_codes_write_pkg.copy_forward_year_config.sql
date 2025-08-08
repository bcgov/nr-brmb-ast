create or replace function farms_codes_write_pkg.copy_forward_year_config(
    in in_to_year farms.farm_program_years.year%type,
    in in_user farms.farm_program_years.who_created%type
)
returns numeric
language plpgsql
as $$
declare
    v_id_rows numeric;
    v_yc_rows numeric;
    v_li_rows numeric;
    v_rows_inserted numeric;
begin

    v_id_rows := farms_codes_write_pkg.copy_year_inventory_details(in_to_year, in_user);
    v_yc_rows := farms_codes_write_pkg.copy_year_configuration_params(in_to_year, in_user);
    v_li_rows := farms_codes_write_pkg.copy_year_line_items(in_to_year, in_user);

    v_rows_inserted := v_id_rows + v_yc_rows + v_li_rows;

    return v_rows_inserted;

end;
$$;
