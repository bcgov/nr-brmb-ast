create or replace function farms_export_pkg.f03_statement_a(
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
               fo.operation_number incm_expn_stmt_num,
               fo.partnership_pin prshp_pin,
               fo.partnership_percent prshp_pct,
               to_char(fo.fiscal_year_start, 'YYYYMMDD') stmt_fy_start_date,
               to_char(fo.fiscal_year_end, 'YYYYMMDD') stmt_fy_end_date,
               fo.federal_accounting_code stmt_accrl_code,
               fo.feeder_member_ind feeder_member_ind,
               fo.landlord_ind lndlrd_crop_shr_ind,
               fo.crop_share_ind tenant_crop_shr_ind,
               fo.gross_income oth_grs_incm_amt,
               fo.expenses oth_farm_expn_tot,
               fo.net_income_before_adj net_incm_b_adj_amt,
               fo.other_deductions net_incm_ded_amt,
               fo.inventory_adjustments oth_cy_inv_adj_tot,
               fo.net_income_after_adj net_incm_adj_amt,
               fo.business_use_home_expense bus_elig_exp_amt,
               fo.net_farm_income net_farm_incm_amt,
               0 gst_rebate_amt
        from farms.farm_chef_statement_a_years_vw sta
        join farms.farm_farming_operations fo on fo.program_year_version_id = sta.program_year_version_id
        where sta.year = in_program_year
        order by participant_oid,
                 stab_yy;

    return cur;
end;
$$;
