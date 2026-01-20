create or replace function farms_codes_read_pkg.export_fmv(
    in in_program_year farms.farm_fair_market_values.program_year%type,
    in in_inventory_code_filter varchar,
    in in_inventory_desc_filter varchar,
    in in_municipality_desc_filter varchar,
    in in_unit_desc_filter varchar
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select fmv.program_year ||
               ',' || fmv.period ||
               ',' || fmv.municipality_code ||
               ',' || fmv.inventory_item_code ||
               ',' || fmv.crop_unit_code ||
               ',' || fmv.average_price ||
               ',' || fmv.percent_variance fmv_export
        from farms.farm_fair_market_values fmv
        join farms.farm_inventory_item_codes iic on iic.inventory_item_code = fmv.inventory_item_code
        join farms.farm_municipality_codes mc on mc.municipality_code = fmv.municipality_code
        join farms.farm_crop_unit_codes cuc on cuc.crop_unit_code = fmv.crop_unit_code
        where fmv.expiry_date is null
        and fmv.program_year = in_program_year
        and (
            (in_inventory_code_filter is null and in_inventory_desc_filter is null) or
            (in_inventory_code_filter is not null and fmv.inventory_item_code like in_inventory_code_filter || '%') or
            (in_inventory_desc_filter is not null and lower(iic.description) like lower(in_inventory_desc_filter || '%'))
        )
        and (
            (in_municipality_desc_filter is null) or
            (in_municipality_desc_filter is not null and lower(mc.description) = lower(in_municipality_desc_filter || '%'))
        )
        and (
            (in_unit_desc_filter is null) or
            (in_unit_desc_filter is not null and lower(cuc.description) like lower(in_unit_desc_filter || '%'))
        )
        order by to_number(fmv.inventory_item_code),
                 fmv.period,
                 to_number(fmv.municipality_code);
    return cur;
end;
$$;
