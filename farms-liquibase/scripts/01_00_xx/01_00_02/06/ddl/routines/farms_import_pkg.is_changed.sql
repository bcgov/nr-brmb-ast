create or replace function farms_import_pkg.is_changed(
    in in_program_year_id farms.program_year.program_year_id%type,
    in in_program_year_vrsn_prev_id farms.program_year_version.program_year_version_id%type
)
returns integer
language plpgsql
as $$
declare
    t boolean := null;
begin
    t := farms_import_pkg.is_changed_bool(in_program_year_id, in_program_year_vrsn_prev_id);
    if t is null then
        return null;
    elsif t then
        return 1;
    else
        return 0;
    end if;
end;
$$;
