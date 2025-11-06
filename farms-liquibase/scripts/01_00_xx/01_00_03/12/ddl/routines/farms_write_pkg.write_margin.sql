create or replace procedure farms_write_pkg.write_margin(
    inout io_benefit_calc_margin_id farms.farm_benefit_calc_margins.benefit_calc_margin_id%type,
    inout io_revision_count farms.farm_benefit_calc_margins.revision_count%type,
    in in_total_allowable_expenses farms.farm_benefit_calc_margins.total_allowable_expenses%type,
    in in_total_allowable_income farms.farm_benefit_calc_margins.total_allowable_income%type,
    in in_unadjusted_prod_margin farms.farm_benefit_calc_margins.unadjusted_production_margin%type,
    in in_production_marg_accr_adjs farms.farm_benefit_calc_margins.production_marg_accr_adjs%type,
    in in_accrual_adjs_crop_inventory farms.farm_benefit_calc_margins.accrual_adjs_crop_inventory%type,
    in in_accrual_adjs_lvstck_inv farms.farm_benefit_calc_margins.accrual_adjs_lvstck_inventory%type,
    in in_accrual_adjs_payables farms.farm_benefit_calc_margins.accrual_adjs_payables%type,
    in in_accrual_adjs_pur_inputs farms.farm_benefit_calc_margins.accrual_adjs_purchased_inputs%type,
    in in_accrual_adjs_receivables farms.farm_benefit_calc_margins.accrual_adjs_receivables%type,
    in in_unadjusted_allowable_income farms.farm_benefit_calc_margins.unadjusted_allowable_income%type,
    in in_yardage_income farms.farm_benefit_calc_margins.yardage_income%type,
    in in_program_payment_income farms.farm_benefit_calc_margins.program_payment_income%type,
    in in_supply_mngd_commodity_inc farms.farm_benefit_calc_margins.supply_mngd_commodity_income%type,
    in in_total_unallowable_income farms.farm_benefit_calc_margins.total_unallowable_income%type,
    in in_unadjusted_allowable_exp farms.farm_benefit_calc_margins.unadjusted_allowable_expenses%type,
    in in_yardage_expenses farms.farm_benefit_calc_margins.yardage_expenses%type,
    in in_contract_work_expenses farms.farm_benefit_calc_margins.contract_work_expenses%type,
    in in_manual_expenses farms.farm_benefit_calc_margins.manual_expenses%type,
    in in_total_unallowable_expenses farms.farm_benefit_calc_margins.total_unallowable_expenses%type,
    in in_deferred_program_payments farms.farm_benefit_calc_margins.deferred_program_payments%type,
    in in_expense_accr_adjs farms.farm_benefit_calc_margins.expense_accr_adjs%type,
    in in_prod_insur_deemed_subtotal farms.farm_benefit_calc_margins.prod_insur_deemed_subtotal%type,
    in in_prod_insur_deemed_total farms.farm_benefit_calc_margins.prod_insur_deemed_total%type,
    in in_farming_operation_id farms.farm_benefit_calc_margins.farming_operation_id%type,
    in in_agristability_scenario_id farms.farm_benefit_calc_margins.agristability_scenario_id%type,
    in in_user_id farms.farm_benefit_calc_margins.who_updated%type
)
language plpgsql
as
$$
declare

    v_rows_affected  bigint := null;
    v_revision_count farms.farm_agristability_claims.revision_count%type := null;

