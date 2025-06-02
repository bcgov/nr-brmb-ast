create or replace function farms_codes_write_pkg.in_use_participant_prof_code(
    in in_participant_profile_code farms.participant_profile_code.participant_profile_code%type
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
            from farms.program_year_version t
            where t.participant_profile_code = in_participant_profile_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
