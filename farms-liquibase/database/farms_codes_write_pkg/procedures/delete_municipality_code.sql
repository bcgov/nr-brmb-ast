create or replace procedure farms_codes_write_pkg.delete_municipality_code(
   in in_municipality_code farms.farm_municipality_codes.municipality_code%type,
   in in_revision_count farms.farm_municipality_codes.revision_count%type
)
language plpgsql
as $$
declare
    v_rows_affected  bigint := null;
    v_in_use numeric;
begin

    v_in_use := farms_codes_write_pkg.in_use_municipality_code(in_municipality_code);

    if v_in_use = 0 then

        delete from farms.farm_office_municipality_xref x
        where x.municipality_code = in_municipality_code;

        delete from farms.farm_municipality_codes c
        where c.municipality_code = in_municipality_code
          and c.revision_count = in_revision_count;

        get diagnostics v_rows_affected = row_count;
        if v_rows_affected = 0 then
            raise exception 'Invalid revision count';
        end if;
    end if;

end;
$$;
