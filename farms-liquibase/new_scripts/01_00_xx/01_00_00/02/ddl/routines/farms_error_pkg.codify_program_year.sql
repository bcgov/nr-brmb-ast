create or replace function farms_error_pkg.codify_program_year(
    in msg varchar,
    in program_year farms.farm_program_years.year%type,
    in agristability_client_id farms.farm_program_years.agristability_client_id%type,
    in participant_profile_code farms.farm_program_year_versions.participant_profile_code%type,
    in federal_status_code farms.farm_program_year_versions.federal_status_code%type,
    in municipality_code farms.farm_program_year_versions.municipality_code%type
)
returns varchar
language plpgsql
as $$
begin
    if msg like '%farm_program_years_agristability_client_id_year_key%' then
        return 'Program Year/Client/Year combination is not unique';
    elsif msg like '%farm_pyv_farm_mc_fk%' then
        return 'The specified Municipality Code (' || municipality_code || ') was not found for this Program year Version';
    elsif msg like '%farm_pyv_farm_ppc_fk%' then
        return 'The specified Participant Profile Code (' || participant_profile_code || ') was not found for this Program year Version';
    elsif msg like '%farm_pyv_farm_fsc_fk%' then
        return 'The specified Federal Status Code (' || federal_status_code || ') was not found for this Program year Version';
    elsif msg like '%farm_pyv_farm_iv_fk%' then
        return 'The specified Import Version was not found for this Program year Version';
    elsif msg like '%farm_pyv_farm_py_fk%' then
        return 'The specified Program Year was not found for this Program year Version';
    else
        return farms_error_pkg.codify(msg);
    end if;
end;
$$;
