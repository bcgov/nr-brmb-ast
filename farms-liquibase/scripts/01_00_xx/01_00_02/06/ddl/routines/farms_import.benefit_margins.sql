create or replace function farms_import_pkg.benefit_margins(
    in in_program_year_id farms.program_year.program_year_id%type,
    in in_sc_id farms.agristability_scenario.agristability_scenario_id%type,
    in in_user varchar
)
returns varchar
language plpgsql
as $$
declare
    bm_insert_cursor cursor for
        select z50.unadjusted_reference_margin unadjusted_reference_margin,
               z50.program_margin production_marg_accr_adjs,
               z50.adjusted_reference_margin production_marg_aft_str_changs,
               z50.structure_change_adjustment_amount structural_change_adjs
        from farms.z50_participant_benefit_calculation z50
        join farms.agristability_client ac on z50.participant_pin = ac.participant_pin
        join farms.program_year py on ac.agristability_client_id = py.agristability_client_id
                                   and py.year = z50.program_year
        where py.program_year_id = in_program_year_id;
    bm_insert_val record;

    c_id numeric;
begin

    for bm_insert_val in bm_insert_cursor
    loop

        select nextval('farms.seq_bct')
        into c_id;

        insert into farms.benefit_calculation_total (
            benefit_calculation_total_id,
            unadjusted_production_margin,
            production_margin_accrual_adjustments,
            production_margin_after_structure_changes,
            structural_change_adjustments,
            agristability_scenario_id,
            revision_count,
            create_user,
            create_date,
            update_user,
            update_date
        ) values (
            c_id,
            bm_insert_val.unadjusted_reference_margin,
            bm_insert_val.production_marg_accr_adjs,
            bm_insert_val.production_marg_aft_str_changs,
            bm_insert_val.structural_change_adjs,
            in_sc_id,
            1,
            in_user,
            current_timestamp,
            in_user,
            current_timestamp
        );

    end loop;

    return null;
exception
    when others then
        call farms_import_pkg.scrub(farms_error_pkg.codify_benefit_margins(
            sqlerrm,
            in_sc_id
        ));
end;
$$;
