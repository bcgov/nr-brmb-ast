create or replace function farms_import_pkg.commodities(
    in in_version_id numeric,
    in in_user varchar
)
returns numeric
language plpgsql
as $$
declare
    iic_cursor cursor for
        select z.inventory_code,
               -- descriptions should be unique in CD; may have more than one in Z so pick one
               max(z.inventory_description) new_description,
               max(cd.description) old_description
        from farms.z29_inventory_code_reference z
        left outer join farms.inventory_item_code cd on z.inventory_code::varchar = cd.inventory_item_code
        where cd.inventory_item_code is null
        group by z.inventory_code;
    iic_val record;

    icc_cursor cursor for
        select z.inventory_type_code,
               -- descriptions should be unique in CD; may have more than one in Z so pick one
               max(z.inventory_type_description) new_description,
               max(cd.description) old_description
        from farms.z29_inventory_code_reference z
        left outer join farms.inventory_class_code cd on z.inventory_type_code::varchar = cd.inventory_class_code
        where cd.inventory_class_code is null
        group by z.inventory_type_code;
    icc_val record;

    igc_cursor cursor for
        select z.inventory_group_code,
               -- descriptions should be unique in CD; may have more than one in Z so pick one
               max(z.inventory_group_description) new_description,
               max(cd.description) old_description
        from farms.z29_inventory_code_reference z
        left outer join farms.inventory_group_code cd on z.inventory_group_code::varchar = cd.inventory_group_code
        where cd.inventory_group_code is null
        group by z.inventory_group_code;
    igc_val record;

    xref_cursor cursor for
        select z.inventory_code,
               z.inventory_type_code,
               z.inventory_group_code,
               z.market_commodity_indicator,
               x.market_commodity_indicator as old_market_commodity_indicator,
               x.agristabilty_commodity_xref_id
        from farms.z29_inventory_code_reference z
        left outer join farms.agristabilty_commodity_xref x on z.inventory_code::varchar = x.inventory_item_code
                                                           and z.inventory_type_code::varchar = x.inventory_class_code
                                                           and (z.inventory_group_code is null
                                                                or z.inventory_group_code::varchar = x.inventory_group_code);
    xref_val record;

    opened numeric := 0;
    closed numeric := 0;
    exp_date date := null;

    v_inventory_code farms.z29_inventory_code_reference.inventory_code%type := null;
    v_inventory_type_code farms.z29_inventory_code_reference.inventory_type_code%type := null;
    v_inventory_group_code farms.z29_inventory_code_reference.inventory_group_code%type := null;
    v_program_year farms.program_year.year%type;

    errors numeric := 0;
