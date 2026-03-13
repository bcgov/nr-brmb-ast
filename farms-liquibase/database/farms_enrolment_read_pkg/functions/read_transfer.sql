create or replace function farms_enrolment_read_pkg.read_transfer(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_pins integer[]
)
returns table(
    agristability_client_id         farms.farm_agristability_clients.agristability_client_id%type,
    participant_pin                 farms.farm_agristability_clients.participant_pin%type,
    program_enrolment_id            farms.farm_program_enrolments.program_enrolment_id%type,
    enrolment_year                  farms.farm_program_enrolments.enrolment_year%type,
    failed_to_generate_ind          farms.farm_program_enrolments.failed_to_generate_ind%type,
    failed_reason                   farms.farm_program_enrolments.failed_reason%type,
    enrolment_fee                   farms.farm_program_enrolments.enrolment_fee%type,
    generated_date                  farms.farm_program_enrolments.generated_date%type,
    generated_from_cra_ind          farms.farm_program_enrolments.generated_from_cra_ind%type,
    generated_from_enw_ind          farms.farm_program_enrolments.generated_from_enw_ind%type,
    contribution_margin_average     farms.farm_program_enrolments.contribution_margin_average%type,
    margin_year_minus_2             farms.farm_program_enrolments.margin_year_minus_2%type,
    margin_year_minus_2_ind         farms.farm_program_enrolments.margin_year_minus_2_ind%type,
    margin_year_minus_3             farms.farm_program_enrolments.margin_year_minus_3%type,
    margin_year_minus_3_ind         farms.farm_program_enrolments.margin_year_minus_3_ind%type,
    margin_year_minus_4             farms.farm_program_enrolments.margin_year_minus_4%type,
    margin_year_minus_4_ind         farms.farm_program_enrolments.margin_year_minus_4_ind%type,
    margin_year_minus_5             farms.farm_program_enrolments.margin_year_minus_5%type,
    margin_year_minus_5_ind         farms.farm_program_enrolments.margin_year_minus_5_ind%type,
    margin_year_minus_6             farms.farm_program_enrolments.margin_year_minus_6%type,
    margin_year_minus_6_ind         farms.farm_program_enrolments.margin_year_minus_6_ind%type,
    create_task_in_barn_ind         farms.farm_program_enrolments.create_task_in_barn_ind%type,
    late_participant_ind            varchar,
    revision_count                  farms.farm_program_enrolments.revision_count%type,
    sector_code_desc                farms.farm_sector_codes.description%type,
    sector_detail_code_desc         farms.farm_sector_detail_codes.description%type,
    is_in_combined_farm             varchar,
    combined_farm_percent           farms.farm_program_enrolments.combined_farm_percent%type
)
language sql
as $$
    select ac.agristability_client_id,
           ac.participant_pin,
           pe.program_enrolment_id,
           pe.enrolment_year,
           pe.failed_to_generate_ind,
           pe.failed_reason,
           pe.enrolment_fee,
           pe.generated_date,
           pe.generated_from_cra_ind,
           pe.generated_from_enw_ind,
           pe.contribution_margin_average,
           pe.margin_year_minus_2,
           pe.margin_year_minus_2_ind,
           pe.margin_year_minus_3,
           pe.margin_year_minus_3_ind,
           pe.margin_year_minus_4,
           pe.margin_year_minus_4_ind,
           pe.margin_year_minus_5,
           pe.margin_year_minus_5_ind,
           pe.margin_year_minus_6,
           pe.margin_year_minus_6_ind,
           pe.create_task_in_barn_ind,
           coalesce(py.late_participant_ind, 'N') late_participant_ind,
           pe.revision_count,
           sc.description sector_code_desc,
           sdc.description sector_detail_code_desc,
           case when scen.combined_farm_number is not null then 'Y'
                else 'N'
           end is_in_combined_farm,
           pe.combined_farm_percent
    from farms.farm_agristability_clients ac
    join (
        select e.*,
               farms_report_pkg.get_sector_detail_code(e.agristability_scenario_id) sector_detail_code
        from farms.farm_program_enrolments e
        where e.failed_to_generate_ind = 'N'
        and e.enrolment_year = in_enrolment_year
    ) pe on pe.agristability_client_id = ac.agristability_client_id
    left outer join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
                                                and py.year = in_enrolment_year
    left outer join farms.farm_sector_detail_xref x on x.sector_detail_code = pe.sector_detail_code
    left outer join farms.farm_sector_detail_codes sdc on sdc.sector_detail_code = x.sector_detail_code
    left outer join farms.farm_sector_codes sc on sc.sector_code = x.sector_code
    left outer join farms.farm_agristability_scenarios scen on scen.agristability_scenario_id = pe.agristability_scenario_id
    where ac.participant_pin = any(in_pins)
    order by ac.participant_pin;
$$;
