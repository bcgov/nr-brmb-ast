create or replace function farms_error_pkg.codify_mun_code(
    in code varchar
)
returns varchar
language plpgsql
as $$
begin
    return 'The specified municipality code (' || code || ') was not found.';
end;
$$;
