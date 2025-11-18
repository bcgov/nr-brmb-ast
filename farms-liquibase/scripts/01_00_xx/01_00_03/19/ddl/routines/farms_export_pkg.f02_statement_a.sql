create or replace function farms_export_pkg.f02_statement_a(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select sta.participant_pin participant_oid,
               sta.year stab_yy,
               pyv.province_of_residence part_res_prov_code,
               pyv.province_of_main_farmstead part_fmstd_prov_code,
               ac.participant_class_code part_type,
               pyv.post_mark_date form_pstmrk_date,
               pyv.received_date form_rcvd_date,
               'N' agristab_only_ind,
               'N' agrinv_only_ind,
               'N' agristab_agrinv_ind,
               pyv.farm_years farm_years,
               pyv.last_year_farming_ind last_farm_year_ind,
               pyv.completed_prod_cycle_ind prod_cycle_cmpl_ind,
               pyv.disaster_ind prod_cycle_dstr_ind,
               pyv.common_share_total corp_shr_tot,
               0 comm_mem_tot
        from farms.farm_chef_statement_a_years_vw sta
        join farms.farm_program_year_versions pyv on pyv.program_year_version_id = sta.program_year_version_id
        join farms.farm_agristability_clients ac on ac.agristability_client_id = sta.agristability_client_id
        where sta.year = in_program_year
        order by participant_oid,
                 stab_yy;

    return cur;
end;
$$;
