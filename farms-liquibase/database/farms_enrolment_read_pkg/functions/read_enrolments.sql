create or replace function farms_enrolment_read_pkg.read_enrolments(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_regional_office_code farms.farm_regional_office_codes.regional_office_code%type
)
returns table(
    agristability_client_id     farms.farm_scenarios_vw.agristability_client_id%type,
    participant_pin             farms.farm_scenarios_vw.participant_pin%type,
    producer_name               farms.farm_persons.corp_name%type,
    scenario_state              varchar,
    failed_to_generate_ind      farms.farm_program_enrolments.failed_to_generate_ind%type,
    failed_reason               farms.farm_program_enrolments.failed_reason%type,
    program_enrolment_id        farms.farm_program_enrolments.program_enrolment_id%type,
    enrolment_year              farms.farm_program_enrolments.enrolment_year%type,
    enrolment_fee               farms.farm_program_enrolments.enrolment_fee%type,
    generated_date              farms.farm_program_enrolments.generated_date%type,
    generated_from_cra_ind      farms.farm_program_enrolments.generated_from_cra_ind%type,
    generated_from_enw_ind      farms.farm_program_enrolments.generated_from_enw_ind%type,
    combined_farm_percent       farms.farm_program_enrolments.combined_farm_percent%type,
    when_updated                farms.farm_program_enrolments.when_updated%type,
    enrolment_revision_count    farms.farm_program_enrolments.revision_count%type
)
language sql
as $$
    with t as (
        select py.year,
               sc.agristability_scenario_id,
               case when (
                        py.year = (in_enrolment_year - 2)
                        and sc.scenario_state_code in ('COMP', 'AMEND')
                        and sc.scenario_category_code = 'FIN'
                        and sc.scenario_class_code = 'USER'
                    ) or (
                        py.year = (in_enrolment_year - 2)
                        and sc.scenario_state_code in ('COMP', 'AMEND')
                        and sc.scenario_category_code = 'FIN'
                        and sc.scenario_class_code = 'USER'
                        and exists (
                            select 1
                            from farms.farm_agristability_scenarios sc2
                            join farms.farm_program_year_versions pyv2 on sc2.program_year_version_id = pyv2.program_year_version_id
                            join farms.farm_program_years py2 on pyv2.program_year_id = py2.program_year_id
                            where py2.year = (in_enrolment_year - 2)
                            and py2.agristability_client_id = py.agristability_client_id
                            and exists (
                                select 1
                                from farms.farm_farming_operations fo
                                join farms.farm_reported_income_expenses rie on rie.farming_operation_id = fo.farming_operation_id
                                                                             and rie.agristability_scenario_id is null
                                 where fo.program_year_version_id = sc2.program_year_version_id
                            )
                            and sc2.scenario_class_code in ('CRA', 'CHEF', 'LOCAL', 'GEN')
                            and sc2.scenario_number = (
                                select max(sc3.scenario_number)
                                from farms.farm_agristability_scenarios sc3
                                join farms.farm_program_year_versions pyv3 on sc3.program_year_version_id = pyv3.program_year_version_id
                                join farms.farm_program_years py3 on pyv3.program_year_id = py3.program_year_id
                                where py3.program_year_id = py2.program_year_id
                                and sc3.scenario_class_code in ('CRA', 'CHEF', 'LOCAL', 'GEN')
                            )
                        )
                    ) then 'COMP'
                    when py.year = (in_enrolment_year - 2)
                         and sc.scenario_state_code = 'EN_COMP'
                         and sc.scenario_category_code = 'ENW' then 'EN_COMP'
                    when py.year = (in_enrolment_year - 2)
                         and sc.scenario_state_code = 'IP'
                         and sc.scenario_category_code = 'ENW' then 'EN_IP'
                    else 'REC'
               end scenario_state,
               first_value(sc.agristability_scenario_id) over (
                   partition by py.agristability_client_id
                   order by case when py.year between (in_enrolment_year - 3) and (in_enrolment_year - 2)
                                      and sc.scenario_state_code in ('COMP', 'AMEND')
                                      and sc.scenario_category_code = 'FIN'
                                      and sc.scenario_class_code = 'USER' then 0
                                 else 1
                            end,
                            case when py.year = (in_enrolment_year - 2)
                                      and sc.scenario_state_code = 'EN_COMP'
                                      and sc.scenario_category_code = 'ENW' then 0
                                 else 1
                            end,
                            case when py.year = (in_enrolment_year - 2)
                                      and sc.scenario_state_code = 'IP'
                                      and sc.scenario_category_code = 'ENW' then 0
                                 else 1
                            end,
                            py.year desc,
                            case when sc.scenario_state_code in ('REC') then 0
                                 else 1
                            end,
                            sc.scenario_number desc
               ) max_sc,
               s.*
        from (
            select py.*
            from farms.farm_program_years py
            where py.year <= (in_enrolment_year - 2)
        ) py
        join (
            select ac.agristability_client_id,
                   ac.participant_pin,
                   coalesce(o.corp_name, o.last_name || ', ' || o.first_name) producer_name,
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
            from farms.farm_agristability_clients ac
            join farms.farm_persons o on o.person_id = ac.person_id
            left outer join farms.farm_program_enrolments pe on pe.agristability_client_id = ac.agristability_client_id
                                                             and pe.enrolment_year = in_enrolment_year
        ) s on s.agristability_client_id = py.agristability_client_id
        join farms.farm_program_year_versions pyv on py.program_year_id = pyv.program_year_id
        join farms.farm_agristability_scenarios sc on pyv.program_year_version_id = sc.program_year_version_id
        left outer join farms.farm_office_municipality_xref omx on omx.municipality_code = pyv.municipality_code
                                                                and (omx.regional_office_code = in_regional_office_code or in_regional_office_code = 'ALL')
    ), x as (
        select t.*,
               row_number() over (partition by t.agristability_scenario_id order by 1) scenario_row_num
        from t
        where t.agristability_scenario_id = t.max_sc
    ), y as (
        select x.*
        from x
        where x.scenario_row_num = 1
    )
    select y.agristability_client_id,
           y.participant_pin,
           y.producer_name,
           y.scenario_state,
           y.failed_to_generate_ind,
           y.failed_reason,
           y.program_enrolment_id,
           y.enrolment_year,
           y.enrolment_fee,
           y.generated_date,
           y.generated_from_cra_ind,
           y.generated_from_enw_ind,
           y.combined_farm_percent,
           y.when_updated,
           y.enrolment_revision_count
    from y
    where y.agristability_scenario_id = y.max_sc
    order by y.participant_pin;
$$;
