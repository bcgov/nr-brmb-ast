create or replace function farms_enrolment_write_pkg.generate_enrolments(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_create_task_in_barn_ind farms.farm_program_enrolments.create_task_in_barn_ind%type,
    in in_user farms.farm_program_enrolments.who_updated%type,
    in in_pins numeric[]
) returns refcursor
language plpgsql
as
$$
declare

    return_cur refcursor;

    staging_rec farms.farm_zprogram_enrolments;

    pin_cursor cursor(in_pins numeric[]) for
        select ac.participant_pin
        from farms.farm_agristability_clients ac
        where ac.participant_pin = any(in_pins);

    v_scenario_id farms.farm_agristability_scenarios.agristability_scenario_id%type;

    v_scenario_state farms.farm_agristability_scenarios.scenario_state_code%type;
    v_scenario_category_code farms.farm_agristability_scenarios.scenario_category_code%type;

begin

    delete from farms.farm_zprogram_enrolments;

    -- loop through the pins
    for pin_rec in pin_cursor(in_pins)
    loop

        staging_rec := null;
        v_scenario_id := farms_enrolment_write_pkg.get_scenario_id(in_enrolment_year, pin_rec.participant_pin);

        if coalesce(v_scenario_id::text, '') = '' then
            insert into farms.farm_zprogram_enrolments (
                participant_pin,
                enrolment_year,
                failed_to_generate_ind,
                failed_reason,
                who_created
            ) values (
                pin_rec.participant_pin,
                in_enrolment_year,
                'Y',
                'Insufficient reference margin data for this participant.',
                in_user
            );
        else

            select sc.scenario_state_code,
                   sc.scenario_category_code
            into v_scenario_state,
                 v_scenario_category_code
            from farms.farm_agristability_scenarios sc
            where sc.agristability_scenario_id = v_scenario_id;

            /* the enrol algorithm for read_py_id may find in progress or enrolment notice complete
             * scenarios with a scenario category code of ENW (enrolment notice workflow).
             * ENW scnearios should be skipped and not returned by this function because the enrolment notice
             * will be generated when the ENW  scenario is set to enrolment notice complete state.
             */
            if v_scenario_category_code = 'ENW' and v_scenario_state in ('IP','EN_COMP') then
                --dbms_output.put_line('found enw. skipping.');
                null; -- skip and do not return (as the list of pins to attempt calculation in java)
            else
                call farms_enrolment_write_pkg.generate_non_enw_enrolment(
                    in_enrolment_year,
                    in_create_task_in_barn_ind,
                    in_user,
                    v_scenario_id,
                    v_scenario_state,
                    v_scenario_category_code,
                    pin_rec.participant_pin
                );
            end if;
        end if;

    end loop;

    open return_cur for
        select z.participant_pin
        from farms.farm_zprogram_enrolments z
        where z.failed_to_generate_ind = 'N'
        and coalesce(z.enrolment_fee::text, '') = '';

    return return_cur;

end;
$$;
