create or replace procedure farms_codes_write_pkg.delete_federal_status_code(
   in in_federal_status_code farms.federal_status_code.federal_status_code%type,
   in in_revision_count farms.federal_status_code.revision_count%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    v_in_use := farms_codes_write_pkg.in_use_federal_status_code(in_federal_status_code);

    if v_in_use = 0 then

        delete from farms.federal_status_code c
        where c.federal_status_code = in_federal_status_code
        and c.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;
    end if;
end;
$$;
