create or replace procedure farms_codes_write_pkg.delete_farm_type_code(
   in in_farm_type_code farms.farm_type_code.farm_type_code%type,
   in in_revision_count farms.farm_type_code.revision_count%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    v_in_use := farms_codes_write_pkg.in_use_farm_type_code(in_farm_type_code);

    if v_in_use = 0 then

        delete from farms.farm_type_code c
        where c.farm_type_code = in_farm_type_code
        and c.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;
    end if;

end;
$$;
