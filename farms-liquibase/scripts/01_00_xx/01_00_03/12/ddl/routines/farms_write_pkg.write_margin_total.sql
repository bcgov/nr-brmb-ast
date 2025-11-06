create or replace procedure farms_write_pkg.write_margin_total(
    inout io_benefit_calc_total_id farms.farm_benefit_calc_totals.benefit_calc_total_id%type,
    inout io_revision_count farms.farm_benefit_calc_totals.revision_count%type,
    in in_total_allowable_expenses farms.farm_benefit_calc_totals.total_allowable_expenses%type,
    in in_total_allowable_income farms.farm_benefit_calc_totals.total_allowable_income%type,
    in in_unadjusted_prod_margin farms.farm_benefit_calc_totals.unadjusted_production_margin%type,
    in in_production_marg_accr_adjs farms.farm_benefit_calc_totals.production_marg_accr_adjs%type,
    in in_prod_marg_aft_str_changs farms.farm_benefit_calc_totals.production_marg_aft_str_changs%type,
    in in_accrual_adjs_crop_inventory farms.farm_benefit_calc_totals.accrual_adjs_crop_inventory%type,
    in in_accrual_adjs_lvstck_inv farms.farm_benefit_calc_totals.accrual_adjs_lvstck_inventory%type,
    in in_accrual_adjs_payables farms.farm_benefit_calc_totals.accrual_adjs_payables%type,
    in in_accrual_adjs_pur_inputs farms.farm_benefit_calc_totals.accrual_adjs_purchased_inputs%type,
    in in_accrual_adjs_receivables farms.farm_benefit_calc_totals.accrual_adjs_receivables%type,
    in in_unadjusted_allowable_income farms.farm_benefit_calc_totals.unadjusted_allowable_income%type,
    in in_yardage_income farms.farm_benefit_calc_totals.yardage_income%type,
    in in_program_payment_income farms.farm_benefit_calc_totals.program_payment_income%type,
    in in_supply_mngd_commodity_inc farms.farm_benefit_calc_totals.supply_mngd_commodity_income%type,
    in in_total_unallowable_income farms.farm_benefit_calc_totals.total_unallowable_income%type,
    in in_unadjusted_allowable_exp farms.farm_benefit_calc_totals.unadjusted_allowable_income%type,
    in in_yardage_expenses farms.farm_benefit_calc_totals.yardage_expenses%type,
    in in_contract_work_expenses farms.farm_benefit_calc_totals.contract_work_expenses%type,
    in in_manual_expenses farms.farm_benefit_calc_totals.manual_expenses%type,
    in in_total_unallowable_expenses farms.farm_benefit_calc_totals.total_unallowable_expenses%type,
    in in_deferred_program_payments farms.farm_benefit_calc_totals.deferred_program_payments%type,
    in in_expense_accr_adjs farms.farm_benefit_calc_totals.expense_accr_adjs%type,
    in in_expense_structural_chg_adjs farms.farm_benefit_calc_totals.expense_structural_chg_adjs%type,
    in in_expenses_aft_str_chg farms.farm_benefit_calc_totals.expenses_aft_str_chg%type,
    in in_structural_change_adjs farms.farm_benefit_calc_totals.structural_change_adjs%type,
    in in_fiscal_year_pro_rate_adjs farms.farm_benefit_calc_totals.fiscal_year_pro_rate_adjs%type,
    in in_farm_size_ratio farms.farm_benefit_calc_totals.farm_size_ratio%type,
    in in_expense_farm_size_ratio farms.farm_benefit_calc_totals.expense_farm_size_ratio%type,
    in in_struct_change_notable_ind farms.farm_benefit_calc_totals.structural_change_notable_ind%type,
    in in_bpu_lead_ind farms.farm_benefit_calc_totals.bpu_lead_ind%type,
    in in_ratio_structural_change_adj farms.farm_benefit_calc_totals.ratio_structural_change_adjs%type,
    in in_additive_structural_chg_adj farms.farm_benefit_calc_totals.additive_structural_change_adj%type,
    in in_ratio_prod_marg_aft_str_chg farms.farm_benefit_calc_totals.ratio_prod_marg_aft_str_changs%type,
    in in_addtv_prod_marg_aft_str_chg farms.farm_benefit_calc_totals.additive_prod_marg_aft_str_chg%type,
    in in_ratio_struc_chg_notable_ind farms.farm_benefit_calc_totals.ratio_struc_chg_notable_ind%type,
    in in_additiv_str_chg_notable_ind farms.farm_benefit_calc_totals.additive_struc_chg_notable_ind%type,
    in in_agristability_scenario_id farms.farm_benefit_calc_totals.agristability_scenario_id%type,
    in in_user_id farms.farm_benefit_calc_totals.who_updated%type
)
language plpgsql
as
$$
declare

    v_rows_affected  bigint := null;
    v_revision_count farms.farm_agristability_claims.revision_count%type := null;

