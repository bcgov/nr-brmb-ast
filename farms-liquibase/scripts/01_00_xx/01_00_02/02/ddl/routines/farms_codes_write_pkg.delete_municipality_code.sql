create or replace procedure farms_codes_write_pkg.delete_municipality_code(
   in in_municipality_code farms.municipality_code.municipality_code%type,
   in in_revision_count farms.municipality_code.revision_count%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    v_in_use := farms_codes_write_pkg.in_use_municipality_code(in_municipality_code);

    if v_in_use = 0 then

        delete from farms.office_municipality_xref x
        where x.municipality_code = in_municipality_code;

        delete from farms.municipality_code c
        where c.municipality_code = in_municipality_code
          and c.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;
    end if;

end;
$$;
