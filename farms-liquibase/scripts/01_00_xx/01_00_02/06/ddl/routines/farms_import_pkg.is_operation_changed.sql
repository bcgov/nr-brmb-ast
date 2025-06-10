create or replace function farms_import_pkg.is_operation_changed(
    in in_program_year_id farms.program_year.program_year_id%type,
    in in_program_year_vrsn_prev_id farms.program_year_version.program_year_version_id%type
)
returns boolean
language plpgsql
as $$
declare
    cnt numeric;
begin
    -- want to know when something is different ... so missing in Z03 doesn't count as a difference but missing in op does count
    select count(*) cnt
    into cnt
    from (
        select op.federal_accounting_code,
               op.business_use_home_expense,
               op.crop_disaster_indicator,
               op.crop_share_indicator,
               op.expenses,
               op.feeder_member_indicator,
               op.fiscal_year_end,
               op.fiscal_year_start,
               op.gross_income,
               op.inventory_adjustments,
               op.landlord_indicator,
               op.livestock_disaster_indicator,
               op.net_farm_income,
               op.net_income_after_adjustments,
               op.net_income_before_adjustments,
               op.other_deductions,
               op.partnership_name,
               op.partnership_percent,
               op.partnership_pin,
               ac.participant_pin,
               op.operation_number,
               row_number() over (partition by op.federal_accounting_code,
               op.business_use_home_expense, op.crop_disaster_indicator, op.crop_share_indicator,
               op.expenses, op.feeder_member_indicator, op.fiscal_year_end, op.fiscal_year_start,
               op.gross_income, op.inventory_adjustments, op.landlord_indicator,
               op.livestock_disaster_indicator, op.net_farm_income, op.net_income_after_adjustments,
               op.net_income_before_adjustments, op.other_deductions, op.partnership_name,
               op.partnership_percent, op.partnership_pin, ac.participant_pin,
               op.operation_number order by 1) rn
        from farms.agristability_client ac
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
        join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
        join farms.farming_operation op on op.program_year_version_id = pyv.program_year_version_id
        where pyv.program_year_version_id = in_program_year_vrsn_prev_id
        and op.locally_generated_indicator = 'N'
        except
        select to_char(z.accounting_code) federal_accounting_code,
               z.business_use_of_home_expenses business_use_home_expense,
               z.crop_disaster_indicator,
               z.crop_share_indicator,
               z.expenses,
               z.feeder_member_indicator,
               to_date(z.fiscal_year_end, 'YYYYMMDD') fiscal_year_end,
               to_date(z.fiscal_year_start, 'YYYYMMDD') fiscal_year_start,
               z.gross_income,
               z.inventory_adjustments,
               z.landlord_indicator,
               z.livestock_disaster_indicator,
               z.net_farm_income,
               z.net_income_after_adjustments,
               z.net_income_before_adjustments,
               z.other_deductions,
               z.partnership_name,
               z.partnership_percent,
               z.partnership_pin,
               z.participant_pin,
               z.operation_number,
               row_number() over (partition by to_char(z.accounting_code),
               z.business_use_of_home_expenses, z.crop_disaster_indicator, z.crop_share_indicator,
               z.expenses, z.feeder_member_indicator, to_date(z.fiscal_year_end, 'YYYYMMDD'),
               to_date(z.fiscal_year_start, 'YYYYMMDD'), z.gross_income,
               z.inventory_adjustments, z.landlord_indicator, z.livestock_disaster_indicator,
               z.net_farm_income, z.net_income_after_adjustments, z.net_income_before_adjustments,
               z.other_deductions, z.partnership_name, z.partnership_percent,
               z.partnership_pin, z.participant_pin, z.operation_number order by 1) rn
        from farms.z03_statement_information z
        join farms.agristability_client ac on z.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and z.program_year = py.year
        where py.program_year_id = in_program_year_id
    ) t1
    union all
    (
        select to_char(z.accounting_code) federal_accounting_code,
               z.business_use_of_home_expenses business_use_home_expense,
               z.crop_disaster_indicator,
               z.crop_share_indicator,
               z.expenses,
               z.feeder_member_indicator,
               to_date(z.fiscal_year_end, 'YYYYMMDD') fiscal_year_end,
               to_date(z.fiscal_year_start, 'YYYYMMDD') fiscal_year_start,
               z.gross_income,
               z.inventory_adjustments,
               z.landlord_indicator,
               z.livestock_disaster_indicator,
               z.net_farm_income,
               z.net_income_after_adjustments,
               z.net_income_before_adjustments,
               z.other_deductions,
               z.partnership_name,
               z.partnership_percent,
               z.partnership_pin,
               z.participant_pin,
               z.operation_number,
               row_number() over (partition by to_char(z.accounting_code),
               z.business_use_of_home_expenses, z.crop_disaster_indicator, z.crop_share_indicator,
               z.expenses, z.feeder_member_indicator, to_date(z.fiscal_year_end, 'YYYYMMDD'),
               to_date(z.fiscal_year_start, 'YYYYMMDD'), z.gross_income,
               z.inventory_adjustments, z.landlord_indicator, z.livestock_disaster_indicator,
               z.net_farm_income, z.net_income_after_adjustments, z.net_income_before_adjustments,
               z.other_deductions, z.partnership_name, z.partnership_percent,
               z.partnership_pin, z.participant_pin, z.operation_number order by 1) rn
        from farms.z03_statement_information z
        join farms.agristability_client ac on z.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and z.program_year = py.year
        where py.program_year_id = in_program_year_id
        except
        select op.federal_accounting_code,
               op.business_use_home_expense,
               op.crop_disaster_indicator,
               op.crop_share_indicator,
               op.expenses,
               op.feeder_member_indicator,
               op.fiscal_year_end,
               op.fiscal_year_start,
               op.gross_income,
               op.inventory_adjustments,
               op.landlord_indicator,
               op.livestock_disaster_indicator,
               op.net_farm_income,
               op.net_income_after_adjustments,
               op.net_income_before_adjustments,
               op.other_deductions,
               op.partnership_name,
               op.partnership_percent,
               op.partnership_pin,
               ac.participant_pin,
               op.operation_number,
               row_number() over (partition by op.federal_accounting_code,
               op.business_use_home_expense, op.crop_disaster_indicator, op.crop_share_indicator,
               op.expenses, op.feeder_member_indicator, op.fiscal_year_end, op.fiscal_year_start,
               op.gross_income, op.inventory_adjustments, op.landlord_indicator,
               op.livestock_disaster_indicator, op.net_farm_income, op.net_income_after_adjustments,
               op.net_income_before_adjustments, op.other_deductions, op.partnership_name,
               op.partnership_percent, op.partnership_pin, ac.participant_pin,
               op.operation_number order by 1) rn
        from farms.agristability_client ac
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
        join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
        join farms.farming_operation op on op.program_year_version_id = pyv.program_year_version_id
        where pyv.program_year_version_id = in_program_year_vrsn_prev_id
        and op.locally_generated_indicator = 'N'
    );

    if cnt > 0 then
        return true;
    end if;

    -- want to know when something is different ...
    -- we've already check the operation so assume it exists
    -- don't can about missing imports so join to z05
    -- want to detect missing partners so outer join
    select count(*) cnt
    into cnt
    from (
        select prt.partner_sin,
               prt.first_name,
               prt.last_name,
               prt.corp_name,
               prt.partner_percent,
               prt.partnership_pin,
               row_number() over (partition by prt.partner_sin, prt.first_name, prt.last_name,
               prt.corp_name, prt.partner_percent, prt.partnership_pin order by 1) rn
        from farms.agristability_client ac
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
        join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
        join farms.farming_operation op on op.program_year_version_id = pyv.program_year_version_id
        join farms.farming_operation_partner prt on op.farming_operation_id = prt.farming_operation_id
        where pyv.program_year_version_id = in_program_year_vrsn_prev_id
        except
        select z.partner_sin_ctn_business_number partner_sin,
               z.partner_first_name first_name,
               z.partner_last_name last_name,
               z.partner_corp_name corp_name,
               z.partner_percent,
               z.partner_pin,
               row_number() over (partition by z.partner_sin_ctn_business_number, z.partner_first_name,
               z.partner_last_name, z.partner_corp_name, z.partner_percent, z.partner_pin
               order by 1) rn
        from farms.z05_partner_information z
        join farms.agristability_client ac on ac.participant_pin = z.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and py.year = z.program_year
        where py.program_year_id = in_program_year_id
    ) t2
    union all
    (
        select z.partner_sin_ctn_business_number partner_sin,
               z.partner_first_name first_name,
               z.partner_last_name last_name,
               z.partner_corp_name corp_name,
               z.partner_percent,
               z.partner_pin,
               row_number() over (partition by z.partner_sin_ctn_business_number, z.partner_first_name,
               z.partner_last_name, z.partner_corp_name, z.partner_percent, z.partner_pin
               order by 1) rn
        from farms.z05_partner_information z
        join farms.agristability_client ac on ac.participant_pin = z.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and py.year = z.program_year
        where py.program_year_id = in_program_year_id
        except
        select prt.partner_sin,
               prt.first_name,
               prt.last_name,
               prt.corp_name,
               prt.partner_percent,
               prt.partnership_pin,
               row_number() over (partition by prt.partner_sin, prt.first_name, prt.last_name,
               prt.corp_name, prt.partner_percent, prt.partnership_pin order by 1) rn
        from farms.agristability_client ac
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
        join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
        join farms.farming_operation op on op.program_year_version_id = pyv.program_year_version_id
        join farms.farming_operation_partner prt on op.farming_operation_id = prt.farming_operation_id
        where pyv.program_year_version_id = in_program_year_vrsn_prev_id
    );

    if cnt > 0 then
        return true;
    end if;

    -- want to know when something is different ...
    -- we've already check the operation so assume it exists
    -- don't can about missing imports so join to z05
    -- want to detect missing partners so outer join
    select count(*) cnt
    into cnt
    from (
        select op.operation_number,
               pi.production_insurance_number
        from farms.agristability_client ac
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
        join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
        join farms.farming_operation op on op.program_year_version_id = pyv.program_year_version_id
        join farms.production_insurance pi on pi.farming_operation_id = op.farming_operation_id
        where pyv.program_year_version_id = in_program_year_vrsn_prev_id
        except
        select z.operation_number,
               z.production_insurance_number
        from farms.z22_production_insurance z
        join farms.agristability_client ac on ac.participant_pin = z.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and py.year = z.program_year
        where py.program_year_id = in_program_year_id
    ) t3
    union all
    (
        select z.operation_number,
               z.production_insurance_number
        from farms.z22_production_insurance z
        join farms.agristability_client ac on ac.participant_pin = z.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and py.year = z.program_year
        where py.program_year_id = in_program_year_id
        except
        select op.operation_number,
               pi.production_insurance_number
        from farms.agristability_client ac
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
        join farms.program_year_version pyv on pyv.program_year_id = py.program_year_id
        join farms.farming_operation op on op.program_year_version_id = pyv.program_year_version_id
        join farms.production_insurance pi on op.farming_operation_id = pi.farming_operation_id
        where pyv.program_year_version_id = in_program_year_vrsn_prev_id
    );

    if cnt > 0 then
        return true;
    end if;
    return false;
end;
$$;
