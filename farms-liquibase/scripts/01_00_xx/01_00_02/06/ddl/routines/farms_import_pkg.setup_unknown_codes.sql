create or replace procedure farms_import_pkg.setup_unknown_codes()
language plpgsql
as $$
declare
    Unknown constant varchar := '-1';
    cnt numeric;
    dt date;

    code_ins cursor for
        select cds.inventory_class_code
        from farms.farm_inventory_class_codes codes
        left outer join farms.farm_agristabilty_cmmdty_xref x on x.inventory_class_code = cds.inventory_class_code
                                                            and x.inventory_item_code = Unknown
        where x.agristabilty_cmmdty_xref_id is null;
begin
    dt := to_date('31/12/9999', 'DD/MM/YYYY');

    select count(*)
    into cnt
    from farms.farm_inventory_class_codes
    where inventory_class_code = Unknown;

    if cnt < 1 then
        insert into farms.farm_inventory_class_codes (
            inventory_class_code,
            description,
            established_date,
            expiry_date,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
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
    from farms.farm_inventory_group_codes
    where inventory_group_code = Unknown;

    if cnt < 1 then
        insert into farms.farm_inventory_group_codes (
            inventory_group_code,
            description,
            established_date,
            expiry_date,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
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
        insert into farms.farm_agristabilty_cmmdty_xref (
            agristabilty_cmmdty_xref_id,
            inventory_item_code,
            inventory_group_code,
            inventory_class_code,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        ) values (
            nextval('farms.farm_acx_seq'),
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
