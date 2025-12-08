create or replace procedure farms_enrolment_write_pkg.generate_non_enw_enrolment(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_create_task_in_barn_ind farms.farm_program_enrolments.create_task_in_barn_ind%type,
    in in_user farms.farm_program_enrolments.who_updated%type,
    in in_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type,
    in in_scenario_state farms.farm_agristability_scenarios.scenario_state_code%type,
    in in_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type,
    in in_participant_pin farms.farm_agristability_clients.participant_pin%type
)
language plpgsql
as
$$
declare

    cur_stag_rec farms.farm_zprogram_enrolments;

    minimum_fee bigint := 45;
    margins_not_found boolean;
    margins_have_zeroes boolean;
    scenario_count bigint;
    v_program_year farms.farm_program_years.year%type := in_enrolment_year - 2;

    margins_cursor cursor for
        select program_year,
               margin,
               use_margin_ind,
               -- only calculates the average if there are 5 years
               round((avg(case when use_margin_ind = 'Y' then margin end) over (order by margin))::numeric, 2) contribution_margin_average,
               combined_farm_percent
        from (
            select program_year,
                   margin,
                   combined_farm_percent,
                   case
                       when margin_count = 5
                            and margin_rank != margin_count -- 5 years - do not use the highest margin
                            and margin_rank != 1 -- 5 years - do not use the lowest margin
                            then 'Y'
                       else 'N'
                   end use_margin_ind -- fewer than 5 years - will use the 3 most recent years
            from (
                select program_year,
                       case
                           when coalesce(combined_farm_number::text, '') = '' then margin
                           else round((margin * combined_farm_percent)::numeric, 2)
                       end margin,
                       count(margin) over (order by margin) margin_count,
                       row_number() over (order by margin) margin_rank,
                       combined_farm_percent
                from (
                    select py.year program_year,
                           /* structural change is not calcuated for the program year, so we must use the accrual adjusted margin. */
                           case
                               when py.year = v_program_year then bct.production_marg_accr_adjs
                               else bct.production_marg_aft_str_changs
                           end margin,
                           psc.combined_farm_number,
                           case
                               when psc.combined_farm_number is not null
                                    and psc.combined_farm_number::text <> '' then cl.applied_benefit_percent
                           end combined_farm_percent
                    from (
                        select in_scenario_id agristability_scenario_id,
                               in_scenario_id parent_scenario_id
                        union all
                        select rs.agristability_scenario_id,
                               rs.for_agristability_scenario_id parent_scenario_id
                        from farms.farm_reference_scenarios rs
                        join farms.farm_scenarios_vw sv on sv.agristability_scenario_id = rs.agristability_scenario_id
                        where rs.for_agristability_scenario_id = in_scenario_id
                        and sv.year > in_enrolment_year - 7
                    ) sids
                    join farms.farm_agristability_scenarios s on s.agristability_scenario_id = sids.agristability_scenario_id
                    join farms.farm_program_year_versions pyv on pyv.program_year_version_id = s.program_year_version_id
                    join farms.farm_program_years py on py.program_year_id = pyv.program_year_id
                    join farms.farm_benefit_calc_totals bct on bct.agristability_scenario_id = s.agristability_scenario_id
                    join farms.farm_agristability_claims cl on cl.agristability_scenario_id = sids.parent_scenario_id
                    join farms.farm_agristability_scenarios psc on psc.agristability_scenario_id = sids.parent_scenario_id
                ) alias14
            ) alias15
        ) alias16;

