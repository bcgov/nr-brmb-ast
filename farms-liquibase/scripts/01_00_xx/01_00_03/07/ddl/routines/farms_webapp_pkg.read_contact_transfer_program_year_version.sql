create or replace function farms_webapp_pkg.read_contact_transfer_program_year_version(
    in in_client_ids numeric[]
) returns refcursor
language plpgsql
as
$$
declare

    return_cur refcursor;

begin

    open return_cur for
        select
            ac.agristability_client_id,
            pyv.program_year_id,
            pyv.program_year_version_id,
            pyv.program_year_version_number,
            pyv.form_version_number,
            pyv.common_share_total,
            pyv.farm_years,
            pyv.accrual_worksheet_ind,
            pyv.completed_prod_cycle_ind,
            pyv.cwb_worksheet_ind,
            pyv.perishable_commodities_ind,
            pyv.receipts_ind,
            pyv.accrual_cash_conversion_ind,
            pyv.combined_farm_ind,
            pyv.coop_member_ind,
            pyv.corporate_shareholder_ind,
            pyv.disaster_ind,
            pyv.partnership_member_ind,
            pyv.sole_proprietor_ind,
            pyv.other_text,
            pyv.post_mark_date,
            pyv.province_of_residence,
            pyv.received_date,
            pyv.last_year_farming_ind,
            null description_of_change,
            pyv.can_send_cob_to_rep_ind,
            pyv.province_of_main_farmstead,
            pyv.locally_updated_ind,
            pyv.participant_profile_code,
            ppc.description as participant_profile_desc,
            pyv.municipality_code,
            mc.description as municipality_code_description,
            pyv.federal_status_code,
            fsc.description as federal_status_description,
            py.non_participant_ind,
            py.late_participant_ind,
            pyv.revision_count as pyv_revision_count
        from farms.farm_agristability_clients ac
        left outer join farms.farm_persons o on o.person_id = ac.person_id
        left outer join farms.farm_persons c on c.person_id = ac.person_id_client_contacted_by
        left outer join farms.farm_participant_class_codes pcc on pcc.participant_class_code = ac.participant_class_code
        left outer join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
                                                    and py.program_year_id = (
                                                        select max(py2.program_year_id)
                                                        from farms.farm_program_years py2
                                                        where py2.agristability_client_id = ac.agristability_client_id
                                                    )
        left outer join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
                                                             and pyv.program_year_version_id = (
                                                                 select max(pyv2.program_year_version_id)
                                                                 from farms.farm_program_year_versions pyv2
                                                                 where pyv2.program_year_id = py.program_year_id
                                                             )
        left outer join farms.farm_municipality_codes mc on mc.municipality_code = pyv.municipality_code
        left outer join farms.farm_federal_status_codes fsc on fsc.federal_status_code = pyv.federal_status_code
        left outer join farms.farm_participant_profile_codes ppc on pyv.participant_profile_code = ppc.participant_profile_code
        where ac.agristability_client_id = any(in_client_ids);

    return return_cur;

end;
$$;
