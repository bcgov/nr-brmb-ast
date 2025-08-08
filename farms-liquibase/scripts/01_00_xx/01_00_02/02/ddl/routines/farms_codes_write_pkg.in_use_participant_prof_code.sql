create or replace function farms_codes_write_pkg.in_use_participant_prof_code(
    in in_participant_profile_code farms.farm_participant_profile_codes.participant_profile_code%type
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
            from farms.farm_program_year_versions t
            where t.participant_profile_code = in_participant_profile_code
        ) then 1
        else 0
    end) into v_in_use;

    return v_in_use;
end;
$$;
