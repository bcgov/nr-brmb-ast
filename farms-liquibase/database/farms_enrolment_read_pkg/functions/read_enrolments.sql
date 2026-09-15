create or replace function farms_enrolment_read_pkg.read_enrolments(
    in in_enrolment_year farms.farm_program_enrolments.enrolment_year%type,
    in in_regional_office_code farms.farm_regional_office_codes.regional_office_code%type
)
returns table(
    agristability_client_id     farms.farm_scenarios_vw.agristability_client_id%type,
    participant_pin             farms.farm_scenarios_vw.participant_pin%type,
    producer_name               farms.farm_persons.corp_name%type,
    scenario_state              varchar,
    failed_to_generate_ind      farms.farm_program_enrolments.failed_to_generate_ind%type,
    failed_reason               farms.farm_program_enrolments.failed_reason%type,
    program_enrolment_id        farms.farm_program_enrolments.program_enrolment_id%type,
    enrolment_year              farms.farm_program_enrolments.enrolment_year%type,
    enrolment_fee               farms.farm_program_enrolments.enrolment_fee%type,
    generated_date              farms.farm_program_enrolments.generated_date%type,
    generated_from_cra_ind      farms.farm_program_enrolments.generated_from_cra_ind%type,
    generated_from_enw_ind      farms.farm_program_enrolments.generated_from_enw_ind%type,
    combined_farm_percent       farms.farm_program_enrolments.combined_farm_percent%type,
    when_updated                farms.farm_program_enrolments.when_updated%type,
    enrolment_revision_count    farms.farm_program_enrolments.revision_count%type
)
language plpgsql
as $$
declare

    v_enrol_year smallint := in_enrolment_year::smallint;
    v_y_m2       smallint := (in_enrolment_year - 2)::smallint;
    v_y_m3       smallint := (in_enrolment_year - 3)::smallint;

