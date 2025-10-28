create or replace function farms_webapp_pkg.decrypt(
    in in_string text
) returns text
language plpgsql
as
$$
declare

    v_encrypted bytea;
    v_decrypted bytea;
    v_clean bytea;
    v_key text;
    v_return_string text := null;

begin
    if in_string is not null and in_string <> '' then
        -- Decode from base64 to binary
        v_encrypted := decode(in_string, 'base64');

        -- Get encryption key
        v_key := farms_webapp_pkg.get_encryption_key();

        -- Decrypt (same algorithm as encrypt)
        v_decrypted := farms_webapp_pkg.decrypt(v_encrypted, v_key::bytea, '3des');

        -- Remove null bytes *at the binary level*
        v_clean := regexp_replace(encode(v_decrypted, 'escape'), '\\\\000', '', 'g')::bytea;

        -- Now safely convert to text
        v_return_string := rtrim(convert_from(v_clean, 'LATIN1'));
    end if;

    return v_return_string;
end;
$$;