begin
    if coalesce(io_benefit_calc_total_id::text, '') = '' then
        --
        -- insert
        --
        insert into farms.farm_benefit_calc_totals (
            benefit_calc_total_id,
            total_allowable_expenses,
            total_allowable_income,
            unadjusted_production_margin,
            production_marg_accr_adjs,
            production_marg_aft_str_changs,
            accrual_adjs_crop_inventory,
            accrual_adjs_lvstck_inventory,
            accrual_adjs_payables,
            accrual_adjs_purchased_inputs,
            accrual_adjs_receivables,
            unadjusted_allowable_income,
            yardage_income,
            program_payment_income,
            supply_mngd_commodity_income,
            total_unallowable_income,
            unadjusted_allowable_expenses,
            yardage_expenses,
            contract_work_expenses,
            manual_expenses,
            total_unallowable_expenses,
            deferred_program_payments,
            expense_accr_adjs,
            expense_structural_chg_adjs,
            expenses_aft_str_chg,

            structural_change_adjs,
            fiscal_year_pro_rate_adjs,
            farm_size_ratio,
            expense_farm_size_ratio,
            structural_change_notable_ind,
            bpu_lead_ind,
            ratio_structural_change_adjs,
            additive_structural_change_adj,
            ratio_prod_marg_aft_str_changs,
            additive_prod_marg_aft_str_chg,
            ratio_struc_chg_notable_ind,
            additive_struc_chg_notable_ind,

            agristability_scenario_id,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        ) values (
            nextval('farms.farm_bcmt_seq'),
            in_total_allowable_expenses,
            in_total_allowable_income,
            in_unadjusted_prod_margin,
            in_production_marg_accr_adjs,
            in_prod_marg_aft_str_changs,
            in_accrual_adjs_crop_inventory,
            in_accrual_adjs_lvstck_inv,
            in_accrual_adjs_payables,
            in_accrual_adjs_pur_inputs,
            in_accrual_adjs_receivables,
            in_unadjusted_allowable_income,
            in_yardage_income,
            in_program_payment_income,
            in_supply_mngd_commodity_inc,
            in_total_unallowable_income,
            in_unadjusted_allowable_exp,
            in_yardage_expenses,
            in_contract_work_expenses,
            in_manual_expenses,
            in_total_unallowable_expenses,
            in_deferred_program_payments,
            in_expense_accr_adjs,
            in_expense_structural_chg_adjs,
            in_expenses_aft_str_chg,

            in_structural_change_adjs,
            in_fiscal_year_pro_rate_adjs,
            in_farm_size_ratio,
            in_expense_farm_size_ratio,
            in_struct_change_notable_ind,
            in_bpu_lead_ind,
            in_ratio_structural_change_adj,
            in_additive_structural_chg_adj,
            in_ratio_prod_marg_aft_str_chg,
            in_addtv_prod_marg_aft_str_chg,
            in_ratio_struc_chg_notable_ind,
            in_additiv_str_chg_notable_ind,

            in_agristability_scenario_id,
            1,
            in_user_id,
            current_timestamp,
            in_user_id,
            current_timestamp
        )
        returning benefit_calc_total_id, revision_count
        into io_benefit_calc_total_id, io_revision_count;
    else
        --
        -- update
        --
        v_revision_count := (io_revision_count + farms_types_pkg.revision_count_increment()::int);

        update farms.farm_benefit_calc_totals
        set total_allowable_expenses = in_total_allowable_expenses,
            total_allowable_income = in_total_allowable_income,
            unadjusted_production_margin = in_unadjusted_prod_margin,
            production_marg_accr_adjs = in_production_marg_accr_adjs,
            production_marg_aft_str_changs = in_prod_marg_aft_str_changs,
            accrual_adjs_crop_inventory = in_accrual_adjs_crop_inventory,
            accrual_adjs_lvstck_inventory = in_accrual_adjs_lvstck_inv,
            accrual_adjs_payables = in_accrual_adjs_payables,
            accrual_adjs_purchased_inputs = in_accrual_adjs_pur_inputs,
            accrual_adjs_receivables = in_accrual_adjs_receivables,
            unadjusted_allowable_income = in_unadjusted_allowable_income,
            yardage_income = in_yardage_income,
            program_payment_income = in_program_payment_income,
            supply_mngd_commodity_income = in_supply_mngd_commodity_inc,
            total_unallowable_income = in_total_unallowable_income,
            unadjusted_allowable_expenses = in_unadjusted_allowable_exp,
            yardage_expenses = in_yardage_expenses,
            contract_work_expenses = in_contract_work_expenses,
            manual_expenses = in_manual_expenses,
            total_unallowable_expenses = in_total_unallowable_expenses,
            deferred_program_payments = in_deferred_program_payments,
            expense_accr_adjs = in_expense_accr_adjs,
            expense_structural_chg_adjs = in_expense_structural_chg_adjs,
            expenses_aft_str_chg = in_expenses_aft_str_chg,
            structural_change_adjs = in_structural_change_adjs,
            fiscal_year_pro_rate_adjs = in_fiscal_year_pro_rate_adjs,
            farm_size_ratio = in_farm_size_ratio,
            expense_farm_size_ratio = in_expense_farm_size_ratio,
            structural_change_notable_ind = in_struct_change_notable_ind,
            bpu_lead_ind = in_bpu_lead_ind,
            ratio_structural_change_adjs = in_ratio_structural_change_adj,
            additive_structural_change_adj = in_additive_structural_chg_adj,
            ratio_prod_marg_aft_str_changs = in_ratio_prod_marg_aft_str_chg,
            additive_prod_marg_aft_str_chg = in_addtv_prod_marg_aft_str_chg,
            ratio_struc_chg_notable_ind = in_ratio_struc_chg_notable_ind,
            additive_struc_chg_notable_ind = in_additiv_str_chg_notable_ind,
            agristability_scenario_id = in_agristability_scenario_id,
            revision_count = v_revision_count,
            who_updated  = in_user_id,
            when_updated = current_timestamp
        where benefit_calc_total_id = io_benefit_calc_total_id
        and revision_count = io_revision_count
        returning benefit_calc_total_id, revision_count
        into io_benefit_calc_total_id, io_revision_count;

        get diagnostics v_rows_affected = row_count;
        if v_rows_affected = 0 then
            raise exception '%', farms_types_pkg.invalid_revision_count_msg()
            using errcode = farms_types_pkg.invalid_revision_count_code()::text;
        end if;

    end if;
end;
$$;
