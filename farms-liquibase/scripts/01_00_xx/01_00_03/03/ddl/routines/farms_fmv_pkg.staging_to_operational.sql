create or replace procedure farms_fmv_pkg.staging_to_operational(
   in in_import_version_id numeric,
   in in_user varchar
)
language plpgsql
as $$
declare
    c_staging cursor for
        select z.line_number,
               z.program_year,
               z.period,
               round(z.average_price, 2) average_price,
               round(z.percent_variance, 2) percent_variance,
               z.municipality_code,
               (case
                   when x.inventory_class_code = '2' then farms_codes_read_pkg.get_livestock_unit_code(z.inventory_item_code)
                   else z.crop_unit_code
               end) crop_unit_code,
               z.inventory_item_code
        from farms.zfmv_fair_market_value z
        join farms.agristabilty_commodity_xref x on x.inventory_item_code = z.inventory_item_code
        where x.inventory_class_code in ('1', '2')
        union all
        select z.line_number,
               z.program_year,
               z.period,
               round(z.average_price / cucf.conversion_factor, 2) average_price,
               round(z.percent_variance, 2) percent_variance,
               z.municipality_code,
               (case
                   when x.inventory_class_code = '2' then farms_codes_read_pkg.get_livestock_unit_code(z.inventory_item_code)
                   else cucf.target_crop_unit_code
               end) crop_unit_code,
               z.inventory_item_code
        from farms.zfmv_fair_market_value z
        join farms.agristabilty_commodity_xref x on x.inventory_item_code = z.inventory_item_code
        join farms.crop_unit_conversion_factor cucf on cucf.inventory_item_code = z.inventory_item_code
        where x.inventory_class_code in ('1', '2')
        order by line_number;
    v_staging record;

    c_operational cursor (
        p_program_year numeric,
        p_period numeric,
        p_inventory_item_code varchar,
        p_municipality_code varchar,
        p_crop_unit_code varchar
    ) for
        select fair_market_value_id,
               average_price,
               percent_variance,
               revision_count
        from farms.fair_market_value
        where program_year = p_program_year
        and period = p_period
        and inventory_item_code = p_inventory_item_code
        and municipality_code = p_municipality_code
        and crop_unit_code = p_crop_unit_code
        and expiry_date is null;
    v_operational record;

    v_num_staging_rows numeric := 0;
    v_num_added numeric := 0;
    v_num_updated numeric := 0;
    v_xml varchar(2000);
begin

    for v_staging in c_staging
    loop
        v_num_staging_rows := v_num_staging_rows + 1;

        open c_operational(
            v_staging.program_year,
            v_staging.period,
            v_staging.inventory_item_code,
            v_staging.municipality_code,
            v_staging.crop_unit_code);
        fetch c_operational into v_operational;

        if found then
            if v_operational.average_price = v_staging.average_price and v_operational.percent_variance = v_staging.percent_variance then
                close c_operational;
                continue;
            else
                -- expire the old value
                update farms.fair_market_value
                set expiry_date = current_date,
                    revision_count = v_operational.revision_count + 1,
                    create_user = in_user,
                    create_date = current_timestamp
                where fair_market_value_id = v_operational.fair_market_value_id;

                v_num_updated := v_num_updated + 1;
            end if;
        else
            v_num_added := v_num_added + 1;
        end if;

        insert into farms.fair_market_value (
            fair_market_value_id,
            program_year,
            period,
            average_price,
            percent_variance,
            expiry_date,
            inventory_item_code,
            municipality_code,
            crop_unit_code,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        ) values (
            nextval('farms.seq_fmv'),
            v_staging.program_year,
            v_staging.period,
            v_staging.average_price,
            v_staging.percent_variance,
            null,
            v_staging.inventory_item_code,
            v_staging.municipality_code,
            v_staging.crop_unit_code,
            1,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );

        close c_operational;
    end loop;

    v_xml := '<IMPORT_LOG><NUM_TOTAL>' || v_num_staging_rows ||
        '</NUM_TOTAL><NUM_NEW>' || v_num_added ||
        '</NUM_NEW><NUM_UPDATED>' || v_num_updated ||
        '</NUM_UPDATED></IMPORT_LOG>';

    call farms_version_pkg.import_complete(
        in_import_version_id,
        v_xml,
        in_user
    );
end;
$$;
