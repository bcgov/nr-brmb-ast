create or replace function farms_codes_write_pkg.in_use_participant_class_code(
    in in_participant_class_code farms.participant_class_code.participant_class_code%type
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
            from farms.agristability_client t
            where t.participant_class_code = in_participant_class_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
