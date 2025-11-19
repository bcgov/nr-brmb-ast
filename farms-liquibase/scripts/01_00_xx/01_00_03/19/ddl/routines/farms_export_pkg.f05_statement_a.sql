create or replace function farms_export_pkg.f05_statement_a(
    in in_program_year farms.farm_program_years.year%type
) returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select part.farming_operatin_prtnr_id partner_oid,
               sta.participant_pin participant_oid,
               sta.year stab_yy,
               fo.operation_number incm_expn_stmt_num,
               part.first_name prtnr_gname,
               part.last_name prtnr_sname,
               part.corp_name prtnr_corp_name,
               null prtnr_sin,
               null prnr_sbrn,
               part.partner_percent prtnr_pct,
               part.partnership_pin prtnr_pin
        from farms.farm_chef_statement_a_years_vw sta
        join farms.farm_farming_operations fo on fo.program_year_version_id = sta.program_year_version_id
        join farms.farm_farming_operatin_prtnrs part on part.farming_operation_id = fo.farming_operation_id
        where sta.year = in_program_year
        order by participant_oid,
                 stab_yy,
                 incm_expn_stmt_num,
                 prtnr_pin;

    return cur;
end;
$$;
