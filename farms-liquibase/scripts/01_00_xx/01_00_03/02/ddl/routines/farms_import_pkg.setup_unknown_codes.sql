create or replace procedure farms_import_pkg.setup_unknown_codes()
language plpgsql
as $$
declare
    Unknown constant varchar := '-1';
    cnt numeric;
    dt date;

    code_ins cursor for
        select cds.inventory_class_code
        from farms.inventory_class_code cds
        left outer join farms.agristabilty_commodity_xref x on x.inventory_class_code = cds.inventory_class_code
                                                            and x.inventory_item_code = Unknown
        where x.agristabilty_commodity_xref_id is null;
begin
    dt := to_date('31/12/9999', 'DD/MM/YYYY');

    select count(*)
    into cnt
    from farms.inventory_class_code
    where inventory_class_code = Unknown;

    if cnt < 1 then
        insert into farms.inventory_class_code (
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
            Unknown,
            'UNKNOWN',
            current_date,
            dt,
            1,
            'INIT',
            current_timestamp,
            'INIT',
            current_timestamp
        );
    end if;

    select count(*)
    into cnt
    from farms.inventory_group_code
    where inventory_group_code = Unknown;

    if cnt < 1 then
        insert into farms.inventory_group_code (
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
            Unknown,
            'UNKNOWN',
            current_date,
            dt,
            1,
            'INIT',
            current_timestamp,
            'INIT',
            current_timestamp
        );
    end if;

    for cd in code_ins
    loop
        insert into farms.agristabilty_commodity_xref (
            agristabilty_commodity_xref_id,
            inventory_item_code,
            inventory_group_code,
            inventory_class_code,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        ) values (
            nextval('farms.seq_acx'),
            Unknown,
            Unknown,
            cd.inventory_class_code,
            1,
            'INIT',
            current_timestamp,
            'INIT',
            current_timestamp
        );
    end loop;
end;
$$;