begin

    /* Every column reference below is qualified with a table alias on purpose. The
     * RETURNS TABLE columns are PL/pgSQL variables inside this body, and most of them
     * share a name with a column of farm_program_enrolments, so an unqualified
     * reference would be rejected as ambiguous.
     */

    /* Belt and braces: the temporary table is ON COMMIT DROP and the screen calls this
     * function once per transaction, so this is a no-op today. It keeps the function
     * callable twice in one transaction rather than failing on the second call.
     */
    drop table if exists tmp_read_enrolments_best;

    /* One row per client, holding the single scenario that decides that client's
     * displayed state.
     *
     * The previous version built this over every program year with year <= y_m2 - the
     * client's whole history - and sorted the lot to take DISTINCT ON (client). That
     * sort was the bulk of the screen's load time, and almost all of it was wasted:
     * the priority buckets that produce a state other than 'REC' (pri 1, 2 and 3) only
     * ever match a scenario in the two-year window y_m3..y_m2, and DISTINCT ON orders
     * by pri first, so a scenario outside that window can only win when the client has
     * nothing inside it. Ordering within the losing bucket therefore cannot change the
     * output - every row in it yields 'REC' - so only the window is ranked here, and
     * the clients with nothing in it are added below as a semi-join.
     */
    create temporary table tmp_read_enrolments_best on commit drop as
    with cand as (
        select py.agristability_client_id,
               py.year,
               sc.scenario_state_code,
               sc.scenario_category_code,
               sc.scenario_class_code,
               sc.scenario_number,
               case
                 when py.year between v_y_m3 and v_y_m2
                  and sc.scenario_state_code in ('COMP','AMEND')
                  and sc.scenario_category_code = 'FIN'
                  and sc.scenario_class_code    = 'USER'
                   then 1
                 when py.year = v_y_m2
                  and sc.scenario_state_code = 'EN_COMP'
                  and sc.scenario_category_code = 'ENW'
                   then 2
                 when py.year = v_y_m2
                  and sc.scenario_state_code = 'IP'
                  and sc.scenario_category_code = 'ENW'
                   then 3
                 else 4
               end as pri,
               case when sc.scenario_state_code = 'REC' then 0 else 1 end as rec_first
        from farms.farm_program_years           py
        join farms.farm_program_year_versions   pyv on pyv.program_year_id = py.program_year_id
        join farms.farm_agristability_scenarios sc  on sc.program_year_version_id = pyv.program_year_version_id
        where py.year between v_y_m3 and v_y_m2
          and (
               in_regional_office_code = 'ALL'
               or exists (
                    select 1
                    from farms.farm_office_municipality_xref omx
                    where omx.municipality_code    = pyv.municipality_code
                      and omx.regional_office_code = in_regional_office_code
               )
          )
    )
    select distinct on (c.agristability_client_id)
           c.agristability_client_id,
           c.year,
           c.scenario_state_code,
           c.scenario_category_code,
           c.scenario_class_code
    from cand c
    order by c.agristability_client_id, c.pri, c.year desc, c.rec_first, c.scenario_number desc;

    create index on tmp_read_enrolments_best (agristability_client_id);

    /* Clients whose only scenarios sit before the window. They were previously carried
     * by the full-history sort, and they are a large share of the population - roughly
     * half of the rows on this screen - so this step has to stay cheap.
     *
     * The null columns fall through the CASE below to 'REC', which is the state the
     * full-history sort always gave these clients.
     *
     * The two branches exist because "is there an older scenario" and "is there an
     * older scenario in this office" want opposite plans, and writing them as one
     * statement - with the region test as an OR against the parameter - gets the
     * ALL-shaped plan for both. The region code cannot be pushed into a join either,
     * because farm_program_year_versions.municipality_code is nullable and those rows
     * still count under 'ALL'.
     */
    if in_regional_office_code = 'ALL' then

        /* No region to filter on, so ask each client the question directly, anti-joined
         * against the rows already inserted: a few thousand clients, each stopping at
         * its first old scenario. Driving this from farm_program_years instead reads far
         * more than it needs to - the planner pulls the EXISTS up into a semi-join and
         * satisfies it by hash joining all million-odd scenarios to every program year
         * version, which is slower than the full-history sort it replaced.
         */
        insert into tmp_read_enrolments_best (
            agristability_client_id,
            year,
            scenario_state_code,
            scenario_category_code,
            scenario_class_code
        )
        select ac.agristability_client_id,
               null::smallint,
               null::varchar(10),
               null::varchar(10),
               null::varchar(10)
        from farms.farm_agristability_clients ac
        where not exists (
                select 1
                from tmp_read_enrolments_best t
                where t.agristability_client_id = ac.agristability_client_id
          )
          and exists (
                select 1
                from farms.farm_program_years           py
                join farms.farm_program_year_versions   pyv on pyv.program_year_id = py.program_year_id
                join farms.farm_agristability_scenarios sc  on sc.program_year_version_id = pyv.program_year_version_id
                where py.agristability_client_id = ac.agristability_client_id
                  and py.year < v_y_m3
          );

    else

        /* Asking each client the same question with a region attached is the worst case
         * for that shape: most clients are not in the region, and proving that walks
         * their whole history because there is no match to stop at. Driven from the
         * xref instead - a small lookup table, only a few rows for any one office - the
         * municipality index on farm_program_year_versions only ever touches versions
         * that do match.
         */
        insert into tmp_read_enrolments_best (
            agristability_client_id,
            year,
            scenario_state_code,
            scenario_category_code,
            scenario_class_code
        )
        select distinct py.agristability_client_id,
               null::smallint,
               null::varchar(10),
               null::varchar(10),
               null::varchar(10)
        from farms.farm_office_municipality_xref omx
        join farms.farm_program_year_versions pyv on pyv.municipality_code = omx.municipality_code
        join farms.farm_program_years         py  on py.program_year_id = pyv.program_year_id
        where omx.regional_office_code = in_regional_office_code
          and py.year < v_y_m3
          and exists (
                select 1
                from farms.farm_agristability_scenarios sc
                where sc.program_year_version_id = pyv.program_year_version_id
          )
          and not exists (
                select 1
                from tmp_read_enrolments_best t
                where t.agristability_client_id = py.agristability_client_id
          );

    end if;

    analyze tmp_read_enrolments_best;

    /* The y_m3 branch used to be a CTE listing every client whose latest y_m2 base
     * scenario has unassigned reported income/expense rows, hashed and probed with IN.
     * Building it meant walking farm_reported_income_expenses for the whole population
     * to answer a question that only the handful of clients reaching that branch ask.
     * As a correlated EXISTS it is a SubPlan, so it runs only for those clients, and
     * stops at the first matching row rather than collecting all of them.
     */
    return query
    select b.agristability_client_id,
           ac.participant_pin,
           coalesce(o.corp_name, o.last_name || ', ' || o.first_name) as producer_name,
           (case
              when b.year = v_y_m2
               and b.scenario_state_code in ('COMP','AMEND')
               and b.scenario_category_code = 'FIN'
               and b.scenario_class_code    = 'USER'
                then 'COMP'
              when b.year = v_y_m3
               and b.scenario_state_code in ('COMP','AMEND')
               and b.scenario_category_code = 'FIN'
               and b.scenario_class_code    = 'USER'
               and exists (
                     select 1
                     from farms.farm_farming_operations fo
                     join farms.farm_reported_income_expenses rie
                       on rie.farming_operation_id = fo.farming_operation_id
                      and rie.agristability_scenario_id is null
                     where fo.program_year_version_id = (
                         select sc2.program_year_version_id
                         from farms.farm_program_years           py2
                         join farms.farm_program_year_versions   pyv2 on pyv2.program_year_id = py2.program_year_id
                         join farms.farm_agristability_scenarios sc2  on sc2.program_year_version_id = pyv2.program_year_version_id
                         where py2.agristability_client_id = b.agristability_client_id
                           and py2.year = v_y_m2
                           and sc2.scenario_class_code in ('CRA','CHEF','LOCAL','GEN')
                         order by sc2.scenario_number desc
                         limit 1
                     )
               )
                then 'COMP'
              when b.year = v_y_m2
               and b.scenario_state_code = 'EN_COMP'
               and b.scenario_category_code = 'ENW'
                then 'EN_COMP'
              when b.year = v_y_m2
               and b.scenario_state_code = 'IP'
               and b.scenario_category_code = 'ENW'
                then 'EN_IP'
              else 'REC'
            end)::varchar as scenario_state,
           pe.failed_to_generate_ind,
           pe.failed_reason,
           pe.program_enrolment_id,
           pe.enrolment_year,
           pe.enrolment_fee,
           pe.generated_date,
           pe.generated_from_cra_ind,
           pe.generated_from_enw_ind,
           pe.combined_farm_percent,
           pe.when_updated,
           pe.revision_count as enrolment_revision_count
    from tmp_read_enrolments_best b
    join farms.farm_agristability_clients ac on ac.agristability_client_id = b.agristability_client_id
    join farms.farm_persons o on o.person_id = ac.person_id
    left join farms.farm_program_enrolments pe
      on pe.agristability_client_id = b.agristability_client_id
     and pe.enrolment_year = v_enrol_year;

end;
$$;
