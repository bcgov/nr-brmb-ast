create or replace function farms_enrolment_read_pkg.read_enrolments(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_regional_office_code farms.farm_regional_office_codes.regional_office_code%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
        with x as (
            select t.agristability_client_id,
                   t.participant_pin,
                   t.year,
                   t.agristability_scenario_id,
                   case when (
                                 t.year = (in_enrolment_year - 2)
                                 and sc.scenario_state_code in ('COMP','AMEND')
                                 and sc.scenario_category_code = 'FIN' and sc.scenario_class_code = 'USER'
                             )
                             or (
                                 t.year = (in_enrolment_year - 3)
                                 and (sc.scenario_state_code in ('COMP','AMEND'))
                                 and sc.scenario_category_code = 'FIN' and sc.scenario_class_code = 'USER'
                                 and exists (
                                     select *
                                     from farms.farm_scenarios_vw smv2
                                     join farms.farm_agristability_scenarios sc2 on sc2.agristability_scenario_id = smv2.agristability_scenario_id
                                     where smv2.year = (in_enrolment_year - 2)
                                     and smv2.agristability_client_id = t.agristability_client_id
                                     and exists (
                                         select *
                                         from farms.farm_farming_operations fo
                                         join farms.farm_reported_income_expenses rie on rie.farming_operation_id = fo.farming_operation_id
                                                                                      and coalesce(rie.agristability_scenario_id::text, '') = ''
                                         where fo.program_year_version_id = smv2.program_year_version_id
                                     )
                                     and sc2.scenario_class_code in ('CRA','CHEF','LOCAL','GEN')
                                     and smv2.scenario_number = (
                                         select max(smv3.scenario_number)
                                         from farms.farm_scenarios_vw smv3
                                         join farms.farm_agristability_scenarios sc3 on sc3.agristability_scenario_id = smv3.agristability_scenario_id
                                         where smv3.program_year_id = smv2.program_year_id
                                         and sc3.scenario_class_code in ('CRA','CHEF','LOCAL','GEN')
                                     )
                                 )
                             ) then 'COMP'
                        when t.year = (in_enrolment_year - 2)
                             and (sc.scenario_state_code = 'EN_COMP')
                             and (sc.scenario_category_code = 'ENW') then 'EN_COMP'
                        when t.year = (in_enrolment_year - 2)
                             and (sc.scenario_state_code = 'IP')
                             and (sc.scenario_category_code = 'ENW') then 'EN_IP'
                        else 'REC'
                   end scenario_state,
                   first_value(t.agristability_scenario_id)
                   over (
                       partition by t.participant_pin
                       order by case when (
                                              t.year between(in_enrolment_year - 3) and (in_enrolment_year - 2))
                                              and (sc.scenario_state_code in ('COMP','AMEND'))
                                              and sc.scenario_category_code = 'FIN'
                                              and sc.scenario_class_code = 'USER' then 0
                                     else 1
                                end,
                                case when t.year = (in_enrolment_year - 2)
                                          and (sc.scenario_state_code = 'EN_COMP')
                                          and (sc.scenario_category_code = 'ENW') then 0
                                     else 1
                                end,
                                case when t.year = (in_enrolment_year - 2)
                                          and (sc.scenario_state_code = 'IP')
                                          and (sc.scenario_category_code = 'ENW') then 0
                                     else 1
                                end,
                                t.year desc,
                                case when (sc.scenario_state_code in ('REC')) then 0
                                     else 1
                                end,
                                t.scenario_number desc
                   ) max_sc
            from farms.farm_scenarios_vw t
            join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = t.agristability_scenario_id
            where t.year <= (in_enrolment_year - 2)
        ), y as (
            select x.agristability_client_id,
                   x.participant_pin,
                   x.scenario_state,
                   row_number() over (partition by x.agristability_scenario_id order by 1) scenario_row_num
            from x
            join farms.farm_scenarios_vw m on m.agristability_scenario_id = x.agristability_scenario_id
                                           and x.agristability_scenario_id = x.max_sc
            join farms.farm_program_year_versions pyv on pyv.program_year_version_id = m.program_year_version_id
            left outer join farms.farm_office_municipality_xref omx on omx.municipality_code = pyv.municipality_code
            where omx.regional_office_code = in_regional_office_code
            or in_regional_office_code = 'ALL'
        ), scenarios as (
            select y.agristability_client_id,
                   y.participant_pin,
                   y.scenario_state
            from y
            where y.scenario_row_num = 1
        )
        select c.agristability_client_id,
               c.participant_pin,
               coalesce(o.corp_name, o.last_name || ', ' || o.first_name) producer_name,
               c.scenario_state,
               pe.failed_to_generate_ind,
               pe.failed_reason,
               pe.program_enrolment_id,
               pe.enrolment_year,
               pe.enrolment_fee,
               pe.generated_date,
               pe.generated_from_cra_ind,
               pe.generated_from_enw_ind,
               pe.combined_farm_percent,
               pe.when_updated,
               pe.revision_count enrolment_revision_count
        from scenarios c
        join farms.farm_agristability_clients ac on ac.agristability_client_id = c.agristability_client_id
        join farms.farm_persons o on o.person_id = ac.person_id
        left outer join farms.farm_program_enrolments pe on pe.agristability_client_id = c.agristability_client_id
                                                         and pe.enrolment_year = in_enrolment_year
        order by c.participant_pin;

    return cur;

end;
$$;
