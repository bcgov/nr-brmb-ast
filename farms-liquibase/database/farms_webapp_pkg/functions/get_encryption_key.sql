create or replace function farms_webapp_pkg.get_encryption_key()
returns varchar
language plpgsql
as
$$
begin
    return '8bytekey';
end;
$$;
