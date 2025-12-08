create or replace function farms_export_pkg.f05_ade(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select t.participant_pin,
               t."YEAR",
               t.sin,
               t.business_number,
               t.trust_number,
               p.first_name,
               p.last_name,
               p.corp_name,
               p.address_line_1,
               p.address_line_2,
               p.city,
               p.province_state,
               p.postal_code,
               p.country,
               t.participant_lang_code,
               p.fax_number,
               p.daytime_phone,
               p.evening_phone,
               p.cell_number,
               t.participant_class_code
        from (
            select ac.agristability_client_id,
                   max(py.year) "YEAR",
                   max(ac.participant_pin) participant_pin,
                   max(ac.person_id) person_id,
                   max(ac.sin) sin,
                   max(ac.business_number) business_number,
                   max(ac.trust_number) trust_number,
                   max(ac.participant_lang_code) participant_lang_code,
                   max(ac.participant_class_code) participant_class_code
            from farms.farm_agristability_clients ac
            join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
            where py.non_participant_ind = 'N'
            and py.year = in_program_year
            and exists (
                select *
                from farms.farm_agri_scenarios_vw m
                join farms.farm_agristability_scenarios sc on sc.agristability_scenario_id = m.agristability_scenario_id
                where m.program_year_id = py.program_year_id
                and sc.scenario_class_code in ('USER', 'REF')
                and sc.scenario_state_code in ('COMP', 'AMEND')
                and sc.scenario_category_code in ('FIN','AADJ','PADJ')
            )
            group by ac.agristability_client_id
        ) t
        join farms.farm_persons p on t.person_id = p.person_id;

    return cur;
end;
$$;
