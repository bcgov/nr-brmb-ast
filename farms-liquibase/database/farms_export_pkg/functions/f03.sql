create or replace function farms_export_pkg.f03(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    /* These have to be SET LOCAL statements in the body, not a SET clause on the
     * function. A refcursor's query is not executed when the function runs - it is
     * executed later, when the caller issues FETCH - and a SET clause attached to a
     * function is reverted the moment the function returns, taking any SET LOCAL made
     * inside it along with it. Set this way, with no SET clause on the function, they
     * last until the caller's transaction ends, so they cover the CREATE TABLE AS
     * below, the planning of the cursor's query, and the FETCH that executes it.
     * Planning under one work_mem and executing under another is what has to be
     * avoided: the planner sizes hash joins for the memory it was told about, and the
     * executor then has to spill them into a far larger number of batches.
     *
     * Parallelism is disabled because CREATE TABLE AS, unlike the cursor's query, is
     * parallelised, and each worker gets its own work_mem for every hash and sort node.
     */
    set local work_mem to '32MB';
    set local max_parallel_workers_per_gather to 0;

    /* The ranked/chosen subquery is materialized into a temporary table rather than
     * left inline in the cursor's query, so that the three window sorts over it happen
     * once, here, leaving the cursor a plain join against an analyzed table.
     *
     * The row filters that used to sit in the join's ON clause and the outer WHERE are
     * applied here too. They only ever referenced this subquery, and it is an inner
     * join, so moving them changes no results - but the temporary table then holds
     * only usable rows.
     */
    create temporary table tmp_f03_ranked on commit drop as
        with ranked as (
            /* this subquery is duplicated in F01, F02, F03, F20, F21, F30, F31, F40, F60.
             * if it is modified here, it should be modified there as well.
             */
            select m.program_year_version_id,
                   sc.agristability_scenario_id,
                   m.program_year_id,
                   m.participant_pin,
                   m.year program_year,
                   sc.combined_farm_number,
                   ssa.scenario_state_audit_id,
                   dense_rank() over(
                       partition by m.program_year_id
                       order by case when(sc.scenario_state_code = 'COMP') then 0 else 1 end,
                                case when(sc.scenario_state_code = 'AMEND') then 0 else 1 end,
                                case when(sc.scenario_class_code in ('CRA','LOCAL')) then 0 else 1 end,
                                case when(sc.scenario_category_code in ('FIN','AADJ','PADJ')) then 0 else 1 end,
                                case when(sc.scenario_category_code = 'INT') then 0 else 1 end,
                                case
                                    when sc.scenario_state_code in ('COMP', 'AMEND')
                                         and sc.scenario_class_code = 'USER'
                                         and m.year = in_program_year then 3
                                    when sc.scenario_state_code in ('COMP', 'AMEND')
                                         and sc.scenario_class_code = 'REF'
                                         and m_parent.year = in_program_year then 2
                                    when sc.scenario_class_code in ('CRA','LOCAL') then 1
                                    else 0
                                end desc,
                                ssa.when_created desc,
                                sc.scenario_number desc
                   ) as rnk,
                   first_value(ssa.scenario_state_audit_id) over (
                       partition by sc.agristability_scenario_id
                       order by ssa.when_created desc nulls last
                   ) verified_state_id,
                   first_value(py.non_participant_ind) over (
                       partition by m.agristability_client_id
                       order by m.year desc
                   ) non_participant_ind
            from farms.farm_agri_scenarios_vw m
            join farms.farm_agristability_scenarios sc on m.agristability_scenario_id = sc.agristability_scenario_id
            join farms.farm_program_years py on py.program_year_id = m.program_year_id
            left outer join farms.farm_reference_scenarios rf on rf.agristability_scenario_id = sc.agristability_scenario_id
            left outer join farms.farm_agri_scenarios_vw m_parent on m_parent.agristability_scenario_id = rf.for_agristability_scenario_id
            left outer join farms.farm_scenario_state_audits ssa on ssa.agristability_scenario_id = sc.agristability_scenario_id
                                                                 and ssa.scenario_state_code = 'COMP'
            where m.year between(in_program_year - 5) and in_program_year
        ), chosen as (
            select distinct on (program_year_id)
                   program_year_id,
                   min(agristability_scenario_id) as latest_sc_id
            from ranked
            where rnk = 1
            group by program_year_id
        )
        select r.*
        from ranked r
        join chosen c on r.program_year_id = c.program_year_id
                      and r.agristability_scenario_id = c.latest_sc_id
        where (r.scenario_state_audit_id = r.verified_state_id or coalesce(r.verified_state_id::text, '') = '')
          and r.non_participant_ind = 'N';

    create index on tmp_f03_ranked (program_year_version_id);
    analyze tmp_f03_ranked;

    open cur for
        select ac.participant_pin,
               in_program_year "Year",
               py.year prior_year,
               op.operation_number,
               rie.reported_income_expense_id,
               rie.line_item,
               case
                   when upper(rie.expense_ind) = 'Y' then 'E'
                   when upper(rie.expense_ind) = 'N' then 'I'
               end expense_ind,
               rie.amount
        from farms.farm_agristability_clients ac
        join farms.farm_program_years py on py.agristability_client_id = ac.agristability_client_id
        join farms.farm_program_year_versions pyv on pyv.program_year_id = py.program_year_id
        join tmp_f03_ranked t on pyv.program_year_version_id = t.program_year_version_id
        join farms.farm_farming_operations op on op.program_year_version_id = t.program_year_version_id
        join farms.farm_reported_income_expenses rie on rie.farming_operation_id = op.farming_operation_id
                                                     and (
                                                         coalesce(rie.agristability_scenario_id::text, '') = ''
                                                         or rie.agristability_scenario_id = t.agristability_scenario_id
                                                     )
        order by ac.participant_pin,
                 prior_year,
                 op.operation_number,
                 rie.line_item,
                 rie.reported_income_expense_id;

    return cur;
end;
$$;
