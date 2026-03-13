create or replace function farms_calculator_pkg.read_all_partners()
returns table(
    partnership_pin     farms.farm_farming_operatin_prtnrs.partnership_pin%type,
    partner_percent     farms.farm_farming_operatin_prtnrs.partner_percent%type,
    first_name          farms.farm_farming_operatin_prtnrs.first_name%type,
    last_name           farms.farm_farming_operatin_prtnrs.last_name%type,
    corp_name           farms.farm_farming_operatin_prtnrs.corp_name%type
)
language sql
as $$
    select distinct fop.partnership_pin,
           fop.partner_percent,
           upper(fop.first_name) first_name,
           upper(fop.last_name) last_name,
           upper(fop.corp_name) corp_name
    from farms.farm_program_years py
    join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
    join farms.farm_farming_operations fo on fo.program_year_version_id = pyv.program_year_version_id
    join farms.farm_farming_operatin_prtnrs fop on fop.farming_operation_id = fo.farming_operation_id
    where py.year >= (to_char(current_timestamp, 'YYYY')::int - 6) -- only check current program year and its reference years
    order by fop.partnership_pin,
             corp_name,
             last_name,
             first_name;
$$;
