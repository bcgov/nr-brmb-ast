create or replace procedure farms_ivpr_pkg.staging_to_operational(
   in in_import_version_id numeric,
   in in_user varchar
)
language plpgsql
as $$
declare
    c_staging cursor for
        select *
        from farms.farm_zivpr_iv_premium_rates
        order by line_number;
    v_staging record;

    c_operational cursor (
        p_inv_code varchar,
        p_program_year numeric
    ) for
        select inventory_item_detail_id,
               revision_count
        from farms.farm_inventory_item_details
        where inventory_item_code is not null
        and inventory_item_code = p_inv_code
        and program_year = p_program_year;
    v_operational record;

    v_num_staging_rows numeric := 0;
    v_num_added numeric := 0;
    v_num_updated numeric := 0;
    v_xml varchar(2000);
begin

    select count(line_number)
    into v_num_staging_rows
    from farms.farm_zivpr_iv_premium_rates;

    for v_staging in c_staging
    loop
        open c_operational(
            v_staging.inventory_item_code,
            v_staging.program_year);
        loop
            fetch c_operational into v_operational;

            update farms.farm_inventory_item_details
            set insurable_value = v_staging.insurable_value,
                premium_rate = v_staging.premium_rate,
                revision_count = v_operational.revision_count + 1,
                who_updated = in_user,
                when_updated = current_timestamp
            where inventory_item_detail_id = v_operational.inventory_item_detail_id;

            v_num_updated := v_num_updated + 1;

            exit when not found;
        end loop;

        close c_operational;
    end loop;

    v_xml := '<IMPORT_LOG><NUM_TOTAL>' || v_num_staging_rows ||
        '</NUM_TOTAL><NUM_UPDATED>' || v_num_updated ||
        '</NUM_UPDATED></IMPORT_LOG>';

    call farms_version_pkg.import_complete(
        in_import_version_id,
        v_xml,
        in_user
    );
end;
$$;
