create or replace procedure farms_bpu_pkg.staging_to_operational(
   in in_import_version_id numeric,
   in in_user varchar
)
language plpgsql
as $$
declare
    c_staging cursor for
        select *
        from farms.farm_zbpu_benchmark_per_units
        order by line_number;
    v_staging record;

    c_operational cursor (p_program_year numeric, p_inv_code varchar, p_municipality_code varchar) for
        select benchmark_per_unit_id,
            revision_count
        from farms.farm_benchmark_per_units
        where program_year = p_program_year
        and (
            (inventory_item_code is not null and inventory_item_code = p_inv_code)
            or
            (structure_group_code is not null and structure_group_code = p_inv_code)
        )
        and municipality_code = p_municipality_code
        and expiry_date is null;
    v_operational record;

    v_num_staging_rows numeric;
    v_num_added numeric := 0;
    v_num_updated numeric := 0;
    v_values_updated numeric := 0;
    v_xml varchar(2000);
    v_margin_data numeric[] := ARRAY[0, 0, 0, 0, 0, 0];
    v_expense_data numeric[] := ARRAY[0, 0, 0, 0, 0, 0];
    v_year numeric;
    v_inventory_item_code varchar(10);
    v_structure_group_code varchar(10);
    v_number_margin_updates integer := 0;
    v_number_expense_updates integer := 0;
begin
    select count(line_number)
    into v_num_staging_rows
    from farms.farm_zbpu_benchmark_per_units;

    for v_staging in c_staging
    loop
        v_margin_data[1] := v_staging.year_minus_1_margin;
        v_margin_data[2] := v_staging.year_minus_2_margin;
        v_margin_data[3] := v_staging.year_minus_3_margin;
        v_margin_data[4] := v_staging.year_minus_4_margin;
        v_margin_data[5] := v_staging.year_minus_5_margin;
        v_margin_data[6] := v_staging.year_minus_6_margin;
        v_expense_data[1] := v_staging.year_minus_1_expense;
        v_expense_data[2] := v_staging.year_minus_2_expense;
        v_expense_data[3] := v_staging.year_minus_3_expense;
        v_expense_data[4] := v_staging.year_minus_4_expense;
        v_expense_data[5] := v_staging.year_minus_5_expense;
        v_expense_data[6] := v_staging.year_minus_6_expense;

        open c_operational(
            v_staging.program_year,
            v_staging.inventory_item_code,
            v_staging.municipality_code);
        fetch c_operational into v_operational;

        if found then
            v_number_margin_updates := farms_bpu_pkg.margin_data_differences(
                v_margin_data,
                v_operational.benchmark_per_unit_id,
                v_staging.program_year
            );
            v_number_expense_updates := farms_bpu_pkg.expense_data_differences(
                v_expense_data,
                v_operational.benchmark_per_unit_id,
                v_staging.program_year
            );

            if v_number_margin_updates = 0 and v_number_expense_updates = 0 then
                close c_operational;
                continue;
            else
                update farms.farm_benchmark_per_units
                set expiry_date = current_date,
                    revision_count = v_operational.revision_count + 1,
                    who_updated = in_user,
                    when_updated = current_timestamp
                where benchmark_per_unit_id = v_operational.benchmark_per_unit_id;

                v_num_updated := v_num_updated + 1;
                v_values_updated := v_values_updated + v_number_margin_updates + v_number_expense_updates;
            end if;
        else
            v_num_added := v_num_added + 1;
            v_values_updated := v_values_updated + array_length(v_margin_data, 1) + array_length(v_expense_data, 1);
        end if;

        if farms_bpu_pkg.is_group_code(v_staging.inventory_item_code) then
            v_structure_group_code := v_staging.inventory_item_code;
            v_inventory_item_code := null;
        else
            v_structure_group_code := null;
            v_inventory_item_code := v_staging.inventory_item_code;
        end if;

        insert into farms.farm_benchmark_per_units (
            benchmark_per_unit_id,
            program_year,
            unit_comment,
            expiry_date,
            municipality_code,
            inventory_item_code,
            structure_group_code,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        ) values (
            nextval('farms.farm_bpu_seq'),
            v_staging.program_year,
            v_staging.unit_comment,
            null,
            v_staging.municipality_code,
            v_inventory_item_code,
            v_structure_group_code,
            1,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );

        for ref_year_index in 1..6
        loop
            v_year := v_staging.program_year - ref_year_index;

            insert into farms.farm_benchmark_years (
                benchmark_year,
                average_margin,
                average_expense,
                benchmark_per_unit_id,
                revision_count,
                who_created,
                when_created,
                who_updated,
                when_updated
            ) values (
                v_year,
                v_margin_data[ref_year_index],
                v_expense_data[ref_year_index],
                currval('farms.farm_bpu_seq'),
                1,
                in_user,
                current_timestamp,
                in_user,
                current_timestamp
            );
        end loop;

        close c_operational;
    end loop;

    v_xml := '<IMPORT_LOG><NUM_TOTAL>' || v_num_staging_rows ||
        '</NUM_TOTAL><NUM_NEW>' || v_num_added ||
        '</NUM_NEW><NUM_UPDATED>' || v_num_updated ||
        '</NUM_UPDATED><NUM_VALUES_UPDATED>' || v_values_updated ||
        '</NUM_VALUES_UPDATED></IMPORT_LOG>';

    update farms.farm_import_versions
    set import_audit_info = v_xml,
        import_state_code = 'IC'
    where import_version_id = in_import_version_id;
end;
$$;
