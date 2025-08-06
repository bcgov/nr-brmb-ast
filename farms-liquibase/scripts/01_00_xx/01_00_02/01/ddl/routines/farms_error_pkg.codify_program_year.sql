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
    if msg like '%uk_py_aci_y%' then
        return 'Program Year/Client/Year combination is not unique';
    elsif msg like '%fk_pyv_mc%' then
        return 'The specified Municipality Code (' || municipality_code || ') was not found for this Program year Version';
    elsif msg like '%fk_pyv_ppc%' then
        return 'The specified Participant Profile Code (' || participant_profile_code || ') was not found for this Program year Version';
    elsif msg like '%fk_pyv_fsc%' then
        return 'The specified Federal Status Code (' || federal_status_code || ') was not found for this Program year Version';
    elsif msg like '%fk_pyv_iv%' then
        return 'The specified Import Version was not found for this Program year Version';
    elsif msg like '%fk_pyv_py%' then
        return 'The specified Program Year was not found for this Program year Version';
    else
        return farms_error_pkg.codify(msg);
    end if;
end;
$$;
