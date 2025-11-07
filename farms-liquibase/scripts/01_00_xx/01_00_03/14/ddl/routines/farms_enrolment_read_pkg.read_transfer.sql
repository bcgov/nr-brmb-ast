create or replace function farms_enrolment_read_pkg.read_transfer(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_pins numeric[]
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin
    open cur for
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
               case when (scen.combined_farm_number is not null and scen.combined_farm_number::text <> '') then 'Y'
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

    return cur;

end;
$$;