begin
    if coalesce(io_benefit_calc_margin_id::text, '') = '' then
        --
        -- insert
        --
        insert into farms.farm_benefit_calc_margins (
            benefit_calc_margin_id,
            total_allowable_expenses,
            total_allowable_income,
            unadjusted_production_margin,
            production_marg_accr_adjs,
            accrual_adjs_crop_inventory,
            accrual_adjs_lvstck_inventory,
            accrual_adjs_payables,
            accrual_adjs_purchased_inputs,
            accrual_adjs_receivables,
            supply_mngd_commodity_income,
            unadjusted_allowable_income,
            yardage_income,
            program_payment_income,
            total_unallowable_income,
            unadjusted_allowable_expenses,
            yardage_expenses,
            contract_work_expenses,
            manual_expenses,
            total_unallowable_expenses,
            deferred_program_payments,
            expense_accr_adjs,
            prod_insur_deemed_subtotal,
            prod_insur_deemed_total,
            farming_operation_id,
            agristability_scenario_id,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
        ) values (
            nextval('farms.farm_bcm_seq'),
            in_total_allowable_expenses,
            in_total_allowable_income,
            in_unadjusted_prod_margin,
            in_production_marg_accr_adjs,
            in_accrual_adjs_crop_inventory,
            in_accrual_adjs_lvstck_inv,
            in_accrual_adjs_payables,
            in_accrual_adjs_pur_inputs,
            in_accrual_adjs_receivables,
            in_supply_mngd_commodity_inc,
            in_unadjusted_allowable_income,
            in_yardage_income,
            in_program_payment_income,
            in_total_unallowable_income,
            in_unadjusted_allowable_exp,
            in_yardage_expenses,
            in_contract_work_expenses,
            in_manual_expenses,
            in_total_unallowable_expenses,
            in_deferred_program_payments,
            in_expense_accr_adjs,
            in_prod_insur_deemed_subtotal,
            in_prod_insur_deemed_total,
            in_farming_operation_id,
            in_agristability_scenario_id,
            1,
            in_user_id,
            current_timestamp,
            in_user_id,
            current_timestamp
        )
        returning benefit_calc_margin_id, revision_count
        into io_benefit_calc_margin_id, io_revision_count;
    else
        --
        -- update
        --
        v_revision_count := (io_revision_count + farms_types_pkg.revision_count_increment()::int);

        update farms.farm_benefit_calc_margins
        set total_allowable_expenses = in_total_allowable_expenses,
            total_allowable_income = in_total_allowable_income,
            unadjusted_production_margin = in_unadjusted_prod_margin,
            production_marg_accr_adjs = in_production_marg_accr_adjs,
            accrual_adjs_crop_inventory = in_accrual_adjs_crop_inventory,
            accrual_adjs_lvstck_inventory = in_accrual_adjs_lvstck_inv,
            accrual_adjs_payables = in_accrual_adjs_payables,
            accrual_adjs_purchased_inputs = in_accrual_adjs_pur_inputs,
            accrual_adjs_receivables = in_accrual_adjs_receivables,
            supply_mngd_commodity_income = in_supply_mngd_commodity_inc,
            unadjusted_allowable_income = in_unadjusted_allowable_income,
            yardage_income = in_yardage_income,
            program_payment_income = in_program_payment_income,
            total_unallowable_income = in_total_unallowable_income,
            unadjusted_allowable_expenses = in_unadjusted_allowable_exp,
            yardage_expenses = in_yardage_expenses,
            contract_work_expenses = in_contract_work_expenses,
            manual_expenses = in_manual_expenses,
            total_unallowable_expenses = in_total_unallowable_expenses,
            deferred_program_payments = in_deferred_program_payments,
            expense_accr_adjs = in_expense_accr_adjs,
            prod_insur_deemed_subtotal = in_prod_insur_deemed_subtotal,
            prod_insur_deemed_total = in_prod_insur_deemed_total,
            farming_operation_id = in_farming_operation_id,
            agristability_scenario_id = in_agristability_scenario_id,
            revision_count = v_revision_count,
            who_updated  = in_user_id,
            when_updated = current_timestamp
        where benefit_calc_margin_id = io_benefit_calc_margin_id
        and revision_count = io_revision_count
        returning benefit_calc_margin_id, revision_count
        into io_benefit_calc_margin_id, io_revision_count;

        get diagnostics v_rows_affected = row_count;
        if v_rows_affected = 0 then
            raise exception '%', farms_types_pkg.invalid_revision_count_msg()
            using errcode = farms_types_pkg.invalid_revision_count_code()::text;
        end if;

    end if;
end;
$$;
