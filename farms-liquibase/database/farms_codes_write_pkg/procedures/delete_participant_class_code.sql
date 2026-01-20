create or replace procedure farms_codes_write_pkg.delete_participant_class_code(
   in in_participant_class_code farms.farm_participant_class_codes.participant_class_code%type,
   in in_revision_count farms.farm_participant_class_codes.revision_count%type
)
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    v_in_use := farms_codes_write_pkg.in_use_participant_class_code(in_participant_class_code);

    if v_in_use = 0 then

        delete from farms.farm_participant_class_codes c
        where c.participant_class_code = in_participant_class_code
        and c.revision_count = in_revision_count;

        if sql%rowcount <> 1 then
            raise exception 'Invalid revision count';
        end if;
    end if;
end;
$$;
