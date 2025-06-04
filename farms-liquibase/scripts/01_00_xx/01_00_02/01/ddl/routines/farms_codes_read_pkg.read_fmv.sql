create or replace function farms_codes_read_pkg.read_fmv(
    in in_program_year farms.fair_market_value.program_year%type,
    in in_inventory_item_code farms.fair_market_value.inventory_item_code%type,
    in in_municipality_code farms.fair_market_value.municipality_code%type,
    in in_crop_unit_code farms.fair_market_value.crop_unit_code%type
)
returns refcursor
language plpgsql
as $$
declare
    cur refcursor;
begin
    open cur for
        select fmv.program_year,
               fmv.inventory_item_code,
               fmv.municipality_code,
               fmv.crop_unit_code,
               iic.description inventory_item_code_description,
               mc.description municipality_code_description,
               cuc.description crop_unit_code_description,
               cud.crop_unit_code default_crop_unit_description,
               fmv.fair_market_value_id,
               fmv.period,
               fmv.average_price,
               fmv.percent_variance,
               fmv.revision_count
        from farms.fair_market_value fmv
        join farms.inventory_item_code iic on iic.inventory_item_code = fmv.inventory_item_code
        join farms.municipality_code mc on mc.municipality_code = fmv.municipality_code
        join farms.crop_unit_code cuc on cuc.crop_unit_code = fmv.crop_unit_code
        left outer join farms.crop_unit_default cud on cud.inventory_item_code = fmv.inventory_item_code
        left outer join farms.crop_unit_code cudc on cudc.crop_unit_code = cud.crop_unit_code
        where fmv.program_year = in_program_year
        and (in_inventory_item_code is null or fmv.inventory_item_code = in_inventory_item_code)
        and (in_municipality_code is null or fmv.municipality_code = in_municipality_code)
        and (in_crop_unit_code is null or fmv.crop_unit_code = in_crop_unit_code)
        and (fmv.expiry_date is null or fmv.expiry_date > current_date)
        /* Read relies on this ordering to construct FMV objects. */
        group by lower(iic.description),
                 fmv.inventory_item_code,
                 lower(mc.description),
                 fmv.municipality_code,
                 lower(cuc.description),
                 fmv.crop_unit_code,
                 fmv.period;
    return cur;
end;
$$;
