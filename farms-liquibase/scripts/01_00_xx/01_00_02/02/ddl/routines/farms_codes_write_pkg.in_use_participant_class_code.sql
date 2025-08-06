create or replace function farms_codes_write_pkg.in_use_participant_class_code(
    in in_participant_class_code farms.farm_participant_class_codes.participant_class_code%type
)
returns numeric
language plpgsql
as $$
declare
    v_in_use numeric;
begin

    select (case
        when exists(
            select null
            from farms.farm_agristability_clients t
            where t.participant_class_code = in_participant_class_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
