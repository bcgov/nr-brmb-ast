create or replace procedure farms_calculator_pkg.delete_partner(
    in in_farming_operatin_prtnr_id farms.farm_farming_operatin_prtnrs.farming_operatin_prtnr_id%type,
    in in_revision_count farms.farm_farming_operatin_prtnrs.revision_count%type
)
language plpgsql
as
$$
declare
    ora2pg_rowcount int;
begin

    delete from farms.farm_farming_operatin_prtnrs fop
    where fop.farming_operatin_prtnr_id = in_farming_operatin_prtnr_id
    and fop.revision_count = in_revision_count;

    get diagnostics ora2pg_rowcount = row_count;
    if ora2pg_rowcount <> 1 then
        raise exception '%', farms_types_pkg.invalid_revision_count_msg()
        using errcode = farms_types_pkg.invalid_revision_count_code()::text;
    end if;

end;
$$;