begin

    --dbms_output.put_line('generating non-enw enrolment.');
    cur_stag_rec := null;
    cur_stag_rec.participant_pin := in_participant_pin;
    cur_stag_rec.enrolment_year := in_enrolment_year;
    cur_stag_rec.create_task_in_barn_ind := in_create_task_in_barn_ind;
    cur_stag_rec.error_ind := 'N';
    cur_stag_rec.margin_year_minus_2_ind := 'N';
    cur_stag_rec.margin_year_minus_3_ind := 'N';
    cur_stag_rec.margin_year_minus_4_ind := 'N';
    cur_stag_rec.margin_year_minus_5_ind := 'N';
    cur_stag_rec.margin_year_minus_6_ind := 'N';
    cur_stag_rec.generated_from_cra_ind := 'N';
    cur_stag_rec.generated_from_enw_ind := 'N';
    cur_stag_rec.revision_count := 1;
    cur_stag_rec.who_created := in_user;
    cur_stag_rec.who_updated := in_user;

    if in_scenario_category_code in ('FIN','PADJ','AADJ') and in_scenario_state in ('COMP','AMEND') then
        --dbms_output.put_line('found verified final scenario.');
        -- loop through the year margins
        for margin_rec in margins_cursor loop
            --dbms_output.put_line('margin loop year: ' || margin_rec.program_year);
            if margin_rec.program_year = in_enrolment_year - 2 then
                cur_stag_rec.contribution_margin_average := margin_rec.contribution_margin_average; -- not for a specific year, but only need to do this once.
                cur_stag_rec.combined_farm_percent := margin_rec.combined_farm_percent;
                cur_stag_rec.margin_year_minus_2 := margin_rec.margin;
                cur_stag_rec.margin_year_minus_2_ind := margin_rec.use_margin_ind;
            elsif margin_rec.program_year = in_enrolment_year - 3 then
                cur_stag_rec.margin_year_minus_3 := margin_rec.margin;
                cur_stag_rec.margin_year_minus_3_ind := margin_rec.use_margin_ind;
            elsif margin_rec.program_year = in_enrolment_year - 4 then
                cur_stag_rec.margin_year_minus_4 := margin_rec.margin;
                cur_stag_rec.margin_year_minus_4_ind := margin_rec.use_margin_ind;
            elsif margin_rec.program_year = in_enrolment_year - 5 then
                cur_stag_rec.margin_year_minus_5 := margin_rec.margin;
                cur_stag_rec.margin_year_minus_5_ind := margin_rec.use_margin_ind;
            elsif margin_rec.program_year = in_enrolment_year - 6 then
                cur_stag_rec.margin_year_minus_6 := margin_rec.margin;
                cur_stag_rec.margin_year_minus_6_ind := margin_rec.use_margin_ind;
            end if;

        end loop;
        -- end margins loop
    end if;

    margins_not_found := coalesce(cur_stag_rec.margin_year_minus_2::text, '') = ''
                         or coalesce(cur_stag_rec.margin_year_minus_3::text, '') = ''
                         or coalesce(cur_stag_rec.margin_year_minus_4::text, '') = '';

    margins_have_zeroes := cur_stag_rec.margin_year_minus_2 = 0
                           or cur_stag_rec.margin_year_minus_3 = 0
                           or cur_stag_rec.margin_year_minus_4 = 0;

    --dbms_output.put_line('margins_not_found: ' || case when margins_not_found then 'true' else 'false' end);
    --dbms_output.put_line('margins_have_zeroes: ' || case when margins_have_zeroes then 'true' else 'false' end);
    if margins_not_found or margins_have_zeroes then
        -- the contribution margin average was assigned above
        -- but if we are missing data it is not valid, so clear it.
        cur_stag_rec.contribution_margin_average := null;
    elsif coalesce(cur_stag_rec.contribution_margin_average::text, '') = '' then
        -- the query did not calculate the average because there were fewer than 5 margins.
        -- so we use the three most recent years.
        cur_stag_rec.margin_year_minus_2_ind := 'Y';
        cur_stag_rec.margin_year_minus_3_ind := 'Y';
        cur_stag_rec.margin_year_minus_4_ind := 'Y';
        cur_stag_rec.margin_year_minus_5_ind := 'N';
        cur_stag_rec.margin_year_minus_6_ind := 'N';

        cur_stag_rec.contribution_margin_average := (cur_stag_rec.margin_year_minus_2
                                                    + cur_stag_rec.margin_year_minus_3
                                                    + cur_stag_rec.margin_year_minus_4)
                                                    / 3;

    end if;


    if margins_not_found then
        -- check if scenarios exist to calculate from
        select count(*)
        into scenario_count
        from farms.farm_scenarios_vw sv
        where sv.participant_pin = cur_stag_rec.participant_pin
        and sv.year between in_enrolment_year - 4 and in_enrolment_year - 2
        and sv.scenario_number = 1;

        --dbms_output.put_line('scenario_count: ' || scenario_count);
        if scenario_count != 3 then
            -- missing one of year -2 to year -4
            cur_stag_rec.failed_to_generate_ind := 'Y';
            cur_stag_rec.failed_reason := 'Insufficient reference margin data for this participant.';
        else
            --dbms_output.put_line('found 3 years.');
            -- if the required years were found but not the margins then we will attempt to calculate the margins in java
            cur_stag_rec.failed_to_generate_ind := 'N';
        end if;
    elsif margins_have_zeroes then
        -- if we do have year -2 to year -4 then we must have zero or null margins for those years.
        -- this is for the very rare case that a verified scenario has a zero margin.
        cur_stag_rec.failed_to_generate_ind := 'Y';
        cur_stag_rec.failed_reason := 'must have non-zero margins for the three most recent years.';
    else
        cur_stag_rec.generated_date := current_timestamp;
        cur_stag_rec.failed_to_generate_ind := 'N';

        if in_enrolment_year >= 2013 then
            cur_stag_rec.enrolment_fee := (cur_stag_rec.contribution_margin_average * 0.0045 * 0.70);
        else
            cur_stag_rec.enrolment_fee := (cur_stag_rec.contribution_margin_average * 0.0045 * 0.85);
        end if;

        if cur_stag_rec.enrolment_fee < minimum_fee then
            cur_stag_rec.enrolment_fee := minimum_fee;
        end if;

        cur_stag_rec.agristability_scenario_id := in_scenario_id;
    end if;

    cur_stag_rec.when_created := current_timestamp;
    cur_stag_rec.when_updated := cur_stag_rec.when_created;

    insert into farms.farm_zprogram_enrolments
    values (cur_stag_rec.*);

end;
$$;
