create or replace procedure farms_codes_write_pkg.delete_crop_unit_code(
   in in_crop_unit_code farms.farm_crop_unit_codes.crop_unit_code%type,
   in in_revision_count farms.farm_crop_unit_codes.revision_count%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    v_in_use := farms_codes_write_pkg.in_use_crop_unit_code(in_crop_unit_code);

    if v_in_use = 0 then

        delete from farms.farm_crop_unit_codes c
        where c.crop_unit_code = in_crop_unit_code
        and c.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;
    end if;

end;
$$;
