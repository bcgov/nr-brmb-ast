create or replace procedure farms_calculator_pkg.update_pyv_copy_old(
    in in_old_pyv_id farms.farm_program_year_versions.program_year_version_id%type,
    in in_new_pyv_id farms.farm_program_year_versions.program_year_version_id%type,
    in in_pyv_copy_old_data text,
    in in_op_nums_copy_old_data varchar[],
    in in_user text
)
language plpgsql
as
$$
declare

    op_curs cursor for
        select old_op.farming_operation_id old_op_id,
               new_op.farming_operation_id new_op_id
        from farms.farm_farming_operations old_op
        join farms.farm_farming_operations new_op on old_op.operation_number = new_op.operation_number
        where old_op.program_year_version_id = in_old_pyv_id
        and new_op.program_year_version_id = in_new_pyv_id
        and old_op.operation_number = any(in_op_nums_copy_old_data);

    prod_insurance_nums varchar[];

begin

    if in_pyv_copy_old_data = 'Y' then

        update farms.farm_program_year_versions new_pyv
        set locally_updated_ind = 'Y',
            farm_years = old_pyv.farm_years,
            federal_status_code = old_pyv.federal_status_code,
            province_of_residence = old_pyv.province_of_residence,
            province_of_main_farmstead = old_pyv.province_of_main_farmstead,
            other_text = old_pyv.other_text,
            municipality_code = old_pyv.municipality_code,
            post_mark_date = old_pyv.post_mark_date,
            received_date = old_pyv.received_date,
            common_share_total = old_pyv.common_share_total,
            participant_profile_code = old_pyv.participant_profile_code,
            sole_proprietor_ind = old_pyv.sole_proprietor_ind,
            completed_prod_cycle_ind = old_pyv.completed_prod_cycle_ind,
            corporate_shareholder_ind = old_pyv.corporate_shareholder_ind,
            disaster_ind = old_pyv.disaster_ind,
            partnership_member_ind = old_pyv.partnership_member_ind,
            last_year_farming_ind = old_pyv.last_year_farming_ind,
            coop_member_ind = old_pyv.coop_member_ind,
            accrual_cash_conversion_ind = old_pyv.accrual_cash_conversion_ind,
            combined_farm_ind = old_pyv.combined_farm_ind,
            can_send_cob_to_rep_ind = old_pyv.can_send_cob_to_rep_ind,
            cwb_worksheet_ind = old_pyv.cwb_worksheet_ind,
            receipts_ind = old_pyv.receipts_ind,
            accrual_worksheet_ind = old_pyv.accrual_worksheet_ind,
            perishable_commodities_ind = old_pyv.perishable_commodities_ind,
            who_updated = in_user,
            when_updated = current_timestamp
        from farms.farm_program_year_versions old_pyv
        where new_pyv.program_year_version_id = in_new_pyv_id
        and old_pyv.program_year_version_id = in_old_pyv_id;

    end if;


    update farms.farm_farming_operations new_op
    set locally_updated_ind = 'Y',
        federal_accounting_code = old_op.federal_accounting_code,
        fiscal_year_start = old_op.fiscal_year_start,
        fiscal_year_end = old_op.fiscal_year_end,
        crop_share_ind = old_op.crop_share_ind,
        feeder_member_ind = old_op.feeder_member_ind,
        partnership_pin = old_op.partnership_pin,
        partnership_name = old_op.partnership_name,
        partnership_percent = old_op.partnership_percent,
        crop_disaster_ind = old_op.crop_disaster_ind,
        livestock_disaster_ind = old_op.livestock_disaster_ind,
        landlord_ind = old_op.landlord_ind,
        expenses = old_op.expenses,
        business_use_home_expense = old_op.business_use_home_expense,
        other_deductions = old_op.other_deductions,
        inventory_adjustments = old_op.inventory_adjustments,
        gross_income = old_op.gross_income,
        net_farm_income = old_op.net_farm_income,
        net_income_before_adj = old_op.net_income_before_adj,
        net_income_after_adj = old_op.net_income_after_adj,
        who_updated = in_user,
        when_updated = current_timestamp
    from farms.farm_farming_operations old_op
    where old_op.program_year_version_id = in_old_pyv_id
    and new_op.operation_number = old_op.operation_number
    and new_op.program_year_version_id = in_new_pyv_id
    and new_op.operation_number = any(in_op_nums_copy_old_data);


    prod_insurance_nums := '{}';
    for op_rec in op_curs
    loop

        prod_insurance_nums := array(
            select pi.production_insurance_number
            from farms.farm_production_insurances pi
            where pi.farming_operation_id = op_rec.old_op_id
        );

        call farms_calculator_pkg.update_pi(op_rec.new_op_id, prod_insurance_nums, in_user);

    end loop;

end;
$$;