begin
    -- type casts will not occur because they have been completed

    for iic_val in iic_cursor
    loop
        v_inventory_code := iic_val.inventory_code;
        if opened < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '<COMMODITIES>');
            opened := 1;
        end if;

        begin
            if iic_val.inventory_code is not null then
                if iic_val.old_description is null then
                    call farms_import_pkg.append_imp1(in_version_id,
                        '<COMMODITYCODE table="inventory_code" code="' ||
                        farms_import_pkg.scrub(iic_val.inventory_code) || '" action="add">' ||
                        '<ATTR name="description" new="' ||
                        farms_import_pkg.scrub(iic_val.new_description) ||
                        '"/></COMMODITYCODE>');

                    exp_date := to_date('12/31/9999', 'MM/DD/YYYY');

                    select py.year program_year
                    into v_program_year
                    from farms.program_year_version pyv
                    join farms.program_year py on py.program_year_id = pyv.program_year_id
                    where pyv.import_version_id = in_version_id;

                    insert into farms.inventory_item_code(
                        inventory_item_code,
                        description,
                        effective_date,
                        expiry_date,
                        revision_count,
                        create_user,
                        create_date,
                        update_user,
                        update_date
                    ) values (
                        iic_val.inventory_code::varchar,
                        iic_val.new_description,
                        current_date,
                        exp_date,
                        1,
                        in_user,
                        current_timestamp,
                        in_user,
                        current_timestamp
                    );

                    insert into farms.inventory_item_attribute(
                        inventory_item_attribute_id,
                        inventory_item_code,
                        rollup_inventory_item_code,
                        create_user,
                        create_date,
                        update_user,
                        update_date,
                        revision_count
                    ) values (
                        nextval('farms.seq_iia'),
                        iic_val.inventory_code::varchar,
                        null,
                        in_user,
                        current_timestamp,
                        in_user,
                        current_timestamp,
                        1
                    );

                    insert into farms.inventory_item_detail(
                        inventory_item_detail_id,
                        program_year,
                        eligibility_indicator,
                        inventory_item_code,
                        revision_count,
                        create_user,
                        create_date,
                        update_user,
                        update_date
                    )
                    select
                        nextval('farms.seq_iid'),
                        y.program_year,
                        'Y',
                        iic_val.inventory_code::varchar,
                        1,
                        in_user,
                        current_timestamp,
                        in_user,
                        current_timestamp
                    from (select distinct year program_year from farms.program_year) y;
                end if;
            end if;
        exception
            when others then
                errors := errors + 1;

                call farms_import_pkg.append_imp1(in_version_id,
                    '<COMMODITYCODE table="inventory_code" code="' ||
                    farms_import_pkg.scrub(iic_val.inventory_code) ||
                    '" action="error"><ERROR>' ||
                    farms_import_pkg.scrub(farms_error_pkg.codify_inventory_code(sqlerrm,
                                                                                v_inventory_code::varchar,
                                                                                exp_date)) ||
                    '</ERROR></COMMODITYCODE>');
        end;
    end loop;

    for iic_val in icc_cursor
    loop
        v_inventory_type_code := iic_val.inventory_type_code;

        if opened < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '<COMMODITIES>');
            opened := 1;
        end if;

        begin
            if iic_val.inventory_type_code is not null then
                if iic_val.old_description is null then
                    call farms_import_pkg.append_imp1(in_version_id,
                        '<COMMODITYCODE table="inventory_type_code" code="' ||
                        farms_import_pkg.scrub(iic_val.inventory_type_code) ||
                        '" action="add">' ||
                        '<ATTR name="description" new="' ||
                        farms_import_pkg.scrub(iic_val.new_description) ||
                        '"/></COMMODITYCODE>');

                    exp_date := to_date('12/31/9999', 'MM/DD/YYYY');

                    insert into farms.inventory_class_code(
                        inventory_class_code,
                        description,
                        effective_date,
                        expiry_date,
                        revision_count,
                        create_user,
                        create_date,
                        update_user,
                        update_date
                    ) values (
                        iic_val.inventory_type_code::varchar,
                        iic_val.new_description,
                        current_date,
                        exp_date,
                        1,
                        in_user,
                        current_timestamp,
                        in_user,
                        current_timestamp
                    );
                end if;
            end if;
        exception
            when others then
                errors := errors + 1;

                call farms_import_pkg.append_imp1(in_version_id,
                    '<COMMODITYCODE table="inventory_type_code" code="' ||
                    farms_import_pkg.scrub(iic_val.inventory_type_code) ||
                    '" action="error"><ERROR>' ||
                    farms_import_pkg.scrub(farms_error_pkg.codify_inventory_code(sqlerrm,
                                                                                v_inventory_type_code::varchar,
                                                                                exp_date)) ||
                    '</ERROR></COMMODITYCODE>');
        end;
    end loop;

    for igc_val in igc_cursor
    loop
        v_inventory_group_code := igc_val.inventory_group_code;

        if opened < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '<COMMODITIES>');
            opened := 1;
        end if;

        begin
            if igc_val.inventory_group_code is not null then
                if igc_val.old_description is null then
                    call farms_import_pkg.append_imp1(in_version_id,
                        '<COMMODITYCODE table="inventory_group_code" code="' ||
                        farms_import_pkg.scrub(igc_val.inventory_group_code) ||
                        '" action="add">' ||
                        '<ATTR name="description" new="' ||
                        farms_import_pkg.scrub(igc_val.new_description) ||
                        '"/></COMMODITYCODE>');

                    exp_date := to_date('12/31/9999', 'MM/DD/YYYY');

                    insert into farms.inventory_group_code(
                        inventory_group_code,
                        description,
                        effective_date,
                        expiry_date,
                        revision_count,
                        create_user,
                        create_date,
                        update_user,
                        update_date
                    ) values (
                        igc_val.inventory_group_code::varchar,
                        igc_val.new_description,
                        current_date,
                        exp_date,
                        1,
                        in_user,
                        current_timestamp,
                        in_user,
                        current_timestamp
                    );
                end if;
            end if;
        exception
            when others then
                errors := errors + 1;

                call farms_import_pkg.append_imp1(in_version_id,
                    '<COMMODITYCODE table="inventory_group_code" code="' ||
                    farms_import_pkg.scrub(igc_val.inventory_group_code) ||
                    '" action="update"><ERROR>' ||
                    farms_import_pkg.scrub(farms_error_pkg.codify_inventory_code(sqlerrm,
                                                                                v_inventory_group_code::varchar,
                                                                                exp_date)) ||
                    '</ERROR></COMMODITYCODE>');
        end;
    end loop;

    for xref_val in xref_cursor
    loop
        if opened < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '<COMMODITIES>');
            opened := 1;
        end if;

        begin
            if xref_val.agristabilty_commodity_xref_id is null then

                exp_date := to_date('12/31/9999', 'MM/DD/YYYY');

                insert into farms.agristabilty_commodity_xref(
                    agristabilty_commodity_xref_id,
                    inventory_item_code,
                    inventory_group_code,
                    inventory_class_code,
                    market_commodity_indicator,
                    revision_count,
                    create_user,
                    create_date,
                    update_user,
                    update_date
                ) values (
                    nextval('farms.seq_acx'),
                    xref_val.inventory_code::varchar,
                    xref_val.inventory_group_code::varchar,
                    xref_val.inventory_type_code::varchar,
                    xref_val.market_commodity_indicator,
                    1,
                    in_user,
                    current_timestamp,
                    in_user,
                    current_timestamp
                );
            else
                if xref_val.old_market_commodity_indicator <> xref_val.market_commodity_indicator then
                    -- only time we get here is when the three code are found
                    update farms.agristabilty_commodity_xref
                    set market_commodity_indicator = xref_val.market_commodity_indicator,
                        revision_count = revision_count + 1,
                        update_date = current_timestamp,
                        update_user = in_user
                    where agristabilty_commodity_xref_id = xref_val.agristabilty_commodity_xref_id;
                end if;
            end if;
        end;
    end loop;

    if opened > 0 then
        call farms_import_pkg.append_imp1(in_version_id, '</COMMODITIES>');
        closed := 1;
    end if;

    return 0;
exception
    when others then
        if opened > 0 and closed < 1 then
            call farms_import_pkg.append_imp1(in_version_id, '</COMMODITIES>');
        end if;

        call farms_import_pkg.append_imp1(in_version_id, -- general errors
            '<ERROR>' || farms_import_pkg.scrub(farms_error_pkg.codify(sqlerrm)) ||
            '</ERROR>');

        return 1;
end;
$$;
