create or replace procedure farms_version_pkg.perform_import(
   in in_version_id numeric,
   in in_user varchar
)
language plpgsql
as $$
declare
    r numeric;
begin
    r := farms_import_pkg.run(in_version_id, in_user);
    r := coalesce(r, 0);
    update farms.import_version iv
    set iv.import_date = current_date,
        iv.import_state_code = (case when r = 0 then 'IC' else 'IPC' end),
        iv.revision_count = iv.revision_count + 1,
        iv.update_user = in_user,
        iv.update_date = current_timestamp,
    where iv.import_version_id = in_version_id;
exception
    when others then
        update farms.import_version iv
        set iv.import_date = current_date,
            iv.import_state_code = 'IF',
            iv.revision_count = iv.revision_count + 1,
            iv.update_user = in_user,
            iv.update_date = current_timestamp,
        where iv.import_version_id = in_version_id;
        raise;
end;
$$;
