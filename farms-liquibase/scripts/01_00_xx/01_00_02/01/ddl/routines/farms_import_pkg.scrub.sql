create or replace function farms_import_pkg.scrub(
    in in_str varchar
)
returns varchar
language plpgsql
as $$
declare
    r varchar;
begin
    r := replace(in_str, '&', '&amp;');
    r := replace(r, '<', '&lt;');
    r := replace(r, '>', '&gt;');
    r := replace(r, '"', '&quot;');
    r := replace(r, '''', '&apos;');
    r := replace(r, chr(50057), '&#201;');
    r := replace(r, chr(50089), '&#233;');
    return r;
end;
$$;
