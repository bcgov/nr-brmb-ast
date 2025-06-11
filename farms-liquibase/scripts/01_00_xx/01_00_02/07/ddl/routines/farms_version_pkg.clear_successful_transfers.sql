create or replace procedure farms_version_pkg.clear_successful_transfers()
language plpgsql
as $$
begin
    delete from farms.import_version iv
    where iv.import_state_code = 'IC'
    and iv.import_class_code in ('XENROL', 'XSTATE', 'XCONTACT');
end;
$$;
