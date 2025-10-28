create or replace function farms_webapp_pkg.encrypt(
    in in_string text
) returns text
language plpgsql
as
$$
declare

    v_key text;
    v_padded bytea;
    v_encrypted bytea;
    v_return_string text;
    v_mod int;

begin
    if in_string is not null and in_string <> '' then
        -- Convert input to binary
        v_padded := in_string::bytea;

        -- Pad to a multiple of 8 bytes (DES/3DES block size)
        v_mod := length(v_padded) % 8;
        if v_mod <> 0 then
            v_padded := v_padded || repeat(e'\\000', 8 - v_mod)::bytea;
        end if;

        -- Get encryption key
        v_key := farms_webapp_pkg.get_encryption_key();

        -- Encrypt using 3DES
        v_encrypted := farms_webapp_pkg.encrypt(v_padded, v_key::bytea, '3des');

        -- Return base64 text
        v_return_string := encode(v_encrypted, 'base64');
    end if;

    return v_return_string;
end;
$$;
