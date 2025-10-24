create or replace function farms_calculator_pkg.create_py(
    in in_agristability_client_id farms.farm_agristability_clients.agristability_client_id%type,
    in in_year_to_create farms.farm_program_years.year%type,
    in in_user farms.farm_program_years.who_created%type
) returns farms.farm_program_years.program_year_id%type
language plpgsql
as
$$
declare

    new_py_id farms.farm_program_years.program_year_id%type;

begin
    select nextval('farms.farm_py_seq')
    into new_py_id;

    insert into farms.farm_program_years(
       program_year_id,
       "YEAR",
       non_participant_ind,
       agristability_client_id,
       cash_margins_ind,
       cash_margins_opt_in_date,
       farm_type_code,
       revision_count,
       who_created,
       when_created,
       who_updated,
       when_updated)
    select new_py_id as program_year_id,
           in_year_to_create,
           'N' as non_participant_ind,
           in_agristability_client_id,
           coalesce((
               select py.cash_margins_ind
               from farms.farm_program_years py
               where py.year = (in_year_to_create - 1)
               and py.agristability_client_id = in_agristability_client_id
           ), 'N') as cash_margins_ind,
           (
               select py.cash_margins_opt_in_date
               from farms.farm_program_years py
               where py.year = (in_year_to_create - 1)
               and py.agristability_client_id = in_agristability_client_id
           ) as cash_margins_opt_in_date,
           (
               select farm_type_code
               from (
                   select first_value(py.farm_type_code) over (order by py.year desc) as farm_type_code
                   from farms.farm_program_years py
                   where py.agristability_client_id = in_agristability_client_id
                   and py.year < in_year_to_create
               ) alias8 limit 1
           ) as farm_type_code,
           1 as revision_count,
           in_user as who_created,
           current_timestamp as when_created,
           in_user as who_updated,
           current_timestamp as when_updated;

    return new_py_id;
end;
$$;
