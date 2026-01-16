create or replace function farms_export_pkg.f26_ade()
returns refcursor
language plpgsql
as
$$
declare

    cur refcursor;

begin

    open cur for
        select sgc.structure_group_code,
               sgc.description
        from farms.farm_structure_group_codes sgc
        where sgc.expiry_date > current_timestamp
        or coalesce(sgc.expiry_date::text, '') = '';

    return cur;
end;
$$;
