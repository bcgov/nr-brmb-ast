create or replace function farms_import_pkg.append_imp(
    in in_version_id numeric,
    in in_msg varchar
)
returns numeric
language plpgsql
as $$
declare
    id numeric;
begin
    select nextval('farms.farm_ilg_seq')
    into id;

    insert into farms.farm_import_logs (
        import_version_id,
        import_log_id,
        log_message
    ) values (
        in_version_id,
        id,
        in_msg
    );

    return id;
end;
$$;
