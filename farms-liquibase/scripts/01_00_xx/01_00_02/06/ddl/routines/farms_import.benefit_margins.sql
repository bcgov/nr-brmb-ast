create or replace function farms_import_pkg.benefit_margins(
    in in_program_year_id farms.farm_program_years.program_year_id%type,
    in in_sc_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
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
               z50.structure_change_adj_amount structural_change_adjs
        from farms.farm_z50_participnt_bnft_calcs z50
        join farms.farm_agristability_clients ac on z50.participant_pin = ac.participant_pin
        join farms.farm_program_years py on ac.agristability_client_id = py.agristability_client_id
                                   and py.year = z50.program_year
        where py.program_year_id = in_program_year_id;
    bm_insert_val record;

    c_id numeric;
begin

    for bm_insert_val in bm_insert_cursor
    loop

        select nextval('farms.seq_bct')
        into c_id;

        insert into farms.farm_benefit_calc_totals (
            benefit_calc_total_id,
            unadjusted_production_margin,
            production_marg_accr_adjs,
            production_marg_aft_str_changs,
            structural_change_adjs,
            agristability_scenario_id,
            revision_count,
            who_created,
            when_created,
            who_updated,
            when_updated
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
